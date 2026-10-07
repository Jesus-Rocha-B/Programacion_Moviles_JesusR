package com.rocha.saludplus.repository

import com.rocha.saludplus.model.Usuario

// object es una sola instancia compartida por todas las pantallas
object Repositorio {
    // Usuario de prueba para no registrarse cada vez
    val usuarios = mutableListOf(
        Usuario("Juan Pérez", "999888777", "juan@correo.com", "123456")
    )
    // Usuario con la sesión iniciada (null = nadie logueado)
    var usuarioActual: Usuario? = null
    // Devuelve falso si el correo ya está registrado
    fun registrarUsuario(usuario: Usuario): Boolean {
        val existe = usuarios.any { it.correo.equals(usuario.correo, ignoreCase = true) }
        if (existe) return false
        usuarios.add(usuario)
        return true
    }
    // Busca el usuario en la lista, si lo encuentra deja la sesión iniciada
    fun iniciarSesion(correo: String, contrasena: String): Boolean {
        val usuario = usuarios.find {
            it.correo.equals(correo, ignoreCase = true) && it.contrasena == contrasena
        }
        usuarioActual = usuario
        return usuario != null
    }
    // Cerrar sesión
    fun cerrarSesion() {
        usuarioActual = null
    }
}