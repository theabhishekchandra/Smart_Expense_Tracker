package com.abhishek.smartexpensetracker.data.repository.local

import com.abhishek.smartexpensetracker.data.local.room.dao.ContactDao
import com.abhishek.smartexpensetracker.data.local.room.entity.ContactEntity
import com.abhishek.smartexpensetracker.data.model.DEFAULT_LOCAL_USER_ID
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ContactRepository @Inject constructor(
    private val contactDao: ContactDao
) : IContactRepository {
    override suspend fun add(contact: ContactEntity): Long = contactDao.insert(contact)
    override suspend fun update(contact: ContactEntity) = contactDao.update(contact)
    override suspend fun delete(contact: ContactEntity) = contactDao.delete(contact)

    override fun observeContacts(userId: Long): Flow<List<ContactEntity>> =
        contactDao.getContactsForUser(userId)

    override fun observeById(id: Long): Flow<ContactEntity?> = contactDao.getById(id)

    override suspend fun getOrCreateContactId(name: String, phone: String): Long {
        if (phone.isNotBlank()) {
            contactDao.getByPhone(phone)?.let { return it.contactId }
        }
        return contactDao.insert(
            ContactEntity(userId = DEFAULT_LOCAL_USER_ID, name = name, phone = phone.ifBlank { null })
        )
    }
}

interface IContactRepository {
    suspend fun add(contact: ContactEntity): Long
    suspend fun update(contact: ContactEntity)
    suspend fun delete(contact: ContactEntity)
    fun observeContacts(userId: Long): Flow<List<ContactEntity>>
    fun observeById(id: Long): Flow<ContactEntity?>
    suspend fun getOrCreateContactId(name: String, phone: String): Long
}
