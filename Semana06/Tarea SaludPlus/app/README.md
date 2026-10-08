# Clínica SaludPlus
---

## Prompt 1: Lógica de cálculo de fechas hábiles (Fechas.kt)

```text
Contexto: Crea un archivo nuevo llamado Fechas.kt en el paquete com.rocha.saludplus.ui.components.
Implementa exclusivamente la lógica de cálculo de fechas con java.time para manejar días hábiles: Requerimientos técnicos y funcionales:
1. Define la función: fun diasHabiles(semana: Int): List<LocalDate>
2. La función debe retornar exactamente 5 días consecutivos de lunes a viernes (excluyendo sábados y domingos).
3. Lógica para la Semana 0 (semana actual):
   - Si la fecha actual (LocalDate.now()) es un día hábil (lunes a viernes), los 5 días deben iniciar en el día de hoy.
   - Si hoy es sábado o domingo, la semana debe iniciar obligatoriamente el lunes de la semana siguiente.
4. Lógica para la Semana n (donde n >= 1):
   - Se deben tomar los mismos 5 días hábiles calculados para la semana base (Semana 0) y desplazarlos exactamente 7 * n días hacia adelante usando plusDays().
5. Añade comentarios explicativos breves en cada paso crítico del algoritmo (por ejemplo, manejo del fin de semana y desplazamiento por semanas).
6. Agrega todos los imports necesarios de java.time (LocalDate, DayOfWeek, etc.).
```

## Prompt 2: Funciones auxiliares de formato de texto en español (Fechas.kt)

```text
Contexto: A continuación del código existente en Fechas.kt, agrega tres funciones auxiliares de formato de texto en español, asegurando que no dependan del Locale del dispositivo para evitar incongruencias de idioma:
1. fun nombreMes(fecha: LocalDate): String
   - Debe retornar el nombre del mes y el año con la primera letra en mayúscula (ejemplo: "Octubre 2026").
   - Utiliza una estructura interna o lista estática en español. Nota obligatoria: El mes 9 debe escribirse estrictamente como "Setiembre" (con 't', no "septiembre").
2. fun nombreDiaCorto(fecha: LocalDate): String
   - Debe retornar la abreviatura de 3 letras correspondiente al día de la semana: "Lun", "Mar", "Mié", "Jue", "Vie", "Sáb", "Dom".
3. fun formatearFechaLarga(fecha: String): String
   - Recibe un String con formato ISO estándar "yyyy-MM-dd" (por ejemplo, "2026-09-16").
   - Debe parsearlo de manera segura (con manejo de excepciones o runCatching) y transformarlo a un formato largo en español, por ejemplo: "Miércoles 16 de setiembre 2026".
   - Si el String recibido es nulo, vacío o tiene un formato inválido, debe retornar el texto original sin lanzar excepciones ni crashear la app.
```

## Prompt 3: Componente reutilizable ChipDia (Componentes.kt)

```text
Contexto: Te comparto el contenido actual de Componentes.kt. Añade al final del archivo un nuevo Composable llamado ChipDia, manteniendo idéntico el estilo visual, los colores y las convenciones de Material 3 que ya usa ChipHorario:
@Composable fun ChipDia(nombre: String, numero: String, seleccionado: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier)
Especificaciones de diseño UI (Material 3):
- Envuelve el contenido en un Card interactivo con su respectivo parámetro onClick.
- En su interior, coloca una Column con alineación horizontal centrada (Alignment.CenterHorizontally) y un padding vertical interno de 8.dp.
- Primera línea de texto: muestra el nombre del día utilizando tipografía bodySmall.
- Segunda línea de texto: muestra el número del día utilizando tipografía titleMedium con estilo en negrita (fontWeight = FontWeight.Bold).
- Comportamiento de color según el estado de selección:
  * Si seleccionado es true: el contenedor usa MaterialTheme.colorScheme.primary y el texto usa MaterialTheme.colorScheme.onPrimary.
  * Si seleccionado es false: el contenedor usa MaterialTheme.colorScheme.surfaceVariant y el texto usa MaterialTheme.colorScheme.onSurfaceVariant.
- Mantén limpios los imports, reutilizando los existentes y agregando únicamente los strictly necesarios para Compose.
```

## Prompt 4: Integración del calendario dinámico (FechaHoraScreen.kt)

```text
Contexto: Usa el archivo FechaHoraScreen.kt. Ya disponemos en el proyecto de diasHabiles(semana), nombreMes(fecha), nombreDiaCorto(fecha) y el componente ChipDia. Actualiza la pantalla para incorporar el calendario dinámico con las siguientes pautas:
Gestión de Estado:
- Elimina la lista estática anterior de días y el texto quemado del mes ("Octubre 2026").
- Declara el estado de la semana actual: var semana by remember { mutableStateOf(0) }.
- Obtiene la lista de días con: val dias = diasHabiles(semana).
- Mantén fechaSeleccionada como String en formato "yyyy-MM-dd", inicializándola por defecto en dias[0].toString().
- Mantén horaSeleccionada como String inicializada en vacío ("").
Estructura de la Cabecera del Calendario (debajo de la tarjeta del médico):
- Diseña un Row que contenga:
  i. Un IconButton con el ícono Icons.AutoMirrored.Filled.KeyboardArrowLeft para retroceder de semana. Este botón debe estar explícitamente deshabilitado (enabled = semana > 0) cuando estemos en la semana actual.
  ii. Un Text centrado en el medio que muestre dinámicamente nombreMes(dias[0]) usando tipografía titleMedium.
  iii. Un IconButton con el ícono Icons.AutoMirrored.Filled.KeyboardArrowRight para avanzar de semana (incrementando semana en 1).
- Lógica de cambio de semana: Al presionar cualquier flecha, la variable semana se actualiza, fechaSeleccionada se actualiza automáticamente al primer día del nuevo rango (dias[0].toString()), y horaSeleccionada se limpia a "".
Fila de selección de días:
- Dibuja un Row con los 5 días de la lista utilizando el componente ChipDia (donde nombre = nombreDiaCorto(dia), numero = dia.dayOfMonth.toString(), y modifier = Modifier.weight(1f)).
- El parámetro seleccionado debe evaluar si dia.toString() == fechaSeleccionada.
- Al hacer clic en un ChipDia específico: se actualiza fechaSeleccionada = dia.toString() y se reinicia horaSeleccionada = "".
Restricciones estrictas:
- No alteres la tarjeta del médico, la grilla LazyVerticalGrid de horarios, el componente MensajeVacio, el botón "Continuar" ni la lógica de navegación.
- Los horarios disponibles deben continuar consultándose mediante Repositorio.horariosDisponibles(medicoId, fechaSeleccionada).
```

## Prompt 5: Formateo de fecha en confirmación (ConfirmarCitaScreen.kt)

```text
Contexto: Usa el archivo ConfirmarCitaScreen.kt. Dado que ya se encuentra implementada en el proyecto la función utilitaria formatearFechaLarga(fecha: String): String, realiza un único cambio focalizado:
- Ubica la sección donde se renderiza la fila resumen de la cita (específicamente en la llamada a FilaDato para el campo "Fecha").
- Reemplaza la función anterior formatearFecha(fecha) por formatearFechaLarga(fecha).
- Esto garantizará que en la Pantalla 7 se visualice la fecha completa en español, por ejemplo: "Martes 16 de setiembre 2026".
Restricciones estrictas:
- No modifiques ni elimines la función formatearFecha original, ya que otras pantallas del sistema (como Mis Citas, Detalle y Notificaciones) la siguen requiriendo estrictamente en formato numérico dd/MM/yyyy.
```

## Prompt 6: Paleta clínica y estilos globales (Color.kt, Theme.kt y Componentes.kt)

```text
Ajusta la paleta de colores y los componentes reutilizables para replicar el estilo visual clínico de la maqueta:
1. Modifica la paleta de colores del tema:
   - Primary: Azul clínico brillante (#0D6EFD o #1977F2).
   - OnPrimary: Blanco puro (#FFFFFF).
   - Background y Surface: Blanco (#FFFFFF) o gris ultra claro (#F8F9FA).
   - SurfaceVariant: Fondo de chips/tarjetas secundarias en tono suave (#F1F3F5).
   - OnSurfaceVariant: Texto secundario gris neutral (#6C757D).
2. Modifica ChipHorario en Componentes.kt:
   - Mantén exactamente los mismos parámetros.
   - Si seleccionado == true: containerColor debe ser MaterialTheme.colorScheme.primary y el texto color onPrimary (blanco).
   - Si seleccionado == false: containerColor debe ser surfaceVariant (#F1F3F5) o blanco con borde tenue, y texto onSurface.
   - Asegura bordes redondeados (RoundedCornerShape(8.dp) a 12.dp) idénticos a los de ChipDia.
3. Modifica BotonPrincipal en Componentes.kt:
   - Botón azul sólido con bordes redondeados (RoundedCornerShape(12.dp)) y altura táctil estándar (aprox. 50.dp).
```

## Prompt 7: Tarjetas médicas y calificaciones (Componentes.kt y FechaHoraScreen.kt)

```text
Actualiza el diseño visual de las tarjetas médicas para que incluyan la estructura completa que se muestra en la referencia:
1. En TarjetaMedico y en la tarjeta resumen del médico de FechaHoraScreen:
   - Coloca un contenedor circular para el avatar del médico a la izquierda (Box circular con borde o Surface circular con iniciales/ícono de doctor o imagen vectorial si no hay drawable).
   - Al lado derecho del avatar, alinea verticalmente:
     * Nombre del médico en negrita (titleMedium).
     * Especialidad en color grisáceo (bodySmall).
     * Fila con ícono de estrella dorada/amarilla (Icons.Default.Star), calificación numérica (ej. "4.9") y número de reseñas entre paréntesis (ej. "(124)").
     * Insignia o chip pequeño de estado en color verde tenue ("Disponible hoy" o "Próximos horarios").
   - La tarjeta debe tener esquinas redondeadas (RoundedCornerShape(12.dp)) y elevación sutil o fondo limpio sobre surfaceVariant.
```

## Prompt 8: Iconografía y colegiatura en resumen (ConfirmarCitaScreen.kt)

```text
Actualiza el diseño de la Pantalla 7 (ConfirmarCitaScreen.kt) y los componentes necesarios para que sea idéntico a la imagen de referencia:
1. Cabecera médica en ConfirmarCitaScreen.kt:
   - Dentro de la tarjeta superior o antes del resumen, incluye el avatar circular del médico (Surface circular con CircleShape, ícono de persona y fondo primaryContainer).
   - Al lado del avatar: Nombre del médico en negrita (titleMedium), Especialidad en texto secundario (bodySmall) y código de colegiatura "CMP: 123456" en tono gris.
2. Iconografía en los datos de la cita (Componentes.kt y ConfirmarCitaScreen.kt):
   - Modifica o sobrecarga FilaDato para que acepte un parámetro opcional icono: ImageVector? = null.
   - En cada fila, si el ícono está presente, muéstralo a la izquierda con un contenedor pequeño o tint azul tenue (primary) de tamaño 20.dp a 24.dp, seguido de una columna con la etiqueta en gris y el valor en texto principal.
   - Aplica los siguientes datos e íconos en el resumen de ConfirmarCitaScreen:
     * Fecha: Icons.Default.DateRange con formatearFechaLarga(fecha).
     * Hora: Icons.Default.Schedule con el rango (ejemplo: "09:30 a 10:00" o "$hora").
     * Tipo de atención: Icons.Default.Person con "Consulta presencial".
     * Dirección: Icons.Default.LocationOn o Place con "Av. Los Olivos 123, Lima".
3. Campo de texto y botón:
   - El campo "Motivo de la consulta (opcional)" debe tener estilo OutlinedTextField limpio con bordes redondeados (12.dp) y placeholder "Consulta de rutina".
   - Botón "Agendar cita" azul sólido con RoundedCornerShape(12.dp) y altura 50.dp.
```

## Prompt 9: Tarjetas pastel y accesos rápidos (HomeScreen.kt, EspecialidadesScreen.kt y Auth)

```text
Ajusta las pantallas principales (Home, Especialidades y Auth) para que compartan la misma identidad visual clínica y colorimetría que se observa en la maqueta:
1. En HomeScreen.kt (Accesos rápidos temáticos):
   - Actualiza los 4 accesos rápidos ("Agendar cita", "Mis citas", "Mis datos", "Resultados") para que cada tarjeta tenga un fondo pastel suave diferenciado y su respectivo ícono temático centrado:
     * Agendar cita: fondo azul pastel (#E3F2FD), ícono de calendario o agregar en azul (#1976D2).
     * Mis citas: fondo verde pastel (#E8F8F0), ícono de citas/reloj en verde (#388E3C).
     * Mis datos: fondo lila/morado pastel (#F3E5F5), ícono de usuario en morado (#7B1FA2).
     * Resultados: fondo naranja/ámbar pastel (#FFF3E6), ícono de documento o check en naranja (#F57C00).
   - En "Especialidades destacadas", las tarjetas horizontales deben tener fondo blanco con borde suave, ícono centrado y el nombre debajo.
2. En EspecialidadesScreen.kt:
   - Las tarjetas de especialidad (TarjetaEspecialidad) deben incluir un contenedor circular o cuadrado redondeado a la izquierda con un ícono alusivo (Medicina General, Pediatría, Ginecología, etc.) con tintes diferenciados, flecha ">" a la derecha (KeyboardArrowRight) y fondo blanco/gris ultra tenue.
3. En LoginScreen.kt y RegistroScreen.kt:
   - Los campos de texto OutlinedTextField deben usar RoundedCornerShape(12.dp) con fondo blanco.
   - Los botones principales "Iniciar sesión" y "Registrarme" deben ser azules sólidos (#0D6EFD) con texto en negrita y altura de 50.dp, manteniendo la consistencia con las demás pantallas.
```

## Prompt 10: Integración de recursos gráficos e imágenes reales (Modelos, Repositorio y Pantallas)

```text
Necesito que el diseño visual sea idéntico a la maqueta de referencia, reemplazando los íconos genéricos por imágenes reales y recursos gráficos específicos:
1. MODELO Y REPOSITORIO (Medico.kt, Especialidad.kt y Repositorio.kt):
   - Agrega a la data class Medico el campo fotoResId: Int (o un identificador drawable de Android R.drawable). Asigna a cada uno de los médicos su respectiva foto o drawable de perfil (Dra. Ana Torres, Dra. Claudia Rojas, Dr. Luis Ramírez, Dra. Mariana Soto, etc.).
   - Agrega a la data class Especialidad el campo iconoResId: Int (o color distintivo e ilustración correspondiente: Medicina General, Pediatría, Ginecología, etc.).
2. PANTALLA 1 — SPLASH SCREEN (SplashScreen.kt):
   - En lugar de un icono genérico de persona, implementa la ilustración central del médico con bata, estetoscopio y carpeta azul usando un componente Image(painter = painterResource(id = ...)) con contentScale = ContentScale.Fit, altura aproximada de 260.dp a 280.dp, centrado y con esquinas o fondo suave idéntico a la Pantalla 1.
   - Si no se cuenta aún con el PNG en res/drawable, crea un fallback gráfico en Compose que dibuje la tarjeta ilustrada con el médico y su carpeta mediante vectores o una función Composable IlustracionMedicoSplash() que cargue R.drawable.doctor_splash con manejo seguro.
   - En la parte superior coloca el logotipo de la cruz médica (azul y cian) con el texto "Clínica SaludPlus" y el subtítulo "Tu salud, nuestra prioridad".
3. PANTALLAS 3 Y 4 — ESPECIALIDADES (HomeScreen.kt y EspecialidadesScreen.kt):
   - En "Especialidades destacadas" (Home) y en la lista de EspecialidadesScreen, reemplaza los íconos grises por los avatares ilustrados a color: Medicina General, Pediatría, Ginecología, Cardiología, Dermatología, Traumatología, Oftalmología.
   - En las tarjetas de EspecialidadesScreen, la imagen o ícono debe estar dentro de un círculo con fondo del mismo color pero en tonalidad pastel suave.
4. PANTALLAS 5, 6 Y 7 — AVATARES REALES DE MÉDICOS (MedicosScreen.kt, FechaHoraScreen.kt, ConfirmarCitaScreen.kt y Componentes.kt):
   - En TarjetaMedico y en la cabecera médica de FechaHoraScreen y ConfirmarCitaScreen, sustituye el Icon(Icons.Default.Person) por un avatar circular real con borde blanco y elevación.
   - Asegura que la estrella de calificación sea amarilla (#FFB800), el número de reseñas esté entre paréntesis "(124)", y el badge verde pastel con texto verde ("Disponible hoy" o "Próximos horarios") esté ubicado en la esquina derecha de la tarjeta como en la imagen.
Entrega los archivos actualizados asegurando que si los recursos de imagen drawable aún no están en la carpeta res/drawable, el código compile sin error usando drawables vectoriales creados en Compose o referencias seguras.
```

## Prompt 11: Rediseño visual centrado de la pantalla de perfil (PerfilScreen.kt)

```text
Modifica el archivo PerfilScreen.kt en el paquete com.rocha.saludplus.ui.perfil para actualizar su diseño visual centrado:
Requerimientos de UI y maquetación:
1. Avatar centrado en la parte superior:
   - Un Surface circular (CircleShape) de 90.dp centrado horizontalmente con borde suave en azul claro (Color(0xFFE6F2FF)) y elevación ligera.
   - Dentro del círculo, coloca un ícono de usuario (Icons.Default.Person) en color azul primario (#1877F2) con tamaño de 54.dp (o Image si existe un recurso en drawable).
2. Nombre y subtítulo:
   - Justo debajo de la imagen circular, muestra el nombre completo del usuario centrado (titleLarge, negrita, color #0F1E36).
   - Abajo del nombre, un texto secundario en color gris (#758A99) que diga "Paciente registrado" (bodySmall).
3. Tarjeta contenedora de datos:
   - Un Card blanco (RoundedCornerShape(16.dp)) con borde gris tenue (#EFEFEF) y padding interno de 16.dp.
   - En su interior, organiza verticalmente los datos del paciente usando FilaDato con sus respectivos íconos:
     * Teléfono: Icons.Default.Phone
     * Correo: Icons.Default.Email
     * Citas agendadas: Icons.Default.EventAvailable con el total de citas activas del usuario ($totalCitas).
4. Botón de acción:
   - Botón principal "Cerrar sesión" en la parte inferior que limpie la sesión en Repositorio y navegue a Rutas.SPLASH limpiando el backstack hasta Rutas.HOME.
Mantén limpios los imports reutilizando Componentes.kt (BarraSuperior, BotonPrincipal, FilaDato) y el Scaffold existente.
```

## VII. Preguntas de reflexión

### 1. ¿Por qué los modelos, Rutas.kt y AppNavigation.kt se entregaron completos y las pantallas no? ¿Qué tienen en común los archivos que sí se dejaron como esqueleto?
Porque los modelos y las rutas representan la estructura base, el contrato de datos y la arquitectura de navegación de toda la aplicación. Si cada uno definía rutas o parámetros diferentes, el proyecto se rompía por completo. Los archivos que se dejaron como esqueleto tienen en común que corresponden a la capa de interfaz de usuario (UI), donde el objetivo pedagógico era justamente diseñar, maquetar con Jetpack Compose y consumir la lógica del repositorio en cada pantalla.

### 2. ¿Por qué el Repositorio es un object y no una clase normal? ¿Qué pasaría con las citas si cada pantalla creara su propia lista?
El Repositorio se define como un `object` en Kotlin para aplicar el patrón Singleton, garantizando que exista una única instancia compartida en memoria durante todo el ciclo de vida de la app. Si fuera una clase normal y cada pantalla creara su propia lista o instancia, los datos quedarían aislados: una cita agendada en la pantalla de confirmación nunca se reflejaría en la pantalla de Mis Citas ni bloquearía los horarios en Fecha y Hora, ya que cada pantalla manipularía una copia distinta e independiente.

### 3. ¿Cómo lograste que la búsqueda de especialidades y los horarios disponibles se actualicen solos, sin que tú "actualices" nada a mano?
Se logró aprovechando el sistema de reactividad y estado de Jetpack Compose mediante variables observables con `remember { mutableStateOf(...) }`. Cada vez que el usuario escribe en el campo de búsqueda o cambia la fecha seleccionada en el calendario, el estado cambia automáticamente. Al mutar ese valor, Compose detecta la modificación y dispara una recomposición inmediata de la UI, recalculando los filtros y volviendo a consultar `Repositorio.horariosDisponibles()` en tiempo real sin necesidad de hacer manipulaciones manuales en la vista.

### 4. ¿Qué diferencia notaste entre navigate() normal (Especialidades -> Médicos) y el que usa popUpTo (Confirmar cita -> Cita agendada)? ¿Qué pasa al presionar Atrás en cada caso?
La diferencia radica en la gestión de la pila de retroceso (backstack). Con un `navigate()` simple (como pasar de Especialidades a Médicos), la nueva pantalla se apila encima de la anterior, por lo que al presionar el botón Atrás del sistema regresas naturalmente a la lista de especialidades. En cambio, al agendar la cita se utiliza `popUpTo(Rutas.HOME)`, lo que limpia y destruye del historial todas las pantallas intermedias del flujo de reserva (médico, fecha, hora y confirmación). De este modo, si el usuario presiona Atrás desde la pantalla de cita exitosa, vuelve directamente a la pantalla principal (Home) en lugar de regresar al formulario para intentar agendar la misma cita otra vez.

### 5. ¿Qué tuviste que corregir del código que te generó la IA para el calendario dinámico?
Tuve que corregir la lógica de inicio de la semana 0 para asegurar que si hoy es sábado o domingo no intente mostrar días hábiles pasados, sino que salte estrictamente al lunes de la semana siguiente. También se corrigió la escritura fija del mes para usar "Setiembre" con 't' como exigía la guía sin depender del idioma del teléfono, y se aseguró de reiniciar `horaSeleccionada = ""` cada vez que se cambiara de día o de semana para evitar que quedara seleccionada una hora que ya no estuviera disponible en la nueva fecha.

### 6. Compara el NavigationDrawer del Laboratorio 6 con el NavigationBar de esta tarea: ¿en qué caso usarías cada uno en un proyecto propio?
El `NavigationBar` (barra inferior) lo usaría en proyectos donde la aplicación tiene entre 3 y 5 secciones principales a las que el usuario necesita acceder constantemente con una sola mano y de forma rápida, tal como el flujo de un paciente que navega entre Inicio, Citas y Perfil. En cambio, el `NavigationDrawer` (menú lateral desplegable) lo implementaría en aplicaciones con muchas más opciones de configuración, categorías secundarias, soporte o módulos administrativos complejos, donde mostrar todo en la barra inferior sobrecargaría visualmente la pantalla.
