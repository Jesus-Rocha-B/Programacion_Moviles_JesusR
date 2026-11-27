package com.tecsup.avisoscampus.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = AzulTecsup,
    onPrimary = Color.White,
    background = FondoGrisClaro,
    surface = Color.White,
    onSurface = TextoOscuro,
    error = RojoDenegado,
    errorContainer = FondoError,
    onErrorContainer = RojoDenegado
)

@Composable
fun AvisosCampusTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        content = content
    )
}