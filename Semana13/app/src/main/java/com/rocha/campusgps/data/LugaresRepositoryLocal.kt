package com.rocha.campusgps.data

import com.rocha.campusgps.model.Lugar

class LugaresRepositoryLocal : LugaresRepository {
    override fun obtenerLugares(): List<Lugar> = listOf(
        Lugar(1, "Patio", "Eventos", -12.04412, -76.95282),
        Lugar(2, "Pabellón 7 - Tecnología Digital", "Estudio", -12.04444, -76.95305),
        Lugar(3, "Pabellón 15 - Tecnología Digital", "Estudio", -12.04371, -76.95321),
        Lugar(4, "Cafetería Gourmet", "Comida", -12.04425, -76.95244),
        Lugar(5, "Cafetería Central (Comedor)", "Comida", -12.04495, -76.95244),
        Lugar(6, "Polideportivo - Fútbol Principal", "Deportes", -12.04389, -76.95376),
        Lugar(7, "Polideportivo - Cancha Sintética", "Deportes", -12.04356, -76.95398),
        Lugar(8, "Polideportivo - Vóley / Básquet", "Deportes", -12.04357, -76.95361),
        Lugar(9, "Auditorio", "Eventos", -12.04480, -76.952715),
        Lugar(10, "Atención al Estudiante (Pabellón 1)", "Servicios", -12.04512, -76.95288)
    )
}
