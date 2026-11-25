package com.rocha.centrotareas.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rocha.centrotareas.model.CargaUi
import com.rocha.centrotareas.model.PrimosUi
import com.rocha.centrotareas.viewmodel.TareasViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaTareas(vm: TareasViewModel) {
    val mensaje by vm.mensaje.collectAsState()
    val progreso by vm.progreso.collectAsState()
    val corriendo by vm.corriendo.collectAsState()
    val primos by vm.primos.collectAsState()
    val carga by vm.carga.collectAsState()
    val simularError by vm.simularError.collectAsState()

    var toques by remember { mutableIntStateOf(0) }
    var textoNumero by remember { mutableStateOf("") }
    var hastaConsultado by remember { mutableIntStateOf(0) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "CentroTareas",
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1976D2)
                )
            )
        },
        containerColor = Color(0xFFF8FAFC)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Tarjeta 1: Tarea con progreso (Pasos 3 y 4)
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text(
                        text = "Tarea con progreso",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1976D2)
                    )

                    // Botón 1: Tócame (contador centrado)
                    Button(
                        onClick = { toques++ },
                        shape = RoundedCornerShape(50),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF1976D2),
                            contentColor = Color.White
                        ),
                        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 10.dp)
                    ) {
                        Text(
                            text = "Tócame: $toques",
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Fila de botones centrados: Iniciar y Cancelar (Paso 4)
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = { vm.tareaLarga() },
                            enabled = !corriendo,
                            shape = RoundedCornerShape(50),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF1976D2),
                                contentColor = Color.White,
                                disabledContainerColor = Color(0xFFE2E8F0),
                                disabledContentColor = Color(0xFF94A3B8)
                            ),
                            contentPadding = PaddingValues(horizontal = 22.dp, vertical = 10.dp)
                        ) {
                            Text(
                                text = "Iniciar",
                                fontWeight = FontWeight.Bold
                            )
                        }

                        OutlinedButton(
                            onClick = { vm.cancelar() },
                            enabled = corriendo,
                            shape = RoundedCornerShape(50),
                            border = BorderStroke(
                                1.5.dp,
                                if (corriendo) Color(0xFF1976D2) else Color(0xFFCBD5E1)
                            ),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = Color(0xFF1976D2),
                                disabledContentColor = Color(0xFF94A3B8)
                            ),
                            contentPadding = PaddingValues(horizontal = 22.dp, vertical = 10.dp)
                        ) {
                            Text(
                                text = "Cancelar",
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    // Barra de progreso interactiva (0% a 100%)
                    LinearProgressIndicator(
                        progress = { progreso },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp)
                            .height(6.dp),
                        color = Color(0xFF1976D2),
                        trackColor = Color(0xFFE2E8F0)
                    )

                    // Porcentaje numérico (ej. 40 % o 62 %)
                    Text(
                        text = "${(progreso * 100).toInt()} %",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF64748B),
                        fontWeight = FontWeight.SemiBold
                    )

                    // Texto del mensaje ("Listo", "Trabajando...", "Cancelado", "Terminó")
                    Text(
                        text = mensaje,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF334155),
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Tarjeta 2: Contar números primos (Paso 5)
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text(
                        text = "Contar números primos",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1976D2)
                    )

                    OutlinedTextField(
                        value = textoNumero,
                        onValueChange = { nuevo ->
                            if (nuevo.all { it.isDigit() }) {
                                textoNumero = nuevo
                            }
                        },
                        label = { Text("Hasta el número") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF1976D2),
                            unfocusedBorderColor = Color(0xFF90CAF9),
                            focusedLabelColor = Color(0xFF1976D2),
                            unfocusedLabelColor = Color(0xFF1976D2),
                            focusedTextColor = Color(0xFF1E293B),
                            unfocusedTextColor = Color(0xFF1E293B),
                            cursorColor = Color(0xFF1976D2)
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    val numeroValido = textoNumero.toIntOrNull()?.takeIf { it >= 2 }
                    val calculandoPrimos = primos is PrimosUi.Calculando

                    // Botones Contar y Cancelar
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = {
                                numeroValido?.let {
                                    hastaConsultado = it
                                    vm.contar(it)
                                }
                            },
                            enabled = numeroValido != null && !calculandoPrimos,
                            shape = RoundedCornerShape(50),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF1976D2),
                                contentColor = Color.White,
                                disabledContainerColor = Color(0xFFE2E8F0),
                                disabledContentColor = Color(0xFF94A3B8)
                            ),
                            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 10.dp)
                        ) {
                            Text(
                                text = "Contar",
                                fontWeight = FontWeight.Bold
                            )
                        }

                        OutlinedButton(
                            onClick = { vm.cancelarPrimos() },
                            enabled = calculandoPrimos,
                            shape = RoundedCornerShape(50),
                            border = BorderStroke(
                                1.5.dp,
                                if (calculandoPrimos) Color(0xFF1976D2) else Color(0xFFCBD5E1)
                            ),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = Color(0xFF1976D2),
                                disabledContentColor = Color(0xFF94A3B8)
                            ),
                            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 10.dp)
                        ) {
                            Text(
                                text = "Cancelar",
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    // Recuadro de resultado según el estado de primos
                    when (val estado = primos) {
                        is PrimosUi.Listo -> {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color(0xFFE8F5E9), shape = RoundedCornerShape(12.dp))
                                    .border(1.dp, Color(0xFFA5D6A7), shape = RoundedCornerShape(12.dp))
                                    .padding(vertical = 12.dp, horizontal = 16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = "Primos hasta $hastaConsultado: ${estado.cantidad}",
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1B5E20),
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Text(
                                    text = "Tiempo: ${estado.milisegundos} ms",
                                    fontWeight = FontWeight.Medium,
                                    color = Color(0xFF2E7D32),
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                        is PrimosUi.Calculando -> {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color(0xFFF8FAFC), shape = RoundedCornerShape(12.dp))
                                    .border(1.dp, Color(0xFFE2E8F0), shape = RoundedCornerShape(12.dp))
                                    .padding(vertical = 12.dp, horizontal = 16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                LinearProgressIndicator(
                                    progress = { estado.progreso },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(6.dp),
                                    color = Color(0xFF1976D2),
                                    trackColor = Color(0xFFE2E8F0)
                                )
                                Text(
                                    text = "Calculando: ${(estado.progreso * 100).toInt()} %",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color(0xFF64748B),
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                        is PrimosUi.Cancelado -> {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color(0xFFF8FAFC), shape = RoundedCornerShape(12.dp))
                                    .border(1.dp, Color(0xFFE2E8F0), shape = RoundedCornerShape(12.dp))
                                    .padding(vertical = 12.dp, horizontal = 16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Cancelado",
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF475569),
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                        PrimosUi.Inicial -> {
                            // Sin recuadro hasta iniciar cálculo
                        }
                    }
                }
            }

            // Tarjeta 3: Dos cargas a la vez con async (Paso 6)
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text(
                        text = "Dos cargas",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1976D2)
                    )

                    // Interruptor Simular error (Paso 7)
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Switch(
                            checked = simularError,
                            onCheckedChange = { vm.cambiarSimularError(it) },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = Color(0xFF1976D2),
                                uncheckedThumbColor = Color.White,
                                uncheckedTrackColor = Color(0xFFCBD5E1)
                            )
                        )
                        Text(
                            text = "Simular error",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF334155),
                            fontWeight = FontWeight.Medium
                        )
                    }

                    val estaCargando = carga is CargaUi.Cargando || carga is CargaUi.Reintentando

                    // Fila de botones: Una por una (OutlinedButton) y A la vez (Button)
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedButton(
                            onClick = { vm.cargarUnaPorUna() },
                            enabled = !estaCargando,
                            shape = RoundedCornerShape(50),
                            border = BorderStroke(
                                1.5.dp,
                                if (!estaCargando) Color(0xFF1976D2) else Color(0xFFCBD5E1)
                            ),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = Color(0xFF1976D2),
                                disabledContentColor = Color(0xFF94A3B8)
                            ),
                            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 10.dp)
                        ) {
                            Text(
                                text = "Una por una",
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Button(
                            onClick = { vm.cargarALaVez() },
                            enabled = !estaCargando,
                            shape = RoundedCornerShape(50),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF1976D2),
                                contentColor = Color.White,
                                disabledContainerColor = Color(0xFFE2E8F0),
                                disabledContentColor = Color(0xFF94A3B8)
                            ),
                            contentPadding = PaddingValues(horizontal = 22.dp, vertical = 10.dp)
                        ) {
                            Text(
                                text = "A la vez",
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    // Estado de Carga
                    when (val estado = carga) {
                        is CargaUi.Cargando -> {
                            CircularProgressIndicator(
                                modifier = Modifier.padding(vertical = 8.dp),
                                color = Color(0xFF1976D2)
                            )
                        }
                        is CargaUi.Reintentando -> {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.padding(vertical = 4.dp),
                                    color = Color(0xFF1976D2)
                                )
                                Text(
                                    text = "Reintentando (intento ${estado.intento} de ${estado.maxIntentos})...",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color(0xFFE65100),
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                        is CargaUi.Exito -> {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color(0xFFE8F5E9), shape = RoundedCornerShape(12.dp))
                                    .border(1.dp, Color(0xFFA5D6A7), shape = RoundedCornerShape(12.dp))
                                    .padding(vertical = 12.dp, horizontal = 16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = "Modo: ${estado.modo}",
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1B5E20),
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Text(
                                    text = "Perfil: ${estado.perfil}",
                                    color = Color(0xFF2E7D32),
                                    style = MaterialTheme.typography.bodySmall
                                )
                                Text(
                                    text = "Pedidos: ${estado.pedidos}",
                                    color = Color(0xFF2E7D32),
                                    style = MaterialTheme.typography.bodySmall
                                )
                                Text(
                                    text = "Tiempo: ${estado.milisegundos} ms",
                                    color = Color(0xFF2E7D32),
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }

                            if (estado.modo == "A la vez") {
                                Box(
                                    modifier = Modifier
                                        .background(Color(0xFFF1F5F9), RoundedCornerShape(50))
                                        .border(1.dp, Color(0xFFCBD5E1), RoundedCornerShape(50))
                                        .padding(horizontal = 12.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = "«Una por una» tarda ≈ 5000 ms",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = Color(0xFF64748B),
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                        is CargaUi.Error -> {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(Color(0xFFFEE2E2), shape = RoundedCornerShape(12.dp))
                                        .border(1.dp, Color(0xFFFCA5A5), shape = RoundedCornerShape(12.dp))
                                        .padding(vertical = 12.dp, horizontal = 16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = estado.mensaje,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFFDC2626),
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }

                                OutlinedButton(
                                    onClick = { vm.cargarALaVez() },
                                    shape = RoundedCornerShape(50),
                                    border = BorderStroke(1.5.dp, Color(0xFF1976D2)),
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        contentColor = Color(0xFF1976D2)
                                    ),
                                    contentPadding = PaddingValues(horizontal = 24.dp, vertical = 10.dp)
                                ) {
                                    Text(
                                        text = "Reintentar",
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                        CargaUi.Inicial -> {
                            // Sin recuadro hasta iniciar carga
                        }
                    }
                }
            }
        }
    }
}
