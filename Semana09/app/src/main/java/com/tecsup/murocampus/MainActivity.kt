package com.tecsup.murocampus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.tecsup.murocampus.data.PostRepositoryRemote
import com.tecsup.murocampus.data.remote.RetrofitClient
import com.tecsup.murocampus.ui.PantallaDetalle
import com.tecsup.murocampus.ui.PantallaCrear
import com.tecsup.murocampus.ui.PantallaLista
import com.tecsup.murocampus.ui.theme.AzulTecsup
import com.tecsup.murocampus.ui.theme.MuroCampusTheme
import com.tecsup.murocampus.viewmodel.PostsViewModel
import com.tecsup.murocampus.viewmodel.PostsViewModelFactory

class MainActivity : ComponentActivity() {
    private val vm: PostsViewModel by viewModels {
        PostsViewModelFactory(PostRepositoryRemote(RetrofitClient.api))
    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var idSeleccionado by rememberSaveable { mutableStateOf<Int?>(null) }
            var creando by rememberSaveable { mutableStateOf(false) }
            BackHandler(enabled = creando || idSeleccionado != null) {
                if (creando) creando = false else idSeleccionado = null
            }
            MuroCampusTheme {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = {
                                Text(
                                    text = "MuroCampus",
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = AzulTecsup
                            )
                        )
                    },
                    floatingActionButton = {
                        if (!creando && idSeleccionado == null) {
                            FloatingActionButton(onClick = {
                                vm.limpiarPublicacion()
                                creando = true
                            }) {
                                Text("+")
                            }
                        }
                    }
                ) { innerPadding ->
                    val modifierPantalla = Modifier.fillMaxSize().padding(innerPadding)
                    val id = idSeleccionado
                    if (creando) {
                        PantallaCrear(vm = vm, modifier = modifierPantalla)
                    } else if (id == null) {
                        PantallaLista(
                            vm = vm,
                            modifier = modifierPantalla,
                            onPostClick = { postId ->
                                idSeleccionado = postId
                                vm.abrir(postId)
                            }
                        )
                    } else {
                        PantallaDetalle(
                            vm = vm,
                            id = id,
                            onVolver = { idSeleccionado = null },
                            modifier = modifierPantalla
                        )
                    }
                }
            }
        }
    }
}
