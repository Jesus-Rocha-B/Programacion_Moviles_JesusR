package com.rocha.saludplus.ui.doctores

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.rocha.saludplus.navigation.Rutas
import com.rocha.saludplus.repository.Repositorio
import com.rocha.saludplus.ui.components.BotonPrincipal

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MisDoctoresScreen(navController: NavController) {
    val especialidades = Repositorio.especialidades
    var especialidadSeleccionadaId by remember { mutableStateOf(especialidades.firstOrNull()?.id ?: 1) }

    val doctoresFiltrados = Repositorio.medicosPorEspecialidad(especialidadSeleccionadaId)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis doctores", fontWeight = FontWeight.Bold, color = Color(0xFF0F1E36)) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                tonalElevation = 8.dp
            ) {
                NavigationBarItem(
                    selected = false,
                    onClick = { navController.navigate(Rutas.HOME) { popUpTo(Rutas.HOME) { inclusive = true } } },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Inicio") },
                    label = { Text("Inicio") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF1877F2),
                        selectedTextColor = Color(0xFF1877F2),
                        unselectedIconColor = Color(0xFF758A99),
                        unselectedTextColor = Color(0xFF758A99)
                    )
                )
                NavigationBarItem(
                    selected = true,
                    onClick = { },
                    icon = { Icon(Icons.Default.LocalHospital, contentDescription = "Mis doctores") },
                    label = { Text("Doctores") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF1877F2),
                        selectedTextColor = Color(0xFF1877F2),
                        indicatorColor = Color(0xFFE6F2FF),
                        unselectedIconColor = Color(0xFF758A99),
                        unselectedTextColor = Color(0xFF758A99)
                    )
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { navController.navigate(Rutas.MIS_CITAS) },
                    icon = { Icon(Icons.Default.DateRange, contentDescription = "Citas") },
                    label = { Text("Citas") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF1877F2),
                        selectedTextColor = Color(0xFF1877F2),
                        unselectedIconColor = Color(0xFF758A99),
                        unselectedTextColor = Color(0xFF758A99)
                    )
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { navController.navigate(Rutas.RESULTADOS) },
                    icon = { Icon(Icons.Default.Check, contentDescription = "Resultados") },
                    label = { Text("Resultados") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF1877F2),
                        selectedTextColor = Color(0xFF1877F2),
                        unselectedIconColor = Color(0xFF758A99),
                        unselectedTextColor = Color(0xFF758A99)
                    )
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { navController.navigate(Rutas.PERFIL) },
                    icon = { Icon(Icons.Default.Person, contentDescription = "Perfil") },
                    label = { Text("Perfil") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF1877F2),
                        selectedTextColor = Color(0xFF1877F2),
                        unselectedIconColor = Color(0xFF758A99),
                        unselectedTextColor = Color(0xFF758A99)
                    )
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            Text(
                text = "Filtra por especialidad",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0F1E36)
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Filtros exclusivamente por especialidad (NO hay filtro "todos")
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(especialidades) { esp ->
                    val seleccionada = esp.id == especialidadSeleccionadaId
                    FilterChip(
                        selected = seleccionada,
                        onClick = { especialidadSeleccionadaId = esp.id },
                        label = { Text(esp.nombre) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Color(0xFF1877F2),
                            selectedLabelColor = Color.White,
                            containerColor = Color(0xFFF6F8FA),
                            labelColor = Color(0xFF0F1E36)
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Lista organizada de médicos para la especialidad seleccionada (2 por especialidad)
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(doctoresFiltrados) { medico ->
                    val especialidadNombre = Repositorio.obtenerEspecialidad(medico.especialidadId)?.nombre ?: ""
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(id = medico.fotoResId),
                                contentDescription = medico.nombre,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(64.dp)
                                    .clip(CircleShape)
                            )

                            Spacer(modifier = Modifier.width(16.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = medico.nombre,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF0F1E36)
                                )
                                Text(
                                    text = especialidadNombre,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color(0xFF758A99)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = null,
                                        tint = Color(0xFFFFC107),
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "${medico.calificacion} (${medico.aniosExperiencia} años exp.)",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color(0xFF758A99)
                                    )
                                }
                            }

                            BotonPrincipal(
                                texto = "Agendar",
                                onClick = { navController.navigate(Rutas.fechaHora(medico.id)) },
                                modifier = Modifier.width(100.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
//