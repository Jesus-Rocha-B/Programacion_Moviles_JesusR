package com.rocha.tareatecsup.ui.screens
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

data class Doctor(
    val id: Int,
    val name: String,
    val specialty: String,
    val rating: Double
)

@Composable
fun HomeScreen(navController: NavController) {
    // Especialidades disponibles (mínimo 2)
    val specialties = listOf("Cardiología", "Pediatría", "Dermatología")

    // Lista de médicos disponibles (mínimo 3)
    val doctors = listOf(
        Doctor(1, "Dra. Ana Torres", "Cardiología", 4.9),
        Doctor(2, "Dr. Luis Vega", "Pediatría", 4.7),
        Doctor(3, "Dra. Rosa Díaz", "Dermatología", 4.8)
    )

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(
            text = "Clínica Salud+",
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Especialidades",
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Fila horizontal de chips de especialidad
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

        Text(
            text = "Médicos disponibles",
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Columna vertical con la lista de médicos
        LazyColumn {
            items(doctors.size) { index ->
                val doctor = doctors[index]
                ListItem(
                    headlineContent = { Text(doctor.name) },
                    supportingContent = { Text(doctor.specialty) },
                    trailingContent = { Text("⭐ ${doctor.rating}") }
                )
                HorizontalDivider()
            }
        }
    }
}