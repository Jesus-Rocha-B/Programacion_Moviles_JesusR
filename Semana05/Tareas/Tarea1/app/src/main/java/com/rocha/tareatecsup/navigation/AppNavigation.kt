package com.rocha.tareatecsup.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.rocha.tareatecsup.ui.screens.DoctorProfileScreen
import com.rocha.tareatecsup.ui.screens.HomeScreen

@Composable
fun AppNavigation() {
    // Variable que controla el historial de pantallas
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) { HomeScreen(navController) }

        // Ruta con parámetro: id del médico
        composable(
            route = Screen.DoctorProfile.route,
            arguments = listOf(navArgument("doctorId") {
                type = NavType.IntType
                defaultValue = 0
            })
        ) { backStackEntry ->
            val doctorId = backStackEntry.arguments?.getInt("doctorId") ?: 0
            DoctorProfileScreen(doctorId = doctorId, navController = navController)
        }
    }
}