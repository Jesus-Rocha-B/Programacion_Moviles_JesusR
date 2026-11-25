package com.rocha.campusgps.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.Circle
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberUpdatedMarkerState
import com.rocha.campusgps.model.Lugar
import com.rocha.campusgps.model.LugarConDistancia
import com.rocha.campusgps.model.UbicacionUi
import com.rocha.campusgps.ui.theme.TecsupBorder
import com.rocha.campusgps.ui.theme.TecsupCyan
import com.rocha.campusgps.ui.theme.TecsupCyanDark
import kotlinx.coroutines.launch

// Estilo visual nocturno oficial de Google Maps para modo oscuro
private const val ESTILO_MAPA_OSCURO = """
[
  {"elementType": "geometry", "stylers": [{"color": "#242f3e"}]},
  {"elementType": "labels.text.fill", "stylers": [{"color": "#746855"}]},
  {"elementType": "labels.text.stroke", "stylers": [{"color": "#242f3e"}]},
  {"featureType": "administrative.locality", "elementType": "labels.text.fill", "stylers": [{"color": "#d59563"}]},
  {"featureType": "poi", "elementType": "labels.text.fill", "stylers": [{"color": "#d59563"}]},
  {"featureType": "poi.park", "elementType": "geometry", "stylers": [{"color": "#263c3f"}]},
  {"featureType": "poi.park", "elementType": "labels.text.fill", "stylers": [{"color": "#6b9a76"}]},
  {"featureType": "road", "elementType": "geometry", "stylers": [{"color": "#38414e"}]},
  {"featureType": "road", "elementType": "geometry.stroke", "stylers": [{"color": "#212a37"}]},
  {"featureType": "road", "elementType": "labels.text.fill", "stylers": [{"color": "#9ca5b3"}]},
  {"featureType": "road.highway", "elementType": "geometry", "stylers": [{"color": "#746855"}]},
  {"featureType": "road.highway", "elementType": "geometry.stroke", "stylers": [{"color": "#1f2835"}]},
  {"featureType": "road.highway", "elementType": "labels.text.fill", "stylers": [{"color": "#f3d19c"}]},
  {"featureType": "transit", "elementType": "geometry", "stylers": [{"color": "#2f3948"}]},
  {"featureType": "transit.station", "elementType": "labels.text.fill", "stylers": [{"color": "#d59563"}]},
  {"featureType": "water", "elementType": "geometry", "stylers": [{"color": "#17263c"}]},
  {"featureType": "water", "elementType": "labels.text.fill", "stylers": [{"color": "#515c6d"}]},
  {"featureType": "water", "elementType": "labels.text.stroke", "stylers": [{"color": "#17263c"}]}
]
"""

@Composable
fun MapaUbicacion(
    estado: UbicacionUi,
    lugares: List<LugarConDistancia>,
    masCercano: Lugar?,
    seleccionado: Int?,
    radio: Int? = null,
    modoOscuro: Boolean = false,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()

    // Cámara inicial centrada en el Patio de Tecsup Santa Anita (zoom 18f)
    val camara = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(-12.04412, -76.95282), 18f)
    }

    // LaunchedEffect(estado): si es Lista, animar la cámara (zoom 17f)
    LaunchedEffect(estado) {
        if (estado is UbicacionUi.Lista) {
            camara.animate(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(estado.posicion.latitud, estado.posicion.longitud),
                    17f
                )
            )
        }
    }

    // LaunchedEffect(seleccionado): anima la cámara hasta ese lugar con zoom 18f
    LaunchedEffect(seleccionado) {
        val lugar = lugares.firstOrNull { it.lugar.id == seleccionado }?.lugar
        if (lugar != null) {
            camara.animate(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(lugar.latitud, lugar.longitud),
                    18f
                )
            )
        }
    }

    Box(modifier = modifier) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = camara,
            properties = MapProperties(
                isMyLocationEnabled = estado is UbicacionUi.Lista,
                mapStyleOptions = if (modoOscuro) MapStyleOptions(ESTILO_MAPA_OSCURO) else null
            ),
            uiSettings = MapUiSettings(
                zoomControlsEnabled = false, // Desactivamos los controles nativos grises
                compassEnabled = true,
                myLocationButtonEnabled = estado is UbicacionUi.Lista
            )
        ) {
            // Mejora 3: Círculo vectorial de radio de cercanía cuando el usuario tiene ubicación y radio activo
            if (estado is UbicacionUi.Lista && radio != null) {
                Circle(
                    center = LatLng(estado.posicion.latitud, estado.posicion.longitud),
                    radius = radio.toDouble(),
                    fillColor = TecsupCyan.copy(alpha = if (modoOscuro) 0.25f else 0.15f),
                    strokeColor = if (modoOscuro) Color(0xFF38BDF8) else TecsupCyan,
                    strokeWidth = 3f
                )
            }

            // Marcador propio "Estás aquí" en rojo (por defecto)
            if (estado is UbicacionUi.Lista) {
                Marker(
                    state = rememberUpdatedMarkerState(
                        position = LatLng(estado.posicion.latitud, estado.posicion.longitud)
                    ),
                    title = "Estás aquí"
                )
            }

            // Marcadores de los lugares del campus:
            // El más cercano en verde (HUE_GREEN); los demás en celeste eléctrico (HUE_CYAN)
            lugares.forEach { item ->
                key(item.lugar.id) {
                    val markerState = rememberUpdatedMarkerState(
                        position = LatLng(item.lugar.latitud, item.lugar.longitud)
                    )

                    LaunchedEffect(seleccionado) {
                        if (item.lugar.id == seleccionado) {
                            markerState.showInfoWindow()
                        }
                    }

                    val esMasCercano = item.lugar.id == masCercano?.id
                    val color = if (esMasCercano) {
                        BitmapDescriptorFactory.HUE_GREEN
                    } else {
                        BitmapDescriptorFactory.HUE_CYAN
                    }

                    Marker(
                        state = markerState,
                        title = item.lugar.nombre,
                        snippet = item.lugar.categoria,
                        icon = BitmapDescriptorFactory.defaultMarker(color)
                    )
                }
            }
        }

        // Controles de zoom personalizados (+ y -) con diseño Tecsup (Celeste y Blanco / Modo Oscuro)
        Surface(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(end = 12.dp, top = 68.dp),
            shape = RoundedCornerShape(10.dp),
            color = if (modoOscuro) Color(0xFF1E293B) else Color.White,
            shadowElevation = 4.dp,
            border = BorderStroke(1.dp, if (modoOscuro) Color(0xFF334155) else TecsupBorder)
        ) {
            Column {
                IconButton(
                    onClick = {
                        scope.launch {
                            camara.animate(CameraUpdateFactory.zoomIn())
                        }
                    },
                    modifier = Modifier.size(38.dp)
                ) {
                    Text(
                        text = "+",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = if (modoOscuro) Color(0xFF38BDF8) else TecsupCyanDark
                    )
                }

                Box(
                    modifier = Modifier
                        .width(38.dp)
                        .height(1.dp)
                        .background(if (modoOscuro) Color(0xFF334155) else TecsupBorder)
                )

                IconButton(
                    onClick = {
                        scope.launch {
                            camara.animate(CameraUpdateFactory.zoomOut())
                        }
                    },
                    modifier = Modifier.size(38.dp)
                ) {
                    Text(
                        text = "−",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = if (modoOscuro) Color(0xFF38BDF8) else TecsupCyanDark
                    )
                }
            }
        }
    }
}
