package com.example.ehcf_doctor.HealthCube.Model

data class Resource(
    val code: Code,
    val identifier: List<Identifier>,
    val resourceType: String,
    val subject: Subject,
    val valueQuantity: ValueQuantity
)