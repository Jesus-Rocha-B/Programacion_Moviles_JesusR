package com.rocha.saludplus.model

// Datos del paciente que se registra en la app
data class Usuario(
    val nombre: String,
    val telefono: String,
    val correo: String,
    val contrasena: String
)