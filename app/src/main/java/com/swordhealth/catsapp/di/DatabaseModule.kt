package com.swordhealth.catsapp.di

import android.content.Context
import androidx.room.Room
import com.swordhealth.catsapp.daos.CatDao
import com.swordhealth.catsapp.daos.FavoriteItemDao
import com.swordhealth.catsapp.utils.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(appContext, AppDatabase::class.java, "cats_db").build()
    }

    @Provides
    fun provideCatDao(db: AppDatabase): CatDao {
        return db.catDao()
    }
    @Provides
    fun provideFavoriteItemDao(db: AppDatabase): FavoriteItemDao {
        return db.favoritesDao()
    }
}