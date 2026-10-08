package com.rocha.saludplus.ui.notificaciones

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.rocha.saludplus.repository.Repositorio
import com.rocha.saludplus.ui.components.*

@Composable
fun NotificacionesScreen(navController: NavController) {
    // map convierte cada cita en un mensaje de recordatorio
    val mensajes = Repositorio.citasDelUsuario().map { cita ->
        val medico = Repositorio.obtenerMedico(cita.medicoId)
        "Recordatorio: tienes cita con ${medico?.nombre ?: "tu médico"} el ${formatearFecha(cita.fecha)} a las ${cita.hora}"
    }

    Scaffold(
        topBar = { BarraSuperior("Notificaciones", onVolver = { navController.popBackStack() }) }
    ) { padding ->
        if (mensajes.isEmpty()) {
            Box(modifier = Modifier.padding(padding)) {
                MensajeVacio("No tienes notificaciones")
            }
        } else {
            LazyColumn(
                modifier = Modifier.padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(mensajes) { mensaje ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Text(text = mensaje, modifier = Modifier.padding(16.dp))
                    }
                }
            }
        }
    }
}