package com.swordhealth.catsapp.localDataSources.favoriteItem

import com.swordhealth.catsapp.models.AddToFavRequest
import com.swordhealth.catsapp.models.AddToFavResponse
import com.swordhealth.catsapp.models.FavoriteItem
import com.swordhealth.catsapp.models.RemoveFromFavResponse
import com.swordhealth.catsapp.utils.Resource

interface FavoritesLocalDataContract {
    suspend fun upsertAll(favorites: List<FavoriteItem>): Resource<Boolean>
    suspend fun getFavorites(subId: String): Resource<List<FavoriteItem>>
    suspend fun addToFavorite(id: Long,favoriteRequest: AddToFavRequest): Resource<AddToFavResponse>
    suspend fun removeFromFavorites(favoriteId: Long): Resource<RemoveFromFavResponse>
}