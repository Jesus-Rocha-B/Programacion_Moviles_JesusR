package com.rocha.huellitasvet.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.rocha.huellitasvet.R
import com.rocha.huellitasvet.model.Especie

@Composable
fun MascotaAvatar(
    especie: String,
    modifier: Modifier = Modifier,
    tamano: Dp = 56.dp
) {
    val resId = when (especie) {
        Especie.PERRO.name -> R.drawable.perro
        Especie.GATO.name -> R.drawable.gato
        else -> R.drawable.otros
    }

    val fondo = when (especie) {
        Especie.PERRO.name -> Color(0xFFFEF3C7)  // Crema suave para perro
        Especie.GATO.name -> Color(0xFFFFEDD5)   // Durazno / naranja suave para gato
        else -> Color(0xFFE0F2FE)                // Celeste / cian suave para otros
    }

    Box(
        modifier = modifier
            .size(tamano)
            .clip(CircleShape)
            .background(fondo),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = resId),
            contentDescription = especie,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .size(tamano * 0.82f)
                .padding(2.dp)
        )
    }
}
