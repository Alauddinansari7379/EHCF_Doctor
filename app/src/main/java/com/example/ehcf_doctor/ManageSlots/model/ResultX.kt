package com.example.ehcf_doctor.ManageSlots.model

data class ResultX(
    val created_at: String,
    val date: String,
    val doctor_id: String,
    val end_time: String,
    val id: Int,
    var day:String?=null,
    val start_time: String,
    val address: String?,
    val consultation_type: String,
    val IsActive: String,
    val status: String,
    val updated_at: String
)