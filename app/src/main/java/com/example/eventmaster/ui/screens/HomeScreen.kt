
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
import com.example.eventmaster.ui.viewmodels.EventViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onEventClick: (Int) -> Unit,
    viewModel: EventViewModel = hiltViewModel()
) {
    val eventsState by viewModel.eventsState.collectAsState()
    var groupedEvents by remember { mutableStateOf<Map<String, List<com.example.eventmaster.data.model.Event>>?>(null) }

    LaunchedEffect(eventsState) {
        if (eventsState is com.example.eventmaster.ui.viewmodels.EventsUiState.Success) {
            val events = (eventsState as com.example.eventmaster.ui.viewmodels.EventsUiState.Success).events
            groupedEvents = events.groupBy { it.category.name }
        }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("EventMaster") }) }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            when (eventsState) {
                is com.example.eventmaster.ui.viewmodels.EventsUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.fillMaxSize())
                }
                is com.example.eventmaster.ui.viewmodels.EventsUiState.Success -> {
                    LazyColumn(contentPadding = PaddingValues(16.dp)) {
                        groupedEvents?.forEach { (category, events) ->
                            item {
                                Text(
                                    text = category,
                                    style = MaterialTheme.typography.titleLarge,
                                    modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
                                )
                            }
                            items(events) { event ->
                                EventCard(event = event) { onEventClick(event.id) }
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }
                }
                is com.example.eventmaster.ui.viewmodels.EventsUiState.Error -> {
                    Text("Error: ${(eventsState as com.example.eventmaster.ui.viewmodels.EventsUiState.Error).message}")
                }
            }
        }
    }
}
