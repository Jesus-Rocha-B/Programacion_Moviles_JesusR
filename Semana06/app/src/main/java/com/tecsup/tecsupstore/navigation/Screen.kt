package com.tecsup.tecsupstore.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Registro : Screen("registro")
    object List : Screen("list")
    object Profile : Screen("profile")
    object Detail : Screen("detail/{productoId}") {
        fun createRoute(productoId: Int) = "detail/$productoId"
    }
}