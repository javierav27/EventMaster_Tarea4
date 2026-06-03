package com.example.eventmaster

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.eventmaster.navigation.EventMasterNavHost
import com.example.eventmaster.ui.theme.EventMasterTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EventMasterTheme {
                val navController = rememberNavController()
                val currentBackStack by navController.currentBackStackEntryAsState()
                val currentRoute = currentBackStack?.destination?.route
                Scaffold(
                    bottomBar = {
                        if (currentRoute != "detail/{eventId}") {
                            NavigationBar {
                                listOf("home", "categories", "addEvent").forEach { route ->
                                    NavigationBarItem(
                                        selected = currentRoute == route,
                                        onClick = { navController.navigate(route) },
                                        label = { Text(stringResource(id = getLabelRes(route))) },
                                        icon = {
                                            Icon(
                                                imageVector = getIcon(route),
                                                contentDescription = stringResource(id = getLabelRes(route))
                                            )
                                        }
                                    )
                                }
                            }
                        }
                    }
                ) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        EventMasterNavHost(navController = navController)
                    }
                }
            }
        }
    }

    private fun getLabelRes(route: String): Int = when (route) {
        "home" -> R.string.home
        "categories" -> R.string.categories
        "addEvent" -> R.string.add_event
        else -> 0
    }

    private fun getIcon(route: String): ImageVector = when (route) {
        "home" -> Icons.Default.Home
        "categories" -> Icons.AutoMirrored.Filled.List
        "addEvent" -> Icons.Default.Add
        else -> Icons.Default.Home
    }
}
