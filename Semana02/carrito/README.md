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

