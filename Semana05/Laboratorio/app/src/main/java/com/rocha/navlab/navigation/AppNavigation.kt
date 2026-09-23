package com.rocha.navlab.navigation
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.rocha.navlab.ui.screens.DetailScreen
import com.rocha.navlab.ui.screens.HomeScreen
import com.rocha.navlab.ui.screens.ListScreen
import com.rocha.navlab.ui.screens.ProfileScreen

@Composable
fun AppNavigation() {
    // Variable que controla el historial de pantallas
    val navController = rememberNavController()
    // Mapa de Rutas
    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) { HomeScreen(navController) }
        composable(Screen.List.route) { ListScreen(navController) }
        composable(Screen.Profile.route) { ProfileScreen(navController) }
        // Ruta con parametro
        composable(
            route = Screen.Detail.route,
            arguments = listOf(navArgument("itemId") { type = NavType.IntType
            defaultValue = 0})
        ) { backStackEntry ->
            val itemId = backStackEntry.arguments?.getInt("itemId") ?: 0
            DetailScreen(itemId = itemId, navController = navController)
        }
    }
}