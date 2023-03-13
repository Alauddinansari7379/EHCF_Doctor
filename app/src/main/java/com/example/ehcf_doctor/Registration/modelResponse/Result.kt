package com.example.ehcf_doctor.Registration.modelResponse

data class Result(
    val blood_group: String,
    val created_at: String,
    val customer_name: String,
    val email: String,
    val fcm_token: String,
    val gender: String,
    val id: Int,
    val last_active_address: String,
    val no_of_ratings: String,
    val overall_ratings: String,
    val password: String,
    val phone_number: String,
    val phone_with_code: String,
    val pre_existing_desease: String?=null,
    val profile_picture: String,
    val status: String,
    val updated_at: String,
    val wallet: String
)