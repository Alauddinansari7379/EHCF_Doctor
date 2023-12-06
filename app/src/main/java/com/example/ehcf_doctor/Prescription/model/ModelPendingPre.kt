package com.example.ehcf_doctor.Prescription.model

data class ModelPendingPre(
    val message: String,
    val result: ArrayList<ResultPrePending>,
    val status: Int
)