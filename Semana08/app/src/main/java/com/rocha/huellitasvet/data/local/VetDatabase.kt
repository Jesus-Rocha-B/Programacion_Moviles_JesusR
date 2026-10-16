package com.rocha.huellitasvet.data.local
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.rocha.huellitasvet.data.local.dao.CitaDao
import com.rocha.huellitasvet.data.local.dao.MascotaDao
import com.rocha.huellitasvet.data.local.dao.ServicioDao
import com.rocha.huellitasvet.data.local.dao.UsuarioDao
import com.rocha.huellitasvet.data.local.entity.Cita
import com.rocha.huellitasvet.data.local.entity.Mascota
import com.rocha.huellitasvet.data.local.entity.Servicio
import com.rocha.huellitasvet.data.local.entity.Usuario

@Database(
    entities = [Servicio::class, Usuario::class, Mascota::class, Cita::class],
    version = 1,
    exportSchema = false
)
abstract class VetDatabase : RoomDatabase() {
    abstract fun servicioDao(): ServicioDao
    abstract fun usuarioDao(): UsuarioDao
    abstract fun mascotaDao(): MascotaDao
    abstract fun citaDao(): CitaDao
    companion object {
        @Volatile
        private var INSTANCE: VetDatabase? = null
        // Singleton: una sola instancia para toda la app
        fun obtener(context: Context): VetDatabase = INSTANCE ?: synchronized(this) {
            INSTANCE ?: Room.databaseBuilder(
                context.applicationContext,
                VetDatabase::class.java,
                "huellitasvet.db"
            ).build().also { INSTANCE = it }
        }
    }
}