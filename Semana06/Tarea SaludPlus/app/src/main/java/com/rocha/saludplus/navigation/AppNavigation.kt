package com.rocha.saludplus.navigation
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.rocha.saludplus.ui.auth.*
import com.rocha.saludplus.ui.home.*
import com.rocha.saludplus.ui.agendamiento.*
// IMPORTS NUEVOS

@Composable
fun AppNavigation() {
    // Variable que controla el historial de pantallas
    val navController = rememberNavController()
    // Mapa de rutas
    NavHost(navController = navController, startDestination = Rutas.SPLASH) {
        composable(Rutas.SPLASH) { SplashScreen(navController) }
        composable(Rutas.REGISTRO) { RegistroScreen(navController) }
        composable(Rutas.LOGIN) { LoginScreen(navController) }
        composable(Rutas.TERMINOS) { TerminosScreen(navController) }
        composable(Rutas.HOME) { HomeScreen(navController) }
        composable(Rutas.ESPECIALIDADES) { EspecialidadesScreen(navController) }
        composable(
            route = Rutas.MEDICOS,
            arguments = listOf(navArgument("especialidadId") { type = NavType.IntType })
        ) { backStackEntry ->
            val especialidadId = backStackEntry.arguments?.getInt("especialidadId") ?: 0
            MedicosScreen(especialidadId, navController)
        }
        // RUTAS NUEVAS
    }
}