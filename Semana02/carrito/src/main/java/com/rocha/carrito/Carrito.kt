package com.rocha.carrito
data class Producto(
    val nombre: String,
    val precio: Double,
    private var cantidad: Int, // Encapsulado: Unidades en el carrito
    private var stock: Int // Encapsulado: Inventario en tienda
) {
    // Getters para consulta segura
    fun obtenerCantidad(): Int = cantidad
    fun obtenerStock(): Int = stock

    // Propiedad calculada para el importe (Encapsulamiento de lógica de cálculo)
    fun calcularImporte(): Double = precio * cantidad

    // Método para modificar la cantidad validando contra el stock real
    fun actualizarCantidad(nuevaCantidad: Int): Boolean {
        if (nuevaCantidad <= 0) {
            println("Error: La cantidad debe ser mayor a 0.")
            return false
        }
        
        val diferencia = nuevaCantidad - cantidad
        
        return if (diferencia > 0) {
            // Si el cliente pide más, intentamos reducir del stock de la tienda
            if (reducirStock(diferencia)) {
                cantidad = nuevaCantidad
                true
            } else {
                false
            }
        } else if (diferencia < 0) {
            // Si el cliente pide menos, devolvemos el excedente al stock de la tienda
            aumentarStock(-diferencia)
            cantidad = nuevaCantidad
            true
        } else {
            true // La cantidad es la misma
        }
    }

    // Método para aumentar el stock (por reposición o devolución)
    fun aumentarStock(cantidadAReponer: Int) {
        if (cantidadAReponer > 0) {
            stock += cantidadAReponer
        }
    }

    // Método para reducir el stock (por venta)
    fun reducirStock(cantidadAVender: Int): Boolean {
        return if (cantidadAVender > 0 && stock >= cantidadAVender) {
            stock -= cantidadAVender
            true
        } else {
            println("Error: No hay suficiente stock de $nombre o cantidad inválida.")
            false
        }
    }
}

class Carrito {
    private val productos = mutableListOf<Producto>()

    // Método para agregar productos al carrito
    fun agregarProducto(producto: Producto) {
        productos.add(producto)
        println("Producto agregado: ${producto.nombre}")
    }

    // Métodos de solo consulta que recalculan los valores en el momento
    fun calcularSubtotal(): Double = productos.sumOf { it.calcularImporte() }

    fun calcularIGV(): Double = calcularSubtotal() * 0.18

    fun calcularTotal(): Double = calcularSubtotal() + calcularIGV()

    fun obtenerCantidadItems(): Int = productos.size

    fun obtenerProductoMasCaro(): Producto? = productos.maxByOrNull { it.precio }

    // El descuento también es una lógica propia del estado del carrito
    fun calcularDescuento(): Double {
        val total = calcularTotal()
        return when {
            total > 5000 -> total * 0.10
            total > 3000 -> total * 0.05
            else -> 0.0
        }
    }

    // Método encapsulado para mostrar el detalle
    fun mostrarDetalle() {
        println("-----------DETALLE DEL CARRITO-----------")
        var i = 1
        for (p in productos) {
            val importe = p.calcularImporte()
            println(String.format("%d. %-20s x%d S/ %8.2f", i,
                p.nombre, p.obtenerCantidad(), importe))
            i++
        }
        println("-----------------------------------------")
    }
}

fun main() {
    println("=========================================")
    println("   CARRITO DE COMPRAS - TIENDA TECSUP    ")
    println("=========================================")
    val nombreCliente = "Jesús Rocha"
    val miCarrito = Carrito()
    
    // Mencionamos al cliente
    println("Cliente: $nombreCliente")
    println("")
    
    // Añadir productos al carrito usando el método de la clase
    miCarrito.agregarProducto(Producto("Laptop HP", 2500.0, 1, 5))
    miCarrito.agregarProducto(Producto("Mouse Logitech", 45.5, 2, 10))
    miCarrito.agregarProducto(Producto("Mouse Rayzer", 99.9, 3, 8))
    miCarrito.agregarProducto(Producto("Teclado Redragon", 199.5, 10, 20))
    
    println("")
    
    // Obtenemos los valores calculados al momento
    val subtotal = miCarrito.calcularSubtotal()
    val igv = miCarrito.calcularIGV()
    val total = miCarrito.calcularTotal()
    
    miCarrito.mostrarDetalle()
    
    println("Cantidad de productos: ${miCarrito.obtenerCantidadItems()}")
    println("")
    println(String.format("Subtotal: S/ %8.2f", subtotal))
    println(String.format("IGV 18%%: S/ %8.2f", igv))
    println(String.format("TOTAL A PAGAR: S/ %8.2f", total))
    
    // Buscamos el producto más caro usando la lógica del carrito
    val masCaro = miCarrito.obtenerProductoMasCaro()
    if (masCaro != null) {
        println("Producto mas caro: ${masCaro.nombre} " +
                String.format("(S/ %.2f)", masCaro.precio))
    }
    
    // Calculamos el descuento y el total final
    val descuento = miCarrito.calcularDescuento()
    val totalConDescuento = total - descuento
    
    println(String.format("Descuento aplicado: S/ %8.2f", descuento))
    println(String.format("Total con descuento: S/ %8.2f", totalConDescuento))
}