package com.rocha.huellitasvet.ui.components.graficos

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun GraficoBarras(
    datos: List<DatoGrafico>,
    modifier: Modifier = Modifier,
    alturaMax: Dp = 110.dp
) {
    val maximo = (datos.maxOfOrNull { it.valor } ?: 0f).coerceAtLeast(1f)
    val colorDestacado = MaterialTheme.colorScheme.primary
    val colorBase = MaterialTheme.colorScheme.primaryContainer

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.Bottom
    ) {
        datos.forEach { d ->
            val esMaximo = d.valor == maximo && maximo > 0f
            val colorBarra = if (esMaximo) colorDestacado else colorBase
            val alturaCalculada = (alturaMax * (d.valor / maximo)).coerceAtLeast(12.dp)

            Column(
                modifier = Modifier.width(72.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "${d.valor.toInt()}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (esMaximo) colorDestacado else MaterialTheme.colorScheme.onSurface
                )
                Spacer(Modifier.height(4.dp))
                Box(
                    modifier = Modifier
                        .width(40.dp)
                        .height(alturaCalculada)
                        .background(
                            color = colorBarra,
                            shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)
                        )
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    text = d.etiqueta,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = if (esMaximo) FontWeight.Bold else FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
