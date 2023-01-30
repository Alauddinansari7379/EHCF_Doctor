package com.example.ehcf_doctor.Appointments.Upcoming.model

data class Result(
    val comments: String?=null,
    val created_at: String,
    val customer_comments:String?=null,
    val customer_name: String,
    val customer_rating: String,
    val date: String?=null,
    val description: String,
    val doctor_id: String,
    val email: String,
    val id: String,
    val patient_id: String,
    val payment_mode: String,
    val payment_name: String,
    val phone_number: String,
    val profile_picture: String,
    val rating: String,
    val slug: String?=null,
    val start_time: String,
    val status: String,
    val status_for_doctor:String?=null,
    val status_name: String?=null,
    val time:String?=null,
    val title:String?=null,
    val total_amount: String,
    val updated_at: String
)