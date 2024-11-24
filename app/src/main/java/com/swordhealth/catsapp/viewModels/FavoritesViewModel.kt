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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val repository: FavoriteRepositoryContract,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _toggleFavoriteFailure = MutableStateFlow<String?>(null)
    val toggleFavoriteFailure: StateFlow<String?> = _toggleFavoriteFailure
    val page = 0
    val limit = 100

    fun toggleFavorite(catUi: CatUI) {
        viewModelScope.launch {
            if (catUi.isFavorite) { // remove from favorite
                repository.removeFromFavorites(catUi.favoriteID!!)
            } else { // add to favorite
                repository.addToFavorite(AddToFavRequest(imageId = catUi.cat.id, Constants.SUB_ID))
            }
                .catch { e ->
                    e.printStackTrace()
                    _toggleFavoriteFailure.value = context.getString(R.string.data_error)
                }
                .collect { toggleActionResource ->
                    if (toggleActionResource.errorMessage != null) {
                        _toggleFavoriteFailure.value = toggleActionResource.errorMessage
                    }
                }
        }
    }
}