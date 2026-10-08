package com.rocha.saludplus.ui.perfil

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.rocha.saludplus.navigation.Rutas
import com.rocha.saludplus.repository.Repositorio
import com.rocha.saludplus.ui.components.*

@Composable
fun PerfilScreen(navController: NavController) {
    // Datos de la sesión actual
    val usuario = Repositorio.usuarioActual
    val totalCitas = Repositorio.citasDelUsuario().size

    Scaffold(
        topBar = { BarraSuperior("Mi perfil", onVolver = { navController.popBackStack() }) }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(24.dp)) {
            Text(
                text = usuario?.nombre ?: "Sin sesión",
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(16.dp))
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    FilaDato("Teléfono", usuario?.telefono ?: "")
                    FilaDato("Correo", usuario?.correo ?: "")
                    FilaDato("Citas agendadas", "$totalCitas")
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            BotonPrincipal(
                texto = "Cerrar sesión",
                onClick = {
                    Repositorio.cerrarSesion()
                    // Vuelve al Splash y borra el historial
                    navController.navigate(Rutas.SPLASH) {
                        popUpTo(Rutas.HOME) { inclusive = true }
                    }
                }
            )
        }
    }
}