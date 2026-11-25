package com.rocha.campusgps.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.rocha.campusgps.model.TipoError
import com.rocha.campusgps.model.UbicacionUi
import com.rocha.campusgps.ui.theme.TecsupBorder
import com.rocha.campusgps.ui.theme.TecsupCyan
import com.rocha.campusgps.ui.theme.TecsupCyanDark
import com.rocha.campusgps.ui.theme.TecsupTextPrimary
import com.rocha.campusgps.ui.theme.TecsupTextSecondary

@Composable
fun MensajeError(
    error: UbicacionUi.Error,
    onReintentar: () -> Unit,
    onAbrirAjustes: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        color = Color.White,
        border = BorderStroke(1.dp, TecsupBorder),
        shadowElevation = 1.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = error.mensaje,
                color = TecsupTextPrimary,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )

            when (error.tipo) {
                TipoError.SIN_PERMISO -> {
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedButton(
                        onClick = onAbrirAjustes,
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, TecsupCyan),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = TecsupCyanDark
                        )
                    ) {
                        Text("Abrir ajustes", fontWeight = FontWeight.SemiBold)
                    }
                }

                TipoError.GPS_APAGADO, TipoError.OTRO -> {
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedButton(
                        onClick = onReintentar,
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, TecsupCyan),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = TecsupCyanDark
                        )
                    ) {
                        Text("Reintentar", fontWeight = FontWeight.SemiBold)
                    }
                }

                TipoError.SIN_PLAY_SERVICES -> {
                    // Sin botón de acción
                }
            }
        }
    }
}
