package com.rocha.tareatecsup.ui.screens
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppointmentScreen(
    doctorId: Int,
    navController: NavController
) {
    // Fechas y horas disponibles (mínimo 3 cada una)
    val dates = listOf("Jue 26", "Vie 27", "Sáb 28")
    val hours = listOf("9:00", "10:30", "3:00")

    var selectedDate by remember { mutableStateOf(dates[1]) }
    var selectedHour by remember { mutableStateOf(hours[1]) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Agendar cita") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(24.dp)) {
            Text("Selecciona fecha", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Row {
                dates.forEach { date ->
                    FilterChip(
                        selected = date == selectedDate,
                        onClick = { selectedDate = date },
                        label = { Text(date) },
                        modifier = Modifier.padding(end = 8.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text("Selecciona hora", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Row {
                hours.forEach { hour ->
                    FilterChip(
                        selected = hour == selectedHour,
                        onClick = { selectedHour = hour },
                        label = { Text(hour) },
                        modifier = Modifier.padding(end = 8.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { "" },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Confirmar cita")
            }
        }
    }
}