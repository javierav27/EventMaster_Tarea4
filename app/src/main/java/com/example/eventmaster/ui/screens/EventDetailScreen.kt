
package com.example.eventmaster.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.eventmaster.ui.viewmodels.EventViewModel
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventDetailScreen(
    eventId: Int,
    eventViewModel: EventViewModel = hiltViewModel(),
    categoryViewModel: com.example.eventmaster.ui.viewmodels.CategoryViewModel = hiltViewModel()
) {
    val detailState by eventViewModel.detailState.collectAsState()
    val categoriesState by categoryViewModel.uiState.collectAsState()

    LaunchedEffect(eventId) {
        eventViewModel.loadEventDetail(eventId)
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Detalle del evento") }) }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            when (detailState) {
                is com.example.eventmaster.ui.viewmodels.EventDetailUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.fillMaxSize())
                }
                is com.example.eventmaster.ui.viewmodels.EventDetailUiState.Success -> {
                    val event = (detailState as com.example.eventmaster.ui.viewmodels.EventDetailUiState.Success).event


                    val categoryName = if (categoriesState is com.example.eventmaster.ui.viewmodels.CategoriesUiState.Success) {
                        (categoriesState as com.example.eventmaster.ui.viewmodels.CategoriesUiState.Success)
                            .categories.find { it.id == event.categoryId }?.name ?: "Desconocida"
                    } else {
                        "Cargando..."
                    }

                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(event.name, style = MaterialTheme.typography.headlineSmall)
                        Text("Categoría: $categoryName") // Usamos el nombre encontrado
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