package com.example.ehcf_doctor.Appointments.Cancelled.model

data class ModelRejected(
    val message: String,
    val result: List<Result>,
    val status: Int
)