package com.rocha.carrito
data class Producto(
    val nombre: String,
    val precio: Double,
    var cantidad: Int
)
fun main() {
    println("=========================================")
    println("   CARRITO DE COMPRAS - TIENDA TECSUP    ")
    println("=========================================")
    val nombreCliente = "Jesús Rocha"
    val carrito = mutableListOf<Producto>()
    // Mencionamos al cliente
    println("Cliente: $nombreCliente")
    println("")
    // Añadir productos al carrito
    carrito.add(Producto("Laptop HP", 2500.0,1))
    carrito.add(Producto("Mouse Logitech", 45.5, 2))
    carrito.add(Producto("Mouse Rayzer", 99.9, 3))
    carrito.add(Producto("Teclado Redragon", 199.5, 10))
    // Un bucle for para mencionar los productos agregados al carrito
    for (producto in carrito) {
        println("Producto agregado: ${producto.nombre}")
    }
}