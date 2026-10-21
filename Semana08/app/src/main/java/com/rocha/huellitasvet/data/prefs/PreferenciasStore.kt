package com.rocha.huellitasvet.data.prefs
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Se declara una sola vez, a nivel de archivo
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "ajustes")

class PreferenciasStore(private val context: Context) {

    private object Claves {
        val perfilActivo = stringPreferencesKey("perfilActivo")
        val usuarioId = intPreferencesKey("usuarioId")
        val modoOscuro = booleanPreferencesKey("modoOscuro")
        val notificaciones = booleanPreferencesKey("notificaciones")
        val aceptarCitas = booleanPreferencesKey("aceptarCitas")
    }

    // Lectura: un Flow por cada preferencia

    /** "CLIENTE", "ADMIN" o "" si no hay sesión guardada */
    val perfilActivo: Flow<String> =
        context.dataStore.data.map { it[Claves.perfilActivo] ?: "" }

    val usuarioId: Flow<Int> =
        context.dataStore.data.map { it[Claves.usuarioId] ?: 0 }

    val modoOscuro: Flow<Boolean> =
        context.dataStore.data.map { it[Claves.modoOscuro] ?: false }

    val notificaciones: Flow<Boolean> =
        context.dataStore.data.map { it[Claves.notificaciones] ?: true }

    val aceptarCitas: Flow<Boolean> =
        context.dataStore.data.map { it[Claves.aceptarCitas] ?: true }

    // Escritura: funciones suspend

    suspend fun guardarSesion(rol: String, usuarioId: Int) {
        context.dataStore.edit {
            it[Claves.perfilActivo] = rol
            it[Claves.usuarioId] = usuarioId
        }
    }

    suspend fun guardarModoOscuro(b: Boolean) {
        context.dataStore.edit { it[Claves.modoOscuro] = b }
    }

    suspend fun guardarNotificaciones(b: Boolean) {
        context.dataStore.edit { it[Claves.notificaciones] = b }
    }

    suspend fun guardarAceptarCitas(b: Boolean) {
        context.dataStore.edit { it[Claves.aceptarCitas] = b }
    }

    suspend fun cerrarSesion() {
        context.dataStore.edit {
            it.remove(Claves.perfilActivo)
            it.remove(Claves.usuarioId)
        }
    }
}