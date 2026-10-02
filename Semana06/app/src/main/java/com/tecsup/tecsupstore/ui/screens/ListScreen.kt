package com.tecsup.tecsupstore.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.tecsup.tecsupstore.navigation.Screen

@Composable
fun ListScreen(navController: NavController) {
    val elementos = listOf("Laptop Gaming", "Teclado Mecanico", "Mouse Inalambrico", "Monitor 4K")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Lista de Elementos",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(elementos) { elemento ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = elemento,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        IconButton(onClick = {
                            navController.navigate(Screen.Detail.createRoute(1))
                        }) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = "Ver detalle"
                            )
                        }
                    }
                }
            }
        }
    }
}