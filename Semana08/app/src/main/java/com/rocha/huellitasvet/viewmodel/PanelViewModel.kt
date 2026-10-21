package com.rocha.huellitasvet.viewmodel
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rocha.huellitasvet.data.repository.VetRepository
import com.rocha.huellitasvet.model.EstadoCita
import com.rocha.huellitasvet.ui.components.colorDeEstado
import com.rocha.huellitasvet.ui.components.graficos.DatoGrafico
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDate

class PanelViewModel(repo: VetRepository) : ViewModel() {

    private val hoy = LocalDate.now().toString()
    // Día 1 del mes actual: desde ahí se suman los ingresos
    private val inicioMes = LocalDate.now().withDayOfMonth(1).toString()

    private fun <T> Flow<T>.estado(inicial: T): StateFlow<T> =
        stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), inicial)

    // Contadores
    val citasHoy: StateFlow<Int> = repo.observarCitasDeFecha(hoy).estado(0)

    val pendientes: StateFlow<Int> =
        repo.observarCantidadPorEstado(EstadoCita.PENDIENTE).estado(0)

    val atendidas: StateFlow<Int> =
        repo.observarCantidadPorEstado(EstadoCita.ATENDIDA).estado(0)

    // Ingresos del mes (suma del precio de las ATENDIDAS)
    val ingresosMes: StateFlow<Double> = repo.observarIngresosDesde(inicioMes).estado(0.0)

    // Gráficos: se convierten los datos de Room a DatoGrafico
    val porEstado: StateFlow<List<DatoGrafico>> = combine(
        repo.observarCantidadPorEstado(EstadoCita.PENDIENTE),
        repo.observarCantidadPorEstado(EstadoCita.CONFIRMADA),
        repo.observarCantidadPorEstado(EstadoCita.ATENDIDA),
        repo.observarCantidadPorEstado(EstadoCita.CANCELADA)
    ) { pendiente, confirmada, atendida, cancelada ->
        listOf(
            DatoGrafico("Pendiente", pendiente.toFloat(), colorDeEstado(EstadoCita.PENDIENTE.name)),
            DatoGrafico("Confirmada", confirmada.toFloat(), colorDeEstado(EstadoCita.CONFIRMADA.name)),
            DatoGrafico("Atendida", atendida.toFloat(), colorDeEstado(EstadoCita.ATENDIDA.name)),
            DatoGrafico("Cancelada", cancelada.toFloat(), colorDeEstado(EstadoCita.CANCELADA.name))
        )
    }.estado(emptyList())

    private val coloresServicio = listOf(
        Color(0xFF1D4ED8), Color(0xFF60A5FA), Color(0xFF93C5FD), Color(0xFFBFDBFE)
    )

    val porServicio: StateFlow<List<DatoGrafico>> = repo.observarCitasPorServicio()
        .map { lista ->
            lista.mapIndexed { i, c ->
                DatoGrafico(
                    etiqueta = c.nombre,
                    valor = c.total.toFloat(),
                    color = coloresServicio.getOrElse(i) { coloresServicio.last() }
                )
            }
        }
        .estado(emptyList())
}