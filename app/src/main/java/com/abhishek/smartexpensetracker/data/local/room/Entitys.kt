package com.abhishek.smartexpensetracker.data.local.room

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Relation
import androidx.room.TypeConverter
import com.abhishek.smartexpensetracker.data.local.room.entity.UserEntity
import java.util.Date

//@Entity(
//    tableName = "allocations",
//    foreignKeys = [
//        ForeignKey(entity = UserEntity::class, parentColumns = ["id"], childColumns = ["staffId"], onDelete = ForeignKey.CASCADE)
//    ],
//    indices = [Index("staffId")]
//)
////data class AllocationEntity(
//    @PrimaryKey(autoGenerate = true) val id: Long = 0,
//    val staffId: Long,
//    val title: String,
//    val category: String,
//    val allocatedAmount: Double,
//    val usedAmount: Double = 0.0,
//    val notes: String? = null,
//    val createdAt: Long = System.currentTimeMillis(),
//    val expiresAt: Long? = null,
//    val status: String = "Active" // Active / Closed / Expired
//)
class Converters {

//    private val gson = Gson()

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

    // Map / JSON (for permissions or other small JSON fields)
//    @TypeConverter
//    fun fromMap(map: Map<String, Any>?): String? {
//        return if (map == null) null else gson.toJson(map)
//    }

//    @TypeConverter
//    fun toMap(value: String?): Map<String, Any>? {
//        if (value.isNullOrBlank()) return null
//        val type = object : TypeToken<Map<String, Any>>() {}.type
//        return gson.fromJson(value, type)
//    }
}






