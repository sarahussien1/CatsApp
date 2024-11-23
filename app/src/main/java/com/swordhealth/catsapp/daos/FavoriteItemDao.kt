package com.swordhealth.catsapp.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Upsert
import com.swordhealth.catsapp.models.FavoriteItem

@Dao
interface FavoriteItemDao {
    @Upsert
    suspend fun upsertFavorites(favorites: List<FavoriteItem>)

    @Query("SELECT * FROM favorites WHERE subId = :subId")
    suspend fun getAllFavorites(subId: String): List<FavoriteItem>

    @Insert
    suspend fun addToFavorite(favorite: FavoriteItem)

    @Query("DELETE FROM cats WHERE id = :favoriteId")
    suspend fun removeFromFavorite(favoriteId: Long)
}