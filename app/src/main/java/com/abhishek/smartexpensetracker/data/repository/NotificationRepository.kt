package com.abhishek.smartexpensetracker.data.repository

import com.abhishek.smartexpensetracker.data.local.room.Notification
import com.abhishek.smartexpensetracker.data.local.room.NotificationDao
import javax.inject.Inject

interface INotificationRepository {
    suspend fun insertNotification(notification: Notification)
    suspend fun getNotificationsForUser(userId: Long): List<Notification>
    suspend fun markAsRead(id: Long)
    suspend fun clearOldNotifications(cutoff: Long)
}

class NotificationRepository @Inject constructor(
    private val dao: NotificationDao
) : INotificationRepository {
    override suspend fun insertNotification(notification: Notification) = dao.insertNotification(notification)
    override suspend fun getNotificationsForUser(userId: Long): List<Notification> = dao.getNotificationsForUser(userId)
    override suspend fun markAsRead(id: Long) = dao.markAsRead(id)
    override suspend fun clearOldNotifications(cutoff: Long) = dao.clearOldNotifications(cutoff)
}
