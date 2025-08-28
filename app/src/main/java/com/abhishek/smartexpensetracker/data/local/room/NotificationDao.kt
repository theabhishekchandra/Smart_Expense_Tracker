package com.abhishek.smartexpensetracker.data.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface NotificationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotification(notification: Notification)

    @Query("SELECT * FROM notifications WHERE userId = :userId ORDER BY createdAt DESC")
    suspend fun getNotificationsForUser(userId: Long): List<Notification>

    @Query("UPDATE notifications SET isRead = 1 WHERE id = :id")
    suspend fun markAsRead(id: Long)

    @Query("DELETE FROM notifications WHERE createdAt < :cutoff")
    suspend fun clearOldNotifications(cutoff: Long)
}
