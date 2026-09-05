# Registro de Notas

Aplicación móvil desarrollada en Jetpack Compose que calcula el promedio ponderado de 4 cursos de programación (Fundamentos 20%, POO 25%, Móviles 30% y Base de Datos 25%). Permite asignar las notas en un rango de 0 a 20 mediante Sliders, redondear el resultado final con un Switch, y requiere la confirmación obligatoria del usuario mediante un Checkbox para habilitar el cálculo.

---

## Funcionalidades

- **4 Sliders de Notas (0 - 20)**: Permite seleccionar la nota de cada curso en vivo, con un badge que muestra el valor entero y un semáforo de color (Verde Oscuro, Verde Normal, Ámbar y Rojo) según la puntuación.
- **Aporte Individual por Curso**: Muestra en tiempo real la cantidad exacta de puntos que aporta cada materia al promedio total.
- **Switch de Redondeo**: Permite alternar entre mostrar el promedio final con decimales o redondeado al entero más cercano.
- **Checkbox de Confirmación**: Desbloquea el botón CALCULAR únicamente cuando el usuario confirma que las notas ingresadas son correctas, mostrando un mensaje verde de confirmación.
- **Cálculo de Promedios**: Deriva dinámicamente el promedio ponderado exacto y el promedio final sin almacenar estados redundantes.
- **Tarjeta de Resultado y Chip de Observación**: Presenta el resultado en una tarjeta con jerarquía visual y un chip con color e indicador de rendimiento (EXCELENTE, APROBADO, EN RECUPERACIÓN, DESAPROBADO).
- **Botón LIMPIAR**: Permite reiniciar todos los sliders y controles a su estado inicial (0f y false).

---

## Tecnologías Utilizadas

- **Lenguaje**: Kotlin
- **UI Framework**: Jetpack Compose
- **Componentes**: Material Design 3

---

## Captura de pantalla

![Registro de Notas - Vista Principal](cap1.jpg)
![Registro de Notas - Resultado Calculado](cap2.jpg)

---

## Casos de prueba verificados

| Notas (F, POO, M, BD) | Redondear | Promedio ponderado | Promedio final | Observación |
|---|---|---|---|---|
| 15, 13, 16, 14 | ON | 14.55 | 15 | APROBADO |
| 12, 10, 11, 9 | OFF | 10.45 | 10.45 | EN RECUPERACIÓN |
| 18, 17, 19, 18 | ON | 18.05 | 18 | EXCELENTE |
| 8, 9, 7, 10 | OFF | 8.45 | 8.45 | DESAPROBADO |

---

## Proceso de desarrollo con IA

- **Prompt 1**:
  > Vamos a construir una app en Jetpack Compose llamada "Registro de Notas".
  > CONTEXTO GENERAL: Un estudiante calcula el promedio ponderado de 4 cursos de programación, cada uno con un peso fijo (Fundamentos 20%, POO 25%, Móviles 30%, BD 25%). El usuario asigna cada nota (0-20) con un Slider, debe marcar un Checkbox de confirmación para habilitar el botón CALCULAR, y puede activar un Switch para redondear el promedio final. El resultado se muestra en una tarjeta con un chip de color según la observación (EXCELENTE/APROBADO/EN RECUPERACIÓN/DESAPROBADO).
  > Antes de escribir código, quiero que me confirmes que entiendes: 1. Los 4 estados de notas (Float, uno por Slider) 2. Que promedio ponderado y promedio final son valores CALCULADOS (no van en remember, se derivan de los estados) 3. La diferencia entre Slider/Switch/Checkbox como "el mismo patrón de estado que TextField" (value/onValueChange) 4. Las reglas de negocio de la tabla de observaciones.
  > No generes código todavía. Solo explícame tu plan de estados y composables antes de empezar, para que yo lo revise.

- **Prompt 2**:
  > Antes de continuar, necesito corregir tres puntos de tu plan porque no coinciden con el enunciado original. Primero, los rangos de observación que propusiste están mal y debes usar estos exactos: EXCELENTE de 17.0 a 20.0 con chip verde oscuro, APROBADO de 13.0 a 16.99 con chip verde normal (un tono distinto al de EXCELENTE), EN RECUPERACIÓN de 10.0 a 12.99 con chip ámbar, y DESAPROBADO para menor a 10.0 con chip rojo. Verifica en tu cabeza este caso antes de seguir: las notas 12, 10, 11 y 9 dan un promedio final de 10.45, que debe caer en EN RECUPERACIÓN según el rango de 10 a 12.99, y no en DESAPROBADO. Con el rango que propusiste antes, de 11 a 14, este caso fallaba, así que confírmame que ya corregiste el when. Segundo, el texto exacto del checkbox debe decir literalmente "Confirmo que las notas son correctas" y no "Confirmar que las notas son correctas". Tercero, el resto de tu plan está bien: los cuatro estados Float independientes para las notas, el promedio ponderado y el promedio final como valores derivados sin usar remember, el patrón value/onValueChange igual en Slider, Switch y Checkbox, y la estructura de composables con RegistroNotasScreen, CursoSlider, OpcionesFormulario y ResultadoCard. Confírmame que corregiste los rangos y el texto del checkbox, y muéstrame el when actualizado de obtenerObservacion antes de generar cualquier código.

- **Prompt 3**:
  > Perfecto, tu corrección de rangos y colores está bien, ahora construye el Composable principal con los 4 estados remember, es decir notaFundamentos, notaPOO, notaMoviles y notaBD como Float, todos inicializados en 0f, además de redondear y confirmado como Boolean, y mostrarResultado también como Boolean; agrega una TopBar con el texto "Registro de Notas" en color primario y un fondo con degradado suave usando Brush.verticalGradient; crea un composable reutilizable llamado CursoSlider que reciba como parámetros nombreCurso, peso, notaValue y onNotaChange, y que muestre el texto "nombreCurso (peso%)", un badge con la nota en vivo, y el Slider configurado con valueRange = 0f..20f y steps = 19; usa este CursoSlider cuatro veces con los pesos correctos, Fundamentos con 20%, POO con 25%, Móviles con 30% y BD con 25%; todavía no conectes el Switch, el Checkbox, el botón ni los cálculos, ya que por ahora solo se busca verificar que los cuatro Sliders actualicen su badge en vivo al moverlos. Usa lenguaje kotlin netamente, nada de java.

- **Prompt 4**:
  > El código está bien en general, pero hay que corregir dos cosas: primero, el badge de cada CursoSlider debe mostrar la nota como número entero usando notaValue.toInt().toString(), en lugar de mostrarla con un decimal como "15.0", ya que las figuras muestran los valores "15", "13", "16" y "14" sin decimales; segundo, la TopBar debe tener el fondo en color primario, es decir containerColor = MaterialTheme.colorScheme.primary, en vez de transparente, y el texto del título debe ir en color blanco u onPrimary para que se lea bien sobre ese fondo, tal como se ve en las Figuras 1 y 2 con la barra morada sólida; además, ya se adelantó el Switch, el Checkbox y el botón CALCULAR en este paso, lo cual está bien y no hace falta deshacerlo, pero en los próximos pasos se seguirá la secuencia acordada para verificar cada parte por separado, así que se pide mostrar únicamente el código corregido de la TopBar y del badge, sin reescribir todo el archivo.

- **Prompt 5**:
  > Ahora, usando los cálculos que ya tienes (promedioPonderado, promedioFinal, obtenerObservacion), implementa la parte visual:
  > Debajo del botón CALCULAR:
  > - Si mostrarResultado es false: Text en gris "Asigna las notas y confirma para calcular"
  > - Si mostrarResultado es true: una Card (ResultadoCard) que muestre:
  >   a) "Promedio ponderado: X.XX" siempre con 2 decimales, usa String.format(Locale.US, "%.2f", promedioPonderado)
  >   b) "Promedio final: X" en negrita — si redondear está activo, muéstralo sin decimales (es un entero); si no, con 2 decimales
  >   c) Debajo del promedio final, el texto "(redondeado)" en gris pequeño, SOLO si redondear está activo
  >   d) Un chip (Surface o AssistChip) con el texto y color que devuelve obtenerObservacion(promedioFinal)
  > No agregues todavía el mensaje verde de confirmación ni el footer, eso va en un paso posterior.

- **Prompt 6**:
  > El código funciona bien y los cuatro casos de prueba coinciden con lo esperado: para (15,13,16,14) con redondear activado, el cálculo da 15×0.20+13×0.25+16×0.30+14×0.25=14.55 y kotlin.math.round(14.55) da 15, cayendo en APROBADO ya que 15 está entre 13 y 16.99; para (12,10,11,9) sin redondear, el cálculo da 12×0.20+10×0.25+11×0.30+9×0.25=10.45, que sin redondeo queda en 10.45 y cae en EN RECUPERACIÓN porque es mayor o igual a 10; para (18,17,19,18) con redondear activado, el cálculo da 18×0.20+17×0.25+19×0.30+18×0.25=18.05 y al redondear da 18, cayendo en EXCELENTE por ser mayor o igual a 17; y para (8,9,7,10) sin redondear, el cálculo da 8×0.20+9×0.25+7×0.30+10×0.25=8.45, que sin redondeo queda en 8.45 y cae correctamente en DESAPROBADO por ser menor a 10, así que los cuatro casos pasan. Sin embargo, hay que notar que la IA no respetó la división que habíamos pedido antes: en lugar de entregar solo la lógica de cálculo, ya adelantó también toda la parte visual del ResultadoCard con el chip de observación, el formato de dos decimales y el texto "(redondeado)" condicional, es decir que hizo de una sola vez lo que habíamos separado en dos pasos; además agregó por su cuenta un detalle no solicitado pero razonable: cada vez que se mueve un Slider o se desmarca el Checkbox, mostrarResultado se pone en false automáticamente, ocultando un resultado desactualizado hasta que se presione CALCULAR de nuevo, lo cual es una mejora de UX que no rompe ningún caso de prueba y se puede dejar tal cual sin pedir que la deshaga. Dado que ese paso ya quedó cubierto en este mismo commit, puedes registrarlo como uno solo (por ejemplo "feat: agregar cálculo de promedios, observación y tarjeta de resultado") y saltar directo al último paso, que cierra la app con el mensaje verde de confirmación, el footer con tu nombre y, si el tiempo alcanza, los tres retos opcionales (aporte por curso, semáforo de color en el badge, y botón LIMPIAR).

- **Prompt 7**:
  > Necesito que revises y ajustes el código de RegistroNotasScreen según estas 4 reglas de oro de diseño y legibilidad sobre el fondo con degradado: primero, contraste suficiente texto-fondo, es decir que todo texto mantenga una relación de contraste mínima de 4.5:1 con su fondo, incluso en las zonas donde el degradado cambia de tono, revisando que ningún texto use colores grises claros directamente sobre el degradado y que se use MaterialTheme.colorScheme.onSurface u onSurfaceVariant sobre superficies sólidas como las Cards, no sobre el fondo degradado directo; segundo, jerarquía visual clara, o sea que los elementos más importantes como el Promedio final y la observación destaquen por tamaño y peso de fuente frente a los secundarios como el footer o la aclaración "(redondeado)", confirmando que el Promedio final se vea claramente más grande y en negrita que el ponderado, y que el footer sea el texto más pequeño de toda la pantalla; tercero, espaciado y "aire" consistente, usando valores uniformes de padding y spacing en todo el layout como 8dp, 12dp y 16dp, sin mezclar valores arbitrarios ni dejar elementos pegados a los bordes de la pantalla o entre sí; y cuarto, área táctil accesible, es decir que todo control interactivo como Slider, Switch, Checkbox y Button mantenga un área táctil mínima de 48dp por 48dp, sin reducir el tamaño de estos componentes con modifiers personalizados que los hagan más pequeños que el estándar de Material3; además, debe revisar el código completo contra estas cuatro reglas y decir específicamente qué cambios hizo si hizo alguno, o confirmar que ya se cumplían todas, mostrando solo las partes del código que cambió y no todo el archivo.

---

Desarrollado por: Jesús Enrique Rocha Bobadilla
