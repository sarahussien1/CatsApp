package com.swordhealth.catsapp.repositories.cat

import com.swordhealth.catsapp.localDataSources.cat.CatsLocalDataContract
import com.swordhealth.catsapp.localDataSources.cat.CatsLocalDataSource
import com.swordhealth.catsapp.remoteDataSources.cat.CatsRemoteDataContract
import com.swordhealth.catsapp.remoteDataSources.cat.CatsRemoteDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface CatRepositoryModule {
    @Binds
    @Singleton
    fun provideCatRepository(repo: CatRepository): CatRepositoryContract

    @Binds
    @Singleton
    fun provideLocalDataSource(local: CatsLocalDataSource): CatsLocalDataContract

    @Binds
    @Singleton
    fun provideRemoteDataSource(remote: CatsRemoteDataSource): CatsRemoteDataContract

}