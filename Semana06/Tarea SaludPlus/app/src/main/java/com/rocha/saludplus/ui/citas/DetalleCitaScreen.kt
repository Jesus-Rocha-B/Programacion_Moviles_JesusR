package com.rocha.saludplus.ui.citas

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.rocha.saludplus.repository.Repositorio
import com.rocha.saludplus.ui.components.*

@Composable
fun DetalleCitaScreen(citaId: Int, navController: NavController) {
    val cita = Repositorio.obtenerCita(citaId)
    val medico = cita?.let { Repositorio.obtenerMedico(it.medicoId) }
    val especialidad = medico?.let { Repositorio.obtenerEspecialidad(it.especialidadId) }
    // Controla si se muestra el cuadro de confirmación
    var mostrarDialogo by remember { mutableStateOf(false) }

    Scaffold(
        topBar = { BarraSuperior("Detalle de cita", onVolver = { navController.popBackStack() }) }
    ) { padding ->
        if (cita == null) {
            Box(modifier = Modifier.padding(padding)) {
                MensajeVacio("La cita ya no existe")
            }
        } else {
            Column(modifier = Modifier.padding(padding).padding(16.dp)) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(1.dp, Color(0xFFEFEFEF)),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        FilaDato("Médico", medico?.nombre ?: "", Icons.Default.Person)
                        FilaDato("Especialidad", especialidad?.nombre ?: "", Icons.Default.MedicalServices)
                        FilaDato("Fecha", formatearFecha(cita.fecha), Icons.Default.DateRange)
                        FilaDato("Hora", cita.hora, Icons.Default.Schedule)
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
                BotonSecundario(
                    texto = "Cancelar cita",
                    onClick = { mostrarDialogo = true }
                )
            }
        }
    }

    // Cuadro de confirmación antes de eliminar la cita
    if (mostrarDialogo) {
        AlertDialog(
            onDismissRequest = { mostrarDialogo = false },
            title = { Text("Cancelar cita") },
            text = { Text("¿Seguro que deseas cancelar esta cita?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        // Quita la cita de la lista y vuelve a Mis citas
                        Repositorio.cancelarCita(citaId)
                        mostrarDialogo = false
                        navController.popBackStack()
                    }
                ) {
                    Text("Sí, cancelar")
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarDialogo = false }) {
                    Text("No")
                }
            }
        )
    }
}
