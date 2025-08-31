package com.abhishek.smartexpensetracker.data.repository

import com.abhishek.smartexpensetracker.data.local.room.dao.NotificationDao
import com.abhishek.smartexpensetracker.data.local.room.entity.NotificationEntity
import javax.inject.Inject

interface INotificationRepository {
    suspend fun insertNotification(notification: NotificationEntity)
    suspend fun getNotificationsForUser(userId: Long): List<NotificationEntity>
    suspend fun markAsRead(id: Long)
    suspend fun clearOldNotifications(cutoff: Long)
}

class NotificationRepository @Inject constructor(
    private val dao: NotificationDao
) : INotificationRepository {
    override suspend fun insertNotification(notification: NotificationEntity) = dao.insertNotification(notification)
    override suspend fun getNotificationsForUser(userId: Long): List<NotificationEntity> = dao.getNotificationsForUser(userId)
    override suspend fun markAsRead(id: Long) = dao.markAsRead(id)
    override suspend fun clearOldNotifications(cutoff: Long) = dao.clearOldNotifications(cutoff)

//    fun observeForUser(userId: Long): Flow<List<Notification>> = notificationDao.getForUser(userId)
}
