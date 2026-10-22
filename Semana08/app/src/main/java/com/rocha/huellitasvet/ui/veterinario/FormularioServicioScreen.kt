package com.rocha.huellitasvet.ui.veterinario

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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.rocha.huellitasvet.ui.components.AppTopBar
import com.rocha.huellitasvet.ui.components.FilaSwitch
import com.rocha.huellitasvet.ui.components.VetButton
import com.rocha.huellitasvet.ui.components.VetTextField
import com.rocha.huellitasvet.ui.components.iconoDeServicio
import com.rocha.huellitasvet.viewmodel.ServiciosViewModel

@Composable
fun FormularioServicioScreen(
    vm: ServiciosViewModel,
    servicioId: Int?,          // null = servicio nuevo
    onListo: () -> Unit
) {
    val servicios by vm.servicios.collectAsState()
    val errores by vm.errores.collectAsState()
    val mensaje by vm.mensaje.collectAsState()
    val guardado by vm.guardado.collectAsState()
    val eliminado by vm.eliminado.collectAsState()

    var nombre by remember { mutableStateOf("") }
    var duracion by remember { mutableIntStateOf(30) }
    var precio by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var icono by remember { mutableStateOf("heart") }
    var activo by remember { mutableStateOf(true) }
    var cargado by remember { mutableStateOf(false) }
    var confirmarEliminar by remember { mutableStateOf(false) }
    val snackbar = remember { SnackbarHostState() }

    // Si es edición, carga los datos del servicio una sola vez
    val existente = servicios.firstOrNull { it.id == servicioId }
    LaunchedEffect(existente) {
        val s = existente
        if (s != null && !cargado) {
            nombre = s.nombre
            duracion = s.duracionMin
            precio = s.precio.toString()
            descripcion = s.descripcion
            icono = s.icono
            activo = s.activo
            cargado = true
        }
    }

    LaunchedEffect(mensaje) {
        mensaje?.let {
            snackbar.showSnackbar(it)
            vm.limpiarMensaje()
        }
    }
    LaunchedEffect(guardado, eliminado) {
        if (guardado || eliminado) onListo()
    }

    val colores = MaterialTheme.colorScheme

    Scaffold(
        topBar = {
            AppTopBar(
                titulo = if (servicioId == null) "Nuevo servicio" else "Editar servicio",
                onVolver = onListo
            )
        },
        snackbarHost = { SnackbarHost(snackbar) },
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
            VetTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = "Nombre del servicio",
                error = errores.nombre
            )

            Spacer(Modifier.height(12.dp))

            // Duración
            Text("Duración", fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(6.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf(15, 30, 45, 60).forEach { min ->
                    FilterChip(
                        selected = duracion == min,
                        onClick = { duracion = min },
                        label = { Text("$min min") },
                        shape = RoundedCornerShape(50)
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            VetTextField(
                value = precio,
                onValueChange = { precio = it },
                label = "Precio (S/)",
                keyboardType = KeyboardType.Decimal,
                error = errores.precio
            )

            Spacer(Modifier.height(4.dp))

            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                label = { Text("Descripción") },
                minLines = 3,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(Modifier.height(16.dp))

            // Ícono
            Text("Ícono", fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(6.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                listOf("heart", "syringe", "scissors").forEach { nombreIcono ->
                    val elegido = icono == nombreIcono
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .height(56.dp)
                            .clickable { icono = nombreIcono },
                        colors = CardDefaults.cardColors(
                            containerColor = if (elegido) colores.primaryContainer else colores.surface
                        ),
                        border = BorderStroke(
                            if (elegido) 2.dp else 1.dp,
                            if (elegido) colores.primary else colores.outlineVariant
                        ),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Icon(
                                iconoDeServicio(nombreIcono),
                                contentDescription = nombreIcono,
                                tint = colores.primary
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // Switch "Servicio activo"
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = colores.surface),
                shape = RoundedCornerShape(14.dp)
            ) {
                Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)) {
                    FilaSwitch(
                        titulo = "Servicio activo",
                        descripcion = "Los dueños podrán pedirlo",
                        checked = activo,
                        onChange = { activo = it }
                    )
                }
            }

            Spacer(Modifier.height(24.dp))

            VetButton(
                texto = "Guardar servicio",
                onClick = {
                    vm.guardar(
                        id = servicioId ?: 0,
                        nombre = nombre,
                        duracionMin = duracion,
                        precio = precio,
                        descripcion = descripcion,
                        icono = icono,
                        activo = activo
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
            ) { Text("Cancelar") }

            // Eliminar: solo al editar. Si tiene citas, el ViewModel avisa y no lo borra.
            if (existente != null) {
                Spacer(Modifier.height(8.dp))
                TextButton(
                    onClick = { confirmarEliminar = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Eliminar servicio", color = colores.error)
                }
            }
        }

        if (confirmarEliminar && existente != null) {
            AlertDialog(
                onDismissRequest = { confirmarEliminar = false },
                title = { Text("Eliminar servicio") },
                text = { Text("¿Seguro que quieres eliminar \"${existente.nombre}\"?") },
                confirmButton = {
                    TextButton(onClick = {
                        confirmarEliminar = false
                        vm.eliminar(existente)
                    }) { Text("Eliminar", color = MaterialTheme.colorScheme.error) }
                },
                dismissButton = {
                    TextButton(onClick = { confirmarEliminar = false }) { Text("Cancelar") }
                }
            )
        }
    }
}