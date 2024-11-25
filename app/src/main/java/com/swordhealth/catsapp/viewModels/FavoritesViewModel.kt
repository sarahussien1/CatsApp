package com.swordhealth.catsapp.viewModels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swordhealth.catsapp.R
import com.swordhealth.catsapp.models.AddToFavRequest
import com.swordhealth.catsapp.repositories.favoriteItem.FavoriteRepositoryContract
import com.swordhealth.catsapp.ui.models.CatUI
import com.swordhealth.catsapp.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val repository: FavoriteRepositoryContract,
    @ApplicationContext private val context: Context
) : ViewModel() {
    private val _toggleFavoriteFailure = MutableSharedFlow<String?>()
    val toggleFavoriteFailure: SharedFlow<String?> = _toggleFavoriteFailure
    private val _notifyDataSetChanged = MutableStateFlow<Boolean>(false)
    val notifyDataSetChanged: StateFlow<Boolean> = _notifyDataSetChanged
    private val _uiNotifySuccessEvent = MutableSharedFlow<Boolean>()
    val uiNotifySuccessEvent: SharedFlow<Boolean> = _uiNotifySuccessEvent

    fun toggleFavorite(catUi: CatUI, notifyUiOnSuccess: Boolean = false) {
        viewModelScope.launch {
            val favoriteCurrentState = catUi.isFavorite.value
            reflectOnToggleButtonActionInstantly(catUi)
            if (favoriteCurrentState) { // remove from favorite
                catUi.favoriteID.value?.let { id ->
                    repository.removeFromFavorites(id)
                        .catch { e ->
                            e.printStackTrace()
                            _toggleFavoriteFailure.emit(context.getString(R.string.data_error))
                            clearButtonActionRemoveFavInFailure(catUi, id)
                        }
                        .collect { toggleActionResource ->
                            if (toggleActionResource.errorMessage != null) {
                                _toggleFavoriteFailure.emit(toggleActionResource.errorMessage)
                                clearButtonActionRemoveFavInFailure(catUi, id)
                            } else {
                                catUi.favoriteID.value = null
                                if (notifyUiOnSuccess) {
                                    _uiNotifySuccessEvent.emit(true)
                                    _uiNotifySuccessEvent.emit(false)
                                }
                            }
                        }
                }
            } else { // add to favorite
                repository.addToFavorite(AddToFavRequest(imageId = catUi.cat.id, Constants.SUB_ID))
                    .catch { e ->
                        e.printStackTrace()
                        _toggleFavoriteFailure.emit(context.getString(R.string.data_error))
                        clearButtonActionAddFavInFailure(catUi)
                    }
                    .collect { toggleActionResource ->
                        if (toggleActionResource.errorMessage != null) {
                            _toggleFavoriteFailure.emit(toggleActionResource.errorMessage)
                            clearButtonActionAddFavInFailure(catUi)
                        } else {
                            catUi.favoriteID.value = toggleActionResource.data?.id
                            if (notifyUiOnSuccess) {
                                _uiNotifySuccessEvent.emit(true)
                                _uiNotifySuccessEvent.emit(false)
                            }
                        }
                    }
            }

        }
    }

    private fun reflectOnToggleButtonActionInstantly(catUi: CatUI) {
        catUi.isFavorite.value = !catUi.isFavorite.value
        _notifyDataSetChanged.value = true
    }

    private fun clearButtonActionRemoveFavInFailure(catUi: CatUI, id: Long) {
        catUi.isFavorite.value = true
        catUi.favoriteID.value = id
        _notifyDataSetChanged.value = true
    }

    private fun clearButtonActionAddFavInFailure(catUi: CatUI) {
        catUi.favoriteID.value = null
        catUi.isFavorite.value = false
        _notifyDataSetChanged.value = true
    }

}