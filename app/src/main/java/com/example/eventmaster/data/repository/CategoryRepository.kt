package com.example.eventmaster.data.repository

import com.example.eventmaster.models.Category
import com.example.eventmaster.data.network.ApiService
import javax.inject.Inject

interface CategoryRepository {
    suspend fun getCategories(): List<Category>
    suspend fun createCategory(name: String): Category
}

class CategoryRepositoryImpl @Inject constructor(
    private val api: ApiService
) : CategoryRepository {
    override suspend fun getCategories(): List<Category> = api.getCategories()
    override suspend fun createCategory(name: String): Category =
        api.createCategory(Category(id = 0, name = name))
}