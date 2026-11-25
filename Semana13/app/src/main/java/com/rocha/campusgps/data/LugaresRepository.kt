package com.rocha.campusgps.data

import com.rocha.campusgps.model.Lugar

interface LugaresRepository {
    fun obtenerLugares(): List<Lugar>
}
