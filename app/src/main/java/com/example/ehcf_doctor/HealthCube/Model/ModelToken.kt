package com.example.ehcf_doctor.HealthCube.Model

data class ModelToken(
    val access_token: String,
    val expires_in: Int,
     val refresh_expires_in: Int,
    val scope: String,
    val token_type: String,
    val error_description: String,
    val error: String,
)