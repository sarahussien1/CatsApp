package com.swordhealth.catsapp.usecases

import androidx.compose.runtime.mutableStateOf
import com.swordhealth.catsapp.repositories.cat.CatRepositoryContract
import com.swordhealth.catsapp.repositories.favoriteItem.FavoriteRepositoryContract
import com.swordhealth.catsapp.ui.models.CatUI
import com.swordhealth.catsapp.utils.Resource
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class CatsUseCase @Inject constructor(
    private val catsRepository: CatRepositoryContract,
    private val favoritesRepository: FavoriteRepositoryContract,
) {

    fun getCatsWithFavorites(
        size: String?,
        hasBreeds: Boolean?,
        order: String?,
        page: Int,
        limit: Int,
        subId: String
    ): Flow<Resource<List<CatUI>>> {
        return combine(catsRepository.getCats(size, hasBreeds, order, page, limit), favoritesRepository.getFavorites(subId)) { cats, favorites ->
            if (cats.errorMessage != null) {
                return@combine Resource.DataError(cats.errorMessage)
            }
            // Create a map of image IDs associate by favorite IDs for quick lookup
            val favoriteCatIds = favorites.data?.associate { it.imageId to it.id }
            // Map cats to CatUI, setting isFavorite based on presence in favoriteIds
            val catUis = cats.data?.map { cat ->
                CatUI(
                    cat = cat,
                    isFavorite = mutableStateOf(favoriteCatIds?.contains(cat.id) ?: false) ,
                    favoriteID = mutableStateOf(if (favoriteCatIds?.contains(cat.id) == true) favoriteCatIds[cat.id] else null)
                )
            }
            return@combine Resource.Success(catUis ?: listOf())
        }
    }
}