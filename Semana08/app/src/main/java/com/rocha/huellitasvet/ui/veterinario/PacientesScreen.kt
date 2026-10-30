package com.rocha.huellitasvet.ui.veterinario

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.rocha.huellitasvet.data.local.relation.MascotaConDueno
import com.rocha.huellitasvet.model.edadTexto
import com.rocha.huellitasvet.model.nombreBonito
import com.rocha.huellitasvet.ui.components.AppTopBar
import com.rocha.huellitasvet.ui.components.MascotaAvatar
import com.rocha.huellitasvet.viewmodel.PacientesViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun PacientesScreen(
    vm: PacientesViewModel,
    onPaciente: (Int) -> Unit
) {
    val pacientes by vm.pacientes.collectAsState()
    val busqueda by vm.busqueda.collectAsState()
    val filtros by vm.filtros.collectAsState()
    val visitas by vm.ultimasVisitas.collectAsState()

    val opcionesFiltro = listOf(
        Pair("TODOS", "Todos"),
        Pair("PERRO", "Perros"),
        Pair("GATO", "Gatos"),
        Pair("OTRO", "Otros")
    )

    Scaffold(
        topBar = { AppTopBar(titulo = "Pacientes") },
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            // Buscador Compacto (Altura 48dp)
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp),
                color = Color.White,
                border = BorderStroke(1.dp, Color(0xFFCBD5E1))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = null,
                        tint = Color(0xFF64748B),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    BasicTextField(
                        value = busqueda,
                        onValueChange = { vm.buscar(it) },
                        singleLine = true,
                        textStyle = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.Medium
                        ),
                        modifier = Modifier.weight(1f),
                        decorationBox = { innerTextField ->
                            if (busqueda.isEmpty()) {
                                Text(
                                    "Buscar mascota, dueño o raza...",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color(0xFF94A3B8)
                                )
                            }
                            innerTextField()
                        }
                    )
                    if (busqueda.isNotEmpty()) {
                        IconButton(
                            onClick = { vm.buscar("") },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                Icons.Filled.Clear,
                                contentDescription = "Limpiar",
                                tint = Color(0xFF64748B),
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(12.dp))

            // Filtro por especie en Chips Múltiples (Blanco/Azul)
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(opcionesFiltro) { (clave, etiqueta) ->
                    val seleccionada = filtros.contains(clave)
                    Card(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .clickable { vm.toggleFiltro(clave) },
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

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                if (pacientes.isEmpty()) {
                    item {
                        Text(
                            text = "No se encontraron pacientes",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(top = 12.dp)
                        )
                    }
                } else {
                    items(pacientes, key = { it.mascota.id }) { p ->
                        TarjetaPaciente(
                            paciente = p,
                            ultimaVisita = visitas[p.mascota.id],
                            onClick = { onPaciente(p.mascota.id) }
                        )
                    }

                    // Contador de pacientes al final de la lista de tarjetas
                    item {
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = "${pacientes.size} paciente${if (pacientes.size == 1) "" else "s"} encontrado${if (pacientes.size == 1) "" else "s"}",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TarjetaPaciente(
    paciente: MascotaConDueno,
    ultimaVisita: String?,
    onClick: () -> Unit
) {
    val m = paciente.mascota
    val visita = ultimaVisita?.let {
        "Última visita: " + LocalDate.parse(it)
            .format(DateTimeFormatter.ofPattern("d MMM", Locale.forLanguageTag("es-PE")))
    } ?: "Sin visitas"

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
            MascotaAvatar(especie = m.especie, tamano = 56.dp)
            Spacer(Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(m.nombre, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(
                    "${m.raza.ifBlank { m.especie.nombreBonito() }} · ${m.edadTexto()}",
                    style = MaterialTheme.typography.bodySmall
                )
                Text("Dueño: ${paciente.dueno.nombre}", style = MaterialTheme.typography.bodySmall)
                Text(visita, style = MaterialTheme.typography.labelSmall)
            }
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null)
        }
    }
}
