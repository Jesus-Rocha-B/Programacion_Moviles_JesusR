package com.rocha.huellitasvet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rocha.huellitasvet.data.local.relation.MascotaConDueno
import com.rocha.huellitasvet.data.repository.VetRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class PacientesViewModel(repo: VetRepository) : ViewModel() {
    private val _busqueda = MutableStateFlow("")
    val busqueda: StateFlow<String> = _busqueda.asStateFlow()

    /** Conjunto de especies activas ("TODOS", "PERRO", "GATO", "OTRO") */
    private val _filtros = MutableStateFlow(setOf("TODOS"))
    val filtros: StateFlow<Set<String>> = _filtros.asStateFlow()

    /** Mascotas con su dueño, filtradas por texto y combinación de especies seleccionadas */
    val pacientes: StateFlow<List<MascotaConDueno>> = combine(
        repo.observarTodasLasMascotas(),
        _busqueda,
        _filtros
    ) { lista, texto, conjuntoEspecies ->
        val q = texto.trim().lowercase()
        lista.filter { p ->
            val eUpper = p.mascota.especie.uppercase()
            val coincideEspecie = conjuntoEspecies.contains("TODOS") ||
                    (eUpper == "PERRO" && conjuntoEspecies.contains("PERRO")) ||
                    (eUpper == "GATO" && conjuntoEspecies.contains("GATO")) ||
                    (eUpper != "PERRO" && eUpper != "GATO" && conjuntoEspecies.contains("OTRO"))

            val coincideTexto = q.isEmpty() ||
                    p.mascota.nombre.lowercase().contains(q) ||
                    p.dueno.nombre.lowercase().contains(q) ||
                    p.mascota.raza.lowercase().contains(q)

            coincideEspecie && coincideTexto
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    /** mascotaId -> fecha de su última atención */
    val ultimasVisitas: StateFlow<Map<Int, String>> = repo.observarUltimasVisitas()
        .map { lista -> lista.associate { it.mascotaId to it.fecha } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyMap())

    fun buscar(texto: String) {
        _busqueda.value = texto
    }

    fun toggleFiltro(clave: String) {
        if (clave == "TODOS") {
            _filtros.value = setOf("TODOS")
            return
        }

        val nuevos = _filtros.value.toMutableSet()
        nuevos.remove("TODOS")

        if (nuevos.contains(clave)) {
            nuevos.remove(clave)
        } else {
            nuevos.add(clave)
        }

        if (nuevos.isEmpty() || nuevos.containsAll(setOf("PERRO", "GATO", "OTRO"))) {
            _filtros.value = setOf("TODOS")
        } else {
            _filtros.value = nuevos
        }
    }
}
