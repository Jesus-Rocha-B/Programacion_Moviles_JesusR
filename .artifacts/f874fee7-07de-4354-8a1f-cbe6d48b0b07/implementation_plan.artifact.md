# Plan para corregir Lab03RegistroProducto

Este plan soluciona los errores de compilación y ejecución en el proyecto de la Semana 3, asegurando que `MainActivity` funcione correctamente con una configuración estable de Android y Compose.

## Problemas Identificados
1. **Versiones Incompatibles**: El proyecto usa AGP 9.3.2 y SDK 37, los cuales fallan en este entorno. Se bajará a AGP 8.7.3 y SDK 35.
2. **Conflicto de Nombres**: Existe una clase de anotación `Lab03RegistroProductoTheme` que entra en conflicto con la función del tema en `Theme.kt`.
3. **Funcionalidad Incompleta**: `MainActivity.kt` tiene la interfaz iniciada pero le falta el botón de acción y la lógica para completar el registro.

## Cambios Propuestos

### Configuración del Proyecto (Gradle)

#### [MODIFY] [libs.versions.toml](file:///Users/tecsup/Documents/Rocha/Laboratorio02/Semana%203/gradle/libs.versions.toml)
- Downgrade `agp` de 9.3.2 a 8.7.3.
- Ajustar versiones de `coreKtx`, `lifecycle`, `activityCompose` y `composeBom` a valores estables compatibles con AGP 8.7.x.

#### [MODIFY] [build.gradle.kts](file:///Users/tecsup/Documents/Rocha/Laboratorio02/Semana%203/app/build.gradle.kts)
- Cambiar `compileSdk` y `targetSdk` a 35.
- Corregir la sintaxis de `compileSdk` y `buildTypes`.

### Limpieza de Código

#### [DELETE] [Lab03RegistroProductoTheme.kt](file:///Users/tecsup/Documents/Rocha/Laboratorio02/Semana%203/app/src/main/java/com/rojas/lab03registroproducto/ui/theme/Lab03RegistroProductoTheme.kt)
- Eliminar esta clase de anotación redundante que causa conflictos de nombrado con el tema real.

### Implementación de la Interfaz

#### [MODIFY] [MainActivity.kt](file:///Users/tecsup/Documents/Rocha/Laboratorio02/Semana%203/app/src/main/java/com/rojas/lab03registroproducto/MainActivity.kt)
- Agregar el botón "Agregar Producto".
- Implementar la lógica para validar y mostrar un resumen (o un Toast) al registrar.
- Asegurar que todos los componentes de Material3 estén correctamente importados.

## Plan de Verificación

### Pruebas Automatizadas
- Ejecutar `./gradlew assembleDebug` en la carpeta `Semana 3` para verificar que el build sea exitoso.

### Verificación Manual
- Abrir el archivo `MainActivity.kt` y verificar que el Preview de Compose se renderice correctamente.
- (Opcional) Desplegar en dispositivo/emulador si está disponible.
