package com.rocha.saludplus.ui.auth
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.rocha.saludplus.ui.components.*

@Composable
fun TerminosScreen(navController: NavController) {
    Scaffold(
        topBar = { BarraSuperior("Términos y condiciones", onVolver = { navController.popBackStack() }) }
    ) { padding ->
        // Scroll porque el texto es largo
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text("1. Uso de la aplicación", style = MaterialTheme.typography.titleMedium)
            Text("La app permite a los pacientes de la Clínica SaludPlus agendar citas médicas con las especialidades disponibles.")
            Spacer(modifier = Modifier.height(16.dp))
            Text("2. Datos personales", style = MaterialTheme.typography.titleMedium)
            Text("Los datos ingresados se usan solo para registrar y gestionar tus citas dentro de la app.")
            Spacer(modifier = Modifier.height(16.dp))
            Text("3. Citas", style = MaterialTheme.typography.titleMedium)
            Text("Cada horario puede ser reservado por un solo paciente. Una cita se puede cancelar desde su detalle.")
            Spacer(modifier = Modifier.height(16.dp))
            Text("4. Información en memoria", style = MaterialTheme.typography.titleMedium)
            Text("Esta versión no guarda información de forma permanente, los datos se pierden al cerrar la app.")
            Spacer(modifier = Modifier.height(24.dp))
            BotonPrincipal(texto = "Entendido", onClick = { navController.popBackStack() })
        }
    }
}