package com.rocha.saludplus.model

// Médico asociado a una especialidad mediante especialidadId
data class Medico(
    val id: Int,
    val nombre: String,
    val especialidadId: Int,
    val calificacion: Double,
    val aniosExperiencia: Int
)