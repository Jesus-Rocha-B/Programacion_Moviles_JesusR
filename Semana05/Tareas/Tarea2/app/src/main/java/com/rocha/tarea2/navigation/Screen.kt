package com.rocha.tarea2.navigation

// Rutas de la app, con las que llevan classId como parámetro
sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Reservations : Screen("reservations")
    object Routines : Screen("routines")
    object Profile : Screen("profile")

    // Ruta con parámetro: classId
    object ClassDetail : Screen("classDetail/{classId}") {
        fun createRoute(classId: Int) = "classDetail/$classId"
    }

    // Ruta con parámetro: reutiliza el mismo classId que ClassDetail
    object Confirmation : Screen("confirmation/{classId}") {
        fun createRoute(classId: Int) = "confirmation/$classId"
    }
}