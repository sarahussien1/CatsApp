package com.swordhealth.catsapp.repositories.favoriteItem

import com.swordhealth.catsapp.localDataSources.favoriteItem.FavoritesLocalDataContract
import com.swordhealth.catsapp.localDataSources.favoriteItem.FavoritesLocalDataSource
import com.swordhealth.catsapp.remoteDataSources.favoriteItem.FavoritesRemoteDataContract
import com.swordhealth.catsapp.remoteDataSources.favoriteItem.FavoritesRemoteDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface FavoriteRepositoryModule {
    @Binds
    @Singleton
    fun provideFavoriteRepository(repo: FavoriteRepository): FavoriteRepositoryContract

    @Binds
    @Singleton
    fun provideLocalDataSource(local: FavoritesLocalDataSource): FavoritesLocalDataContract

    @Binds
    @Singleton
    fun provideRemoteDataSource(remote: FavoritesRemoteDataSource): FavoritesRemoteDataContract


}
