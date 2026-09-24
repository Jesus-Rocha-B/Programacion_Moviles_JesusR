package com.rocha.tarea2.ui.screens

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.rocha.tarea2.navigation.Screen

private val DarkGreen = Color(0xFF00695C)
private val LightGreen = Color(0xFFE0F2F1)
private val GrayText = Color(0xFF757575)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClassDetailScreen(classId: Int, navController: NavController) {
    // Busca la clase seleccionada en la lista de ejemplo, según el id recibido
    val classItem = sampleClasses.find { it.id == classId }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            TopAppBar(
                title = {
                    // Flecha de volver pegada inmediatamente a la izquierda del título
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { navController.popBackStack() }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color.Black,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Detalle de clase",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            // Card grande con fondo verde muy claro e ícono de mancuerna centrado
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = LightGreen
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.FitnessCenter,
                        contentDescription = null,
                        tint = DarkGreen,
                        modifier = Modifier.size(64.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (classItem != null) {
                // Nombre de la clase en grande y negrita
                Text(
                    text = classItem.name,
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Horario, sala y duración en gris, separados por "·"
                val dateDetail = if (classItem.dayLabel == "Hoy") {
                    "${classItem.time} · ${classItem.room} · ${classItem.duration}"
                } else {
                    "${classItem.dayLabel}, ${classItem.time} · ${classItem.room} · ${classItem.duration}"
                }

                Text(
                    text = dateDetail,
                    style = MaterialTheme.typography.bodySmall,
                    color = GrayText
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Descripción de la clase en un párrafo
                Text(
                    text = classItem.description,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        lineHeight = 22.sp
                    ),
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Cupos disponibles en verde oscuro y negrita
                Text(
                    text = "${classItem.availableSpots} de ${classItem.totalSpots} cupos disponibles",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = DarkGreen
                    )
                )

                Spacer(modifier = Modifier.weight(1f))

                // Botón para reservar: vincula la reserva automáticamente a "Mis reservas"
                Button(
                    onClick = {
                        if (classItem != null) {
                            // Reducir cupos disponibles
                            if (classItem.availableSpots > 0) {
                                classItem.availableSpots -= 1
                            }
                            // Agregar a la lista de reservas si aún no está registrada
                            val alreadyReserved = sampleReservations.any {
                                it.classItem.id == classItem.id && it.status == "Confirmada"
                            }
                            if (!alreadyReserved) {
                                val dateLabel = if (classItem.dayLabel == "Hoy") "Hoy, ${classItem.time}" else "${classItem.dayLabel}, ${classItem.time}"
                                sampleReservations.add(
                                    0,
                                    Reservation(
                                        classItem = classItem,
                                        dateLabel = dateLabel,
                                        status = "Confirmada"
                                    )
                                )
                            }
                        }
                        navController.navigate(Screen.Confirmation.createRoute(classId))
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = DarkGreen,
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = "Reservar cupo",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            } else {
                Text(
                    text = "No se encontró la clase seleccionada.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = GrayText
                )
            }
        }
    }
}
