package com.rocha.tareatecsup.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class AppointmentItem(
    val doctorName: String,
    val dateTime: String,
    val status: String
)

@Composable
fun MyAppointmentsScreen() {
    val appointments = listOf(
        AppointmentItem("Dra. Ana Torres", "Viernes 27, 10:30 am", "Confirmada"),
        AppointmentItem("Dr. Luis Vega", "Miércoles 15, 3:00 pm", "Completada")
    )

    val darkPurple = Color(0xFF4A148C)
    val cardBg = Color(0xFFF3F3F5)

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            // Título principal
            Text(
                text = "Mis citas",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF212121)
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Lista de tarjetas de citas
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(appointments) { item ->
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = cardBg,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(IntrinsicSize.Min)
                        ) {
                            // Barra indicadora vertical morada en el extremo izquierdo
                            Box(
                                modifier = Modifier
                                    .width(4.dp)
                                    .fillMaxHeight()
                                    .background(
                                        color = darkPurple,
                                        shape = RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp)
                                    )
                            )

                            // Contenido interno de la tarjeta
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 20.dp, vertical = 16.dp)
                            ) {
                                Text(
                                    text = item.doctorName,
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF212121)
                                    )
                                )

                                Spacer(modifier = Modifier.height(4.dp))

                                Text(
                                    text = item.dateTime,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color = Color.Gray
                                    )
                                )

                                Spacer(modifier = Modifier.height(10.dp))

                                // Chip / Badge de estado
                                val isConfirmed = item.status == "Confirmada"
                                Surface(
                                    shape = RoundedCornerShape(50),
                                    color = if (isConfirmed) Color(0xFFE8F5E9) else Color(0xFFE0E0E0)
                                ) {
                                    Box(
                                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = item.status,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isConfirmed) Color(0xFF00897B) else Color(0xFF616161)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
