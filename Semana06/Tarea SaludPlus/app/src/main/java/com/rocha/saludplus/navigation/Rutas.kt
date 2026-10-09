package com.rocha.saludplus.navigation

// Rutas
object Rutas {
    const val SPLASH = "splash"
    const val REGISTRO = "registro"
    const val LOGIN = "login"
    const val TERMINOS = "terminos"
    const val HOME = "home"
    const val MIS_DOCTORES = "misdoctores"
    const val ESPECIALIDADES = "especialidades"
    const val MIS_CITAS = "miscitas"
    const val PERFIL = "perfil"
    const val RESULTADOS = "resultados"
    const val NOTIFICACIONES = "notificaciones"

    // Rutas con parametros
    const val MEDICOS = "medicos/{especialidadId}"
    const val FECHA_HORA = "fechahora/{medicoId}"
    const val CONFIRMAR = "confirmar/{medicoId}/{fecha}/{hora}"
    const val CITA_EXITOSA = "citaexitosa/{citaId}"
    const val DETALLE_CITA = "detallecita/{citaId}"

    // Funciones que arman la ruta con el valor real
    fun medicos(especialidadId: Int) = "medicos/$especialidadId"
    fun fechaHora(medicoId: Int) = "fechahora/$medicoId"
    fun confirmar(medicoId: Int, fecha: String, hora: String) = "confirmar/$medicoId/$fecha/$hora"
    fun citaExitosa(citaId: Int) = "citaexitosa/$citaId"
    fun detalleCita(citaId: Int) = "detallecita/$citaId"
}