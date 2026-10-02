package com.tecsup.tecsupstore.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.tecsup.tecsupstore.navigation.Screen
import java.util.Locale

data class ItemProducto(
    val id: Int,
    val nombre: String,
    val precio: Double
)

@Composable
fun ItemProductoCard(
    producto: ItemProducto,
    navController: NavController,
    mostrarMenuEjemplo: Boolean = false
) {
    var expanded by remember { mutableStateOf(mostrarMenuEjemplo) }

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
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.ShoppingBag,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(32.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = producto.nombre,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "S/ ${String.format(Locale.getDefault(), "%.2f", producto.precio)}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.outline
                )
            }

            Box {
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "Opciones"
                    )
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Ver detalle") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = null
                            )
                        },
                        onClick = {
                            expanded = false
                            navController.navigate(Screen.Detail.createRoute(producto.id))
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Favoritos") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.FavoriteBorder,
                                contentDescription = null
                            )
                        },
                        onClick = {
                            expanded = false
                        }
                    )
                    HorizontalDivider()
                    DropdownMenuItem(
                        text = { Text("Eliminar", color = MaterialTheme.colorScheme.error) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.error
                            )
                        },
                        onClick = {
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun HomeScreen(navController: NavController) {
    val productos = remember {
        listOf(
            ItemProducto(1, "Audífonos Bluetooth", 89.00),
            ItemProducto(2, "Smartwatch Deportivo", 199.00),
            ItemProducto(3, "Funda de Celular", 25.00),
            ItemProducto(4, "Cargador Rápido Type-C", 45.00)
        )
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // Cabecera sencilla con el título de la tienda
        Surface(
            color = MaterialTheme.colorScheme.primaryContainer,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "TECSUP Store",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Más vendidos",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                )
            }
        }

        // Lista perezosa (LazyColumn) de productos
        LazyColumn(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(productos) { producto ->
                ItemProductoCard(
                    producto = producto,
                    navController = navController,
                    mostrarMenuEjemplo = (producto.id == 1)
                )
            }
        }
    }
}
