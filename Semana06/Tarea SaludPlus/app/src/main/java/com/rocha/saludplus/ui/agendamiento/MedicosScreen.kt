package com.rocha.saludplus.ui.agendamiento
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.rocha.saludplus.navigation.Rutas
import com.rocha.saludplus.repository.Repositorio
import com.rocha.saludplus.ui.components.*

// especialidadId llega tipado como Int desde el NavHost
@Composable
fun MedicosScreen(especialidadId: Int, navController: NavController) {
    val nombreEspecialidad = Repositorio.obtenerEspecialidad(especialidadId)?.nombre ?: "Especialidad"
    var busqueda by remember { mutableStateOf("") }
    // Filtra por especialidad y por el texto buscado, mejor calificados primero
    val lista = Repositorio.buscarMedicos(especialidadId, busqueda)

    Scaffold(
        topBar = {
            BarraSuperior("Médicos de $nombreEspecialidad", onVolver = { navController.popBackStack() })
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp)) {
            CampoTexto(
                valor = busqueda,
                onValorCambia = { busqueda = it },
                etiqueta = "Buscar médico"
            )
            Spacer(modifier = Modifier.height(8.dp))
            if (lista.isEmpty()) {
                MensajeVacio("No se encontraron médicos")
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(lista) { medico ->
                        TarjetaMedico(
                            medico = medico,
                            especialidad = nombreEspecialidad,
                            onClick = { navController.navigate(Rutas.fechaHora(medico.id)) }
                        )
                    }
                }
            }
        }
    }
}