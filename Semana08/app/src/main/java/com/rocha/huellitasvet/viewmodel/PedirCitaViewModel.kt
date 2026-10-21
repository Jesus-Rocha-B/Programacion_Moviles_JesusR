package com.rocha.huellitasvet.viewmodel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rocha.huellitasvet.data.local.entity.Mascota
import com.rocha.huellitasvet.data.local.entity.Servicio
import com.rocha.huellitasvet.data.repository.VetRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime

@OptIn(ExperimentalCoroutinesApi::class)
class PedirCitaViewModel(
    private val repo: VetRepository,
    duenoId: Int
) : ViewModel() {

    /** Solo las mascotas de este dueño */
    val mascotas: StateFlow<List<Mascota>> = repo.observarMascotasDeDueno(duenoId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    /** Solo servicios ACTIVOS: los inactivos no aparecen */
    val servicios: StateFlow<List<Servicio>> = repo.observarServiciosActivos()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    /** Chips de fecha: de hoy a 7 días */
    val fechas: List<LocalDate> = (0..7).map { LocalDate.now().plusDays(it.toLong()) }

    // ---------- Lo que el usuario va eligiendo ----------
    private val _mascotaId = MutableStateFlow<Int?>(null)
    val mascotaId: StateFlow<Int?> = _mascotaId.asStateFlow()

    private val _servicioId = MutableStateFlow<Int?>(null)
    val servicioId: StateFlow<Int?> = _servicioId.asStateFlow()

    private val _fecha = MutableStateFlow(LocalDate.now()) // por defecto: hoy
    val fecha: StateFlow<LocalDate> = _fecha.asStateFlow()

    private val _hora = MutableStateFlow<String?>(null)
    val hora: StateFlow<String?> = _hora.asStateFlow()

    /** Las horas ocupadas cambian cada vez que cambia la fecha */
    val horasOcupadas: StateFlow<List<String>> = _fecha
        .flatMapLatest { repo.observarHorasOcupadas(it.toString()) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // ---------- Mensajes y resultado ----------
    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje.asStateFlow()

    private val _creada = MutableStateFlow(false)
    val creada: StateFlow<Boolean> = _creada.asStateFlow()

    private var guardando = false

    // ---------- Acciones ----------
    fun elegirMascota(id: Int) { _mascotaId.value = id }

    fun elegirServicio(id: Int) { _servicioId.value = id }

    /** Si la fecha cambia, se limpia la hora elegida */
    fun elegirFecha(f: LocalDate) {
        _fecha.value = f
        _hora.value = null
    }

    fun elegirHora(h: String) {
        if (horaPasada(h) || h in horasOcupadas.value) return
        _hora.value = h
    }

    /** Si la fecha es hoy, las horas que ya pasaron no se pueden elegir */
    private fun horaPasada(h: String): Boolean =
        _fecha.value == LocalDate.now() && !LocalTime.parse(h).isAfter(LocalTime.now())

    fun limpiarMensaje() { _mensaje.value = null }

    /** Valida todo y crea la cita en estado PENDIENTE. */
    fun confirmar() {
        if (guardando) return

        val mId = _mascotaId.value
        val sId = _servicioId.value
        val hora = _hora.value
        val fecha = _fecha.value

        if (mId == null || sId == null || hora == null) {
            _mensaje.value = "Elige mascota, servicio, fecha y hora"
            return
        }
        val hoy = LocalDate.now()
        if (fecha.isBefore(hoy) || fecha.isAfter(hoy.plusDays(7))) {
            _mensaje.value = "La fecha debe estar entre hoy y los próximos 7 días"
            return
        }
        if (mascotas.value.none { it.id == mId }) {
            _mensaje.value = "Esa mascota no es tuya"
            return
        }
        if (servicios.value.none { it.id == sId }) {
            _mensaje.value = "Ese servicio ya no está disponible"
            return
        }
        if (horaPasada(hora)) {
            _hora.value = null
            _mensaje.value = "Ese horario ya pasó. Elige otro"
            return
        }

        guardando = true
        viewModelScope.launch {
            // Vuelve a consultar si el horario sigue libre antes de crear
            val ocupadas = repo.observarHorasOcupadas(fecha.toString()).first()
            if (hora in ocupadas) {
                _hora.value = null
                _mensaje.value = "Ese horario acaba de ocuparse. Elige otro"
            } else {
                repo.crearCita(mId, sId, fecha.toString(), hora, "")
                _creada.value = true
            }
            guardando = false
        }
    }
}