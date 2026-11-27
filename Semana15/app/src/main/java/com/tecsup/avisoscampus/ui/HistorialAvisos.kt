package com.tecsup.avisoscampus.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tecsup.avisoscampus.model.Aviso
import com.tecsup.avisoscampus.ui.theme.AzulTecsup
import com.tecsup.avisoscampus.ui.theme.GrisBorde
import com.tecsup.avisoscampus.ui.theme.TextoOscuro
import com.tecsup.avisoscampus.ui.theme.TextoSecundario

@Composable
fun HistorialAvisos(avisos: List<Aviso>) {
    TarjetaSeccion(titulo = "Historial") {
        val recientes = avisos.take(20)
        if (recientes.isEmpty()) {
            Text(
                text = "Sin avisos todavía",
                style = MaterialTheme.typography.bodyMedium,
                color = TextoSecundario
            )
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                recientes.forEachIndexed { indice, aviso ->
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                color = AzulTecsup.copy(alpha = 0.10f),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = aviso.origen.texto,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    style = MaterialTheme.typography.labelMedium,
                                    color = AzulTecsup
                                )
                            }
                            Text(
                                text = aviso.hora,
                                style = MaterialTheme.typography.labelMedium,
                                color = TextoSecundario
                            )
                        }
                        Text(
                            text = aviso.titulo,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = TextoOscuro
                        )
                        Text(
                            text = aviso.texto,
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextoSecundario
                        )
                    }
                    if (indice < recientes.lastIndex) {
                        HorizontalDivider(color = GrisBorde)
                    }
                }
            }
        }
    }
}
