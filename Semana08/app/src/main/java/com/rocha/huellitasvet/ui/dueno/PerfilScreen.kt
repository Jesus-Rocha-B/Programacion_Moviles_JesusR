package com.rocha.huellitasvet.ui.dueno

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.rocha.huellitasvet.data.local.entity.Usuario
import com.rocha.huellitasvet.ui.components.FilaSwitch
import com.rocha.huellitasvet.ui.components.TarjetaUsuario

@Composable
fun PerfilScreen(
    usuario: Usuario,
    mascotas: Int,
    citasProximas: Int,
    atenciones: Int,
    modoOscuro: Boolean,
    recordatorios: Boolean,
    onModoOscuro: (Boolean) -> Unit,
    onRecordatorios: (Boolean) -> Unit,
    onCambiarPerfil: () -> Unit,
    onCerrarSesion: () -> Unit
) {
    val colores = MaterialTheme.colorScheme

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        Text(
            "Perfil",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(16.dp))

        TarjetaUsuario(nombre = usuario.nombre, correo = usuario.correo, etiqueta = "Dueño")

        Spacer(Modifier.height(16.dp))

        // Contadores
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Contador(mascotas, "Mascotas", Modifier.weight(1f))
            Contador(citasProximas, "Citas próximas", Modifier.weight(1f))
            Contador(atenciones, "Atenciones", Modifier.weight(1f))
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
                    titulo = "Recordatorios",
                    descripcion = "Avisar de mis próximas citas",
                    checked = recordatorios,
                    onChange = onRecordatorios
                )
            }
        }

        Spacer(Modifier.height(20.dp))

        OutlinedButton(
            onClick = onCambiarPerfil,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(14.dp)
        ) {
            Icon(Icons.Filled.SwapHoriz, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text("Cambiar de perfil (ir a Veterinario)")
        }

        Spacer(Modifier.height(10.dp))

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

@Composable
private fun Contador(valor: Int, etiqueta: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                valor.toString(),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(etiqueta, style = MaterialTheme.typography.labelSmall)
        }
    }
}