# TECSUP Store

## Requisitos Funcionales
- Cada tarjeta de producto (en cualquier sección de la TECSUP Store) tiene un ícono de 3 puntos (⋮) a la derecha.
- Al tocarlo, se despliega un DropdownMenu con mínimo 3 opciones: "Favoritos", "Compartir", "Reportar" (o equivalentes).
- Cada opción del DropdownMenu tiene su ícono correspondiente (leadingIcon).
- Un ícono ☰ en la topBar abre un NavigationDrawer con mínimo 4 destinos: Inicio, Mis pedidos, Favoritos, Perfil (+ Cerrar sesión opcional).
- El encabezado del drawer muestra el avatar/iniciales y datos básicos del usuario.
- El destino activo del drawer se resalta visualmente (color de fondo distinto al resto).

## Prompts de Desarrollo

### Prompt 1: Configuración inicial del menú lateral (DrawerMenu.kt)
> **Contexto:** Estoy desarrollando la aplicación "TECSUP Store" en Android Studio utilizando Jetpack Compose con la librería de Material 3. El paquete principal del proyecto es `com.tecsup.tecsupstore`.
> 
> **Qué hacer:** Crea el componente `DrawerMenu.kt` que servirá como menú lateral desplegable. Debe incluir un encabezado de usuario con el avatar/iniciales 'MR', el nombre "Maria Rojas" y el correo "maria@tecsup.edu.pe". Debajo, agrega los ítems navegables con componentes `NavigationDrawerItem` para las opciones: Inicio, Registro / Carrito, Lista de Elementos y Mi Perfil.
> 
> **Qué NO hacer:** No apliques colores ni fuentes personalizadas complejas. No fuerces la paleta con valores Hexadecimales estáticos (como `0xFF4A148C`). Evita librerías externas fuera del ecosistema estándar de Compose Material 3.
> 
> **Explicación esperada:** Al finalizar el código, explica brevemente cómo se gestiona el estado de la ruta seleccionada (`currentRoute`) y cómo se ejecuta el callback `onNavigate`.

---

### Prompt 2: Pantalla principal y menú contextual de productos (HomeScreen.kt)
> **Contexto:** Dentro del mismo proyecto "TECSUP Store", necesito implementar la vista principal de la tienda donde se listen los productos más vendidos usando una lista perezosa (`LazyColumn`).
> 
> **Qué hacer:** Desarrolla `HomeScreen.kt` con un header representativo de la tienda y tarjetas (`Card`) simples de Material 3. Cada tarjeta debe mostrar el ícono de una bolsa de compras, el nombre del producto, el precio y un botón de tres puntos vertical (`IconButton`) que abra un `DropdownMenu` contextual con las opciones "Ver detalle" y "Eliminar".
> 
> **Qué NO hacer:** No recargues la interfaz con animaciones complejas ni sombras pesadas. No hardcodees estilos visuales que rompan la coherencia del `MaterialTheme` del laboratorio.
> 
> **Explicación esperada:** Describe brevemente la lógica del estado del `DropdownMenu` (`expanded`) y cómo se capturan los eventos para navegar a los detalles o eliminar el ítem.

---

### Prompt 3: Integración de rutas, pantallas secundarias y MainActivity
> **Contexto:** Ya cuento con las vistas individuales y necesito interconectarlas de forma fluida mediante el sistema de navegación de Jetpack Compose (`androidx.navigation`).
> 
> **Qué hacer:** Configura la estructura completa de navegación implementando:
> 1. `Screen.kt` con la definición de rutas (`Home`, `Registro`, `List`, `Profile` y `Detail` aceptando un `productoId`).
> 2. `AppNavigation.kt` configurando el `NavHost` y registrando todas las pantallas.
> 3. Las pantallas secundarias de relleno `ListScreen.kt`, `ProfileScreen.kt` y `DetailScreen.kt`.
> 4. `MainActivity.kt` envolviendo todo dentro de `ModalNavigationDrawer`, configurando la `TopAppBar` con el botón hamburguesa (☰) y aplicando el tema `TecsupStoreTheme`.
> 
> **Qué NO hacer:** No importes temas ni paquetes de proyectos o laboratorios anteriores (por ejemplo, evita referencias a `Lab04CarritoTecsupTheme`). No omitas el manejo del padding en el `Scaffold`.
> 
> **Explicación esperada:** Explica el flujo de navegación completo, cómo se abre/cierra el drawer mediante la corrutina (`rememberCoroutineScope`) y cómo se pasan los argumentos dinámicos a la pantalla de detalle.

## Estructura del Proyecto

```text
app/src/main/java/com/tecsup/tecsupstore/
│
├── MainActivity.kt                  # Se envuelve con ModalNavigationDrawer y contiene la TopAppBar
│
├── navigation/
│   ├── Screen.kt                    # Definición de rutas sealed class
│   └── AppNavigation.kt             # NavHost principal con las pantallas registradas
│
└── ui/
    ├── screens/
    │   ├── DrawerMenu.kt            # Contenido del NavigationDrawer con header de usuario
    │   ├── HomeScreen.kt            # Pantalla principal con tarjetas de productos e ícono de 3 puntos
    │   ├── ListScreen.kt            # Lista de elementos general
    │   ├── DetailScreen.kt          # Detalle parametrizado del producto seleccionable
    │   ├── ProfileScreen.kt         # Pantalla de perfil de usuario
    │   └── RegistroScreen.kt        # Formulario de registro / carrito
    │
    └── theme/                       # Tema y configuración de colores de Material 3
```
