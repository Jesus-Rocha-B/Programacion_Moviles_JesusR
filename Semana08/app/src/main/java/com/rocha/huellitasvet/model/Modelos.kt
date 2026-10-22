package com.rocha.huellitasvet.model

enum class Rol {
    CLIENTE, ADMIN
}
enum class EstadoCita {
    PENDIENTE, CONFIRMADA, ATENDIDA, CANCELADA
}
enum class Especie {
    PERRO, GATO, OTRO
}
enum class Sexo {
    MACHO, HEMBRA
}
data class ConteoServicio(
    val nombre: String,
    val total: Int
)
object Horarios {
    val todos: List<String> = (0 until 18).map { i ->
        val minutos = 9 * 60 + i * 30
        "%02d:%02d".format(minutos / 60, minutos % 60)
    }
}
data class UltimaVisita(
    val mascotaId: Int,
    val fecha: String
)
data class RegistroPeso(
    val fecha: String,
    val peso: Double
)