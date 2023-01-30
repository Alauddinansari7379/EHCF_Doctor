package com.example.ehcf_doctor.Retrofit


import com.example.ehcf.PhoneNumber.ModelReponse.ForgotPasswordResponse
import com.example.ehcf_doctor.Appointments.Upcoming.model.ModelUpComingResponse
import com.example.ehcf_doctor.Login.modelResponse.LoginResponse
import com.example.ehcf_doctor.ManageSlots.model.ModelManageSlotRes
import com.example.ehcf_doctor.Profile.modelResponse.ResetPassResponse
import com.example.ehcf_doctor.Registration.modelResponse.RegistationResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiInterface {


    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("phone_with_code") phone_with_code:String,
        @Field("password") password:String,
        @Field("fcm_token") fcm_token:String,
    ):Call<LoginResponse>

    @FormUrlEncoded
    @POST("register")
    fun register(
            @Field("doctor_name") doctor_name:String,
            @Field("qualification") qualification:String,
            @Field("additional_qualification") additional_qualification:String,
            @Field("phone_number") phone_number:String,
            @Field("phone_with_code") phone_with_code:String,
            @Field("fcm_token") fcm_token:String,
            @Field("password") password:String,
            @Field("email") email:String,
            @Field("opening_time") opening_time:String,
            @Field("closing_time") closing_time:String,
            @Field("latitude") latitude:String,
            @Field("longitude") longitude:String,
    ): Call<RegistationResponse>

    @FormUrlEncoded
    @POST("forget_password")
    fun forgotPassword(
        @Field("phone_with_code") phone_with_code:String,
    ): Call<ForgotPasswordResponse>
//
//    @FormUrlEncoded
//    @POST("add_address")
//    fun addAddress(
//        @Field("customer id") customer_id:Int,
//        @Field("address") address:String,
//        @Field("landmark") landmark:String,
//        @Field("lat") lat:String,
//        @Field("lng") lng:String,
//    ): Call<AddAddressResponse>
//
//
//    @POST("all_addresses")
//    fun allAddress(@Query("customer id") customer_id: Int
//    ): Call<AddressListResponse>
//
    @FormUrlEncoded
    @POST("reset_password")
    fun resetPassword(
        @Field("id") id:String,
        @Field("password") password:String,
    ): Call<ResetPassResponse>

    @FormUrlEncoded
    @POST("get_bookings")
    fun slotManagement(
        @Field("doctor_id") id:String,
    ): Call<ModelManageSlotRes>

    @FormUrlEncoded
    @POST("get_bookings")
    fun getBooking(
        @Field("doctor_id") id:String,
    ): Call<ModelUpComingResponse>



}