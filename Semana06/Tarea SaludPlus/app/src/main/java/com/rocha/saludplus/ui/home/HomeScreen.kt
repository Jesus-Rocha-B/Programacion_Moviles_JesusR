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
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.rocha.saludplus.navigation.Rutas
import com.rocha.saludplus.repository.Repositorio
import com.rocha.saludplus.ui.components.TarjetaAccion
import com.rocha.saludplus.ui.components.TarjetaDestacada

@Composable
fun HomeScreen(navController: NavController) {
    // Solo el primer nombre del usuario con la sesión iniciada
    val nombre = Repositorio.usuarioActual?.nombre?.substringBefore(" ") ?: "Paciente"
    // Las 4 primeras especialidades para el LazyRow
    val destacadas = Repositorio.especialidadesDestacadas()

    Scaffold(
        // Menú principal inferior con 4 destinos
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = true,
                    onClick = { },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Inicio") },
                    label = { Text("Inicio") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { navController.navigate(Rutas.MIS_CITAS) },
                    icon = { Icon(Icons.Default.DateRange, contentDescription = "Citas") },
                    label = { Text("Citas") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { navController.navigate(Rutas.RESULTADOS) },
                    icon = { Icon(Icons.Default.Check, contentDescription = "Resultados") },
                    label = { Text("Resultados") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { navController.navigate(Rutas.PERFIL) },
                    icon = { Icon(Icons.Default.Person, contentDescription = "Perfil") },
                    label = { Text("Perfil") }
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(24.dp)
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
                        style = MaterialTheme.typography.headlineMedium
                    )
                    Text(
                        text = "¿Qué deseas hacer hoy?",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                IconButton(onClick = { navController.navigate(Rutas.NOTIFICACIONES) }) {
                    Icon(Icons.Default.Notifications, contentDescription = "Notificaciones")
                }
            }
            Spacer(modifier = Modifier.height(24.dp))

            // Accesos rápidos temáticos (2 por fila) con colores pastel e íconos
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                TarjetaAccion(
                    titulo = "Agendar cita",
                    onClick = { navController.navigate(Rutas.ESPECIALIDADES) },
                    icono = Icons.Default.DateRange,
                    backgroundColor = Color(0xFFE3F2FD),
                    iconColor = Color(0xFF1976D2),
                    modifier = Modifier.weight(1f)
                )
                TarjetaAccion(
                    titulo = "Mis citas",
                    onClick = { navController.navigate(Rutas.MIS_CITAS) },
                    icono = Icons.Default.Schedule,
                    backgroundColor = Color(0xFFE8F5E9),
                    iconColor = Color(0xFF388E3C),
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
                    backgroundColor = Color(0xFFF3E5F5),
                    iconColor = Color(0xFF7B1FA2),
                    modifier = Modifier.weight(1f)
                )
                TarjetaAccion(
                    titulo = "Resultados",
                    onClick = { navController.navigate(Rutas.RESULTADOS) },
                    icono = Icons.AutoMirrored.Filled.Assignment,
                    backgroundColor = Color(0xFFFFF3E0),
                    iconColor = Color(0xFFF57C00),
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
                    style = MaterialTheme.typography.titleMedium
                )
                TextButton(onClick = { navController.navigate(Rutas.ESPECIALIDADES) }) {
                    Text("Ver todas")
                }
            }
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
