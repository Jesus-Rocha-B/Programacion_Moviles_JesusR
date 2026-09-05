# Registro de Productos usando Jetpack Compose

**Estudiante:** Jesús Enrique Rocha Bobadilla  
**Laboratorio:** Estado y Formulario en Jetpack Compose  

---
## Descripción del Proyecto

Se desarrollo una aplicación utilizando Jetpack compose para poder registrar productos, incluyendo un formulario con validación, manejo de estados usando remember y mutableLStateOf, cálculo con el importe total y una tarjeta de resumen con un mensaje de confirmación de que el producto fue agregado.

---

## Capturas de Pantalla

### 1. Formulario vacío
![Formulario Vacío](./capformvacio.jpg)

### 2. Formulario con datos
![Formulario con Datos](./cap1.jpg)

### 3. Producto registrado y resumen
![Producto Registrado](./cap2.jpg)

---

## Mejora con IA

| Prompt que usé | Qué generó Gemini | Qué acepté o corregí (y por qué) |
| :--- | :--- | :--- |
| **Prompt 1:** En PantallaRegistro de MainActivity.kt agrega una validación al presionar agregar producto para verificar que los campos de nombre, precio y cantidad no estén vacíos, si alguno está vacío no muestres la card de resumen y muestra un mensaje de error en color rojo que diga por favor completa todos los campos, si los tres campos tienen datos muestra la card de resumen normalmente con la información ingresada, también agrega un botón limpiar al lado o debajo de agregar producto que al presionarlo deje los campos de nombre, precio y cantidad vacíos, oculte la card de resumen y quite el mensaje de error, no modifiques el diseño, colores, tamaños ni estilos que ya tienen los OutlinedTextFields ni la card de resumen, solo agrega la lógica de validación, el mensaje de error y el funcionamiento del botón limpiar, mantén la estructura existente y no hagas cambios innecesarios. | Generó la variable de estado `mostrarError`, la lógica de validación para verificar campos vacíos (`nombre.isBlank() \|\| precio.isBlank() \|\| cantidad.isBlank()`), el mensaje de error en texto rojo y el botón "LIMPIAR" en una `Row` para resetear el formulario. | **Acepté** la lógica para campos vacíos, el botón "LIMPIAR" y la visualización de la Card. **Corregí** la variable de estado cambiando `mostrarError` (Booleano) por `mensajeError` (String) para poder manejar mensajes de error dinámicos e independientes según la falla del usuario. |
| **Prompt 2:** En PantallaRegistro de MainActivity.kt revisa y mejora la validación sin cambiar el diseño actual. prueba principalmente los casos de campos vacíos y cuando se ingrese texto o letras en el campo precio. la validación de campos vacíos ya funciona, así que mantenla, pero agrega una validación para que el precio solo acepte un valor numérico válido y no permita que un dato incorrecto termine mostrando S/ 0.00 en la card. revisa también la cantidad para evitar valores que no sean números. si algún dato es inválido, muestra un mensaje de error adecuado y no muestres la card de resumen. revisa los nombres de variables, textos, espaciados y la lógica si encuentras algo que se pueda mejorar, pero sin hacer cambios innecesarios. no cambies los colores ni los estilos de los OutlinedTextFields, botones o de la card, ya que estoy probando la aplicación en un xiaomi y los colores pueden depender del tema del dispositivo. mantén el diseño y la estructura general que ya tengo. | Generó la conversión con `precio.toDoubleOrNull()` y `cantidad.toIntOrNull()`, validando en un bloque `when` si eran nulos o menores a cero, mostrando un mensaje de error dinámico y bloqueando la tarjeta de resumen. | **Acepté** la conversión numérica y el bloqueo de la Card de resumen para evitar el cálculo erróneo `S/ 0.00`. **Corregí** el manejo del reseteo del mensaje de error añadiendo `mensajeError = ""` en el `onValueChange` de cada `OutlinedTextField` para que al empezar a escribir nuevamente, el mensaje de error desaparezca de inmediato y no confunda al usuario. |

---

## Pregunta de Reflexión

### ¿Qué pasaría si declaras las variables de los campos sin remember?

Si declaramos  las variables de estado utilizando solo mutableStateOf("") sin usar remember:
var nombre by mutableStateOf("") Cada vez que el usuario escribe un carácter en un TextField, Compose desencadena una recomposición para redibujar la interfaz. Sin remember, la función @Composable se vuelve a ejecutar desde el principio y la variable se reinicia a su valor inicial ("").

**Efectos principales:**
- El texto ingresado por el usuario se borra instantáneamente en cada pulsación de tecla.
- Es imposible guardar o mostrar los datos ingresados.
- Remember es necesario para preservar y mantener el valor del estado en memoria a lo largo de las recomposiciones.
