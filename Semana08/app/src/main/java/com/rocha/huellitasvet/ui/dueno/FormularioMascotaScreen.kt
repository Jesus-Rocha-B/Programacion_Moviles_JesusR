package com.rocha.huellitasvet.ui.dueno
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FilterChip
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
    var especie by remember { mutableStateOf<String?>(null) }
    var raza by remember { mutableStateOf("") }
    var anio by remember { mutableStateOf("") }
    var peso by remember { mutableStateOf("") }
    var sexo by remember { mutableStateOf<String?>(null) }

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
            especie = m.especie
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
            MascotaAvatar(especie = especie ?: Especie.OTRO.name, tamano = 96.dp)

            Spacer(Modifier.height(16.dp))

            VetTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = "Nombre",
                error = errores.nombre
            )

            Spacer(Modifier.height(12.dp))

            // Especie
            Column(modifier = Modifier.fillMaxWidth()) {
                Text("Especie", fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(6.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Especie.values().forEach { e ->
                        FilterChip(
                            selected = especie == e.name,
                            onClick = { especie = e.name },
                            label = { Text(e.name.nombreBonito()) },
                            shape = RoundedCornerShape(50)
                        )
                    }
                }
                errores.especie?.let {
                    Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                }
            }

            Spacer(Modifier.height(12.dp))

            VetTextField(
                value = raza,
                onValueChange = { raza = it },
                label = "Raza"
            )

            Spacer(Modifier.height(4.dp))

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

            Spacer(Modifier.height(8.dp))

            // Sexo
            Column(modifier = Modifier.fillMaxWidth()) {
                Text("Sexo", fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(6.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Sexo.values().forEach { s ->
                        FilterChip(
                            selected = sexo == s.name,
                            onClick = { sexo = s.name },
                            label = { Text(s.name.nombreBonito()) },
                            shape = RoundedCornerShape(50)
                        )
                    }
                }
                errores.sexo?.let {
                    Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                }
            }

            Spacer(Modifier.height(24.dp))

            VetButton(
                texto = "Guardar mascota",
                onClick = {
                    vm.guardar(
                        id = mascotaId ?: 0,
                        nombre = nombre,
                        especie = especie,
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
                Text("Cancelar")
            }
        }
    }
}