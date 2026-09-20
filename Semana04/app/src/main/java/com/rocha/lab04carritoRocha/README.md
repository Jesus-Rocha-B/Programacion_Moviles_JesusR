# Lab 04 - Carrito de Compras
**Alumno:** Jesús Rocha

### Descripción
Aplicación móvil desarrollada en Jetpack Compose que permite registrar productos, calcular subtotal, IGV, descuentos y el total a pagar, con opción de eliminar elementos de la lista en tiempo real.

---

### Capturas de Pantalla

**1. Estado Vacío:**
*(Insertar captura cuando el carrito está vacío)*

**2. Con Productos y Panel de Totales:**
*(Insertar captura cuando hay productos agregados en el carrito)*

---

### Preguntas Conceptuales

**a) ¿Por qué se usa `mutableStateListOf` y no una `MutableList` normal?**
Porque `mutableStateListOf` avisa a Jetpack Compose cuando hay cambios (al agregar o eliminar productos). Si usaras una lista normal, los datos cambiarían internamente pero la pantalla no se actualizaría sola.

**b) ¿Por qué la lista se declara con `val` y aún así podemos agregarle elementos?**
Porque `val` significa que la variable no puede ser reasignada a otra lista distinta. Pero la lista en sí misma es un objeto mutable que permite modificar su contenido interno (agregar o quitar ítems).

**c) ¿Qué hace `weight(1f)` en la `LazyColumn`?**
Hace que la lista ocupe todo el espacio disponible que sobra en la pantalla, evitando que se desborde y dejando espacio fijo para el formulario y los totales.
