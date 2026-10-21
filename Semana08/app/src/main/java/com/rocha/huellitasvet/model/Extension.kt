package com.rocha.huellitasvet.model
import com.rocha.huellitasvet.data.local.entity.Mascota
import com.rocha.huellitasvet.data.local.relation.CitaConDetalle
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale

/** Fecha y hora de la cita como LocalDateTime (para comparar con "ahora"). */
fun CitaConDetalle.fechaHora(): LocalDateTime =
    LocalDateTime.of(LocalDate.parse(cita.fecha), LocalTime.parse(cita.hora))

fun CitaConDetalle.fechaLegible(): String =
    fechaHora()
        .format(DateTimeFormatter.ofPattern("EEE d MMM · HH:mm", Locale.forLanguageTag("es-PE")))
        .replaceFirstChar { it.uppercase() }

/** Edad = año actual − año de nacimiento */
fun Mascota.edad(): Int = LocalDate.now().year - anioNacimiento

fun Mascota.edadTexto(): String {
    val e = edad()
    return when {
        e <= 0 -> "Menos de 1 año"
        e == 1 -> "1 año"
        else -> "$e años"
    }
}

/** "PERRO" -> "Perro" */
fun String.nombreBonito(): String = lowercase().replaceFirstChar { it.uppercase() }