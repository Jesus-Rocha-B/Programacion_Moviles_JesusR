package com.rocha.registrodenotas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rocha.registrodenotas.ui.theme.RegistrodeNotasTheme
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RegistrodeNotasTheme {
                RegistroNotasScreen()
            }
        }
    }
}

// Estructura de datos para la observación
data class ObservacionInfo(
    val texto: String,
    val colorContenedor: Color,
    val colorTexto: Color = Color.White
)

// Función para obtener la observación según las reglas de negocio
fun obtenerObservacion(promedio: Float): ObservacionInfo {
    return when {
        promedio >= 17.0f -> ObservacionInfo(
            texto = "EXCELENTE",
            colorContenedor = Color(0xFF1B5E20) // Verde Oscuro
        )
        promedio >= 13.0f -> ObservacionInfo(
            texto = "APROBADO",
            colorContenedor = Color(0xFF4CAF50) // Verde Normal
        )
        promedio >= 10.0f -> ObservacionInfo(
            texto = "EN RECUPERACIÓN",
            colorContenedor = Color(0xFFFFC107), // Ámbar
            colorTexto = Color.Black
        )
        else -> ObservacionInfo(
            texto = "DESAPROBADO",
            colorContenedor = Color(0xFFD32F2F) // Rojo
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroNotasScreen() {
    // 1. Estados de notas (Float)
    var notaFundamentos by remember { mutableFloatStateOf(0f) }
    var notaPOO by remember { mutableFloatStateOf(0f) }
    var notaMoviles by remember { mutableFloatStateOf(0f) }
    var notaBD by remember { mutableFloatStateOf(0f) }

    // 2. Estados de control
    var redondear by remember { mutableStateOf(false) }
    var confirmado by remember { mutableStateOf(false) }
    var mostrarResultado by remember { mutableStateOf(false) }

    // 3. Valores CALCULADOS (derivados de los estados)
    val promedioPonderado = (notaFundamentos * 0.20f) + (notaPOO * 0.25f) + (notaMoviles * 0.30f) + (notaBD * 0.25f)
    val promedioFinal = if (redondear) kotlin.math.round(promedioPonderado) else promedioPonderado

    // Fondo con degradado suave
    val gradientBrush = Brush.verticalGradient(
        colors = listOf(
            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.25f),
            MaterialTheme.colorScheme.surface
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Registro de Notas",
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(gradientBrush)
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // 4 Sliders de Cursos con su badge en vivo
                CursoSlider(
                    nombreCurso = "Fundamentos",
                    peso = 20,
                    notaValue = notaFundamentos,
                    onNotaChange = {
                        notaFundamentos = it
                        mostrarResultado = false
                    }
                )

                CursoSlider(
                    nombreCurso = "POO",
                    peso = 25,
                    notaValue = notaPOO,
                    onNotaChange = {
                        notaPOO = it
                        mostrarResultado = false
                    }
                )

                CursoSlider(
                    nombreCurso = "Móviles",
                    peso = 30,
                    notaValue = notaMoviles,
                    onNotaChange = {
                        notaMoviles = it
                        mostrarResultado = false
                    }
                )

                CursoSlider(
                    nombreCurso = "BD",
                    peso = 25,
                    notaValue = notaBD,
                    onNotaChange = {
                        notaBD = it
                        mostrarResultado = false
                    }
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Controles adicionales (Switch y Checkbox)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Redondear promedio final")
                    Switch(
                        checked = redondear,
                        onCheckedChange = { redondear = it }
                    )
                }

                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = confirmado,
                            onCheckedChange = {
                                confirmado = it
                                if (!it) mostrarResultado = false
                            }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Confirmo que las notas son correctas")
                    }

                    // Mensaje verde de confirmación
                    if (confirmado) {
                        Text(
                            text = "✓ Notas confirmadas y listas para calcular",
                            color = Color(0xFF2E7D32),
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(start = 12.dp)
                        )
                    }
                }

                // Botones CALCULAR y LIMPIAR (Reto 3)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { mostrarResultado = true },
                        enabled = confirmado,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = "CALCULAR")
                    }

                    OutlinedButton(
                        onClick = {
                            notaFundamentos = 0f
                            notaPOO = 0f
                            notaMoviles = 0f
                            notaBD = 0f
                            redondear = false
                            confirmado = false
                            mostrarResultado = false
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = "LIMPIAR")
                    }
                }

                // Sección del Resultado (debajo de los botones)
                if (!mostrarResultado) {
                    Text(
                        text = "Asigna las notas y confirma para calcular",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    )
                } else {
                    ResultadoCard(
                        promedioPonderado = promedioPonderado,
                        promedioFinal = promedioFinal,
                        redondear = redondear
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Footer con nombre
                Text(
                    text = "Registro de Notas v1.0 • Desarrollado por Rocha",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                )
            }
        }
    }
}

@Composable
fun CursoSlider(
    nombreCurso: String,
    peso: Int,
    notaValue: Float,
    onNotaChange: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    // Reto 1: Aporte del curso al promedio total (nota * peso%)
    val aporte = notaValue * (peso / 100f)

    // Reto 2: Semáforo de color según el rango de nota
    val (badgeBgColor, badgeTextColor) = when {
        notaValue >= 17f -> Color(0xFF1B5E20) to Color.White // Verde Oscuro
        notaValue >= 13f -> Color(0xFF4CAF50) to Color.White // Verde Normal
        notaValue >= 10f -> Color(0xFFFFC107) to Color.Black // Ámbar
        else -> Color(0xFFD32F2F) to Color.White             // Rojo
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "$nombreCurso ($peso%)",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                // Badge con semáforo de color
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = badgeBgColor,
                    contentColor = badgeTextColor
                ) {
                    Text(
                        text = notaValue.toInt().toString(),
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Slider(
                value = notaValue,
                onValueChange = onNotaChange,
                valueRange = 0f..20f,
                steps = 19,
                modifier = Modifier.fillMaxWidth()
            )

            // Aporte individual del curso
            Text(
                text = "Aporte al promedio: +${String.format(Locale.US, "%.2f", aporte)} pts",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.align(Alignment.End)
            )
        }
    }
}

@Composable
fun ResultadoCard(
    promedioPonderado: Float,
    promedioFinal: Float,
    redondear: Boolean,
    modifier: Modifier = Modifier
) {
    val observacion = obtenerObservacion(promedioFinal)

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // a) Promedio ponderado: X.XX siempre con 2 decimales
            Text(
                text = "Promedio ponderado: ${String.format(Locale.US, "%.2f", promedioPonderado)}",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )

            // b) Promedio final: X en negrita (sin decimales si redondear, con 2 decimales si no)
            val textoPromedioFinal = if (redondear) {
                promedioFinal.toInt().toString()
            } else {
                String.format(Locale.US, "%.2f", promedioFinal)
            }

            Text(
                text = "Promedio final: $textoPromedioFinal",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            // c) Debajo del promedio final, el texto "(redondeado)" en gris pequeño, SOLO si redondear está activo
            if (redondear) {
                Text(
                    text = "(redondeado)",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // d) Un chip con el texto y color que devuelve obtenerObservacion(promedioFinal)
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = observacion.colorContenedor,
                contentColor = observacion.colorTexto
            ) {
                Text(
                    text = observacion.texto,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegistroNotasScreenPreview() {
    RegistrodeNotasTheme {
        RegistroNotasScreen()
    }
}
