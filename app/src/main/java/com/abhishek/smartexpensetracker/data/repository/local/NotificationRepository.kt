package com.abhishek.smartexpensetracker.data.repository.local

import com.abhishek.smartexpensetracker.data.local.room.entity.ContactEntity
import com.abhishek.smartexpensetracker.data.local.room.entity.NotificationEntity

class NotificationRepository()
    :INotificationRepository {

    override suspend fun scheduleReminder(notification: NotificationEntity): Long {
        TODO("Not yet implemented")
    }

    override suspend fun getAllReminders(): List<NotificationEntity> {
        TODO("Not yet implemented")
    }

    override suspend fun sendWhatsAppUpdate(
        contact: ContactEntity,
        message: String
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun sendSmsUpdate(
        contact: ContactEntity,
        message: String
    ) {
        TODO("Not yet implemented")
    }
}

interface INotificationRepository{
    suspend fun scheduleReminder(notification: NotificationEntity): Long
    suspend fun getAllReminders(): List<NotificationEntity>
    suspend fun sendWhatsAppUpdate(contact: ContactEntity, message: String)
    suspend fun sendSmsUpdate(contact: ContactEntity, message: String)
}
