package com.rocha.huellitasvet.data.repository

import androidx.room.withTransaction
import com.rocha.huellitasvet.data.local.VetDatabase
import com.rocha.huellitasvet.data.local.entity.Cita
import com.rocha.huellitasvet.data.local.entity.Mascota
import com.rocha.huellitasvet.data.local.entity.Servicio
import com.rocha.huellitasvet.data.local.entity.Usuario
import com.rocha.huellitasvet.data.local.relation.CitaConDetalle
import com.rocha.huellitasvet.data.local.relation.MascotaConDueno
import com.rocha.huellitasvet.model.ConteoServicio
import com.rocha.huellitasvet.model.EstadoCita
import kotlinx.coroutines.flow.Flow
import com.rocha.huellitasvet.model.UltimaVisita

class VetRepositoryRoom(private val db: VetDatabase) : VetRepository {
    private val usuarioDao = db.usuarioDao()
    private val servicioDao = db.servicioDao()
    private val mascotaDao = db.mascotaDao()
    private val citaDao = db.citaDao()
    // Usuarios
    override suspend fun login(correo: String, clave: String): Usuario? =
        usuarioDao.login(correo, clave)

    override suspend fun obtenerUsuario(id: Int): Usuario? =
        usuarioDao.obtener(id)

    override suspend fun obtenerUsuarioPorRol(rol: String): Usuario? =
        usuarioDao.obtenerPorRol(rol)
    // Servicios
    override fun observarServicios(): Flow<List<Servicio>> =
        servicioDao.observarTodos()
    override fun observarServiciosActivos(): Flow<List<Servicio>> =
        servicioDao.observarActivos()
    override suspend fun guardarServicio(s: Servicio) {
        // Nuevo (id = 0) se inserta; existente se actualiza
        if (s.id == 0) servicioDao.insertar(s) else servicioDao.actualizar(s)
    }
    override suspend fun eliminarServicio(s: Servicio) =
        servicioDao.eliminar(s)
    override suspend fun cambiarActivo(id: Int, activo: Boolean) =
        servicioDao.cambiarActivo(id, activo)
    override suspend fun servicioTieneCitas(id: Int): Boolean =
        citaDao.contarDeServicio(id) > 0
    // Mascotas
    override fun observarMascotasDeDueno(duenoId: Int): Flow<List<Mascota>> =
        mascotaDao.observarPorDueno(duenoId)
    override fun observarMascota(id: Int): Flow<Mascota?> =
        mascotaDao.observarUna(id)
    override fun observarTodasLasMascotas(): Flow<List<MascotaConDueno>> =
        mascotaDao.observarTodas()
    override suspend fun guardarMascota(m: Mascota) {
        if (m.id == 0) mascotaDao.insertar(m) else mascotaDao.actualizar(m)
    }
    override suspend fun eliminarMascota(m: Mascota) =
        mascotaDao.eliminar(m)
    override suspend fun mascotaTieneCitas(id: Int): Boolean =
        mascotaDao.contarCitas(id) > 0
    // Citas
    override fun observarHorasOcupadas(fecha: String): Flow<List<String>> =
        citaDao.observarHorasOcupadas(fecha)
    override suspend fun crearCita(
        mascotaId: Int, servicioId: Int, fecha: String, hora: String, nota: String
    ): Long {
        // Busca el servicio para copiar su precio en ese momento
        val servicio = servicioDao.obtener(servicioId)
            ?: throw IllegalArgumentException("El servicio $servicioId no existe")
        val cita = Cita(
            mascotaId = mascotaId,
            servicioId = servicioId,
            fecha = fecha,
            hora = hora,
            nota = nota,
            estado = EstadoCita.PENDIENTE.name,
            precio = servicio.precio,
            fechaCreacion = System.currentTimeMillis()
        )
        return citaDao.insertar(cita)
    }
    override fun observarCitasDeDueno(duenoId: Int): Flow<List<CitaConDetalle>> =
        citaDao.observarPorDueno(duenoId)
    override fun observarCitasPorFecha(fecha: String): Flow<List<CitaConDetalle>> =
        citaDao.observarPorFecha(fecha)
    override fun observarCita(id: Int): Flow<CitaConDetalle?> =
        citaDao.observarUna(id)
    override fun observarHistorial(mascotaId: Int): Flow<List<CitaConDetalle>> =
        citaDao.observarHistorial(mascotaId)
    override suspend fun cambiarEstadoCita(id: Int, estado: EstadoCita) =
        citaDao.actualizarEstado(id, estado.name)
    override suspend fun reprogramarCita(citaId: Int, nuevaFecha: String, nuevaHora: String) =
        citaDao.reprogramarCita(citaId, nuevaFecha, nuevaHora)
    override suspend fun registrarAtencion(
        citaId: Int, diagnostico: String, tratamiento: String, peso: Double
    ) {
        // Una sola transacción: si algo falla, no queda nada a medias
        db.withTransaction {
            val cita = citaDao.obtener(citaId) ?: return@withTransaction
            citaDao.registrarAtencion(citaId, diagnostico, tratamiento, peso)
            mascotaDao.actualizarPeso(cita.mascotaId, peso)
        }
    }

    // Panel
    override fun observarCitasDeFecha(fecha: String): Flow<Int> =
        citaDao.contarPorFecha(fecha)
    override fun observarCantidadPorEstado(estado: EstadoCita): Flow<Int> =
        citaDao.contarPorEstado(estado.name)
    override fun observarCitasPorServicio(): Flow<List<ConteoServicio>> =
        citaDao.contarPorServicio()
    override fun observarIngresosDesde(fechaInicio: String): Flow<Double> =
        citaDao.ingresosDesde(fechaInicio)
    override fun observarUltimasVisitas(): Flow<List<UltimaVisita>> =
        citaDao.ultimasVisitas()
    override fun observarPesosMascota(mascotaId: Int): Flow<List<com.rocha.huellitasvet.model.RegistroPeso>> =
        citaDao.observarPesosMascota(mascotaId)
}