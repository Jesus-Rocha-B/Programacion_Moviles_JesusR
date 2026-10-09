package com.rocha.saludplus.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Accessibility
import androidx.compose.material.icons.filled.ChildCare
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Female
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.rocha.saludplus.model.Especialidad
import com.rocha.saludplus.model.Medico

// BOTÓN PRINCIPAL

@Composable
fun BotonPrincipal(
    texto: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    habilitado: Boolean = true
) {
    Button(
        onClick = onClick,
        enabled = habilitado,
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF1877F2),
            contentColor = Color.White
        ),
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
    ) {
        Text(
            text = texto,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
    }
}

// BOTÓN SECUNDARIO

@Composable
fun BotonSecundario(
    texto: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick = onClick,
        shape = RoundedCornerShape(14.dp),
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
    ) {
        Text(
            text = texto,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1877F2)
        )
    }
}

// CAMPO DE TEXTO

@Composable
fun CampoTexto(
    valor: String,
    onValorCambia: (String) -> Unit,
    etiqueta: String,
    modifier: Modifier = Modifier,
    error: String? = null,
    esContrasena: Boolean = false,
    tipoTeclado: KeyboardType = KeyboardType.Text,
    placeholder: String? = null,
    iconoLeading: ImageVector? = null
) {
    var contrasenaVisible by remember { mutableStateOf(false) }

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        if (iconoLeading != null) {
            Surface(
                modifier = Modifier.size(52.dp),
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFFE6F2FF),
                border = BorderStroke(1.dp, Color(0xFFD6E4FF))
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = iconoLeading,
                        contentDescription = null,
                        tint = Color(0xFF1877F2),
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }

        OutlinedTextField(
            value = valor,
            onValueChange = onValorCambia,
            label = {
                Text(etiqueta)
            },
            placeholder = if (placeholder != null) {
                { Text(placeholder, color = Color(0xFF9E9E9E)) }
            } else {
                null
            },
            trailingIcon = if (esContrasena) {
                {
                    IconButton(onClick = { contrasenaVisible = !contrasenaVisible }) {
                        Icon(
                            imageVector = if (contrasenaVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = "Alternar visibilidad",
                            tint = Color(0xFF758A99)
                        )
                    }
                }
            } else {
                null
            },
            isError = error != null,
            supportingText = if (error != null) {
                {
                    Text(error)
                }
            } else {
                null
            },
            singleLine = true,
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedBorderColor = Color(0xFF1877F2),
                unfocusedBorderColor = Color(0xFFE0E0E0)
            ),
            visualTransformation = if (esContrasena && !contrasenaVisible) {
                PasswordVisualTransformation()
            } else {
                VisualTransformation.None
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = if (esContrasena) {
                    KeyboardType.Password
                } else {
                    tipoTeclado
                }
            ),
            modifier = Modifier.weight(1f)
        )
    }
}

// BARRA SUPERIOR

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BarraSuperior(
    titulo: String,
    onVolver: () -> Unit,
    acciones: @Composable RowScope.() -> Unit = {}
) {
    TopAppBar(
        title = {
            Text(
                text = titulo,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0F1E36)
            )
        },
        navigationIcon = {
            IconButton(onClick = onVolver) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Volver",
                    tint = Color(0xFF0F1E36)
                )
            }
        },
        actions = acciones
    )
}

// FILA DE DATOS

@Composable
fun FilaDato(
    etiqueta: String,
    valor: String,
    icono: ImageVector? = null
) {
    if (icono != null) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(42.dp),
                shape = RoundedCornerShape(10.dp),
                color = Color(0xFFEBF3FE)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icono,
                        contentDescription = null,
                        tint = Color(0xFF1877F2),
                        modifier = Modifier.size(22.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = etiqueta,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF758A99)
                )
                Text(
                    text = valor,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F1E36)
                )
            }
        }
    } else {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = etiqueta,
                color = Color(0xFF758A99)
            )

            Text(
                text = valor,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0F1E36)
            )
        }
    }
}

// MENSAJE VACÍO

@Composable
fun MensajeVacio(
    texto: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = texto,
            color = Color(0xFF758A99),
            textAlign = TextAlign.Center
        )
    }
}

// TARJETA DE ACCIÓN DEL HOME

@Composable
fun TarjetaAccion(
    titulo: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icono: ImageVector? = null,
    backgroundColor: Color = Color(0xFFE6F2FF),
    iconColor: Color = Color(0xFF1877F2)
) {
    Card(
        onClick = onClick,
        modifier = modifier.height(110.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (icono != null) {
                Icon(
                    imageVector = icono,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(30.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
            Text(
                text = titulo,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0F1E36),
                textAlign = TextAlign.Center
            )
        }
    }
}

// TARJETA DESTACADA DEL HOME

@Composable
fun TarjetaDestacada(
    especialidad: Especialidad,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val (icono, iconColor, bgColor) = when (especialidad.id) {
        1 -> Triple(Icons.Default.Person, Color(0xFF1877F2), Color(0xFFE6F2FF))
        2 -> Triple(Icons.Default.ChildCare, Color(0xFFF39C12), Color(0xFFFEF5E7))
        3 -> Triple(Icons.Default.Female, Color(0xFFE91E63), Color(0xFFFCE4EC))
        4 -> Triple(Icons.Default.Favorite, Color(0xFFE74C3C), Color(0xFFFDEDEC))
        5 -> Triple(Icons.Default.Face, Color(0xFFE67E22), Color(0xFFFBEEE6))
        6 -> Triple(Icons.Default.Accessibility, Color(0xFF2980B9), Color(0xFFEBF5FB))
        7 -> Triple(Icons.Default.Visibility, Color(0xFF16A085), Color(0xFFE8F8F5))
        else -> Triple(Icons.Default.Person, Color(0xFF1877F2), Color(0xFFE6F2FF))
    }

    Card(
        onClick = onClick,
        modifier = modifier
            .width(130.dp)
            .height(115.dp),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, Color(0xFFEAEAEA)),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Surface(
                modifier = Modifier.size(44.dp),
                shape = CircleShape,
                color = bgColor
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icono,
                        contentDescription = null,
                        tint = iconColor,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = especialidad.nombre,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0F1E36),
                textAlign = TextAlign.Center,
                maxLines = 1
            )
        }
    }
}

// TARJETA PARA LA LISTA DE ESPECIALIDADES

@Composable
fun TarjetaEspecialidad(
    especialidad: Especialidad,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val (icono, iconColor, bgColor) = when (especialidad.id) {
        1 -> Triple(Icons.Default.Person, Color(0xFF1877F2), Color(0xFFE6F2FF))
        2 -> Triple(Icons.Default.ChildCare, Color(0xFFF39C12), Color(0xFFFEF5E7))
        3 -> Triple(Icons.Default.Female, Color(0xFFE91E63), Color(0xFFFCE4EC))
        4 -> Triple(Icons.Default.Favorite, Color(0xFFE74C3C), Color(0xFFFDEDEC))
        5 -> Triple(Icons.Default.Face, Color(0xFFE67E22), Color(0xFFFBEEE6))
        6 -> Triple(Icons.Default.Accessibility, Color(0xFF2980B9), Color(0xFFEBF5FB))
        7 -> Triple(Icons.Default.Visibility, Color(0xFF16A085), Color(0xFFE8F8F5))
        else -> Triple(Icons.Default.MedicalServices, Color(0xFF1877F2), Color(0xFFE6F2FF))
    }

    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        border = BorderStroke(1.dp, Color(0xFFEFEFEF)),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(48.dp),
                shape = CircleShape,
                color = bgColor
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icono,
                        contentDescription = null,
                        tint = iconColor,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = especialidad.nombre,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F1E36)
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = especialidad.descripcion,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF758A99)
                )
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "Ver médicos",
                tint = Color(0xFF758A99)
            )
        }
    }
}

// TARJETA PARA LA LISTA DE MÉDICOS

@Composable
fun TarjetaMedico(
    medico: Medico,
    especialidad: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        border = BorderStroke(1.dp, Color(0xFFEFEFEF)),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = CircleShape,
                modifier = Modifier.size(56.dp),
                border = BorderStroke(2.dp, Color.White),
                shadowElevation = 2.dp,
                color = Color(0xFFE6F2FF)
            ) {
                if (medico.fotoResId != 0) {
                    Image(
                        painter = painterResource(id = medico.fotoResId),
                        contentDescription = medico.nombre,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = medico.nombre,
                            tint = Color(0xFF1877F2),
                            modifier = Modifier.size(36.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = medico.nombre,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F1E36)
                )

                Text(
                    text = especialidad,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF758A99)
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Calificación",
                        tint = Color(0xFFFFB800),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = medico.calificacion.toString(),
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F1E36)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "(124)",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF758A99)
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xFFE8F8F0)
                ) {
                    Text(
                        text = "Disponible hoy",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFF27AE60),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

// Tarjeta de una cita, se usa en Mis citas
@Composable
fun TarjetaCita(
    medicoNombre: String,
    especialidad: String,
    fecha: String,
    hora: String,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        border = BorderStroke(1.dp, Color(0xFFEFEFEF)),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(48.dp),
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFFE6F2FF)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = null,
                        tint = Color(0xFF1877F2),
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = medicoNombre,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F1E36)
                )
                Text(
                    text = especialidad,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF758A99)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "$fecha · $hora",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1877F2)
                )
            }
        }
    }
}

// Horario de la grilla, cambia de color cuando está seleccionado
@Composable
fun ChipHorario(
    hora: String,
    seleccionado: Boolean,
    ocupado: Boolean = false,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = { if (!ocupado) onClick() },
        enabled = !ocupado,
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = when {
                ocupado -> Color(0xFFFDEDEC) // Soft red
                seleccionado -> Color(0xFF1877F2)
                else -> Color(0xFFF6F8FA)
            },
            contentColor = when {
                ocupado -> Color(0xFFE74C3C) // Red text
                seleccionado -> Color.White
                else -> Color(0xFF0F1E36)
            }
        ),
        border = when {
            ocupado -> BorderStroke(1.dp, Color(0xFFF5B7B1))
            !seleccionado -> BorderStroke(1.dp, Color(0xFFE5E9EC))
            else -> null
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (ocupado) "$hora (Ocup.)" else hora,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold,
                color = when {
                    ocupado -> Color(0xFFE74C3C)
                    seleccionado -> Color.White
                    else -> Color(0xFF0F1E36)
                }
            )
        }
    }
}

// Día para la lista de fechas
@Composable
fun ChipDia(
    nombre: String,
    numero: String,
    seleccionado: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (seleccionado) {
                Color(0xFF1877F2)
            } else {
                Color.White
            },
            contentColor = if (seleccionado) {
                Color.White
            } else {
                Color(0xFF0F1E36)
            }
        ),
        border = if (!seleccionado) BorderStroke(1.dp, Color(0xFFE0E0E0)) else null
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = nombre,
                style = MaterialTheme.typography.bodySmall,
                color = if (seleccionado) Color.White else Color(0xFF758A99)
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = numero,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = if (seleccionado) Color.White else Color(0xFF0F1E36)
            )
        }
    }
}

// Cambia "2026-10-12" a "12/10/2026"
fun formatearFecha(fecha: String): String {
    return fecha.split("-").reversed().joinToString("/")
}
