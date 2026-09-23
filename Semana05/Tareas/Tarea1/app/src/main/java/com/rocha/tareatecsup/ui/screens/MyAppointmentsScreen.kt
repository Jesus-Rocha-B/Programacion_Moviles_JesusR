package com.rocha.tareatecsup.ui.screens
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

data class AppointmentItem(val doctorName: String, val dateTime: String, val status: String)

@Composable
fun MyAppointmentsScreen() {
    val appointments = listOf(
        AppointmentItem("Dra. Ana Torres", "Viernes 27, 10:30 am", "Confirmada"),
        AppointmentItem("Dr. Luis Vega", "Miércoles 15, 3:00 pm", "Completada")
    )

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Mis citas", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(appointments.size) { index ->
                val item = appointments[index]
                Card(modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(item.doctorName, style = MaterialTheme.typography.titleMedium)
                        Text(item.dateTime, style = MaterialTheme.typography.bodyMedium)
                        Spacer(modifier = Modifier.height(4.dp))
                        AssistChip(onClick = { }, label = { Text(item.status) })
                    }
                }
            }
        }
    }
}