package com.rocha.huellitasvet.data.local
import com.rocha.huellitasvet.data.local.entity.Cita
import com.rocha.huellitasvet.data.local.entity.Mascota
import com.rocha.huellitasvet.data.local.entity.Servicio
import com.rocha.huellitasvet.data.local.entity.Usuario
import com.rocha.huellitasvet.model.Especie
import com.rocha.huellitasvet.model.EstadoCita
import com.rocha.huellitasvet.model.Rol
import com.rocha.huellitasvet.model.Sexo
import java.time.LocalDate

object DatosIniciales {
    private fun dia(n: Int): String = LocalDate.now().plusDays(n.toLong()).toString()
    suspend fun sembrar(db: VetDatabase) {
        // Solo se siembra la primera vez
        if (db.usuarioDao().contar() > 0) return
        // IDs explícitos para poder usarlos en las claves foráneas
        db.usuarioDao().insertarTodos(
            listOf(
                Usuario(1, "Ana Torres", "ana@correo.test", "1234", Rol.CLIENTE.name),
                Usuario(2, "Luis Ramos", "luis@correo.test", "1234", Rol.CLIENTE.name),
                Usuario(3, "Dra. Vega", "vet@huellitas.test", "admin", Rol.ADMIN.name)
            )
        )
        db.servicioDao().insertarTodos(
            listOf(
                Servicio(1, "Consulta general", "Revisión médica general", 30, 60.0, true, "heart"),
                Servicio(2, "Vacunación", "Aplicación de vacunas", 30, 45.0, true, "syringe"),
                Servicio(3, "Baño y corte", "Baño, corte de pelo y limpieza", 60, 50.0, true, "scissors"),
                Servicio(4, "Desparasitación", "Tratamiento antiparasitario", 15, 25.0, false, "syringe")
            )
        )

        db.mascotaDao().insertarTodos(
            listOf(
                Mascota(1, 1, "Luna", Especie.PERRO.name, "Labrador", 2021, 12.6, Sexo.HEMBRA.name),
                Mascota(2, 1, "Max", Especie.PERRO.name, "Beagle", 2019, 10.2, Sexo.MACHO.name),
                Mascota(3, 2, "Rocky", Especie.PERRO.name, "Bulldog", 2020, 17.8, Sexo.MACHO.name),
                Mascota(4, 2, "Michi", Especie.GATO.name, "Siamés", 2022, 4.1, Sexo.HEMBRA.name)
            )
        )

        fun cita(
            mascotaId: Int, servicioId: Int, fecha: String, hora: String,
            estado: EstadoCita, precio: Double,
            diagnostico: String? = null, tratamiento: String? = null, peso: Double? = null
        ) = Cita(
            mascotaId = mascotaId,
            servicioId = servicioId,
            fecha = fecha,
            hora = hora,
            estado = estado.name,
            precio = precio,
            diagnostico = diagnostico,
            tratamiento = tratamiento,
            pesoRegistrado = peso
        )

        db.citaDao().insertarTodos(
            listOf(
                // Pasadas ATENDIDAS (con diagnóstico, tratamiento y peso)
                cita(1, 1, dia(-30), "10:00", EstadoCita.ATENDIDA, 60.0,
                    "Control general, buen estado de salud", "Ninguno", 12.0),
                cita(1, 2, dia(-14), "09:30", EstadoCita.ATENDIDA, 45.0,
                    "Vacuna anual aplicada sin reacciones", "Refuerzo en 12 meses", 12.4),
                cita(1, 1, dia(-3), "11:00", EstadoCita.ATENDIDA, 60.0,
                    "Leve irritación en la piel", "Pomada antiinflamatoria por 7 días", 12.6),
                cita(2, 3, dia(-7), "15:00", EstadoCita.ATENDIDA, 50.0,
                    "Baño, corte y limpieza de oídos", "Limpieza de oídos", 10.2),
                cita(3, 2, dia(-10), "16:00", EstadoCita.ATENDIDA, 45.0,
                    "Refuerzo de vacuna múltiple", "Ninguno", 17.8),
                cita(4, 1, dia(-5), "10:30", EstadoCita.ATENDIDA, 60.0,
                    "Control de peso y pelaje", "Alimento balanceado", 4.1),
                // Hoy
                cita(2, 1, dia(0), "17:00", EstadoCita.CONFIRMADA, 60.0),
                cita(1, 2, dia(0), "16:30", EstadoCita.PENDIENTE, 45.0),
                // Próximos días
                cita(3, 3, dia(1), "09:00", EstadoCita.PENDIENTE, 50.0),
                cita(4, 2, dia(2), "11:30", EstadoCita.CONFIRMADA, 45.0),
                cita(1, 1, dia(4), "10:00", EstadoCita.PENDIENTE, 60.0)
            )
        )
    }
}