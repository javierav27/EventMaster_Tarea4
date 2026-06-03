// navigation/NavGraph.kt
package com.example.eventmaster.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.eventmaster.ui.screens.*

@Composable
fun EventMasterNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(onEventClick = { eventId ->
                navController.navigate("detail/$eventId")
            })
        }
        composable("categories") {
            CategoryScreen()
        }
        composable("addEvent") {
            AddEventScreen(onEventCreated = { navController.popBackStack() })
        }
        composable(
            "detail/{eventId}",
            arguments = listOf(navArgument("eventId") { type = NavType.IntType })
        ) { backStackEntry ->
            val eventId = backStackEntry.arguments?.getInt("eventId") ?: return@composable
            EventDetailScreen(eventId = eventId)
        }
    }
}