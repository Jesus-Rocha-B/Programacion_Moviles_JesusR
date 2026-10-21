package com.rocha.huellitasvet.ui.theme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.rocha.huellitasvet.model.Rol

// Dueño: esmeralda. Veterinario: azul.
private val DuenoClaro = lightColorScheme(
    primary = Color(0xFF059669),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFD1FAE5),
    onPrimaryContainer = Color(0xFF064E3B),
    background = Color(0xFFF6FDF9),
    surface = Color(0xFFF6FDF9)
)

private val VetClaro = lightColorScheme(
    primary = Color(0xFF1D4ED8),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFDBEAFE),
    onPrimaryContainer = Color(0xFF1E3A8A),
    background = Color(0xFFF7F9FF),
    surface = Color(0xFFF7F9FF)
)

private val DuenoOscuro = darkColorScheme(
    primary = Color(0xFF34D399),
    onPrimary = Color(0xFF003824),
    primaryContainer = Color(0xFF065F46),
    onPrimaryContainer = Color(0xFFD1FAE5),
    background = Color(0xFF0F1A15),
    surface = Color(0xFF0F1A15)
)

private val VetOscuro = darkColorScheme(
    primary = Color(0xFF60A5FA),
    onPrimary = Color(0xFF0B1F4B),
    primaryContainer = Color(0xFF1E3A8A),
    onPrimaryContainer = Color(0xFFDBEAFE),
    background = Color(0xFF0E1424),
    surface = Color(0xFF0E1424)
)

@Composable
fun VetTheme(
    rol: Rol? = null,          // null = colores del dueño
    oscuro: Boolean = false,
    content: @Composable () -> Unit
) {
    val esVet = rol == Rol.ADMIN
    val colores = when {
        oscuro && esVet -> VetOscuro
        oscuro -> DuenoOscuro
        esVet -> VetClaro
        else -> DuenoClaro
    }
    MaterialTheme(
        colorScheme = colores,
        typography = Typography,
        content = content
    )
}