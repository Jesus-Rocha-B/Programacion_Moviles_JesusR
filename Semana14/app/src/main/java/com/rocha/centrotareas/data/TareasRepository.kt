package com.rocha.centrotareas.data

import kotlinx.coroutines.flow.Flow

interface TareasRepository {
    fun ejecutarTareaLarga(): Flow<Float>
    suspend fun contarPrimos(hasta: Int): Int
    suspend fun contarPrimos(hasta: Int, onProgreso: (suspend (Float) -> Unit)?): Int
    suspend fun cargarPerfil(): String
    suspend fun cargarPedidos(simularError: Boolean): List<String>
}
