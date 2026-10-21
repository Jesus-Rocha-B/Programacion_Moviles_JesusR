package com.rocha.huellitasvet
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.rocha.huellitasvet.model.Rol
import com.rocha.huellitasvet.ui.navigation.NavVet
import com.rocha.huellitasvet.ui.theme.VetTheme
import com.rocha.huellitasvet.viewmodel.SesionViewModel
import com.rocha.huellitasvet.viewmodel.VetViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val app = application as VetApp
        val factory = VetViewModelFactory(app.repository, app.prefs)

        setContent {
            val sesionVm: SesionViewModel = viewModel(factory = factory)
            val oscuro by sesionVm.modoOscuro.collectAsState()
            val usuario by sesionVm.usuarioActual.collectAsState()

            VetTheme(rol = usuario?.let { Rol.valueOf(it.rol) }, oscuro = oscuro) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Box(modifier = Modifier.safeDrawingPadding()) {
                        NavVet(sesionVm)
                    }
                }
            }
        }
    }
}