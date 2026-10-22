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

    /** null = Todos; si no, "PERRO", "GATO" u "OTRO" */
    private val _filtro = MutableStateFlow<String?>(null)
    val filtro: StateFlow<String?> = _filtro.asStateFlow()

    /** Mascotas con su dueño, filtradas por texto (mascota, dueño o raza) y por especie simultáneamente */
    val pacientes: StateFlow<List<MascotaConDueno>> = combine(
        repo.observarTodasLasMascotas(),
        _busqueda,
        _filtro
    ) { lista, texto, especie ->
        val q = texto.trim().lowercase()
        lista.filter { p ->
            val coincideEspecie = especie == null || p.mascota.especie.equals(especie, ignoreCase = true)
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

    fun filtrar(especie: String?) {
        _filtro.value = especie
    }
}