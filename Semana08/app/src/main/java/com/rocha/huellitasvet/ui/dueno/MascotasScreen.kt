package com.rocha.huellitasvet.ui.dueno

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.rocha.huellitasvet.data.local.entity.Mascota
import com.rocha.huellitasvet.model.Especie
import com.rocha.huellitasvet.model.edadTexto
import com.rocha.huellitasvet.model.nombreBonito
import com.rocha.huellitasvet.ui.components.MascotaAvatar
import com.rocha.huellitasvet.viewmodel.InicioViewModel

@Composable
fun MascotasScreen(
    vm: InicioViewModel,
    onMascota: (Int) -> Unit,
    onAgregar: () -> Unit
) {
    val mascotas by vm.mascotas.collectAsState()
    var filtrosSeleccionados by remember { mutableStateOf(setOf("TODOS")) }

    fun toggleFiltro(clave: String) {
        if (clave == "TODOS") {
            filtrosSeleccionados = setOf("TODOS")
            return
        }

        val nuevos = filtrosSeleccionados.toMutableSet()
        nuevos.remove("TODOS")

        if (nuevos.contains(clave)) {
            nuevos.remove(clave)
        } else {
            nuevos.add(clave)
        }

        if (nuevos.isEmpty() || nuevos.containsAll(setOf("PERRO", "GATO", "OTROS"))) {
            filtrosSeleccionados = setOf("TODOS")
        } else {
            filtrosSeleccionados = nuevos
        }
    }

    val mascotasFiltradas = remember(mascotas, filtrosSeleccionados) {
        if (filtrosSeleccionados.contains("TODOS")) {
            mascotas
        } else {
            mascotas.filter { m ->
                when (m.especie) {
                    Especie.PERRO.name -> filtrosSeleccionados.contains("PERRO")
                    Especie.GATO.name -> filtrosSeleccionados.contains("GATO")
                    else -> filtrosSeleccionados.contains("OTROS")
                }
            }
        }
    }

    val opcionesFiltro = listOf(
        Pair("TODOS", "Todos"),
        Pair("PERRO", "Perros"),
        Pair("GATO", "Gatos"),
        Pair("OTROS", "Otros")
    )

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            Text(
                text = "Mis mascotas",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(12.dp))

            // Chips de Filtro con fondo Blanco (no seleccionado) y Verde (seleccionado)
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(opcionesFiltro) { (clave, etiqueta) ->
                    val seleccionada = filtrosSeleccionados.contains(clave)
                    Card(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .clickable { toggleFiltro(clave) },
                        colors = CardDefaults.cardColors(
                            containerColor = if (seleccionada) MaterialTheme.colorScheme.primary else Color.White
                        ),
                        border = BorderStroke(
                            width = 1.dp,
                            color = if (seleccionada) MaterialTheme.colorScheme.primary else Color(0xFFCBD5E1)
                        ),
                        shape = RoundedCornerShape(50)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = etiqueta,
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = if (seleccionada) FontWeight.Bold else FontWeight.Medium,
                                color = if (seleccionada) Color.White else Color(0xFF334155)
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(14.dp))

            if (mascotasFiltradas.isEmpty()) {
                Text(
                    text = if (mascotas.isEmpty()) "Aún no tienes mascotas registradas" else "No hay mascotas con los filtros seleccionados",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 16.dp)
                )
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 88.dp)
                ) {
                    items(mascotasFiltradas, key = { it.id }) { m ->
                        MascotaItem(m) { onMascota(m.id) }
                    }
                }
            }
        }

        FloatingActionButton(
            onClick = onAgregar,
            shape = CircleShape,
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(20.dp)
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Agregar mascota")
        }
    }
}

@Composable
private fun MascotaItem(m: Mascota, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(2.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            MascotaAvatar(especie = m.especie, tamano = 60.dp)
            Spacer(Modifier.width(14.dp))
            Column {
                Text(m.nombre, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(
                    "${m.especie.nombreBonito()} · ${m.raza}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(m.edadTexto(), style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}
