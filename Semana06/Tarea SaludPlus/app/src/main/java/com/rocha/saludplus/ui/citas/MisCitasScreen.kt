package com.rocha.saludplus.ui.citas

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.rocha.saludplus.navigation.Rutas
import com.rocha.saludplus.repository.Repositorio
import com.rocha.saludplus.ui.components.*

@Composable
fun MisCitasScreen(navController: NavController) {
    // Citas del usuario con la sesión iniciada, ordenadas por fecha y hora
    val citas = Repositorio.citasDelUsuario()

    Scaffold(
        topBar = { BarraSuperior("Mis citas", onVolver = { navController.popBackStack() }) }
    ) { padding ->
        if (citas.isEmpty()) {
            // Mensaje cuando la lista está vacía
            Box(modifier = Modifier.padding(padding)) {
                MensajeVacio("Aún no tienes citas agendadas")
            }
        } else {
            LazyColumn(
                modifier = Modifier.padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(citas) { cita ->
                    val medico = Repositorio.obtenerMedico(cita.medicoId)
                    val especialidad = medico?.let { Repositorio.obtenerEspecialidad(it.especialidadId) }
                    TarjetaCita(
                        medicoNombre = medico?.nombre ?: "",
                        especialidad = especialidad?.nombre ?: "",
                        fecha = formatearFecha(cita.fecha),
                        hora = cita.hora,
                        onClick = { navController.navigate(Rutas.detalleCita(cita.id)) }
                    )
                }
            }
        }
    }
}