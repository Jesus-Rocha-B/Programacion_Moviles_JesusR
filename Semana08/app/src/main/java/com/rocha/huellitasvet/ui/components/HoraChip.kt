package com.rocha.huellitasvet.ui.components
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp

@Composable
fun HoraChip(
    hora: String,
    seleccionada: Boolean,
    ocupado: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colores = MaterialTheme.colorScheme
    val forma = RoundedCornerShape(12.dp)

    val fondo = when {
        ocupado -> colores.surfaceVariant.copy(alpha = 0.5f)
        seleccionada -> colores.primary
        else -> colores.surface
    }
    val colorTexto = when {
        ocupado -> colores.onSurface.copy(alpha = 0.38f)
        seleccionada -> colores.onPrimary
        else -> colores.onSurface
    }

    Box(
        modifier = modifier
            .height(44.dp)
            .clip(forma)
            .background(fondo)
            .border(1.dp, if (seleccionada) colores.primary else colores.outlineVariant, forma)
            .clickable(enabled = !ocupado, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = hora,
            color = colorTexto,
            fontWeight = FontWeight.SemiBold,
            textDecoration = if (ocupado) TextDecoration.LineThrough else TextDecoration.None
        )
    }
}