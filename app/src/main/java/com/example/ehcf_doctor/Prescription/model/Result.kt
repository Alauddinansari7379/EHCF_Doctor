package com.example.ehcf_doctor.Prescription.model

data class Result(
    val assessment: String,
    val booking_id: String,
    val created_at: String,
    val date: String,
    val doctor_id: String,
    val doctor_notes: String,
    val id: String,
    val pid: String,
    val objective_information: String,
    val patient_id: String,
    val plan: String,
    val category_name: String,
    val customer_name: String,
    val profile_image: String,
    val member_name: String,
    val doctor_name: String,
    val start_time: String,
    val end_time: String,
    val subjective_information: String,
    val report: String?="null",
    val is_test: String,
    val updated_at: String
)