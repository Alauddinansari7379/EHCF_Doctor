package com.example.ehcf_doctor.PrivacyTerms.model

data class Result(
    val created_at: String,
    val description: String,
    val id: Int,
    val privacy_policy_type_id: String,
    val status: String,
    val title: String,
    val updated_at: String,
    val user: String
)