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
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
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
                    onNotaChange = { notaFundamentos = it }
                )

                CursoSlider(
                    nombreCurso = "POO",
                    peso = 25,
                    notaValue = notaPOO,
                    onNotaChange = { notaPOO = it }
                )

                CursoSlider(
                    nombreCurso = "Móviles",
                    peso = 30,
                    notaValue = notaMoviles,
                    onNotaChange = { notaMoviles = it }
                )

                CursoSlider(
                    nombreCurso = "BD",
                    peso = 25,
                    notaValue = notaBD,
                    onNotaChange = { notaBD = it }
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Controles adicionales (Switch y Checkbox sin conectar cálculos aún)
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

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = confirmado,
                        onCheckedChange = { confirmado = it }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Confirmo que las notas son correctas")
                }

                Button(
                    onClick = { mostrarResultado = true },
                    enabled = confirmado,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "CALCULAR")
                }
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

                // Badge con la nota en vivo
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ) {
                    Text(
                        text = String.format(Locale.US, "%.1f", notaValue),
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Slider(
                value = notaValue,
                onValueChange = onNotaChange,
                valueRange = 0f..20f,
                steps = 19,
                modifier = Modifier.fillMaxWidth()
            )
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
