package com.example.eventmaster.models

import kotlinx.serialization.Serializable

@Serializable
data class EventRequest(
    val name: String,
    val description: String,
    val date: String,
    val location: String,
    val category_id: Int
)