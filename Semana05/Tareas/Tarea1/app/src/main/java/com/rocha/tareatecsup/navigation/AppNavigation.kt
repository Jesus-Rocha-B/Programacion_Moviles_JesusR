package com.rocha.tareatecsup.navigation
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.rocha.tareatecsup.ui.screens.AppointmentScreen
import com.rocha.tareatecsup.ui.screens.ConfirmationScreen
import com.rocha.tareatecsup.ui.screens.DoctorProfileScreen
import com.rocha.tareatecsup.ui.screens.HomeScreen
import com.rocha.tareatecsup.ui.screens.MedicalHistoryScreen
import com.rocha.tareatecsup.ui.screens.MyAppointmentsScreen
import com.rocha.tareatecsup.ui.screens.ProfileScreen
import kotlinx.coroutines.launch

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Text("Juan Pérez", modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.titleMedium)
                HorizontalDivider()

                NavigationDrawerItem(
                    label = { Text("Inicio") },
                    selected = currentRoute == Screen.Home.route,
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Home.route) { inclusive = true }
                        }
                    },
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
                NavigationDrawerItem(
                    label = { Text("Mis citas") },
                    selected = currentRoute == Screen.MyAppointments.route,
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate(Screen.MyAppointments.route)
                    },
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
                NavigationDrawerItem(
                    label = { Text("Historial médico") },
                    selected = currentRoute == Screen.MedicalHistory.route,
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate(Screen.MedicalHistory.route)
                    },
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
                NavigationDrawerItem(
                    label = { Text("Perfil") },
                    selected = currentRoute == Screen.Profile.route,
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate(Screen.Profile.route)
                    },
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
            }
        }
    ) {
        NavHost(navController = navController, startDestination = Screen.Home.route) {
            composable(Screen.Home.route) {
                HomeScreen(navController) { scope.launch { drawerState.open() } }
            }

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

            composable(Screen.MyAppointments.route) { MyAppointmentsScreen() }
            composable(Screen.MedicalHistory.route) { MedicalHistoryScreen() }
            composable(Screen.Profile.route) { ProfileScreen() }
        }
    }
}