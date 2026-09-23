package com.rocha.navlab.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.rocha.navlab.navigation.Screen

// Modelo de datos local para los alumnos
data class Student(
    val id: Int,
    val name: String,
    val career: String,
    val imageUrl: String,
    val code: String,
    val email: String,
    val faculty: String,
    val biography: String
)

// Lista de datos estática usando URLs de fotos reales
val sampleStudents = listOf(
    Student(
        id = 1,
        name = "Juan León",
        career = "Ingeniería de Sistemas",
        imageUrl = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=400&auto=format&fit=crop&q=80",
        code = "2024-001",
        email = "juan.leon@example.com",
        faculty = "Ingeniería y Tecnología",
        biography = "Estudiante destacado con interés en desarrollo Android."
    ),
    Student(
        id = 2,
        name = "Maria Garcia",
        career = "Arquitectura",
        imageUrl = "https://images.unsplash.com/photo-1517841905240-472988babdf9?w=400&auto=format&fit=crop&q=80",
        code = "2024-002",
        email = "maria.garcia@example.com",
        faculty = "Diseño y Arquitectura",
        biography = "Apasionada por el diseño sostenible y planificación urbana."
    ),
    Student(
        id = 3,
        name = "Carlos Perez",
        career = "Medicina",
        imageUrl = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=400&auto=format&fit=crop&q=80",
        code = "2024-003",
        email = "carlos.perez@example.com",
        faculty = "Ciencias de la Salud",
        biography = "Enfocado en medicina preventiva y salud pública."
    ),
    Student(
        id = 4,
        name = "Ana Lopez",
        career = "Derecho",
        imageUrl = "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=400&auto=format&fit=crop&q=80",
        code = "2024-004",
        email = "ana.lopez@example.com",
        faculty = "Ciencias Jurídicas",
        biography = "Interesada en derecho corporativo y debates académicos."
    ),
    Student(
        id = 5,
        name = "Luis Ramirez",
        career = "Administración",
        imageUrl = "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=400&auto=format&fit=crop&q=80",
        code = "2024-005",
        email = "luis.ramirez@example.com",
        faculty = "Ciencias Empresariales",
        biography = "Especializado en gestión de proyectos y emprendimiento digital."
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(navController: NavController) {
    val purpleDark = Color(0xFF6A1B9A)
    val lightLilacBar = Color(0xFFE1D5F0)
    val cardBackground = Color(0xFFEDE7F6)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Directorio de Alumnos",
                        fontWeight = FontWeight.Bold,
                        color = purpleDark
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = purpleDark
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = lightLilacBar
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(padding)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(sampleStudents) { student ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            navController.navigate(Screen.Detail.createRoute(student.id))
                        },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = cardBackground),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Foto circular del alumno cargada por URL
                        AsyncImage(
                            model = student.imageUrl,
                            contentDescription = student.name,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                        )

                        Spacer(modifier = Modifier.width(16.dp))

                        // Nombre y Carrera
                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = student.name,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = student.career,
                                style = MaterialTheme.typography.bodySmall,
                                color = purpleDark,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        // Ícono de flecha hacia la derecha
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = "Ver detalle",
                            tint = Color.Gray
                        )
                    }
                }
            }
        }
    }
}
