package com.abhishek.spendly.data.local.room

import androidx.room.TypeConverter
import java.util.Date

class Converters {

    // List<String> <-> String
    @TypeConverter
    fun fromStringList(list: List<String>?): String? {
        return list?.joinToString("|")
    }

    @TypeConverter
    fun toStringList(data: String?): List<String>? {
        return data?.split("|")?.map { it.trim() }
    }

    // Date <-> Long
    @TypeConverter
    fun fromDate(date: Date?): Long? = date?.time

    @TypeConverter
    fun toDate(value: Long?): Date? = value?.let { Date(it) }
}


