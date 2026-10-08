package com.rocha.saludplus.ui.agendamiento

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.rocha.saludplus.navigation.Rutas
import com.rocha.saludplus.repository.Repositorio
import com.rocha.saludplus.ui.components.*

// Recibe 3 parámetros desde la ruta: medicoId, fecha y hora
@Composable
fun ConfirmarCitaScreen(medicoId: Int, fecha: String, hora: String, navController: NavController) {
    val medico = Repositorio.obtenerMedico(medicoId)
    val especialidad = medico?.let { Repositorio.obtenerEspecialidad(it.especialidadId) }
    var motivo by remember { mutableStateOf("") }
    var mensaje by remember { mutableStateOf("") }

    Scaffold(
        topBar = { BarraSuperior("Confirmar cita", onVolver = { navController.popBackStack() }) }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp)) {
            // Resumen de la cita
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    FilaDato("Médico", medico?.nombre ?: "")
                    FilaDato("Especialidad", especialidad?.nombre ?: "")
                    FilaDato("Fecha", formatearFecha(fecha))
                    FilaDato("Hora", hora)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            CampoTexto(
                valor = motivo,
                onValorCambia = { motivo = it },
                etiqueta = "Motivo de consulta (opcional)"
            )
            if (mensaje.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = mensaje, color = MaterialTheme.colorScheme.error)
            }
            Spacer(modifier = Modifier.height(16.dp))
            BotonPrincipal(
                texto = "Agendar cita",
                onClick = {
                    // Devuelve null si el horario ya fue reservado
                    val cita = Repositorio.agendarCita(medicoId, fecha, hora)
                    if (cita != null) {
                        navController.navigate(Rutas.citaExitosa(cita.id)) {
                            // Borra el flujo de agendamiento del historial y deja Home
                            popUpTo(Rutas.HOME)
                        }
                    } else {
                        mensaje = "Ese horario ya fue reservado, elige otro"
                    }
                }
            )
        }
    }
}