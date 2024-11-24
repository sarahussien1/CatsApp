package com.swordhealth.catsapp.localDataSources.favoriteItem

import android.content.Context
import com.swordhealth.catsapp.R
import com.swordhealth.catsapp.daos.FavoriteItemDao
import com.swordhealth.catsapp.models.AddToFavRequest
import com.swordhealth.catsapp.models.AddToFavResponse
import com.swordhealth.catsapp.models.FavoriteItem
import com.swordhealth.catsapp.models.RemoveFromFavResponse
import com.swordhealth.catsapp.utils.Resource
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Date
import javax.inject.Inject

class FavoritesLocalDataSource @Inject constructor(
    private val favoritesDao: FavoriteItemDao,
    @ApplicationContext private val context: Context
) : FavoritesLocalDataContract {
    override suspend fun upsertAll(favorites: List<FavoriteItem>): Resource<Boolean> {
        return try {
            favoritesDao.upsertFavorites(favorites)
            Resource.Success(true)
        } catch (exception: Exception) {
            exception.printStackTrace()
            Resource.DataError(context.getString(R.string.data_error))
        }
    }

    override suspend fun getFavorites(subId: String): Resource<List<FavoriteItem>> {
        return try {
            Resource.Success(favoritesDao.getAllFavorites(subId))
        } catch (exception: Exception) {
            exception.printStackTrace()
            Resource.DataError(context.getString(R.string.data_error))
        }
    }

    override suspend fun addToFavorite(id: Long, favoriteRequest: AddToFavRequest): Resource<AddToFavResponse> {
        return try {
            favoritesDao.addToFavorite(FavoriteItem(id, favoriteRequest.imageId, favoriteRequest.subId ,Date()))
            Resource.Success(AddToFavResponse(message = "SUCCESS", id = id))
        } catch (exception: Exception) {
            //TODO: cover tests here
            exception.printStackTrace()
            Resource.DataError(context.getString(R.string.data_error))
        }
    }

    override suspend fun removeFromFavorites(favoriteId: Long): Resource<RemoveFromFavResponse> {
        return try {
            favoritesDao.removeFromFavorite(favoriteId)
            Resource.Success(RemoveFromFavResponse(message = "SUCCESS"))
        } catch (exception: Exception) {
            exception.printStackTrace()
            Resource.DataError(context.getString(R.string.data_error))
        }
    }

}