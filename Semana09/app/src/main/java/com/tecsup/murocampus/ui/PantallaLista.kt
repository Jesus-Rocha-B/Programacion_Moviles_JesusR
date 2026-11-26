package com.tecsup.murocampus.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.murocampus.viewmodel.PostsViewModel
import com.tecsup.murocampus.viewmodel.UiState

@Composable
fun PantallaLista(
    vm: PostsViewModel,
    modifier: Modifier = Modifier,
    onPostClick: (Int) -> Unit
) {
    val estado by vm.lista.collectAsStateWithLifecycle()

    when (val actual = estado) {
        UiState.Cargando -> Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Cargando()
        }

        is UiState.Error -> Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            ErrorConReintento(mensaje = actual.mensaje, onReintentar = vm::cargar)
        }

        is UiState.Exito -> LazyColumn(
            modifier = modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(actual.datos, key = { it.id }) { post ->
                TarjetaPost(
                    userId = post.userId,
                    titulo = post.title,
                    cuerpo = post.body,
                    onClick = { onPostClick(post.id) }
                )
            }
        }
    }
}
