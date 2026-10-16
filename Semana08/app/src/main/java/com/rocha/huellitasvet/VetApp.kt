package com.rocha.huellitasvet
import android.app.Application
import com.rocha.huellitasvet.data.local.DatosIniciales
import com.rocha.huellitasvet.data.local.VetDatabase
import com.rocha.huellitasvet.data.repository.VetRepository
import com.rocha.huellitasvet.data.repository.VetRepositoryRoom
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class VetApp : Application() {
    lateinit var db: VetDatabase
        private set
    lateinit var repository: VetRepository
        private set
    override fun onCreate() {
        super.onCreate()
        db = VetDatabase.obtener(this)
        repository = VetRepositoryRoom(db)
        CoroutineScope(Dispatchers.IO).launch {
            DatosIniciales.sembrar(db)
        }
    }
}