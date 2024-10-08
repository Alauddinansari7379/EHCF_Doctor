package com.example.ehcf_doctor.Appointments.Upcoming.model

data class ResultX(
    val comments: Any,
    val consultation_type: String,
    val created_at: String,
    val customer_comments: Any,
    val customer_name: String,
    val member_name: String,
    val customer_rating: String,
    val date: String?=null,
    val doctor_id: String,
    val id: String,
    val patient_id: String,
    val payment_mode: String,
    val payment_name: String,
    val phone_number: String,
    val phone_with_code: String,
    val profile_picture: String,
    val rating: String,
    val profile_image: String,
    val slug: String,
    val status: String,
    val status_for_doctor: String,
    val status_name: String,
    val time: String?=null,
    val total: String,
    val start_time: String,
    val updated_at: String
)