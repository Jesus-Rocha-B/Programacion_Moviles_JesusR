package com.rocha.tarea2.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

private val DarkGreen = Color(0xFF00695C)
private val LightGreenTag = Color(0xFFE0F2F1)
private val LightGrayCard = Color(0xFFF8F9FA)
private val GrayBorderCompleted = Color(0xFFB0BEC5)
private val GrayTagCompleted = Color(0xFFE0E0E0)
private val DarkGrayTextCompleted = Color(0xFF616161)
private val GrayText = Color(0xFF757575)

@Composable
fun ReservationsScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 20.dp, vertical = 20.dp)
    ) {
        // Título "Mis reservas" en negrita arriba de la pantalla
        Text(
            text = "Mis reservas",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Lista de reservas
        LazyColumn {
            items(sampleReservations) { reservation ->
                val isConfirmed = reservation.status == "Confirmada"
                val borderColor = if (isConfirmed) DarkGreen else GrayBorderCompleted
                val tagBgColor = if (isConfirmed) LightGreenTag else GrayTagCompleted
                val tagTextColor = if (isConfirmed) DarkGreen else DarkGrayTextCompleted

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = LightGrayCard
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(IntrinsicSize.Min)
                    ) {
                        // Borde izquierdo grueso de color
                        Box(
                            modifier = Modifier
                                .width(6.dp)
                                .fillMaxHeight()
                                .background(borderColor)
                        )

                        // Contenido dentro de la Card
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            // Nombre de la clase en negrita
                            Text(
                                text = reservation.classItem.name,
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )
                            )

                            Spacer(modifier = Modifier.height(2.dp))

                            // Fecha y hora en gris, tamaño pequeño
                            Text(
                                text = reservation.dateLabel,
                                style = MaterialTheme.typography.bodySmall,
                                color = GrayText
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            // Etiqueta pequeña con el estado
                            Surface(
                                color = tagBgColor,
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = reservation.status,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.SemiBold,
                                        color = tagTextColor,
                                        fontSize = 12.sp
                                    ),
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
