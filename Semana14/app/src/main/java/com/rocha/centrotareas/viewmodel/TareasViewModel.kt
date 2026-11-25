package com.rocha.centrotareas.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rocha.centrotareas.data.TareasRepository
import com.rocha.centrotareas.model.CargaUi
import com.rocha.centrotareas.model.PrimosUi
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TareasViewModel(private val repo: TareasRepository) : ViewModel() {
    // ----- Tarea con progreso (pasos 1 a 4)
    private val _mensaje = MutableStateFlow("Listo")
    val mensaje: StateFlow<String> = _mensaje.asStateFlow()

    // Paso 3: Barra de progreso con StateFlow
    private val _progreso = MutableStateFlow(0f)
    val progreso: StateFlow<Float> = _progreso.asStateFlow()

    // Paso 4: Control de corrutina y cancelación con Job
    private var trabajo: Job? = null
    private val _corriendo = MutableStateFlow(false)
    val corriendo: StateFlow<Boolean> = _corriendo.asStateFlow()

    // Tarea larga que actualiza el progreso en segundo plano y puede cancelarse (Mejora 1: Flow desde Repository)
    fun tareaLarga() {
        if (_corriendo.value) return
        trabajo = viewModelScope.launch {
            _corriendo.value = true
            try {
                _progreso.value = 0f
                _mensaje.value = "Trabajando..."
                repo.ejecutarTareaLarga().collect { valor ->
                    _progreso.value = valor
                }
                _mensaje.value = "Terminó"
            } catch (e: CancellationException) {
                _mensaje.value = "Cancelado"
                throw e
            } finally {
                _corriendo.value = false
            }
        }
    }

    fun cancelar() {
        trabajo?.cancel()
        _mensaje.value = "Cancelado"
    }

    // ----- Contar primos (paso 5)
    private val _primos = MutableStateFlow<PrimosUi>(PrimosUi.Inicial)
    val primos: StateFlow<PrimosUi> = _primos.asStateFlow()
    private var trabajoPrimos: Job? = null

    fun contar(hasta: Int) {
        trabajoPrimos?.cancel()
        trabajoPrimos = viewModelScope.launch {
            _primos.value = PrimosUi.Calculando(0f)
            val inicio = System.currentTimeMillis()
            try {
                val cantidad = repo.contarPrimos(hasta) { progresoActual ->
                    _primos.value = PrimosUi.Calculando(progresoActual)
                }
                val duracion = System.currentTimeMillis() - inicio
                _primos.value = PrimosUi.Listo(cantidad, duracion)
            } catch (e: CancellationException) {
                _primos.value = PrimosUi.Cancelado
                throw e
            }
        }
    }

    fun cancelarPrimos() {
        trabajoPrimos?.cancel()
    }

    // ----- Dos cargas (pasos 6 y 7 + Mejora 2: Reintento automático)
    private val _carga = MutableStateFlow<CargaUi>(CargaUi.Inicial)
    val carga: StateFlow<CargaUi> = _carga.asStateFlow()

    private val _simularError = MutableStateFlow(false)
    val simularError: StateFlow<Boolean> = _simularError.asStateFlow()

    fun cambiarSimularError(valor: Boolean) {
        _simularError.value = valor
    }

    // Mejora 2: Reintento automático de hasta 3 veces con espera creciente
    private suspend fun <T> ejecutarConReintento(
        maxIntentos: Int = 3,
        bloque: suspend () -> T
    ): T {
        var esperaMs = 1000L
        for (intento in 1..maxIntentos) {
            try {
                return bloque()
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                if (intento == maxIntentos) throw e
                _carga.value = CargaUi.Reintentando(intento, maxIntentos)
                delay(esperaMs)
                esperaMs += 1000L
            }
        }
        throw java.io.IOException("Sin conexión")
    }

    fun cargarUnaPorUna() {
        if (_carga.value is CargaUi.Cargando || _carga.value is CargaUi.Reintentando) return
        viewModelScope.launch {
            _carga.value = CargaUi.Cargando
            val inicio = System.currentTimeMillis()
            try {
                val (perfil, pedidos) = ejecutarConReintento {
                    val p = repo.cargarPerfil()
                    val ped = repo.cargarPedidos(_simularError.value)
                    Pair(p, ped)
                }
                val duracion = System.currentTimeMillis() - inicio
                _carga.value = CargaUi.Exito("Una por una", perfil, pedidos.size, duracion)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _carga.value = CargaUi.Error("No se pudo cargar: ${e.message}")
            }
        }
    }

    fun cargarALaVez() {
        if (_carga.value is CargaUi.Cargando || _carga.value is CargaUi.Reintentando) return
        viewModelScope.launch {
            _carga.value = CargaUi.Cargando
            val inicio = System.currentTimeMillis()
            try {
                val (perfil, pedidos) = ejecutarConReintento {
                    coroutineScope {
                        val a = async { repo.cargarPerfil() }
                        val b = async { repo.cargarPedidos(_simularError.value) }
                        Pair(a.await(), b.await())
                    }
                }
                val duracion = System.currentTimeMillis() - inicio
                _carga.value = CargaUi.Exito("A la vez", perfil, pedidos.size, duracion)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _carga.value = CargaUi.Error("No se pudo cargar: ${e.message}")
            }
        }
    }
}
