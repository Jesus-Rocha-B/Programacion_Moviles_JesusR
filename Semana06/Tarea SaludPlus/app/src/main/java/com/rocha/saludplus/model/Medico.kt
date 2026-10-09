package com.rocha.saludplus.model

// Médico asociado a una especialidad mediante especialidadId y fotoResId, incluyendo sede y horarios
data class Medico(
    val id: Int,
    val nombre: String,
    val especialidadId: Int,
    val calificacion: Double,
    val aniosExperiencia: Int,
    val fotoResId: Int = 0,
    val sede: String = "Independencia",
    val horariosAtencion: List<String> = listOf("08:00", "09:00", "10:00", "11:00", "14:00", "15:00", "16:00")
)
