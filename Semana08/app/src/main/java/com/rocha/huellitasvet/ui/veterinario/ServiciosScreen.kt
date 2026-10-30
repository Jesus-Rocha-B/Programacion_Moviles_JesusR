package com.rocha.huellitasvet.ui.veterinario

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.rocha.huellitasvet.R
import com.rocha.huellitasvet.data.local.entity.Servicio
import com.rocha.huellitasvet.ui.components.AppTopBar
import com.rocha.huellitasvet.viewmodel.ServiciosViewModel

@Composable
fun ServiciosScreen(
    vm: ServiciosViewModel,
    onNuevo: () -> Unit,
    onEditar: (Int) -> Unit
) {
    val servicios by vm.servicios.collectAsState()
    val mensaje by vm.mensaje.collectAsState()
    val snackbar = remember { SnackbarHostState() }
    var filtroEstado by remember { mutableStateOf("TODOS") }

    val serviciosFiltrados = remember(servicios, filtroEstado) {
        when (filtroEstado) {
            "ACTIVOS" -> servicios.filter { it.activo }
            "INACTIVOS" -> servicios.filter { !it.activo }
            else -> servicios
        }
    }

    val opcionesFiltro = listOf(
        Pair("TODOS", "Todos"),
        Pair("ACTIVOS", "Activos"),
        Pair("INACTIVOS", "Inactivos")
    )

    LaunchedEffect(mensaje) {
        mensaje?.let {
            snackbar.showSnackbar(it)
            vm.limpiarMensaje()
        }
    }

    val colores = MaterialTheme.colorScheme

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topBar = { AppTopBar(titulo = "Servicios") },
            snackbarHost = { SnackbarHost(snackbar) },
            containerColor = colores.background,
            contentWindowInsets = WindowInsets(0)
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                // Filtros Chips Blanco / Azul (Igual que el perfil de Dueño)
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)
                ) {
                    items(opcionesFiltro) { (clave, etiqueta) ->
                        val seleccionada = filtroEstado == clave
                        Card(
                            modifier = Modifier
                                .clip(RoundedCornerShape(50))
                                .clickable { filtroEstado = clave },
                            colors = CardDefaults.cardColors(
                                containerColor = if (seleccionada) colores.primary else Color.White
                            ),
                            border = BorderStroke(
                                width = 1.dp,
                                color = if (seleccionada) colores.primary else Color(0xFFCBD5E1)
                            ),
                            shape = RoundedCornerShape(50)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 18.dp, vertical = 8.dp),
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

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 4.dp, bottom = 88.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    if (serviciosFiltrados.isEmpty()) {
                        item {
                            Text(
                                text = "No hay servicios con el filtro seleccionado",
                                style = MaterialTheme.typography.bodyMedium,
                                color = colores.onSurfaceVariant,
                                modifier = Modifier.padding(top = 12.dp)
                            )
                        }
                    } else {
                        items(serviciosFiltrados, key = { it.id }) { s ->
                            ServicioItem(
                                servicio = s,
                                onToggle = { vm.alternar(s) },
                                onEditar = { onEditar(s.id) }
                            )
                        }
                    }
                }
            }
        }

        // Botón Flotante "+" Círculo Perfecto en Azul
        FloatingActionButton(
            onClick = onNuevo,
            shape = CircleShape,
            containerColor = colores.primary,
            contentColor = colores.onPrimary,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(20.dp)
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Nuevo servicio")
        }
    }
}

@Composable
private fun ServicioItem(servicio: Servicio, onToggle: () -> Unit, onEditar: () -> Unit) {
    val colores = MaterialTheme.colorScheme
    val resId = when {
        servicio.nombre.contains("consulta", ignoreCase = true) -> R.drawable.ic_servicio_consulta
        servicio.nombre.contains("vacun", ignoreCase = true) -> R.drawable.ic_servicio_vacunas
        servicio.nombre.contains("baño", ignoreCase = true) || servicio.nombre.contains("bano", ignoreCase = true) || servicio.nombre.contains("corte", ignoreCase = true) -> R.drawable.ic_servicio_bano
        servicio.nombre.contains("desparasit", ignoreCase = true) -> R.drawable.ic_servicio_desparasitacion
        else -> R.drawable.ic_servicio_consulta
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = colores.surface),
        elevation = CardDefaults.cardElevation(2.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Insignia circular idéntica al diseño del dueño (CircleShape)
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(colores.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = resId),
                    contentDescription = servicio.nombre,
                    tint = colores.primary,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(servicio.nombre, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(
                    "${servicio.duracionMin} min · S/ ${"%.2f".format(servicio.precio)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = colores.onSurfaceVariant
                )
            }
            Switch(checked = servicio.activo, onCheckedChange = { onToggle() })
            IconButton(onClick = onEditar) {
                Icon(Icons.Filled.Edit, contentDescription = "Editar", tint = colores.primary)
            }
        }
    }
}
