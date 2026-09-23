package com.rocha.tareatecsup.navigation

// Clase cerrada (solo existen las pantallas que se declaran aquí)
sealed class Screen(val route: String) {
    object Home : Screen("home")
    object MyAppointments : Screen("my_appointments")
    object MedicalHistory : Screen("medical_history")
    object Profile : Screen("profile")

    // Ruta con parámetro: id del médico elegido
    object DoctorProfile : Screen("doctor_profile/{doctorId}") {
        fun createRoute(doctorId: Int) = "doctor_profile/$doctorId"
    }

    // Ruta con parámetro: agendar cita con ese médico
    object Appointment : Screen("appointment/{doctorId}") {
        fun createRoute(doctorId: Int) = "appointment/$doctorId"
    }

    object Confirmation : Screen("confirmation")
}