package com.rocha.saludplus.ui.perfil

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.EventAvailable
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.rocha.saludplus.R
import com.rocha.saludplus.navigation.Rutas
import com.rocha.saludplus.repository.Repositorio
import com.rocha.saludplus.ui.components.BarraSuperior
import com.rocha.saludplus.ui.components.BotonPrincipal
import com.rocha.saludplus.ui.components.FilaDato

@Composable
fun PerfilScreen(navController: NavController) {
    // Datos de la sesión actual
    val usuario = Repositorio.usuarioActual
    val totalCitas = Repositorio.citasDelUsuario().size

    Scaffold(
        topBar = { BarraSuperior("Mi perfil", onVolver = { navController.popBackStack() }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            // 1. Fotografía real de perfil de la persona / usuario
            Surface(
                modifier = Modifier.size(90.dp),
                shape = CircleShape,
                color = Color(0xFFE6F2FF),
                border = BorderStroke(2.dp, Color.White),
                shadowElevation = 3.dp
            ) {
                Image(
                    painter = painterResource(id = R.drawable.user_profile),
                    contentDescription = "Foto de perfil de ${usuario?.nombre}",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 2. Nombre y subtítulo
            Text(
                text = usuario?.nombre ?: "Sin sesión",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0F1E36)
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = "Paciente registrado",
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF758A99)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 3. Tarjeta contenedora de datos
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, Color(0xFFEFEFEF)),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    FilaDato(
                        etiqueta = "Teléfono",
                        valor = usuario?.telefono ?: "",
                        icono = Icons.Default.Phone
                    )
                    FilaDato(
                        etiqueta = "Correo",
                        valor = usuario?.correo ?: "",
                        icono = Icons.Default.Email
                    )
                    FilaDato(
                        etiqueta = "Citas agendadas",
                        valor = totalCitas.toString(),
                        icono = Icons.Default.EventAvailable
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // 4. Botón de acción: Cerrar sesión
            BotonPrincipal(
                texto = "Cerrar sesión",
                onClick = {
                    Repositorio.cerrarSesion()
                    navController.navigate(Rutas.SPLASH) {
                        popUpTo(Rutas.HOME) { inclusive = true }
                    }
                }
            )
        }
    }
}
