package com.rocha.huellitasvet.viewmodel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rocha.huellitasvet.data.local.entity.Mascota
import com.rocha.huellitasvet.data.local.relation.CitaConDetalle
import com.rocha.huellitasvet.data.repository.VetRepository
import com.rocha.huellitasvet.model.EstadoCita
import com.rocha.huellitasvet.model.fechaHora
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDateTime

class InicioViewModel(
    repo: VetRepository,
    duenoId: Int
) : ViewModel() {

    /** Solo las mascotas de este dueño */
    val mascotas: StateFlow<List<Mascota>> = repo.observarMascotasDeDueno(duenoId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    /**
     * Próxima cita = la primera PENDIENTE o CONFIRMADA con fecha y hora desde ahora,
     * ordenadas por fecha y hora. Se calcula aquí a partir de observarCitasDeDueno.
     */
    val proximaCita: StateFlow<CitaConDetalle?> = repo.observarCitasDeDueno(duenoId)
        .map { citas ->
            val ahora = LocalDateTime.now()
            citas
                .filter {
                    it.cita.estado == EstadoCita.PENDIENTE.name ||
                            it.cita.estado == EstadoCita.CONFIRMADA.name
                }
                .filter { !it.fechaHora().isBefore(ahora) }
                .minByOrNull { it.fechaHora() }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    /** Recordatorios de próximos controles fijados por el veterinario */
    val proximosControles: StateFlow<List<CitaConDetalle>> = repo.observarProximosControles(duenoId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}