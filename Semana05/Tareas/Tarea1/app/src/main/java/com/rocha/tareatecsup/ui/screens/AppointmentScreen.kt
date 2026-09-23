package com.rocha.tareatecsup.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.rocha.tareatecsup.navigation.Screen

data class DateItem(val dayName: String, val fullDayName: String, val dayNumber: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppointmentScreen(
    doctorId: Int,
    navController: NavController
) {
    val doctor = getDoctorById(doctorId)

    val dates = listOf(
        DateItem("Jue", "Jueves", "26"),
        DateItem("Vie", "Viernes", "27"),
        DateItem("Sáb", "Sábado", "28")
    )
    val hours = listOf("9:00", "10:30", "3:00")

    var selectedDate by remember { mutableStateOf(dates[1]) }
    var selectedHour by remember { mutableStateOf(hours[1]) }

    val darkPurple = Color(0xFF4A148C)
    val unselectedCardBg = Color(0xFFF3F3F5)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Agendar cita",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        },
        containerColor = Color.White
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            // Sección Selecciona fecha
            Text(
                text = "Selecciona fecha",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color.Gray,
                    fontWeight = FontWeight.SemiBold
                )
            )
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                dates.forEach { dateItem ->
                    val isSelected = dateItem == selectedDate
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = if (isSelected) darkPurple else unselectedCardBg,
                        modifier = Modifier
                            .weight(1f)
                            .clickable { selectedDate = dateItem }
                    ) {
                        Column(
                            modifier = Modifier.padding(vertical = 14.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = dateItem.dayName,
                                fontSize = 13.sp,
                                color = if (isSelected) Color.White.copy(alpha = 0.8f) else Color.Gray
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = dateItem.dayNumber,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) Color.White else Color(0xFF212121)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Sección Selecciona hora
            Text(
                text = "Selecciona hora",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color.Gray,
                    fontWeight = FontWeight.SemiBold
                )
            )
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                hours.forEach { hour ->
                    val isSelected = hour == selectedHour
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = if (isSelected) darkPurple else unselectedCardBg,
                        modifier = Modifier
                            .weight(1f)
                            .clickable { selectedHour = hour }
                    ) {
                        Box(
                            modifier = Modifier.padding(vertical = 16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = hour,
                                fontSize = 15.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) Color.White else Color(0xFF212121)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Botón Confirmar cita
            Button(
                onClick = {
                    val formattedDate = "${selectedDate.fullDayName} ${selectedDate.dayNumber}"
                    val amPm = if (selectedHour == "3:00") "pm" else "am"
                    val dateTimeString = "$formattedDate, $selectedHour $amPm"

                    // Registra automáticamente la cita en estado "Pendiente" por defecto
                    AppointmentsRepository.addAppointment(doctor.name, dateTimeString)

                    navController.navigate(
                        Screen.Confirmation.createRoute(doctor.name, formattedDate, "$selectedHour $amPm")
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = darkPurple,
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = "Confirmar cita",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
