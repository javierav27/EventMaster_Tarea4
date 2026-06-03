package com.example.eventmaster.data.repository

import com.example.eventmaster.models.Event
import com.example.eventmaster.models.EventRequest
import com.example.eventmaster.data.network.ApiService
import javax.inject.Inject

interface EventRepository {
    suspend fun getAllEvents(): List<Event>
    suspend fun getEventDetail(id: Int): Event
    suspend fun createEvent(event: EventRequest): Event
}

class EventRepositoryImpl @Inject constructor(
    private val api: ApiService
) : EventRepository {
    override suspend fun getAllEvents(): List<Event> = api.getEvents()
    override suspend fun getEventDetail(id: Int): Event = api.getEventDetail(id)
    override suspend fun createEvent(event: EventRequest): Event = api.createEvent(event)
}