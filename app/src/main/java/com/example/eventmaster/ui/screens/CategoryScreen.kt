package com.example.eventmaster.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.eventmaster.viewmodel.CategoryViewModel
import com.example.eventmaster.viewmodel.CategoriesUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryScreen(
    viewModel: CategoryViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var categoryName by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Gestionar Categorías") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                if (categoryName.isNotBlank()) {
                    viewModel.createCategory(categoryName)
                    categoryName = ""
                } else {
                    showError = true
                }
            }) {
                Text("Agregar")
            }
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            OutlinedTextField(
                value = categoryName,
                onValueChange = { categoryName = it; showError = false },
                label = { Text("Nombre categoría") },
                isError = showError,
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            )
            if (showError) {
                Text("El nombre no puede estar vacío", color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(horizontal = 16.dp))
            }
            when (uiState) {
                is CategoriesUiState.Loading -> {
                    // Carga silenciosa
                }
                is CategoriesUiState.Success -> {
                    LazyColumn {
                        items((uiState as CategoriesUiState.Success).categories) { category ->
                            ListItem(
                                headlineContent = { Text(category.name) }
                            )
                        }
                    }
                }
                is CategoriesUiState.Error -> {
                    Text("Error: ${(uiState as CategoriesUiState.Error).message}", modifier = Modifier.padding(16.dp))
                }
            }
        }
    }
}
