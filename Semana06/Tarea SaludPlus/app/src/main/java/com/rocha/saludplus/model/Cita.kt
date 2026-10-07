package com.rocha.saludplus.model

// Cita agendada. La fecha se guarda como (año-mes-día) para poder ordenarlas fácilmente y usarla en las rutas sin espacios
data class Cita(
    val id: Int,
    val correoUsuario: String,
    val medicoId: Int,
    val fecha: String,
    val hora: String
)