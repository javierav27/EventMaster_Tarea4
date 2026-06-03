package com.example.eventmaster.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
@Entity(tableName = "events")
data class Event(
    @PrimaryKey
    val id: Int,
    val name: String,
    val description: String,
    val date: String, // Changed to String for simplicity with Room or use TypeConverter
    val location: String,
    val categoryId: Int
)