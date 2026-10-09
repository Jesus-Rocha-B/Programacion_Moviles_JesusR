package com.rocha.saludplus.ui.agendamiento

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.rocha.saludplus.navigation.Rutas
import com.rocha.saludplus.repository.Repositorio
import com.rocha.saludplus.ui.components.BarraSuperior
import com.rocha.saludplus.ui.components.MensajeVacio
import com.rocha.saludplus.ui.components.TarjetaEspecialidad

@Composable
fun EspecialidadesScreen(navController: NavController, sede: String = "") {
    var busqueda by remember { mutableStateOf("") }
    val todas = Repositorio.buscarEspecialidades(busqueda)
    val lista = if (sede.isNotBlank()) {
        val especialidadesIdsEnSede = Repositorio.medicos
            .filter { it.sede.equals(sede, ignoreCase = true) }
            .map { it.especialidadId }
            .toSet()
        todas.filter { it.id in especialidadesIdsEnSede }
    } else {
        todas
    }

    Scaffold(
        topBar = { BarraSuperior(if (sede.isNotBlank()) "Especialidades ($sede)" else "Especialidades", onVolver = { navController.popBackStack() }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {
            // Barra de búsqueda tipo cápsula
            OutlinedTextField(
                value = busqueda,
                onValueChange = { busqueda = it },
                placeholder = { Text("Buscar especialidad...", color = Color(0xFF758A99)) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Buscar",
                        tint = Color(0xFF758A99)
                    )
                },
                singleLine = true,
                shape = RoundedCornerShape(24.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedBorderColor = Color(0xFF1877F2),
                    unfocusedBorderColor = Color(0xFFE0E0E0)
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (lista.isEmpty()) {
                MensajeVacio("No se encontraron especialidades")
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    items(lista) { especialidad ->
                        TarjetaEspecialidad(
                            especialidad = especialidad,
                            onClick = {
                                if (sede.isNotBlank()) {
                                    navController.navigate(Rutas.medicosSede(sede, especialidad.id))
                                } else {
                                    navController.navigate(Rutas.medicos(especialidad.id))
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}
