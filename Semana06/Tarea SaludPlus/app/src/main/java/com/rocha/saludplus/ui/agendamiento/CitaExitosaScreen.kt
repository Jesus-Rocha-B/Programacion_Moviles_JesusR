package com.rocha.saludplus.ui.agendamiento

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.rocha.saludplus.navigation.Rutas
import com.rocha.saludplus.repository.Repositorio
import com.rocha.saludplus.ui.components.*

@Composable
fun CitaExitosaScreen(citaId: Int, navController: NavController) {
    val cita = Repositorio.obtenerCita(citaId)
    val medico = cita?.let { Repositorio.obtenerMedico(it.medicoId) }

    Scaffold { padding ->
        // El contenido se centra
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "¡Cita agendada!",
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Tu cita fue registrada con éxito",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(24.dp))
            // Resumen de la cita
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    FilaDato("Médico", medico?.nombre ?: "")
                    FilaDato("Fecha", formatearFecha(cita?.fecha ?: ""))
                    FilaDato("Hora", cita?.hora ?: "")
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            BotonPrincipal(
                texto = "Ver mis citas",
                onClick = {
                    navController.navigate(Rutas.MIS_CITAS) {
                        popUpTo(Rutas.HOME)
                    }
                }
            )
            Spacer(modifier = Modifier.height(12.dp))
            // Vuelve al inicio y limpia el historial
            BotonSecundario(
                texto = "Ir al inicio",
                onClick = {
                    navController.navigate(Rutas.HOME) {
                        popUpTo(Rutas.HOME) { inclusive = true }
                    }
                }
            )
        }
    }
}