package com.example.ehcf_doctor.Appointments.Cancelled.model

data class Result(
    val comments: Any,
    val consultation_type: String,
    val created_at: String,
    val customer_comments: Any,
    val customer_name: String,
    val customer_rating: String,
    val date: String,
    val doctor_id: String,
    val email: String,
    val id: String,
    val patient_id: String,
    val payment_mode: String,
    val payment_name: String,
    val phone_number: String,
    val profile_picture: String,
    val rating: String,
    val slug: String,
    val status: String,
    val status_for_doctor: String,
    val status_name: String,
    val time: String,
    val total: String,
    val updated_at: String
)