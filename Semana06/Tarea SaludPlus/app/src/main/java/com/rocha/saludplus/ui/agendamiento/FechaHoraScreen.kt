package com.rocha.saludplus.ui.agendamiento

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.rocha.saludplus.navigation.Rutas
import com.rocha.saludplus.repository.Repositorio
import com.rocha.saludplus.ui.components.*

@Composable
fun FechaHoraScreen(medicoId: Int, navController: NavController) {
    val medico = Repositorio.obtenerMedico(medicoId)
    val especialidad = medico?.let { Repositorio.obtenerEspecialidad(it.especialidadId) }

    // Lista fija de días, en la Fase 2 se generan con LocalDate
    val dias = listOf(
        "Jue" to "2026-10-08",
        "Vie" to "2026-10-09",
        "Lun" to "2026-10-12",
        "Mar" to "2026-10-13",
        "Mié" to "2026-10-14"
    )
    var fechaSeleccionada by remember { mutableStateOf(dias[0].second) }
    var horaSeleccionada by remember { mutableStateOf("") }
    // Se recalcula sola: si alguien reserva, el horario desaparece de la grilla
    val horarios = Repositorio.horariosDisponibles(medicoId, fechaSeleccionada)

    Scaffold(
        topBar = { BarraSuperior("Seleccionar fecha y hora", onVolver = { navController.popBackStack() }) }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp)) {
            // Datos del médico elegido
            if (medico != null) {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = medico.nombre, style = MaterialTheme.typography.titleMedium)
                        Text(
                            text = especialidad?.nombre ?: "",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Octubre 2026", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))

            // Los 5 días, uno al lado del otro
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                dias.forEach { (nombre, fecha) ->
                    ChipHorario(
                        hora = "$nombre ${fecha.takeLast(2).toInt()}",
                        seleccionado = fecha == fechaSeleccionada,
                        onClick = {
                            fechaSeleccionada = fecha
                            // Al cambiar de día se reinicia la hora elegida
                            horaSeleccionada = ""
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Horarios disponibles", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))

            // La grilla ocupa el espacio que sobra
            Box(modifier = Modifier.weight(1f)) {
                if (horarios.isEmpty()) {
                    MensajeVacio("No hay horarios disponibles")
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(horarios) { hora ->
                            ChipHorario(
                                hora = hora,
                                seleccionado = hora == horaSeleccionada,
                                onClick = { horaSeleccionada = hora }
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            // Solo se habilita con día y hora elegidos
            BotonPrincipal(
                texto = "Continuar",
                habilitado = horaSeleccionada.isNotEmpty(),
                onClick = {
                    navController.navigate(Rutas.confirmar(medicoId, fechaSeleccionada, horaSeleccionada))
                }
            )
        }
    }
}