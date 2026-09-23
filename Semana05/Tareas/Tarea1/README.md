---

## Prompts Utilizados y Archivos Modificados

### Prompt 1: Inicio y Perfil del Médico

**Prompt:**
```text
CONTEXTO DEL PROYECTO:

Tengo un proyecto Android en Kotlin con Jetpack Compose, paquete com.rocha.tareatecsup, con navegación mediante Navigation Compose (NavController, NavHost, composable).

Uso componentes de Material3 (Scaffold, TopAppBar, Card, FilterChip, Surface, MaterialTheme).

Tengo implementado un menú lateral (ModalNavigationDrawer) en AppNavigation.kt que debe abrirse únicamente mediante el gesto de deslizar desde el borde izquierdo de la pantalla.

ARCHIVOS A MEJORAR:

ui/screens/HomeScreen.kt
ui/screens/DoctorProfileScreen.kt

ESTILO VISUAL Y FUNCIONALIDAD PARA HomeScreen.kt:

- Encabezado superior con fondo morado oscuro sólido (no degradado), esquinas inferiores redondeadas (24.dp). Debe incluir el título "Clínica Salud+" en blanco y negrita, y debajo "Hola, Juan" en un tono lila claro. Sin ningún botón de menú ni ícono "=".
- Chips de especialidad ("Todas", "Cardiología", "Pediatría", "Dermatología") en forma de píldora (RoundedCornerShape 50).
  - Chip no seleccionado: fondo lila muy claro y texto morado.
  - Chip seleccionado: fondo morado sólido y texto blanco.
  - FUNCIONALIDAD DE FILTRO: Al presionar un chip, la lista de médicos debe filtrarse dinámicamente según la especialidad elegida ("Todas" debe mostrar la lista completa).
- Sección "Médicos disponibles" con tarjetas (Card) individuales, esquinas redondeadas (16.dp), fondo blanco y elevación sutil (2.dp).
- A la izquierda de cada tarjeta, un ícono circular morado claro con un ícono de cruz médica morada dentro.
- Nombre del médico en negrita, especialidad debajo en gris, y a la derecha la estrella con la calificación en morado.
- Al hacer clic en la tarjeta, debe navegar a la pantalla de perfil usando navController.navigate(Screen.DoctorProfile.createRoute(doctor.id)).

ESTILO VISUAL Y FUNCIONALIDAD PARA DoctorProfileScreen.kt:

- TopAppBar con flecha de volver (Icons.AutoMirrored.Filled.ArrowBack) a la izquierda que ejecute navController.popBackStack() y el título "Perfil del médico".
- Ícono circular grande centrado con fondo lila claro y un ícono de cruz médica morada en el centro.
- Nombre del médico centrado, en negrita y tamaño grande.
- Especialidad y años de experiencia debajo en gris.
- Calificación con estrella y número de reseñas entre paréntesis.
- Párrafo de descripción del médico centrado en texto gris oscuro.
- NO debe mostrarse ningún ID ni texto técnico en la interfaz visual.
- Al final de la pantalla, un botón "Agendar cita" de ancho completo (fillMaxWidth), fondo morado sólido, esquinas redondeadas y texto blanco en negrita, que navegue a Screen.Appointment.createRoute(doctorId).
- Espaciado vertical adecuado entre cada elemento para una vista limpia.

LO QUE NECESITO QUE HAGAS:

- Mejora la presentación visual y la funcionalidad de los filtros en ambas pantallas.
- No cambies los nombres de las rutas ni las funciones composable existentes (HomeScreen y DoctorProfileScreen).
- No agregues ni muestres ningún texto de ID numérico en la pantalla.
- Utiliza componentes de Material3 como Card, FilterChip, Surface, Box, Icon, MaterialTheme.colorScheme y typography.
- Dame el código Kotlin completo de ambas pantallas listo para copiar y pegar directamente en mi proyecto.
- Al final, explícame brevemente los cambios realizados en cada pantalla.
```

**Archivos Modificados:**
- `app/src/main/java/com/rocha/tareatecsup/ui/screens/HomeScreen.kt`
- `app/src/main/java/com/rocha/tareatecsup/ui/screens/DoctorProfileScreen.kt`

---

### Prompt 2: Menú Lateral y Pantalla Mis Citas

**Prompt:**
```text
CONTEXTO DEL PROYECTO:

Tengo un proyecto Android en Kotlin con Jetpack Compose, paquete com.rocha.tareatecsup, con navegación funcionando mediante Navigation Compose.

- navigation/Screen.kt tiene las rutas
- navigation/AppNavigation.kt maneja el NavHost y el ModalNavigationDrawer (menú lateral)
- ui/screens/MyAppointmentsScreen.kt es la pantalla de "Mis citas"

ARCHIVOS A MEJORAR:
- navigation/AppNavigation.kt
- ui/screens/MyAppointmentsScreen.kt

ESTILO VISUAL QUE QUIERO PARA EL MENÚ LATERAL (ModalNavigationDrawer / ModalDrawerSheet):

- Encabezado del menú:
  - Un avatar circular en el lado izquierdo con fondo lila claro (#E1BEE7) y las iniciales "JP" en morado (#4A148C) en negrita.
  - Al lado, el nombre del usuario "Juan Pérez" en negrita y debajo el texto "Paciente" en gris.
  - Una línea divisoria horizontal (HorizontalDivider) separando el encabezado de la lista.
- Ítemes del menú ("Inicio", "Mis citas", "Historial médico", "Perfil"):
  - Cada ítem debe incluir a la izquierda un ícono de círculo/radio.
  - Ítem seleccionado ("Mis citas"): fondo en contenedor lila suave (#F3E5F5), texto en morado oscuro (#4A148C) en negrita y el círculo de la izquierda rellenado en morado.
  - Ítemes no seleccionados: fondo transparente, texto e ícono de círculo deseleccionado en gris oscuro (#424242).

ESTILO VISUAL QUE QUIERO PARA MyAppointmentsScreen.kt ("Mis citas"):

- Título principal "Mis citas" en la parte superior en negrita y tamaño grande (headlineSmall).
- Lista de citas en tarjetas (Card / Surface) con fondo gris muy claro suave (#F3F3F5) y esquinas redondeadas (16.dp):
  - A la izquierda de la tarjeta, una barra indicadora vertical de color morado oscuro (#4A148C) de 4.dp de ancho.
  - Dentro de la tarjeta:
    - Nombre del médico en negrita ("Dra. Ana Torres", "Dr. Luis Vega").
    - Fecha y hora debajo en texto gris ("Viernes 27, 10:30 am", "Miércoles 15, 3:00 pm").
    - Chip / Badge de estado:
      - Para "Confirmada": fondo verde claro (#E8F5E9) con texto en verde teal (#00897B) o verde oscuro.
      - Para "Completada": fondo gris claro (#E0E0E0) con texto en gris (#616161).

LO QUE NECESITO QUE HAGAS:

- Mejora la presentación visual del menú lateral en AppNavigation.kt y de la pantalla MyAppointmentsScreen.kt para que coincidan exactamente con la imagen.
- Mantén la lógica de navegación existente entre las pantallas (Screen.Home, Screen.MyAppointments, Screen.MedicalHistory, Screen.Profile).
- Usa componentes de Material3 como ModalDrawerSheet, NavigationDrawerItem, Card, Surface, AssistChip / SuggestionChip y MaterialTheme.
- Dame el código Kotlin completo de ambos archivos (AppNavigation.kt y MyAppointmentsScreen.kt) listo para copiar y pegar en mi proyecto.
- Al final, explícame brevemente qué modificaste en cada archivo.
```

**Archivos Modificados:**
- `app/src/main/java/com/rocha/tareatecsup/navigation/AppNavigation.kt`
- `app/src/main/java/com/rocha/tareatecsup/ui/screens/MyAppointmentsScreen.kt`

---

### Prompt 3: Agendar Cita, Confirmación, Perfil e Historial Médico

**Prompt:**
```text
CONTEXTO DEL PROYECTO:

Tengo un proyecto Android en Kotlin con Jetpack Compose, paquete com.rocha.tareatecsup, con navegación funcionando mediante Navigation Compose.

- navigation/Screen.kt maneja las rutas y los argumentos enviados.
- navigation/AppNavigation.kt tiene el NavHost.

ARCHIVOS A MEJORAR:
- ui/screens/AppointmentScreen.kt
- ui/screens/ConfirmationScreen.kt
- ui/screens/ProfileScreen.kt
- ui/screens/MedicalHistoryScreen.kt

ESTILO VISUAL QUE QUIERO PARA AppointmentScreen.kt ("Agendar cita"):

- TopAppBar en la parte superior con flecha de volver (Icons.AutoMirrored.Filled.ArrowBack) a la izquierda y título "Agendar cita" en negrita.
- Fondo de pantalla blanco (Color.White).
- Sección "Selecciona fecha":
  - Título "Selecciona fecha" en gris (Color.Gray) y texto semi-negrita.
  - Fila (Row) con 3 tarjetas redondeadas (RoundedCornerShape 16.dp) distribuidas equitativamente:
    - Tarjetas deseleccionadas: fondo gris claro (#F3F3F5), con el día abreviado arriba en gris ("Jue", "Sáb") y el número de fecha abajo ("26", "28") en negrita oscura.
    - Tarjeta seleccionada ("Vie 27"): fondo morado oscuro sólido (#4A148C), con el día en texto blanco semitransparente y el número ("27") en blanco en negrita (18.sp).
- Sección "Selecciona hora":
  - Título "Selecciona hora" en gris.
  - Fila (Row) con 3 tarjetas redondeadas:
    - Horas deseleccionadas ("9:00", "3:00"): fondo gris claro (#F3F3F5) con texto oscuro.
    - Hora seleccionada ("10:30"): fondo morado oscuro sólido (#4A148C) con texto blanco en negrita.
- Botón "Confirmar cita" al final de la pantalla (fillMaxWidth, altura 54.dp, RoundedCornerShape 16.dp), con fondo morado oscuro (#4A148C) y texto blanco en negrita.
- Al hacer clic, debe navegar enviando el nombre real del médico (doctorName), la fecha elegida y la hora a Screen.Confirmation.

ESTILO VISUAL QUE QUIERO PARA ConfirmationScreen.kt ("¡Cita agendada!"):

- Contenido totalmente centrado vertical y horizontalmente en pantalla blanca.
- Ícono superior: Círculo verde claro (#E8F5E9) de 88.dp con un ícono de check (Icons.Default.Check) verde teal (#00897B) centrado.
- Título principal "¡Cita agendada!" en negrita y grande (headlineMedium).
- Debe mostrar dinámicamente el NOMBRE DEL MÉDICO recibido por parámetro (ej. "Dra. Ana Torres" o "Dr. Luis Vega"), NO el ID.
- Fecha y hora formateada debajo (ej. "Viernes 27, 10:30 am") en gris.
- Botón "Ver mis citas": centrado, fondo gris claro (#F3F3F5), bordes redondeados (16.dp) y texto en color gris oscuro (#424242). Al presionar, navega a la pantalla de mis citas (Screen.MyAppointments).

ESTILO VISUAL QUE QUIERO PARA ProfileScreen.kt ("Perfil"):

- Título "Perfil" en negrita grande.
- Avatar circular centrado con iniciales "JP" en fondo lila claro (#E1BEE7).
- Nombre "Juan Pérez" y etiqueta "Paciente".
- Tarjeta personal con correo y teléfono con íconos teñidos en morado.
- Sin botón de cerrar sesión ni datos innecesarios.

ESTILO VISUAL QUE QUIERO PARA MedicalHistoryScreen.kt ("Historial médico"):

- Título "Historial médico" en negrita.
- Tarjetas redondeadas (#F3F3F5) con consultas pasadas completadas, fecha, médico tratante y resumen de diagnóstico.

LO QUE NECESITO QUE HAGAS:

- Implementa la presentación visual y la lógica de transferencia del nombre del médico, fecha y hora entre las pantallas.
- No muestres ningún ID numérico en la interfaz visual.
- Usa componentes de Material3 (Scaffold, TopAppBar, Surface, Button, Card, Icon, MaterialTheme).
- Dame el código Kotlin completo de las pantallas listo para usar.
```

**Archivos Modificados:**
- `app/src/main/java/com/rocha/tareatecsup/ui/screens/AppointmentScreen.kt`
- `app/src/main/java/com/rocha/tareatecsup/ui/screens/ConfirmationScreen.kt`
- `app/src/main/java/com/rocha/tareatecsup/ui/screens/ProfileScreen.kt`
- `app/src/main/java/com/rocha/tareatecsup/ui/screens/MedicalHistoryScreen.kt`
- `app/src/main/java/com/rocha/tareatecsup/navigation/Screen.kt`

---

## Ajustes y Correcciones Solicitadas tras la Ejecución de los Prompts

Tras la ejecución inicial de los prompts base, se identificaron detalles específicos de interfaz, lógica y navegación que requerían correcciones adicionales. A continuación se resumen los ajustes solicitados y aplicados:

### 1. Ajustes en Inicio (HomeScreen.kt) y Perfil del Médico (DoctorProfileScreen.kt)
- **Eliminación del Botón "="**: Se solicitó remover el botón de ícono de menú en la cabecera superior de la Home para que la apertura del menú lateral sea exclusivamente a través del gesto de deslizamiento (swipe).
- **Filtros Funcionales**: Se requirió hacer interactivos los chips de especialidades ("Todas", "Cardiología", "Pediatría", "Dermatología"), de modo que al seleccionar cada uno la lista de médicos se filtre dinámicamente.
- **Ocultar IDs Numéricos**: Se pidió quitar cualquier texto técnico o ID expuesto en pantalla (ej. ID del médico recibido: 1), mostrando únicamente el nombre real del doctor.
- **Unificación del Ícono de Cruz Médica**: Se solicitó que el ícono de cruz médica usado en el avatar del perfil del doctor fuera exactamente el mismo (Icons.Default.LocalHospital) que se muestra en las tarjetas de la lista principal.

### 2. Ajustes en Mis Citas (MyAppointmentsScreen.kt) e Historial Médico (MedicalHistoryScreen.kt)
- **Lógica de la Línea Lateral Morada**: Se solicitó que la barra indicadora vertical morada (#4A148C) en el extremo izquierdo de las tarjetas aparezca únicamente para las citas activas ("Confirmada" o "Pendiente"), y no en citas completadas.
- **Separación de Citas y Consultas Pasadas**:
  - **Mis Citas**: Debe contener únicamente las citas pendientes o confirmadas cercanas que el paciente tiene activas.
  - **Historial Médico**: Debe almacenar las consultas médicas ya realizadas ("Completada"), con sus observaciones clínicas, diagnóstico y nombre del profesional.

### 3. Ajustes en Agendar Cita (AppointmentScreen.kt) y Confirmación (ConfirmationScreen.kt)
- **Estado Inicial en "Pendiente"**: Se pidió que al agendar automáticamente una nueva cita, su estado inicial por defecto se registre como "Pendiente" en lugar de "Confirmada".
- **Unificación de Formato de Fechas**: Se ajustó el formato para que en todas las pantallas las fechas se muestren de forma homogénea (ej. "Viernes 27, 10:30 am").
- **Nombre Real del Médico**: Se corrigió el traspaso de información en la navegación para que al confirmar se pase el nombre real del médico (ej. "Dra. Ana Torres") a la alerta y al listado de citas, evitando cadenas genéricas.
- **Redirección del Botón "Ver mis citas"**: Se corrigió la acción del botón "Ver mis citas" en la alerta de confirmación para que navegue directamente a la pantalla de Mis Citas (Screen.MyAppointments.route).

### 4. Ajustes en Perfil (ProfileScreen.kt)
- **Depuración de Campos**: Se solicitó eliminar el campo de "Grupo sanguíneo" y el botón "Cerrar sesión", dejando únicamente los datos principales de contacto (Correo electrónico y Teléfono).

### 5. Ajustes de Estructura y Configuración del Proyecto
- **Configuración de Tema en Manifiesto**: Se añadió android:theme="@android:style/Theme.Material.Light.NoActionBar" en AndroidManifest.xml para evitar que la barra nativa duplicada del sistema tape las pantallas de Jetpack Compose.
- **Simplificación de Paquetes**: Se solicitó eliminar cualquier paquete externo como data o archivos adicionales de modelo, manteniendo el proyecto organizado únicamente dentro de navigation y ui.screens.

---

## Prompt: Generación Completa del Proyecto

A continuación se presenta el prompt maestro unificado que condensa todos los requisitos, maquetados, flujos y ajustes finales en una sola instrucción ejecutable:

```text
CONTEXTO DEL PROYECTO:

Tengo un proyecto Android en Kotlin con Jetpack Compose, paquete com.rocha.tareatecsup, con navegación funcionando mediante Navigation Compose (NavController, NavHost, composable).

Uso componentes de Material3 (Scaffold, TopAppBar, Card, FilterChip, Surface, Button, Icon, MaterialTheme).

ESTRUCTURA DE PAQUETES Y ARCHIVOS DEL PROYECTO:

- com.rocha.tareatecsup (MainActivity.kt)
- com.rocha.tareatecsup.navigation (AppNavigation.kt, Screen.kt)
- com.rocha.tareatecsup.ui.screens (HomeScreen.kt, DoctorProfileScreen.kt, AppointmentScreen.kt, ConfirmationScreen.kt, MyAppointmentsScreen.kt, MedicalHistoryScreen.kt, ProfileScreen.kt)

No debe existir ningún paquete 'data' ni clases de modelo en archivos externos; los datos y modelos deben residir dentro de los paquetes indicados.

REQUISITOS DE NAVEGACIÓN Y CONFIGURACIÓN:

- En AndroidManifest.xml debe aplicarse android:theme="@android:style/Theme.Material.Light.NoActionBar" para evitar barras nativas duplicadas del sistema.
- En gradle.properties debe configurarse android.useAndroidX=true y memoria org.gradle.jvmargs=-Xmx2048m.
- AppNavigation.kt maneja el NavHost y el ModalNavigationDrawer (menú lateral).
- El menú lateral debe incluir un encabezado con avatar circular lila (#E1BEE7) con las iniciales "JP" en morado (#4A148C), nombre "Juan Pérez" y rol "Paciente".
- Los ítems del menú ("Inicio", "Mis citas", "Historial médico", "Perfil") deben incluir íconos circulares/radio en el extremo izquierdo. El ítem seleccionado resalta con fondo contenedor lila suave (#F3E5F5) y texto morado.
- El menú lateral debe abrirse únicamente mediante el gesto de deslizar (swipe) desde el borde izquierdo de la pantalla.

ESTILO VISUAL Y FUNCIONALIDAD DE PANTALLAS:

1. HomeScreen.kt ("Inicio"):
   - Encabezado superior con fondo morado oscuro sólido (#4A148C) y esquinas inferiores redondeadas (24.dp), con título "Clínica Salud+" en blanco y negrita, y debajo "Hola, Juan" en lila claro. Sin ningún botón de menú ni ícono "=".
   - Chips de especialidad ("Todas", "Cardiología", "Pediatría", "Dermatología") en forma de píldora (RoundedCornerShape 50). El seleccionado resalta en morado sólido con texto blanco.
   - FILTRO FUNCIONAL: Al presionar un chip, la lista de médicos debe filtrarse dinámicamente según la especialidad elegida ("Todas" muestra la lista completa).
   - Tarjetas individuales de médicos (Card / Surface) con esquinas redondeadas (16.dp), fondo blanco y elevación sutil (2.dp).
   - A la izquierda de cada tarjeta, un ícono circular morado claro con el ícono de cruz médica (Icons.Default.LocalHospital) en morado.
   - Nombre del médico en negrita, especialidad debajo en gris, y calificación con estrella a la derecha en morado.
   - Clic en la tarjeta navega a Screen.DoctorProfile.createRoute(doctor.id).

2. DoctorProfileScreen.kt ("Perfil del médico"):
   - TopAppBar con flecha de volver (Icons.AutoMirrored.Filled.ArrowBack) a la izquierda (navController.popBackStack()) y título "Perfil del médico".
   - Ícono circular grande centrado con fondo lila claro (#E1BEE7) y el mismo ícono de cruz médica morada (Icons.Default.LocalHospital) en el centro.
   - Nombre del médico centrado en negrita (ej. "Dra. Ana Torres"), especialidad y experiencia debajo en gris ("Cardióloga · 12 años exp."), y calificación ("⭐ 4.9 (128 reseñas)").
   - Párrafo de descripción del médico centrado en texto gris.
   - NO debe mostrarse ningún ID numérico ni texto técnico en pantalla.
   - Botón "Agendar cita" al final de ancho completo (fillMaxWidth, altura 54.dp, RoundedCornerShape 16.dp), fondo morado oscuro (#4A148C) y texto blanco en negrita, que navegue a Screen.Appointment.createRoute(doctor.id).

3. AppointmentScreen.kt ("Agendar cita"):
   - TopAppBar con título "Agendar cita" y botón de volver.
   - Selección de fecha ("Selecciona fecha") con tarjetas redondeadas (16.dp): muestran el día abreviado arriba ("Jue", "Vie", "Sáb") y el número de día abajo ("26", "27", "28"). La opción seleccionada resalta en morado oscuro (#4A148C) con texto blanco.
   - Selección de hora ("Selecciona hora") con tarjetas redondeadas para "9:00", "10:30", "3:00". La seleccionada resalta en morado oscuro.
   - Botón "Confirmar cita" al final de ancho completo, fondo morado oscuro (#4A148C) y texto blanco en negrita.
   - Al confirmar, debe registrar automáticamente la nueva cita en la lista activa de "Mis citas" con el estado por defecto "Pendiente" y el formato unificado de fecha (ej. "Viernes 27, 10:30 am"), y luego navegar a Screen.Confirmation.

4. ConfirmationScreen.kt ("¡Cita agendada!"):
   - Contenido totalmente centrado en pantalla blanca.
   - Círculo verde claro (#E8F5E9) de 88.dp con ícono de check (Icons.Default.Check) verde teal (#00897B) centrado.
   - Título "¡Cita agendada!" en negrita grande.
   - Muestra dinámicamente el NOMBRE REAL DEL MÉDICO recibido por parámetro (ej. "Dra. Ana Torres"), NO el ID ni etiquetas genéricas.
   - Fecha y hora formateadas (ej. "Viernes 27, 10:30 am") en gris.
   - Botón "Ver mis citas" centrado con fondo gris claro (#F3F3F5) y bordes redondeados (16.dp) que redirige directamente a la pantalla de Mis Citas (Screen.MyAppointments.route).

5. MyAppointmentsScreen.kt ("Mis citas"):
   - Título principal "Mis citas" en negrita.
   - Muestra ÚNICAMENTE citas activas cercanas ("Confirmada" o "Pendiente").
   - Tarjetas de citas en fondo gris claro (#F3F3F5) con esquinas redondeadas (16.dp).
   - En el borde izquierdo de la tarjeta, una barra indicadora vertical morada (#4A148C) de 4.dp de ancho presente en todas las citas activas.
   - Badge de estado redondeado: "Confirmada" en fondo verde claro (#E8F5E9) con texto verde, "Pendiente" en fondo naranja claro (#FFF3E0) con texto naranja.

6. MedicalHistoryScreen.kt ("Historial médico"):
   - Título principal "Historial médico" en negrita.
   - Muestra las consultas pasadas ya realizadas ("Completada").
   - Tarjetas redondeadas (#F3F3F5) con fecha, título de la consulta, nombre del médico tratante, resumen diagnóstico y badge "Completado".

7. ProfileScreen.kt ("Perfil"):
   - Título "Perfil" en negrita.
   - Avatar circular centrado en lila claro (#E1BEE7) con iniciales "JP" en morado (#4A148C).
   - Nombre "Juan Pérez" y rol "Paciente".
   - Tarjeta de datos de contacto con Correo electrónico y Teléfono con íconos teñidos en morado. Sin grupo sanguíneo ni botón de cerrar sesión.

LO QUE NECESITO QUE HAGAS:

- Genera y actualiza todo el código fuente del proyecto aplicando exactamente todas las especificaciones de diseño, flujo y navegación indicadas.
- Mantén la coherencia visual con la paleta de colores morados (#4A148C, #6A1B9A, #E1BEE7, #F3E5F5).
- Dame el código fuente completo de todos los archivos necesarios listo para usar.
```
