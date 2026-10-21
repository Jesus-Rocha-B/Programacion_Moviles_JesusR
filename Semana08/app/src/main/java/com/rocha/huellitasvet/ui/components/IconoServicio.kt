package com.rocha.huellitasvet.ui.components
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.ui.graphics.vector.ImageVector

/** El campo Servicio.icono guarda: heart, syringe o scissors. */
fun iconoDeServicio(icono: String): ImageVector = when (icono) {
    "syringe" -> Icons.Filled.MedicalServices
    "scissors" -> Icons.Filled.ContentCut
    else -> Icons.Filled.Favorite
}