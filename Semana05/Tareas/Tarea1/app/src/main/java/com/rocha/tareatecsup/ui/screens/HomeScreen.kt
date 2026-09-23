package com.rocha.tareatecsup.ui.screens
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.rocha.tareatecsup.navigation.Screen
import kotlinx.coroutines.launch

data class Doctor(
    val id: Int,
    val name: String,
    val specialty: String,
    val rating: Double
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController, onMenuClick: () -> Unit) {
    val specialties = listOf("Cardiología", "Pediatría", "Dermatología")
    val doctors = listOf(
        Doctor(1, "Dra. Ana Torres", "Cardiología", 4.9),
        Doctor(2, "Dr. Luis Vega", "Pediatría", 4.7),
        Doctor(3, "Dra. Rosa Díaz", "Dermatología", 4.8)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Clínica Salud+") },
                navigationIcon = {
                    IconButton(onClick = onMenuClick) {
                        Icon(Icons.Filled.Menu, contentDescription = "Menú")
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            Text("Especialidades", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))

            LazyRow {
                items(specialties.size) { index ->
                    AssistChip(
                        onClick = { },
                        label = { Text(specialties[index]) },
                        modifier = Modifier.padding(end = 8.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            Text("Médicos disponibles", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn {
                items(doctors.size) { index ->
                    val doctor = doctors[index]
                    ListItem(
                        headlineContent = { Text(doctor.name) },
                        supportingContent = { Text(doctor.specialty) },
                        trailingContent = { Text("⭐ ${doctor.rating}") },
                        modifier = Modifier.clickable {
                            navController.navigate(Screen.DoctorProfile.createRoute(doctor.id))
                        }
                    )
                    HorizontalDivider()
                }
            }
        }
    }
}