package com.rocha.saludplus.ui.agendamiento

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.rocha.saludplus.navigation.Rutas
import com.rocha.saludplus.repository.Repositorio
import com.rocha.saludplus.ui.components.BarraSuperior
import com.rocha.saludplus.ui.components.BotonPrincipal
import com.rocha.saludplus.ui.components.CampoTexto
import com.rocha.saludplus.ui.components.FilaDato
import com.rocha.saludplus.ui.components.formatearFechaLarga

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ConfirmarCitaScreen(medicoId: Int, fecha: String, hora: String, navController: NavController) {
    val medico = Repositorio.obtenerMedico(medicoId)
    val especialidad = medico?.let { Repositorio.obtenerEspecialidad(it.especialidadId) }
    var motivo by remember { mutableStateOf("") }
    var mensaje by remember { mutableStateOf("") }

    // Rango de hora formateado
    val rangoHora = if (hora.contains(":")) {
        val horaInicio = hora.take(2).toIntOrNull() ?: 9
        val horaFin = (horaInicio + 1).toString().padStart(2, '0')
        "$hora a $horaFin:${hora.takeLast(2)}"
    } else {
        "$hora hrs"
    }

    Scaffold(
        topBar = { BarraSuperior("Confirmar cita", onVolver = { navController.popBackStack() }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {
            // Tarjeta Superior del Médico en fondo celeste suave (#F4F7FC)
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFF4F7FC)
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = CircleShape,
                        modifier = Modifier.size(64.dp),
                        border = BorderStroke(2.dp, Color.White),
                        shadowElevation = 2.dp,
                        color = Color(0xFFE6F2FF)
                    ) {
                        if (medico?.fotoResId != null && medico.fotoResId != 0) {
                            Image(
                                painter = painterResource(id = medico.fotoResId),
                                contentDescription = medico.nombre,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        } else {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = null,
                                    tint = Color(0xFF1877F2),
                                    modifier = Modifier.size(36.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = medico?.nombre ?: "",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F1E36)
                        )
                        Text(
                            text = especialidad?.nombre ?: "",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF758A99)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "CMP: 123456",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF758A99)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Tarjeta de Resumen con Iconografía Azul Integrada
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(1.dp, Color(0xFFEFEFEF)),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Detalles de la cita",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F1E36)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    FilaDato(
                        etiqueta = "Fecha",
                        valor = formatearFechaLarga(fecha),
                        icono = Icons.Default.DateRange
                    )
                    FilaDato(
                        etiqueta = "Hora",
                        valor = rangoHora,
                        icono = Icons.Default.Schedule
                    )
                    FilaDato(
                        etiqueta = "Tipo de atención",
                        valor = "Consulta presencial",
                        icono = Icons.Default.Person
                    )
                    FilaDato(
                        etiqueta = "Dirección",
                        valor = "Av. Los Olivos 123 \n Lima",
                        icono = Icons.Default.LocationOn
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Campo de motivo de consulta
            Text(
                text = "Motivo de la consulta (opcional)",
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF758A99),
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(4.dp))

            CampoTexto(
                valor = motivo,
                onValorCambia = { motivo = it },
                etiqueta = "Motivo de consulta",
                placeholder = "Consulta de rutina"
            )

            if (mensaje.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = mensaje,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón principal
            BotonPrincipal(
                texto = "Agendar cita",
                onClick = {
                    val cita = Repositorio.agendarCita(medicoId, fecha, hora)
                    if (cita != null) {
                        navController.navigate(Rutas.citaExitosa(cita.id)) {
                            popUpTo(Rutas.HOME)
                        }
                    } else {
                        mensaje = "Ese horario ya fue reservado, elige otro"
                    }
                }
            )
        }
    }
}
