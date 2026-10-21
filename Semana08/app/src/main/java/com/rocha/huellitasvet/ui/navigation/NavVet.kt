package com.rocha.huellitasvet.ui.navigation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.rocha.huellitasvet.model.Rol
import com.rocha.huellitasvet.ui.dueno.LoginScreen
import com.rocha.huellitasvet.ui.dueno.SplashScreen
import com.rocha.huellitasvet.viewmodel.SesionViewModel

@Composable
fun NavVet(sesionVm: SesionViewModel) {
    val nav = rememberNavController()
    fun irAlInicio(rol: Rol?, desde: String) {
        val destino = when (rol) {
            Rol.CLIENTE -> Rutas.INICIO
            Rol.ADMIN -> Rutas.PANEL
            null -> Rutas.LOGIN
        }
        nav.navigate(destino) { popUpTo(desde) { inclusive = true } }
    }
    NavHost(navController = nav, startDestination = Rutas.SPLASH) {
        composable(Rutas.SPLASH) {
            SplashScreen(sesionVm) { rol -> irAlInicio(rol, Rutas.SPLASH) }
        }
        composable(Rutas.LOGIN) {
            LoginScreen(sesionVm) { rol -> irAlInicio(rol, Rutas.LOGIN) }
        }
        // TEMPORAL: se reemplaza en el Paso 7 por InicioScreen
        composable(Rutas.INICIO) {
            PantallaTemporal("Inicio del dueño (Paso 7)") {
                sesionVm.cerrarSesion()
                nav.navigate(Rutas.LOGIN) { popUpTo(Rutas.INICIO) { inclusive = true } }
            }
        }

        // TEMPORAL: se reemplaza en el Paso 11 por PanelScreen
        composable(Rutas.PANEL) {
            PantallaTemporal("Panel del veterinario (Paso 11)") {
                sesionVm.cerrarSesion()
                nav.navigate(Rutas.LOGIN) { popUpTo(Rutas.PANEL) { inclusive = true } }
            }
        }
    }
}

@Composable
private fun PantallaTemporal(titulo: String, onCerrarSesion: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(titulo)
        Button(onClick = onCerrarSesion) { Text("Cerrar sesión") }
    }
}