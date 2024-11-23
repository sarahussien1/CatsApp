package com.swordhealth.catsapp.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.swordhealth.catsapp.models.Cat

@Dao
interface CatDao {
    @Upsert
    suspend fun upsertCats(cat: List<Cat>)

    @Query("SELECT * FROM cats") //TODO: change this query to have the given parameters from repository
    suspend fun getAllCats(): List<Cat>
}