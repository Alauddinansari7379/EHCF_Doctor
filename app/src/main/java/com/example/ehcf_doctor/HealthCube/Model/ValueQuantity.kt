package com.example.ehcf_doctor.HealthCube.Model

data class ValueQuantity(
    val code: String,
    val system: String,
    val unit: String,
    val value: Double
)