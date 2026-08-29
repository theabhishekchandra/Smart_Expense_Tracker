package com.abhishek.spendly.data.local.room.dao

import androidx.room.*
import com.abhishek.spendly.data.local.room.entity.ContactEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ContactDao {
    /** Insert or update a contact for lender or business.*/
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(contact: ContactEntity): Long

    @Update
    suspend fun update(contact: ContactEntity)

    @Delete
    suspend fun delete(contact: ContactEntity)

    @Query("SELECT * FROM contacts WHERE userId = :userId ORDER BY name ASC")
    fun getContactsForUser(userId: Long): Flow<List<ContactEntity>>

    @Query("SELECT * FROM contacts WHERE contactId = :id")
    fun getById(id: Long): Flow<ContactEntity?>

    @Query("SELECT * FROM contacts WHERE phone = :phone LIMIT 1")
    suspend fun getByPhone(phone: String): ContactEntity?
}
