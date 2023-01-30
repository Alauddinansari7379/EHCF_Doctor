package com.example.ehcf_doctor.Registration

import android.R
import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import com.example.ehcf.Helper.myToast
import com.example.ehcf.sharedpreferences.SessionManager
import com.example.ehcf_doctor.Login.activity.SignIn
import com.example.ehcf_doctor.MainActivity
import com.example.ehcf_doctor.Registration.modelResponse.RegistationResponse
import com.example.ehcf_doctor.databinding.ActivityRegirstrationTestBinding
import com.example.myrecyview.apiclient.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegirstrationTest : AppCompatActivity() {
    private lateinit var binding:ActivityRegirstrationTestBinding
    private val context: Context =this@RegirstrationTest
    private var bloodGroup=""
    private var phoneNumberWithCode=""
    var progressDialog: ProgressDialog? =null
    private var phoneNumberWithCodeNew=""
    private var genderValue=0
    var openingTimeList = ArrayList<String>()
    var closingTimeList = ArrayList<String>()
    var countryCodeNew="91"

    private lateinit var sessionManager: SessionManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityRegirstrationTestBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sessionManager = SessionManager(this)

        binding.imgBack.setOnClickListener {
            onBackPressed()
        }

        openingTimeList.add("01:00:00")
        openingTimeList.add("02:00:00")
        openingTimeList.add("03:00:00")
        openingTimeList.add("04:00:00")
        openingTimeList.add("05:00:00")
        openingTimeList.add("06:00:00")
        openingTimeList.add("07:00:00")
        openingTimeList.add("08:00:00")
        openingTimeList.add("09:00:00")
        openingTimeList.add("10:00:00")
        openingTimeList.add("11:00:00")
        openingTimeList.add("12:00:00")
        openingTimeList.add("13:00:00")
        openingTimeList.add("14:00:00")
        openingTimeList.add("15:00:00")
        openingTimeList.add("16:00:00")
        openingTimeList.add("17:00:00")
        openingTimeList.add("18:00:00")
        openingTimeList.add("19:00:00")
        openingTimeList.add("20:00:00")
        openingTimeList.add("21:00:00")
        openingTimeList.add("22:00:00")
        openingTimeList.add("23:00:00")
        openingTimeList.add("24:00:00")


        binding.spinnerOpen.adapter = ArrayAdapter<String>(context, R.layout.simple_list_item_1, openingTimeList)
        binding.spinnerClose.adapter = ArrayAdapter<String>(context, R.layout.simple_list_item_1, openingTimeList)

        binding.btnRegister.setOnClickListener {
            callApi()
        }

        binding.spinnerCountryCode.setOnCountryChangeListener {
            val countryCode = binding.spinnerCountryCode.selectedCountryCodeWithPlus

            countryCodeNew = countryCode.substring(1)
            Log.e("Log","countryCode-$countryCodeNew")

        }

    }
    @SuppressLint("LogNotTimber")
    private fun callApi(){

        if (binding.edtFirstName.text.isEmpty()){
            binding.edtFirstName.error="First Name Required"
            binding.edtFirstName.requestFocus()
        }
        if (binding.edtLastName.text.isEmpty()){
            binding.edtLastName.error="Last Name Required"
            binding.edtLastName.requestFocus()
        }
        if (binding.edtPhoneNumber.text.isEmpty()){
            binding.edtPhoneNumber.error="Phone Number Required"
            binding.edtPhoneNumber.requestFocus()
        }
        if (binding.edtEmail.text.isEmpty()){
            binding.edtEmail.error="Email Required"
            binding.edtEmail.requestFocus()
        }

        if (binding.edtPassword.text.isEmpty()){
            binding.edtPassword.error="Password Required"
            binding.edtPassword.requestFocus()
        }
        if (binding.edtConfirmPassword.text!!.isEmpty()){
            binding.edtConfirmPassword.error="Password Required"
            binding.edtConfirmPassword.requestFocus()
        }
        if (binding.edtQualification.text.isEmpty()){
            binding.edtQualification.error="Please enter Qualification"
            binding.edtQualification.requestFocus()
        }
        if (binding.edtAdditionalQualification.text.isEmpty()){
            binding.edtAdditionalQualification.error="Please enter AdditionalQualification"
            binding.edtAdditionalQualification.requestFocus()
        }

        val firstName= binding.edtFirstName.text.toString()
        val lastName= binding.edtLastName.text.toString()
        val doctorName= "$firstName $lastName"
        val phoneNumber=binding.edtPhoneNumber.text.toString()
              phoneNumberWithCode=binding.edtPhoneNumberWithCode.text.toString()
        val email=binding.edtEmail.text.toString()
        val qualification=binding.edtQualification.text.toString()
        val additionalQualification=binding.edtAdditionalQualification.text.toString()
        val password=binding.edtPassword.text.toString()
        val confirmPassword=binding.edtConfirmPassword.text.toString()
        val fcmToken="fdsfsdfsdfsr4354fdsfsdgffdsgfd"
        val openningTime="09:00:00"
        val closeTiming="17:00:00"
        val lattitude="11.854369"
        val longitude="32.856965"
        phoneNumberWithCodeNew = countryCodeNew + phoneNumberWithCode

        Log.e("Ala", "doctorName-$doctorName")
        Log.e("Ala", "qualification-$qualification")
        Log.e("Ala", "additionalQualification-$additionalQualification")
        Log.e("Ala", "phoneNumber-$phoneNumber")
        Log.e("Ala", "phoneNumberWithCodeNew-$phoneNumberWithCodeNew")
        Log.e("Ala", "fcmToken-$fcmToken")
        Log.e("Ala", "confirmPassword-$confirmPassword")
        Log.e("Ala", "email-$email")



        if(password !=confirmPassword){
            binding.edtConfirmPassword.error="Password Miss Match"
            binding.edtConfirmPassword.requestFocus()
        }
        else{
            progressDialog = ProgressDialog(this@RegirstrationTest)
            progressDialog!!.setMessage("Loading..")
            progressDialog!!.setTitle("Please Wait")
            progressDialog!!.isIndeterminate = false
            progressDialog!!.setCancelable(true)

            progressDialog!!.show()

            ApiClient.apiService.register(doctorName,qualification,additionalQualification,phoneNumber,
                phoneNumberWithCodeNew,fcmToken,confirmPassword,email,openningTime,closeTiming,lattitude,longitude)
                .enqueue(object : Callback<RegistationResponse> {
                    @SuppressLint("LogNotTimber")
                    override fun onResponse(
                        call: Call<RegistationResponse>, response: Response<RegistationResponse>
                    ) {

                        Log.e("Ala","${response.body()!!.result}")
                        Log.e("Ala", response.body()!!.message)
                        Log.e("Ala","${response.body()!!.status}")

                        if (response.body()!!.status==1){
                            myToast(this@RegirstrationTest,response.body()!!.message)
                            progressDialog!!.dismiss()
                            val intent = Intent(applicationContext, SignIn::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                            finish()
                            startActivity(intent)
                        }
                        else{
                            myToast(this@RegirstrationTest,"${response.body()!!.message}")
                            progressDialog!!.dismiss()

                        }

                    }

                    override fun onFailure(call: Call<RegistationResponse>, t: Throwable) {
                        myToast(this@RegirstrationTest,t.message.toString())
                        progressDialog!!.dismiss()

                    }

                })
        }

    }
}