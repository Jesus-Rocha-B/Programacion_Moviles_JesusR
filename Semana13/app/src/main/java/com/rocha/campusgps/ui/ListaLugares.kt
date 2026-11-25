package com.rocha.campusgps.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.rocha.campusgps.model.LugarConDistancia
import com.rocha.campusgps.ui.theme.TecsupBorder
import com.rocha.campusgps.ui.theme.TecsupCyan
import com.rocha.campusgps.ui.theme.TecsupCyanContainer
import com.rocha.campusgps.ui.theme.TecsupCyanDark
import com.rocha.campusgps.ui.theme.TecsupCyanLight
import com.rocha.campusgps.ui.theme.TecsupTextPrimary
import com.rocha.campusgps.ui.theme.TecsupTextSecondary
import com.rocha.campusgps.util.formatearDistancia

@Composable
fun ListaLugares(
    lugares: List<LugarConDistancia>,
    seleccionado: Int?,
    onSeleccionar: (Int) -> Unit,
    modoOscuro: Boolean = false,
    modifier: Modifier = Modifier
) {
    val fondoLista = if (modoOscuro) Color(0xFF1E293B) else Color.White
    val colorBordeCard = if (modoOscuro) Color(0xFF334155) else TecsupBorder
    val colorTextoPrincipal = if (modoOscuro) Color(0xFFF8FAFC) else TecsupTextPrimary
    val colorTextoSecundario = if (modoOscuro) Color(0xFF94A3B8) else TecsupTextSecondary

    if (lugares.isEmpty()) {
        Box(
            modifier = modifier
                .background(fondoLista)
                .fillMaxWidth()
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "No se encontraron lugares en este rango",
                style = MaterialTheme.typography.bodyMedium,
                color = colorTextoSecundario
            )
        }
    } else {
        LazyColumn(
            modifier = modifier.background(fondoLista),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(lugares, key = { it.lugar.id }) { item ->
            val esSeleccionado = item.lugar.id == seleccionado

            val fondoCard = when {
                esSeleccionado && modoOscuro -> Color(0xFF0F172A)
                esSeleccionado && !modoOscuro -> TecsupCyanContainer
                !esSeleccionado && modoOscuro -> Color(0xFF1E293B)
                else -> Color.White
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSeleccionar(item.lugar.id) },
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = fondoCard
                ),
                border = if (esSeleccionado) {
                    BorderStroke(1.5.dp, TecsupCyan)
                } else {
                    BorderStroke(1.dp, colorBordeCard)
                },
                elevation = CardDefaults.cardElevation(defaultElevation = if (esSeleccionado) 2.dp else 0.5.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Punto indicador celeste Tecsup
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .background(
                                if (esSeleccionado) TecsupCyan else TecsupCyan.copy(alpha = 0.5f),
                                CircleShape
                            )
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = item.lugar.nombre,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = colorTextoPrincipal,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = item.lugar.categoria,
                            style = MaterialTheme.typography.bodySmall,
                            color = colorTextoSecundario
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    val textoDistancia = item.metros?.let { formatearDistancia(it) } ?: "Sin calcular"
                    Text(
                        text = textoDistancia,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = if (item.metros != null) {
                            if (modoOscuro) TecsupCyanLight else TecsupCyanDark
                        } else colorTextoSecundario
                    )
                }
            }
        }
    }
}
}
