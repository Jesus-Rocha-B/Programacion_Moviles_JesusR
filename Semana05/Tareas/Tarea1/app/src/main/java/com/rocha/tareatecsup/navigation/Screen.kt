package com.rocha.tareatecsup.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object MyAppointments : Screen("my_appointments")
    object MedicalHistory : Screen("medical_history")
    object Profile : Screen("profile")

    object DoctorProfile : Screen("doctor_profile/{doctorId}") {
        fun createRoute(doctorId: Int) = "doctor_profile/$doctorId"
    }

    object Appointment : Screen("appointment/{doctorId}") {
        fun createRoute(doctorId: Int) = "appointment/$doctorId"
    }

    object Confirmation : Screen("confirmation/{doctorName}/{date}/{hour}") {
        fun createRoute(doctorId: Int, date: String, hour: String) =
            "confirmation/Doctor$doctorId/$date/$hour"
    }
}