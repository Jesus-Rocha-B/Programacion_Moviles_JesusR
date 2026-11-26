package com.tecsup.murocampus.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.murocampus.ui.theme.AzulTecsup
import com.tecsup.murocampus.ui.theme.TextoOscuro
import com.tecsup.murocampus.viewmodel.PostsViewModel
import com.tecsup.murocampus.viewmodel.UiState

@Composable
fun PantallaLista(
    vm: PostsViewModel,
    modifier: Modifier = Modifier,
    onPostClick: (Int) -> Unit
) {
    val estado by vm.lista.collectAsStateWithLifecycle()
    val usuarioFiltro by vm.usuarioFiltro.collectAsStateWithLifecycle()
    val coloresChip = FilterChipDefaults.filterChipColors(
        containerColor = Color.White,
        labelColor = TextoOscuro,
        selectedContainerColor = Color(0xFFBFE9FF),
        selectedLabelColor = AzulTecsup
    )

    Column(modifier = modifier.fillMaxSize()) {
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                FilterChip(
                    selected = usuarioFiltro == null,
                    onClick = { vm.filtrar(null) },
                    colors = coloresChip,
                    label = { Text("Todos") }
                )
            }
            items((1..10).toList()) { userId ->
                FilterChip(
                    selected = usuarioFiltro == userId,
                    onClick = { vm.filtrar(userId) },
                    colors = coloresChip,
                    label = { Text("Usuario $userId") }
                )
            }
        }

        when (val actual = estado) {
            UiState.Cargando -> Box(Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                Cargando()
            }

            is UiState.Error -> Box(Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                ErrorConReintento(mensaje = actual.mensaje, onReintentar = vm::cargar)
            }

            is UiState.Exito -> if (actual.datos.isEmpty()) {
                Box(Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Text("Sin publicaciones")
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f).fillMaxWidth(),
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
    }
}
