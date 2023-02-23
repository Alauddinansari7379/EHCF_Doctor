package com.example.ehcf_doctor.Booking.model

data class ModelGetConsultation(
    val message: String,
    val result: List<Result>,
    val status: Int
)