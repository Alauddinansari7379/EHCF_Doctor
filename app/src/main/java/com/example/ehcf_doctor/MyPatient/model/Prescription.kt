package com.example.ehcf_doctor.MyPatient.model

data class Prescription(
    val after: String,
    val customer_name: String,
    val instructions: String,
    val test_name: String,
    val test_report: String
)