package com.rocha.saludplus.repository

import androidx.compose.runtime.mutableStateListOf
import com.rocha.saludplus.R
import com.rocha.saludplus.model.Cita
import com.rocha.saludplus.model.Especialidad
import com.rocha.saludplus.model.Medico
import com.rocha.saludplus.model.Usuario

// object es una sola instancia compartida por todas las pantallas
object Repositorio {
    // Usuario de prueba para no registrarse cada vez
    val usuarios = mutableListOf(
        Usuario("Juan Pérez", "999888777", "juan@correo.com", "123456")
    )
    // Usuario con la sesión iniciada (null = nadie logueado)
    var usuarioActual: Usuario? = null

    // Devuelve falso si el correo ya está registrado
    fun registrarUsuario(usuario: Usuario): Boolean {
        val existe = usuarios.any { it.correo.equals(usuario.correo, ignoreCase = true) }
        if (existe) return false
        usuarios.add(usuario)
        return true
    }

    // Busca el usuario en la lista, si lo encuentra deja la sesión iniciada
    fun iniciarSesion(correo: String, contrasena: String): Boolean {
        val usuario = usuarios.find {
            it.correo.equals(correo, ignoreCase = true) && it.contrasena == contrasena
        }
        usuarioActual = usuario
        return usuario != null
    }

    // Cerrar sesión
    fun cerrarSesion() {
        usuarioActual = null
    }

    // Lista de especialidades
    val especialidades = listOf(
        Especialidad(1, "Medicina General", "Atención integral"),
        Especialidad(2, "Pediatría", "Infantes y adolescentes"),
        Especialidad(3, "Ginecología", "Salud de la mujer"),
        Especialidad(4, "Cardiología", "Cuidado del corazón"),
        Especialidad(5, "Dermatología", "Piel, cabello y uñas"),
        Especialidad(6, "Traumatología", "Huesos y articulaciones"),
        Especialidad(7, "Oftalmología", "Salud visual")
    )

    // Lista de médicos con sede y exactamente 3 horarios de atención fijos por día (2 por especialidad)
    val medicos = listOf(
        // Medicina General (2)
        Medico(1, "Dr. Carlos Mendoza", 1, 4.6, 10, R.drawable.doc_male_2, "Independencia", listOf("09:00", "11:00", "15:00")),
        Medico(2, "Dra. María Pérez", 1, 4.8, 14, R.drawable.doc_female_1, "La Molina", listOf("10:00", "14:00", "16:00")),
        // Pediatría (2)
        Medico(3, "Dra. Sofía Vargas", 2, 4.9, 11, R.drawable.doc_female_2, "Independencia", listOf("08:00", "11:00", "15:00")),
        Medico(4, "Dr. Roberto Gómez", 2, 4.7, 9, R.drawable.doc_male_1, "La Molina", listOf("09:00", "14:00", "17:00")),
        // Ginecología (2)
        Medico(5, "Dra. Ana Torres", 3, 4.9, 12, R.drawable.doc_female_1, "Independencia", listOf("09:00", "12:00", "16:00")),
        Medico(6, "Dra. Claudia Rojas", 3, 4.8, 8, R.drawable.doc_female_2, "La Molina", listOf("10:00", "14:00", "17:00")),
        // Cardiología (2)
        Medico(7, "Dr. Jorge Castillo", 4, 4.8, 20, R.drawable.doc_male_2, "Independencia", listOf("08:00", "11:00", "15:00")),
        Medico(8, "Dra. Elena Ramos", 4, 4.7, 16, R.drawable.doc_female_2, "La Molina", listOf("09:00", "14:00", "16:00")),
        // Dermatología (2)
        Medico(9, "Dra. Valeria Núñez", 5, 4.7, 8, R.drawable.doc_female_1, "Independencia", listOf("10:00", "13:00", "16:00")),
        Medico(10, "Dr. Mateo Silva", 5, 4.6, 12, R.drawable.doc_male_1, "La Molina", listOf("09:00", "11:00", "15:00")),
        // Traumatología (2)
        Medico(11, "Dr. Diego Herrera", 6, 4.5, 13, R.drawable.doc_male_1, "Independencia", listOf("08:00", "10:00", "14:00")),
        Medico(12, "Dra. Lucía Méndez", 6, 4.8, 18, R.drawable.doc_female_2, "La Molina", listOf("11:00", "15:00", "17:00")),
        // Oftalmología (2)
        Medico(13, "Dra. Lucía Castro", 7, 4.6, 10, R.drawable.doc_female_2, "Independencia", listOf("09:00", "12:00", "16:00")),
        Medico(14, "Dr. Andrés Morales", 7, 4.9, 15, R.drawable.doc_male_2, "La Molina", listOf("10:00", "14:00", "17:00"))
    )

    // Filtra por nombre sin importar mayúsculas, con texto vacío devuelve todas
    fun buscarEspecialidades(texto: String): List<Especialidad> {
        return especialidades.filter { it.nombre.contains(texto, ignoreCase = true) }
    }

    // Las primeras 4 para el LazyRow del Inicio
    fun especialidadesDestacadas(): List<Especialidad> {
        return especialidades.take(4)
    }

    // Busca una especialidad por su id
    fun obtenerEspecialidad(id: Int): Especialidad? {
        return especialidades.find { it.id == id }
    }

    // Busca un médico por su id
    fun obtenerMedico(id: Int): Medico? {
        return medicos.find { it.id == id }
    }

    // Médicos de una especialidad, los mejor calificados primero
    fun medicosPorEspecialidad(especialidadId: Int): List<Medico> {
        return medicos
            .filter { it.especialidadId == especialidadId }
            .sortedByDescending { it.calificacion }
    }

    // Médicos filtrados por sede y especialidad
    fun medicosPorSedeYEspecialidad(sede: String, especialidadId: Int): List<Medico> {
        return medicos
            .filter { it.sede.equals(sede, ignoreCase = true) && it.especialidadId == especialidadId }
            .sortedByDescending { it.calificacion }
    }

    // Busca médicos por especialidad y texto
    fun buscarMedicos(especialidadId: Int, texto: String): List<Medico> {
        return medicosPorEspecialidad(especialidadId)
            .filter { it.nombre.contains(texto, ignoreCase = true) }
    }

    // Busca médicos por sede, especialidad y texto
    fun buscarMedicosPorSede(sede: String, especialidadId: Int, texto: String): List<Medico> {
        return medicosPorSedeYEspecialidad(sede, especialidadId)
            .filter { it.nombre.contains(texto, ignoreCase = true) }
    }

    // Lista de citas, mutableStateListOf hace que Compose se entere de los cambios
    val citas = mutableStateListOf<Cita>()

    // Horarios disponibles basados en el horario de atención propio del médico menos los ya agendados
    fun horariosDisponibles(medicoId: Int, fecha: String): List<String> {
        val medico = obtenerMedico(medicoId) ?: return emptyList()
        val ocupados = citas
            .filter { it.medicoId == medicoId && it.fecha == fecha }
            .map { it.hora }
        return medico.horariosAtencion.filter { it !in ocupados }
    }

    // Retorna true si el horario está bloqueado por defecto (el primero de sus 3 horarios) o ya fue agendado
    fun esHorarioOcupado(medicoId: Int, fecha: String, hora: String): Boolean {
        val medico = obtenerMedico(medicoId) ?: return false
        val bloqueadoPorDefecto = medico.horariosAtencion.firstOrNull() == hora
        val agendado = citas.any { it.medicoId == medicoId && it.fecha == fecha && it.hora == hora }
        return bloqueadoPorDefecto || agendado
    }

    // Devuelve la cita creada, o null si no hay sesión o el horario ya está ocupado
    fun agendarCita(medicoId: Int, fecha: String, hora: String): Cita? {
        val usuario = usuarioActual ?: return null
        if (esHorarioOcupado(medicoId, fecha, hora)) return null
        val nuevoId = (citas.maxOfOrNull { it.id } ?: 0) + 1
        val cita = Cita(nuevoId, usuario.correo, medicoId, fecha, hora)
        citas.add(cita)
        return cita
    }

    // Citas del usuario con sesión iniciada, ordenadas por fecha y luego por hora
    fun citasDelUsuario(): List<Cita> {
        val correo = usuarioActual?.correo ?: return emptyList()
        return citas
            .filter { it.correoUsuario == correo }
            .sortedWith(compareBy({ it.fecha }, { it.hora }))
    }

    // Busca una cita por su id
    fun obtenerCita(id: Int): Cita? {
        return citas.find { it.id == id }
    }

    // Elimina la cita de la lista, se usa en el reto de Detalle de cita
    fun cancelarCita(id: Int) {
        citas.removeAll { it.id == id }
    }
}
