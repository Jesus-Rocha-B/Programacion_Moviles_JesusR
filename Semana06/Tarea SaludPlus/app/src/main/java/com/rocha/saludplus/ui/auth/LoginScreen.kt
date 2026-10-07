package com.rocha.saludplus.ui.auth
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.rocha.saludplus.navigation.Rutas
import com.rocha.saludplus.repository.Repositorio
import com.rocha.saludplus.ui.components.*

@Composable
fun LoginScreen(navController: NavController) {
    var correo by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var intento by remember { mutableStateOf(false) }
    var mensaje by remember { mutableStateOf("") }

    Scaffold(
        topBar = { BarraSuperior("Iniciar sesión", onVolver = { navController.popBackStack() }) }
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding).padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Bienvenido de nuevo",
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.height(24.dp))
            CampoTexto(
                valor = correo,
                onValorCambia = { correo = it },
                etiqueta = "Correo electrónico",
                error = if (intento && correo.isBlank()) "Ingresa tu correo" else null,
                tipoTeclado = KeyboardType.Email
            )
            Spacer(modifier = Modifier.height(8.dp))
            CampoTexto(
                valor = contrasena,
                onValorCambia = { contrasena = it },
                etiqueta = "Contraseña",
                error = if (intento && contrasena.isBlank()) "Ingresa tu contraseña" else null,
                esContrasena = true
            )
            if (mensaje.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = mensaje, color = MaterialTheme.colorScheme.error)
            }
            Spacer(modifier = Modifier.height(16.dp))
            BotonPrincipal(
                texto = "Iniciar sesión",
                onClick = {
                    intento = true
                    if (correo.isNotBlank() && contrasena.isNotBlank()) {
                        // Busca el usuario en la lista del Repositorio
                        if (Repositorio.iniciarSesion(correo.trim(), contrasena)) {
                            navController.navigate(Rutas.HOME) {
                                popUpTo(Rutas.SPLASH) { inclusive = true }
                            }
                        } else {
                            mensaje = "Correo o contraseña incorrectos"
                        }
                    }
                }
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextButton(onClick = { navController.navigate(Rutas.REGISTRO) }) {
                Text("¿No tienes cuenta? Regístrate")
            }
        }
    }
}