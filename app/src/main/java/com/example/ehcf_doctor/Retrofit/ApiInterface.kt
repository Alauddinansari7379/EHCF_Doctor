package com.example.ehcf_doctor.Retrofit


import com.example.ehcf.PhoneNumber.ModelReponse.ForgotPasswordResponse
import com.example.ehcf_doctor.Appointments.Upcoming.model.ModelAppointmentDatails
import com.example.ehcf_doctor.Appointments.Upcoming.model.ModelConfirmSlotRes
import com.example.ehcf_doctor.Appointments.Upcoming.model.ModelUpComingResponse
import com.example.ehcf_doctor.Booking.model.ModelGetConsultation
import com.example.ehcf_doctor.Login.modelResponse.LoginResponse
import com.example.ehcf_doctor.MainActivity.model.ModelOnline
import com.example.ehcf_doctor.ManageSlots.model.*
import com.example.ehcf_doctor.Prescription.model.ModelCreatePrescription
import com.example.ehcf_doctor.Profile.modelResponse.ModelSpecilist
import com.example.ehcf_doctor.Profile.modelResponse.ModelUpdateQulification
import com.example.ehcf_doctor.Profile.modelResponse.ResetPassResponse
import com.example.ehcf_doctor.Registration.modelResponse.ModelLanguage
import com.example.ehcf_doctor.Registration.modelResponse.ModelSpecilList
import com.example.ehcf_doctor.Registration.modelResponse.RegistationResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiInterface {


    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("phone_with_code") phone_with_code: String,
        @Field("password") password: String,
        @Field("fcm_token") fcm_token: String,
    ): Call<LoginResponse>

    @FormUrlEncoded
    @POST("register")
    fun register(
        @Field("doctor_name") doctor_name: String,
        @Field("qualification") qualification: String,
        @Field("additional_qualification") additional_qualification: String,
        @Field("phone_number") phone_number: String,
        @Field("phone_with_code") phone_with_code: String,
        @Field("fcm_token") fcm_token: String,
        @Field("password") password: String,
        @Field("email") email: String,
        @Field("opening_time") opening_time: String,
        @Field("closing_time") closing_time: String,
        @Field("latitude") latitude: String,
        @Field("longitude") longitude: String,
        @Field("slot_interval") slot_interval: String,
        @Field("specialist") specialist: String,
        @Field("experience") experience: String,
        @Field("gender") gender: String,
        @Field("languages") languages: String,
        @Field("description") description: String,

    ): Call<RegistationResponse>

    @FormUrlEncoded
    @POST("forget_password")
    fun forgotPassword(
        @Field("phone_with_code") phone_with_code: String,
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
        @Field("id") id: String,
        @Field("password") password: String,
    ): Call<ResetPassResponse>

    @FormUrlEncoded
    @POST("get_bookings")
    fun slotManagement(
        @Field("doctor_id") id: String,
    ): Call<ModelManageSlotRes>

    @FormUrlEncoded
    @POST("get_bookings")
    fun getBooking(
        @Field("doctor_id") id: String,
    ): Call<ModelUpComingResponse>

    @FormUrlEncoded
    @POST("accept_booking")
    fun confirmSlot(
        @Field("booking_request_id") booking_request_id: String,
        @Field("slug") slug: String,
    ): Call<ModelConfirmSlotRes>

  @FormUrlEncoded
    @POST("create_slot")
    fun createSlot(
        @Field("doctor_id") doctor_id: String,
        @Field("start_time") start_time: String,
        @Field("end_time") end_time: String,
        @Field("date") date: String,
    ): Call<ModelCreateSlot>


    @POST("get_slot")
    fun getTimeSlot(
        @Query("doctor_id") doctorid: String?,
    ): Call<ModelSlotList>

    @POST("delete_slot")
    fun deleteSlot(
        @Query("slot_id") slot_id: String?,
    ): Call<ModelDeleteSlot>

    @POST("update_slot")
    fun updateSlot(
        @Query("doctor_id") doctor_id: String?,
        @Query("start_time") start_time: String?,
        @Query("end_time") end_time: String?,
        @Query("date") date: String?,
        @Query("slot_id") slot_id: String?,
    ): Call<ModelUpdateSlot>

    @GET("specialist_category")
    fun specialistCategoryTest(
    ): Call<ModelSpecilList>

    @GET("get_languages")
    fun languageList(
    ): Call<ModelLanguage>

    @POST("qualification_update")
    fun updateQualification(
        @Query("specialist") specialist: String?,
        @Query("sub_specialist") sub_specialist: String?,
        @Query("experience") experience: String?,
        @Query("gender") gender: String?,
        @Query("languages") languages: String?,
        @Query("id") id: String?,
    ): Call<ModelUpdateQulification>

    @POST("change_online_status")
    fun changeOnlineStatus(
        @Query("id") id: String?,
        @Query("online_status") online_status: String?,
    ): Call<ModelOnline>

    @POST("create_prescription")
    fun createPrescription(
        @Query("booking_id") booking_id: String?,
        @Query("subjective_information") subjective_information: String?,
        @Query("objective_information") objective_information: String?,
        @Query("assessment") assessment: String?,
        @Query("plan") plan: String?,
        @Query("doctor_notes") doctor_notes: String?,
    ): Call<ModelCreatePrescription>

    @POST("get_consultations")
    fun getConsultation(
        @Query("doctor_id") doctor_id: String?,
    ): Call<ModelGetConsultation>
    @POST("get_consultation_details")
    fun consultationDetails(
        @Query("id") id: String?,
    ): Call<ModelAppointmentDatails>
}