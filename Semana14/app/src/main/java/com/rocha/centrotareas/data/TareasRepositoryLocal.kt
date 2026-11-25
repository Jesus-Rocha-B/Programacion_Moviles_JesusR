package com.rocha.centrotareas.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import java.io.IOException

class TareasRepositoryLocal : TareasRepository {
    override fun ejecutarTareaLarga(): Flow<Float> = flow {
        for (i in 1..100) {
            delay(40)
            emit(i / 100f)
        }
    }.flowOn(Dispatchers.Default)

    override suspend fun cargarPerfil(): String {
        delay(2000)
        return "Ana Torres (ficticia)"
    }

    override suspend fun cargarPedidos(simularError: Boolean): List<String> {
        delay(3000)
        if (simularError) throw IOException("Sin conexión")
        return listOf("Pedido 101", "Pedido 102", "Pedido 103")
    }

    override suspend fun contarPrimos(hasta: Int): Int = contarPrimos(hasta, null)

    override suspend fun contarPrimos(
        hasta: Int,
        onProgreso: (suspend (Float) -> Unit)?
    ): Int = withContext(Dispatchers.Default) {
        var cantidad = 0
        val pasoReporte = (hasta / 100).coerceAtLeast(1)
        for (n in 2..hasta) {
            ensureActive()
            if (esPrimo(n)) {
                cantidad++
            }
            if (onProgreso != null && (n % pasoReporte == 0 || n == hasta)) {
                val progreso = (n - 1).toFloat() / (hasta - 1).coerceAtLeast(1)
                onProgreso(progreso)
            }
        }
        cantidad
    }

    private fun esPrimo(n: Int): Boolean {
        if (n < 2) return false
        var d = 2
        while (d.toLong() * d <= n) {
            if (n % d == 0) return false
            d++
        }
        return true
    }
}
