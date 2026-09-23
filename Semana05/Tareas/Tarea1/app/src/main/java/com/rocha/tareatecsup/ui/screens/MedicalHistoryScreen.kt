package com.rocha.tareatecsup.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class MedicalRecord(
    val date: String,
    val title: String,
    val doctorInfo: String,
    val summary: String,
    val status: String
)

@Composable
fun MedicalHistoryScreen() {
    val historyRecords = listOf<MedicalRecord>(
        MedicalRecord(
            date = "Miércoles 15 de Septiembre, 2026",
            title = "Consulta de Pediatría",
            doctorInfo = "Dr. Luis Vega · Pediatría",
            summary = "Evaluación de desarrollo infantil y chequeo de salud general. Paciente en perfecto estado de crecimiento.",
            status = "Completada"
        ),
        MedicalRecord(
            date = "12 de Agosto, 2026",
            title = "Chequeo Preventivo General",
            doctorInfo = "Dra. Ana Torres · Medicina General",
            summary = "Paciente en excelente estado general. Presión arterial y ritmo cardíaco dentro de los rangos normales.",
            status = "Completada"
        )
    )

    val darkPurple = Color(0xFF4A148C)
    val cardBg = Color(0xFFF3F3F5)

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            // Título principal
            Text(
                text = "Historial médico",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF212121)
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Lista de tarjetas del historial médico (consultas completadas)
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(historyRecords) { record ->
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = cardBg,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp)
                        ) {
                            // Fecha del registro
                            Text(
                                text = record.date,
                                fontSize = 12.sp,
                                color = Color.Gray,
                                fontWeight = FontWeight.Medium
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            // Título de la consulta / diagnóstico
                            Text(
                                text = record.title,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF212121)
                                )
                            )

                            Spacer(modifier = Modifier.height(2.dp))

                            // Información del médico y especialidad
                            Text(
                                text = record.doctorInfo,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = darkPurple,
                                    fontWeight = FontWeight.SemiBold
                                )
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            // Resumen o notas clínicas
                            Text(
                                text = record.summary,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = Color(0xFF424242)
                                )
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            // Chip de estado
                            Surface(
                                shape = RoundedCornerShape(50),
                                color = Color(0xFFE8F5E9)
                            ) {
                                Box(
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = record.status,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF00897B)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
