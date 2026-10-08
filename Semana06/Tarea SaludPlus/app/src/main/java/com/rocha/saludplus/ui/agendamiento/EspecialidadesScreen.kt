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

@Composable
fun EspecialidadesScreen(navController: NavController) {
    // Texto de la búsqueda
    var busqueda by remember { mutableStateOf("") }
    // Se recalcula sola cada vez que cambia el texto
    val lista = Repositorio.buscarEspecialidades(busqueda)

    Scaffold(
        topBar = { BarraSuperior("Especialidades", onVolver = { navController.popBackStack() }) }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp)) {
            CampoTexto(
                valor = busqueda,
                onValorCambia = { busqueda = it },
                etiqueta = "Buscar especialidad"
            )
            Spacer(modifier = Modifier.height(8.dp))
            if (lista.isEmpty()) {
                MensajeVacio("No se encontraron especialidades")
            } else {
                // Solo dibuja los elementos visibles en pantalla
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(lista) { especialidad ->
                        TarjetaEspecialidad(
                            especialidad = especialidad,
                            // Pasa el id de la especialidad a la pantalla Médicos
                            onClick = { navController.navigate(Rutas.medicos(especialidad.id)) }
                        )
                    }
                }
            }
        }
    }
}