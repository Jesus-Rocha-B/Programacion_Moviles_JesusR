    package com.rocha.saludplus.ui.components
    import android.os.Build
    import androidx.annotation.RequiresApi
    import java.time.DayOfWeek
    import java.time.LocalDate

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
