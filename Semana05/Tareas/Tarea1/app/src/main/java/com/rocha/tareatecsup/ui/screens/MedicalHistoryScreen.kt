package com.rocha.tareatecsup.ui.screens
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MedicalHistoryScreen() {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Historial médico", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))
        Text("Aún no tienes historial médico registrado.", style = MaterialTheme.typography.bodyMedium)
    }
}