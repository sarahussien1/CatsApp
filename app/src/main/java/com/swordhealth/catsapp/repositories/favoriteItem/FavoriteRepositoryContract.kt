package com.swordhealth.catsapp.repositories.favoriteItem

import com.swordhealth.catsapp.models.AddToFavRequest
import com.swordhealth.catsapp.models.AddToFavResponse
import com.swordhealth.catsapp.models.FavoriteItem
import com.swordhealth.catsapp.models.RemoveFromFavResponse
import com.swordhealth.catsapp.utils.Resource
import kotlinx.coroutines.flow.Flow

interface FavoriteRepositoryContract {
     fun getFavorites(subId: String): Flow<Resource<List<FavoriteItem>>>
     fun addToFavorite(favoriteRequest: AddToFavRequest): Flow<Resource<AddToFavResponse>>
     fun removeFromFavorites(favoriteId: Long): Flow<Resource<RemoveFromFavResponse>>
}