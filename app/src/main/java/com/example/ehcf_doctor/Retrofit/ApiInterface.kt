package com.example.ehcf_doctor.Retrofit


import com.example.ehcf.Upload.model.ModelGetAllReport
import com.example.ehcf.report.model.ModelGetTest
import com.example.ehcf_doctor.Appointments.Upcoming.model.ModelAppointmentDatails
import com.example.ehcf_doctor.Appointments.Upcoming.model.ModelConfirmSlotRes
import com.example.ehcf_doctor.Appointments.Upcoming.model.ModelUpComingResponse
import com.example.ehcf_doctor.Booking.model.ModelGetConsultation
import com.example.ehcf_doctor.Dashboard.model.ModelDashboard
import com.example.ehcf_doctor.ForgotPassword.modelReponse.ModelForgotPass
import com.example.ehcf_doctor.HealthCube.Model.ModelHealthCubeReg
import com.example.ehcf_doctor.HealthCube.Model.ModelTestHistory
import com.example.ehcf_doctor.HealthCube.Model.ModelTotalCount
import com.example.ehcf_doctor.Invoice.model.ModelInvoice
import com.example.ehcf_doctor.Invoice.model.ModelInvoiceDetial
import com.example.ehcf_doctor.Login.modelResponse.LoginResponse
import com.example.ehcf_doctor.MainActivity.model.ModelOnline
import com.example.ehcf_doctor.ManageSlots.model.*
import com.example.ehcf_doctor.MyPatient.model.ModelAllReport
import com.example.ehcf_doctor.MyPatient.model.ModelCommentList
import com.example.ehcf_doctor.MyPatient.model.ModelMyPatient
import com.example.ehcf_doctor.MyPatient.model.ModelPatientHistoryX
import com.example.ehcf_doctor.Prescription.model.*
import com.example.ehcf_doctor.PrivacyTerms.model.ModelPrivacyPolicies
import com.example.ehcf_doctor.Profile.modelResponse.*
import com.example.ehcf_doctor.Rating.ModelRating
import com.example.ehcf_doctor.Registration.modelResponse.ModelDegreeJava
import com.example.ehcf_doctor.Registration.modelResponse.ModelLanguage
import com.example.ehcf_doctor.Registration.modelResponse.ModelOTP
import com.example.ehcf_doctor.Registration.modelResponse.ModelRegistrationNew
import com.example.ehcf_doctor.Registration.modelResponse.ModelSpecilList
import okhttp3.MultipartBody
import okhttp3.RequestBody
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

    @Multipart
    @POST("register")
    fun register(
        @Query("doctor_name") doctor_name: String,
        @Query("qualification") qualification: String,
        @Query("additional_qualification") additional_qualification: String,
        @Query("phone_number") phone_number: String,
        @Query("phone_with_code") phone_with_code: String,
        @Query("fcm_token") fcm_token: String,
        @Query("password") password: String,
        @Query("email") email: String,
        @Query("opening_time") opening_time: String,
        @Query("closing_time") closing_time: String,
        @Query("latitude") latitude: String,
        @Query("longitude") longitude: String,
        @Query("slot_interval") slot_interval: String,
        @Query("specialist") specialist: String,
        @Query("experience") experience: String,
        @Query("gender") gender: String,
        @Query("languages") languages: String,
        @Query("description") description: String,
        @Query("reg_no") reg_no: String,
        @Part reg_cer: MultipartBody.Part,
        @Part("desc") desc: RequestBody,
        @Query("follow_up") follow_up: String,
        @Query("postal_code") postal_code: String,

        ): Call<ModelRegistrationNew>

    @FormUrlEncoded
    @POST("forget_password")
    fun forgotPassword(
        @Field("phone_with_code") phone_with_code: String,
    ): Call<ModelForgotPass>

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

//    @FormUrlEncoded
//    @POST("accept_booking")
//    fun confirmSlot(
//        @Field("booking_request_id") booking_request_id: String,
//        @Field("slug") slug: String,
//    ): Call<ModelConfirmSlotRes>

    @FormUrlEncoded
    @POST("consultation_status_change")
    fun confirmSlot(
        @Field("consultation_request_id") booking_request_id: String,
        @Field("slug") slug: String,
    ): Call<ModelConfirmSlotRes>

    @POST("create_slot")
    fun createSlot(
        @Query("doctor_id") doctor_id: String,
        @Query("start_time") start_time: String,
        @Query("end_time") end_time: String,
        @Query("day") day: String,
        @Query("consultation_type") consultation_type: String,
        @Query("address") address: String,
    ): Call<My_Model>


    @POST("get_slot")
    fun getTimeSlot(
        @Query("doctor_id") doctorid: String?,
        @Query("day") day: String?,
    ): Call<ModelSlotList>

    @GET("doctor_address")
    fun getAddress(
        @Query("id") doctorid: String?,
     ): Call<ModelGetAddress>

    @POST("delete_slot")
    fun deleteSlot(
        @Query("slot_id") slot_id: String?,
    ): Call<ModelDeleteSlot>

    @POST("update_slot")
    fun updateSlot(
        @Query("doctor_id") doctor_id: String?,
        @Query("start_time") start_time: String?,
        @Query("end_time") end_time: String?,
        @Query("day") day: String?,
        @Query("slot_id") slot_id: String?,
    ): Call<ModelUpdateSlot>

    @GET("specialist_category")
    fun specialistCategoryTest(
    ): Call<ModelSpecilList>

    @GET("get_test_name")
    fun testName(
    ): Call<ModelTestList>

    @GET("get_medicine_name")
    fun medicineName(
    ): Call<ModelTestList>

    @GET("get_degree")
    fun getDegree(
    ): Call<ModelDegreeJava>

    @GET("get_year")
    fun getYear(
    ): Call<ModelYear>

    @GET("get_state")
    fun getState(
    ): Call<ModelState>

    @GET("get_languages")
    fun languageList(
    ): Call<ModelLanguage>

    @GET("get_city")
    fun getCity(
    ): Call<ModelCity>

    @POST("profile_update")
    fun profileUpdate(
        @Query("specialist") specialist: String?,
        @Query("sub_specialist") sub_specialist: String?,
        @Query("experience") experience: String?,
        @Query("gender") gender: String?,
        @Query("id") id: String?,
        @Query("clinic_name") clinic_name: String?,
        @Query("address") address: String?,
        @Query("city") city: String?,
        @Query("country") country: String?,
        @Query("state") state: String?,
        @Query("pricing") pricing: String?,
        @Query("services") services: String?,
        @Query("degree") degree: String?,
        @Query("yearofcom") yearofcom: String?,
        @Query("college") college: String?,
        @Query("hos_address") hos_address: String?,
        @Query("hos_name") hos_name: String?,
        @Query("registration") registration: String?,
        @Query("reg_year") reg_year: String?,
        @Query("clinic_address") clinic_address: String?,
        @Query("clinic_address_one") clinic_address_one: String?,
        @Query("clinic_address_two") clinic_address_two: String?,
        @Query("opening_time") opening_time: String,
        @Query("closing_time") closing_time: String,
        @Query("postal_code") postal_code: String,
        @Query("street") street: String,
        @Query("line1") line1: String,
        @Query("line2") line2: String,
    ): Call<ModelProfileUpdate>
//street,, added
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
        @Query("is_test") is_test: String?,
        @Query("test_name") test_name: String?,
        @Query("instructions") instructions: String?,
        @Query("status") status: String?,
        @Query("start_follow_up_date") start_follow_up_date: String?,
        @Query("end_follow_up_date") end_follow_up_date: String?,
    ): Call<ModelPreJava>

    @POST("create_prescription_items")
    fun createMedicine(
        @Query("prescription_id") booking_id: String?,
        @Query("medicine_name") subjective_information: String?,
        @Query("timing") objective_information: String?,
        @Query("intake") assessment: String?,
        @Query("frequency") plan: String?,
        @Query("duration") doctor_notes: String?,
    ): Call<ModeMedicine>

    @POST("get_test")
    fun getTest(
        @Query("customer_prescription") customer_prescription: String?,
    ): Call<ModelGetTest>

    @POST("create_diagnosis")
    fun createDiagnosis(
        @Query("prescription_id") prescription_id: String?,
        @Query("diagnosis_name") diagnosis_name: String?,
        @Query("description") description: String?,
    ): Call<ModeMedicine>

    @POST("create_lab_test")
    fun createLabTest(
        @Query("prescription_id") prescription_id: String?,
        @Query("test_name") test_name: String?,
        @Query("after") after: String?,
        @Query("instructions") instructions: String?,
    ): Call<ModeMedicine>

    @POST("get_consultations")
    fun getConsultation(
        @Query("doctor_id") doctor_id: String?,
        @Query("slug") slug: String?,
    ): Call<ModelGetConsultation>

    @POST("notify")
    fun notify(
        @Query("patient_id") doctor_id: String?,
     ): Call<ModelGetConsultation>

    @POST("search_appointments")
    fun searchAppointments(
        @Query("patient_name") patient_name: String?,
        @Query("doctor_id") doctor_id: String?,
        @Query("slug") slug: String?,
    ): Call<ModelGetConsultation>

    @POST("get_consultation_details")
    fun consultationDetails(
        @Query("id") id: String?,
    ): Call<ModelAppointmentDatails>

    @POST("get_prescribed")
    fun getPrescribed(
        @Query("doctor_id") id: String?,
    ): Call<ModelPrescribed>

    @POST("view_prescription")
    fun getPrescriptionDetial(
        @Query("id") id: String?,
    ): Call<ModelPreDetJava>
    @POST("p_history")
    fun patientHistory(
        @Query("patient_id") id: String,
    ): Call<ModelPatientHistoryX>
    @POST("get_patients")
    fun getPatients(
        @Query("doctor_id") id: String?,
    ): Call<ModelMyPatient>

    @POST("healthcube_patient_list")
    fun healthcubePatientList(
        @Query("doctor_id") id: String?,
    ): Call<ModelMyPatient>

    @POST("get_healthcube_report")
    fun healthCubeReportHistory(
        @Query("id") id: String?,
     ): Call<ModelTestHistory>

    @POST("healthcube_exsisting_patient")
    fun healthcubeExsistingpatient(
        @Query("doctor_id") id: String?,
    ): Call<ModelMyPatient>

    @GET("customer_all_comments")
    fun customerAllComments(
        @Query("patient_id") doctor_id: String
    ): Call<ModelCommentList>

    @POST("pending_prescription")
    fun pendingPre(
        @Query("doctor_id") id: String?,
    ): Call<ModelPendingPre>

    @POST("profile_update")
    fun updateNameEmail(
        @Query("id") id: String?,
        @Query("doctor_name") doctor_name: String?,
        @Query("email") email: String?,
    ): Call<ModelUpdateNameEmail>

    @POST("get_privacy_policy")
    fun privacyPolicy(
        @Query("user_type") id: String?,
    ): Call<ModelPrivacyPolicies>

    @POST("serch_prescription")
    fun searchPrescription(
        @Query("name") name: String?,
        @Query("doctor_id") doctor_id: String?,
    ): Call<ModelPendingPre>

    @POST("serch_prescription")
    fun searchAppointment(
        @Query("name") name: String?,
        @Query("doctor_id") doctor_id: String?,
    ): Call<ModelGetConsultation>

    @POST("serch_prescription")
    fun searchPrescribed(
        @Query("name") name: String?,
        @Query("doctor_id") doctor_id: String?,
    ): Call<ModelPrescribed>

    @POST("search_patients")
    fun searchPatient(
        @Query("doctor_id") doctor_id: String?,
        @Query("patient_name") patient_name: String?,
    ): Call<ModelMyPatient>

    @POST("get_invoice_list")
    fun invoiceList(
        @Query("doctor_id") doctor_id: String?,
    ): Call<ModelInvoice>

    @POST("search_patients_date")
    fun searchPatientsDate(
        @Query("patient_name") patient_name: String?,
        @Query("date") date: String?,
        @Query("doctor_id") doctor_id: String?,
    ): Call<ModelInvoice>

    @POST("filter_doctor_date")
    fun filterAppointmentByDate(
        @Query("doctor_id") doctor_id: String?,
        @Query("date") date: String?,
        @Query("slug") slug: String?,
    ): Call<ModelDashboard>

    @POST("invoice_details")
    fun invoiceDetails(
        @Query("invoice_id") invoice_id: String?,
    ): Call<ModelInvoiceDetial>

    @POST("inactive_slot")
    fun inactiveSlot(
        @Query("doctor_id") invoice_id: String?,
        @Query("day") day: String?,
    ): Call<ModelActive>

    @POST("active_slot")
    fun activeSlot(
        @Query("doctor_id") invoice_id: String?,
        @Query("day") day: String?,
    ): Call<ModelActive>

    @POST("active_deactive_slot")
    fun activeASlot(
        @Query("id") invoice_id: String?,
        @Query("IsActive") IsActive: String?,
    ): Call<ModelActive>

    @POST("get_all_sll")
    fun switchButton(
        @Query("doctor_id") invoice_id: String?,
    ): Call<ModelSwitechButton>

    @POST("get_repo")
    fun patientReport(
        @Query("patient_id") patient_id: String?,
    ): Call<ModelAllReport>

    @POST("get_repo")
    fun getReport(
        @Query("patient_id") patient_id: String?,
    ): Call<ModelGetAllReport>

    @POST("edit_prescription")
    fun modifyPrescrption(
        @Query("id") booking_id: String?,
        @Query("subjective_information") subjective_information: String?,
        @Query("objective_information") objective_information: String?,
        @Query("assessment") assessment: String?,
        @Query("plan") plan: String?,
        @Query("is_test") is_test: String?,
        @Query("doctor_notes") doctor_notes: String?,
    ): Call<ModelModify>

    @POST("check_phone")
    fun checkPhone(
        @Query("phone_with_code") phone_with_code: String?,
    ): Call<ModelOTP>

    @POST("customer_consultation_rating")
    fun rating(
        @Query("id") id: String?,
        @Query("rating") rating: String?,
        @Query("comments") comments: String?,
    ): Call<ModelRating>

    @POST("get_healthcube_report_count")
    fun healthcubeReportCount(
        @Query("doctor_id") id: String?,
     ): Call<ModelTotalCount>

    @POST("patient_healthcube")
    fun patientHealthcubeReg(
        @Query("name") name: String?,
        @Query("gender") gender: String?,
        @Query("city") city: String?,
        @Query("state") state: String?,
        @Query("dob") dob: String?,
        @Query("country") country: String?,
        @Query("phone") phone: String?,
        @Query("operator_phone") operator_phone: String?,
        @Query("external_patient_id") external_patient_id: String?,
        @Query("test_list") test_list: ArrayList<String>,
        @Query("zipcode") zipcode: String?,
        @Query("email") email: String?,
        @Query("doctor_id") doctor_id: String?,
        @Query("patient_id") patient_id: String?,
    ): Call<ModelHealthCubeReg>

    @POST("healthcube_report_insert")
    fun healthcubeReportInsert(
        @Query("patient_health_cubes_id") patient_health_cubes_id: String?,
        @Query("test_name") test_name: String?,
        @Query("doctor_id") doctor_id: String?,
        @Query("temperature") temperature: String?,
        @Query("weight") weight: String?,
        @Query("bmi") bmi: String?,
        @Query("metabolism") metabolism: String?,
        @Query("body_water") body_water: String?,
        @Query("body_fat") body_fat: String?,
        @Query("bone_mass") bone_mass: String?,
        @Query("protein") protein: String?,
         @Query("visceralfat") visceralfat: String?,
        @Query("bodyage") bodyage: String?,
        @Query("musclemass") musclemass: String?,
        @Query("lbm") lbm: String?,
        @Query("obesity") obesity: String?,
        @Query("height") height: String?,
        @Query("diastolic") diastolic: String?,
        @Query("systolic") systolic: String?,
        @Query("spotwo") spotwo: String?,
        @Query("pulse_rate") pulse_rate: String?,
        @Query("blood_glucose") blood_glucose: String?,
        @Query("uric_acid") uric_acid: String?,
        @Query("hemoglobin") hemoglobin: String?,
        @Query("malaria") malaria: String?,
        @Query("typhoid") typhoid: String?,
    ): Call<ModelHealthCubeReg>


    @Multipart
    @Headers("Accept: application/json")
    @POST("profile_picture")
    fun profilePicture(
        @Query("id") id: String,
        @Part image: MultipartBody.Part,
        @Part("desc") desc: RequestBody,
        ): Call<ModelProfilePic>

}