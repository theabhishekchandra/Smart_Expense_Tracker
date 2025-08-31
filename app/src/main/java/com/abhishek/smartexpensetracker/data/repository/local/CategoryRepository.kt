package com.abhishek.smartexpensetracker.data.repository.local

import com.abhishek.smartexpensetracker.data.local.room.dao.CategoryDao
import com.abhishek.smartexpensetracker.data.local.room.entity.CategoryEntity
import javax.inject.Inject

interface ICategoryRepository {
    suspend fun insertCategory(category: CategoryEntity)
    suspend fun getAllCategories(): List<CategoryEntity>
    suspend fun deleteCategory(id: Long)
}

class CategoryRepository @Inject constructor(
    private val dao: CategoryDao
) : ICategoryRepository {
    override suspend fun insertCategory(category: CategoryEntity) = dao.insertCategory(category)
    override suspend fun getAllCategories(): List<CategoryEntity> = dao.getAllCategories()
    override suspend fun deleteCategory(id: Long) = dao.deleteCategory(id)
    // TODO: Implement Edit Category.
}
