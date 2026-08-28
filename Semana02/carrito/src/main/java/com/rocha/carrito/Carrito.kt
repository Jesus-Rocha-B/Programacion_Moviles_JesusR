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
// Función para mostrar el detalle del carrito
fun mostrarDetallesCarrito(productos: List<Producto>) {
    println("-----------DETALLE DEL CARRITO-----------")
    // Variable para numerar cada producto en la lista (lo inicilizamos desde 1)
    var i = 1
    for (p in productos) {
        // Calculamos el importe total de cada producto
        val importe = p.precio * p.cantidad
        println(String.format("%d. %-20s x%d S/ %8.2f", i,
            p.nombre, p.cantidad, importe))
        // Aumentar el contador para el siguiente producto
        i++
    }
    println("-----------------------------------------")
}
// Calcular descuento dependiendo el total de la compra
fun calcularDescuentoCarrito(total: Double): Double {
    return when {
        total > 5000 -> total * 0.10
        total > 3000 -> total * 0.05
        else -> 0.0
    }
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
    for (producto in carrito) {
        println("Producto agregado: ${producto.nombre}")
    }
    // Usamos las funciones creadas para calcular el subtotal, igv y total del carrito
    val subtotal = calcularSubtotal(carrito)
    val igv = calcularIGV(subtotal)
    val total = calcularTotal(subtotal, igv)
    mostrarDetallesCarrito(carrito)
    println("Cantidad de productos: ${carrito.size}")
    println("")
    println(String.format("Subtotal: S/ %8.2f", subtotal))
    println(String.format("IGV 18%%: S/ %8.2f", igv))
    println(String.format("TOTAL A PAGAR: S/ %8.2f", total))
    // Buscamos el producto más caro del carrito
    val masCaro = carrito.maxByOrNull { it.precio }
    if (masCaro != null) {
        println("Producto mas caro: ${masCaro.nombre} " +
                String.format("(S/ %.2f)", masCaro.precio))
    }
    // Calculamos el descuento según el total y mostramos el total con descuento
    val descuento = calcularDescuentoCarrito(total)
    val totalConDescuento = total - descuento
    println(String.format("Descuento aplicado: S/ %8.2f", descuento))
    println(String.format("Total con descuento: S/ %8.2f", totalConDescuento))
}