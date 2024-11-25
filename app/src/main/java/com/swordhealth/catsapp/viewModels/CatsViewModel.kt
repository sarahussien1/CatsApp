package com.swordhealth.catsapp.viewModels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swordhealth.catsapp.R
import com.swordhealth.catsapp.ui.models.CatUI
import com.swordhealth.catsapp.usecases.CatsUseCase
import com.swordhealth.catsapp.utils.Constants
import com.swordhealth.catsapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CatsViewModel @Inject constructor(
    private val catsUseCase: CatsUseCase,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _cats = MutableStateFlow<Resource<List<CatUI>>>(Resource.Loading())
    val cats: StateFlow<Resource<List<CatUI>>> = _cats
    val page = 0
    val limit = 20

    init {
        getCats()
    }

    fun getCats(
        imageSize: String? = Constants.DEFAULT_SIZE,
        hasBreeds: Boolean? = Constants.DEFAULT_HAS_BREEDS,
        order: String? = Constants.DEFAULT_ORDER
    ) {
        viewModelScope.launch {
            _cats.value = Resource.Loading()
            catsUseCase.getCatsWithFavorites(
                imageSize,
                hasBreeds,
                order,
                page,
                limit,
                Constants.SUB_ID
            )
                .catch { e ->
                    e.printStackTrace()
                    _cats.value = Resource.DataError(context.getString(R.string.data_error))
                }
                .collect { catList ->
                    _cats.value = catList
                }
        }
    }

    fun notifyDataSetChanged() {
        notifyDataSetChangedCats()
//        notifyDataSetChangedFavs()
    }

    private fun notifyDataSetChangedCats() {
        _cats.value.data?.let { list ->
            _cats.value = Resource.Success(list)
        }
    }
//    private fun notifyDataSetChangedFavs() {
//        _filteredCats.value.data?.let { list ->
//            _filteredCats.value = Resource.Success(list)
//        }
//    }

    fun filterWithFavorites(catUIs: List<CatUI>): List<CatUI> {
        return _cats.value.data?.filter { it.isFavorite.value } ?: listOf()
    }

}