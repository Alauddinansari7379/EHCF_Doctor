package com.example.ehcf_doctor.Prescription.model

data class ResultXX(
    val comments: Any,
    val consultation_type: String,
    val created_at: String,
    val customer_comments: Any,
    val customer_rating: String,
    val date: String,
    val doctor_id: String,
    val id: Int,
    val patient_id: String,
    val payment_mode: String,
    val customer_name: String?=null,
    val category_name: String,
    val rating: String,
    val start_time: String,
    val end_time: String,
    val doctor_name: String,
    val rtest: String,
    val slot_id: String,
    val status: String,
    val time: String,
    val total: String,
    val updated_at: String
)