package com.rocha.saludplus.ui.auth
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.rocha.saludplus.navigation.Rutas
import com.rocha.saludplus.ui.components.*

@Composable
fun SplashScreen(navController: NavController) {
    Scaffold { padding ->
        // El contenido se centra
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Clínica SaludPlus",
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Tu salud, nuestra prioridad",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(48.dp))
            // Botón para acción principal (crear cuenta)
            BotonPrincipal(
                texto = "Comenzar",
                onClick = { navController.navigate(Rutas.REGISTRO) }
            )
            Spacer(modifier = Modifier.height(12.dp))
            // Botón para acción secundaria (ya tengo cuenta)
            BotonSecundario(
                texto = "Ya tengo una cuenta",
                onClick = { navController.navigate(Rutas.LOGIN) }
            )
        }
    }
}