package com.abhishek.smartexpensetracker.data.repository.local

import com.abhishek.smartexpensetracker.data.local.room.dao.CategoryDao
import com.abhishek.smartexpensetracker.data.local.room.entity.CategoryEntity
import javax.inject.Inject

interface ICategoryRepository {
    suspend fun insertCategory(category: CategoryEntity): Long
    suspend fun getAllCategories(): List<CategoryEntity>
    suspend fun deleteCategory(id: Long)
    suspend fun getOrCreateCategoryId(name: String): Long
}

class CategoryRepository @Inject constructor(
    private val dao: CategoryDao
) : ICategoryRepository {
    override suspend fun insertCategory(category: CategoryEntity): Long = dao.insertCategory(category)
    override suspend fun getAllCategories(): List<CategoryEntity> = dao.getAllCategories()
    override suspend fun deleteCategory(id: Long) = dao.deleteCategory(id)

    override suspend fun getOrCreateCategoryId(name: String): Long {
        dao.getCategoryByName(name)?.let { return it.categoryId }
        return dao.insertCategory(CategoryEntity(name = name))
    }
    // TODO: Implement Edit Category.
}
