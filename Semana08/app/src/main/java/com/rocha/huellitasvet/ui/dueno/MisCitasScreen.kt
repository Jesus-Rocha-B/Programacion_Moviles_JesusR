package com.rocha.huellitasvet.ui.dueno
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.rocha.huellitasvet.data.local.relation.CitaConDetalle
import com.rocha.huellitasvet.model.fechaLegible
import com.rocha.huellitasvet.ui.components.CitaCard
import com.rocha.huellitasvet.viewmodel.MisCitasViewModel

@Composable
fun MisCitasScreen(
    vm: MisCitasViewModel,
    onPedirCita: () -> Unit
) {
    val proximas by vm.proximas.collectAsState()
    val historial by vm.historial.collectAsState()
    val mensaje by vm.mensaje.collectAsState()

    var pestana by remember { mutableIntStateOf(0) }        // 0 = Próximas, 1 = Historial
    var porCancelar by remember { mutableStateOf<CitaConDetalle?>(null) }
    val snackbar = remember { SnackbarHostState() }

    LaunchedEffect(mensaje) {
        mensaje?.let {
            snackbar.showSnackbar(it)
            vm.limpiarMensaje()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbar) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onPedirCita,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Pedir una cita")
            }
        },
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            Text(
                "Mis citas",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(12.dp))

            // Pestañas Próximas / Historial
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(
                    selected = pestana == 0,
                    onClick = { pestana = 0 },
                    label = { Text("Próximas") },
                    shape = RoundedCornerShape(50)
                )
                FilterChip(
                    selected = pestana == 1,
                    onClick = { pestana = 1 },
                    label = { Text("Historial") },
                    shape = RoundedCornerShape(50)
                )
            }
            Spacer(Modifier.height(12.dp))

            val lista = if (pestana == 0) proximas else historial

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 88.dp) // espacio para el botón flotante
            ) {
                if (lista.isEmpty()) {
                    item {
                        Text(
                            if (pestana == 0) "No tienes citas próximas" else "Aún no tienes historial"
                        )
                    }
                }

                items(lista, key = { it.cita.id }) { c ->
                    // El botón Cancelar solo se ofrece en las citas activas de "Próximas".
                    // Si ya faltan 2 horas o menos, al tocarlo avisa que no se puede.
                    val ofreceCancelar = pestana == 0
                    CitaCard(
                        cita = c,
                        onCancelar = if (ofreceCancelar) {
                            {
                                if (vm.puedeCancelar(c)) porCancelar = c
                                else vm.avisarNoCancelable()
                            }
                        } else null
                    )
                }

                if (pestana == 0) {
                    item { AvisoCancelacion() }
                }
            }
        }
    }

    // Confirmación con AlertDialog
    porCancelar?.let { c ->
        AlertDialog(
            onDismissRequest = { porCancelar = null },
            title = { Text("Cancelar cita") },
            text = {
                Text(
                    "¿Seguro que quieres cancelar ${c.servicio.nombre} de " +
                            "${c.mascota.mascota.nombre} (${c.fechaLegible()})?"
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    vm.cancelar(c)
                    porCancelar = null
                }) { Text("Sí, cancelar", color = MaterialTheme.colorScheme.error) }
            },
            dismissButton = {
                TextButton(onClick = { porCancelar = null }) { Text("No") }
            }
        )
    }
}

@Composable
private fun AvisoCancelacion() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF3C7)),
        shape = RoundedCornerShape(14.dp)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Filled.Schedule, contentDescription = null, tint = Color(0xFFD97706))
            Spacer(Modifier.width(10.dp))
            Text(
                "Puedes cancelar hasta 2 horas antes",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF78350F)
            )
        }
    }
}