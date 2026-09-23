package com.rocha.tarea2.ui.screens
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.rocha.tarea2.navigation.Screen

@Composable
fun ConfirmationScreen(classId: Int, navController: NavController) {
    // Busca la clase reservada, usando el mismo id que llegó a ClassDetail
    val classItem = sampleClasses.find { it.id == classId }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Ícono grande de check, confirma la reserva visualmente
        Icon(
            imageVector = Icons.Filled.CheckCircle,
            contentDescription = null,
            modifier = Modifier.height(64.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "¡Cupo reservado!",
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.height(4.dp))

        if (classItem != null) {
            Text(
                text = classItem.name,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "Hoy, ${classItem.time} · ${classItem.room}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Navega a Reservations, sin popUpTo porque sí es válido volver atrás
        Button(
            onClick = { navController.navigate(Screen.Reservations.route) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Ver mis reservas")
        }
    }
}