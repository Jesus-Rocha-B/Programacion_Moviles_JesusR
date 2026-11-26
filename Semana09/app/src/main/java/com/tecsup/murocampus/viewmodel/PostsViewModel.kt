package com.tecsup.murocampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.murocampus.data.PostRepository
import com.tecsup.murocampus.model.Comentario
import com.tecsup.murocampus.model.NuevoPost
import com.tecsup.murocampus.model.Post
import kotlinx.coroutines.async
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class DetalleUi(val post: Post, val comentarios: List<Comentario>)

class PostsViewModel(private val repo: PostRepository) : ViewModel() {
    private val _lista = MutableStateFlow<UiState<List<Post>>>(UiState.Cargando)
    val lista = _lista.asStateFlow()
    private val _busqueda = MutableStateFlow("")
    val busqueda = _busqueda.asStateFlow()
    val listaVisible: StateFlow<UiState<List<Post>>> = combine(_lista, _busqueda) { estado, texto ->
        if (estado is UiState.Exito) {
            UiState.Exito(estado.datos.filter { it.title.contains(texto.trim(), ignoreCase = true) })
        } else {
            estado
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), UiState.Cargando)
    private val _usuarioFiltro = MutableStateFlow<Int?>(null)
    val usuarioFiltro = _usuarioFiltro.asStateFlow()
    private val _detalle = MutableStateFlow<UiState<DetalleUi>>(UiState.Cargando)
    val detalle = _detalle.asStateFlow()
    private val _publicacion = MutableStateFlow<UiState<Post>?>(null)
    val publicacion = _publicacion.asStateFlow()
    private val _eliminacion = MutableStateFlow<UiState<Unit>?>(null)
    val eliminacion = _eliminacion.asStateFlow()
    private var cargaJob: Job? = null

    init {
        cargar()
    }

    fun cargar() {
        cargaJob?.cancel()
        cargaJob = viewModelScope.launch {
            _lista.value = UiState.Cargando
            val resultado = _usuarioFiltro.value?.let { repo.porUsuario(it) } ?: repo.listar()
            resultado
                .onSuccess { _lista.value = UiState.Exito(it) }
                .onFailure { _lista.value = UiState.Error(mensajeDe(it)) }
        }
    }

    fun filtrar(userId: Int?) {
        _usuarioFiltro.value = userId
        cargar()
    }

    fun buscar(texto: String) {
        _busqueda.value = texto
    }

    fun abrir(id: Int) {
        _eliminacion.value = null
        viewModelScope.launch {
            _detalle.value = UiState.Cargando
            val (postResultado, comentariosResultado) = coroutineScope {
                val post = async { repo.detalle(id) }
                val comentarios = async { repo.comentarios(id) }
                post.await() to comentarios.await()
            }
            val post = postResultado.getOrElse {
                _detalle.value = UiState.Error(mensajeDe(it))
                return@launch
            }
            val comentarios = comentariosResultado.getOrElse {
                _detalle.value = UiState.Error(mensajeDe(it))
                return@launch
            }
            _detalle.value = UiState.Exito(DetalleUi(post, comentarios))
        }
    }

    fun eliminar(id: Int) {
        viewModelScope.launch {
            _eliminacion.value = UiState.Cargando
            repo.eliminar(id)
                .onSuccess {
                    val actual = _lista.value
                    if (actual is UiState.Exito) {
                        _lista.value = UiState.Exito(actual.datos.filterNot { it.id == id })
                    }
                    _eliminacion.value = UiState.Exito(Unit)
                }
                .onFailure { _eliminacion.value = UiState.Error(mensajeDe(it)) }
        }
    }

    fun limpiarPublicacion() {
        _publicacion.value = null
    }

    fun publicar(titulo: String, texto: String) {
        if (titulo.isBlank() || texto.isBlank()) return
        viewModelScope.launch {
            _publicacion.value = UiState.Cargando
            repo.crear(NuevoPost(userId = 1, title = titulo, body = texto))
                .onSuccess { _publicacion.value = UiState.Exito(it) }
                .onFailure { _publicacion.value = UiState.Error(mensajeDe(it)) }
        }
    }
}
