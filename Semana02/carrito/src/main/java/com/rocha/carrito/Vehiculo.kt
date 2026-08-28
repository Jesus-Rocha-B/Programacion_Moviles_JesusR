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

fun main() {
    val scanner = Scanner(System.`in`)
    val registros = mutableListOf<RegistroVehiculo>()

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
