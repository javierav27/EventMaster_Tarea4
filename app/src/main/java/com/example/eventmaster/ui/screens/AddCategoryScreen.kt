package com.example.eventmaster.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.eventmaster.R
import com.example.eventmaster.ui.components.CustomTextField
import com.example.eventmaster.viewmodel.EventMasterViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCategoryScreen(
    viewModel: EventMasterViewModel,
    onNavigateBack: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }

    Scaffold(
        topBar = { TopAppBar(title = { Text(stringResource(R.string.add_category_title)) }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            CustomTextField(
                value = name,
                onValueChange = { name = it; showError = false },
                label = stringResource(R.string.category_name_label),
                isError = showError && name.isBlank(),
                errorMessage = if (showError) stringResource(R.string.category_name_error) else null
            )
            Button(
                onClick = {
                    if (name.isNotBlank()) {
                        viewModel.addCategory(name)
                        onNavigateBack()
                    } else {
                        showError = true
                    }
                }
            ) {
                Text(stringResource(R.string.save))
            }
        }
    }
}
