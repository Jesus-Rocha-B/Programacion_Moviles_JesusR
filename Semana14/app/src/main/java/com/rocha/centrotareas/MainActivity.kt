package com.rocha.centrotareas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.rocha.centrotareas.data.TareasRepositoryLocal
import com.rocha.centrotareas.ui.PantallaTareas
import com.rocha.centrotareas.ui.theme.CentroTareasTheme
import com.rocha.centrotareas.viewmodel.TareasViewModel
import com.rocha.centrotareas.viewmodel.TareasViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CentroTareasTheme {
                val repo = TareasRepositoryLocal()
                val vm: TareasViewModel = viewModel(factory = TareasViewModelFactory(repo))
                PantallaTareas(vm = vm)
            }
        }
    }
}
