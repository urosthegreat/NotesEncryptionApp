package com.example.notesencryptedapp.utils

import androidx.room.TypeConverter
import java.util.Date

class Converters {
    @TypeConverter
    fun fromDate(date: Date): Long = date.time

    @TypeConverter
    fun toDate(dateLong: Long): Date = Date(dateLong)
}
