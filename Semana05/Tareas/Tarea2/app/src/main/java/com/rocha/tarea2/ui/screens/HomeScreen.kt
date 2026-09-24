package com.rocha.tarea2.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.rocha.tarea2.navigation.Screen

// Definición de colores principales
private val DarkGreen = Color(0xFF00695C)
private val LightGreen = Color(0xFFE0F2F1)
private val LightGray = Color(0xFFF8F9FA)
private val UnselectedChipBg = Color(0xFFF0F0F0)
private val UnselectedChipText = Color(0xFF666666)
private val GrayText = Color(0xFF757575)

// Modelo de una clase de gimnasio
data class ClassItem(
    val id: Int,
    val name: String,
    val time: String,
    val room: String,
    val duration: String,
    val description: String,
    var availableSpots: Int,
    val totalSpots: Int,
    val filterGroup: String = "Hoy", // "Hoy" o "Esta semana"
    val dayLabel: String = "Hoy"     // "Hoy", "Jueves", "Viernes", etc.
)

// Modelo de una reserva ya confirmada
data class Reservation(
    val classItem: ClassItem,
    val dateLabel: String,
    val status: String // "Confirmada" o "Completada"
)

// Lista de clases reactiva y modificable para actualizar cupos disponibles
val sampleClasses = mutableStateListOf(
    ClassItem(
        id = 1,
        name = "Yoga funcional",
        time = "7:00 am",
        room = "Sala 2",
        duration = "50 min",
        description = "Sesión de yoga enfocada en movilidad y equilibrio.",
        availableSpots = 6,
        totalSpots = 12,
        filterGroup = "Hoy",
        dayLabel = "Hoy"
    ),
    ClassItem(
        id = 2,
        name = "Cross Training",
        time = "6:00 pm",
        room = "Sala 1",
        duration = "45 min",
        description = "Entrenamiento funcional de alta intensidad. Cupos limitados.",
        availableSpots = 8,
        totalSpots = 12,
        filterGroup = "Hoy",
        dayLabel = "Hoy"
    ),
    ClassItem(
        id = 3,
        name = "Spinning",
        time = "7:30 pm",
        room = "Sala 3",
        duration = "40 min",
        description = "Rutina de ciclismo indoor con cambios de ritmo.",
        availableSpots = 4,
        totalSpots = 12,
        filterGroup = "Esta semana",
        dayLabel = "Jueves"
    )
)

// Lista de reservas reactiva para que se actualice automáticamente al reservar
val sampleReservations = mutableStateListOf(
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

    // Filtrado de clases según el chip seleccionado
    val displayedClasses = sampleClasses.filter { classItem ->
        if (selectedFilter == "Hoy") {
            classItem.filterGroup == "Hoy"
        } else {
            true // En "Esta semana" se muestran todas las clases de la semana
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Encabezado superior verde oscuro como rectángulo recto (sin bordes redondeados abajo)
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = DarkGreen,
            shape = RectangleShape
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 20.dp)
            ) {
                Text(
                    text = "TECSUP Fit",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Hola, Diego",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Color.White.copy(alpha = 0.9f)
                    )
                )
            }
        }

        // Contenido principal debajo del encabezado
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Chips de filtro: "Hoy" y "Esta semana"
            LazyRow {
                items(listOf("Hoy", "Esta semana")) { filter ->
                    val isSelected = selectedFilter == filter
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedFilter = filter },
                        label = {
                            Text(
                                text = filter,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = DarkGreen,
                            selectedLabelColor = Color.White,
                            containerColor = UnselectedChipBg,
                            labelColor = UnselectedChipText
                        ),
                        border = null,
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier.padding(end = 8.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Título "Clases disponibles" en color negro intenso
            Text(
                text = "Clases disponibles",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color.Black
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Lista de clases filtradas
            LazyColumn {
                items(displayedClasses) { classItem ->
                    // Formato de hora y día según el filtro activo
                    val dateSubtitle = if (selectedFilter == "Hoy") {
                        "${classItem.time} · ${classItem.room}"
                    } else {
                        "${classItem.dayLabel}, ${classItem.time} · ${classItem.room}"
                    }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                            .clickable {
                                navController.navigate(Screen.ClassDetail.createRoute(classItem.id))
                            },
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = LightGray
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Ícono a la izquierda dentro de un rectángulo con esquinas redondeadas
                            Box(
                                modifier = Modifier
                                    .size(width = 56.dp, height = 44.dp)
                                    .background(
                                        color = LightGreen,
                                        shape = RoundedCornerShape(12.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.FitnessCenter,
                                    contentDescription = null,
                                    tint = DarkGreen,
                                    modifier = Modifier.size(24.dp)
                                )
                            }

                            Spacer(modifier = Modifier.size(16.dp))

                            // Nombre de la clase e información de día/hora/sala
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = classItem.name,
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = Color.Black
                                    )
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = dateSubtitle,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = GrayText
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
