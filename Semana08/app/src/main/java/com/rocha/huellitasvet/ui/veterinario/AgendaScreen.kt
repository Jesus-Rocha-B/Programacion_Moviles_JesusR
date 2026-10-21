package com.rocha.huellitasvet.ui.veterinario
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.rocha.huellitasvet.data.local.relation.CitaConDetalle
import com.rocha.huellitasvet.model.EstadoCita
import com.rocha.huellitasvet.ui.components.AppTopBar
import com.rocha.huellitasvet.ui.components.EstadoCitaChip
import com.rocha.huellitasvet.ui.components.FechaChip
import com.rocha.huellitasvet.ui.components.MascotaAvatar
import com.rocha.huellitasvet.viewmodel.AgendaViewModel

@Composable
fun AgendaScreen(
    vm: AgendaViewModel,
    onAtender: (Int) -> Unit
) {
    val fechaSel by vm.fechaSeleccionada.collectAsState()
    val citas by vm.citas.collectAsState()
    val mensaje by vm.mensaje.collectAsState()
    val snackbar = remember { SnackbarHostState() }

    LaunchedEffect(mensaje) {
        mensaje?.let {
            snackbar.showSnackbar(it)
            vm.limpiarMensaje()
        }
    }

    Scaffold(
        topBar = { AppTopBar(titulo = "Agenda") },
        snackbarHost = { SnackbarHost(snackbar) },
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Chips de los próximos días
            LazyRow(
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(vm.fechas) { f ->
                    FechaChip(
                        fecha = f,
                        seleccionada = f == fechaSel,
                        onClick = { vm.elegirFecha(f) }
                    )
                }
            }

            // Línea de tiempo por hora
            LazyColumn(
                contentPadding = PaddingValues(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (citas.isEmpty()) {
                    item { Text("No hay citas para este día") }
                }
                items(citas, key = { it.cita.id }) { c ->
                    FilaAgenda(
                        cita = c,
                        onConfirmar = { vm.confirmar(c) },
                        onAtender = { onAtender(c.cita.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun FilaAgenda(
    cita: CitaConDetalle,
    onConfirmar: () -> Unit,
    onAtender: () -> Unit
) {
    val colores = MaterialTheme.colorScheme
    val dueno = cita.mascota.dueno.nombre.split(" ").let { partes ->
        if (partes.size > 1) "${partes.first()} ${partes.last().first()}." else partes.first()
    }

    Row(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = cita.cita.hora,
            color = colores.primary,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .width(56.dp)
                .padding(top = 14.dp)
        )

        Card(
            modifier = Modifier.weight(1f),
            colors = CardDefaults.cardColors(containerColor = colores.surface),
            elevation = CardDefaults.cardElevation(2.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    MascotaAvatar(especie = cita.mascota.mascota.especie, tamano = 44.dp)
                    Spacer(Modifier.width(10.dp))
                    Column {
                        Text(
                            "${cita.mascota.mascota.nombre} · ${cita.servicio.nombre}",
                            fontWeight = FontWeight.Bold
                        )
                        Text("Dueño: $dueno", style = MaterialTheme.typography.bodySmall)
                    }
                }

                Spacer(Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    EstadoCitaChip(estado = cita.cita.estado)

                    when (cita.cita.estado) {
                        EstadoCita.PENDIENTE.name ->
                            Button(
                                onClick = onConfirmar,
                                shape = RoundedCornerShape(50)
                            ) { Text("Confirmar") }

                        EstadoCita.CONFIRMADA.name ->
                            Button(
                                onClick = onAtender,
                                shape = RoundedCornerShape(50)
                            ) { Text("Atender") }
                    }
                }
            }
        }
    }
}