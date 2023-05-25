package com.example.ehcf_doctor.PrivacyTerms.model

data class ModelPrivacyPolicies(
    val count: Int,
    val message: String,
    val result: List<Result>,
    val status: Int
)