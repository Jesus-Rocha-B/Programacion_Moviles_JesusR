package com.tecsup.murocampus.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.murocampus.ui.theme.ErrorRojo
import com.tecsup.murocampus.viewmodel.PostsViewModel
import com.tecsup.murocampus.viewmodel.UiState

@Composable
fun PantallaCrear(vm: PostsViewModel, modifier: Modifier = Modifier) {
    var titulo by rememberSaveable { mutableStateOf("") }
    var texto by rememberSaveable { mutableStateOf("") }
    val estado by vm.publicacion.collectAsStateWithLifecycle()

    LaunchedEffect(estado) {
        if (estado is UiState.Exito) {
            titulo = ""
            texto = ""
        }
    }

    Column(
        modifier = modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Crear publicación", style = MaterialTheme.typography.headlineSmall)
        OutlinedTextField(
            value = titulo,
            onValueChange = { titulo = it },
            label = { Text("Título") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        OutlinedTextField(
            value = texto,
            onValueChange = { texto = it },
            label = { Text("Contenido") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 4
        )
        Button(
            onClick = { vm.publicar(titulo, texto) },
            enabled = titulo.isNotBlank() && texto.isNotBlank() && estado !is UiState.Cargando
        ) {
            Text("Publicar")
        }

        when (val actual = estado) {
            UiState.Cargando -> CircularProgressIndicator()
            is UiState.Exito -> Text("Publicado con id ${actual.datos.id}")
            is UiState.Error -> Text(actual.mensaje, color = ErrorRojo)
            null -> Unit
        }
    }
}
