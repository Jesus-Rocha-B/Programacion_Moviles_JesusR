package com.rocha.huellitasvet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rocha.huellitasvet.data.local.relation.CitaConDetalle
import com.rocha.huellitasvet.data.repository.VetRepository
import com.rocha.huellitasvet.model.EstadoCita
import com.rocha.huellitasvet.model.fechaHora
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalDateTime

class MisCitasViewModel(
    private val repo: VetRepository,
    duenoId: Int
) : ViewModel() {

    // Solo las citas de las mascotas de este dueño. Si el veterinario confirma
    // o atiende una cita, Room avisa y la lista cambia sola.
    private val citas = repo.observarCitasDeDueno(duenoId)

    private fun esActiva(c: CitaConDetalle): Boolean =
        c.cita.estado == EstadoCita.PENDIENTE.name ||
                c.cita.estado == EstadoCita.CONFIRMADA.name

    /** Próximas = PENDIENTE y CONFIRMADA desde ahora, la más cercana primero */
    val proximas: StateFlow<List<CitaConDetalle>> = citas
        .map { lista ->
            val ahora = LocalDateTime.now()
            lista
                .filter { esActiva(it) && !it.fechaHora().isBefore(ahora) }
                .sortedBy { it.fechaHora() }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    /** Historial = ATENDIDA, CANCELADA y las que ya pasaron, la más reciente primero */
    val historial: StateFlow<List<CitaConDetalle>> = citas
        .map { lista ->
            val ahora = LocalDateTime.now()
            lista
                .filter { !(esActiva(it) && !it.fechaHora().isBefore(ahora)) }
                .sortedByDescending { it.fechaHora() }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje.asStateFlow()

    /**
     * Regla de cancelación: solo PENDIENTE o CONFIRMADA
     * y solo si faltan MÁS de 2 horas para la cita.
     */
    fun puedeCancelar(c: CitaConDetalle): Boolean {
        if (!esActiva(c)) return false
        val faltan = Duration.between(LocalDateTime.now(), c.fechaHora())
        return faltan > Duration.ofHours(2)
    }

    fun puedeReprogramar(c: CitaConDetalle): Boolean = puedeCancelar(c)

    fun observarHorasOcupadas(fecha: String) = repo.observarHorasOcupadas(fecha)

    fun avisarNoCancelable() {
        _mensaje.value = "Ya no puedes cancelar o reprogramar esta cita (menos de 2h de anticipación)"
    }

    fun reprogramar(c: CitaConDetalle, nuevaFecha: String, nuevaHora: String) {
        if (!puedeReprogramar(c)) {
            avisarNoCancelable()
            return
        }
        val ahora = LocalDateTime.now()
        val nuevaFechaHora = try {
            LocalDateTime.parse("${nuevaFecha}T${nuevaHora}:00")
        } catch (e: Exception) {
            _mensaje.value = "Fecha u hora seleccionada no es válida"
            return
        }
        if (Duration.between(ahora, nuevaFechaHora) <= Duration.ofHours(2)) {
            _mensaje.value = "La nueva cita debe ser con al menos 2 horas de anticipación"
            return
        }

        viewModelScope.launch {
            repo.reprogramarCita(c.cita.id, nuevaFecha, nuevaHora)
            _mensaje.value = "Cita reprogramada exitosamente"
        }
    }

    fun cancelar(c: CitaConDetalle) {
        // Se vuelve a validar al momento de cancelar, por si pasó el tiempo
        if (!puedeCancelar(c)) {
            avisarNoCancelable()
            return
        }
        viewModelScope.launch {
            repo.cambiarEstadoCita(c.cita.id, EstadoCita.CANCELADA)
            _mensaje.value = "Cita cancelada"
        }
    }

    fun limpiarMensaje() {
        _mensaje.value = null
    }
}