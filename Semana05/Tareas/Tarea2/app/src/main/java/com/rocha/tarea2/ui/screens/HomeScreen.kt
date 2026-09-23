package com.rocha.tarea2.ui.screens
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material3.Card
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.rocha.tarea2.navigation.Screen

// Modelo de una clase de gimnasio
data class ClassItem(
    val id: Int,
    val name: String,
    val time: String,
    val room: String,
    val duration: String,
    val description: String,
    val availableSpots: Int,
    val totalSpots: Int
)

// Modelo de una reserva ya confirmada
data class Reservation(
    val classItem: ClassItem,
    val dateLabel: String,
    val status: String // "Confirmada" o "Completada"
)

// Datos de ejemplo: las 3 clases mínimas que piden los requisitos
val sampleClasses = listOf(
    ClassItem(
        id = 1,
        name = "Yoga funcional",
        time = "7:00 am",
        room = "Sala 2",
        duration = "50 min",
        description = "Sesión de yoga enfocada en movilidad y equilibrio.",
        availableSpots = 6,
        totalSpots = 12
    ),
    ClassItem(
        id = 2,
        name = "Cross Training",
        time = "6:00 pm",
        room = "Sala 1",
        duration = "45 min",
        description = "Entrenamiento funcional de alta intensidad. Cupos limitados.",
        availableSpots = 8,
        totalSpots = 12
    ),
    ClassItem(
        id = 3,
        name = "Spinning",
        time = "7:30 pm",
        room = "Sala 3",
        duration = "40 min",
        description = "Rutina de ciclismo indoor con cambios de ritmo.",
        availableSpots = 4,
        totalSpots = 12
    )
)

// Datos de ejemplo: reservas ya hechas por el usuario
val sampleReservations = listOf(
    Reservation(
        classItem = sampleClasses[1],
        dateLabel = "Hoy, 6:00 pm",
        status = "Confirmada"
    ),
    Reservation(
        classItem = sampleClasses[0],
        dateLabel = "Ayer, 7:00 am",
        status = "Completada"
    )
)

@Composable
fun HomeScreen(navController: NavController) {
    // Controla qué chip está seleccionado: "Hoy" o "Esta semana"
    var selectedFilter by remember { mutableStateOf("Hoy") }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(
            text = "TECSUP Fit",
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = "Hola, Diego",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Chips de filtro, mínimo 2: "Hoy" y "Esta semana"
        LazyRow {
            items(listOf("Hoy", "Esta semana")) { filter ->
                FilterChip(
                    selected = selectedFilter == filter,
                    onClick = { selectedFilter = filter },
                    label = { Text(filter) },
                    modifier = Modifier.padding(end = 8.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Clases disponibles",
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Lista de clases, mínimo 3, cada una navega al detalle
        LazyColumn {
            items(sampleClasses) { classItem ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                        .clickable {
                            navController.navigate(Screen.ClassDetail.createRoute(classItem.id))
                        },
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.FitnessCenter,
                            contentDescription = null,
                            modifier = Modifier.padding(end = 12.dp)
                        )
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(end = 8.dp)
                        ) {
                            Text(
                                text = classItem.name,
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Text(
                                text = "${classItem.time} · ${classItem.room}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}