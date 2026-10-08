package com.rocha.saludplus.ui.resultados

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.rocha.saludplus.ui.components.*

// Modelo propio de esta pantalla
data class Resultado(
    val id: Int,
    val examen: String,
    val fecha: String,
    val estado: String
)

@Composable
fun ResultadosScreen(navController: NavController) {
    // Lista fija de resultados
    val resultados = listOf(
        Resultado(1, "Hemograma completo", "20/09/2026", "Normal"),
        Resultado(2, "Perfil lipídico", "28/09/2026", "Consultar con el médico"),
        Resultado(3, "Examen de orina", "02/10/2026", "Normal")
    )

    Scaffold(
        topBar = { BarraSuperior("Resultados", onVolver = { navController.popBackStack() }) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(resultados) { resultado ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = resultado.examen, style = MaterialTheme.typography.titleMedium)
                        Text(
                            text = resultado.fecha,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(text = "Estado: ${resultado.estado}")
                    }
                }
            }
        }
    }
}