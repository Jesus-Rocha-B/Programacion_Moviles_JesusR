package com.rocha.carrito
data class Producto(
    val nombre: String,
    val precio: Double,
    var cantidad: Int
)
// Función para calcular el subtotal de los productos en el carrito
fun calcularSubtotal(productos: List<Producto>): Double {
    // La variable comienza desde 0 (es var por que va a cambiar su valor)
    var subtotal = 0.0
    for (p in productos) {
        subtotal += p.precio * p.cantidad
    }
    return subtotal
}
// Función para calcular el igv
fun calcularIGV(subtotal: Double): Double {
    var igv = subtotal * 0.18
    return igv
}
// Función para calcular el total del carrito
fun calcularTotal(subtotal: Double, igv: Double): Double {
    var total = subtotal + igv
    return total
}

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
    val subtotal = calcularSubtotal(carrito)
    val igv = calcularIGV(subtotal)
    val total = calcularTotal(subtotal, igv)
    println("Subtotal: S/$subtotal")
    println("IGV 18%: S/$igv")
    println("Total: S/$total")
}