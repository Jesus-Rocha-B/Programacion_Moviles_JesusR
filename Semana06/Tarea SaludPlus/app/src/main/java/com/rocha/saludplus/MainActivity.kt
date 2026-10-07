package com.rocha.saludplus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.rocha.saludplus.navigation.AppNavigation
import com.rocha.saludplus.ui.theme.SaludPlusTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SaludPlusTheme {
                AppNavigation()
            }
        }
    }
}