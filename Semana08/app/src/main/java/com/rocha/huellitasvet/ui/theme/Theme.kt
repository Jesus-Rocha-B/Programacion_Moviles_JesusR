package com.rocha.huellitasvet.ui.theme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.rocha.huellitasvet.model.Rol

// Dueño: esmeralda. Veterinario: azul. Mismo fondo blanco limpio para ambos.
private val DuenoClaro = lightColorScheme(
    primary = Color(0xFF059669),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFD1FAE5),
    onPrimaryContainer = Color(0xFF064E3B),
    secondary = Color(0xFF10B981),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFECFDF5),
    onSecondaryContainer = Color(0xFF047857),
    background = Color(0xFFF8FAFC), // Mismo blanco/gris limpio que Veterinario
    surface = Color(0xFFFFFFFF),
    surfaceVariant = Color(0xFFF1F5F9),
    onSurfaceVariant = Color(0xFF64748B),
    outline = Color(0xFFCBD5E1),
    outlineVariant = Color(0xFFE2E8F0)
)

private val VetClaro = lightColorScheme(
    primary = Color(0xFF1D4ED8), // AZUL para Veterinario
    onPrimary = Color.White,
    primaryContainer = Color(0xFFDBEAFE),
    onPrimaryContainer = Color(0xFF1E3A8A),
    secondary = Color(0xFF2563EB),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFEFF6FF),
    onSecondaryContainer = Color(0xFF1D4ED8),
    background = Color(0xFFF8FAFC), // Mismo blanco/gris limpio que Dueño
    surface = Color(0xFFFFFFFF),
    surfaceVariant = Color(0xFFF1F5F9),
    onSurfaceVariant = Color(0xFF64748B),
    outline = Color(0xFFCBD5E1),
    outlineVariant = Color(0xFFE2E8F0)
)

private val DuenoOscuro = darkColorScheme(
    primary = Color(0xFF34D399),
    onPrimary = Color(0xFF003824),
    primaryContainer = Color(0xFF065F46),
    onPrimaryContainer = Color(0xFFD1FAE5),
    secondary = Color(0xFF10B981),
    onSecondary = Color.Black,
    secondaryContainer = Color(0xFF064E3B),
    onSecondaryContainer = Color(0xFFD1FAE5),
    background = Color(0xFF0F1A15),
    surface = Color(0xFF0F1A15),
    surfaceVariant = Color(0xFF1E293B),
    onSurfaceVariant = Color(0xFF94A3B8),
    outline = Color(0xFF334155),
    outlineVariant = Color(0xFF1E293B)
)

private val VetOscuro = darkColorScheme(
    primary = Color(0xFF60A5FA), // AZUL para Veterinario en oscuro
    onPrimary = Color(0xFF0B1F4B),
    primaryContainer = Color(0xFF1E3A8A),
    onPrimaryContainer = Color(0xFFDBEAFE),
    secondary = Color(0xFF3B82F6),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFF1E3A8A),
    onSecondaryContainer = Color(0xFFDBEAFE),
    background = Color(0xFF0F172A),
    surface = Color(0xFF0F172A),
    surfaceVariant = Color(0xFF1E293B),
    onSurfaceVariant = Color(0xFF94A3B8),
    outline = Color(0xFF334155),
    outlineVariant = Color(0xFF1E293B)
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