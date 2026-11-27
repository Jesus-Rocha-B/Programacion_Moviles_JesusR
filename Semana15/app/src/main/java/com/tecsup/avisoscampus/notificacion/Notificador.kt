package com.tecsup.avisoscampus.notificacion

import android.content.Context
import com.tecsup.avisoscampus.model.Aviso

// [G] GUIADO: completar en los pasos 1, 2, 3 y 4
object Notificador {
    const val CANAL = "avisos"

    fun crearCanal(context: Context) {
        // TODO Paso 1: si SDK_INT >= O, crear NotificationChannel y registrarlo
    }

    fun tienePermiso(context: Context): Boolean {
        // TODO Paso 2: si SDK_INT < 33 devuelve true; si no, revisar con ContextCompat.checkSelfPermission
        return false
    }

    fun mostrar(context: Context, aviso: Aviso): Boolean {
        // TODO Paso 3 y 4: verificar permiso, construir NotificationCompat.Builder con PendingIntent y llamar notify()
        return false
    }
}