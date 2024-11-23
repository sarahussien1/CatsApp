package com.swordhealth.catsapp.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.swordhealth.catsapp.models.Cat

@Dao
interface CatDao {
    @Upsert
    suspend fun upsertCats(cat: List<Cat>)

    @Query("SELECT * FROM cats")
    suspend fun getAllCats(): List<Cat>
}