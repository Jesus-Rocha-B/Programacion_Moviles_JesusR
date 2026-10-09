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
import com.rocha.saludplus.ui.citas.*
import com.rocha.saludplus.ui.perfil.*
import com.rocha.saludplus.ui.resultados.*
import com.rocha.saludplus.ui.notificaciones.*
import com.rocha.saludplus.ui.doctores.*

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
        composable(Rutas.MIS_DOCTORES) { MisDoctoresScreen(navController) }
        composable(Rutas.LOCALES) { LocalesScreen(navController) }
        composable(Rutas.ESPECIALIDADES) { EspecialidadesScreen(navController) }
        composable(
            route = Rutas.ESPECIALIDADES_SEDE,
            arguments = listOf(navArgument("sede") { type = NavType.StringType })
        ) { backStackEntry ->
            val sede = backStackEntry.arguments?.getString("sede") ?: ""
            EspecialidadesScreen(navController, sede)
        }
        composable(
            route = Rutas.MEDICOS,
            arguments = listOf(navArgument("especialidadId") { type = NavType.IntType })
        ) { backStackEntry ->
            val especialidadId = backStackEntry.arguments?.getInt("especialidadId") ?: 0
            MedicosScreen(navController, "", especialidadId)
        }
        composable(
            route = Rutas.MEDICOS_SEDE,
            arguments = listOf(
                navArgument("sede") { type = NavType.StringType },
                navArgument("especialidadId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val sede = backStackEntry.arguments?.getString("sede") ?: ""
            val especialidadId = backStackEntry.arguments?.getInt("especialidadId") ?: 0
            MedicosScreen(navController, sede, especialidadId)
        }
        composable(
            route = Rutas.FECHA_HORA,
            arguments = listOf(navArgument("medicoId") { type = NavType.IntType })
        ) { backStackEntry ->
            val medicoId = backStackEntry.arguments?.getInt("medicoId") ?: 0
            FechaHoraScreen(medicoId, navController)
        }
        composable(
            route = Rutas.CONFIRMAR,
            arguments = listOf(
                navArgument("medicoId") { type = NavType.IntType },
                navArgument("fecha") { type = NavType.StringType },
                navArgument("hora") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val medicoId = backStackEntry.arguments?.getInt("medicoId") ?: 0
            val fecha = backStackEntry.arguments?.getString("fecha") ?: ""
            val hora = backStackEntry.arguments?.getString("hora") ?: ""
            ConfirmarCitaScreen(medicoId, fecha, hora, navController)
        }
        composable(
            route = Rutas.CITA_EXITOSA,
            arguments = listOf(navArgument("citaId") { type = NavType.IntType })
        ) { backStackEntry ->
            val citaId = backStackEntry.arguments?.getInt("citaId") ?: 0
            CitaExitosaScreen(citaId, navController)
        }
        composable(Rutas.MIS_CITAS) { MisCitasScreen(navController) }
        composable(Rutas.PERFIL) { PerfilScreen(navController) }
        composable(Rutas.RESULTADOS) { ResultadosScreen(navController) }
        composable(Rutas.NOTIFICACIONES) { NotificacionesScreen(navController) }
        composable(
            route = Rutas.DETALLE_CITA,
            arguments = listOf(navArgument("citaId") { type = NavType.IntType })
        ) { backStackEntry ->
            val citaId = backStackEntry.arguments?.getInt("citaId") ?: 0
            DetalleCitaScreen(citaId, navController)
        }
    }
}
