package com.example.eventmaster.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eventmaster.models.Event
import com.example.eventmaster.models.EventRequest
import com.example.eventmaster.data.repository.EventRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventViewModel @Inject constructor(
    private val repository: EventRepository
) : ViewModel() {
    private val _eventsState = MutableStateFlow<EventsUiState>(EventsUiState.Loading)
    val eventsState: StateFlow<EventsUiState> = _eventsState.asStateFlow()

    private val _detailState = MutableStateFlow<EventDetailUiState>(EventDetailUiState.Loading)
    val detailState: StateFlow<EventDetailUiState> = _detailState.asStateFlow()

    init {
        loadEvents()
    }

    fun loadEvents() {
        viewModelScope.launch {
            _eventsState.value = EventsUiState.Loading
            try {
                val events = repository.getAllEvents()
                _eventsState.value = EventsUiState.Success(events)
            } catch (e: Exception) {
                _eventsState.value = EventsUiState.Error(e.message ?: "Error al cargar eventos")
            }
        }
    }

    fun loadEventDetail(eventId: Int) {
        viewModelScope.launch {
            _detailState.value = EventDetailUiState.Loading
            try {
                val event = repository.getEventDetail(eventId)
                _detailState.value = EventDetailUiState.Success(event)
            } catch (e: Exception) {
                _detailState.value = EventDetailUiState.Error(e.message ?: "Error al cargar detalle")
            }
        }
    }

    fun createEvent(event: EventRequest, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                repository.createEvent(event)
                loadEvents()
                onSuccess()
            } catch (e: Exception) {
                _eventsState.value = EventsUiState.Error(e.message ?: "Error al crear evento")
            }
        }
    }
}

sealed class EventsUiState {
    object Loading : EventsUiState()
    data class Success(val events: List<Event>) : EventsUiState()
    data class Error(val message: String) : EventsUiState()
}

sealed class EventDetailUiState {
    object Loading : EventDetailUiState()
    data class Success(val event: Event) : EventDetailUiState()
    data class Error(val message: String) : EventDetailUiState()
}
