package com.rocha.tareatecsup.navigation
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.rocha.tareatecsup.ui.screens.AppointmentScreen
import com.rocha.tareatecsup.ui.screens.ConfirmationScreen
import com.rocha.tareatecsup.ui.screens.DoctorProfileScreen
import com.rocha.tareatecsup.ui.screens.HomeScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) { HomeScreen(navController) }

        composable(
            route = Screen.DoctorProfile.route,
            arguments = listOf(navArgument("doctorId") { type = NavType.IntType; defaultValue = 0 })
        ) { backStackEntry ->
            val doctorId = backStackEntry.arguments?.getInt("doctorId") ?: 0
            DoctorProfileScreen(doctorId = doctorId, navController = navController)
        }

        composable(
            route = Screen.Appointment.route,
            arguments = listOf(navArgument("doctorId") { type = NavType.IntType; defaultValue = 0 })
        ) { backStackEntry ->
            val doctorId = backStackEntry.arguments?.getInt("doctorId") ?: 0
            AppointmentScreen(doctorId = doctorId, navController = navController)
        }

        composable(Screen.Confirmation.route) { backStackEntry ->
            val doctorName = backStackEntry.arguments?.getString("doctorName") ?: ""
            val date = backStackEntry.arguments?.getString("date") ?: ""
            val hour = backStackEntry.arguments?.getString("hour") ?: ""
            ConfirmationScreen(doctorName = doctorName, date = date, hour = hour, navController = navController)
        }
    }
}