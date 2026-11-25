package com.rocha.campusgps.data

import com.rocha.campusgps.model.Posicion
import kotlinx.coroutines.flow.Flow

interface UbicacionRepository {
    suspend fun obtenerPosicion(): Result<Posicion>
    fun obtenerPosicionesEnVivo(): Flow<Posicion>
    fun playServicesDisponible(): Boolean
}
