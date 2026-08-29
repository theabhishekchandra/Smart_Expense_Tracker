package com.abhishek.spendly.data.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.abhishek.spendly.data.local.room.entity.NotificationEntity

@Dao
interface NotificationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotification(notification: NotificationEntity)

    @Query("SELECT * FROM notifications WHERE userId = :userId ORDER BY createdAt DESC")
    suspend fun getNotificationsForUser(userId: Long): List<NotificationEntity>

    @Query("UPDATE notifications SET isRead = 1 WHERE notificationId = :id")
    suspend fun markAsRead(id: Long)

    @Query("DELETE FROM notifications WHERE createdAt < :cutoff")
    suspend fun clearOldNotifications(cutoff: Long)
}
