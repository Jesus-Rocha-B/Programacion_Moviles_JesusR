package com.rocha.huellitasvet.viewmodel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rocha.huellitasvet.data.local.relation.CitaConDetalle
import com.rocha.huellitasvet.data.repository.VetRepository
import com.rocha.huellitasvet.model.EstadoCita
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate

@OptIn(ExperimentalCoroutinesApi::class)
class AgendaViewModel(private val repo: VetRepository) : ViewModel() {

    /** Chips de los próximos días */
    val fechas: List<LocalDate> = (0..6).map { LocalDate.now().plusDays(it.toLong()) }

    private val _fechaSeleccionada = MutableStateFlow(LocalDate.now()) // por defecto: hoy
    val fechaSeleccionada: StateFlow<LocalDate> = _fechaSeleccionada.asStateFlow()

    /** Citas del día elegido, ordenadas por hora (el DAO ya las ordena) */
    val citas: StateFlow<List<CitaConDetalle>> = _fechaSeleccionada
        .flatMapLatest { repo.observarCitasPorFecha(it.toString()) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje.asStateFlow()

    fun elegirFecha(f: LocalDate) {
        _fechaSeleccionada.value = f
    }

    /** Regla de estados: solo PENDIENTE -> CONFIRMADA */
    fun confirmar(c: CitaConDetalle) {
        if (c.cita.estado != EstadoCita.PENDIENTE.name) {
            _mensaje.value = "Solo se pueden confirmar citas pendientes"
            return
        }
        viewModelScope.launch {
            repo.cambiarEstadoCita(c.cita.id, EstadoCita.CONFIRMADA)
        }
    }

    /** Regla de estados: se cancela desde PENDIENTE o CONFIRMADA. ATENDIDA y CANCELADA son finales. */
    fun cancelar(c: CitaConDetalle) {
        val estado = c.cita.estado
        if (estado != EstadoCita.PENDIENTE.name && estado != EstadoCita.CONFIRMADA.name) {
            _mensaje.value = "Esta cita ya no se puede cancelar"
            return
        }
        viewModelScope.launch {
            repo.cambiarEstadoCita(c.cita.id, EstadoCita.CANCELADA)
        }
    }

    fun limpiarMensaje() {
        _mensaje.value = null
    }
}