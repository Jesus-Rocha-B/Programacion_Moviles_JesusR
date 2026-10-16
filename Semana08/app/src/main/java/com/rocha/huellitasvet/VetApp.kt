package com.rocha.huellitasvet
import android.app.Application
import com.rocha.huellitasvet.data.local.DatosIniciales
import com.rocha.huellitasvet.data.local.VetDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class VetApp : Application() {
    lateinit var db: VetDatabase
        private set

    override fun onCreate() {
        super.onCreate()
        db = VetDatabase.obtener(this)

        // Siembra los datos de prueba en segundo plano (solo la primera vez)
        CoroutineScope(Dispatchers.IO).launch {
            DatosIniciales.sembrar(db)
        }
    }
}