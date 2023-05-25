package com.example.ehcf_doctor.Prescription.model

data class Data(
    val assessment: String,
    val booking_id: Int,
    val created_at: String,
    val date: String,
    val doctor_id: String,
    val doctor_notes: String,
    val id: Int,
    val objective_information: String,
    val patient_id: String,
    val plan: String,
    val subjective_information: String,
    val updated_at: String
)