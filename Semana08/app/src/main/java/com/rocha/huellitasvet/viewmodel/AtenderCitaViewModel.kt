package com.rocha.huellitasvet.viewmodel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rocha.huellitasvet.data.local.relation.CitaConDetalle
import com.rocha.huellitasvet.data.repository.VetRepository
import com.rocha.huellitasvet.model.EstadoCita
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class ErroresAtencion(
    val diagnostico: String? = null,
    val peso: String? = null
) {
    val hayErrores: Boolean get() = diagnostico != null || peso != null
}

class AtenderCitaViewModel(
    private val repo: VetRepository,
    private val citaId: Int
) : ViewModel() {

    val cita: StateFlow<CitaConDetalle?> = repo.observarCita(citaId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    private val _errores = MutableStateFlow(ErroresAtencion())
    val errores: StateFlow<ErroresAtencion> = _errores.asStateFlow()

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje.asStateFlow()

    private val _terminado = MutableStateFlow(false)
    val terminado: StateFlow<Boolean> = _terminado.asStateFlow()

    /** Solo se puede atender una cita PENDIENTE o CONFIRMADA */
    private fun atendible(c: CitaConDetalle?): Boolean =
        c != null && (
                c.cita.estado == EstadoCita.PENDIENTE.name ||
                        c.cita.estado == EstadoCita.CONFIRMADA.name
                )

    /**
     * Valida y registra la atención. Reglas: diagnóstico >= 5 caracteres, peso > 0.
     * repo.registrarAtencion deja la cita ATENDIDA y actualiza el peso de la
     * mascota en la misma transacción.
     */
    fun registrar(diagnostico: String, tratamiento: String, peso: String, proximoControl: String = "") {
        if (!atendible(cita.value)) {
            _mensaje.value = "Solo se puede atender una cita pendiente o confirmada"
            return
        }

        val pesoNum = peso.trim().replace(',', '.').toDoubleOrNull()
        val errores = ErroresAtencion(
            diagnostico = if (diagnostico.trim().length < 5)
                "Escribe al menos 5 caracteres" else null,
            peso = if (pesoNum == null || pesoNum <= 0.0)
                "El peso debe ser mayor que 0" else null
        )
        _errores.value = errores
        if (errores.hayErrores) return

        viewModelScope.launch {
            repo.registrarAtencion(
                citaId = citaId,
                diagnostico = diagnostico.trim(),
                tratamiento = tratamiento.trim(),
                peso = pesoNum!!,
                proximoControl = proximoControl.trim().ifBlank { null }
            )
            _terminado.value = true
        }
    }

    /** Cancela la cita (solo desde PENDIENTE o CONFIRMADA). */
    fun cancelar() {
        if (!atendible(cita.value)) {
            _mensaje.value = "Esta cita ya no se puede cancelar"
            return
        }
        viewModelScope.launch {
            repo.cambiarEstadoCita(citaId, EstadoCita.CANCELADA)
            _terminado.value = true
        }
    }

    fun limpiarMensaje() {
        _mensaje.value = null
    }
}