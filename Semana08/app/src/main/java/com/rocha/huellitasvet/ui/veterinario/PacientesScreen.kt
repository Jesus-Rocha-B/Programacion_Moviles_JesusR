package com.rocha.huellitasvet.ui.veterinario
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.rocha.huellitasvet.data.local.relation.MascotaConDueno
import com.rocha.huellitasvet.model.Especie
import com.rocha.huellitasvet.model.edadTexto
import com.rocha.huellitasvet.model.nombreBonito
import com.rocha.huellitasvet.ui.components.AppTopBar
import com.rocha.huellitasvet.ui.components.MascotaAvatar
import com.rocha.huellitasvet.viewmodel.PacientesViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun PacientesScreen(
    vm: PacientesViewModel,
    onPaciente: (Int) -> Unit
) {
    val pacientes by vm.pacientes.collectAsState()
    val busqueda by vm.busqueda.collectAsState()
    val filtro by vm.filtro.collectAsState()
    val visitas by vm.ultimasVisitas.collectAsState()

    val opciones = listOf(
        null to "Todos",
        Especie.PERRO.name to "Perros",
        Especie.GATO.name to "Gatos",
        Especie.OTRO.name to "Otros"
    )

    Scaffold(
        topBar = { AppTopBar(titulo = "Pacientes") },
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp)
        ) {
            // Buscador
            OutlinedTextField(
                value = busqueda,
                onValueChange = { vm.buscar(it) },
                placeholder = { Text("Buscar mascota, dueño o raza...") },
                leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
                trailingIcon = {
                    if (busqueda.isNotEmpty()) {
                        IconButton(onClick = { vm.buscar("") }) {
                            Icon(Icons.Filled.Clear, contentDescription = "Limpiar búsqueda")
                        }
                    }
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp)
            )

            Spacer(Modifier.height(10.dp))

            // Filtro por especie
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                opciones.forEach { (valor, etiqueta) ->
                    FilterChip(
                        selected = filtro == valor,
                        onClick = { vm.filtrar(valor) },
                        label = { Text(etiqueta) },
                        shape = RoundedCornerShape(50)
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

            Text(
                text = "${pacientes.size} pacientes encontrados",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.height(8.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                if (pacientes.isEmpty()) {
                    item { Text("No se encontraron pacientes") }
                }
                items(pacientes, key = { it.mascota.id }) { p ->
                    TarjetaPaciente(
                        paciente = p,
                        ultimaVisita = visitas[p.mascota.id],
                        onClick = { onPaciente(p.mascota.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun TarjetaPaciente(
    paciente: MascotaConDueno,
    ultimaVisita: String?,
    onClick: () -> Unit
) {
    val m = paciente.mascota
    val visita = ultimaVisita?.let {
        "Última visita: " + LocalDate.parse(it)
            .format(DateTimeFormatter.ofPattern("d MMM", Locale.forLanguageTag("es-PE")))
    } ?: "Sin visitas"

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(2.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            MascotaAvatar(especie = m.especie, tamano = 56.dp)
            Spacer(Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(m.nombre, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(
                    "${m.raza.ifBlank { m.especie.nombreBonito() }} · ${m.edadTexto()}",
                    style = MaterialTheme.typography.bodySmall
                )
                Text("Dueño: ${paciente.dueno.nombre}", style = MaterialTheme.typography.bodySmall)
                Text(visita, style = MaterialTheme.typography.labelSmall)
            }
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null)
        }
    }
}