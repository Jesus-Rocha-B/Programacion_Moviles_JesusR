package com.rocha.huellitasvet.ui.navigation

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.MedicalInformation
import androidx.compose.material.icons.outlined.MedicalServices
import androidx.compose.material.icons.outlined.Pets
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material.icons.rounded.GridView
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.MedicalInformation
import androidx.compose.material.icons.rounded.MedicalServices
import androidx.compose.material.icons.rounded.Pets
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.rocha.huellitasvet.VetApp
import com.rocha.huellitasvet.model.EstadoCita
import com.rocha.huellitasvet.model.Rol
import com.rocha.huellitasvet.ui.dueno.FichaMascotaScreen
import com.rocha.huellitasvet.ui.dueno.FormularioMascotaScreen
import com.rocha.huellitasvet.ui.dueno.InicioScreen
import com.rocha.huellitasvet.ui.dueno.LoginScreen
import com.rocha.huellitasvet.ui.dueno.MascotasScreen
import com.rocha.huellitasvet.ui.dueno.MisCitasScreen
import com.rocha.huellitasvet.ui.dueno.NotificacionesScreen
import com.rocha.huellitasvet.ui.dueno.PedirCitaScreen
import com.rocha.huellitasvet.ui.dueno.RegistroScreen
import com.rocha.huellitasvet.ui.dueno.PerfilScreen
import com.rocha.huellitasvet.ui.dueno.SplashScreen
import com.rocha.huellitasvet.ui.veterinario.AgendaScreen
import com.rocha.huellitasvet.ui.veterinario.AjustesScreen
import com.rocha.huellitasvet.ui.veterinario.AtenderCitaScreen
import com.rocha.huellitasvet.ui.veterinario.FormularioServicioScreen
import com.rocha.huellitasvet.ui.veterinario.PacientesScreen
import com.rocha.huellitasvet.ui.veterinario.PanelScreen
import com.rocha.huellitasvet.ui.veterinario.ServiciosScreen
import com.rocha.huellitasvet.viewmodel.AgendaViewModel
import com.rocha.huellitasvet.viewmodel.AtenderCitaViewModel
import com.rocha.huellitasvet.viewmodel.InicioViewModel
import com.rocha.huellitasvet.viewmodel.MascotaViewModel
import com.rocha.huellitasvet.viewmodel.MisCitasViewModel
import com.rocha.huellitasvet.viewmodel.PacientesViewModel
import com.rocha.huellitasvet.viewmodel.PanelViewModel
import com.rocha.huellitasvet.viewmodel.PedirCitaViewModel
import com.rocha.huellitasvet.viewmodel.ServiciosViewModel
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

    // Redirección global inmediata al cerrar sesión
    androidx.compose.runtime.LaunchedEffect(usuario) {
        if (usuario == null && rutaActual != null && rutaActual != Rutas.SPLASH && rutaActual != Rutas.LOGIN && rutaActual != Rutas.REGISTRO) {
            nav.navigate(Rutas.LOGIN) {
                popUpTo(0) { inclusive = true }
            }
        }
    }

    fun irAlInicio(rol: Rol?, desde: String) {
        val destino = when (rol) {
            Rol.CLIENTE -> Rutas.INICIO
            Rol.ADMIN -> Rutas.PANEL
            null -> Rutas.LOGIN
        }
        nav.navigate(destino) { popUpTo(desde) { inclusive = true } }
    }

    /** Navega a un destino y limpia toda la pila de navegación. */
    fun irLimpiandoPila(destino: String) {
        nav.navigate(destino) {
            popUpTo(Rutas.SPLASH) { inclusive = true }
            launchSingleTop = true
        }
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
            modifier = Modifier.padding(padding),
            enterTransition = { androidx.compose.animation.fadeIn(animationSpec = androidx.compose.animation.core.tween(300)) + androidx.compose.animation.slideInHorizontally { it / 4 } },
            exitTransition = { androidx.compose.animation.fadeOut(animationSpec = androidx.compose.animation.core.tween(300)) + androidx.compose.animation.slideOutHorizontally { -it / 4 } },
            popEnterTransition = { androidx.compose.animation.fadeIn(animationSpec = androidx.compose.animation.core.tween(300)) + androidx.compose.animation.slideInHorizontally { -it / 4 } },
            popExitTransition = { androidx.compose.animation.fadeOut(animationSpec = androidx.compose.animation.core.tween(300)) + androidx.compose.animation.slideOutHorizontally { it / 4 } }
        ) {

            composable(Rutas.SPLASH) {
                SplashScreen(sesionVm) { rol -> irAlInicio(rol, Rutas.SPLASH) }
            }

            composable(Rutas.LOGIN) {
                LoginScreen(
                    vm = sesionVm,
                    onRegistrarse = { nav.navigate(Rutas.REGISTRO) },
                    onLoginExitoso = { rol -> irAlInicio(rol, Rutas.LOGIN) }
                )
            }

            composable(Rutas.REGISTRO) {
                RegistroScreen(
                    vm = sesionVm,
                    onVolver = { nav.popBackStack() },
                    onRegistroExitoso = { nav.popBackStack() }
                )
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
                        onPedirCita = { nav.navigate(Rutas.pedirCita()) },
                        onPedirCitaServicio = { sId -> nav.navigate(Rutas.pedirCita(servicioId = sId)) },
                        onNotificaciones = { nav.navigate(Rutas.NOTIFICACIONES) }
                    )
                }
            }

            composable(Rutas.NOTIFICACIONES) {
                usuario?.let { u ->
                    val vm: InicioViewModel = viewModel(
                        key = "inicio_${u.id}",
                        factory = VetViewModelFactory(app.repository, app.prefs, u.id)
                    )
                    NotificacionesScreen(
                        vm = vm,
                        onVolver = { nav.popBackStack() }
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

            // Perfil del dueño: datos, contadores, preferencias, cambiar de perfil y cerrar sesión
            composable(Rutas.PERFIL) {
                val u = usuario
                if (u == null) {
                    androidx.compose.runtime.LaunchedEffect(Unit) {
                        irLimpiandoPila(Rutas.LOGIN)
                    }
                } else {
                    val inicioVm: InicioViewModel = viewModel(
                        key = "inicio_${u.id}",
                        factory = VetViewModelFactory(app.repository, app.prefs, u.id)
                    )
                    val citasVm: MisCitasViewModel = viewModel(
                        key = "citas_${u.id}",
                        factory = VetViewModelFactory(app.repository, app.prefs, u.id)
                    )
                    val mascotas by inicioVm.mascotas.collectAsState()
                    val proximas by citasVm.proximas.collectAsState()
                    val historial by citasVm.historial.collectAsState()
                    val oscuro by sesionVm.modoOscuro.collectAsState()
                    val recordatorios by sesionVm.notificaciones.collectAsState()

                    PerfilScreen(
                        usuario = u,
                        mascotas = mascotas.size,
                        citasProximas = proximas.size,
                        atenciones = historial.count { it.cita.estado == EstadoCita.ATENDIDA.name },
                        modoOscuro = oscuro,
                        recordatorios = recordatorios,
                        onModoOscuro = { sesionVm.cambiarModoOscuro(it) },
                        onRecordatorios = { sesionVm.cambiarNotificaciones(it) },
                        onCambiarPerfil = {
                            sesionVm.cambiarPerfil { rol ->
                                irLimpiandoPila(if (rol == Rol.ADMIN) Rutas.PANEL else Rutas.INICIO)
                            }
                        },
                        onCerrarSesion = {
                            sesionVm.cerrarSesion()
                        }
                    )
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
                    },
                    navArgument("servicioId") {
                        type = NavType.IntType
                        defaultValue = -1
                    }
                )
            ) { backStackEntry ->
                val idMascota = backStackEntry.arguments?.getInt("mascotaId") ?: -1
                val idServicio = backStackEntry.arguments?.getInt("servicioId") ?: -1
                usuario?.let { u ->
                    val vm: PedirCitaViewModel = viewModel(
                        factory = VetViewModelFactory(app.repository, app.prefs, u.id)
                    )
                    PedirCitaScreen(
                        vm = vm,
                        mascotaInicial = if (idMascota == -1) null else idMascota,
                        servicioInicial = if (idServicio == -1) null else idServicio,
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

            composable(
                Rutas.ATENDER,
                arguments = listOf(navArgument("citaId") { type = NavType.IntType })
            ) { backStackEntry ->
                val citaId = backStackEntry.arguments?.getInt("citaId") ?: 0
                val vm: AtenderCitaViewModel = viewModel(
                    key = "atender_$citaId",
                    factory = VetViewModelFactory(app.repository, app.prefs, citaId = citaId)
                )
                AtenderCitaScreen(
                    vm = vm,
                    onVolver = { nav.popBackStack() }
                )
            }

            composable(Rutas.PACIENTES) {
                val vm: PacientesViewModel = viewModel(
                    key = "pacientes",
                    factory = VetViewModelFactory(app.repository, app.prefs)
                )
                PacientesScreen(
                    vm = vm,
                    onPaciente = { nav.navigate(Rutas.fichaVet(it)) }
                )
            }

            composable(
                Rutas.FICHA_VET,
                arguments = listOf(navArgument("mascotaId") { type = NavType.IntType })
            ) { backStackEntry ->
                val mascotaId = backStackEntry.arguments?.getInt("mascotaId") ?: 0
                val vm: MascotaViewModel = viewModel(
                    key = "ficha_vet_$mascotaId",
                    factory = VetViewModelFactory(app.repository, app.prefs)
                )
                FichaMascotaScreen(
                    vm = vm,
                    mascotaId = mascotaId,
                    onVolver = { nav.popBackStack() },
                    onEditar = {},
                    onPedirCita = {},
                    soloLectura = true
                )
            }

            // Lista de servicios con switch de activo
            composable(Rutas.SERVICIOS) {
                val vm: ServiciosViewModel = viewModel(
                    key = "servicios",
                    factory = VetViewModelFactory(app.repository, app.prefs)
                )
                ServiciosScreen(
                    vm = vm,
                    onNuevo = { nav.navigate(Rutas.servicioForm()) },
                    onEditar = { nav.navigate(Rutas.servicioForm(it)) }
                )
            }

            // Formulario de servicio: crear (-1) o editar
            composable(
                Rutas.SERVICIO_FORM,
                arguments = listOf(
                    navArgument("servicioId") {
                        type = NavType.IntType
                        defaultValue = -1
                    }
                )
            ) { backStackEntry ->
                val id = backStackEntry.arguments?.getInt("servicioId") ?: -1
                // Sin key: cada vez que se abre el formulario es un ViewModel nuevo
                val vm: ServiciosViewModel = viewModel(
                    factory = VetViewModelFactory(app.repository, app.prefs)
                )
                FormularioServicioScreen(
                    vm = vm,
                    servicioId = if (id == -1) null else id,
                    onListo = { nav.popBackStack() }
                )
            }

            // Ajustes del veterinario
            composable(Rutas.AJUSTES) {
                val u = usuario
                if (u == null) {
                    androidx.compose.runtime.LaunchedEffect(Unit) {
                        irLimpiandoPila(Rutas.LOGIN)
                    }
                } else {
                    val oscuro by sesionVm.modoOscuro.collectAsState()
                    val notificaciones by sesionVm.notificaciones.collectAsState()
                    val aceptarCitas by sesionVm.aceptarCitas.collectAsState()

                    AjustesScreen(
                        usuario = u,
                        modoOscuro = oscuro,
                        notificaciones = notificaciones,
                        aceptarCitas = aceptarCitas,
                        onModoOscuro = { sesionVm.cambiarModoOscuro(it) },
                        onNotificaciones = { sesionVm.cambiarNotificaciones(it) },
                        onAceptarCitas = { sesionVm.cambiarAceptarCitas(it) },
                        onCambiarPerfil = {
                            sesionVm.cambiarPerfil { rol ->
                                irLimpiandoPila(if (rol == Rol.ADMIN) Rutas.PANEL else Rutas.INICIO)
                            }
                        },
                        onCerrarSesion = {
                            sesionVm.cerrarSesion()
                        }
                    )
                }
            }
        }
    }
}

private data class ItemBarraNav(
    val ruta: String,
    val etiqueta: String,
    val resNorm: Int,
    val resSel: Int
)

@Composable
private fun BarraDueno(rutaActual: String?, onPestana: (String) -> Unit) {
    val items = listOf(
        ItemBarraNav(Rutas.INICIO, "Inicio", com.rocha.huellitasvet.R.drawable.ic_nav_home, com.rocha.huellitasvet.R.drawable.ic_nav_home_sel),
        ItemBarraNav(Rutas.MASCOTAS, "Mascotas", com.rocha.huellitasvet.R.drawable.ic_nav_pets, com.rocha.huellitasvet.R.drawable.ic_nav_pets_sel),
        ItemBarraNav(Rutas.CITAS, "Citas", com.rocha.huellitasvet.R.drawable.ic_nav_calendar, com.rocha.huellitasvet.R.drawable.ic_nav_calendar_sel),
        ItemBarraNav(Rutas.PERFIL, "Perfil", com.rocha.huellitasvet.R.drawable.ic_nav_profile, com.rocha.huellitasvet.R.drawable.ic_nav_profile_sel)
    )
    BarraInferior(items, rutaActual, onPestana)
}

@Composable
private fun BarraVet(rutaActual: String?, onPestana: (String) -> Unit) {
    val items = listOf(
        ItemBarraNav(Rutas.PANEL, "Panel", com.rocha.huellitasvet.R.drawable.ic_nav_dashboard, com.rocha.huellitasvet.R.drawable.ic_nav_dashboard_sel),
        ItemBarraNav(Rutas.AGENDA, "Agenda", com.rocha.huellitasvet.R.drawable.ic_nav_calendar, com.rocha.huellitasvet.R.drawable.ic_nav_calendar_sel),
        ItemBarraNav(Rutas.PACIENTES, "Pacientes", com.rocha.huellitasvet.R.drawable.ic_nav_medical, com.rocha.huellitasvet.R.drawable.ic_nav_medical_sel),
        ItemBarraNav(Rutas.SERVICIOS, "Servicios", com.rocha.huellitasvet.R.drawable.ic_nav_pets, com.rocha.huellitasvet.R.drawable.ic_nav_pets_sel),
        ItemBarraNav(Rutas.AJUSTES, "Ajustes", com.rocha.huellitasvet.R.drawable.ic_nav_settings, com.rocha.huellitasvet.R.drawable.ic_nav_settings_sel)
    )
    BarraInferior(items, rutaActual, onPestana)
}

@Composable
private fun BarraInferior(
    items: List<ItemBarraNav>,
    rutaActual: String?,
    onPestana: (String) -> Unit
) {
    val colores = MaterialTheme.colorScheme
    Surface(
        color = colores.surface,
        border = BorderStroke(0.5.dp, colores.outlineVariant.copy(alpha = 0.3f))
    ) {
        NavigationBar(
            containerColor = Color.Transparent,
            tonalElevation = 0.dp
        ) {
            items.forEach { item ->
                val seleccionada = rutaActual == item.ruta

                NavigationBarItem(
                    selected = seleccionada,
                    onClick = { onPestana(item.ruta) },
                    icon = {
                        Icon(
                            painter = androidx.compose.ui.res.painterResource(id = if (seleccionada) item.resSel else item.resNorm),
                            contentDescription = item.etiqueta,
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    label = {
                        Text(
                            text = item.etiqueta,
                            fontWeight = if (seleccionada) FontWeight.Bold else FontWeight.Medium
                        )
                    },
                    alwaysShowLabel = true,
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = colores.primary,
                        selectedTextColor = colores.primary,
                        indicatorColor = colores.primaryContainer,
                        unselectedIconColor = colores.onSurfaceVariant.copy(alpha = 0.7f),
                        unselectedTextColor = colores.onSurfaceVariant.copy(alpha = 0.7f)
                    )
                )
            }
        }
    }
}