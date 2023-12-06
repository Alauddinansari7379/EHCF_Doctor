package com.example.ehcf_doctor.Prescription.model

data class ModelPrescribed(
    val message: String,
    val result: ArrayList<ResultPrePrescribed>,
    val status: Int
)