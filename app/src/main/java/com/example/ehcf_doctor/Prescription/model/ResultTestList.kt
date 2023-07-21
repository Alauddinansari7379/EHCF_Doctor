package com.example.ehcf_doctor.Prescription.model

data class ResultTestList(
    val after: String,
    val created_at: String,
    val instructions: String,
    val prescriptionid: String,
    val id: String,
     val test_name: String,
    val member_name: String,
    val date: String,
    val customer_name: String,
    val test_report: String?=null
)