package com.rocha.huellitasvet.ui.dueno

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.rocha.huellitasvet.data.local.entity.Mascota
import com.rocha.huellitasvet.model.Especie
import com.rocha.huellitasvet.model.Sexo
import com.rocha.huellitasvet.model.nombreBonito
import com.rocha.huellitasvet.ui.components.AppTopBar
import com.rocha.huellitasvet.ui.components.MascotaAvatar
import com.rocha.huellitasvet.ui.components.VetButton
import com.rocha.huellitasvet.ui.components.VetTextField
import com.rocha.huellitasvet.viewmodel.MascotaViewModel
import kotlinx.coroutines.flow.flowOf

@Composable
fun FormularioMascotaScreen(
    vm: MascotaViewModel,
    mascotaId: Int?,          // null = mascota nueva
    onListo: () -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var tipoAnimal by remember { mutableStateOf("PERRO") }
    var especieTexto by remember { mutableStateOf("Perro") }
    var raza by remember { mutableStateOf("") }
    var anio by remember { mutableStateOf("") }
    var peso by remember { mutableStateOf("") }
    var sexo by remember { mutableStateOf<String?>("MACHO") }

    val errores by vm.errores.collectAsState()
    val guardada by vm.guardada.collectAsState()

    // Si es edición, carga los datos de la mascota una sola vez
    val existente by remember(mascotaId) {
        if (mascotaId != null) vm.mascota(mascotaId) else flowOf<Mascota?>(null)
    }.collectAsState(initial = null)
    var cargado by remember { mutableStateOf(false) }

    LaunchedEffect(existente) {
        val m = existente
        if (m != null && !cargado) {
            nombre = m.nombre
            especieTexto = m.especie.nombreBonito()
            tipoAnimal = when (m.especie.uppercase()) {
                "PERRO" -> "PERRO"
                "GATO" -> "GATO"
                else -> "OTRO"
            }
            raza = m.raza
            anio = m.anioNacimiento.toString()
            peso = m.peso.toString()
            sexo = m.sexo
            cargado = true
        }
    }

    LaunchedEffect(guardada) {
        if (guardada) onListo()
    }

    Scaffold(
        topBar = {
            AppTopBar(
                titulo = if (mascotaId == null) "Nueva mascota" else "Editar mascota",
                onVolver = onListo
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // El avatar cambia según la especie elegida
            MascotaAvatar(especie = tipoAnimal, tamano = 96.dp)

            Spacer(Modifier.height(16.dp))

            VetTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = "Nombre",
                error = errores.nombre
            )

            Spacer(Modifier.height(12.dp))

            // Tipo de animal (Chips simétricos de igual tamaño)
            Column(modifier = Modifier.fillMaxWidth()) {
                Text("Tipo de animal", fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Especie.values().forEach { e ->
                        val seleccionada = tipoAnimal == e.name
                        Card(
                            modifier = Modifier
                                .weight(1f)
                                .height(44.dp)
                                .clip(RoundedCornerShape(50))
                                .clickable {
                                    tipoAnimal = e.name
                                    if (e.name == "PERRO") {
                                        especieTexto = "Perro"
                                    } else if (e.name == "GATO") {
                                        especieTexto = "Gato"
                                    } else {
                                        if (especieTexto == "Perro" || especieTexto == "Gato") {
                                            especieTexto = ""
                                        }
                                    }
                                },
                            colors = CardDefaults.cardColors(
                                containerColor = if (seleccionada) MaterialTheme.colorScheme.primary else Color.White
                            ),
                            border = BorderStroke(
                                width = 1.dp,
                                color = if (seleccionada) MaterialTheme.colorScheme.primary else Color(0xFFCBD5E1)
                            ),
                            shape = RoundedCornerShape(50)
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = e.name.nombreBonito(),
                                    style = MaterialTheme.typography.labelLarge,
                                    fontWeight = if (seleccionada) FontWeight.Bold else FontWeight.Medium,
                                    color = if (seleccionada) Color.White else Color(0xFF334155)
                                )
                            }
                        }
                    }
                }
                errores.especie?.let {
                    Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                }
            }

            Spacer(Modifier.height(12.dp))

            // Fila: Especie | Raza (Simétricos 50/50)
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                VetTextField(
                    value = especieTexto,
                    onValueChange = { especieTexto = it },
                    label = "Especie",
                    enabled = tipoAnimal == "OTRO",
                    error = errores.especie,
                    modifier = Modifier.weight(1f)
                )
                VetTextField(
                    value = raza,
                    onValueChange = { raza = it },
                    label = "Raza",
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(Modifier.height(12.dp))

            // Fila: Año de nacimiento | Peso (kg) (Simétricos 50/50)
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                VetTextField(
                    value = anio,
                    onValueChange = { anio = it },
                    label = "Año de nacimiento",
                    keyboardType = KeyboardType.Number,
                    error = errores.anio,
                    modifier = Modifier.weight(1f)
                )
                VetTextField(
                    value = peso,
                    onValueChange = { peso = it },
                    label = "Peso (kg)",
                    keyboardType = KeyboardType.Decimal,
                    error = errores.peso,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(Modifier.height(12.dp))

            // Sexo (Chips simétricos 50/50 de igual tamaño)
            Column(modifier = Modifier.fillMaxWidth()) {
                Text("Sexo", fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Sexo.values().forEach { s ->
                        val seleccionada = sexo == s.name
                        Card(
                            modifier = Modifier
                                .weight(1f)
                                .height(44.dp)
                                .clip(RoundedCornerShape(50))
                                .clickable { sexo = s.name },
                            colors = CardDefaults.cardColors(
                                containerColor = if (seleccionada) MaterialTheme.colorScheme.primary else Color.White
                            ),
                            border = BorderStroke(
                                width = 1.dp,
                                color = if (seleccionada) MaterialTheme.colorScheme.primary else Color(0xFFCBD5E1)
                            ),
                            shape = RoundedCornerShape(50)
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = s.name.nombreBonito(),
                                    style = MaterialTheme.typography.labelLarge,
                                    fontWeight = if (seleccionada) FontWeight.Bold else FontWeight.Medium,
                                    color = if (seleccionada) Color.White else Color(0xFF334155)
                                )
                            }
                        }
                    }
                }
                errores.sexo?.let {
                    Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                }
            }

            Spacer(Modifier.height(24.dp))

            VetButton(
                texto = if (mascotaId == null) "Registrar mascota" else "Guardar cambios",
                onClick = {
                    vm.guardar(
                        id = mascotaId ?: 0,
                        nombre = nombre,
                        especie = especieTexto.ifBlank { tipoAnimal },
                        raza = raza,
                        anio = anio,
                        peso = peso,
                        sexo = sexo
                    )
                }
            )

            Spacer(Modifier.height(8.dp))

            OutlinedButton(
                onClick = onListo,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(14.dp)
            ) {
                Text(
                    text = "Cancelar",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
