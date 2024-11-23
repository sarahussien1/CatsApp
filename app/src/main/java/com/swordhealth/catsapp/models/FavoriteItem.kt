package com.swordhealth.catsapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import java.util.Date

@Entity(tableName = "favorites")
data class FavoriteItem (
    @PrimaryKey
    @Expose
    val id :Long,
    @Expose
    val imageId: String,
    @Expose
    val subId: String,
    @Expose
    val createdAt: Date
)