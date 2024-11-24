package com.swordhealth.catsapp.repositories.favoriteItem

import com.swordhealth.catsapp.localDataSources.favoriteItem.FavoritesLocalDataContract
import com.swordhealth.catsapp.models.AddToFavRequest
import com.swordhealth.catsapp.models.AddToFavResponse
import com.swordhealth.catsapp.models.Cat
import com.swordhealth.catsapp.models.FavoriteItem
import com.swordhealth.catsapp.models.RemoveFromFavResponse
import com.swordhealth.catsapp.remoteDataSources.favoriteItem.FavoritesRemoteDataContract
import com.swordhealth.catsapp.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlin.coroutines.CoroutineContext

class FavoriteRepository (
    private val favoritesRemoteDataSource: FavoritesRemoteDataContract,
    private val favoritesLocalDataSource: FavoritesLocalDataContract,
    private val ioDispatcher: CoroutineContext
): FavoriteRepositoryContract {
    override fun getFavorites(subId: String): Flow<Resource<List<FavoriteItem>>> {
        return flow {
            val remoteResult = favoritesRemoteDataSource.getFavorites(subId)
            if (remoteResult.errorMessage != null) { // fallback to local
                val localResult = favoritesLocalDataSource.getFavorites(subId)
                if (localResult.data is List<FavoriteItem>) {
                    emit(localResult)
                }
            }
            emit(remoteResult)
            if (!remoteResult.data.isNullOrEmpty()) {
                favoritesLocalDataSource.upsertAll(remoteResult.data)
            }
        }.flowOn(ioDispatcher)
    }

    override fun addToFavorite(favoriteRequest: AddToFavRequest): Flow<Resource<AddToFavResponse>> {
        return flow {
            val remoteResult = favoritesRemoteDataSource.addToFavorite(favoriteRequest)
            emit(remoteResult)
            if (remoteResult.data != null) {
                favoritesLocalDataSource.addToFavorite(remoteResult.data.id, favoriteRequest)
            }
        }.flowOn(ioDispatcher)
    }

    override fun removeFromFavorites(favoriteId: Long): Flow<Resource<RemoveFromFavResponse>> {
        return flow {
            val remoteResult = favoritesRemoteDataSource.removeFromFavorites(favoriteId)
            emit(remoteResult)
            if (remoteResult.data != null) {
                favoritesLocalDataSource.removeFromFavorites(favoriteId)
            }
        }.flowOn(ioDispatcher)
    }
}