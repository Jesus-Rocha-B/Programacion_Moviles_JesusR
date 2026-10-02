package com.tecsup.tecsupstore.ui.screens
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.util.Locale

data class Producto(
    val nombre: String,
    val precio: Double,
    val cantidad: Int
)

@Composable
fun TarjetaProducto(producto: Producto, onEliminar: () -> Unit) {
    // Estado para controlar si el menú desplegable está visible
    var expanded by remember { mutableStateOf(false) }

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
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = producto.nombre,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "S/ ${String.format(Locale.getDefault(), "%.2f", producto.precio)} x ${producto.cantidad}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.outline
                )
            }

            val importe = producto.precio * producto.cantidad
            Text(
                text = "S/ ${String.format(Locale.getDefault(), "%.2f", importe)}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(8.dp))

            // Box necesario para posicionar el DropdownMenu pegado al ícono
            Box {
                IconButton(onClick = { expanded = true }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "Opciones de producto"
                    )
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest =  { expanded = false}
                ) {
                    DropdownMenuItem(
                        text = {Text("Ver detalle")},
                        onClick = { expanded = false}
                    )
                    DropdownMenuItem(
                        text = {Text("Eliminar")},
                        onClick = {
                            expanded = false
                            onEliminar()
                        }
                    )
                }
            }
        }
    }
}