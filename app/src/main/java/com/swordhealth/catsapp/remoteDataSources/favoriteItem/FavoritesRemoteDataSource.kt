package com.swordhealth.catsapp.remoteDataSources.favoriteItem

import android.content.Context
import android.util.Log
import com.swordhealth.catsapp.R
import com.swordhealth.catsapp.models.AddToFavRequest
import com.swordhealth.catsapp.models.AddToFavResponse
import com.swordhealth.catsapp.models.FavoriteItem
import com.swordhealth.catsapp.models.RemoveFromFavResponse
import com.swordhealth.catsapp.services.FavoritesApiService
import com.swordhealth.catsapp.utils.NetworkConnectivity
import com.swordhealth.catsapp.utils.Resource
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class FavoritesRemoteDataSource @Inject constructor(
    @ApplicationContext private val context: Context,
    private val networkConnectivity: NetworkConnectivity,
    private val favoritesApiService: FavoritesApiService
) : FavoritesRemoteDataContract {
    override suspend fun getFavorites(subId: String): Resource<List<FavoriteItem>> {
        if (!networkConnectivity.isConnected()) {
            return Resource.DataError(context.getString(R.string.no_internet_error))
        }
        val response = favoritesApiService.getFavorites(subId)
        return if (response.isSuccessful) {
            Resource.Success(response.body() as List<FavoriteItem>)
        } else {
            Resource.DataError(response.message() ?: context.getString(R.string.data_error))
        }
    }

    override suspend fun addToFavorite(favoriteRequest: AddToFavRequest): Resource<AddToFavResponse> {
        if (!networkConnectivity.isConnected()) {
            return Resource.DataError(context.getString(R.string.no_internet_error))
        }
        val response = favoritesApiService.addToFavorites(favoriteRequest)
        return if (response.isSuccessful) {
            Resource.Success(response.body() as AddToFavResponse)
        } else {
            Resource.DataError(response.errorBody()?.string() ?: context.getString(R.string.data_error))
        }
    }

    override suspend fun removeFromFavorites(favoriteId: Long): Resource<RemoveFromFavResponse> {
        if (!networkConnectivity.isConnected()) {
            return Resource.DataError(context.getString(R.string.no_internet_error))
        }
        val response = favoritesApiService.removeFromFavorites(favoriteId)
        return if (response.isSuccessful) {
            Resource.Success(response.body() as RemoveFromFavResponse)
        } else {
            Resource.DataError(response.errorBody()?.string() ?: context.getString(R.string.data_error))
        }
    }
}