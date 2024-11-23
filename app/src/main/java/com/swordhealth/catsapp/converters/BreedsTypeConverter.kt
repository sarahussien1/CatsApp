package com.swordhealth.catsapp.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.swordhealth.catsapp.models.Breed

class BreedsTypeConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromBreedsList(breeds: List<Breed>): String {
        return gson.toJson(breeds)
    }

    @TypeConverter
    fun toBreedsList(breedsString: String): List<Breed> {
        val listType = object : TypeToken<List<Breed>>() {}.type
        return gson.fromJson(breedsString, listType)
    }
}