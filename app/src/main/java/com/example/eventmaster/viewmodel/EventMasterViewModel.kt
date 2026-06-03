package com.example.eventmaster.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eventmaster.data.repository.EventRepository
import com.example.eventmaster.models.Category
import com.example.eventmaster.models.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventMasterViewModel @Inject constructor(
    private val repository: EventRepository
) : ViewModel() {

    // Observar categorías desde la base de datos
    val categories: StateFlow<List<Category>> = repository.allCategories
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Observar todos los eventos (si es necesario tener la lista completa)
    val events: StateFlow<List<Event>> = repository.allEvents
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    init {
        // Podrías inicializar datos aquí si la base de datos está vacía, 
        // pero lo ideal es usar un Callback en el DatabaseModule o un Worker.
        // Por ahora, mantengamos la lógica de inserción inicial si es necesario,
        // pero comentada para no duplicar datos cada vez que se crea el ViewModel.
        /*
        viewModelScope.launch {
            // Ejemplo de cómo podrías insertar datos iniciales una sola vez
            // if (repository.allCategories.first().isEmpty()) { ... }
        }
        */
    }

    fun addCategory(name: String) {
        if (name.isNotBlank()) {
            viewModelScope.launch {
                repository.insertCategory(Category(name = name.trim()))
            }
        }
    }

    fun addEvent(event: Event) {
        viewModelScope.launch {
            repository.insertEvent(event)
        }
    }

    // Para obtener eventos por categoría de forma reactiva en la UI
    fun getEventsByCategory(categoryId: String): StateFlow<List<Event>> {
        return repository.getEventsByCategory(categoryId)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )
    }

    // Para obtener un evento específico
    suspend fun getEventById(eventId: String): Event? {
        return repository.getEventById(eventId)
    }
}
