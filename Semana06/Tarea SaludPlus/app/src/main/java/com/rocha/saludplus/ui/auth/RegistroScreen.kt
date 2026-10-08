package com.rocha.saludplus.ui.auth

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.rocha.saludplus.model.Usuario
import com.rocha.saludplus.navigation.Rutas
import com.rocha.saludplus.repository.Repositorio
import com.rocha.saludplus.ui.components.BarraSuperior
import com.rocha.saludplus.ui.components.BotonPrincipal
import com.rocha.saludplus.ui.components.CampoTexto

@Composable
fun RegistroScreen(navController: NavController) {
    var nombre by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var intento by remember { mutableStateOf(false) }
    var mensaje by remember { mutableStateOf("") }

    val telefonoValido = (telefono.length == 9) && telefono.all { it.isDigit() }
    val correoValido = correo.contains("@") && correo.contains(".")
    val contrasenaValida = contrasena.length >= 6

    Scaffold(
        topBar = { BarraSuperior("Crear cuenta", onVolver = { navController.popBackStack() }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Crear cuenta",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0F1E36)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Registra tus datos para continuar",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF758A99)
            )

            Spacer(modifier = Modifier.height(24.dp))

            CampoTexto(
                valor = nombre,
                onValorCambia = { nombre = it },
                etiqueta = "Nombre completo",
                error = if (intento && nombre.isBlank()) "Ingresa tu nombre" else null,
                iconoLeading = Icons.Default.Person
            )

            Spacer(modifier = Modifier.height(8.dp))

            CampoTexto(
                valor = telefono,
                onValorCambia = { telefono = it },
                etiqueta = "Teléfono",
                error = if (intento && !telefonoValido) "Debe tener 9 dígitos" else null,
                tipoTeclado = KeyboardType.Phone,
                iconoLeading = Icons.Default.Phone
            )

            Spacer(modifier = Modifier.height(8.dp))

            CampoTexto(
                valor = correo,
                onValorCambia = { correo = it },
                etiqueta = "Correo personal",
                error = if (intento && !correoValido) "Ingresa un correo válido" else null,
                tipoTeclado = KeyboardType.Email,
                iconoLeading = Icons.Default.Email
            )

            Spacer(modifier = Modifier.height(8.dp))

            CampoTexto(
                valor = contrasena,
                onValorCambia = { contrasena = it },
                etiqueta = "Contraseña",
                error = if (intento && !contrasenaValida) "Mínimo 6 caracteres" else null,
                esContrasena = true,
                iconoLeading = Icons.Default.Lock
            )

            if (mensaje.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = mensaje, color = MaterialTheme.colorScheme.error)
            }

            Spacer(modifier = Modifier.height(16.dp))

            BotonPrincipal(
                texto = "Registrarme",
                onClick = {
                    intento = true
                    if (nombre.isNotBlank() && telefonoValido && correoValido && contrasenaValida) {
                        val usuario = Usuario(nombre.trim(), telefono, correo.trim(), contrasena)
                        if (Repositorio.registrarUsuario(usuario)) {
                            Repositorio.iniciarSesion(usuario.correo, usuario.contrasena)
                            navController.navigate(Rutas.HOME) {
                                popUpTo(Rutas.SPLASH) { inclusive = true }
                            }
                        } else {
                            mensaje = "Ese correo ya está registrado"
                        }
                    }
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            TextButton(onClick = { navController.navigate(Rutas.TERMINOS) }) {
                Text(
                    text = "Al registrarte aceptas nuestros Términos y Condiciones",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF1877F2),
                    textAlign = TextAlign.Center
                )
            }

            TextButton(onClick = { navController.navigate(Rutas.LOGIN) }) {
                Text(
                    text = "¿Ya tienes cuenta? Iniciar sesión",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1877F2)
                )
            }
        }
    }
}
