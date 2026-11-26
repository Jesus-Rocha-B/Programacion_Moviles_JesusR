package com.tecsup.murocampus.viewmodel

import java.io.IOException
import kotlinx.serialization.SerializationException
import retrofit2.HttpException

// [E] ENTREGADO: Estados de UI
sealed interface UiState<out T> {
    data object Cargando : UiState<Nothing>
    data class Exito<T>(val datos: T) : UiState<T>
    data class Error(val mensaje: String) : UiState<Nothing>
}

fun mensajeDe(error: Throwable): String = when (error) {
    is IOException -> "Sin conexión a internet.\nRevisa tu red e inténtalo otra vez."
    is HttpException -> "Error del servidor (${error.code()})"
    is SerializationException -> "Respuesta inesperada"
    else -> "Error desconocido"
}
