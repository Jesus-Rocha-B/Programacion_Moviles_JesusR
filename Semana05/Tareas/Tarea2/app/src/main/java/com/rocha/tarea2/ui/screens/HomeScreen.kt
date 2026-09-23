package com.rocha.tarea2.ui.screens

// Modelo de una clase de gimnasio
data class ClassItem(
    val id: Int,
    val name: String,
    val time: String,
    val room: String,
    val duration: String,
    val description: String,
    val availableSpots: Int,
    val totalSpots: Int
)

// Modelo de una reserva ya confirmada
data class Reservation(
    val classItem: ClassItem,
    val dateLabel: String,
    val status: String // "Confirmada" o "Completada"
)

// Datos de ejemplo: las 3 clases mínimas que piden los requisitos
val sampleClasses = listOf(
    ClassItem(
        id = 1,
        name = "Yoga funcional",
        time = "7:00 am",
        room = "Sala 2",
        duration = "50 min",
        description = "Sesión de yoga enfocada en movilidad y equilibrio.",
        availableSpots = 6,
        totalSpots = 12
    ),
    ClassItem(
        id = 2,
        name = "Cross Training",
        time = "6:00 pm",
        room = "Sala 1",
        duration = "45 min",
        description = "Entrenamiento funcional de alta intensidad. Cupos limitados.",
        availableSpots = 8,
        totalSpots = 12
    ),
    ClassItem(
        id = 3,
        name = "Spinning",
        time = "7:30 pm",
        room = "Sala 3",
        duration = "40 min",
        description = "Rutina de ciclismo indoor con cambios de ritmo.",
        availableSpots = 4,
        totalSpots = 12
    )
)

// Datos de ejemplo: reservas ya hechas por el usuario
val sampleReservations = listOf(
    Reservation(
        classItem = sampleClasses[1], // Cross Training
        dateLabel = "Hoy, 6:00 pm",
        status = "Confirmada"
    ),
    Reservation(
        classItem = sampleClasses[0], // Yoga funcional
        dateLabel = "Ayer, 7:00 am",
        status = "Completada"
    )
)