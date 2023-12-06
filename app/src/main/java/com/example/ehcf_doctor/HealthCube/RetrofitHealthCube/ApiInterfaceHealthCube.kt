package com.example.ehcf_doctor.HealthCube.RetrofitHealthCube


import com.example.ehcf_doctor.HealthCube.Model.*
import com.example.ehcf_doctor.ManageSlots.model.*
import com.example.ehcf_doctor.Prescription.model.*
import com.example.ehcf_doctor.Profile.modelResponse.*
import retrofit2.Call
import retrofit2.http.*


interface ApiInterfaceHealthCube {

    @POST("api/v1/patients")
    fun registrationHealthCube(
        @Query("firstName") patient_health_cubes_id: String?,
        @Query("dob") dob: String?,
        @Query("gender") gender: String?,
        @Query("phoneNumber") phoneNumber: String?,
        @Query("operatorPhoneNumber") operatorPhoneNumber: String?,
        @Query("externalPatientId") externalPatientId: String?,
        @Query("testList") testList: String?,
    ): Call<ModelHealthCubeReg>

    @POST("api/v1/patients")
    fun postData(
        @Header("Authorization") Authorization: String,
        @Body dataModal: DataModal?
    ): Call<ModelRegister>

    @Headers("Content-Type: application/x-www-form-urlencoded")
    @POST("token")
    fun createToken(@Body tokenModal: DataModalToken?): Call<ModelToken>

    @FormUrlEncoded
    @POST("token")
    @Headers(*["Content-Type: application/x-www-form-urlencoded", "accept-encoding: gzip, deflate", "Accept: application/json;charset=UTF-8"])
    fun createTokenNew(
        @Field("grant_type") grant_type: String?,
        @Field("client_id") client_id: String?,
        @Field("client_secret") client_secret: String?,
    ): Call<ModelToken>

}