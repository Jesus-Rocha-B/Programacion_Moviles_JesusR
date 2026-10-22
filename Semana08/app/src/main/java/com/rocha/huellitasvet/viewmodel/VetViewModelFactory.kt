package com.rocha.huellitasvet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rocha.huellitasvet.data.prefs.PreferenciasStore
import com.rocha.huellitasvet.data.repository.VetRepository

class VetViewModelFactory(
    private val repo: VetRepository,
    private val prefs: PreferenciasStore,
    private val duenoId: Int = 0,
    private val citaId: Int = 0
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = when {
        modelClass.isAssignableFrom(SesionViewModel::class.java) ->
            SesionViewModel(repo, prefs) as T
        modelClass.isAssignableFrom(InicioViewModel::class.java) ->
            InicioViewModel(repo, duenoId) as T
        modelClass.isAssignableFrom(MascotaViewModel::class.java) ->
            MascotaViewModel(repo, duenoId) as T
        modelClass.isAssignableFrom(PedirCitaViewModel::class.java) ->
            PedirCitaViewModel(repo, prefs, duenoId) as T
        modelClass.isAssignableFrom(MisCitasViewModel::class.java) ->
            MisCitasViewModel(repo, duenoId) as T
        modelClass.isAssignableFrom(PanelViewModel::class.java) ->
            PanelViewModel(repo) as T
        modelClass.isAssignableFrom(AgendaViewModel::class.java) ->
            AgendaViewModel(repo) as T
        modelClass.isAssignableFrom(AtenderCitaViewModel::class.java) ->
            AtenderCitaViewModel(repo, citaId) as T
        modelClass.isAssignableFrom(PacientesViewModel::class.java) ->
            PacientesViewModel(repo) as T
        modelClass.isAssignableFrom(ServiciosViewModel::class.java) ->
            ServiciosViewModel(repo) as T
        else -> throw IllegalArgumentException("ViewModel desconocido: ${modelClass.name}")
    }
}