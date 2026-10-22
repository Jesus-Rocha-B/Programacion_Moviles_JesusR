package com.rocha.huellitasvet.ui.veterinario

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.rocha.huellitasvet.data.local.entity.Usuario
import com.rocha.huellitasvet.ui.components.AppTopBar
import com.rocha.huellitasvet.ui.components.FilaSwitch
import com.rocha.huellitasvet.ui.components.TarjetaUsuario

@Composable
fun AjustesScreen(
    usuario: Usuario,
    modoOscuro: Boolean,
    notificaciones: Boolean,
    aceptarCitas: Boolean,
    onModoOscuro: (Boolean) -> Unit,
    onNotificaciones: (Boolean) -> Unit,
    onAceptarCitas: (Boolean) -> Unit,
    onCambiarPerfil: () -> Unit,
    onCerrarSesion: () -> Unit
) {
    val colores = MaterialTheme.colorScheme

    Scaffold(
        topBar = { AppTopBar(titulo = "Ajustes") },
        containerColor = colores.background,
        contentWindowInsets = WindowInsets(0)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {
            TarjetaUsuario(nombre = usuario.nombre, correo = usuario.correo, etiqueta = "Perfil activo")

            Spacer(Modifier.height(16.dp))

            // Cambiar de perfil
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onCambiarPerfil() },
                colors = CardDefaults.cardColors(containerColor = colores.primary),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Filled.SwapHoriz, contentDescription = null, tint = colores.onPrimary)
                    Spacer(Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            "Cambiar de perfil",
                            color = colores.onPrimary,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "Modo práctica: ir a la vista Dueño",
                            color = colores.onPrimary,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    Icon(
                        Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = null,
                        tint = colores.onPrimary
                    )
                }
            }

            Spacer(Modifier.height(20.dp))

            Text("Preferencias", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = colores.surface),
                elevation = CardDefaults.cardElevation(2.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                    FilaSwitch(
                        titulo = "Modo oscuro",
                        descripcion = "Cambia el tema de la app",
                        checked = modoOscuro,
                        onChange = onModoOscuro
                    )
                    FilaSwitch(
                        titulo = "Notificaciones",
                        descripcion = "Avisar de citas nuevas",
                        checked = notificaciones,
                        onChange = onNotificaciones
                    )
                    FilaSwitch(
                        titulo = "Aceptar citas",
                        descripcion = "Abrir o cerrar la agenda",
                        checked = aceptarCitas,
                        onChange = onAceptarCitas
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = colores.primaryContainer),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    "Se guardan en el dispositivo con DataStore",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(12.dp)
                )
            }

            Spacer(Modifier.height(24.dp))

            OutlinedButton(
                onClick = onCerrarSesion,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(14.dp)
            ) {
                Text("Cerrar sesión", color = colores.error)
            }
        }
    }
}