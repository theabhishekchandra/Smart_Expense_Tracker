package com.abhishek.smartexpensetracker.data.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.abhishek.smartexpensetracker.data.local.room.entity.CategoryEntity
import com.abhishek.smartexpensetracker.data.model.Category

@Dao
interface CategoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: CategoryEntity)

    @Query("SELECT * FROM categories ORDER BY name ASC")
    suspend fun getAllCategories(): List<CategoryEntity>

    @Query("DELETE FROM categories WHERE categoryId = :id")
    suspend fun deleteCategory(id: Long)
}
