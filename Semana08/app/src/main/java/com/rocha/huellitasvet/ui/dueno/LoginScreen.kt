package com.rocha.huellitasvet.ui.dueno

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.rocha.huellitasvet.model.Rol
import com.rocha.huellitasvet.ui.components.VetButton
import com.rocha.huellitasvet.ui.components.VetTextField
import com.rocha.huellitasvet.ui.theme.VetTheme
import com.rocha.huellitasvet.viewmodel.SesionViewModel

// Un solo Composable usado por ambos perfiles
@Composable
fun LoginScreen(vm: SesionViewModel, onLoginExitoso: (Rol) -> Unit) {
    var rol by remember { mutableStateOf(Rol.CLIENTE) }
    var correo by remember { mutableStateOf("") }
    var clave by remember { mutableStateOf("") }

    val error by vm.error.collectAsState()
    val usuario by vm.usuarioActual.collectAsState()
    val oscuro by vm.modoOscuro.collectAsState()

    // Cuando el login es correcto, navega según el rol
    LaunchedEffect(usuario) {
        usuario?.let { onLoginExitoso(Rol.valueOf(it.rol)) }
    }

    // El tema cambia de color según la pestaña elegida
    VetTheme(rol = rol, oscuro = oscuro) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "HuellitasVet",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Inicia sesión para continuar",
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(Modifier.height(24.dp))

                // Selector Dueño / Veterinario
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(
                        selected = rol == Rol.CLIENTE,
                        onClick = { rol = Rol.CLIENTE; vm.limpiarError() },
                        label = { Text("Dueño") }
                    )
                    FilterChip(
                        selected = rol == Rol.ADMIN,
                        onClick = { rol = Rol.ADMIN; vm.limpiarError() },
                        label = { Text("Veterinario") }
                    )
                }

                Spacer(Modifier.height(20.dp))

                VetTextField(
                    value = correo,
                    onValueChange = { correo = it; vm.limpiarError() },
                    label = "Correo",
                    keyboardType = KeyboardType.Email
                )
                Spacer(Modifier.height(8.dp))
                VetTextField(
                    value = clave,
                    onValueChange = { clave = it; vm.limpiarError() },
                    label = "Contraseña",
                    esClave = true
                )

                error?.let {
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Spacer(Modifier.height(20.dp))

                VetButton(
                    texto = "Ingresar",
                    onClick = { vm.login(correo, clave, rol) }
                )
            }
        }
    }
}