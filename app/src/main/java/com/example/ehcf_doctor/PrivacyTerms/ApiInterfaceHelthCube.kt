package com.example.ehcf.Testing.Interface

import com.example.ehcf_doctor.HealthCube.Model.ModelRegister
import com.example.ehcf_doctor.PrivacyTerms.model.ModelPrivacyPolicies
import com.example.ehcf_doctor.Registration.modelResponse.ModelSpecilList
import retrofit2.Call
import retrofit2.http.*


interface ApiInterfaceHelthCube {

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


//    @Headers("content-type: application/json")
//    @POST("api/v1/patients")
//    fun createPost(
//        @Header("access_token") access_token: String,
//        @Body dataModal: DataModal?,
//     ): Call<DataModal?>?


//    @GET("api/Profiles/GetProfile?id={id}")
//    fun getUser(
//        @Path("id") id: String?,
//        @Header("Authorization") authHeader: String?
//    ): Call<UserProfile?>?

//    @Headers("content-type: application/json")
//    @POST("api/v1/patients")
//    fun registrationHealthCube(
//        @Header("access_token") access_token: String,
//        @Body dataModal: DataModal?,

//        @Query("firstName") firstName: String,
//        @Query("dob") dob: String,
//        @Query("gender") gender: String,
//        @Query("emailId") emailId: String,
//        @Query("city") city: String,
//        @Query("state") state: String,
//        @Query("zipcode") zipcode: String,
//        @Query("country") country: String,
//        @Query("phoneNumber") phoneNumber: String,
//        @Query("operatorPhoneNumber") operatorPhoneNumber: String,
//        @Query("externalPatientId") externalPatientId: String,
//        @Query("testList") testList:  List<String>,
 //   ): Call<ModelRegister>

//    @POST("register")
//    fun getRegister(): Call<ModelRegisteration>


}
