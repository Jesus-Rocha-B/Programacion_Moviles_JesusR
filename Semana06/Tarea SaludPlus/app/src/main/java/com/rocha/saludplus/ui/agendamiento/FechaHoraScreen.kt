package com.rocha.saludplus.ui.agendamiento

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.rocha.saludplus.navigation.Rutas
import com.rocha.saludplus.repository.Repositorio
import com.rocha.saludplus.ui.components.BarraSuperior
import com.rocha.saludplus.ui.components.BotonPrincipal
import com.rocha.saludplus.ui.components.ChipDia
import com.rocha.saludplus.ui.components.ChipHorario
import com.rocha.saludplus.ui.components.MensajeVacio
import com.rocha.saludplus.ui.components.diasHabiles
import com.rocha.saludplus.ui.components.nombreDiaCorto
import com.rocha.saludplus.ui.components.nombreMes

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FechaHoraScreen(medicoId: Int, navController: NavController) {
    val medico = Repositorio.obtenerMedico(medicoId)
    val especialidad = medico?.let { Repositorio.obtenerEspecialidad(it.especialidadId) }

    var semana by remember { mutableIntStateOf(0) }
    val dias = diasHabiles(semana)

    var fechaSeleccionada by remember { mutableStateOf(dias[0].toString()) }
    var horaSeleccionada by remember { mutableStateOf("") }

    val horarios = Repositorio.horariosDisponibles(medicoId, fechaSeleccionada)

    Scaffold(
        topBar = { BarraSuperior("Seleccionar fecha y hora", onVolver = { navController.popBackStack() }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {
            // Tarjeta superior del médico seleccionada con foto real
            if (medico != null) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(1.dp, Color(0xFFEFEFEF)),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
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
                            modifier = Modifier.size(56.dp),
                            border = BorderStroke(2.dp, Color.White),
                            shadowElevation = 2.dp,
                            color = Color(0xFFE6F2FF)
                        ) {
                            if (medico.fotoResId != 0) {
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
                                        contentDescription = medico.nombre,
                                        tint = Color(0xFF1877F2),
                                        modifier = Modifier.size(36.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = medico.nombre,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0F1E36)
                            )

                            Text(
                                text = especialidad?.nombre ?: "",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF758A99)
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = "Calificación",
                                    tint = Color(0xFFFFB800),
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = medico.calificacion.toString(),
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF0F1E36)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "(124)",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color(0xFF758A99)
                                )
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = Color(0xFFE8F8F0)
                            ) {
                                Text(
                                    text = "Disponible hoy",
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color(0xFF27AE60),
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Navegación de mes
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        if (semana > 0) {
                            semana--
                            val nuevosDias = diasHabiles(semana)
                            fechaSeleccionada = nuevosDias[0].toString()
                            horaSeleccionada = ""
                        }
                    },
                    enabled = semana > 0
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = "Semana anterior",
                        tint = if (semana > 0) Color(0xFF0F1E36) else Color(0xFFB0BEC5)
                    )
                }

                Text(
                    text = nombreMes(dias[0]),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F1E36),
                    textAlign = TextAlign.Center
                )

                IconButton(
                    onClick = {
                        semana++
                        val nuevosDias = diasHabiles(semana)
                        fechaSeleccionada = nuevosDias[0].toString()
                        horaSeleccionada = ""
                    }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = "Semana siguiente",
                        tint = Color(0xFF0F1E36)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Fila de 5 días hábiles
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                dias.forEach { dia ->
                    ChipDia(
                        nombre = nombreDiaCorto(dia),
                        numero = dia.dayOfMonth.toString(),
                        seleccionado = dia.toString() == fechaSeleccionada,
                        onClick = {
                            fechaSeleccionada = dia.toString()
                            horaSeleccionada = ""
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Horarios disponibles",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0F1E36)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Grilla de horarios (3 columnas)
            Box(modifier = Modifier.weight(1f)) {
                if (horarios.isEmpty()) {
                    MensajeVacio("No hay horarios disponibles")
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(horarios) { hora ->
                            ChipHorario(
                                hora = hora,
                                seleccionado = hora == horaSeleccionada,
                                onClick = { horaSeleccionada = hora }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón inferior Continuar
            BotonPrincipal(
                texto = "Continuar",
                habilitado = horaSeleccionada.isNotEmpty(),
                onClick = {
                    navController.navigate(Rutas.confirmar(medicoId, fechaSeleccionada, horaSeleccionada))
                }
            )
        }
    }
}
