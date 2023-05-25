package com.example.ehcf_doctor.Invoice.model

data class ModelInvoice(
    val message: String,
    val result: List<Result>,
    val status: Int
)