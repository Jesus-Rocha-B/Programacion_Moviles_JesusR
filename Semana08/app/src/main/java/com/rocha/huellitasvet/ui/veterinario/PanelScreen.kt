package com.rocha.huellitasvet.ui.veterinario

import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Event
import androidx.compose.material.icons.outlined.HourglassEmpty
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.rocha.huellitasvet.ui.components.graficos.GraficoBarras
import com.rocha.huellitasvet.ui.components.graficos.GraficoDona
import com.rocha.huellitasvet.viewmodel.PanelViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun PanelScreen(vm: PanelViewModel, onVerAgenda: () -> Unit) {
    val citasHoy by vm.citasHoy.collectAsState()
    val pendientes by vm.pendientes.collectAsState()
    val atendidas by vm.atendidas.collectAsState()
    val ingresos by vm.ingresosMes.collectAsState()
    val porEstado by vm.porEstado.collectAsState()
    val porServicio by vm.porServicio.collectAsState()

    val fecha = remember {
        LocalDate.now()
            .format(DateTimeFormatter.ofPattern("EEE d MMM", Locale.forLanguageTag("es-PE")))
            .replaceFirstChar { it.uppercase() }
    }
    val colores = MaterialTheme.colorScheme

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        Text(
            "Panel de la clínica",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Text("Hoy · $fecha", style = MaterialTheme.typography.bodyMedium)

        Spacer(Modifier.height(16.dp))

        // 3 contadores de color estilizados con íconos y texto de alta legibilidad
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Contador(citasHoy, "Citas hoy", Icons.Outlined.Event, colores.primaryContainer, colores.primary, Modifier.weight(1f))
            Contador(pendientes, "Pendientes", Icons.Outlined.HourglassEmpty, Color(0xFFFEF3C7), Color(0xFFD97706), Modifier.weight(1f))
            Contador(atendidas, "Atendidas", Icons.Outlined.CheckCircle, Color(0xFFF3E8FF), Color(0xFF7E22CE), Modifier.weight(1f))
        }

        Spacer(Modifier.height(16.dp))

        // Tarjeta de ingresos del mes
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = colores.primary),
            shape = RoundedCornerShape(20.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Column {
                    Text(
                        "Ingresos del mes",
                        color = colores.onPrimary,
                        style = MaterialTheme.typography.labelMedium
                    )
                    Text(
                        "S/ " + String.format(Locale.US, "%,.2f", ingresos),
                        color = colores.onPrimary,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(
                    "$atendidas atenciones",
                    color = colores.onPrimary,
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }

        Spacer(Modifier.height(20.dp))

        // Citas por estado (dona) con leyenda perfectamente alineada
        Text("Citas por estado", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = colores.surface),
            elevation = CardDefaults.cardElevation(2.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                GraficoDona(datos = porEstado)
                Spacer(Modifier.width(16.dp))
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    porEstado.forEach { d ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .background(d.color, CircleShape)
                                )
                                Spacer(Modifier.width(8.dp))
                                Text(
                                    text = d.etiqueta,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                            Text(
                                text = "${d.valor.toInt()}",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(20.dp))

        // Citas por servicio (barras)
        Text("Citas por servicio", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = colores.surface),
            elevation = CardDefaults.cardElevation(2.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Box(modifier = Modifier.padding(16.dp)) {
                if (porServicio.isEmpty()) {
                    Text("Aún no hay citas")
                } else {
                    GraficoBarras(datos = porServicio)
                }
            }
        }

        // Alerta de citas por confirmar
        if (pendientes > 0) {
            Spacer(Modifier.height(20.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF3C7)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Filled.Warning, contentDescription = null, tint = Color(0xFFD97706))
                    Spacer(Modifier.width(10.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            "$pendientes citas por confirmar",
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF78350F)
                        )
                        Text(
                            "Revisa la agenda",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF78350F)
                        )
                    }
                    Button(
                        onClick = onVerAgenda,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD97706)),
                        shape = RoundedCornerShape(50)
                    ) { Text("Ver") }
                }
            }
        }
    }
}

@Composable
private fun Contador(
    valor: Int,
    etiqueta: String,
    icono: ImageVector,
    fondo: Color,
    colorTexto: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = fondo),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icono,
                contentDescription = null,
                tint = colorTexto,
                modifier = Modifier.size(20.dp)
            )
            Spacer(Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = valor.toString(),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = colorTexto
                )
                Text(
                    text = etiqueta,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = colorTexto,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
