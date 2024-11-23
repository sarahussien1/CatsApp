package com.swordhealth.catsapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose

@Entity(tableName = "cats")
data class Cat(
    @PrimaryKey
    @Expose
    val id: String,
    @Expose
    val url: String,
    @Expose
    val breeds: List<Breed>
)