package com.rocha.huellitasvet.ui.components
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import java.time.format.TextStyle as EstiloDia
import java.util.Locale

@Composable
fun FechaChip(
    fecha: LocalDate,
    seleccionada: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colores = MaterialTheme.colorScheme
    val forma = RoundedCornerShape(14.dp)
    val dia = fecha.dayOfWeek
        .getDisplayName(EstiloDia.SHORT, Locale.forLanguageTag("es-PE"))
        .trimEnd('.')
        .replaceFirstChar { it.uppercase() }
    val colorTexto = if (seleccionada) colores.onPrimary else colores.onSurface

    Column(
        modifier = modifier
            .width(56.dp)
            .clip(forma)
            .background(if (seleccionada) colores.primary else colores.surface)
            .border(1.dp, if (seleccionada) colores.primary else colores.outlineVariant, forma)
            .clickable(onClick = onClick)
            .padding(vertical = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(dia, color = colorTexto, style = MaterialTheme.typography.labelSmall)
        Text(
            fecha.dayOfMonth.toString(),
            color = colorTexto,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
    }
}