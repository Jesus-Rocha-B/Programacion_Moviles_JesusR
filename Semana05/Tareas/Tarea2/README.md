## Requisitos Funcionales

* **Inicio:** `LazyRow` con chips de filtro (mínimo 2: "Hoy" / "Esta semana") y `LazyColumn` con lista de clases (mínimo 3), cada tarjeta con nombre y horario.
* **Detalle de clase:** recibe los datos de la clase elegida por parámetro de navegación; botón "Reservar cupo".
* **Confirmación:** resumen de la reserva (clase, horario); botón para ver reservas.
* **bottomBar:** visible en Inicio, Reservas y Perfil, con 4 pestañas (Inicio, Reservas, Rutinas, Perfil); el ícono activo se resalta según la pantalla actual.
* **Reservas:** `LazyColumn` con las clases reservadas, cada una con su estado (Confirmada / Completada) diferenciado visualmente.
* **Perfil:** datos del usuario y estadísticas simples (ej. clases tomadas, racha de asistencia).

---

## Prompts Utilizados en el Desarrollo

### Prompt 1: Rediseño visual de HomeScreen y ClassDetailScreen
```text
CONTEXTO DEL PROYECTO
Tengo una app de Android con Jetpack Compose y Material3, paquete
com.rocha.tarea2, con navegación mediante Navigation Compose y un
bottomBar condicional en AppNavigation.kt. Ahora necesito mejorar
únicamente la presentación visual de HomeScreen.kt y
ClassDetailScreen.kt.

CÓDIGO ACTUAL DE HOMESCREEN:
[Pega aquí el código completo de HomeScreen.kt]

CÓDIGO ACTUAL DE CLASSDETAILSCREEN:
[Pega aquí el código completo de ClassDetailScreen.kt]

DISEÑO DE HOMESCREEN (INICIO)
- Encabezado superior con fondo verde oscuro (#00695C aprox.),
  esquinas inferiores redondeadas, con el texto "TECSUP Fit" en
  blanco y negrita, y debajo "Hola, Diego" en blanco, tamaño
  pequeño
- Debajo del encabezado, sobre fondo blanco, dos chips: "Hoy"
  seleccionado con fondo verde oscuro y texto blanco, y "Esta
  semana" sin seleccionar con fondo gris claro y texto gris oscuro
- Título "Clases disponibles" en negrita
- Lista de clases, cada una en una Card con fondo gris muy claro y
  esquinas redondeadas, con:
	- Un ícono a la izquierda dentro de un círculo con fondo
          verde claro
	- El nombre de la clase en negrita
	- El horario y la sala en gris, tamaño pequeño, debajo del
          nombre
	- Al tocar la Card, navega al detalle de esa clase

DISEÑO DE CLASSDETAILSCREEN (DETALLE DE CLASE)
- TopAppBar con fondo blanco, flecha de volver a la izquierda y
  título "Detalle de clase" en negro
- Una Card grande con fondo verde muy claro (#E0F2F1 aprox.) y
  esquinas redondeadas, con un ícono grande de mancuerna en verde
  oscuro, centrado
- Debajo, el nombre de la clase en negrita y grande
- El horario, sala y duración en gris, tamaño pequeño, en una sola
  línea separados por "·"
- La descripción de la clase en un párrafo, tamaño normal
- El texto de cupos disponibles ("X de Y cupos disponibles") en
  verde oscuro
- Al final, un Button "Reservar cupo" con ancho completo, fondo
  verde oscuro y texto blanco en negrita

LO QUE NECESITO QUE HAGAS
- Mejora solo la presentación visual de ambos códigos: colores,
  tipografía, espaciado, Card e íconos
- No cambies la lógica existente de navController.navigate() ni el
  uso de Screen.ClassDetail.createRoute(classItem.id) ni
  Screen.Confirmation.createRoute(classId)
- No cambies el nombre de las data class ClassItem y Reservation,
  ni la lista sampleClasses, ni cómo se busca la clase
  (sampleClasses.find { it.id == classId })
- Usa componentes de Material3 como Card, Icon, Button,
  MaterialTheme.colorScheme y MaterialTheme.typography
- No inventes componentes ni uses librerías externas que no estén
  instaladas en mi proyecto
- Mantén los mismos nombres y parámetros de las funciones
  composable: HomeScreen(navController: NavController) y
  ClassDetailScreen(classId: Int, navController: NavController)
- Dame el código Kotlin completo de ambas pantallas modificadas,
  listo para pegar directamente en mi proyecto
- Después del código, explícame qué modificaste y para qué sirve
  cada apartado importante en cada pantalla: el encabezado, los
  chips, las Card, los íconos, los colores, el espaciado y el
  botón de reservar
```

---

### Prompt 2: Rediseño visual de ConfirmationScreen y ReservationsScreen
```text
CONTEXTO DEL PROYECTO
Ya mejoré la presentación visual de HomeScreen.kt y
ClassDetailScreen.kt con un tema verde y blanco. Ahora necesito
mejorar únicamente la presentación visual de ConfirmationScreen.kt
y ReservationsScreen.kt, del mismo proyecto com.rocha.tarea2.

CÓDIGO ACTUAL DE CONFIRMATIONSCREEN:
[Pega aquí el código completo de ConfirmationScreen.kt]

CÓDIGO ACTUAL DE RESERVATIONSSCREEN:
[Pega aquí el código completo de ReservationsScreen.kt]

DISEÑO DE CONFIRMATIONSCREEN (CONFIRMACIÓN)
- Contenido centrado vertical y horizontalmente en toda la
  pantalla
- Un ícono de check dentro de un círculo con fondo verde muy claro
  (#E0F2F1 aprox.) y el ícono en verde oscuro
- Título "¡Cupo reservado!" en negrita
- Debajo, el nombre de la clase en tamaño normal
- Debajo, la fecha, hora y sala en gris, tamaño pequeño
- Un Button "Ver mis reservas" con ancho completo, fondo gris
  claro y texto gris oscuro

DISEÑO DE RESERVATIONSSCREEN (MIS RESERVAS)
- Título "Mis reservas" en negrita, arriba de la pantalla
- Lista de reservas, cada una en una Card con fondo gris muy claro
  y esquinas redondeadas, con un borde izquierdo grueso de color:
	- Borde verde oscuro si el estado es "Confirmada"
	- Borde gris si el estado es "Completada"
- Dentro de cada Card:
	- El nombre de la clase en negrita
	- La fecha y hora en gris, tamaño pequeño, debajo del
          nombre
	- Una etiqueta pequeña con el estado ("Confirmada" en
          verde, "Completada" en gris), con fondo del mismo color
          más claro

LO QUE NECESITO QUE HAGAS
- Mejora solo la presentación visual de ambos códigos: colores,
  tipografía, espaciado, Card e íconos
- No cambies la lógica existente de navController.navigate() hacia
  Screen.Reservations.route
- No cambies cómo se busca la clase (sampleClasses.find { it.id
  == classId }) ni la lista sampleReservations
- Usa componentes de Material3 como Card, Icon, Button, Surface,
  MaterialTheme.colorScheme y MaterialTheme.typography
- No inventes componentes ni uses librerías externas que no estén
  instaladas en mi proyecto
- Mantén los mismos nombres y parámetros de las funciones
  composable: ConfirmationScreen(classId: Int, navController:
  NavController) y ReservationsScreen(navController: NavController)
- Dame el código Kotlin completo de ambas pantallas modificadas,
  listo para pegar directamente en mi proyecto
- Después del código, explícame qué modificaste y para qué sirve
  cada apartado importante en cada pantalla: el ícono de check, el
  botón de ver reservas, el borde de color según estado y la
  etiqueta de estado
```

---

### Prompt 3: Rediseño visual de RoutinesScreen y ProfileScreen
```text
CONTEXTO DEL PROYECTO
Ya mejoré la presentación visual de HomeScreen.kt,
ClassDetailScreen.kt, ConfirmationScreen.kt y ReservationsScreen.kt
con un tema verde y blanco. Ahora necesito mejorar únicamente la
presentación visual de RoutinesScreen.kt y ProfileScreen.kt, del
mismo proyecto com.rocha.tarea2.

CÓDIGO ACTUAL DE ROUTINESSCREEN:
[Pega aquí el código completo de RoutinesScreen.kt]

CÓDIGO ACTUAL DE PROFILESCREEN:
[Pega aquí el código completo de ProfileScreen.kt]

DISEÑO DE ROUTINESSCREEN (RUTINAS)
- Título "Rutinas" en negrita, arriba de la pantalla
- Lista de 2 rutinas de ejemplo, cada una en una Card con fondo
  gris muy claro y esquinas redondeadas:
	- Rutina de Bíceps: nombre en negrita y una descripción
          corta debajo en gris
	- Rutina de Pecho: nombre en negrita y una descripción
          corta debajo en gris
- Cada Card con un ícono a la izquierda dentro de un círculo con
  fondo verde claro

DISEÑO DE PROFILESCREEN (MI PERFIL)
- Título "Mi perfil" en negrita, arriba de la pantalla
- Avatar circular grande con las iniciales "DR" en negrita, fondo
  verde claro
- Debajo, el nombre "Diego Ramos" en negrita
- Debajo, "Plan Premium" en gris, tamaño pequeño
- Dos Card lado a lado, fondo gris muy claro y esquinas
  redondeadas: una con el número "14" y la etiqueta "Clases", otra
  con el número "3" y la etiqueta "Rachas"

LO QUE NECESITO QUE HAGAS
- Mejora solo la presentación visual de ambos códigos: colores,
  tipografía, espaciado, Card e íconos
- Agrega en RoutinesScreen.kt una lista local de 2 rutinas de
  ejemplo (Bíceps y Pecho) directamente en el código, sin base de
  datos ni ViewModel
- No cambies la estructura de datos existente en ProfileScreen.kt
  (userName, userPlan, userInitials, classesCount, streakCount)
- Usa componentes de Material3 como Card, Icon,
  MaterialTheme.colorScheme y MaterialTheme.typography
- No inventes componentes ni uses librerías externas que no estén
  instaladas en mi proyecto
- Mantén los mismos nombres y parámetros de las funciones
  composable: RoutinesScreen(navController: NavController) y
  ProfileScreen(navController: NavController)
- Dame el código Kotlin completo de ambas pantallas modificadas,
  listo para pegar directamente en mi proyecto
- Después del código, explícame qué modificaste y para qué sirve
  cada apartado importante: las Card de rutinas, el avatar, las
  Card de estadísticas y los colores usados
```

---

### Prompt 4: Ajustes de precisión según maquetas y vinculación de reservas
```text
CONTEXTO DEL PROYECTO
Ya generaste una primera versión mejorada de AppNavigation.kt, HomeScreen.kt, ClassDetailScreen.kt, ConfirmationScreen.kt, ReservationsScreen.kt y ProfileScreen.kt con un tema verde y blanco, del proyecto com.rocha.tarea2. Comparé el resultado con las imágenes de referencia y encontré diferencias puntuales que necesito corregir, sin rehacer el diseño desde cero.

CÓDIGO ACTUAL DE CADA PANTALLA:
[Pega aquí el código completo actual de AppNavigation.kt]
[Pega aquí el código completo actual de HomeScreen.kt]
[Pega aquí el código completo actual de ClassDetailScreen.kt]
[Pega aquí el código completo actual de ConfirmationScreen.kt]
[Pega aquí el código completo actual de ReservationsScreen.kt]
[Pega aquí el código completo actual de ProfileScreen.kt]

CORRECCIONES EN APPNAVIGATION.KT
- Los íconos del NavigationBar deben ser versión "outlined" (delineados, con Icons.Outlined en vez de Icons.Filled), no sólidos.
- El ítem seleccionado debe mostrar el ícono y el texto en verde oscuro (#00695C), y los no seleccionados en gris.
- Elimina el indicador de selección tipo píldora de NavigationBarItem, poniendo su color en Color.Transparent dentro de NavigationBarItemDefaults.colors(indicatorColor = Color.Transparent).
- Agrega transiciones de animación suaves entre pantallas en el NavHost (desplazamiento horizontal con desvanecimiento mediante slideInHorizontally + fadeIn y slideOutHorizontally + fadeOut).

CORRECCIONES EN HOMESCREEN.KT
- El encabezado superior verde oscuro debe ser un rectángulo recto, sin esquinas redondeadas abajo (quita el clip o shape redondeado que tiene actualmente).
- El fondo detrás del ícono de mancuerna en cada Card debe ser un rectángulo con esquinas redondeadas (forma de píldora rectangular), no un círculo perfecto.
- Las Card de clases deben tener elevation = 0 (sin sombra), fondo gris muy claro y esquinas redondeadas.
- Asegura que el título "Clases disponibles" y los nombres de las clases se vean en negro intenso (Color.Black con FontWeight.Bold), evitando tonos plomo o grises apagados.
- Configura el subtítulo de la clase de modo que en el filtro "Hoy" muestre la hora de forma clara (ej: "7:00 am · Sala 2") y en el filtro "Esta semana" muestre el día y la hora para mayor claridad (ej: "Jueves, 7:30 pm · Sala 3").

CORRECCIONES EN CLASSDETAILSCREEN.KT
- En el TopAppBar, la flecha de volver debe estar pegada inmediatamente a la izquierda del título "Detalle de clase", no separada.
- Confirma que la Card superior con la mancuerna tenga fondo verde muy claro, esquinas redondeadas suaves e ícono verde oscuro centrado.
- Confirma que el texto de cupos disponibles esté en verde oscuro.
- Confirma que el botón "Reservar cupo" esté al final de la pantalla, ancho completo, verde oscuro y esquinas redondeadas.
- Al presionar "Reservar cupo", la app debe vincular automáticamente la nueva reserva agregándola a la lista reactiva de "Mis reservas" con estado "Confirmada" y descontando 1 cupo disponible.

CORRECCIONES EN CONFIRMATIONSCREEN.KT
- Confirma que el círculo del ícono de check sea verde muy claro con el ícono en verde oscuro, bien centrado.
- Confirma que el botón "Ver mis reservas" tenga fondo gris claro neutro y texto gris oscuro.

CORRECCIONES EN RESERVATIONSSCREEN.KT
- Cada Card debe tener una barra vertical gruesa pegada al borde izquierdo: verde oscuro si el estado es "Confirmada", gris neutro si es "Completada".
- La etiqueta de estado "Confirmada" debe tener fondo verde claro y texto verde oscuro.
- La etiqueta de estado "Completada" debe tener fondo gris claro y texto gris.
- Asegura que la pantalla lea la lista reactiva de reservas para que las nuevas reservas creadas aparezcan automáticamente vinculadas.

CORRECCIONES EN PROFILESCREEN.KT
- Confirma que el avatar sea un círculo verde muy claro con las iniciales "DR" centradas, en verde oscuro y negrita.
- Las dos Card de estadísticas deben ser del mismo tamaño, lado a lado, fondo gris claro, con el número en negrita y prominente.

LO QUE NECESITO QUE HAGAS
- Aplica únicamente las correcciones puntuales listadas arriba sobre el código que ya tienes, sin cambiar el resto del diseño ni la estructura general que ya funciona.
- No cambies ningún nombre de ruta, función composable, parámetro, ni la lógica de navController.navigate() existente en ninguno de los 6 archivos.
- No inventes componentes ni uses librerías externas que no estén instaladas en mi proyecto.
- Dame el código Kotlin completo y corregido de los 6 archivos, listo para pegar directamente en mi proyecto.
- Después del código, explícame brevemente qué corregiste en cada archivo, relacionándolo con la corrección específica que pedí.
```

---

## Análisis de Cambios Realizados

Tras la ejecución consecutiva de los 4 prompts anteriores y la verificación comparativa con las maquetas de diseño, se logró un acabado profesional mediante las siguientes soluciones técnicas e interfaz:

### 1. Sistema de Diseño y Estilizado
* **Paleta cromática:** Se estandarizó la identidad visual utilizando un verde oscuro principal (`#00695C`) combinado con un verde pastel/claro (`#E0F2F1`) para fondos de íconos, badges de estado y avatares.
* **Tipografía e intensidad de contraste:** Se garantizó que el título *"Clases disponibles"* y los nombres de las clases utilicen `Color.Black` con `FontWeight.Bold`, evitando el tono plomo deslucido.
* **Formatos de fecha y hora:** Se ajustó la información secundaria de cada tarjeta para que en el filtro **"Hoy"** muestre únicamente la hora y sala (`7:00 am · Sala 2`), mientras que en **"Esta semana"** incluya también el día (`Jueves, 7:30 pm · Sala 3`).

### 2. Componentes y Navegación
* **AppNavigation.kt:**
  * Implementación de íconos delineados (`Icons.Outlined`).
  * Eliminación del fondo ovalado de selección por defecto en la barra mediante `indicatorColor = Color.Transparent`.
  * Integración de animaciones de desplazamiento horizontal y desvanecimiento cruzado (`slideInHorizontally` + `fadeIn` y `slideOutHorizontally` + `fadeOut`) al navegar entre pantallas.
* **HomeScreen.kt:**
  * Encabezado superior recto en verde oscuro con saludo personalizado (`"TECSUP Fit"` y `"Hola, Diego"`).
  * Chips de filtro interactivos (*Hoy* y *Esta semana*).
  * Contenedores rectangulares con esquinas redondeadas (`12.dp`) para alojar los íconos de mancuerna.
* **ClassDetailScreen.kt:**
  * `TopAppBar` personalizada con la flecha de regreso pegada al texto del título.
  * Banner superior con ícono grande de mancuerna centrado.
  * Botón *"Reservar cupo"* en verde oscuro en la parte inferior.
* **ConfirmationScreen.kt:**
  * Pantalla de confirmación centrada con ícono de check verde e información de la reserva realizada.
* **ReservationsScreen.kt:**
  * Tarjetas con barra lateral coloreada (*Verde oscuro* para *Confirmada* y *Gris* para *Completada*).
  * Badges redondeados indicando el estado actual de la clase.
* **RoutinesScreen.kt:**
  * Listado de rutinas de ejemplo (*Bíceps* y *Pecho*) con descripciones e íconos estilizados.
* **ProfileScreen.kt:**
  * Avatar circular grande con iniciales `"DR"`.
  * Tarjetas de estadísticas alineadas horizontalmente (*14 Clases* y *3 Rachas*).

### 3. Gestión de Estado y Vinculación
* **Listas reactivas:** La colección de reservas se convirtió en una lista reactiva de Compose (`mutableStateListOf`).
* **Sincronización automática:** Al presionar *"Reservar cupo"*, el sistema descuenta un cupo disponible en `HomeScreen` y registra la reserva automáticamente en `ReservationsScreen`, reflejándose de inmediato sin necesidad de recargar la aplicación.
