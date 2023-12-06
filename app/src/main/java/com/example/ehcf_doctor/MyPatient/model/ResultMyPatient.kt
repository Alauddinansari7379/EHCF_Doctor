package com.example.ehcf_doctor.MyPatient.model

data class ResultMyPatient(
    val blood_group: String,
    val created_at: String,
    val customer_name: String,
    val email: String?=null,
    val fcm_token: String,
    val gender: String,
    val name: String,
    val phone: String,
    val dob: String,
    val patient_id: String,
    val id: String,
    val last_active_address: String,
    val no_of_ratings: String,
    val overall_ratings: String,
    val password: String,
    val phone_number: String,
    val phone_with_code: String,
    val pre_existing_desease: Any,
    val profile_picture: String?=null,
    val status: String,
    val updated_at: String,
    val wallet: String
)