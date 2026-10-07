package com.rocha.saludplus.repository
import androidx.compose.runtime.mutableStateListOf
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
    // Lista de médicos, cada uno con el id de su especialidad
    val medicos = listOf(
        // Ginecología
        Medico(1, "Dra. Ana Torres", 3, 4.9, 12),
        Medico(2, "Dra. Claudia Rojas", 3, 4.8, 8),
        Medico(3, "Dr. Luis Ramírez", 3, 4.7, 15),
        Medico(4, "Dra. Mariana Soto", 3, 4.6, 6),
        // Un médico por cada una de las demás especialidades
        Medico(5, "Dr. Carlos Mendoza", 1, 4.6, 10),
        Medico(6, "Dra. Sofía Vargas", 2, 4.9, 11),
        Medico(7, "Dr. Jorge Castillo", 4, 4.8, 20),
        Medico(8, "Dra. Valeria Núñez", 5, 4.7, 8),
        Medico(9, "Dr. Diego Herrera", 6, 4.5, 13),
        Medico(10, "Dr. Andrés Flores", 7, 4.6, 10)
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
    // Igual que el anterior pero filtrando también por el texto buscado
    fun buscarMedicos(especialidadId: Int, texto: String): List<Medico> {
        return medicosPorEspecialidad(especialidadId)
            .filter { it.nombre.contains(texto, ignoreCase = true) }
    }

    // Lista de citas, mutableStateListOf hace que Compose se entere de los cambios
    // y las pantallas se actualicen solas
    val citas = mutableStateListOf<Cita>()
    // Horarios que atiende cualquier médico
    val horariosBase = listOf(
        "08:00", "09:00", "10:00", "11:00",
        "14:00", "15:00", "16:00", "17:00"
    )
    // Horarios base menos los que ya tienen cita ese día con ese médico
    fun horariosDisponibles(medicoId: Int, fecha: String): List<String> {
        val ocupados = citas
            .filter { it.medicoId == medicoId && it.fecha == fecha }
            .map { it.hora }
        return horariosBase.filter { it !in ocupados }
    }
    // Devuelve la cita creada, o null si no hay sesión o el horario ya está ocupado
    fun agendarCita(medicoId: Int, fecha: String, hora: String): Cita? {
        val usuario = usuarioActual ?: return null
        val ocupado = citas.any { it.medicoId == medicoId && it.fecha == fecha && it.hora == hora }
        if (ocupado) return null
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