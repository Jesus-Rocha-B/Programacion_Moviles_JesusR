package com.rocha.navlab.navigation
// Clase cerrada (solo existe las pantallas que se declaran aquí) y el constructor de la clase
sealed class Screen(val route: String) {
    object Login: Screen("login")
    object Home: Screen("home")
    object List: Screen("list")
    object Profile: Screen("profile")
    // Ruta con parametros
    object Detail: Screen("detail/{itemId}") {
        fun createRoute(itemId: Int) = "detail/$itemId"
    }
}