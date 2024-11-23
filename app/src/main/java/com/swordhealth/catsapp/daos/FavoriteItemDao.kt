package com.swordhealth.catsapp.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.swordhealth.catsapp.models.FavoriteItem

@Dao
interface FavoriteItemDao {
    @Upsert
    suspend fun upsertFavorites(favorites: List<FavoriteItem>)

    @Query("SELECT * FROM favorites")
    suspend fun getAllFavorites(): List<FavoriteItem>
}