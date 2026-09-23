package com.rocha.tareatecsup.navigation

import java.net.URLEncoder
import java.nio.charset.StandardCharsets

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
        fun createRoute(doctorName: String, date: String, hour: String): String {
            val encodedName = URLEncoder.encode(doctorName, StandardCharsets.UTF_8.toString())
            val encodedDate = URLEncoder.encode(date, StandardCharsets.UTF_8.toString())
            val encodedHour = URLEncoder.encode(hour, StandardCharsets.UTF_8.toString())
            return "confirmation/$encodedName/$encodedDate/$encodedHour"
        }
    }
}
