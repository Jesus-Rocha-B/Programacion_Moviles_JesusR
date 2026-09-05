# Registro de Productos usando Jetpack Compose

**Estudiante:** Jesús Enrique Rocha Bobadilla  
**Laboratorio:** Estado y Formulario en Jetpack Compose  

---
## Descripción del Proyecto

Se desarrollo una aplicación utilizando Jetpack compose para poder registrar productos, incluyendo un formulario con validación, manejo de estados usando remember y mutableLStateOf, cálculo con el importe total y una tarjeta de resumen con un mensaje de confirmación de que el producto fue agregado.

---

## Capturas de Pantalla

### 1. Formulario vacío
![Formulario Vacío](capformvacio.jpg)

### 2. Formulario con datos
![Formulario con Datos](cap1.jpg)

### 3. Producto registrado y resumen
![Producto Registrado](cap2.jpg)

---

## Pregunta de Reflexión

### ¿Qué pasaría si declaras las variables de los campos sin remember?

Si declaramos  las variables de estado utilizando solo mutableStateOf("") sin usar remember:
var nombre by mutableStateOf("") Cada vez que el usuario escribe un carácter en un TextField, Compose desencadena una recomposición para redibujar la interfaz. Sin remember, la función @Composable se vuelve a ejecutar desde el principio y la variable se reinicia a su valor inicial ("").

**Efectos principales:**
- El texto ingresado por el usuario se borra instantáneamente en cada pulsación de tecla.
- Es imposible guardar o mostrar los datos ingresados.
- Remember es necesario para preservar y mantener el valor del estado en memoria a lo largo de las recomposiciones.
