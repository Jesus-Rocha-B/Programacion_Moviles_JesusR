package com.rocha.huellitasvet.ui.dueno
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.rocha.huellitasvet.R
import com.rocha.huellitasvet.data.local.entity.Servicio
import com.rocha.huellitasvet.model.Horarios
import com.rocha.huellitasvet.ui.components.AppTopBar
import com.rocha.huellitasvet.ui.components.FechaChip
import com.rocha.huellitasvet.ui.components.HoraChip
import com.rocha.huellitasvet.ui.components.MascotaAvatar
import com.rocha.huellitasvet.ui.components.VetButton
import com.rocha.huellitasvet.ui.components.iconoDeServicio
import com.rocha.huellitasvet.viewmodel.PedirCitaViewModel
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun PedirCitaScreen(
    vm: PedirCitaViewModel,
    mascotaInicial: Int?,
    servicioInicial: Int? = null,
    onVolver: () -> Unit,
    onCreada: () -> Unit
) {
    val mascotas by vm.mascotas.collectAsState()
    val servicios by vm.servicios.collectAsState()
    val mascotaId by vm.mascotaId.collectAsState()
    val servicioId by vm.servicioId.collectAsState()
    val fecha by vm.fecha.collectAsState()
    val hora by vm.hora.collectAsState()
    val ocupadas by vm.horasOcupadas.collectAsState()
    val mensaje by vm.mensaje.collectAsState()
    val creada by vm.creada.collectAsState()
    val aceptaCitas by vm.aceptaCitas.collectAsState()
    val snackbar = remember { SnackbarHostState() }

    // Preseleccionar mascota solo si viene de la ficha de mascota (id != -1)
    LaunchedEffect(mascotas) {
        if (mascotaId == null && mascotas.isNotEmpty() && mascotaInicial != null && mascotaInicial != -1) {
            val inicial = mascotas.firstOrNull { it.id == mascotaInicial }
            if (inicial != null) {
                vm.elegirMascota(inicial.id)
            }
        }
    }

    // Preseleccionar servicio solo si viene de un acceso rápido ("Consulta", "Vacunas", "Baño")
    LaunchedEffect(servicios) {
        if (servicioId == null && servicios.isNotEmpty() && servicioInicial != null && servicioInicial != -1) {
            val inicial = servicios.firstOrNull { it.id == servicioInicial }
            if (inicial != null) {
                vm.elegirServicio(inicial.id)
            }
        }
    }
    LaunchedEffect(mensaje) {
        mensaje?.let {
            snackbar.showSnackbar(it)
            vm.limpiarMensaje()
        }
    }
    LaunchedEffect(creada) {
        if (creada) onCreada()
    }

    val mascotaSel = mascotas.firstOrNull { it.id == mascotaId }
    val servicioSel = servicios.firstOrNull { it.id == servicioId }
    val hoy = LocalDate.now()
    val ahora = LocalTime.now()
    val formatoFecha = DateTimeFormatter.ofPattern("EEE d MMM", Locale.forLanguageTag("es-PE"))

    Scaffold(
        topBar = { AppTopBar(titulo = "Pedir cita", onVolver = onVolver) },
        snackbarHost = { SnackbarHost(snackbar) },
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {
            // ---------- ¿Quién viene? ----------
            Text("¿Quién viene?", fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            if (mascotas.isEmpty()) {
                Text("Primero registra una mascota para pedir una cita")
            } else {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(mascotas, key = { it.id }) { m ->
                        FilterChip(
                            selected = mascotaId == m.id,
                            onClick = { vm.elegirMascota(m.id) },
                            label = { Text(m.nombre) },
                            leadingIcon = { MascotaAvatar(especie = m.especie, tamano = 28.dp) },
                            shape = RoundedCornerShape(50)
                        )
                    }
                }
            }
            if (!aceptaCitas) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFEE2E2)),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text(
                        "La clínica no está recibiendo citas",
                        color = Color(0xFF991B1B),
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(14.dp)
                    )
                }
                Spacer(Modifier.height(16.dp))
            }

            Spacer(Modifier.height(20.dp))

            // Orden estricto: 1. Consulta, 2. Vacunas, 3. Baño
            val serviciosOrdenados = remember(servicios) {
                servicios.sortedBy { s ->
                    val n = s.nombre.lowercase()
                    when {
                        n.contains("consulta") -> 1
                        n.contains("vacun") -> 2
                        n.contains("baño") || n.contains("bano") || n.contains("corte") -> 3
                        else -> 4
                    }
                }
            }

            // ---------- Servicio ----------
            Text("Servicio", fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                serviciosOrdenados.forEach { s ->
                    ServicioOpcion(
                        servicio = s,
                        seleccionado = servicioId == s.id,
                        onClick = { vm.elegirServicio(s.id) }
                    )
                }
            }

            Spacer(Modifier.height(20.dp))

            // ---------- Fecha ----------
            Text("Fecha", fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(vm.fechas) { f ->
                    FechaChip(
                        fecha = f,
                        seleccionada = f == fecha,
                        onClick = { vm.elegirFecha(f) }
                    )
                }
            }

            Spacer(Modifier.height(20.dp))

            // ---------- Hora ----------
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Hora", fontWeight = FontWeight.Bold)
                Text("Ocupado = gris", style = MaterialTheme.typography.labelSmall)
            }
            Spacer(Modifier.height(8.dp))
            Horarios.todos.chunked(4).forEach { fila ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    fila.forEach { h ->
                        // Deshabilitada si está ocupada, o si es hoy y ya pasó
                        val pasada = fecha == hoy && !LocalTime.parse(h).isAfter(ahora)
                        HoraChip(
                            hora = h,
                            seleccionada = hora == h,
                            ocupado = h in ocupadas || pasada,
                            onClick = { vm.elegirHora(h) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    repeat(4 - fila.size) { Spacer(Modifier.weight(1f)) }
                }
                Spacer(Modifier.height(8.dp))
            }

            Spacer(Modifier.height(12.dp))

            // ---------- Resumen ----------
            if (mascotaSel != null && servicioSel != null && hora != null) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            "${mascotaSel.nombre} · ${servicioSel.nombre}",
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            "${fecha.format(formatoFecha).replaceFirstChar { it.uppercase() }}" +
                                    " · $hora · S/ ${"%.2f".format(servicioSel.precio)}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
                Spacer(Modifier.height(16.dp))
            }

            VetButton(
                texto = "Confirmar cita",
                onClick = { vm.confirmar() },
                enabled = aceptaCitas && mascotaId != null && servicioId != null && hora != null
            )
        }
    }
}

@Composable
private fun ServicioOpcion(servicio: Servicio, seleccionado: Boolean, onClick: () -> Unit) {
    val colores = MaterialTheme.colorScheme
    val resId = when {
        servicio.nombre.contains("consulta", ignoreCase = true) -> R.drawable.ic_servicio_consulta
        servicio.nombre.contains("vacun", ignoreCase = true) -> R.drawable.ic_servicio_vacunas
        servicio.nombre.contains("baño", ignoreCase = true) || servicio.nombre.contains("bano", ignoreCase = true) || servicio.nombre.contains("corte", ignoreCase = true) -> R.drawable.ic_servicio_bano
        else -> R.drawable.ic_servicio_consulta
    }
    val colorIcono = Color(0xFF059669)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = if (seleccionado) colorIcono.copy(alpha = 0.12f) else colores.surface
        ),
        border = BorderStroke(
            if (seleccionado) 2.dp else 1.dp,
            if (seleccionado) colorIcono else colores.outlineVariant
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(colorIcono.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = resId),
                    contentDescription = servicio.nombre,
                    tint = colorIcono,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = servicio.nombre,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${servicio.duracionMin} min · ${servicio.descripcion}",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Text(
                text = "S/ ${servicio.precio.toInt()}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = colorIcono
            )
        }
    }
}