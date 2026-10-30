package com.rocha.huellitasvet.ui.components.graficos

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.rocha.huellitasvet.model.RegistroPeso

@Composable
fun GraficoLineas(
    datos: List<RegistroPeso>,
    modifier: Modifier = Modifier,
    altura: Dp = 160.dp,
    lineaColor: Color = MaterialTheme.colorScheme.primary,
    puntoColor: Color = MaterialTheme.colorScheme.secondary
) {
    if (datos.isEmpty()) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(altura),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Sin atenciones con peso registrado",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        return
    }

    val minPeso = (datos.minOf { it.peso } * 0.9).coerceAtLeast(0.0).toFloat()
    val maxPeso = (datos.maxOf { it.peso } * 1.1).coerceAtLeast(minPeso.toDouble() + 1.0).toFloat()
    val rangoPeso = if (maxPeso - minPeso == 0f) 1f else maxPeso - minPeso

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "Evolución de Peso (kg)",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(Modifier.height(8.dp))

        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(altura)
                .padding(horizontal = 24.dp, vertical = 20.dp)
        ) {
            val width = size.width
            val height = size.height

            val paintPeso = Paint().apply {
                color = lineaColor.toArgb()
                textSize = 12.dp.toPx()
                isFakeBoldText = true
                textAlign = Paint.Align.CENTER
            }

            val paintFecha = Paint().apply {
                color = android.graphics.Color.parseColor("#64748B")
                textSize = 10.dp.toPx()
                textAlign = Paint.Align.CENTER
            }

            // Líneas de guía
            val gridLines = 3
            for (i in 0..gridLines) {
                val y = height * i / gridLines
                drawLine(
                    color = Color.Gray.copy(alpha = 0.2f),
                    start = Offset(0f, y),
                    end = Offset(width, y),
                    strokeWidth = 1.dp.toPx()
                )
            }

            val numPuntos = datos.size
            val pasoX = if (numPuntos > 1) width / (numPuntos - 1) else width / 2

            val puntos = datos.mapIndexed { index, item ->
                val x = if (numPuntos == 1) width / 2 else index * pasoX
                val y = height - ((item.peso.toFloat() - minPeso) / rangoPeso * height)
                Offset(x, y)
            }

            if (puntos.size > 1) {
                val path = Path().apply {
                    moveTo(puntos[0].x, puntos[0].y)
                    for (i in 1 until puntos.size) {
                        lineTo(puntos[i].x, puntos[i].y)
                    }
                }
                drawPath(
                    path = path,
                    color = lineaColor,
                    style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
                )
            }

            puntos.forEachIndexed { index, pt ->
                drawCircle(
                    color = puntoColor,
                    radius = 5.dp.toPx(),
                    center = pt
                )
                drawCircle(
                    color = Color.White,
                    radius = 2.5.dp.toPx(),
                    center = pt
                )

                drawContext.canvas.nativeCanvas.drawText(
                    "${datos[index].peso} kg",
                    pt.x,
                    pt.y - 10.dp.toPx(),
                    paintPeso
                )

                drawContext.canvas.nativeCanvas.drawText(
                    datos[index].fecha,
                    pt.x,
                    pt.y + 18.dp.toPx(),
                    paintFecha
                )
            }
        }
    }
}
