package com.rocha.saludplus.model

// Especialidad médica con identificador de icono opcional
data class Especialidad(
    val id: Int,
    val nombre: String,
    val descripcion: String,
    val iconoResId: Int = 0
)
