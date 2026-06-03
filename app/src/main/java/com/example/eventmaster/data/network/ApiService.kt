package com.example.eventmaster.data.network

import com.example.eventmaster.models.Category
import com.example.eventmaster.models.Event
import com.example.eventmaster.models.EventRequest
import retrofit2.http.*


interface ApiService {
    @GET("categories")
    suspend fun getCategories(): List<Category>

    @POST("categories")
    suspend fun createCategory(@Body category: Category): Category

    @GET("events")
    suspend fun getEvents(@Query("category_id") categoryId: Int? = null): List<Event>

    @GET("events/{id}")
    suspend fun getEventDetail(@Path("id") id: Int): Event

    @POST("events")
    suspend fun createEvent(@Body event: EventRequest): Event
}