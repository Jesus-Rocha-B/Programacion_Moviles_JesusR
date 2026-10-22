package com.rocha.huellitasvet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rocha.huellitasvet.data.local.entity.Usuario
import com.rocha.huellitasvet.data.prefs.PreferenciasStore
import com.rocha.huellitasvet.data.repository.VetRepository
import com.rocha.huellitasvet.model.Rol
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SesionViewModel(
    private val repo: VetRepository,
    private val prefs: PreferenciasStore
) : ViewModel() {

    private val _usuarioActual = MutableStateFlow<Usuario?>(null)
    val usuarioActual: StateFlow<Usuario?> = _usuarioActual.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    // ---------- Preferencias (DataStore) ----------
    val modoOscuro: StateFlow<Boolean> = prefs.modoOscuro
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    val notificaciones: StateFlow<Boolean> = prefs.notificaciones
        .stateIn(viewModelScope, SharingStarted.Eagerly, true)

    val aceptarCitas: StateFlow<Boolean> = prefs.aceptarCitas
        .stateIn(viewModelScope, SharingStarted.Eagerly, true)

    fun cambiarModoOscuro(valor: Boolean) {
        viewModelScope.launch { prefs.guardarModoOscuro(valor) }
    }

    fun cambiarNotificaciones(valor: Boolean) {
        viewModelScope.launch { prefs.guardarNotificaciones(valor) }
    }

    fun cambiarAceptarCitas(valor: Boolean) {
        viewModelScope.launch { prefs.guardarAceptarCitas(valor) }
    }

    // ---------- Sesión ----------
    fun login(correo: String, clave: String, rolSeleccionado: Rol) {
        if (correo.isBlank() || clave.isBlank()) {
            _error.value = "Completa tu correo y tu contraseña"
            return
        }
        viewModelScope.launch {
            val usuario = repo.login(correo.trim(), clave)
            when {
                usuario == null ->
                    _error.value = "Correo o contraseña incorrectos"

                usuario.rol != rolSeleccionado.name ->
                    _error.value = "Este usuario no pertenece al perfil " +
                            if (rolSeleccionado == Rol.CLIENTE) "Dueño" else "Veterinario"

                else -> {
                    prefs.guardarSesion(usuario.rol, usuario.id)
                    _error.value = null
                    _usuarioActual.value = usuario
                }
            }
        }
    }

    /** Si hay un perfil guardado, restaura el usuario y devuelve su rol; si no, null. */
    suspend fun restaurarSesion(): Rol? {
        val perfil = prefs.perfilActivo.first()
        val id = prefs.usuarioId.first()
        if (perfil.isEmpty() || id == 0) return null
        val usuario = repo.obtenerUsuario(id) ?: return null
        _usuarioActual.value = usuario
        return Rol.valueOf(usuario.rol)
    }

    /**
     * Modo práctica: guarda el otro perfil (Dueño <-> Veterinario) y avisa
     * para que la navegación vaya a su grafo.
     */
    fun cambiarPerfil(onListo: (Rol) -> Unit) {
        val actual = _usuarioActual.value ?: return
        val destino = if (actual.rol == Rol.ADMIN.name) Rol.CLIENTE else Rol.ADMIN

        viewModelScope.launch {
            val otro = repo.obtenerUsuarioPorRol(destino.name)
            if (otro == null) {
                _error.value = "No hay un usuario con ese perfil"
                return@launch
            }
            prefs.guardarSesion(otro.rol, otro.id)
            _usuarioActual.value = otro
            onListo(destino)
        }
    }

    fun limpiarError() {
        _error.value = null
    }

    fun cerrarSesion() {
        viewModelScope.launch {
            prefs.cerrarSesion()
            _usuarioActual.value = null
        }
    }
}