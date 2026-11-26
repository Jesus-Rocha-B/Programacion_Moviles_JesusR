package com.tecsup.murocampus.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.tecsup.murocampus.ui.theme.AzulTecsup
import com.tecsup.murocampus.ui.theme.ErrorRojo
import com.tecsup.murocampus.ui.theme.FondoError
import com.tecsup.murocampus.ui.theme.GrisBorde
import com.tecsup.murocampus.ui.theme.TextoOscuro
import com.tecsup.murocampus.ui.theme.TextoSecundario

// [E] ENTREGADO: Componente TarjetaPost
@Composable
fun TarjetaPost(
    userId: Int,
    titulo: String,
    cuerpo: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, GrisBorde),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Box(
                modifier = Modifier
                    .background(Color(0xFFE3F2FD), RoundedCornerShape(50))
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "Usuario " + userId,
                    style = MaterialTheme.typography.labelSmall,
                    color = AzulTecsup,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = titulo,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = TextoOscuro
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = cuerpo,
                style = MaterialTheme.typography.bodyMedium,
                color = TextoSecundario,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

// [E] ENTREGADO: Componente Cargando
@Composable
fun Cargando(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(color = AzulTecsup)
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Cargando publicaciones...",
            style = MaterialTheme.typography.bodyMedium,
            color = TextoSecundario
        )
    }
}

// [E] ENTREGADO: Componente ErrorConReintento
@Composable
fun ErrorConReintento(
    mensaje: String,
    onReintentar: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth().padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = FondoError),
        border = BorderStroke(1.dp, Color(0xFFFCA5A5))
    ) {
        Column(
            modifier = Modifier.padding(20.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = mensaje,
                color = ErrorRojo,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(14.dp))
            Button(
                onClick = onReintentar,
                colors = ButtonDefaults.buttonColors(containerColor = AzulTecsup),
                shape = RoundedCornerShape(50)
            ) {
                Text(text = "Reintentar", fontWeight = FontWeight.Bold)
            }
        }
    }
}