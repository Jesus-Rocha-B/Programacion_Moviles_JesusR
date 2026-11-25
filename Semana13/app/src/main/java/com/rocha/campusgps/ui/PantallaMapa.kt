package com.rocha.campusgps.ui

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import com.rocha.campusgps.R
import com.rocha.campusgps.model.UbicacionUi
import com.rocha.campusgps.ui.theme.CampusGPSTheme
import com.rocha.campusgps.ui.theme.TecsupBorder
import com.rocha.campusgps.ui.theme.TecsupCyan
import com.rocha.campusgps.ui.theme.TecsupCyanContainer
import com.rocha.campusgps.ui.theme.TecsupCyanDark
import com.rocha.campusgps.ui.theme.TecsupCyanLight
import com.rocha.campusgps.ui.theme.TecsupCyanSurface
import com.rocha.campusgps.ui.theme.TecsupTextPrimary
import com.rocha.campusgps.ui.theme.TecsupTextSecondary
import com.rocha.campusgps.util.formatearDistancia
import com.rocha.campusgps.viewmodel.MapaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaMapa(vm: MapaViewModel) {
    val estado by vm.estado.collectAsState()
    val lugaresFiltrados by vm.lugaresFiltrados.collectAsState()
    val categoriaSeleccionada by vm.categoriaSeleccionada.collectAsState()
    val radioSeleccionado by vm.radioSeleccionado.collectAsState()
    val seleccionado by vm.seleccionado.collectAsState()
    val masCercano by vm.masCercano.collectAsState()
    val siguiendoEnVivo by vm.siguiendoEnVivo.collectAsState()
    val modoOscuro by vm.modoOscuro.collectAsState()
    val contexto = LocalContext.current

    // Paleta adaptativa para Modo Oscuro (Dark Slate & Cyan) y Modo Claro (Tecsup Cyan & White)
    val fondoSuperficie = if (modoOscuro) Color(0xFF1E293B) else Color.White
    val fondoApp = if (modoOscuro) Color(0xFF0F172A) else Color.White
    val colorBorde = if (modoOscuro) Color(0xFF334155) else TecsupBorder
    val colorTextoPrimario = if (modoOscuro) Color(0xFFF8FAFC) else TecsupTextPrimary
    val colorTextoSecundario = if (modoOscuro) Color(0xFF94A3B8) else TecsupTextSecondary
    val fondoIcono = if (modoOscuro) Color(0xFF0F172A) else TecsupCyanSurface
    val tinteIcono = if (modoOscuro) Color(0xFF38BDF8) else TecsupCyanDark

    // Diálogo de confirmación para seguimiento en vivo
    var mostrarDialogoSeguimiento by remember { mutableStateOf(false) }

    // Estado interactivo del panel inferior (subir y bajar)
    var expandido by remember { mutableStateOf(true) }

    val alturaSpacer by animateDpAsState(
        targetValue = if (expandido) 325.dp else 135.dp,
        animationSpec = tween(durationMillis = 300),
        label = "alturaSpacer"
    )

    val alturaLista by animateDpAsState(
        targetValue = if (expandido) 200.dp else 0.dp,
        animationSpec = tween(durationMillis = 300),
        label = "alturaLista"
    )

    val lanzador = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { resultado ->
        val concedido = resultado.values.any { it }
        if (concedido) {
            vm.buscar()
        } else {
            vm.permisoDenegado()
        }
    }

    fun tienePermiso(): Boolean {
        val fine = ContextCompat.checkSelfPermission(
            contexto,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        val coarse = ContextCompat.checkSelfPermission(
            contexto,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        return fine || coarse
    }

    fun gpsEstaEncendido(): Boolean {
        val lm = contexto.getSystemService(Context.LOCATION_SERVICE) as? LocationManager ?: return false
        return lm.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
               lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    fun abrirAjustesGps() {
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        contexto.startActivity(intent)
    }

    fun alPulsar() {
        if (tienePermiso()) {
            vm.buscar()
        } else {
            lanzador.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    fun alPulsarSeguimiento() {
        if (!tienePermiso()) {
            lanzador.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        } else if (!gpsEstaEncendido()) {
            abrirAjustesGps()
        } else {
            vm.iniciarSeguimiento()
        }
    }

    fun abrirAjustes() {
        val intent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", contexto.packageName, null)
        ).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        contexto.startActivity(intent)
    }

    // Diálogo interactivo de confirmación para Seguimiento en Vivo
    if (mostrarDialogoSeguimiento) {
        Dialog(onDismissRequest = { mostrarDialogoSeguimiento = false }) {
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = fondoSuperficie,
                shadowElevation = 8.dp,
                border = BorderStroke(1.dp, colorBorde),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    when {
                        // Caso 1: Seguimiento ya está activo -> Pregunta para detener
                        siguiendoEnVivo -> {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Surface(
                                    shape = CircleShape,
                                    color = if (modoOscuro) Color(0xFF451A1A) else Color(0xFFFEF2F2),
                                    border = BorderStroke(1.5.dp, Color(0xFFEF4444).copy(alpha = 0.4f)),
                                    modifier = Modifier.size(42.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.ic_radar_live),
                                            contentDescription = null,
                                            tint = Color(0xFFEF4444),
                                            modifier = Modifier.size(22.dp)
                                        )
                                    }
                                }

                                Text(
                                    text = "¿Detener seguimiento?",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = colorTextoPrimario
                                )
                            }

                            Text(
                                text = "El seguimiento continuo está activo actualmente. Si lo detienes, se suspenderán las actualizaciones automáticas para ahorrar batería en tu dispositivo.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = colorTextoSecundario,
                                lineHeight = 20.sp,
                                textAlign = TextAlign.Justify
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                OutlinedButton(
                                    onClick = { mostrarDialogoSeguimiento = false },
                                    shape = RoundedCornerShape(10.dp),
                                    border = BorderStroke(1.dp, colorBorde),
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(42.dp)
                                ) {
                                    Text(
                                        text = "Continuar",
                                        color = colorTextoSecundario,
                                        fontWeight = FontWeight.SemiBold,
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }

                                Button(
                                    onClick = {
                                        mostrarDialogoSeguimiento = false
                                        vm.detenerSeguimiento()
                                    },
                                    shape = RoundedCornerShape(10.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFFEF4444),
                                        contentColor = Color.White
                                    ),
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(42.dp)
                                ) {
                                    Text(
                                        text = "Detener",
                                        fontWeight = FontWeight.Bold,
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            }
                        }

                        // Caso 2: Falta permiso de ubicación -> Pide permiso primero
                        !tienePermiso() -> {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Surface(
                                    shape = CircleShape,
                                    color = fondoIcono,
                                    border = BorderStroke(1.5.dp, colorBorde),
                                    modifier = Modifier.size(42.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.ic_radar_live),
                                            contentDescription = null,
                                            tint = tinteIcono,
                                            modifier = Modifier.size(22.dp)
                                        )
                                    }
                                }

                                Text(
                                    text = "Permiso de ubicación",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = colorTextoPrimario
                                )
                            }

                            Text(
                                text = "Para iniciar el seguimiento en vivo de CampusGPS, primero debes conceder el permiso de acceso a la ubicación de tu dispositivo.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = colorTextoSecundario,
                                lineHeight = 20.sp,
                                textAlign = TextAlign.Justify
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                OutlinedButton(
                                    onClick = { mostrarDialogoSeguimiento = false },
                                    shape = RoundedCornerShape(10.dp),
                                    border = BorderStroke(1.dp, colorBorde),
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(42.dp)
                                ) {
                                    Text(
                                        text = "Cancelar",
                                        color = colorTextoSecundario,
                                        fontWeight = FontWeight.SemiBold,
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }

                                Button(
                                    onClick = {
                                        mostrarDialogoSeguimiento = false
                                        lanzador.launch(
                                            arrayOf(
                                                Manifest.permission.ACCESS_FINE_LOCATION,
                                                Manifest.permission.ACCESS_COARSE_LOCATION
                                            )
                                        )
                                    },
                                    shape = RoundedCornerShape(10.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = TecsupCyan,
                                        contentColor = Color.White
                                    ),
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(42.dp)
                                ) {
                                    Text(
                                        text = "Dar permiso",
                                        fontWeight = FontWeight.Bold,
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            }
                        }

                        // Caso 3: El GPS del teléfono está apagado -> Pide activar ubicación
                        !gpsEstaEncendido() -> {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Surface(
                                    shape = CircleShape,
                                    color = fondoIcono,
                                    border = BorderStroke(1.5.dp, colorBorde),
                                    modifier = Modifier.size(42.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.ic_radar_live),
                                            contentDescription = null,
                                            tint = tinteIcono,
                                            modifier = Modifier.size(22.dp)
                                        )
                                    }
                                }

                                Text(
                                    text = "Activa tu ubicación",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = colorTextoPrimario
                                )
                            }

                            Text(
                                text = "El GPS de tu dispositivo está apagado. Para que el mapa pueda seguirte en tiempo real mientras caminas por Tecsup, primero debes encender la ubicación en los ajustes de tu teléfono.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = colorTextoSecundario,
                                lineHeight = 20.sp,
                                textAlign = TextAlign.Justify
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                OutlinedButton(
                                    onClick = { mostrarDialogoSeguimiento = false },
                                    shape = RoundedCornerShape(10.dp),
                                    border = BorderStroke(1.dp, colorBorde),
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(42.dp)
                                ) {
                                    Text(
                                        text = "Cancelar",
                                        color = colorTextoSecundario,
                                        fontWeight = FontWeight.SemiBold,
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }

                                Button(
                                    onClick = {
                                        mostrarDialogoSeguimiento = false
                                        abrirAjustesGps()
                                    },
                                    shape = RoundedCornerShape(10.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = TecsupCyan,
                                        contentColor = Color.White
                                    ),
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(42.dp)
                                ) {
                                    Text(
                                        text = "Activar GPS",
                                        fontWeight = FontWeight.Bold,
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            }
                        }

                        // Caso 4: Todo listo (permiso y GPS encendidos) -> Confirmar inicio
                        else -> {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Surface(
                                    shape = CircleShape,
                                    color = fondoIcono,
                                    border = BorderStroke(1.5.dp, colorBorde),
                                    modifier = Modifier.size(42.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.ic_radar_live),
                                            contentDescription = null,
                                            tint = tinteIcono,
                                            modifier = Modifier.size(22.dp)
                                        )
                                    }
                                }

                                Text(
                                    text = "¿Activar seguimiento en vivo?",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = colorTextoPrimario
                                )
                            }

                            Text(
                                text = "El GPS actualizará tu ubicación continua en tiempo real cada 3 segundos. Tu posición y las distancias a todos los lugares de Tecsup se recalcularán automáticamente mientras caminas.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = colorTextoSecundario,
                                lineHeight = 20.sp,
                                textAlign = TextAlign.Justify
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                OutlinedButton(
                                    onClick = { mostrarDialogoSeguimiento = false },
                                    shape = RoundedCornerShape(10.dp),
                                    border = BorderStroke(1.dp, colorBorde),
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(42.dp)
                                ) {
                                    Text(
                                        text = "Cancelar",
                                        color = colorTextoSecundario,
                                        fontWeight = FontWeight.SemiBold,
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }

                                Button(
                                    onClick = {
                                        mostrarDialogoSeguimiento = false
                                        vm.iniciarSeguimiento()
                                    },
                                    shape = RoundedCornerShape(10.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = TecsupCyan,
                                        contentColor = Color.White
                                    ),
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(42.dp)
                                ) {
                                    Text(
                                        text = "Activar",
                                        fontWeight = FontWeight.Bold,
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.logo_campusgps),
                            contentDescription = "Logo CampusGPS",
                            modifier = Modifier
                                .height(32.dp)
                                .padding(start = 4.dp),
                            contentScale = ContentScale.Fit
                        )

                        // Fila de acciones superiores: Conmutador Sol/Luna y Radar de seguimiento
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.padding(end = 6.dp)
                        ) {
                            // Conmutador interactivo Sol ☀️ / Luna 🌙 (Opción 6 de la guía)
                            Surface(
                                onClick = { vm.alternarModoOscuro() },
                                shape = CircleShape,
                                color = if (modoOscuro) Color(0xFF1E293B) else TecsupCyanSurface,
                                border = BorderStroke(
                                    1.5.dp,
                                    if (modoOscuro) Color(0xFFF59E0B).copy(alpha = 0.6f) else TecsupBorder
                                )
                            ) {
                                Box(
                                    modifier = Modifier.size(38.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        painter = painterResource(
                                            id = if (modoOscuro) R.drawable.ic_sun else R.drawable.ic_moon
                                        ),
                                        contentDescription = if (modoOscuro) "Activar modo claro" else "Activar modo oscuro",
                                        tint = if (modoOscuro) Color(0xFFF59E0B) else TecsupCyanDark,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }

                            // Icono interactivo circular para Seguimiento en Vivo
                            Surface(
                                onClick = { mostrarDialogoSeguimiento = true },
                                shape = CircleShape,
                                color = when {
                                    siguiendoEnVivo -> Color(0xFFDCFCE7)
                                    modoOscuro -> Color(0xFF1E293B)
                                    else -> TecsupCyanSurface
                                },
                                border = BorderStroke(
                                    1.5.dp,
                                    when {
                                        siguiendoEnVivo -> Color(0xFF22C55E)
                                        modoOscuro -> colorBorde
                                        else -> TecsupBorder
                                    }
                                )
                            ) {
                                Box(
                                    modifier = Modifier.size(38.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_radar_live),
                                        contentDescription = "Seguimiento en vivo",
                                        tint = when {
                                            siguiendoEnVivo -> Color(0xFF16A34A)
                                            modoOscuro -> Color(0xFF38BDF8)
                                            else -> TecsupCyanDark
                                        },
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                            }
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = fondoApp
                )
            )
        },
        containerColor = fondoApp
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // 1. Mapa arriba ocupando el espacio superior con espacio reservado abajo
            Column(modifier = Modifier.fillMaxSize()) {
                MapaUbicacion(
                    estado = estado,
                    lugares = lugaresFiltrados,
                    masCercano = masCercano,
                    seleccionado = seleccionado,
                    radio = radioSeleccionado,
                    modoOscuro = modoOscuro,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
                // Espacio inferior animado reservado para el panel flotante
                Spacer(modifier = Modifier.height(alturaSpacer))
            }

            // 2. Panel inferior estilo BottomSheet con bordes redondeados arriba
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter),
                shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
                color = fondoSuperficie,
                shadowElevation = 10.dp,
                border = BorderStroke(1.dp, colorBorde.copy(alpha = 0.8f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(fondoSuperficie)
                ) {
                    // Tirador / Handle bar superior interactivo (subir y bajar al pulsar)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { expandido = !expandido }
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .width(42.dp)
                                .height(5.dp)
                                .background(
                                    if (modoOscuro) Color(0xFF475569) else Color(0xFFCBD5E1),
                                    RoundedCornerShape(3.dp)
                                )
                        )
                    }

                    // Fila horizontal de Chips de filtrado (Celeste y Blanco / Modo Oscuro)
                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 6.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(vm.categorias) { cat ->
                            val esSeleccionado = cat == categoriaSeleccionada
                            FilterChip(
                                selected = esSeleccionado,
                                onClick = { vm.seleccionarCategoria(cat) },
                                label = {
                                    Text(
                                        text = cat,
                                        fontWeight = if (esSeleccionado) FontWeight.Bold else FontWeight.Medium
                                    )
                                },
                                shape = RoundedCornerShape(20.dp),
                                border = FilterChipDefaults.filterChipBorder(
                                    enabled = true,
                                    selected = esSeleccionado,
                                    borderColor = colorBorde,
                                    selectedBorderColor = TecsupCyan,
                                    borderWidth = if (esSeleccionado) 1.5.dp else 1.dp
                                ),
                                colors = FilterChipDefaults.filterChipColors(
                                    containerColor = fondoApp,
                                    selectedContainerColor = if (modoOscuro) Color(0xFF0369A1) else TecsupCyanContainer,
                                    labelColor = colorTextoSecundario,
                                    selectedLabelColor = if (modoOscuro) Color.White else TecsupCyanDark
                                )
                            )
                        }
                    }

                    // Selector de radio interactivo cuando la ubicación ya fue obtenida
                    if (estado is UbicacionUi.Lista) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 2.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "Radio:",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = colorTextoSecundario
                            )
                            vm.radiosDisponibles.forEach { r ->
                                val esActivo = r == radioSeleccionado
                                val etiqueta = r?.let { "${it}m" } ?: "Todo"
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = when {
                                        esActivo -> TecsupCyan
                                        modoOscuro -> Color(0xFF0F172A)
                                        else -> TecsupCyanSurface
                                    },
                                    border = BorderStroke(
                                        1.dp,
                                        when {
                                            esActivo -> TecsupCyan
                                            modoOscuro -> colorBorde
                                            else -> TecsupBorder
                                        }
                                    ),
                                    modifier = Modifier.clickable { vm.seleccionarRadio(r) }
                                ) {
                                    Text(
                                        text = etiqueta,
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = if (esActivo) FontWeight.Bold else FontWeight.Medium,
                                        color = when {
                                            esActivo -> Color.White
                                            modoOscuro -> Color(0xFF38BDF8)
                                            else -> TecsupCyanDark
                                        },
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp)
                                    )
                                }
                            }
                        }
                    }

                    // Controles centrales (Celeste y Blanco / Modo Oscuro)
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 4.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        // Fila de botones principales en una sola línea
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 2.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Botón 1: Obtener mi ubicación
                            Button(
                                onClick = { alPulsar() },
                                enabled = estado !is UbicacionUi.Buscando,
                                shape = RoundedCornerShape(12.dp),
                                contentPadding = PaddingValues(horizontal = 6.dp, vertical = 10.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = TecsupCyan,
                                    contentColor = Color.White,
                                    disabledContainerColor = TecsupCyan.copy(alpha = 0.5f),
                                    disabledContentColor = Color.White.copy(alpha = 0.8f)
                                ),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(44.dp)
                            ) {
                                Text(
                                    text = "Obtener ubicación",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }

                            // Botón 2: Ir al más cercano
                            OutlinedButton(
                                onClick = { masCercano?.let { vm.seleccionar(it.id) } },
                                enabled = masCercano != null,
                                shape = RoundedCornerShape(12.dp),
                                contentPadding = PaddingValues(horizontal = 6.dp, vertical = 10.dp),
                                border = BorderStroke(
                                    1.5.dp,
                                    if (masCercano != null) TecsupCyan else colorBorde
                                ),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = if (modoOscuro) Color(0xFF38BDF8) else TecsupCyanDark,
                                    disabledContentColor = Color.Gray
                                ),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(44.dp)
                            ) {
                                Text(
                                    text = "Ir al más cercano",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }

                        // Texto: Más cercano estrictamente en una sola línea
                        masCercano?.let { cercano ->
                            val dist = lugaresFiltrados.firstOrNull { it.lugar.id == cercano.id }?.metros
                            val textoDist = dist?.let { " (${formatearDistancia(it)})" }.orEmpty()
                            Text(
                                text = "Más cercano: ${cercano.nombre}$textoDist",
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Medium,
                                color = if (modoOscuro) Color(0xFF38BDF8) else TecsupCyanDark,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        when (val e = estado) {
                            UbicacionUi.Buscando -> {
                                CircularProgressIndicator(
                                    color = TecsupCyan,
                                    modifier = Modifier.padding(vertical = 4.dp)
                                )
                            }

                            is UbicacionUi.Error -> {
                                MensajeError(
                                    error = e,
                                    onReintentar = { alPulsar() },
                                    onAbrirAjustes = { abrirAjustes() }
                                )
                            }

                            else -> Unit
                        }
                    }

                    // 4. Lista fija abajo animada
                    ListaLugares(
                        lugares = lugaresFiltrados,
                        seleccionado = seleccionado,
                        onSeleccionar = { vm.seleccionar(it) },
                        modoOscuro = modoOscuro,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(alturaLista)
                    )
                }
            }
        }
    }
}
