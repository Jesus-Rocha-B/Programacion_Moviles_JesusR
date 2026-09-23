package com.rocha.tareatecsup.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.rocha.tareatecsup.navigation.Screen

data class DoctorDetail(
    val id: Int,
    val name: String,
    val specialty: String,
    val rating: Double,
    val experience: String,
    val description: String
)

val doctorsList = listOf(
    DoctorDetail(1, "Dra. Ana Torres", "Cardiología", 4.9, "12 años exp.", "Especialista en arritmias e hipertensión, formación en la Clínica Mayo."),
    DoctorDetail(2, "Dr. Luis Vega", "Pediatría", 4.7, "8 años exp.", "Especialista en pediatría general y desarrollo infantil. Experiencia en atención primaria."),
    DoctorDetail(3, "Dra. Rosa Díaz", "Dermatología", 4.8, "10 años exp.", "Especialista en dermatología clínica y cosmética. Experta en cuidado de la piel.")
)

fun getDoctorById(id: Int): DoctorDetail {
    return doctorsList.find { it.id == id } ?: doctorsList.first()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoctorProfileScreen(
    doctorId: Int,
    navController: NavController
) {
    val doctor = getDoctorById(doctorId)
    val darkPurple = Color(0xFF4A148C)
    val lightPurpleBg = Color(0xFFE1BEE7)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Perfil del médico",
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
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Ícono circular grande centrado con la misma cruz médica que la lista de la Home
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .background(lightPurpleBg, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.LocalHospital,
                    contentDescription = "Cruz médica",
                    tint = darkPurple,
                    modifier = Modifier.size(48.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Nombre del médico en negrita
            Text(
                text = doctor.name,
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF212121)
                ),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Especialidad y experiencia
            Text(
                text = "${doctor.specialty} · ${doctor.experience}",
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = Color.Gray
                ),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Calificación
            Text(
                text = "⭐ ${doctor.rating} (128 reseñas)",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Medium,
                    color = Color.Gray
                ),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(28.dp))

            // Párrafo de descripción del médico
            Text(
                text = doctor.description,
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = Color(0xFF424242)
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            // Botón Agendar Cita ancho completo
            Button(
                onClick = {
                    navController.navigate(Screen.Appointment.createRoute(doctor.id))
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
                    text = "Agendar cita",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}
