package com.rocha.tarea2
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.rocha.tarea2.navigation.AppNavigation
import com.rocha.tarea2.ui.theme.Tarea2Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Tarea2Theme {
                AppNavigation()
            }
        }
    }
}