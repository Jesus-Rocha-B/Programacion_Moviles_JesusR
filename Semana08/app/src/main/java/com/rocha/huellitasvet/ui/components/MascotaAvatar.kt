package com.rocha.huellitasvet.ui.components
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.rocha.huellitasvet.model.Especie

@Composable
fun MascotaAvatar(
    especie: String,
    modifier: Modifier = Modifier,
    tamano: Dp = 56.dp
) {
    val fondo: Color
    val pelaje: Color
    when (especie) {
        Especie.PERRO.name -> { fondo = Color(0xFFFDEBD0); pelaje = Color(0xFFC98A4B) }
        Especie.GATO.name -> { fondo = Color(0xFFE5E7EB); pelaje = Color(0xFF6B7280) }
        else -> { fondo = Color(0xFFDBEAFE); pelaje = Color(0xFF60A5FA) }
    }
    val oscuro = Color(0xFF1F2937)

    Canvas(modifier = modifier.size(tamano).clip(CircleShape)) {
        val w = size.width
        val h = size.height

        drawRect(fondo)

        // Orejas (según la especie)
        when (especie) {
            Especie.PERRO.name -> {
                val oreja = pelaje.copy(alpha = 0.8f)
                drawOval(oreja, topLeft = Offset(w * 0.10f, h * 0.20f), size = Size(w * 0.26f, h * 0.44f))
                drawOval(oreja, topLeft = Offset(w * 0.64f, h * 0.20f), size = Size(w * 0.26f, h * 0.44f))
            }
            Especie.GATO.name -> {
                val izquierda = Path().apply {
                    moveTo(w * 0.22f, h * 0.52f)
                    lineTo(w * 0.24f, h * 0.14f)
                    lineTo(w * 0.48f, h * 0.34f)
                    close()
                }
                val derecha = Path().apply {
                    moveTo(w * 0.78f, h * 0.52f)
                    lineTo(w * 0.76f, h * 0.14f)
                    lineTo(w * 0.52f, h * 0.34f)
                    close()
                }
                drawPath(izquierda, pelaje)
                drawPath(derecha, pelaje)
            }
            else -> {
                drawCircle(pelaje, radius = w * 0.10f, center = Offset(w * 0.27f, h * 0.27f))
                drawCircle(pelaje, radius = w * 0.10f, center = Offset(w * 0.73f, h * 0.27f))
            }
        }

        // Cara
        drawCircle(pelaje, radius = w * 0.30f, center = Offset(w / 2f, h * 0.56f))
        // Ojos
        drawCircle(oscuro, radius = w * 0.04f, center = Offset(w * 0.41f, h * 0.52f))
        drawCircle(oscuro, radius = w * 0.04f, center = Offset(w * 0.59f, h * 0.52f))
        // Nariz
        drawCircle(oscuro, radius = w * 0.05f, center = Offset(w / 2f, h * 0.64f))
    }
}