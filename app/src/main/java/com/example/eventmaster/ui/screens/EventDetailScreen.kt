
package com.example.eventmaster.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.eventmaster.ui.viewmodels.EventViewModel

@Composable
fun EventDetailScreen(
    eventId: Int,
    viewModel: EventViewModel = hiltViewModel()
) {
    val detailState by viewModel.detailState.collectAsState()

    LaunchedEffect(eventId) {
        viewModel.loadEventDetail(eventId)
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Detalle del Evento") }) }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            when (detailState) {
                is com.example.eventmaster.ui.viewmodels.EventDetailUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.fillMaxSize())
                }
                is com.example.eventmaster.ui.viewmodels.EventDetailUiState.Success -> {
                    val event = (detailState as com.example.eventmaster.ui.viewmodels.EventDetailUiState.Success).event
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(event.name, style = MaterialTheme.typography.headlineSmall)
                        Text("Categoría: ${event.category.name}")
                        Text("Fecha: ${event.date}")
                        Text("Ubicación: ${event.location}")
                        Text("Descripción: ${event.description}")
                    }
                }
                is com.example.eventmaster.ui.viewmodels.EventDetailUiState.Error -> {
                    Text("Error: ${(detailState as com.example.eventmaster.ui.viewmodels.EventDetailUiState.Error).message}")
                }
            }
        }
    }
}
