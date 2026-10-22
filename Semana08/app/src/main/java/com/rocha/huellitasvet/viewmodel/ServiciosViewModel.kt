package com.rocha.huellitasvet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rocha.huellitasvet.data.local.entity.Servicio
import com.rocha.huellitasvet.data.repository.VetRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class ErroresServicio(
    val nombre: String? = null,
    val precio: String? = null
) {
    val hayErrores: Boolean get() = nombre != null || precio != null
}

class ServiciosViewModel(private val repo: VetRepository) : ViewModel() {

    val servicios: StateFlow<List<Servicio>> = repo.observarServicios()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _errores = MutableStateFlow(ErroresServicio())
    val errores: StateFlow<ErroresServicio> = _errores.asStateFlow()

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje.asStateFlow()

    private val _guardado = MutableStateFlow(false)
    val guardado: StateFlow<Boolean> = _guardado.asStateFlow()

    private val _eliminado = MutableStateFlow(false)
    val eliminado: StateFlow<Boolean> = _eliminado.asStateFlow()

    fun alternar(s: Servicio) {
        viewModelScope.launch { repo.cambiarActivo(s.id, !s.activo) }
    }

    /**
     * Guarda un servicio nuevo (id = 0) o editado.
     * Reglas: nombre obligatorio, duración 15/30/45/60 y precio > 0.
     */
    fun guardar(
        id: Int,
        nombre: String,
        duracionMin: Int,
        precio: String,
        descripcion: String,
        icono: String,
        activo: Boolean
    ) {
        val precioNum = precio.trim().replace(',', '.').toDoubleOrNull()
        val errores = ErroresServicio(
            nombre = if (nombre.isBlank()) "El nombre es obligatorio" else null,
            precio = if (precioNum == null || precioNum <= 0.0)
                "El precio debe ser mayor que 0" else null
        )
        _errores.value = errores
        if (errores.hayErrores) return

        if (duracionMin !in listOf(15, 30, 45, 60)) {
            _mensaje.value = "Elige una duración válida"
            return
        }

        viewModelScope.launch {
            repo.guardarServicio(
                Servicio(
                    id = id,
                    nombre = nombre.trim(),
                    descripcion = descripcion.trim(),
                    duracionMin = duracionMin,
                    precio = precioNum!!,
                    activo = activo,
                    icono = icono
                )
            )
            _guardado.value = true
        }
    }

    /** Un servicio con citas no se elimina: solo se desactiva. */
    fun eliminar(s: Servicio) {
        viewModelScope.launch {
            if (repo.servicioTieneCitas(s.id)) {
                _mensaje.value = "No se puede eliminar \"${s.nombre}\" porque tiene citas. " +
                        "Puedes desactivarlo."
            } else {
                repo.eliminarServicio(s)
                _eliminado.value = true
            }
        }
    }

    fun limpiarMensaje() {
        _mensaje.value = null
    }
}