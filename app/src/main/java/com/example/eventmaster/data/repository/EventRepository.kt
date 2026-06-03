package com.example.eventmaster.data.repository

import com.example.eventmaster.models.Event
import com.example.eventmaster.models.EventRequest
import com.example.eventmaster.models.Category
import com.example.eventmaster.data.network.ApiService
import com.example.eventmaster.data.local.dao.EventDao
import com.example.eventmaster.data.local.dao.CategoryDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface EventRepository {
    // API methods
    suspend fun getAllEvents(): List<Event>
    suspend fun getEventDetail(id: Int): Event
    suspend fun createEvent(event: EventRequest): Event

    // Local (Room) methods - used by EventMasterViewModel
    val allCategories: Flow<List<Category>>
    val allEvents: Flow<List<Event>>
    suspend fun insertCategory(category: Category)
    suspend fun insertEvent(event: Event)
    fun getEventsByCategory(categoryId: String): Flow<List<Event>>
    suspend fun getEventById(eventId: String): Event?
}

class EventRepositoryImpl @Inject constructor(
    private val api: ApiService,
    private val eventDao: EventDao,
    private val categoryDao: CategoryDao
) : EventRepository {
    override suspend fun getAllEvents(): List<Event> = api.getEvents()
    override suspend fun getEventDetail(id: Int): Event = api.getEventDetail(id)
    override suspend fun createEvent(event: EventRequest): Event = api.createEvent(event)

    override val allCategories: Flow<List<Category>> = categoryDao.getAllCategories()
    override val allEvents: Flow<List<Event>> = eventDao.getAllEvents()

    override suspend fun insertCategory(category: Category) {
        categoryDao.insertCategory(category)
    }

    override suspend fun insertEvent(event: Event) {
        eventDao.insertEvent(event)
    }

    override fun getEventsByCategory(categoryId: String): Flow<List<Event>> {
        val id = categoryId.toIntOrNull() ?: 0
        return eventDao.getEventsByCategory(id)
    }

    override suspend fun getEventById(eventId: String): Event? {
        val id = eventId.toIntOrNull() ?: 0
        return eventDao.getEventById(id)
    }
}
