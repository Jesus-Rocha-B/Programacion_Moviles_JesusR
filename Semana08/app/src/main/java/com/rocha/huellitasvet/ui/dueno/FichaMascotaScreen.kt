package com.rocha.huellitasvet.ui.dueno

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.rocha.huellitasvet.data.local.entity.Mascota
import com.rocha.huellitasvet.model.edadTexto
import com.rocha.huellitasvet.model.nombreBonito
import com.rocha.huellitasvet.ui.components.AppTopBar
import com.rocha.huellitasvet.ui.components.HistorialItem
import com.rocha.huellitasvet.ui.components.MascotaAvatar
import com.rocha.huellitasvet.viewmodel.MascotaViewModel

@Composable
fun FichaMascotaScreen(
    vm: MascotaViewModel,
    mascotaId: Int,
    onVolver: () -> Unit,
    onEditar: (Int) -> Unit,
    onPedirCita: () -> Unit,
    soloLectura: Boolean = false   // true = vista del veterinario (sin editar, pedir cita ni eliminar)
) {
    val mascota by remember(mascotaId) { vm.mascota(mascotaId) }.collectAsState(initial = null)
    val historial by remember(mascotaId) { vm.historial(mascotaId) }.collectAsState()
    val pesos by remember(mascotaId) { vm.observarPesos(mascotaId) }.collectAsState()
    val vacunas by remember(mascotaId) { vm.observarVacunas(mascotaId) }.collectAsState()
    val mensaje by vm.mensaje.collectAsState()
    val eliminada by vm.eliminada.collectAsState()

    var pestana by remember { mutableIntStateOf(0) }       // 0 = Datos, 1 = Historial, 2 = Vacunas
    var confirmarEliminar by remember { mutableStateOf(false) }
    var mostrarDialogoVacuna by remember { mutableStateOf(false) }
    val snackbar = remember { SnackbarHostState() }

    LaunchedEffect(mensaje) {
        mensaje?.let {
            snackbar.showSnackbar(it)
            vm.limpiarMensaje()
        }
    }
    LaunchedEffect(eliminada) {
        if (eliminada) onVolver()
    }

    Scaffold(
        topBar = { AppTopBar(titulo = mascota?.nombre ?: "Mascota", onVolver = onVolver) },
        snackbarHost = { SnackbarHost(snackbar) },
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0)
    ) { padding ->
        val m = mascota
        if (m == null) {
            Box(Modifier.fillMaxSize().padding(padding))
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(20.dp)
            ) {
                // Cabecera
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(2.dp),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            MascotaAvatar(especie = m.especie, tamano = 72.dp)
                            Spacer(Modifier.width(16.dp))
                            Column {
                                Text(
                                    m.nombre,
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    "${m.especie.nombreBonito()} ${m.raza} · ${m.edadTexto()}",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Text(
                                    "${m.sexo.nombreBonito()} · ${"%.1f".format(m.peso)} kg",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                    Spacer(Modifier.height(16.dp))
                }

                // Botones (solo para el dueño)
                if (!soloLectura) {
                    item {
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            Button(
                                onClick = onPedirCita,
                                modifier = Modifier.weight(1f).height(48.dp),
                                shape = RoundedCornerShape(14.dp)
                            ) { Text("Pedir cita") }
                            OutlinedButton(
                                onClick = { onEditar(m.id) },
                                modifier = Modifier.weight(1f).height(48.dp),
                                shape = RoundedCornerShape(14.dp)
                            ) { Text("Editar") }
                        }
                        Spacer(Modifier.height(16.dp))
                    }
                }

                // Pestañas Datos / Historial clínico / Carnet de vacunas
                item {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        FilterChip(
                            selected = pestana == 0,
                            onClick = { pestana = 0 },
                            label = { Text("Datos") },
                            shape = RoundedCornerShape(50)
                        )
                        FilterChip(
                            selected = pestana == 1,
                            onClick = { pestana = 1 },
                            label = { Text("Historial") },
                            shape = RoundedCornerShape(50)
                        )
                        FilterChip(
                            selected = pestana == 2,
                            onClick = { pestana = 2 },
                            label = { Text("Carnet Vacunas") },
                            shape = RoundedCornerShape(50)
                        )
                    }
                    Spacer(Modifier.height(16.dp))
                }

                if (pestana == 0) {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            elevation = CardDefaults.cardElevation(2.dp),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                FilaDato("Especie", m.especie.nombreBonito())
                                FilaDato("Raza", m.raza.ifBlank { "—" })
                                FilaDato("Año de nacimiento", m.anioNacimiento.toString())
                                FilaDato("Edad", m.edadTexto())
                                FilaDato("Sexo", m.sexo.nombreBonito())
                                FilaDato("Peso actual", "${"%.1f".format(m.peso)} kg")
                            }
                        }

                        if (pesos.isNotEmpty()) {
                            Spacer(Modifier.height(16.dp))
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                elevation = CardDefaults.cardElevation(2.dp),
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    com.rocha.huellitasvet.ui.components.graficos.GraficoLineas(datos = pesos)
                                }
                            }
                        }

                        // Eliminar (solo para el dueño)
                        if (!soloLectura) {
                            Spacer(Modifier.height(16.dp))
                            OutlinedButton(
                                onClick = { confirmarEliminar = true },
                                modifier = Modifier.fillMaxWidth().height(48.dp),
                                shape = RoundedCornerShape(14.dp)
                            ) {
                                Text("Eliminar mascota", color = MaterialTheme.colorScheme.error)
                            }
                        }
                    }
                } else if (pestana == 1) {
                    if (historial.isEmpty()) {
                        item { Text("Aún no tiene atenciones") }
                    } else {
                        itemsIndexed(historial, key = { _, c -> c.cita.id }) { i, c ->
                            HistorialItem(cita = c, esUltimo = i == historial.lastIndex)
                        }
                    }
                } else {
                    // Carnet de Vacunas
                    item {
                        Button(
                            onClick = { mostrarDialogoVacuna = true },
                            modifier = Modifier.fillMaxWidth().height(48.dp),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Text("Registrar vacuna")
                        }
                        Spacer(Modifier.height(16.dp))
                    }

                    if (vacunas.isEmpty()) {
                        item { Text("No hay vacunas registradas") }
                    } else {
                        itemsIndexed(vacunas, key = { _, v -> v.id }) { _, v ->
                            TarjetaVacuna(vacuna = v)
                        }
                    }
                }
            }

            if (confirmarEliminar) {
                ConfirmarEliminar(
                    mascota = m,
                    onConfirmar = {
                        confirmarEliminar = false
                        vm.eliminar(m)
                    },
                    onCancelar = { confirmarEliminar = false }
                )
            }

            if (mostrarDialogoVacuna) {
                DialogoRegistrarVacuna(
                    onDismiss = { mostrarDialogoVacuna = false },
                    onConfirmar = { nombre, fecha, proxima ->
                        vm.registrarVacuna(m.id, nombre, fecha, proxima)
                        mostrarDialogoVacuna = false
                    }
                )
            }
        }
    }
}

@Composable
private fun FilaDato(etiqueta: String, valor: String) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(etiqueta, style = MaterialTheme.typography.bodyMedium)
        Text(valor, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun ConfirmarEliminar(mascota: Mascota, onConfirmar: () -> Unit, onCancelar: () -> Unit) {
    AlertDialog(
        onDismissRequest = onCancelar,
        title = { Text("Eliminar mascota") },
        text = { Text("¿Seguro que quieres eliminar a ${mascota.nombre}?") },
        confirmButton = { TextButton(onClick = onConfirmar) { Text("Eliminar") } },
        dismissButton = { TextButton(onClick = onCancelar) { Text("Cancelar") } }
    )
}

@Composable
private fun TarjetaVacuna(vacuna: com.rocha.huellitasvet.data.local.entity.Vacuna) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(2.dp),
        shape = RoundedCornerShape(14.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(vacuna.nombre, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text("Fecha de aplicación: ${vacuna.fecha}", style = MaterialTheme.typography.bodyMedium)
            vacuna.proximaDosis?.let {
                Text("Próxima dosis: $it", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
private fun DialogoRegistrarVacuna(
    onDismiss: () -> Unit,
    onConfirmar: (nombre: String, fecha: String, proximaDosis: String?) -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var fecha by remember { mutableStateOf(java.time.LocalDate.now().toString()) }
    var proximaDosis by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Registrar vacuna", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                com.rocha.huellitasvet.ui.components.VetTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = "Nombre de la vacuna (ej. Rabia, Triple)"
                )
                com.rocha.huellitasvet.ui.components.VetTextField(
                    value = fecha,
                    onValueChange = { fecha = it },
                    label = "Fecha de aplicación (YYYY-MM-DD)"
                )
                com.rocha.huellitasvet.ui.components.VetTextField(
                    value = proximaDosis,
                    onValueChange = { proximaDosis = it },
                    label = "Próxima dosis (Opcional, YYYY-MM-DD)"
                )
            }
        },
        confirmButton = {
            Button(
                enabled = nombre.isNotBlank(),
                onClick = { onConfirmar(nombre, fecha, proximaDosis) }
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}