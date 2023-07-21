package com.example.ehcf.report.model

import com.example.ehcf_doctor.Prescription.model.ResultTestList

data class ModelGetTest(
    val message: String,
    val result: List<ResultTestList>,
    val status: Int
)