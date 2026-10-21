package com.rocha.huellitasvet.ui.components
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.rocha.huellitasvet.model.EstadoCita
import com.rocha.huellitasvet.model.nombreBonito

fun colorDeEstado(estado: String): Color = when (estado) {
    EstadoCita.PENDIENTE.name -> Color(0xFFD97706)   // ámbar
    EstadoCita.CONFIRMADA.name -> Color(0xFF16A34A)  // verde
    EstadoCita.ATENDIDA.name -> Color(0xFF475569)    // gris
    else -> Color(0xFFDC2626)                        // rojo (cancelada)
}

@Composable
fun EstadoCitaChip(
    estado: String,
    modifier: Modifier = Modifier,
    fondoBlanco: Boolean = false
) {
    val color = colorDeEstado(estado)
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(50),
        color = if (fondoBlanco) Color.White else color.copy(alpha = 0.15f)
    ) {
        Text(
            text = estado.nombreBonito(),
            color = color,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp)
        )
    }
}