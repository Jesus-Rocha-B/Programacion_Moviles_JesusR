package com.rocha.huellitasvet.ui.navigation
object Rutas {
    const val SPLASH = "splash"
    const val LOGIN = "login"
    // Dueño (con barra inferior)
    const val INICIO = "inicio"
    const val MASCOTAS = "mascotas"
    const val CITAS = "citas"
    const val PERFIL = "perfil"
    // Dueño (pantallas secundarias)
    const val FICHA = "ficha/{mascotaId}"
    const val FORMULARIO = "formulario?mascotaId={mascotaId}"
    const val PEDIR_CITA = "pedir_cita?mascotaId={mascotaId}"
    fun ficha(id: Int) = "ficha/$id"
    /** Sin argumento = mascota nueva (-1). Con id = editar. */
    fun formulario(id: Int = -1) = "formulario?mascotaId=$id"
    /** Sin argumento = sin mascota preseleccionada (-1). */
    fun pedirCita(mascotaId: Int = -1) = "pedir_cita?mascotaId=$mascotaId"
    // Veterinario (con barra inferior)
    const val PANEL = "panel"
    const val AGENDA = "agenda"
    const val PACIENTES = "pacientes"
    const val SERVICIOS = "servicios"
    const val AJUSTES = "ajustes"

    // Veterinario (pantallas secundarias)
    const val ATENDER = "atender/{citaId}"
    fun atender(id: Int) = "atender/$id"
}