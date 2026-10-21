package com.rocha.huellitasvet.ui.components.graficos
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun GraficoDona(
    datos: List<DatoGrafico>,
    modifier: Modifier = Modifier,
    tamano: Dp = 130.dp,
    grosor: Dp = 22.dp
) {
    val total = datos.sumOf { it.valor.toDouble() }.toFloat()

    Box(modifier = modifier.size(tamano), contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val trazo = grosor.toPx()
            val diametro = size.minDimension - trazo
            val origen = Offset((size.width - diametro) / 2f, (size.height - diametro) / 2f)
            val tamanoArco = Size(diametro, diametro)

            if (total <= 0f) {
                // Sin datos: aro gris
                drawArc(
                    color = Color.LightGray.copy(alpha = 0.4f),
                    startAngle = 0f,
                    sweepAngle = 360f,
                    useCenter = false,
                    topLeft = origen,
                    size = tamanoArco,
                    style = Stroke(width = trazo)
                )
            } else {
                var inicio = -90f
                datos.forEach { d ->
                    val barrido = d.valor / total * 360f
                    if (barrido > 0f) {
                        drawArc(
                            color = d.color,
                            startAngle = inicio,
                            sweepAngle = barrido,
                            useCenter = false,
                            topLeft = origen,
                            size = tamanoArco,
                            style = Stroke(width = trazo)
                        )
                    }
                    inicio += barrido
                }
            }
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = total.toInt().toString(),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text("citas", style = MaterialTheme.typography.labelSmall)
        }
    }
}