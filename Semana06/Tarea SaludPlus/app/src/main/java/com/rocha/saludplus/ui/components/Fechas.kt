package com.rocha.saludplus.ui.components

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.DayOfWeek
import java.time.LocalDate

// Listas estáticas para nombres de meses en español (el mes 9 debe ser estrictamente "Setiembre")
private val MESES = listOf(
    "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
    "Julio", "Agosto", "Setiembre", "Octubre", "Noviembre", "Diciembre",
)

private val MESES_MINUSCULAS = listOf(
    "enero", "febrero", "marzo", "abril", "mayo", "junio",
    "julio", "agosto", "setiembre", "octubre", "noviembre", "diciembre",
)

private val DIAS_LARGOS = listOf(
    "Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo",
)

/**
 * Calcula y retorna una lista de 5 días hábiles consecutivos (de lunes a viernes,
 * excluyendo sábados y domingos) para la semana especificada.
 *
 * @param semana Número de la semana (0 para la semana base/actual, n >= 1 para semanas futuras).
 * @return Lista de exactamente 5 objetos [LocalDate] que representan los días hábiles.
 */
@RequiresApi(Build.VERSION_CODES.O)
fun diasHabiles(semana: Int): List<LocalDate> {
    // Obtener la fecha actual
    val hoy = LocalDate.now()

    // Paso 1: Determinar la fecha de inicio de la Semana 0 (semana base).
    // Si hoy es un día hábil (lunes a viernes), los 5 días inician en el día de hoy.
    // Si hoy es sábado o domingo, la semana debe iniciar obligatoriamente el lunes de la semana siguiente.
    val inicioBase = when (hoy.dayOfWeek) {
        DayOfWeek.SATURDAY -> hoy.plusDays(2)
        DayOfWeek.SUNDAY -> hoy.plusDays(1)
        else -> hoy
    }

    // Paso 2: Calcular los 5 días hábiles consecutivos para la Semana 0.
    // Se recorren los días uno a uno a partir de la fecha de inicio, omitiendo fines de semana.
    val diasBase = mutableListOf<LocalDate>()
    var fechaActual = inicioBase
    while (diasBase.size < 5) {
        if ((fechaActual.dayOfWeek != DayOfWeek.SATURDAY) && (fechaActual.dayOfWeek != DayOfWeek.SUNDAY)) {
            diasBase.add(fechaActual)
        }
        fechaActual = fechaActual.plusDays(1)
    }

    // Paso 3: Desplazar los días hábiles para la Semana n (donde n >= 1).
    // Se toman los 5 días hábiles de la Semana 0 y se desplazan exactamente 7 * n días hacia adelante usando plusDays().
    if (semana == 0) {
        return diasBase
    }

    return diasBase.map { fecha ->
        fecha.plusDays((7 * semana).toLong())
    }
}

/**
 * Retorna el nombre del mes y el año con la primera letra en mayúscula.
 * Ejemplo: "Octubre 2026" o "Setiembre 2026".
 *
 * @param fecha Instancia de [LocalDate].
 * @return Texto formateado con el mes y año en español.
 */
@RequiresApi(Build.VERSION_CODES.O)
fun nombreMes(fecha: LocalDate): String {
    val mes = MESES[fecha.monthValue - 1]
    return "$mes ${fecha.year}"
}

/**
 * Retorna la abreviatura de 3 letras correspondiente al día de la semana.
 * Ejemplo: "Lun", "Mar", "Mié", "Jue", "Vie", "Sáb", "Dom".
 *
 * @param fecha Instancia de [LocalDate].
 * @return Abreviatura de 3 letras del día de la semana en español.
 */
@RequiresApi(Build.VERSION_CODES.O)
fun nombreDiaCorto(fecha: LocalDate): String {
    return when (fecha.dayOfWeek) {
        DayOfWeek.MONDAY -> "Lun"
        DayOfWeek.TUESDAY -> "Mar"
        DayOfWeek.WEDNESDAY -> "Mié"
        DayOfWeek.THURSDAY -> "Jue"
        DayOfWeek.FRIDAY -> "Vie"
        DayOfWeek.SATURDAY -> "Sáb"
        DayOfWeek.SUNDAY -> "Dom"
    }
}

/**
 * Parsea una fecha en formato ISO "yyyy-MM-dd" y la transforma a un formato largo en español.
 * Ejemplo: "2026-09-16" -> "Miércoles 16 de setiembre 2026".
 * Si la fecha recibida es nula, vacía o inválida, retorna el texto original.
 *
 * @param fecha String con la fecha en formato ISO "yyyy-MM-dd".
 * @return Fecha formateada en español o el texto original en caso de error.
 */
@RequiresApi(Build.VERSION_CODES.O)
fun formatearFechaLarga(fecha: String?): String {
    if (fecha.isNullOrBlank()) return fecha ?: ""
    return runCatching {
        val parsed = LocalDate.parse(fecha)
        val diaSemana = DIAS_LARGOS[parsed.dayOfWeek.value - 1]
        val mes = MESES_MINUSCULAS[parsed.monthValue - 1]
        "$diaSemana ${parsed.dayOfMonth} de $mes ${parsed.year}"
    }.getOrDefault(fecha)
}
