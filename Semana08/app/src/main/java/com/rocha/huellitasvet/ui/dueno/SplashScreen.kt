package com.rocha.huellitasvet.ui.dueno
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.rocha.huellitasvet.model.Rol
import com.rocha.huellitasvet.viewmodel.SesionViewModel
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(vm: SesionViewModel, onDestino: (Rol?) -> Unit) {
    LaunchedEffect(Unit) {
        val rol = vm.restaurarSesion()
        delay(1200)
        onDestino(rol) // null = sin sesión guardada -> login
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "HuellitasVet",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}