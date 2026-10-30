package com.rocha.huellitasvet.ui.dueno
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.rocha.huellitasvet.R
import com.rocha.huellitasvet.data.local.relation.CitaConDetalle
import com.rocha.huellitasvet.model.fechaLegible
import com.rocha.huellitasvet.ui.components.EstadoCitaChip
import com.rocha.huellitasvet.ui.components.MascotaAvatar
import com.rocha.huellitasvet.ui.components.VetButton
import com.rocha.huellitasvet.viewmodel.InicioViewModel

@Composable
fun InicioScreen(
    vm: InicioViewModel,
    nombre: String,
    onMascota: (Int) -> Unit,
    onAgregar: () -> Unit,
    onVerTodas: () -> Unit,
    onPedirCita: () -> Unit,
    onPedirCitaServicio: (Int) -> Unit = {},
    onNotificaciones: () -> Unit = {}
) {
    val mascotas by vm.mascotas.collectAsState()
    val proxima by vm.proximaCita.collectAsState()
    val controles by vm.proximosControles.collectAsState()
    val sinLeer by vm.sinLeerCount.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        // Saludo + Campana de Notificaciones estilo iOS
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Hola, $nombre",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "¿Cómo están tus mascotas hoy?",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFF1F5F9))
                    .clickable { onNotificaciones() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Notifications,
                    contentDescription = "Notificaciones",
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(22.dp)
                )
                if (sinLeer > 0) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFEF4444))
                            .align(Alignment.TopEnd)
                    )
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        // Tarjeta de próxima cita
        TarjetaProximaCita(proxima)

        if (controles.isNotEmpty()) {
            Spacer(Modifier.height(14.dp))
            TarjetaProximoControl(controles.first())
        }

        Spacer(Modifier.height(20.dp))

        // Mis mascotas
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Mis mascotas",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            TextButton(onClick = onVerTodas) { Text("Ver todas") }
        }

        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            items(mascotas, key = { it.id }) { m ->
                Card(
                    modifier = Modifier
                        .width(110.dp)
                        .height(138.dp)
                        .clickable { onMascota(m.id) },
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(2.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        MascotaAvatar(especie = m.especie, tamano = 58.dp)
                        Spacer(Modifier.height(4.dp))
                        Text(m.nombre, fontWeight = FontWeight.Bold, maxLines = 1)
                        Text("Ver ficha", style = MaterialTheme.typography.labelSmall)
                    }
                }
            }
            // Botón "Agregar" de las mismas dimensiones exactas (110dp x 138dp)
            item {
                Card(
                    modifier = Modifier
                        .width(110.dp)
                        .height(138.dp)
                        .clickable { onAgregar() },
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(12.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            Icons.Filled.Add,
                            contentDescription = "Agregar mascota",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            "Agregar",
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(20.dp))

        // Accesos rápidos a servicios
        Text(
            text = "Servicios",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            AccesoServicio(
                texto = "Consulta",
                resId = R.drawable.ic_servicio_consulta,
                colorIcono = Color(0xFF059669),
                colorFondo = Color(0xFFECFDF5),
                onClick = { onPedirCitaServicio(1) },
                modifier = Modifier.weight(1f)
            )
            AccesoServicio(
                texto = "Vacunas",
                resId = R.drawable.ic_servicio_vacunas,
                colorIcono = Color(0xFF059669),
                colorFondo = Color(0xFFECFDF5),
                onClick = { onPedirCitaServicio(2) },
                modifier = Modifier.weight(1f)
            )
            AccesoServicio(
                texto = "Baño y Corte",
                resId = R.drawable.ic_servicio_bano,
                colorIcono = Color(0xFF059669),
                colorFondo = Color(0xFFECFDF5),
                onClick = { onPedirCitaServicio(3) },
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(Modifier.height(24.dp))

        VetButton(texto = "Pedir una cita", onClick = onPedirCita)
    }
}

@Composable
private fun TarjetaProximaCita(cita: CitaConDetalle?) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
        shape = RoundedCornerShape(20.dp)
    ) {
        if (cita == null) {
            Column(Modifier.padding(20.dp)) {
                Text(
                    "Próxima cita",
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.labelMedium
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    "Aún no tienes citas",
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        } else {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        "Próxima cita",
                        color = MaterialTheme.colorScheme.onPrimary,
                        style = MaterialTheme.typography.labelMedium
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        "${cita.mascota.mascota.nombre} · ${cita.servicio.nombre}",
                        color = MaterialTheme.colorScheme.onPrimary,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        cita.fechaLegible(),
                        color = MaterialTheme.colorScheme.onPrimary,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(Modifier.height(8.dp))
                    EstadoCitaChip(estado = cita.cita.estado, fondoBlanco = true)
                }
                Box(
                    modifier = Modifier
                        .background(Color.White, RoundedCornerShape(16.dp))
                        .padding(6.dp)
                ) {
                    MascotaAvatar(especie = cita.mascota.mascota.especie, tamano = 52.dp)
                }
            }
        }
    }
}

@Composable
private fun TarjetaProximoControl(cita: CitaConDetalle) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    "Recordatorio de Próximo Control",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    "${cita.mascota.mascota.nombre} · Fecha de control: ${cita.cita.proximoControl}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
                Text(
                    "Servicio previo: ${cita.servicio.nombre}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }
    }
}

@Composable
private fun AccesoServicio(
    texto: String,
    resId: Int,
    colorIcono: Color,
    colorFondo: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = colorFondo),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, colorIcono.copy(alpha = 0.2f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(colorIcono.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = resId),
                    contentDescription = texto,
                    tint = colorIcono,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(Modifier.height(8.dp))
            Text(
                text = texto,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}