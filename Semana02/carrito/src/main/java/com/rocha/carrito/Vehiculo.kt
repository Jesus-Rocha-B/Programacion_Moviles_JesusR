package com.rocha.carrito

/**
 * Enum que define los tipos de vehículos permitidos.
 */
enum class TipoVehiculo {
    MOTO, AUTO, CAMIONETA, TRAILER
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
    val registros = mutableListOf<RegistroVehiculo>()
    val historialVisitas = mutableMapOf<String, Int>()

    var cantidadVehiculos = 0
    while (true) {
        print("¿Cuántos vehículos desea registrar? ")
        val input = readlnOrNull() ?: return
        val numero = input.toIntOrNull()
        if (numero != null && numero > 0) {
            cantidadVehiculos = numero
            break
        } else {
            println("Error: Ingrese un número entero mayor a 0.")
        }
    }

    for (i in 1..cantidadVehiculos) {
        println("\n--- Registro del Vehículo $i ---")
        print("Nombre del cliente: ")
        val nombre = readlnOrNull() ?: ""
        print("Placa del vehículo: ")
        val placa = readlnOrNull() ?: ""

        var tipo: TipoVehiculo? = null
        while (tipo == null) {
            print("Tipo de vehículo (moto, auto, camioneta, trailer): ")
            val inputTipo = readlnOrNull()?.uppercase() ?: ""
            try {
                tipo = TipoVehiculo.valueOf(inputTipo)
            } catch (e: IllegalArgumentException) {
                println("Error: Tipo inválido.")
            }
        }

        var horas = 0
        while (true) {
            print("Cantidad de horas: ")
            val inputHoras = readlnOrNull()?.toIntOrNull()
            if (inputHoras != null && inputHoras >= 1) {
                horas = inputHoras
                break
            } else {
                println("Error: Ingrese un número válido.")
            }
        }

        val registro = RegistroVehiculo(placa, tipo, horas, nombre)
        registros.add(registro)
    }

    var recaudacionTotal = 0.0
    registros.forEach { registro ->
        val ticket = calcularTicket(registro, historialVisitas)
        imprimirTicket(ticket)
        recaudacionTotal += ticket.total
    }
}

fun imprimirTicket(ticket: TicketCalculado) {
    val tarifaBase = ticket.detalles.firstOrNull()?.tarifa ?: 0.0
    println("\n=======================================================")
    println("TARIFA BASICA: S/ %.2f".format(tarifaBase))
    println("Cliente: ${ticket.registro.nombreCliente.uppercase()}")
    println("Tipo: ${ticket.registro.tipoVehiculo}")
    println("-------------------------------------------------------")
    println("%-8s%-12s%-11s%-10s".format("Hora", "Tarifa", "Recargo", "Importe"))
    ticket.detalles.forEach { d ->
        println("%-8d S/ %-9.2f %-10s S/ %-10.2f".format(d.hora, d.tarifa, "${d.recargo}%", d.importe))
    }
    println("-------------------------------------------------------")
    println("%-30s S/ %10.2f".format("TOTAL A PAGAR:", ticket.total))
    println("=======================================================")
}

fun calcularTicket(
    registro: RegistroVehiculo,
    historial: MutableMap<String, Int>
): TicketCalculado {
    // Solo se agregó el tipo Trailer y su tarifa base de 20 soles
    val tarifaBase = when (registro.tipoVehiculo) {
        TipoVehiculo.MOTO -> 2.0
        TipoVehiculo.AUTO -> 4.0
        TipoVehiculo.CAMIONETA -> 10.0
        TipoVehiculo.TRAILER -> 20.0
    }

    val detalles = mutableListOf<DetalleHora>()
    var subtotal = 0.0

    for (h in 1..registro.horas) {
        // Lógica de recargos (comisiones) por hora
        val porcentajeRecargo = if (registro.tipoVehiculo == TipoVehiculo.TRAILER) {
            // Reglas específicas para el Trailer
            when {
                h <= 2 -> 0      // 1-2 horas: Sin recargo
                h <= 5 -> 20     // 3-5 horas: 20%
                h <= 10 -> 40    // 6-10 horas: 40%
                else -> 50       // 11 horas o más: 50%
            }
        } else {
            // Reglas para Moto, Auto y Camioneta
            when {
                h <= 2 -> 0      // 1-2 horas: Sin recargo
                h <= 4 -> 20     // 3-4 horas: 20%
                else -> 50       // 5 horas o más: 50%
            }
        }
        val importeHora = tarifaBase + (tarifaBase * porcentajeRecargo / 100.0)
        detalles.add(DetalleHora(h, tarifaBase, porcentajeRecargo, importeHora))
        subtotal += importeHora
    }

    val visitasPrevias = historial[registro.nombreCliente] ?: 0
    val esFrecuente = visitasPrevias >= 4
    val descuento = if (esFrecuente) subtotal * 0.10 else 0.0
    val total = subtotal - descuento

    historial[registro.nombreCliente] = visitasPrevias + 1

    return TicketCalculado(registro, detalles, subtotal, descuento, total, esFrecuente)
}
