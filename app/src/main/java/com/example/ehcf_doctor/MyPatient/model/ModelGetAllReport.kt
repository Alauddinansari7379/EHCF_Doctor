package com.example.ehcf.Upload.model

import com.example.ehcf_doctor.MyPatient.model.ResultX

data class ModelGetAllReport(
    val message: String,
    val result: List<ResultX>,
    val status: Int
)