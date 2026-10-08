package com.rocha.saludplus.ui.agendamiento

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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

// Recibe 3 parámetros desde la ruta: medicoId, fecha y hora
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ConfirmarCitaScreen(medicoId: Int, fecha: String, hora: String, navController: NavController) {
    val medico = Repositorio.obtenerMedico(medicoId)
    val especialidad = medico?.let { Repositorio.obtenerEspecialidad(it.especialidadId) }
    var motivo by remember { mutableStateOf("") }
    var mensaje by remember { mutableStateOf("") }

    Scaffold(
        topBar = { BarraSuperior("Confirmar cita", onVolver = { navController.popBackStack() }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {
            // Tarjeta de información del médico
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Avatar circular del médico
                    Surface(
                        modifier = Modifier.size(60.dp),
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primaryContainer
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(36.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    // Nombre, especialidad y código CMP
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = medico?.nombre ?: "",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = especialidad?.nombre ?: "",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "CMP: 123456",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Tarjeta con resumen de la cita e iconografía
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
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
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    FilaDato(
                        etiqueta = "Fecha",
                        valor = formatearFechaLarga(fecha),
                        icono = Icons.Default.DateRange
                    )
                    FilaDato(
                        etiqueta = "Hora",
                        valor = "$hora hrs",
                        icono = Icons.Default.Schedule
                    )
                    FilaDato(
                        etiqueta = "Tipo de atención",
                        valor = "Consulta presencial",
                        icono = Icons.Default.Person
                    )
                    FilaDato(
                        etiqueta = "Lugar de atención",
                        valor = "Av. Los Olivos 123, Lima",
                        icono = Icons.Default.LocationOn
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Campo de motivo de consulta con placeholder y bordes redondeados
            CampoTexto(
                valor = motivo,
                onValorCambia = { motivo = it },
                etiqueta = "Motivo de la consulta (opcional)",
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

            // Botón principal de agendamiento
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
