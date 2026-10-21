package com.rocha.huellitasvet.ui.components
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.rocha.huellitasvet.data.local.relation.CitaConDetalle
import com.rocha.huellitasvet.model.fechaLegible

@Composable
fun CitaCard(
    cita: CitaConDetalle,
    modifier: Modifier = Modifier,
    onCancelar: (() -> Unit)? = null   // null = no muestra el botón
) {
    val colores = MaterialTheme.colorScheme

    Card(
        modifier = modifier.fillMaxWidth(),
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
            // Ícono del servicio
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .background(colores.primaryContainer, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    iconoDeServicio(cita.servicio.icono),
                    contentDescription = null,
                    tint = colores.primary
                )
            }

            Spacer(Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(cita.servicio.nombre, fontWeight = FontWeight.Bold)
                Text(
                    "${cita.mascota.mascota.nombre} · ${cita.fechaLegible()}",
                    style = MaterialTheme.typography.bodySmall
                )
                Spacer(Modifier.size(6.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    EstadoCitaChip(estado = cita.cita.estado)
                    Text(
                        "S/ ${"%.2f".format(cita.cita.precio)}",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            if (onCancelar != null) {
                TextButton(onClick = onCancelar) {
                    Text("Cancelar", color = colores.error)
                }
            }
        }
    }
}