package com.example.eventmaster.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.eventmaster.ui.components.EventCard
import com.example.eventmaster.viewmodel.EventViewModel
import com.example.eventmaster.viewmodel.EventsUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onEventClick: (Int) -> Unit,
    viewModel: EventViewModel = hiltViewModel()
) {
    val eventsState by viewModel.eventsState.collectAsState()
    var groupedEvents by remember { mutableStateOf<Map<String, List<com.example.eventmaster.models.Event>>?>(null) }

    LaunchedEffect(eventsState) {
        if (eventsState is EventsUiState.Success) {
            val events = (eventsState as EventsUiState.Success).events
            groupedEvents = events.groupBy { it.categoryId.toString() }
        }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("EventMaster") }) }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
            when (eventsState) {
                is EventsUiState.Loading -> {
                    // Carga silenciosa
                }
                is EventsUiState.Success -> {
                    val events = (eventsState as EventsUiState.Success).events
                    if (events.isEmpty()) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                            Text("No hay eventos disponibles")
                        }
                    } else {
                        LazyColumn(contentPadding = PaddingValues(16.dp)) {
                            groupedEvents?.forEach { (category, eventsInCategory) ->
                                item {
                                    Text(
                                        text = "Categoría $category",
                                        style = MaterialTheme.typography.titleLarge,
                                        modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
                                    )
                                }
                                items(eventsInCategory) { event ->
                                    EventCard(event = event) { onEventClick(event.id) }
                                    Spacer(modifier = Modifier.height(8.dp))
                                }
                            }
                        }
                    }
                }
                is EventsUiState.Error -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                        Text("Error: ${(eventsState as EventsUiState.Error).message}", color = MaterialTheme.colorScheme.error)
                    }
                }
            }
        }
    }
}
