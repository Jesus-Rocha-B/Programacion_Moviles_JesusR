package com.rocha.huellitasvet.viewmodel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rocha.huellitasvet.data.local.entity.Mascota
import com.rocha.huellitasvet.data.local.relation.CitaConDetalle
import com.rocha.huellitasvet.data.repository.VetRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate

/** Un mensaje de error por campo (null = sin error). */
data class ErroresMascota(
    val nombre: String? = null,
    val especie: String? = null,
    val sexo: String? = null,
    val anio: String? = null,
    val peso: String? = null
) {
    val hayErrores: Boolean
        get() = listOf(nombre, especie, sexo, anio, peso).any { it != null }
}

class MascotaViewModel(
    private val repo: VetRepository,
    private val duenoId: Int
) : ViewModel() {

    private val _errores = MutableStateFlow(ErroresMascota())
    val errores: StateFlow<ErroresMascota> = _errores.asStateFlow()

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje.asStateFlow()

    private val _guardada = MutableStateFlow(false)
    val guardada: StateFlow<Boolean> = _guardada.asStateFlow()

    private val _eliminada = MutableStateFlow(false)
    val eliminada: StateFlow<Boolean> = _eliminada.asStateFlow()

    fun mascota(id: Int): Flow<Mascota?> = repo.observarMascota(id)

    fun historial(mascotaId: Int): StateFlow<List<CitaConDetalle>> =
        repo.observarHistorial(mascotaId)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun observarPesos(mascotaId: Int): StateFlow<List<com.rocha.huellitasvet.model.RegistroPeso>> =
        repo.observarPesosMascota(mascotaId)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun observarVacunas(mascotaId: Int): StateFlow<List<com.rocha.huellitasvet.data.local.entity.Vacuna>> =
        repo.observarVacunasMascota(mascotaId)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun registrarVacuna(mascotaId: Int, nombre: String, fecha: String, proximaDosis: String?) {
        if (nombre.isBlank()) return
        viewModelScope.launch {
            repo.registrarVacuna(
                com.rocha.huellitasvet.data.local.entity.Vacuna(
                    mascotaId = mascotaId,
                    nombre = nombre.trim(),
                    fecha = fecha.ifBlank { LocalDate.now().toString() },
                    proximaDosis = proximaDosis?.ifBlank { null }
                )
            )
            _mensaje.value = "Vacuna registrada exitosamente"
        }
    }

    /**
     * Valida y guarda. id = 0 crea una mascota nueva; id > 0 edita la existente.
     * Reglas: nombre >= 2 caracteres, especie y sexo elegidos,
     * año entre 1990 y el año actual, peso > 0.
     */
    fun guardar(
        id: Int,
        nombre: String,
        especie: String?,
        raza: String,
        anio: String,
        peso: String,
        sexo: String?
    ) {
        val anioActual = LocalDate.now().year
        val anioNum = anio.trim().toIntOrNull()
        val pesoNum = peso.trim().replace(',', '.').toDoubleOrNull()

        val errores = ErroresMascota(
            nombre = if (nombre.trim().length < 2) "Escribe al menos 2 caracteres" else null,
            especie = if (especie == null) "Elige una especie" else null,
            sexo = if (sexo == null) "Elige el sexo" else null,
            anio = if (anioNum == null || anioNum !in 1990..anioActual)
                "Ingresa un año entre 1990 y $anioActual" else null,
            peso = if (pesoNum == null || pesoNum <= 0.0)
                "El peso debe ser mayor que 0" else null
        )
        _errores.value = errores
        if (errores.hayErrores) return

        viewModelScope.launch {
            repo.guardarMascota(
                Mascota(
                    id = id,
                    duenoId = duenoId,
                    nombre = nombre.trim(),
                    especie = especie!!,
                    raza = raza.trim(),
                    anioNacimiento = anioNum!!,
                    peso = pesoNum!!,
                    sexo = sexo!!
                )
            )
            _guardada.value = true
        }
    }

    /** Si la mascota tiene citas no se elimina y se muestra un mensaje. */
    fun eliminar(m: Mascota) {
        viewModelScope.launch {
            if (repo.mascotaTieneCitas(m.id)) {
                _mensaje.value = "No se puede eliminar a ${m.nombre}: tiene citas registradas"
            } else {
                repo.eliminarMascota(m)
                _eliminada.value = true
            }
        }
    }

    fun limpiarMensaje() {
        _mensaje.value = null
    }
}