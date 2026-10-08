package com.rocha.saludplus.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.rocha.saludplus.navigation.Rutas
import com.rocha.saludplus.repository.Repositorio
import com.rocha.saludplus.ui.components.TarjetaAccion
import com.rocha.saludplus.ui.components.TarjetaDestacada

@Composable
fun HomeScreen(navController: NavController) {
    val nombre = Repositorio.usuarioActual?.nombre?.substringBefore(" ") ?: "Paciente"
    val destacadas = Repositorio.especialidadesDestacadas()

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                tonalElevation = 8.dp
            ) {
                NavigationBarItem(
                    selected = true,
                    onClick = { },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Inicio") },
                    label = { Text("Inicio") },
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
                .padding(20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Saludo y campana de notificaciones
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "¡Hola, $nombre!",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F1E36)
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "¿Qué deseas hacer hoy?",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF758A99)
                    )
                }
                IconButton(onClick = { navController.navigate(Rutas.NOTIFICACIONES) }) {
                    Icon(
                        imageVector = Icons.Default.NotificationsNone,
                        contentDescription = "Notificaciones",
                        tint = Color(0xFF0F1E36)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Cuadrícula 2x2 de accesos rápidos con colores pastel idénticos a la maqueta
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                TarjetaAccion(
                    titulo = "Agendar cita",
                    onClick = { navController.navigate(Rutas.ESPECIALIDADES) },
                    icono = Icons.Default.DateRange,
                    backgroundColor = Color(0xFFE6F2FF),
                    iconColor = Color(0xFF1877F2),
                    modifier = Modifier.weight(1f)
                )
                TarjetaAccion(
                    titulo = "Mis citas",
                    onClick = { navController.navigate(Rutas.MIS_CITAS) },
                    icono = Icons.Default.Schedule,
                    backgroundColor = Color(0xFFE8F8F0),
                    iconColor = Color(0xFF27AE60),
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                TarjetaAccion(
                    titulo = "Mis datos",
                    onClick = { navController.navigate(Rutas.PERFIL) },
                    icono = Icons.Default.Person,
                    backgroundColor = Color(0xFFF3E8FF),
                    iconColor = Color(0xFF8E44AD),
                    modifier = Modifier.weight(1f)
                )
                TarjetaAccion(
                    titulo = "Resultados",
                    onClick = { navController.navigate(Rutas.RESULTADOS) },
                    icono = Icons.AutoMirrored.Filled.Assignment,
                    backgroundColor = Color(0xFFFFF3E6),
                    iconColor = Color(0xFFE67E22),
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Especialidades destacadas
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Especialidades destacadas",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F1E36)
                )
                TextButton(onClick = { navController.navigate(Rutas.ESPECIALIDADES) }) {
                    Text(
                        text = "Ver todas",
                        color = Color(0xFF1877F2),
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                items(destacadas) { especialidad ->
                    TarjetaDestacada(
                        texto = especialidad.nombre,
                        onClick = { navController.navigate(Rutas.medicos(especialidad.id)) }
                    )
                }
            }
        }
    }
}
