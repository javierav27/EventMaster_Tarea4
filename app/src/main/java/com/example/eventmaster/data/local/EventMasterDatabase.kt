package com.example.eventmaster.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.eventmaster.data.local.dao.CategoryDao
import com.example.eventmaster.data.local.dao.EventDao
import com.example.eventmaster.models.Category
import com.example.eventmaster.models.Event

@Database(entities = [Category::class, Event::class], version = 1, exportSchema = false)
abstract class EventMasterDatabase : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao
    abstract fun eventDao(): EventDao
}
