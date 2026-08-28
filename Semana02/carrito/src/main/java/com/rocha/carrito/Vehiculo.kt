package com.rocha.carrito

import java.util.Scanner

/**
 * Enum que define los tipos de vehículos permitidos.
 */
enum class TipoVehiculo {
    MOTO, AUTO, CAMIONETA
}

/**
 * Clase de datos que representa el registro de un vehículo en el estacionamiento.
 */
data class RegistroVehiculo(
    val placa: String,
    val tipoVehiculo: TipoVehiculo,
    val horas: Int,
    val nombreCliente: String
)

/**
 * Representa el desglose de costo de una hora específica.
 */
data class DetalleHora(
    val hora: Int,
    val tarifa: Double,
    val recargo: Int,
    val importe: Double
)

/**
 * Contiene el resultado final del cálculo del estacionamiento.
 */
data class TicketCalculado(
    val registro: RegistroVehiculo,
    val detalles: List<DetalleHora>,
    val subtotal: Double,
    val descuento: Double,
    val total: Double,
    val esFrecuente: Boolean
)

fun main() {
    val scanner = Scanner(System.`in`)
    val registros = mutableListOf<RegistroVehiculo>()
    // Mapa para el historial de visitas (Nombre del cliente -> Cantidad de visitas)
    val historialVisitas = mutableMapOf<String, Int>()

    // 1. Validar la cantidad de vehículos a registrar
    var cantidadVehiculos = 0
    while (true) {
        print("¿Cuántos vehículos desea registrar? ")
        val input = scanner.nextLine()
        val numero = input.toIntOrNull()
        if (numero != null && numero > 0) {
            cantidadVehiculos = numero
            break
        } else {
            println("Error: Ingrese un número entero mayor a 0.")
        }
    }

    // 2. Procesar cada vehículo uno por uno
    for (i in 1..cantidadVehiculos) {
        println("\n--- Registro del Vehículo $i ---")

        // Pedir nombre del cliente
        print("Nombre del cliente: ")
        val nombre = scanner.nextLine()

        // Pedir placa
        print("Placa del vehículo: ")
        val placa = scanner.nextLine()

        // Pedir tipo de vehículo con validación
        var tipo: TipoVehiculo? = null
        while (tipo == null) {
            print("Tipo de vehículo (moto, auto, camioneta): ")
            val inputTipo = scanner.nextLine().uppercase()
            try {
                tipo = TipoVehiculo.valueOf(inputTipo)
            } catch (e: IllegalArgumentException) {
                println("Error: Tipo inválido. Debe ser 'moto', 'auto' o 'camioneta'.")
            }
        }

        // Pedir horas con validación (mínimo 1)
        var horas = 0
        while (true) {
            print("Cantidad de horas: ")
            val inputHoras = scanner.nextLine().toIntOrNull()
            if (inputHoras != null && inputHoras >= 1) {
                horas = inputHoras
                break
            } else {
                println("Error: Las horas deben ser un número entero mayor o igual a 1.")
            }
        }

        // Almacenar el registro en la lista
        val registro = RegistroVehiculo(placa, tipo, horas, nombre)
        registros.add(registro)
    }

    // Mostrar resumen de registros (Opcional, para verificar)
    println("\n=== Resumen de Registros ===")
    registros.forEach { println(it) }
}

/**
 * Función que implementa la lógica de negocio para calcular el ticket.
 */
fun calcularTicket(
    registro: RegistroVehiculo,
    historial: MutableMap<String, Int>
): TicketCalculado {
    // 1. Obtener tarifa base
    val tarifaBase = when (registro.tipoVehiculo) {
        TipoVehiculo.MOTO -> 2.0
        TipoVehiculo.AUTO -> 4.0
        TipoVehiculo.CAMIONETA -> 10.0
    }

    val detalles = mutableListOf<DetalleHora>()
    var subtotal = 0.0

    // 2. Calcular desglose hora por hora
    for (h in 1..registro.horas) {
        val porcentajeRecargo = when {
            h <= 2 -> 0
            h <= 4 -> 20 // De la hora 3 a la 4 aplicamos 20%
            else -> 50   // De la hora 5 en adelante aplicamos 50%
        }

        val importeHora = tarifaBase + (tarifaBase * porcentajeRecargo / 100.0)
        detalles.add(DetalleHora(h, tarifaBase, porcentajeRecargo, importeHora))
        subtotal += importeHora
    }

    // 3. Lógica de cliente frecuente (5ta visita en adelante)
    val visitasPrevias = historial[registro.nombreCliente] ?: 0
    val esFrecuente = visitasPrevias >= 4
    val descuento = if (esFrecuente) subtotal * 0.10 else 0.0
    val total = subtotal - descuento

    // 4. Actualizar historial de visitas
    historial[registro.nombreCliente] = visitasPrevias + 1

    return TicketCalculado(registro, detalles, subtotal, descuento, total, esFrecuente)
}
