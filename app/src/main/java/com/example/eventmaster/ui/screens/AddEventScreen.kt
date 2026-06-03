
package com.example.eventmaster.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.eventmaster.data.model.EventRequest
import com.example.eventmaster.ui.viewmodels.CategoryViewModel
import com.example.eventmaster.ui.viewmodels.EventViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun AddEventScreen(
    onEventCreated: () -> Unit,
    categoryViewModel: CategoryViewModel = hiltViewModel(),
    eventViewModel: EventViewModel = hiltViewModel()
) {
    val categoriesState by categoryViewModel.uiState.collectAsState()
    var selectedCategoryId by remember { mutableStateOf<Int?>(null) }
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var date by remember { mutableStateOf(LocalDate.now().toString()) }
    var location by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Nuevo Evento") }) }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues).padding(16.dp)) {
            OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Nombre") })
            OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text("Descripción") })
            OutlinedTextField(value = date, onValueChange = { date = it }, label = { Text("Fecha (YYYY-MM-DD)") })
            OutlinedTextField(value = location, onValueChange = { location = it }, label = { Text("Ubicación") })
            when (categoriesState) {
                is com.example.eventmaster.ui.viewmodels.CategoriesUiState.Success -> {
                    val categories = (categoriesState as com.example.eventmaster.ui.viewmodels.CategoriesUiState.Success).categories
                    DropdownMenuBox(
                        selectedCategoryId = selectedCategoryId,
                        categories = categories,
                        onCategorySelected = { selectedCategoryId = it }
                    )
                }
                else -> CircularProgressIndicator()
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                if (selectedCategoryId != null && name.isNotBlank() && description.isNotBlank() && date.isNotBlank() && location.isNotBlank()) {
                    val eventRequest = EventRequest(
                        name = name,
                        description = description,
                        date = LocalDate.parse(date, DateTimeFormatter.ISO_DATE),
                        location = location,
                        category_id = selectedCategoryId!!
                    )
                    eventViewModel.createEvent(eventRequest) {
                        onEventCreated()
                    }
                } else {
                    showError = true
                }
            }) {
                Text("Guardar Evento")
            }
            if (showError) {
                Text("Todos los campos son obligatorios", color = MaterialTheme.colorScheme.error)
            }
        }
    }
}

@Composable
fun DropdownMenuBox(selectedCategoryId: Int?, categories: List<com.example.eventmaster.data.model.Category>, onCategorySelected: (Int) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val selectedCategory = categories.find { it.id == selectedCategoryId }
    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it }) {
        TextField(
            value = selectedCategory?.name ?: "",
            onValueChange = {},
            readOnly = true,
            label = { Text("Categoría") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) }
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            categories.forEach { category ->
                DropdownMenuItem(
                    text = { Text(category.name) },
                    onClick = {
                        onCategorySelected(category.id)
                        expanded = false
                    }
                )
            }
        }
    }
}