package com.abhishek.smartexpensetracker.data.repository.local

import com.abhishek.smartexpensetracker.data.local.room.dao.ContactDao
import com.abhishek.smartexpensetracker.data.local.room.entity.ContactEntity
import kotlinx.coroutines.flow.Flow

class ContactRepository(
    private val contactDao: ContactDao
) : IContactRepository{
    override suspend fun add(contact: ContactEntity): Long = contactDao.insert(contact)
    override suspend fun update(contact: ContactEntity) = contactDao.update(contact)
    override suspend fun delete(contact: ContactEntity) = contactDao.delete(contact)

    override fun observeContacts(userId: Long): Flow<List<ContactEntity>> =
        contactDao.getContactsForUser(userId)

    override fun observeById(id: Long): Flow<ContactEntity?> = contactDao.getById(id)
}

interface IContactRepository{
    suspend fun add(contact: ContactEntity): Long
    suspend fun update(contact: ContactEntity)
    suspend fun delete(contact: ContactEntity)
    fun observeContacts(userId: Long): Flow<List<ContactEntity>>
    fun observeById(id: Long): Flow<ContactEntity?>
}
