package com.rocha.saludplus.ui.auth

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.rocha.saludplus.R
import com.rocha.saludplus.navigation.Rutas
import com.rocha.saludplus.ui.components.BotonPrincipal

@Composable
fun ClinicaLogoIcon(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.size(64.dp),
        shape = RoundedCornerShape(20.dp),
        color = Color(0xFFEBF3FF)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Canvas(modifier = Modifier.size(44.dp)) {
                val w = size.width
                val h = size.height
                val barW = w * 0.36f
                val corner = CornerRadius(8f, 8f)

                // Vertical bar
                drawRoundRect(
                    color = Color(0xFF1877F2),
                    topLeft = Offset((w - barW) / 2f, 0f),
                    size = Size(barW, h),
                    cornerRadius = corner
                )
                // Horizontal bar
                drawRoundRect(
                    color = Color(0xFF1877F2),
                    topLeft = Offset(0f, (h - barW) / 2f),
                    size = Size(w, barW),
                    cornerRadius = corner
                )
            }
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
fun SplashScreen(navController: NavController) {
    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // Logotipo superior estilizado con el logo exacto (cruz + corazón)
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                ClinicaLogoIcon()

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Clínica\nSaludPlus",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F1E36),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Tu salud, nuestra prioridad",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF758A99),
                    textAlign = TextAlign.Center
                )
            }

            // Ilustración central usando img_1.png SIN BORDES, un poco más grande y alta
            Image(
                painter = painterResource(id = R.drawable.doctor_splash),
                contentDescription = "Ilustración Doctor",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(330.dp)
            )

            // Acciones inferiores: Botón "Comenzar" y "Ya tengo una cuenta"
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                BotonPrincipal(
                    texto = "Comenzar",
                    onClick = { navController.navigate(Rutas.REGISTRO) }
                )

                Spacer(modifier = Modifier.height(8.dp))

                TextButton(onClick = { navController.navigate(Rutas.LOGIN) }) {
                    Text(
                        text = "Ya tengo una cuenta",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1877F2)
                    )
                }
            }
        }
    }
}
