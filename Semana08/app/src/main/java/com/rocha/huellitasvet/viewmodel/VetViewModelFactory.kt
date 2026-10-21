package com.rocha.huellitasvet.viewmodel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rocha.huellitasvet.data.prefs.PreferenciasStore
import com.rocha.huellitasvet.data.repository.VetRepository

class VetViewModelFactory(
    private val repo: VetRepository,
    private val prefs: PreferenciasStore,
    private val duenoId: Int = 0
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
            PedirCitaViewModel(repo, duenoId) as T
        modelClass.isAssignableFrom(MisCitasViewModel::class.java) ->
            MisCitasViewModel(repo, duenoId) as T
        // Aquí se irán agregando los demás ViewModels en los siguientes pasos
        else -> throw IllegalArgumentException("ViewModel desconocido: ${modelClass.name}")
    }
}