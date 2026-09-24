package com.rocha.tarea2.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.rocha.tarea2.navigation.Screen

private val DarkGreen = Color(0xFF00695C)
private val LightGreen = Color(0xFFE0F2F1)
private val LightGrayButton = Color(0xFFF0F0F0)
private val DarkGrayText = Color(0xFF333333)
private val GrayText = Color(0xFF757575)

@Composable
fun ConfirmationScreen(classId: Int, navController: NavController) {
    // Busca la clase reservada, usando el mismo id que llegó a ClassDetail
    val classItem = sampleClasses.find { it.id == classId }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Ícono de check dentro de un círculo con fondo verde muy claro y el ícono en verde oscuro
        Box(
            modifier = Modifier
                .size(96.dp)
                .background(color = LightGreen, shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Check,
                contentDescription = null,
                tint = DarkGreen,
                modifier = Modifier.size(48.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Título "¡Cupo reservado!" en negrita
        Text(
            text = "¡Cupo reservado!",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (classItem != null) {
            // Nombre de la clase en tamaño normal
            Text(
                text = classItem.name,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )
            )
            Spacer(modifier = Modifier.height(4.dp))
            // Fecha, hora y sala en gris, tamaño pequeño
            Text(
                text = "Hoy, ${classItem.time} · ${classItem.room}",
                style = MaterialTheme.typography.bodySmall,
                color = GrayText
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        // Botón "Ver mis reservas" con ancho completo, fondo gris claro y texto gris oscuro
        Button(
            onClick = { navController.navigate(Screen.Reservations.route) },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = LightGrayButton,
                contentColor = DarkGrayText
            )
        ) {
            Text(
                text = "Ver mis reservas",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}
