package com.rocha.saludplus.ui.auth
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.rocha.saludplus.model.Usuario
import com.rocha.saludplus.navigation.Rutas
import com.rocha.saludplus.repository.Repositorio
import com.rocha.saludplus.ui.components.*

@Composable
fun RegistroScreen(navController: NavController) {
    // Estado de cada campo, remember lo conserva al recomponer
    var nombre by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    // Los errores solo se muestran después de presionar el botón
    var intento by remember { mutableStateOf(false) }
    var mensaje by remember { mutableStateOf("") }

    // Validaciones
    val telefonoValido = telefono.length == 9 && telefono.all { it.isDigit() }
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
            CampoTexto(
                valor = nombre,
                onValorCambia = { nombre = it },
                etiqueta = "Nombre completo",
                error = if (intento && nombre.isBlank()) "Ingresa tu nombre" else null
            )
            Spacer(modifier = Modifier.height(8.dp))
            CampoTexto(
                valor = telefono,
                onValorCambia = { telefono = it },
                etiqueta = "Teléfono",
                error = if (intento && !telefonoValido) "Debe tener 9 dígitos" else null,
                tipoTeclado = KeyboardType.Phone
            )
            Spacer(modifier = Modifier.height(8.dp))
            CampoTexto(
                valor = correo,
                onValorCambia = { correo = it },
                etiqueta = "Correo electrónico",
                error = if (intento && !correoValido) "Ingresa un correo válido" else null,
                tipoTeclado = KeyboardType.Email
            )
            Spacer(modifier = Modifier.height(8.dp))
            CampoTexto(
                valor = contrasena,
                onValorCambia = { contrasena = it },
                etiqueta = "Contraseña",
                error = if (intento && !contrasenaValida) "Mínimo 6 caracteres" else null,
                esContrasena = true
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
                            // Deja la sesión iniciada y entra al Inicio
                            Repositorio.iniciarSesion(usuario.correo, usuario.contrasena)
                            navController.navigate(Rutas.HOME) {
                                // Borra el Splash y el registro del historial
                                popUpTo(Rutas.SPLASH) { inclusive = true }
                            }
                        } else {
                            mensaje = "Ese correo ya está registrado"
                        }
                    }
                }
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextButton(onClick = { navController.navigate(Rutas.TERMINOS) }) {
                Text("Términos y condiciones")
            }
            TextButton(onClick = { navController.navigate(Rutas.LOGIN) }) {
                Text("¿Ya tienes cuenta? Iniciar sesión")
            }
        }
    }
}