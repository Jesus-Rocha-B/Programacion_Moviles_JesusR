package com.tecsup.tecsupstore.ui.screens
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DrawerMenu(
    onNavigate: (String) -> Unit
) {
    ModalDrawerSheet {
        Text(
            text = "Menu Principal",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(16.dp)
        )
    }
}
