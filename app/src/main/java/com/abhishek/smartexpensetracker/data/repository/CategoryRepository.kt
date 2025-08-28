package com.abhishek.smartexpensetracker.data.repository

import com.abhishek.smartexpensetracker.data.local.room.Category
import com.abhishek.smartexpensetracker.data.local.room.CategoryDao
import javax.inject.Inject

interface ICategoryRepository {
    suspend fun insertCategory(category: Category)
    suspend fun getAllCategories(): List<Category>
    suspend fun deleteCategory(id: Long)
}

class CategoryRepository @Inject constructor(
    private val dao: CategoryDao
) : ICategoryRepository {
    override suspend fun insertCategory(category: Category) = dao.insertCategory(category)
    override suspend fun getAllCategories(): List<Category> = dao.getAllCategories()
    override suspend fun deleteCategory(id: Long) = dao.deleteCategory(id)
}
