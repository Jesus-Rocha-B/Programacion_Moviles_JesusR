package com.rocha.huellitasvet.ui.veterinario
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.rocha.huellitasvet.model.EstadoCita
import com.rocha.huellitasvet.model.edadTexto
import com.rocha.huellitasvet.model.fechaLegible
import com.rocha.huellitasvet.model.nombreBonito
import com.rocha.huellitasvet.ui.components.AppTopBar
import com.rocha.huellitasvet.ui.components.MascotaAvatar
import com.rocha.huellitasvet.ui.components.VetButton
import com.rocha.huellitasvet.ui.components.VetTextField
import com.rocha.huellitasvet.viewmodel.AtenderCitaViewModel

@Composable
fun AtenderCitaScreen(
    vm: AtenderCitaViewModel,
    onVolver: () -> Unit
) {
    val cita by vm.cita.collectAsState()
    val errores by vm.errores.collectAsState()
    val mensaje by vm.mensaje.collectAsState()
    val terminado by vm.terminado.collectAsState()

    var peso by remember { mutableStateOf("") }
    var diagnostico by remember { mutableStateOf("") }
    var tratamiento by remember { mutableStateOf("") }
    var proximoControl by remember { mutableStateOf("") }
    var confirmarCancelar by remember { mutableStateOf(false) }
    val snackbar = remember { SnackbarHostState() }

    // Precarga el peso actual de la mascota (una sola vez)
    LaunchedEffect(cita?.cita?.id) {
        cita?.let { if (peso.isEmpty()) peso = it.mascota.mascota.peso.toString() }
    }
    LaunchedEffect(mensaje) {
        mensaje?.let {
            snackbar.showSnackbar(it)
            vm.limpiarMensaje()
        }
    }
    LaunchedEffect(terminado) {
        if (terminado) onVolver()
    }

    Scaffold(
        topBar = { AppTopBar(titulo = "Atender cita", onVolver = onVolver) },
        snackbarHost = { SnackbarHost(snackbar) },
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0)
    ) { padding ->
        val c = cita
        if (c == null) {
            Box(Modifier.fillMaxSize().padding(padding))
            return@Scaffold
        }

        val m = c.mascota.mascota
        val colores = MaterialTheme.colorScheme
        val atendible = c.cita.estado == EstadoCita.PENDIENTE.name ||
                c.cita.estado == EstadoCita.CONFIRMADA.name
        val pesoNuevo = peso.trim().replace(',', '.').toDoubleOrNull()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {
            // ---------- Cabecera ----------
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = colores.surface),
                elevation = CardDefaults.cardElevation(2.dp),
                shape = RoundedCornerShape(20.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    MascotaAvatar(especie = m.especie, tamano = 64.dp)
                    Spacer(Modifier.width(14.dp))
                    Column {
                        Text(
                            "${m.nombre} · ${m.especie.nombreBonito()} ${m.raza}",
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "${m.edadTexto()} · ${m.sexo.nombreBonito()} · Dueño: ${c.mascota.dueno.nombre}",
                            style = MaterialTheme.typography.bodySmall
                        )
                        Text(
                            "${c.servicio.nombre} · ${c.fechaLegible()}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = colores.primary,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            Spacer(Modifier.height(20.dp))

            Text(
                "Registro clínico",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(8.dp))

            // ---------- Peso nuevo + peso anterior ----------
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.Top
            ) {
                VetTextField(
                    value = peso,
                    onValueChange = { peso = it },
                    label = "Peso (kg)",
                    keyboardType = KeyboardType.Decimal,
                    error = errores.peso,
                    modifier = Modifier.weight(1f)
                )
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .padding(top = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = colores.primaryContainer),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Filled.Favorite,
                            contentDescription = null,
                            tint = Color(0xFFDC2626),
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Column {
                            Text("Peso anterior", style = MaterialTheme.typography.labelSmall)
                            Text("${"%.1f".format(m.peso)} kg", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            Spacer(Modifier.height(8.dp))

            // ---------- Diagnóstico y tratamiento ----------
            OutlinedTextField(
                value = diagnostico,
                onValueChange = { diagnostico = it },
                label = { Text("Diagnóstico") },
                minLines = 3,
                isError = errores.diagnostico != null,
                supportingText = errores.diagnostico?.let { { Text(it) } },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = tratamiento,
                onValueChange = { tratamiento = it },
                label = { Text("Tratamiento") },
                minLines = 3,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )
            Spacer(Modifier.height(8.dp))
            VetTextField(
                value = proximoControl,
                onValueChange = { proximoControl = it },
                label = "Próximo control (Opcional, YYYY-MM-DD)"
            )

            // ---------- Aviso ----------
            if (pesoNuevo != null && pesoNuevo > 0.0) {
                Spacer(Modifier.height(12.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = colores.primaryContainer),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Filled.Info,
                            contentDescription = null,
                            tint = colores.primary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            "Se actualizará el peso de ${m.nombre} a ${"%.1f".format(pesoNuevo)} kg",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }

            if (!atendible) {
                Spacer(Modifier.height(12.dp))
                Text(
                    "Esta cita ya está ${c.cita.estado.nombreBonito().lowercase()} y no se puede modificar",
                    color = colores.error
                )
            }

            Spacer(Modifier.height(24.dp))

            VetButton(
                texto = "Registrar atención",
                onClick = { vm.registrar(diagnostico, tratamiento, peso, proximoControl) },
                enabled = atendible
            )

            Spacer(Modifier.height(8.dp))

            OutlinedButton(
                onClick = { confirmarCancelar = true },
                enabled = atendible,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(14.dp)
            ) {
                Text("Cancelar cita", color = if (atendible) colores.error else colores.outline)
            }
        }

        if (confirmarCancelar) {
            AlertDialog(
                onDismissRequest = { confirmarCancelar = false },
                title = { Text("Cancelar cita") },
                text = { Text("¿Seguro que quieres cancelar la cita de ${m.nombre}?") },
                confirmButton = {
                    TextButton(onClick = {
                        confirmarCancelar = false
                        vm.cancelar()
                    }) { Text("Sí, cancelar", color = MaterialTheme.colorScheme.error) }
                },
                dismissButton = {
                    TextButton(onClick = { confirmarCancelar = false }) { Text("No") }
                }
            )
        }
    }
}