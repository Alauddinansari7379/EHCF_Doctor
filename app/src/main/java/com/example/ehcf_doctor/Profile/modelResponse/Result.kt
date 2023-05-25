package com.example.ehcf_doctor.Profile.modelResponse

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Result(
    val category_image: String,
    val category_name: String,
    val created_at: String,
    val id: Int,
    val status: String,
    val updated_at: String
)
