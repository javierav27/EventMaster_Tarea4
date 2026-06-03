package com.example.eventmaster.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eventmaster.models.Category
import com.example.eventmaster.data.repository.CategoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val repository: CategoryRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<CategoriesUiState>(CategoriesUiState.Loading)
    val uiState: StateFlow<CategoriesUiState> = _uiState.asStateFlow()

    init {
        loadCategories()
    }

    fun loadCategories() {
        viewModelScope.launch {
            _uiState.value = CategoriesUiState.Loading
            try {
                val categories = repository.getCategories()
                _uiState.value = CategoriesUiState.Success(categories)
            } catch (e: Exception) {
                _uiState.value = CategoriesUiState.Error(e.message ?: "Error desconocido")
            }
        }
    }

    fun createCategory(name: String) {
        viewModelScope.launch {
            try {
                repository.createCategory(name)
                loadCategories()
            } catch (e: Exception) {
                _uiState.value = CategoriesUiState.Error(e.message ?: "Error al crear categoría")
            }
        }
    }
}

sealed class CategoriesUiState {
    object Loading : CategoriesUiState()
    data class Success(val categories: List<Category>) : CategoriesUiState()
    data class Error(val message: String) : CategoriesUiState()
}
