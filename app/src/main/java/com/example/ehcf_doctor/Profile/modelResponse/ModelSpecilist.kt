package com.example.ehcf_doctor.Profile.modelResponse

data class ModelSpecilist(
    val count: Int,
    val message: String,
    val result: List<Result>,
    val status: Int
)