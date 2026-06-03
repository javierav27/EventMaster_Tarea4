package com.example.eventmaster.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.eventmaster.R
import com.example.eventmaster.ui.components.EventCard
import com.example.eventmaster.viewmodel.EventMasterViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventsScreen(
    viewModel: EventMasterViewModel,
    categoryId: String,
    onAddEventClick: () -> Unit,
    onEventClick: (String) -> Unit,
    onNavigateBack: () -> Unit
) {
    // Usamos remember para mantener la referencia al StateFlow y collectAsState para observar los cambios
    val eventsStateFlow = remember(categoryId) { viewModel.getEventsByCategory(categoryId) }
    val events by eventsStateFlow.collectAsState()
    
    val categories by viewModel.categories.collectAsState()
    // Se corrige la comparación convirtiendo el id (Int) a String para que coincida con categoryId
    val categoryName = categories.find { it.id.toString() == categoryId }?.name ?: "Eventos"

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(categoryName) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddEventClick) {
                Icon(
                    painter = painterResource(R.drawable.ic_add),
                    contentDescription = "Agregar evento"
                )
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            if (events.isEmpty()) {
                item {
                    Text("No hay eventos en esta categoría")
                }
            } else {
                items(events) { event ->
                    // Se convierte el id (Int) a String para que coincida con la firma de onEventClick
                    EventCard(event = event, onClick = { onEventClick(event.id.toString()) })
                }
            }
        }
    }
}
