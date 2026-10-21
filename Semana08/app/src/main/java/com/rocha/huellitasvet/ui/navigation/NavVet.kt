package com.rocha.huellitasvet.ui.navigation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.rocha.huellitasvet.VetApp
import com.rocha.huellitasvet.model.Rol
import com.rocha.huellitasvet.ui.dueno.InicioScreen
import com.rocha.huellitasvet.ui.dueno.LoginScreen
import com.rocha.huellitasvet.ui.dueno.MascotasScreen
import com.rocha.huellitasvet.ui.dueno.SplashScreen
import com.rocha.huellitasvet.viewmodel.InicioViewModel
import com.rocha.huellitasvet.viewmodel.SesionViewModel
import com.rocha.huellitasvet.viewmodel.VetViewModelFactory

private val rutasConBarra = listOf(Rutas.INICIO, Rutas.MASCOTAS, Rutas.CITAS, Rutas.PERFIL)

@Composable
fun NavVet(sesionVm: SesionViewModel) {
    val nav = rememberNavController()
    val app = LocalContext.current.applicationContext as VetApp
    val usuario by sesionVm.usuarioActual.collectAsState()
    val entrada by nav.currentBackStackEntryAsState()
    val rutaActual = entrada?.destination?.route

    fun irAlInicio(rol: Rol?, desde: String) {
        val destino = when (rol) {
            Rol.CLIENTE -> Rutas.INICIO
            Rol.ADMIN -> Rutas.PANEL
            null -> Rutas.LOGIN
        }
        nav.navigate(destino) { popUpTo(desde) { inclusive = true } }
    }

    fun irAPestana(ruta: String) {
        nav.navigate(ruta) {
            popUpTo(Rutas.INICIO) { saveState = true }
            launchSingleTop = true
            restoreState = true
        }
    }

    Scaffold(
        contentWindowInsets = WindowInsets(0),
        bottomBar = {
            if (rutaActual in rutasConBarra) BarraDueno(rutaActual, ::irAPestana)
        }
    ) { padding ->
        NavHost(
            navController = nav,
            startDestination = Rutas.SPLASH,
            modifier = Modifier.padding(padding)
        ) {

            composable(Rutas.SPLASH) {
                SplashScreen(sesionVm) { rol -> irAlInicio(rol, Rutas.SPLASH) }
            }

            composable(Rutas.LOGIN) {
                LoginScreen(sesionVm) { rol -> irAlInicio(rol, Rutas.LOGIN) }
            }

            // DUEÑO
            composable(Rutas.INICIO) {
                usuario?.let { u ->
                    val vm: InicioViewModel = viewModel(
                        key = "inicio_${u.id}",
                        factory = VetViewModelFactory(app.repository, app.prefs, u.id)
                    )
                    InicioScreen(
                        vm = vm,
                        nombre = u.nombre.substringBefore(" "),
                        onMascota = { nav.navigate(Rutas.ficha(it)) },
                        onAgregar = { nav.navigate(Rutas.FORMULARIO) },
                        onVerTodas = { irAPestana(Rutas.MASCOTAS) },
                        onPedirCita = { nav.navigate(Rutas.PEDIR_CITA) }
                    )
                }
            }

            composable(Rutas.MASCOTAS) {
                usuario?.let { u ->
                    val vm: InicioViewModel = viewModel(
                        key = "mascotas_${u.id}",
                        factory = VetViewModelFactory(app.repository, app.prefs, u.id)
                    )
                    MascotasScreen(
                        vm = vm,
                        onMascota = { nav.navigate(Rutas.ficha(it)) },
                        onAgregar = { nav.navigate(Rutas.FORMULARIO) }
                    )
                }
            }

            // TEMPORAL: se reemplaza en el Paso 10
            composable(Rutas.CITAS) {
                PantallaTemporal("Mis citas (Paso 10)", "Pedir una cita") {
                    nav.navigate(Rutas.PEDIR_CITA)
                }
            }

            // TEMPORAL: se completa en el Paso 13 (aquí queda el cierre de sesión)
            composable(Rutas.PERFIL) {
                PantallaTemporal("Perfil (Paso 13)", "Cerrar sesión") {
                    sesionVm.cerrarSesion()
                    nav.navigate(Rutas.LOGIN) { popUpTo(Rutas.INICIO) { inclusive = true } }
                }
            }

            // TEMPORAL: se reemplaza en el Paso 8
            composable(
                Rutas.FICHA,
                arguments = listOf(navArgument("mascotaId") { type = NavType.IntType })
            ) {
                PantallaTemporal("Ficha de la mascota (Paso 8)", "Volver") { nav.popBackStack() }
            }

            // TEMPORAL: se reemplaza en el Paso 8
            composable(Rutas.FORMULARIO) {
                PantallaTemporal("Formulario de mascota (Paso 8)", "Volver") { nav.popBackStack() }
            }

            // TEMPORAL: se reemplaza en el Paso 9
            composable(Rutas.PEDIR_CITA) {
                PantallaTemporal("Pedir cita (Paso 9)", "Volver") { nav.popBackStack() }
            }

            // ---------- VETERINARIO ----------
            // TEMPORAL: se reemplaza en el Paso 11 por PanelScreen
            composable(Rutas.PANEL) {
                PantallaTemporal("Panel del veterinario (Paso 11)", "Cerrar sesión") {
                    sesionVm.cerrarSesion()
                    nav.navigate(Rutas.LOGIN) { popUpTo(Rutas.PANEL) { inclusive = true } }
                }
            }
        }
    }
}
@Composable
private fun BarraDueno(rutaActual: String?, onPestana: (String) -> Unit) {
    val items = listOf(
        Triple(Rutas.INICIO, "Inicio", Icons.Filled.Home),
        Triple(Rutas.MASCOTAS, "Mascotas", Icons.Filled.Pets),
        Triple(Rutas.CITAS, "Citas", Icons.Filled.CalendarMonth),
        Triple(Rutas.PERFIL, "Perfil", Icons.Filled.Person)
    )
    NavigationBar {
        items.forEach { (ruta, etiqueta, icono) ->
            NavigationBarItem(
                selected = rutaActual == ruta,
                onClick = { onPestana(ruta) },
                icon = { Icon(icono, contentDescription = etiqueta) },
                label = { Text(etiqueta) }
            )
        }
    }
}
@Composable
private fun PantallaTemporal(titulo: String, textoBoton: String, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(titulo)
        Button(onClick = onClick) { Text(textoBoton) }
    }
}