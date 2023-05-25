package com.example.ehcf.Testing.Interface

import com.example.ehcf_doctor.MyPatient.model.ModelPatientHistoryX
import com.example.ehcf_doctor.PrivacyTerms.model.ModelPrivacyPolicies
import com.example.ehcf_doctor.Registration.modelResponse.ModelSpecilList
import retrofit2.Call
import retrofit2.http.*

interface apiInterface {

    //https://jsonplaceholder.typicode.com/users
    //https://ehcf.thedemostore.in/api/customer/register


//    @GET("photos")
//    fun getPhotos(): Call<List<ModelPhotos>>



    @POST("get_privacy_policy")
    fun privacyPolicy(
        @Query("user_type") id: String?,
    ): Call<ModelPrivacyPolicies>

    @GET("specialist_category")
    fun specialistCategory(): Call<ModelSpecilList>

    @POST("p_history")
    fun patientHistory(
        @Query("patient_id") id: String,
    ): Call<ModelPatientHistoryX>

//    @POST("register")
//    fun getRegister(): Call<ModelRegisteration>


}
