package com.rocha.huellitasvet.ui.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.Settings
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.rocha.huellitasvet.VetApp
import com.rocha.huellitasvet.model.Rol
import com.rocha.huellitasvet.ui.dueno.FichaMascotaScreen
import com.rocha.huellitasvet.ui.dueno.FormularioMascotaScreen
import com.rocha.huellitasvet.ui.dueno.InicioScreen
import com.rocha.huellitasvet.ui.dueno.LoginScreen
import com.rocha.huellitasvet.ui.dueno.MascotasScreen
import com.rocha.huellitasvet.ui.dueno.MisCitasScreen
import com.rocha.huellitasvet.ui.dueno.PedirCitaScreen
import com.rocha.huellitasvet.ui.dueno.SplashScreen
import com.rocha.huellitasvet.ui.veterinario.AgendaScreen
import com.rocha.huellitasvet.ui.veterinario.PanelScreen
import com.rocha.huellitasvet.viewmodel.AgendaViewModel
import com.rocha.huellitasvet.viewmodel.InicioViewModel
import com.rocha.huellitasvet.viewmodel.MascotaViewModel
import com.rocha.huellitasvet.viewmodel.MisCitasViewModel
import com.rocha.huellitasvet.viewmodel.PanelViewModel
import com.rocha.huellitasvet.viewmodel.PedirCitaViewModel
import com.rocha.huellitasvet.viewmodel.SesionViewModel
import com.rocha.huellitasvet.viewmodel.VetViewModelFactory

private val rutasConBarraDueno = listOf(Rutas.INICIO, Rutas.MASCOTAS, Rutas.CITAS, Rutas.PERFIL)
private val rutasConBarraVet =
    listOf(Rutas.PANEL, Rutas.AGENDA, Rutas.PACIENTES, Rutas.SERVICIOS, Rutas.AJUSTES)

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

    fun irAPestanaDueno(ruta: String) {
        nav.navigate(ruta) {
            popUpTo(Rutas.INICIO) { saveState = true }
            launchSingleTop = true
            restoreState = true
        }
    }

    fun irAPestanaVet(ruta: String) {
        nav.navigate(ruta) {
            popUpTo(Rutas.PANEL) { saveState = true }
            launchSingleTop = true
            restoreState = true
        }
    }

    Scaffold(
        contentWindowInsets = WindowInsets(0),
        bottomBar = {
            when (rutaActual) {
                in rutasConBarraDueno -> BarraDueno(rutaActual, ::irAPestanaDueno)
                in rutasConBarraVet -> BarraVet(rutaActual, ::irAPestanaVet)
            }
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

            // ================= DUEÑO =================
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
                        onAgregar = { nav.navigate(Rutas.formulario()) },
                        onVerTodas = { irAPestanaDueno(Rutas.MASCOTAS) },
                        onPedirCita = { nav.navigate(Rutas.pedirCita()) }
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
                        onAgregar = { nav.navigate(Rutas.formulario()) }
                    )
                }
            }

            composable(Rutas.CITAS) {
                usuario?.let { u ->
                    val vm: MisCitasViewModel = viewModel(
                        key = "citas_${u.id}",
                        factory = VetViewModelFactory(app.repository, app.prefs, u.id)
                    )
                    MisCitasScreen(
                        vm = vm,
                        onPedirCita = { nav.navigate(Rutas.pedirCita()) }
                    )
                }
            }

            // TEMPORAL: se completa en el Paso 13 (aquí queda el cierre de sesión)
            composable(Rutas.PERFIL) {
                PantallaTemporal("Perfil (Paso 13)", "Cerrar sesión") {
                    sesionVm.cerrarSesion()
                    nav.navigate(Rutas.LOGIN) { popUpTo(Rutas.INICIO) { inclusive = true } }
                }
            }

            composable(
                Rutas.FICHA,
                arguments = listOf(navArgument("mascotaId") { type = NavType.IntType })
            ) { backStackEntry ->
                val mascotaId = backStackEntry.arguments?.getInt("mascotaId") ?: 0
                usuario?.let { u ->
                    val vm: MascotaViewModel = viewModel(
                        factory = VetViewModelFactory(app.repository, app.prefs, u.id)
                    )
                    FichaMascotaScreen(
                        vm = vm,
                        mascotaId = mascotaId,
                        onVolver = { nav.popBackStack() },
                        onEditar = { nav.navigate(Rutas.formulario(it)) },
                        onPedirCita = { nav.navigate(Rutas.pedirCita(mascotaId)) }
                    )
                }
            }

            composable(
                Rutas.FORMULARIO,
                arguments = listOf(
                    navArgument("mascotaId") {
                        type = NavType.IntType
                        defaultValue = -1
                    }
                )
            ) { backStackEntry ->
                val id = backStackEntry.arguments?.getInt("mascotaId") ?: -1
                usuario?.let { u ->
                    val vm: MascotaViewModel = viewModel(
                        factory = VetViewModelFactory(app.repository, app.prefs, u.id)
                    )
                    FormularioMascotaScreen(
                        vm = vm,
                        mascotaId = if (id == -1) null else id,
                        onListo = { nav.popBackStack() }
                    )
                }
            }

            composable(
                Rutas.PEDIR_CITA,
                arguments = listOf(
                    navArgument("mascotaId") {
                        type = NavType.IntType
                        defaultValue = -1
                    }
                )
            ) { backStackEntry ->
                val id = backStackEntry.arguments?.getInt("mascotaId") ?: -1
                usuario?.let { u ->
                    val vm: PedirCitaViewModel = viewModel(
                        factory = VetViewModelFactory(app.repository, app.prefs, u.id)
                    )
                    PedirCitaScreen(
                        vm = vm,
                        mascotaInicial = if (id == -1) null else id,
                        onVolver = { nav.popBackStack() },
                        onCreada = { irAPestanaDueno(Rutas.CITAS) }
                    )
                }
            }

            // ================= VETERINARIO =================
            composable(Rutas.PANEL) {
                val vm: PanelViewModel = viewModel(
                    key = "panel",
                    factory = VetViewModelFactory(app.repository, app.prefs)
                )
                PanelScreen(
                    vm = vm,
                    onVerAgenda = { irAPestanaVet(Rutas.AGENDA) }
                )
            }

            composable(Rutas.AGENDA) {
                val vm: AgendaViewModel = viewModel(
                    key = "agenda",
                    factory = VetViewModelFactory(app.repository, app.prefs)
                )
                AgendaScreen(
                    vm = vm,
                    onAtender = { nav.navigate(Rutas.atender(it)) }
                )
            }

            // TEMPORAL: se reemplaza en el Paso 12
            composable(Rutas.PACIENTES) {
                PantallaTemporal("Pacientes (Paso 12)", "Ir al panel") {
                    irAPestanaVet(Rutas.PANEL)
                }
            }

            // TEMPORAL: se reemplaza en el Paso 13
            composable(Rutas.SERVICIOS) {
                PantallaTemporal("Servicios (Paso 13)", "Ir al panel") {
                    irAPestanaVet(Rutas.PANEL)
                }
            }

            // TEMPORAL: se completa en el Paso 13 (aquí queda el cierre de sesión)
            composable(Rutas.AJUSTES) {
                PantallaTemporal("Ajustes (Paso 13)", "Cerrar sesión") {
                    sesionVm.cerrarSesion()
                    nav.navigate(Rutas.LOGIN) { popUpTo(Rutas.PANEL) { inclusive = true } }
                }
            }

            // TEMPORAL: se reemplaza en el Paso 12
            composable(
                Rutas.ATENDER,
                arguments = listOf(navArgument("citaId") { type = NavType.IntType })
            ) {
                PantallaTemporal("Atender cita (Paso 12)", "Volver") { nav.popBackStack() }
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
    BarraInferior(items, rutaActual, onPestana)
}

@Composable
private fun BarraVet(rutaActual: String?, onPestana: (String) -> Unit) {
    val items = listOf(
        Triple(Rutas.PANEL, "Panel", Icons.Filled.BarChart),
        Triple(Rutas.AGENDA, "Agenda", Icons.Filled.CalendarMonth),
        Triple(Rutas.PACIENTES, "Pacientes", Icons.Filled.Pets),
        Triple(Rutas.SERVICIOS, "Servicios", Icons.Filled.LocalOffer),
        Triple(Rutas.AJUSTES, "Ajustes", Icons.Filled.Settings)
    )
    BarraInferior(items, rutaActual, onPestana)
}

@Composable
private fun BarraInferior(
    items: List<Triple<String, String, ImageVector>>,
    rutaActual: String?,
    onPestana: (String) -> Unit
) {
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