package com.rocha.lab04carritoRocha

data class Producto(
    val nombre: String,
    val precio: Double,
    var cantidad: Int
)

// Función para calcular el subtotal de los productos en el carrito
fun calcularSubtotal(productos: List<Producto>): Double {
    var subtotal = 0.0
    for (p in productos) {
        subtotal += p.precio * p.cantidad
    }
    return subtotal
}

// Función para calcular el IGV
fun calcularIGV(subtotal: Double): Double {
    val igv = subtotal * 0.18
    return igv
}

// Función para calcular el total del carrito
fun calcularTotal(subtotal: Double, igv: Double): Double {
    val total = subtotal + igv
    return total
}

// Calcular descuento dependiendo el total de la compra
fun calcularDescuentoCarrito(total: Double): Double {
    return when {
        total > 5000 -> total * 0.10
        total > 3000 -> total * 0.05
        else -> 0.0
    }
}
