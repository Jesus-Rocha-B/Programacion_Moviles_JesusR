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
    const val FORMULARIO = "formulario"
    const val PEDIR_CITA = "pedir_cita"
    fun ficha(id: Int) = "ficha/$id"
    // Veterinario
    const val PANEL = "panel"
}