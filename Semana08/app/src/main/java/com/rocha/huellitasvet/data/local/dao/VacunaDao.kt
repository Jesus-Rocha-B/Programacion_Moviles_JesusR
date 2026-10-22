package com.rocha.huellitasvet.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.rocha.huellitasvet.data.local.entity.Vacuna
import kotlinx.coroutines.flow.Flow

@Dao
interface VacunaDao {
    @Query("SELECT * FROM vacunas WHERE mascotaId = :mascotaId ORDER BY fecha DESC")
    fun observarPorMascota(mascotaId: Int): Flow<List<Vacuna>>

    @Insert
    suspend fun insertar(v: Vacuna): Long

    @Insert
    suspend fun insertarTodas(lista: List<Vacuna>)
}
