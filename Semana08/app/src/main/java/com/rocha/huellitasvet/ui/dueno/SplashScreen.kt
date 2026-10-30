package com.rocha.huellitasvet.ui.dueno

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.rocha.huellitasvet.model.Especie
import com.rocha.huellitasvet.model.Rol
import com.rocha.huellitasvet.ui.components.MascotaAvatar
import com.rocha.huellitasvet.ui.components.VetButton
import com.rocha.huellitasvet.viewmodel.SesionViewModel

@Composable
fun SplashScreen(vm: SesionViewModel, onDestino: (Rol?) -> Unit) {
    LaunchedEffect(Unit) {
        val rol = vm.restaurarSesion()
        if (rol != null) {
            onDestino(rol)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.size(96.dp)
            ) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Icon(
                        Icons.Filled.Pets,
                        contentDescription = "Logo",
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(52.dp)
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            Text(
                text = "HuellitasVet",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "Cuidamos a tu mejor amigo",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.height(36.dp))

            // Fila de 3 avatares de mascotas
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                MascotaAvatar(especie = Especie.PERRO.name, tamano = 64.dp)
                MascotaAvatar(especie = Especie.GATO.name, tamano = 64.dp)
                MascotaAvatar(especie = Especie.OTRO.name, tamano = 64.dp)
            }

            Spacer(Modifier.height(48.dp))

            VetButton(
                texto = "Comenzar",
                onClick = { onDestino(null) }
            )

            Spacer(Modifier.height(12.dp))

            Text(
                text = "Ya tengo una cuenta",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { onDestino(null) }
            )
        }
    }
}