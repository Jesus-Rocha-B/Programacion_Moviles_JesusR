package com.rocha.campusgps.data

import android.annotation.SuppressLint
import android.content.Context
import android.os.Looper
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.rocha.campusgps.model.Posicion
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class UbicacionRepositoryGoogle(context: Context) : UbicacionRepository {

    private val appContext = context.applicationContext
    private val cliente = LocationServices.getFusedLocationProviderClient(appContext)

    override fun playServicesDisponible(): Boolean {
        val disponibilidad = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(appContext)
        return disponibilidad == ConnectionResult.SUCCESS
    }

    @SuppressLint("MissingPermission")
    override suspend fun obtenerPosicion(): Result<Posicion> = runCatching {
        val cts = CancellationTokenSource()
        val loc = cliente.getCurrentLocation(
            Priority.PRIORITY_HIGH_ACCURACY,
            cts.token
        ).await()
            ?: throw IllegalStateException("No se pudo obtener la ubicación. Activa el GPS e intenta de nuevo")

        Posicion(latitud = loc.latitude, longitud = loc.longitude)
    }

    @SuppressLint("MissingPermission")
    override fun obtenerPosicionesEnVivo(): Flow<Posicion> = callbackFlow {
        val request = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 3000L)
            .setMinUpdateIntervalMillis(1500L)
            .setMinUpdateDistanceMeters(2f)
            .build()

        val callback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                result.lastLocation?.let { loc ->
                    trySend(Posicion(latitud = loc.latitude, longitud = loc.longitude))
                }
            }
        }

        cliente.requestLocationUpdates(request, callback, Looper.getMainLooper())

        awaitClose {
            cliente.removeLocationUpdates(callback)
        }
    }
}
