package com.example.ehcf_doctor.Prescription.model

data class ModelPrescribed(
    val message: String,
    val result: List<Result>,
    val status: Int
)