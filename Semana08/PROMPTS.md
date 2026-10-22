# Prompts utilizados

## Prompt 1: Gráfico de peso de la mascota
**Texto enviado a Gemini:**
"Tengo una app Kotlin con Jetpack Compose, Material 3, MVVM, Room y un VetRepository (interface). Agrega en la ficha de la mascota un gráfico de línea con el peso registrado en cada cita ATENDIDA. La consulta va en CitaDao, la función en VetRepository y su implementación, y la lista de puntos se expone como StateFlow en MascotaViewModel. La View solo dibuja el gráfico con Canvas sin librerías externas. Muéstrame solo los archivos que cambian."

**Qué generó la IA:**
Generó el modelo `RegistroPeso`, la consulta `@Query` en `CitaDao`, la declaración en `VetRepository` y `VetRepositoryRoom`, la exposición como `StateFlow` en `MascotaViewModel` y el Composable `GraficoLineas` utilizando Canvas.

**Qué tuve que corregir yo:**
Ajusté el trazado del gráfico en `GraficoLineas.kt` para calcular correctamente los márgenes y escalas proporcionales del eje Y, garantizando que cuando solo exista una lectura de peso se dibuje el punto centrado en pantalla.

**¿Respeta MVVM?:**
Sí, porque la View solo consume el `StateFlow` expuesto por `MascotaViewModel` y no interactúa con DAOs ni con la base de datos Room directamente.

---

## Prompt 2: Reprogramar cita
**Texto enviado a Gemini:**
"Agrega la funcionalidad para que el dueño pueda reprogramar la fecha y hora de una cita PENDIENTE o CONFIRMADA. Agrega la consulta UPDATE en CitaDao, la función suspend en VetRepository y VetRepositoryRoom, las reglas de validación (mínimo 2 horas de anticipación y verificar que el horario no esté ocupado) en MisCitasViewModel, y el modal con selectores en MisCitasScreen."

**Qué generó la IA:**
Generó el método `reprogramarCita` en `CitaDao`, la firma en el repositorio, la lógica de validación temporal en `MisCitasViewModel` y el cuadro de diálogo `DialogoReprogramarCita` utilizando `FechaChip` y `HoraChip`.

**Qué tuve que corregir yo:**
Corregí la recolección del estado de horas ocupadas (`observarHorasOcupadas`) dentro del diálogo para que se actualice dinámicamente cada vez que el dueño cambia la fecha seleccionada.

**¿Respeta MVVM?:**
Sí, porque las reglas de negocio (validación de 2 horas y cálculo de disponibilidad de fechas) se encuentran encapsuladas en `MisCitasViewModel`.

---

## Prompt 3: Búsqueda y filtrado de pacientes
**Texto enviado a Gemini:**
"Optimiza la búsqueda de pacientes en PacientesViewModel y PacientesScreen para que el buscador filtre por nombre de mascota, nombre de dueño o raza, combinándolo simultáneamente con el filtro por especie mediante StateFlow y combine."

**Qué generó la IA:**
Generó el operador `combine` en `PacientesViewModel` mezclando los flujos de búsqueda por texto y especie, y actualizó el `OutlinedTextField` en `PacientesScreen`.

**Qué tuve que corregir yo:**
Agregué el botón de limpieza instantánea (`IconButton` con `Clear`) en el campo de texto y el contador en vivo de pacientes encontrados para mejorar la experiencia de usuario.

**¿Respeta MVVM?:**
Sí, porque la lógica de filtrado reactivo ocurre mediante flujos en el ViewModel y la View únicamente renderiza el estado.

---

## Prompt 4: Carnet de vacunas
**Texto enviado a Gemini:**
"Crea la entidad Vacuna con la relación a Mascota, incrementa la versión de VetDatabase a 2, crea VacunaDao con consultas reactive Flow, agrega los métodos en VetRepository y MascotaViewModel, e integra la pestaña Carnet Vacunas en FichaMascotaScreen."

**Qué generó la IA:**
Generó la entidad `@Entity Vacuna`, `VacunaDao`, la actualización de la base de datos `VetDatabase`, los métodos del repositorio y ViewModel, y la pestaña de vacunas con formulario modal.

**Qué tuve que corregir yo:**
Agregué la migración destructiva alternativa `.fallbackToDestructiveMigration()` en `VetDatabase` para evitar fallos de ejecución al subir la versión de la base de datos de 1 a 2 sin perder los datos iniciales sembrados.

**¿Respeta MVVM?:**
Sí, las vacunas se consultan y registran a través de `VetRepository` sin exponer Room a los Composables de la UI.

---

## Prompt 5: Recordatorio de próximo control
**Texto enviado a Gemini:**
"Agrega el campo proximoControl a la entidad Cita y actualiza registrarAtencion en CitaDao, VetRepository, VetRepositoryRoom y AtenderCitaViewModel para que el veterinario pueda fijar una fecha de próximo control. Expón la consulta en InicioViewModel y muestra una tarjeta de recordatorio en InicioScreen."

**Qué generó la IA:**
Agregó `proximoControl` en la entidad `Cita`, modificó la consulta de actualización en `CitaDao`, expuso `observarProximosControles` en el repositorio e `InicioViewModel`, y agregó el campo en `AtenderCitaScreen` y la tarjeta de aviso en `InicioScreen`.

**Qué tuve que corregir yo:**
Ajusté el diseño de la tarjeta `TarjetaProximoControl` en `InicioScreen` con colores del tema secundario (`secondaryContainer`) para diferenciar visualmente los controles agendados de las citas regulares.

**¿Respeta MVVM?:**
Sí, la comunicación fluye estrictamente de Room al Repositorio, del Repositorio al ViewModel vía `StateFlow` y del ViewModel a la Vista.

---

## Reflexión (5 líneas)
- La IA aceleró notablemente la generación de código repetitivo como entidades Room, DAOs y estructuras básicas de ViewModel.
- Sin embargo, cometió imprecisiones al conectar los eventos de UI con el estado del ViewModel y en el cálculo de márgenes para trazados en Canvas.
- Aprendí la importancia de mantener la regla de oro en Room: lecturas reactivas mediante `Flow` sin `suspend` y acciones de escritura mediante funciones `suspend`.
- Comprendí cómo abstraer la base de datos mediante la interfaz `VetRepository` para evitar acoplamientos y facilitar pruebas unitarias.
- Validé la importancia de manejar adecuadamente las versiones de la base de datos Room al introducir nuevas entidades como `Vacuna`.
