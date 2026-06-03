package com.example.eventmaster.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.eventmaster.viewmodel.EventViewModel
import com.example.eventmaster.viewmodel.CategoryViewModel
import com.example.eventmaster.viewmodel.EventDetailUiState
import com.example.eventmaster.viewmodel.CategoriesUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventDetailScreen(
    eventId: Int,
    eventViewModel: EventViewModel = hiltViewModel(),
    categoryViewModel: CategoryViewModel = hiltViewModel()
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
                is EventDetailUiState.Loading -> {
                    // Círculo de carga eliminado
                }
                is EventDetailUiState.Success -> {
                    val event = (detailState as EventDetailUiState.Success).event

                    val categoryName = if (categoriesState is CategoriesUiState.Success) {
                        (categoriesState as CategoriesUiState.Success)
                            .categories.find { it.id == event.categoryId }?.name ?: "Desconocida"
                    } else {
                        "Cargando..."
                    }

                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(event.name, style = MaterialTheme.typography.headlineSmall)
                        Text("Categoría: $categoryName")
                        Text("Fecha: ${event.date}")
                        Text("Ubicación: ${event.location}")
                        Text("Descripción: ${event.description}")
                    }
                }
                is EventDetailUiState.Error -> {
                    Text("Error: ${(detailState as EventDetailUiState.Error).message}")
                }
            }
        }
    }
}
