package com.rocha.huellitasvet.ui.dueno

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.rocha.huellitasvet.R
import com.rocha.huellitasvet.model.Rol
import com.rocha.huellitasvet.ui.components.VetButton
import com.rocha.huellitasvet.ui.components.VetTextField
import com.rocha.huellitasvet.ui.theme.VetTheme
import com.rocha.huellitasvet.viewmodel.SesionViewModel

@Composable
fun LoginScreen(
    vm: SesionViewModel,
    onRegistrarse: () -> Unit = {},
    onLoginExitoso: (Rol) -> Unit
) {
    var rol by remember { mutableStateOf(Rol.CLIENTE) }
    var correo by remember { mutableStateOf("") }
    var clave by remember { mutableStateOf("") }

    val error by vm.error.collectAsState()
    val usuario by vm.usuarioActual.collectAsState()
    val oscuro by vm.modoOscuro.collectAsState()

    LaunchedEffect(usuario) {
        usuario?.let { onLoginExitoso(Rol.valueOf(it.rol)) }
    }

    VetTheme(rol = rol, oscuro = oscuro) {
        val duenoBgColor by androidx.compose.animation.animateColorAsState(
            targetValue = if (rol == Rol.CLIENTE) MaterialTheme.colorScheme.primary else Color.Transparent,
            animationSpec = androidx.compose.animation.core.spring(dampingRatio = androidx.compose.animation.core.Spring.DampingRatioNoBouncy, stiffness = androidx.compose.animation.core.Spring.StiffnessMedium),
            label = "duenoBg"
        )
        val vetBgColor by androidx.compose.animation.animateColorAsState(
            targetValue = if (rol == Rol.ADMIN) MaterialTheme.colorScheme.primary else Color.Transparent,
            animationSpec = androidx.compose.animation.core.spring(dampingRatio = androidx.compose.animation.core.Spring.DampingRatioNoBouncy, stiffness = androidx.compose.animation.core.Spring.StiffnessMedium),
            label = "vetBg"
        )
        val duenoTextColor by androidx.compose.animation.animateColorAsState(
            targetValue = if (rol == Rol.CLIENTE) MaterialTheme.colorScheme.onPrimary else Color(0xFF475569),
            animationSpec = androidx.compose.animation.core.tween(250),
            label = "duenoText"
        )
        val vetTextColor by androidx.compose.animation.animateColorAsState(
            targetValue = if (rol == Rol.ADMIN) MaterialTheme.colorScheme.onPrimary else Color(0xFF475569),
            animationSpec = androidx.compose.animation.core.tween(250),
            label = "vetText"
        )

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(top = 40.dp, start = 24.dp, end = 24.dp, bottom = 24.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "Bienvenido",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "Inicia sesión para continuar",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(Modifier.height(24.dp))

                // Selector de Rol estilo tarjeta
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFE2E8F0)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .clip(RoundedCornerShape(12.dp))
                                .background(duenoBgColor)
                                .clickable {
                                    rol = Rol.CLIENTE
                                    vm.limpiarError()
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Dueño",
                                fontWeight = FontWeight.Bold,
                                color = duenoTextColor
                            )
                        }

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .clip(RoundedCornerShape(12.dp))
                                .background(vetBgColor)
                                .clickable {
                                    rol = Rol.ADMIN
                                    vm.limpiarError()
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Veterinario",
                                fontWeight = FontWeight.Bold,
                                color = vetTextColor
                            )
                        }
                    }
                }

                Spacer(Modifier.height(24.dp))

                AnimatedContent(
                    targetState = rol,
                    transitionSpec = {
                        if (targetState == Rol.ADMIN) {
                            (slideInHorizontally(animationSpec = tween(350, easing = FastOutSlowInEasing)) { width -> width / 3 } +
                                    fadeIn(animationSpec = tween(350))) togetherWith
                            (slideOutHorizontally(animationSpec = tween(350, easing = FastOutSlowInEasing)) { width -> -width / 3 } +
                                    fadeOut(animationSpec = tween(250)))
                        } else {
                            (slideInHorizontally(animationSpec = tween(350, easing = FastOutSlowInEasing)) { width -> -width / 3 } +
                                    fadeIn(animationSpec = tween(350))) togetherWith
                            (slideOutHorizontally(animationSpec = tween(350, easing = FastOutSlowInEasing)) { width -> width / 3 } +
                                    fadeOut(animationSpec = tween(250)))
                        }
                    },
                    label = "loginForm",
                    modifier = Modifier.fillMaxWidth()
                ) { targetRol ->
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.Start
                    ) {
                        VetTextField(
                            value = correo,
                            onValueChange = { correo = it; vm.limpiarError() },
                            label = if (targetRol == Rol.CLIENTE) "Correo electrónico" else "Correo electrónico",
                            keyboardType = KeyboardType.Email,
                            leadingIcon = {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_input_email),
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        )
                        Spacer(Modifier.height(12.dp))
                        VetTextField(
                            value = clave,
                            onValueChange = { clave = it; vm.limpiarError() },
                            label = "Contraseña",
                            esClave = true,
                            leadingIcon = {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_input_lock),
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        )
                    }
                }

                Spacer(Modifier.height(8.dp))

                Text(
                    text = "¿Olvidaste tu contraseña?",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { }
                        .padding(vertical = 4.dp),
                    textAlign = TextAlign.End
                )

                error?.let {
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Spacer(Modifier.height(24.dp))

                VetButton(
                    texto = "Ingresar",
                    onClick = { vm.login(correo, clave, rol) }
                )

                Spacer(Modifier.height(16.dp))

                Text(
                    text = "¿No tienes cuenta? Regístrate",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onRegistrarse() },
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}