package com.rocha.saludplus

import com.rocha.saludplus.ui.components.diasHabiles
import com.rocha.saludplus.ui.components.formatearFechaLarga
import com.rocha.saludplus.ui.components.nombreDiaCorto
import com.rocha.saludplus.ui.components.nombreMes
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.DayOfWeek
import java.time.LocalDate

class ExampleUnitTest {

    @Test
    fun testDiasHabilesRetorna5DiasHabiles() {
        val dias = diasHabiles(0)
        assertEquals(5, dias.size)
        dias.forEach { dia ->
            assertTrue(dia.dayOfWeek != DayOfWeek.SATURDAY && dia.dayOfWeek != DayOfWeek.SUNDAY)
        }
    }

    @Test
    fun testNombreMes() {
        val fechaSetiembre = LocalDate.of(2026, 9, 16)
        assertEquals("Setiembre 2026", nombreMes(fechaSetiembre))

        val fechaOctubre = LocalDate.of(2026, 10, 8)
        assertEquals("Octubre 2026", nombreMes(fechaOctubre))
    }

    @Test
    fun testNombreDiaCorto() {
        val lunes = LocalDate.of(2026, 10, 12)
        assertEquals("Lun", nombreDiaCorto(lunes))

        val miercoles = LocalDate.of(2026, 10, 14)
        assertEquals("Mié", nombreDiaCorto(miercoles))

        val domingo = LocalDate.of(2026, 10, 11)
        assertEquals("Dom", nombreDiaCorto(domingo))
    }

    @Test
    fun testFormatearFechaLargaValida() {
        val fechaFormateada = formatearFechaLarga("2026-09-16")
        assertEquals("Miércoles 16 de setiembre 2026", fechaFormateada)
    }

    @Test
    fun testFormatearFechaLargaInvalidaOVacia() {
        assertEquals("", formatearFechaLarga(""))
        assertEquals("", formatearFechaLarga(null))
        assertEquals("fecha-invalida", formatearFechaLarga("fecha-invalida"))
    }
}
