package com.rocha.tarea2.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.FitnessCenter
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.rocha.tarea2.ui.screens.ClassDetailScreen
import com.rocha.tarea2.ui.screens.ConfirmationScreen
import com.rocha.tarea2.ui.screens.HomeScreen
import com.rocha.tarea2.ui.screens.ProfileScreen
import com.rocha.tarea2.ui.screens.ReservationsScreen
import com.rocha.tarea2.ui.screens.RoutinesScreen

// Pantallas donde SÍ se muestra el bottomBar
private val bottomBarRoutes = listOf(
    Screen.Home.route,
    Screen.Reservations.route,
    Screen.Routines.route,
    Screen.Profile.route
)

private val DarkGreen = Color(0xFF00695C)
private val GrayColor = Color(0xFF757575)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    // Ruta actual, para saber si mostrar el bottomBar y qué tab resaltar
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            // Solo se dibuja el bottomBar si la ruta actual está en la lista
            if (currentRoute in bottomBarRoutes) {
                NavigationBar(
                    containerColor = Color.White
                ) {
                    val itemColors = NavigationBarItemDefaults.colors(
                        selectedIconColor = DarkGreen,
                        selectedTextColor = DarkGreen,
                        unselectedIconColor = GrayColor,
                        unselectedTextColor = GrayColor,
                        indicatorColor = Color.Transparent
                    )

                    NavigationBarItem(
                        selected = currentRoute == Screen.Home.route,
                        onClick = { navController.navigate(Screen.Home.route) },
                        icon = { Icon(Icons.Outlined.Home, contentDescription = "Inicio") },
                        label = { Text("Inicio") },
                        colors = itemColors
                    )
                    NavigationBarItem(
                        selected = currentRoute == Screen.Reservations.route,
                        onClick = { navController.navigate(Screen.Reservations.route) },
                        icon = { Icon(Icons.Outlined.CalendarMonth, contentDescription = "Reservas") },
                        label = { Text("Reservas") },
                        colors = itemColors
                    )
                    NavigationBarItem(
                        selected = currentRoute == Screen.Routines.route,
                        onClick = { navController.navigate(Screen.Routines.route) },
                        icon = { Icon(Icons.Outlined.FitnessCenter, contentDescription = "Rutinas") },
                        label = { Text("Rutinas") },
                        colors = itemColors
                    )
                    NavigationBarItem(
                        selected = currentRoute == Screen.Profile.route,
                        onClick = { navController.navigate(Screen.Profile.route) },
                        icon = { Icon(Icons.Outlined.Person, contentDescription = "Perfil") },
                        label = { Text("Perfil") },
                        colors = itemColors
                    )
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(padding),
            enterTransition = {
                slideInHorizontally(initialOffsetX = { 300 }, animationSpec = tween(280)) + fadeIn(animationSpec = tween(280))
            },
            exitTransition = {
                slideOutHorizontally(targetOffsetX = { -300 }, animationSpec = tween(280)) + fadeOut(animationSpec = tween(280))
            },
            popEnterTransition = {
                slideInHorizontally(initialOffsetX = { -300 }, animationSpec = tween(280)) + fadeIn(animationSpec = tween(280))
            },
            popExitTransition = {
                slideOutHorizontally(targetOffsetX = { 300 }, animationSpec = tween(280)) + fadeOut(animationSpec = tween(280))
            }
        ) {
            composable(Screen.Home.route) { HomeScreen(navController) }
            composable(Screen.Reservations.route) { ReservationsScreen(navController) }
            composable(Screen.Routines.route) { RoutinesScreen(navController) }
            composable(Screen.Profile.route) { ProfileScreen(navController) }

            composable(
                route = Screen.ClassDetail.route,
                arguments = listOf(navArgument("classId") { type = NavType.IntType })
            ) { backStackEntry ->
                val classId = backStackEntry.arguments?.getInt("classId") ?: 0
                ClassDetailScreen(classId = classId, navController = navController)
            }

            composable(
                route = Screen.Confirmation.route,
                arguments = listOf(navArgument("classId") { type = NavType.IntType })
            ) { backStackEntry ->
                val classId = backStackEntry.arguments?.getInt("classId") ?: 0
                ConfirmationScreen(classId = classId, navController = navController)
            }
        }
    }
}
