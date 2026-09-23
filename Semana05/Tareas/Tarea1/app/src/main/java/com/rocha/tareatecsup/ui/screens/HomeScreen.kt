package com.rocha.tareatecsup.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.rocha.tareatecsup.navigation.Screen

data class Doctor(
    val id: Int,
    val name: String,
    val specialty: String,
    val rating: Double
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    onMenuClick: () -> Unit = {}
) {
    val specialties = listOf("Todas", "Cardiología", "Pediatría", "Dermatología")
    var selectedSpecialty by remember { mutableStateOf("Todas") }

    val doctors = remember {
        listOf(
            Doctor(1, "Dra. Ana Torres", "Cardiología", 4.9),
            Doctor(2, "Dr. Luis Vega", "Pediatría", 4.7),
            Doctor(3, "Dra. Rosa Díaz", "Dermatología", 4.8)
        )
    }

    val filteredDoctors = remember(selectedSpecialty) {
        if (selectedSpecialty == "Todas") {
            doctors
        } else {
            doctors.filter { it.specialty.equals(selectedSpecialty, ignoreCase = true) }
        }
    }

    val darkPurple = Color(0xFF4A148C)
    val lightPurple = Color(0xFFE1BEE7)
    val selectedPurple = Color(0xFF6A1B9A)
    val unselectedPurpleBg = Color(0xFFF3E5F5)

    Scaffold(
        containerColor = Color(0xFFF8F9FA)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Encabezado superior morado oscuro con esquinas inferiores redondeadas
            Surface(
                color = darkPurple,
                shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 24.dp, bottom = 24.dp)
                ) {
                    Text(
                        text = "Clínica Salud+",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Hola, Juan",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            color = Color(0xFFE1BEE7)
                        )
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                Spacer(modifier = Modifier.height(24.dp))

                // Sección Especialidades
                Text(
                    text = "Especialidades",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF212121)
                    )
                )
                Spacer(modifier = Modifier.height(12.dp))

                // Chips de especialidad en forma de píldora
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(specialties) { specialty ->
                        val isSelected = specialty == selectedSpecialty
                        FilterChip(
                            selected = isSelected,
                            onClick = { selectedSpecialty = specialty },
                            label = {
                                Text(
                                    text = specialty,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            shape = RoundedCornerShape(50),
                            colors = FilterChipDefaults.filterChipColors(
                                containerColor = unselectedPurpleBg,
                                labelColor = darkPurple,
                                selectedContainerColor = selectedPurple,
                                selectedLabelColor = Color.White
                            ),
                            border = null
                        )
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))

                // Sección Médicos disponibles
                Text(
                    text = "Médicos disponibles",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF212121)
                    )
                )
                Spacer(modifier = Modifier.height(12.dp))

                // Tarjetas individuales de médicos filtrados
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 24.dp)
                ) {
                    items(filteredDoctors) { doctor ->
                        Card(
                            onClick = {
                                navController.navigate(Screen.DoctorProfile.createRoute(doctor.id))
                            },
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color.White
                            ),
                            elevation = CardDefaults.cardElevation(
                                defaultElevation = 2.dp
                            ),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Ícono circular morado claro con cruz médica
                                Box(
                                    modifier = Modifier
                                        .size(48.dp)
                                        .background(lightPurple, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.LocalHospital,
                                        contentDescription = "Cruz médica",
                                        tint = darkPurple,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.width(16.dp))

                                // Nombre y especialidad
                                Column(
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(
                                        text = doctor.name,
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF212121)
                                        )
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = doctor.specialty,
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            color = Color.Gray
                                        )
                                    )
                                }

                                // Calificación a la derecha
                                Text(
                                    text = "⭐ ${doctor.rating}",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = selectedPurple
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
