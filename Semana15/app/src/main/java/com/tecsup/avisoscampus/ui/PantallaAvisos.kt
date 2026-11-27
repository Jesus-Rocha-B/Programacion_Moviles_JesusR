package com.tecsup.avisoscampus.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tecsup.avisoscampus.model.PermisoUi
import com.tecsup.avisoscampus.notificacion.Notificador
import com.tecsup.avisoscampus.ui.theme.AzulTecsup
import com.tecsup.avisoscampus.ui.theme.FondoGrisClaro
import com.tecsup.avisoscampus.ui.theme.GrisBorde
import com.tecsup.avisoscampus.ui.theme.RojoDenegado
import com.tecsup.avisoscampus.ui.theme.TextoOscuro
import com.tecsup.avisoscampus.ui.theme.TextoSecundario
import com.tecsup.avisoscampus.ui.theme.VerdePermitido
import com.tecsup.avisoscampus.viewmodel.AvisosViewModel

// [G] GUIADO: completar TODOs en los pasos 2 a 8
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaAvisos(vm: AvisosViewModel) {
    val contexto = LocalContext.current
    val permiso by vm.permiso.collectAsState()
    val avisos by vm.avisos.collectAsState()

    LaunchedEffect(Unit) {
        vm.eventos.collect { aviso ->
            val mostrado = Notificador.mostrar(contexto, aviso)
            // TODO Paso 8: si no se mostró, avisar al ViewModel (vm.noSePudoMostrar())
        }
    }

    // TODO Paso 2: rememberLauncherForActivityResult y LaunchedEffect para tienePermiso
    // TODO Paso 6: DisposableEffect para ModoAvionReceiver

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "AvisosCampus",
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = AzulTecsup)
            )
        },
        containerColor = FondoGrisClaro
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Tarjeta 1: Permiso de notificaciones
            TarjetaSeccion(titulo = "Permiso de notificaciones") {
                val (textoEstado, colorEstado) = when (permiso) {
                    PermisoUi.SinPedir -> "Sin pedir" to TextoSecundario
                    PermisoUi.Concedido -> "Permitido" to VerdePermitido
                    PermisoUi.Denegado -> "Denegado" to RojoDenegado
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "Estado:",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextoOscuro
                    )
                    Text(
                        text = textoEstado,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = colorEstado
                    )
                }
                Button(
                    onClick = { /* TODO Paso 2: pedir permiso POST_NOTIFICATIONS */ },
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(containerColor = AzulTecsup)
                ) {
                    Text(text = "Permitir notificaciones", fontWeight = FontWeight.Bold)
                }
                // TODO Paso 8: mostrar mensaje de error y botón "Abrir ajustes" si permiso denegado
            }

            // Tarjeta 2: Avisos
            TarjetaSeccion(titulo = "Avisos") {
                Button(
                    onClick = { /* TODO Paso 3: vm.avisarAhora() */ },
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(containerColor = AzulTecsup)
                ) {
                    Text(text = "Mostrar aviso", fontWeight = FontWeight.Bold)
                }
            }

            // Tarjeta 3: Recordatorio
            TarjetaSeccion(titulo = "Recordatorio") {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = { /* TODO Paso 5: vm.recordarEn(10) */ },
                        shape = RoundedCornerShape(50),
                        colors = ButtonDefaults.buttonColors(containerColor = AzulTecsup)
                    ) {
                        Text(text = "Recordar en 10 s", fontWeight = FontWeight.Bold)
                    }
                    OutlinedButton(
                        onClick = { /* TODO Paso 5: vm.cancelarRecordatorio() */ },
                        enabled = false,
                        shape = RoundedCornerShape(50),
                        border = BorderStroke(1.dp, GrisBorde)
                    ) {
                        Text(text = "Cancelar", fontWeight = FontWeight.Bold)
                    }
                }
            }

            // Tarjeta 4: Receptor de anuncios
            TarjetaSeccion(titulo = "Receptor de anuncios") {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Escuchar el modo avión",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium,
                            color = TextoOscuro
                        )
                        Text(
                            text = "Activa o desactiva el modo avión del celular",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextoSecundario
                        )
                    }
                    Switch(
                        checked = false,
                        onCheckedChange = { /* TODO Paso 6: vm.cambiarEscucha(it) */ }
                    )
                }
            }

            // Tarjeta 5: Historial (Paso 7: reemplazar por HistorialAvisos(avisos))
            TarjetaSeccion(titulo = "Historial") {
                if (avisos.isEmpty()) {
                    Text(
                        text = "Sin avisos todavía",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextoSecundario
                    )
                }
            }
        }
    }
}

@Composable
private fun TarjetaSeccion(
    titulo: String,
    contenido: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, GrisBorde),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = titulo,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = AzulTecsup,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(2.dp))
            contenido()
        }
    }
}