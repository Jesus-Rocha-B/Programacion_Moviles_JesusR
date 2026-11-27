# Prompts y decisiones de la Fase 2

El prompt de trabajo resume las indicaciones dadas en esta conversación. La respuesta y la implementación corresponden a Codex.

| Prompt de trabajo | Respuesta observada de Codex | Qué se implementó | Criterio técnico |
| --- | --- | --- | --- |
| **Mejora 1 · Texto largo.** Actúa como desarrollador Android especializado en Kotlin y Jetpack Compose. En AvisosCampus, agrega `NotificationCompat.BigTextStyle` para que el texto completo se vea al expandir la notificación. Haz que **Mostrar aviso** genere un mensaje suficientemente largo y detallado para comprobarlo. Conserva el permiso, el canal, el `PendingIntent`, los identificadores únicos y la separación MVVM + Repository. Compila el proyecto y deja los cambios sin commit. | Propuse aplicar `BigTextStyle` al builder existente y ampliar el texto del aviso generado por el botón. La compilación confirmó que los cambios son válidos. | Añadí `setStyle(NotificationCompat.BigTextStyle().bigText(aviso.texto))` en `Notificador.kt` y un mensaje de varias líneas en `AvisosViewModel.avisarAhora()`. | El ViewModel decide el contenido; `Notificador` usa las APIs de Android para presentarlo. El texto corto sigue visible y, al expandir la notificación, se muestra el mensaje completo. |
