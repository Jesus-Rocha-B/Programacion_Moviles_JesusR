package com.tecsup.tecsupstore.ui.screens
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.RadioButtonUnchecked
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tecsup.tecsupstore.navigation.Screen

@Composable
fun DrawerMenu(
    currentRoute: String?,
    onNavigate: (String) -> Unit
) {
    ModalDrawerSheet {
        // Header con Avatar usando colores por defecto del tema
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "MR",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = "Maria Rojas",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "maria@tecsup.edu.pe",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline
                )
            }
        }

        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
        Spacer(modifier = Modifier.height(16.dp))
        NavigationDrawerItem(
            label = { Text("Inicio") },
            selected = currentRoute == Screen.Home.route,
            icon = { Icon(Icons.Outlined.RadioButtonUnchecked, contentDescription = null) },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
            onClick = { onNavigate(Screen.Home.route) }
        )

        NavigationDrawerItem(
            label = { Text("Registro / Carrito") },
            selected = currentRoute == Screen.Registro.route,
            icon = { Icon(Icons.Outlined.RadioButtonUnchecked, contentDescription = null) },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
            onClick = { onNavigate(Screen.Registro.route) }
        )

        NavigationDrawerItem(
            label = { Text("Lista de Elementos") },
            selected = currentRoute == Screen.List.route,
            icon = { Icon(Icons.Outlined.RadioButtonUnchecked, contentDescription = null) },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
            onClick = { onNavigate(Screen.List.route) }
        )

        NavigationDrawerItem(
            label = { Text("Mi Perfil") },
            selected = currentRoute == Screen.Profile.route,
            icon = { Icon(Icons.Outlined.RadioButtonUnchecked, contentDescription = null) },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
            onClick = { onNavigate(Screen.Profile.route) }
        )
    }
}