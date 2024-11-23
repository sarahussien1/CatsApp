package com.swordhealth.catsapp.utils

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.swordhealth.catsapp.converters.BreedsTypeConverter
import com.swordhealth.catsapp.converters.CreatedAtDateConverter
import com.swordhealth.catsapp.daos.CatDao
import com.swordhealth.catsapp.daos.FavoriteItemDao
import com.swordhealth.catsapp.models.Cat
import com.swordhealth.catsapp.models.FavoriteItem

@Database(entities = [Cat::class, FavoriteItem::class], version = 1)
@TypeConverters(BreedsTypeConverter::class, CreatedAtDateConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun catDao(): CatDao
    abstract fun favoritesDao(): FavoriteItemDao
}