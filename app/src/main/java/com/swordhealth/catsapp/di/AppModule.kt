package com.swordhealth.catsapp.di

import com.swordhealth.catsapp.daos.CatDao
import com.swordhealth.catsapp.utils.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideDispatcher(): CoroutineContext {
        return Dispatchers.IO
    }
}