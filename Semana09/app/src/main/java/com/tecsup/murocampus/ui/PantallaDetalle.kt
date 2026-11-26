package com.tecsup.murocampus.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.murocampus.ui.theme.AzulTecsup
import com.tecsup.murocampus.ui.theme.ErrorRojo
import com.tecsup.murocampus.viewmodel.PostsViewModel
import com.tecsup.murocampus.viewmodel.UiState

@Composable
fun PantallaDetalle(
    vm: PostsViewModel,
    id: Int,
    onVolver: () -> Unit,
    modifier: Modifier = Modifier
) {
    val estado by vm.detalle.collectAsStateWithLifecycle()
    val eliminacion by vm.eliminacion.collectAsStateWithLifecycle()

    when (val actual = estado) {
        UiState.Cargando -> Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Cargando()
        }

        is UiState.Error -> Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            ErrorConReintento(
                mensaje = actual.mensaje,
                onReintentar = { vm.abrir(id) }
            )
        }

        is UiState.Exito -> LazyColumn(
            modifier = modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = actual.datos.post.title,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = actual.datos.post.body,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    when (val resultado = eliminacion) {
                        UiState.Cargando -> CircularProgressIndicator()
                        is UiState.Error -> Text(resultado.mensaje, color = ErrorRojo)
                        is UiState.Exito -> {
                            Text("Eliminación aceptada (simulada por JSONPlaceholder)")
                            Button(onClick = onVolver) { Text("Volver a la lista") }
                        }
                        null -> Unit
                    }
                    if (eliminacion !is UiState.Exito) {
                        Button(
                            onClick = { vm.eliminar(id) },
                            enabled = eliminacion !is UiState.Cargando
                        ) { Text("Eliminar") }
                    }
                }
            }
            item {
                Text(
                    text = "Comentarios (${actual.datos.comentarios.size})",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }
            items(actual.datos.comentarios, key = { it.id }) { comentario ->
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = comentario.email,
                        color = AzulTecsup,
                        style = MaterialTheme.typography.labelLarge
                    )
                    Text(
                        text = comentario.body,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    HorizontalDivider(modifier = Modifier.padding(top = 8.dp))
                }
            }
        }
    }
}
