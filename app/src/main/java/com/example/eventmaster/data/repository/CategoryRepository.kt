package com.example.eventmaster.data.repository

import com.example.eventmaster.models.Category
import com.example.eventmaster.data.network.ApiService
import com.example.eventmaster.data.local.dao.CategoryDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface CategoryRepository {
    suspend fun getCategories(): List<Category>
    suspend fun createCategory(name: String): Category
    // Local Room methods
    fun getAllCategoriesFlow(): Flow<List<Category>>
    suspend fun insertLocalCategory(category: Category)
}

class CategoryRepositoryImpl @Inject constructor(
    private val api: ApiService,
    private val categoryDao: CategoryDao
) : CategoryRepository {
    override suspend fun getCategories(): List<Category> = api.getCategories()
    
    override suspend fun createCategory(name: String): Category {
        val category = api.createCategory(Category(id = 0, name = name))
        // También lo guardamos localmente para que aparezca de inmediato si usamos Flow
        categoryDao.insertCategory(category)
        return category
    }

    override fun getAllCategoriesFlow(): Flow<List<Category>> = categoryDao.getAllCategories()
    
    override suspend fun insertLocalCategory(category: Category) {
        categoryDao.insertCategory(category)
    }
}
