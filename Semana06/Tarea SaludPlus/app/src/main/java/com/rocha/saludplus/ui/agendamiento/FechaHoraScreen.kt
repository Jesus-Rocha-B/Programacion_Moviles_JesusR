package com.rocha.saludplus.ui.agendamiento

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.rocha.saludplus.navigation.Rutas
import com.rocha.saludplus.repository.Repositorio
import com.rocha.saludplus.ui.components.BarraSuperior
import com.rocha.saludplus.ui.components.BotonPrincipal
import com.rocha.saludplus.ui.components.ChipDia
import com.rocha.saludplus.ui.components.ChipHorario
import com.rocha.saludplus.ui.components.MensajeVacio
import com.rocha.saludplus.ui.components.diasHabiles
import com.rocha.saludplus.ui.components.nombreDiaCorto
import com.rocha.saludplus.ui.components.nombreMes

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FechaHoraScreen(medicoId: Int, navController: NavController) {
    val medico = Repositorio.obtenerMedico(medicoId)
    val especialidad = medico?.let { Repositorio.obtenerEspecialidad(it.especialidadId) }

    // Estado de la semana actual (0 para la semana base)
    var semana by remember { mutableIntStateOf(0) }
    val dias = diasHabiles(semana)

    // Estado de la fecha seleccionada en formato "yyyy-MM-dd" (inicializada por defecto en el primer día)
    var fechaSeleccionada by remember { mutableStateOf(dias[0].toString()) }
    var horaSeleccionada by remember { mutableStateOf("") }

    // Se recalcula sola: si alguien reserva, el horario desaparece de la grilla
    val horarios = Repositorio.horariosDisponibles(medicoId, fechaSeleccionada)

    Scaffold(
        topBar = { BarraSuperior("Seleccionar fecha y hora", onVolver = { navController.popBackStack() }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {
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

            // Cabecera del calendario: Navegación de semana y nombre del mes
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        if (semana > 0) {
                            semana--
                            val nuevosDias = diasHabiles(semana)
                            fechaSeleccionada = nuevosDias[0].toString()
                            horaSeleccionada = ""
                        }
                    },
                    enabled = semana > 0
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = "Semana anterior"
                    )
                }

                Text(
                    text = nombreMes(dias[0]),
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center
                )

                IconButton(
                    onClick = {
                        semana++
                        val nuevosDias = diasHabiles(semana)
                        fechaSeleccionada = nuevosDias[0].toString()
                        horaSeleccionada = ""
                    }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = "Semana siguiente"
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))

            // Fila de selección de los 5 días hábiles con ChipDia
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                dias.forEach { dia ->
                    ChipDia(
                        nombre = nombreDiaCorto(dia),
                        numero = dia.dayOfMonth.toString(),
                        seleccionado = dia.toString() == fechaSeleccionada,
                        onClick = {
                            fechaSeleccionada = dia.toString()
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
