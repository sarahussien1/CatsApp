package com.swordhealth.catsapp.converters

import androidx.room.TypeConverter
import java.util.Date

class CreatedAtDateConverter {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}