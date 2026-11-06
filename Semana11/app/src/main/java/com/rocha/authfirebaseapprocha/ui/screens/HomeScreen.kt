package com.rocha.authfirebaseapprocha.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth

@Composable
fun HomeScreen(
    onLogout: () -> Unit
) {
    val auth = FirebaseAuth.getInstance()
    val email = auth.currentUser?.email ?: "Usuario"

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Bienvenido/a 🎉",
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Sesión iniciada como: $email")
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = {
                    auth.signOut()
                    onLogout()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Cerrar sesión")
            }
        }
    }
}
