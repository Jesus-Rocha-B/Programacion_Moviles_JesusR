# Prompt utilizados para el ejercicio

### Primer prompt
Primer prompt
Actúa como un desarrollador senior en Kotlin. Desarrolla una aplicación de consola para la gestión de una estacionamiento. Para comenzar, te debes enfocar en el modelo datos y el flujo de los inputs y output de la información.
1. Estructura de datos
    - Define un enum class que se llame “TipoVehiculo” con las opciones de moto, auto, camioneta.
    - Crea una data class que se llama “RegistroVehiculo” con los atributo:
        - placa : String
        - tipoVehiculo: TipoVehiculo
        - horas: Int
        - nombreCliente: String
2. Flujo de la aplicación
    - Al iniciar, el programa pide cuántos vehículos se van a registrar, para esto se debe utilizar un bucle while para poder validar que sea un numero entero y mayor a 0
    - Después, procesa uno a uno los vehículos pidiendo: Nombre del cliente, Placa, Tipo de Vehículos (este debe pasar una validación para que se cumpla que sea una moto, auto o camioneta)
    - Las horas no pueden ser a 1 ni negativas. Si el usuario ingresa un valor erróneo se debería volver a pedirle ese dato
    - Almacena los registros en una list que se llame RegistroVehiculo
      Me entregas el código estructurado de las clases y de la función main con lo que te solicite, agregas comentarios básicos a cada parte. Después de que escribas el código me debes explicar brevemente cada parte que pusiste y el por que lo pusiste.
Dato: El archivo que vamos a trabajar se llama “Vehiculo.kt”

Se modifico el usuario que hace el commit
### Segundo prompt
Tomando la base que creaste, ahora toca implementar la lógica de negocios y las reglas de cálculo en el código.
1. Tarifas base por hora
   - Moto: 2 soles
   - Autor: 4 soles
   - Camioneta: 10 soles
2. Reglas de recargo por horas
   - Hora 1 y 2: Sin recargo (se cobra la tarifa normal).
   - Hora 3 y 5: Recargo del 20% sobre el precio original de la tarifa base por cada una de estas horas
   - Hora 5 en adelante: Recargo del 50% sobre el precio original de la tarifa base por cada una de estas horas.
   - Crea una data class DetalleHora (val hora: Int, val tarifa: Double, val recargo: Int, val importe: Double) para almacenar el requerimiento de cada hora.
3. Control de cliente frecuente y descuento por fidelidad
   - Manten en memoria un historial de visitas por cliente. Para ello utiliza un MutableMap (usando String y int) con el nombre del cliente. Además, si el cliente tiene más de 4 visitas previas, es decir en su quinta visita se le aplica un 10% de descuento sobre el total que va a pagar.
4. Estructura del resultado
   - Crea una data class TinketCalculado y una función que reciba un RegistroVehiculo y el mapa de historial, calcule el desglose hora por hora, determinen el subtotal, aplique el descuento por cliente cliente frecuente si esto corresponde y retorne toda la información lista para ser impresa.
     Generas solo las clases y funciones necesarias para la lógica de calculo sin alterar aún la impresión final por consola.
     Cada cambio o lo que añadas me lo debes explicar
### Tercer prompt
Tomando como base el código que generaste. Iimplementa la función de visualización e integra todo el flujo en el main
1. Función de Impresión de Ticket:
- - Crea una función que se llame imprimirTicket (ticket: TicketCalculado) que formatee la salida de forma alineada, clara y en moneda en soles peruanos. El formato debe seguir exactamente este esquema:

  =======================================================
  TARIFA BASICA: XXX
  Cliente: [NOMBRE]-
- =======================================================
- Hora    Tarifa      Recargo    Importe
- 1       S/ 4.00     0%         S/ 4.00
- 2       S/ 4.00     0%         S/ 4.00
- 3       S/ 4.00     20%        S/ 4.80
- =======================================================
- Subtotal: S/ 12.80 (Este debe aparecer si el cliente tiene descuento)
- Descuento Cliente Frecuente:   S/ 0.00 (o -S/ X.XX se aplica el 10%, este no debe aparecer si no es el caso)
- TOTAL A PAGAR: S/ 12.80
- =======================================================
2. Integración en el main():
- Reemplaza el resumen de prueba temporal por el procesamiento real de cada registro usando calcularTicket.
- Llama a imprimirTicket para cada vehículo registrado.
- Si se registraron múltiples vehículos, muestra opcionalmente al final un resumen general con la cantidad total de vehículos atendidos y la recaudación total en soles.
### Cuuarto prompt
Modificar la introducción de datos. No hay que usar Java (Scanner), netamente se tiene que usar el lenguaje Kotlin
### Resultado en consola
![Resultado final](captura.png)
![Resultado final 2](captura1.png)