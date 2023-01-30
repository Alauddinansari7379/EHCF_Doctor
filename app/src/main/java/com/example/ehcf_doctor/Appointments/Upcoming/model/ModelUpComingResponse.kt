package com.example.ehcf_doctor.Appointments.Upcoming.model

data class ModelUpComingResponse(
    val message: String,
    val result: List<Result>,
    val status: Int
)