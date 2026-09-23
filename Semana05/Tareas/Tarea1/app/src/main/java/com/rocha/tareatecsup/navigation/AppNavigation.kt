package com.rocha.tareatecsup.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RadioButtonChecked
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
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
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    val darkPurple = Color(0xFF4A148C)
    val lightPurpleBg = Color(0xFFE1BEE7)
    val selectedItemBg = Color(0xFFF3E5F5)

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                // Encabezado del usuario con Avatar "JP"
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp, top = 28.dp, bottom = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(lightPurpleBg, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "JP",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = darkPurple
                            )
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column {
                        Text(
                            text = "Juan Pérez",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF212121)
                            )
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Paciente",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = Color.Gray
                            )
                        )
                    }
                }

                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Lista de ítems del menú lateral
                val navItems = listOf(
                    Triple("Inicio", Screen.Home.route, Screen.Home.route),
                    Triple("Mis citas", Screen.MyAppointments.route, Screen.MyAppointments.route),
                    Triple("Historial médico", Screen.MedicalHistory.route, Screen.MedicalHistory.route),
                    Triple("Perfil", Screen.Profile.route, Screen.Profile.route)
                )

                navItems.forEach { (label, route, targetRoute) ->
                    val isSelected = currentRoute == route

                    NavigationDrawerItem(
                        icon = {
                            Icon(
                                imageVector = if (isSelected) Icons.Default.RadioButtonChecked else Icons.Default.RadioButtonUnchecked,
                                contentDescription = null,
                                tint = if (isSelected) darkPurple else Color(0xFF424242)
                            )
                        },
                        label = {
                            Text(
                                text = label,
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) darkPurple else Color(0xFF424242)
                                )
                            )
                        },
                        selected = isSelected,
                        onClick = {
                            scope.launch { drawerState.close() }
                            navController.navigate(targetRoute) {
                                if (targetRoute == Screen.Home.route) {
                                    popUpTo(Screen.Home.route) { inclusive = true }
                                }
                            }
                        },
                        colors = NavigationDrawerItemDefaults.colors(
                            selectedContainerColor = selectedItemBg,
                            unselectedContainerColor = Color.Transparent
                        ),
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                    )
                }
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

            composable(
                route = Screen.Confirmation.route,
                arguments = listOf(
                    navArgument("doctorName") { type = NavType.StringType },
                    navArgument("date") { type = NavType.StringType },
                    navArgument("hour") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val rawName = backStackEntry.arguments?.getString("doctorName") ?: ""
                val rawDate = backStackEntry.arguments?.getString("date") ?: ""
                val rawHour = backStackEntry.arguments?.getString("hour") ?: ""

                val doctorName = try {
                    URLDecoder.decode(rawName, StandardCharsets.UTF_8.toString())
                } catch (_: Exception) { rawName }

                val date = try {
                    URLDecoder.decode(rawDate, StandardCharsets.UTF_8.toString())
                } catch (_: Exception) { rawDate }

                val hour = try {
                    URLDecoder.decode(rawHour, StandardCharsets.UTF_8.toString())
                } catch (_: Exception) { rawHour }

                ConfirmationScreen(
                    doctorName = doctorName.ifBlank { "Dra. Ana Torres" },
                    date = date,
                    hour = hour,
                    navController = navController
                )
            }

            composable(Screen.MyAppointments.route) { MyAppointmentsScreen() }
            composable(Screen.MedicalHistory.route) { MedicalHistoryScreen() }
            composable(Screen.Profile.route) { ProfileScreen() }
        }
    }
}
