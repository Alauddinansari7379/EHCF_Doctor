package com.example.ehcf_doctor.Login.activity

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.ehcf.Helper.myToast
import com.example.ehcf.sharedpreferences.SessionManager
import com.example.ehcf_doctor.Login.modelResponse.LoginResponse
import com.example.ehcf_doctor.MainActivity.activity.MainActivity
import com.example.ehcf_doctor.ForgotPassword.MobileNumber
import com.example.ehcf_doctor.Prescription.AddPrescription
import com.example.ehcf_doctor.Registration.activity.RegirstrationTest
import com.example.ehcf_doctor.databinding.ActivitySignInBinding
import com.example.myrecyview.apiclient.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignIn : AppCompatActivity() {
    private val context: Context =this@SignIn
    var progressDialog : ProgressDialog?=null
    private lateinit var sessionManager: SessionManager
    private lateinit var binding:ActivitySignInBinding
    var countryCodeNew="91"
    @SuppressLint("LogNotTimber")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)
        Log.e("Log","countryCode-$countryCodeNew")

        binding.spinnerCountryCode.setOnCountryChangeListener {
           val countryCode = binding.spinnerCountryCode.selectedCountryCodeWithPlus

            countryCodeNew = countryCode.substring(1)
            Log.e("Log","countryCode-$countryCodeNew")

        }
        if (sessionManager.isLogin) {
            startActivity(Intent(context, MainActivity::class.java))
            finish()
        }
        progressDialog = ProgressDialog(this@SignIn)
        progressDialog!!.setMessage("Loading..")
        progressDialog!!.setTitle("Please Wait")
        progressDialog!!.isIndeterminate = false
        progressDialog!!.setCancelable(true)

        binding.tvForgot.setOnClickListener {
            startActivity(Intent(this, MobileNumber::class.java))
        }
        binding.tvSignUp.setOnClickListener{
            startActivity(Intent(this, RegirstrationTest::class.java))
        }
        binding.btnSignIn.setOnClickListener{
            if (binding.edtPhone.text!!.isEmpty()) {
                binding.edtPhone.error = "Enter Phone Number"
                binding.edtPhone.requestFocus()
                return@setOnClickListener
            }
            if (binding.edtPassword.text!!.isEmpty()) {
                binding.edtPassword.error = "Enter Password"
                binding.edtPassword.requestFocus()
                return@setOnClickListener
            }
            val phoneNumber = binding.edtPhone.text.toString().trim()
            val password = binding.edtPassword.text.toString().trim()

            val phoneNumberNew=countryCodeNew+phoneNumber
            Log.e("Alauddin","phoneNumber-${phoneNumberNew}")
            Log.e("Alauddin","password-${password}")


            val fcmToken="sadasdqweqq34e23fdcdsf"
            progressDialog!!.show()

            ApiClient.apiService.login(phoneNumberNew,password,fcmToken).enqueue(object :
                Callback<LoginResponse> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    if (response.body()!!.status==1){
                        myToast(this@SignIn, response.body()!!.message)
                        progressDialog!!.dismiss()

                        sessionManager.isLogin = true
                        sessionManager.fcmToken = response.body()!!.result.fcm_token
                        sessionManager.onlineStatus = response.body()!!.result.online_status
                        sessionManager.password =response.body()!!.result.password
                        sessionManager.doctorName =response.body()!!.result.doctor_name
                        sessionManager.email =response.body()!!.result.email
                        sessionManager.phoneNumber =response.body()!!.result.phone_number
                        sessionManager.phoneWithCode =response.body()!!.result.phone_with_code
                        sessionManager.gender = response.body()!!.result.gender.toString()
                        sessionManager.id =response.body()!!.result.id
                        sessionManager.cID =response.body()!!.result.c_id
                        sessionManager.sStat =response.body()!!.result.c_stat
                        sessionManager.status =response.body()!!.result.status
                        sessionManager.experience =response.body()!!.result.experience
                        sessionManager.qualification =response.body()!!.result.qualification
                        sessionManager.uniqueCode =response.body()!!.result.unique_code
                        sessionManager.hospitalID =response.body()!!.result.hospital_id

                        Log.e("Alauddin","sessionManager.fcmToken-${sessionManager.fcmToken}")
                        Log.e("Alauddin","sessionManager.password-${sessionManager.password}")
                        Log.e("Alauddin","sessionManager.doctorName-${sessionManager.doctorName}")
                        Log.e("Alauddin","sessionManager.email-${sessionManager.email}")
                        Log.e("Alauddin","sessionManager.phoneNumber-${sessionManager.phoneNumber}")
                        Log.e("Alauddin","sessionManager.phoneWithCode-${sessionManager.phoneWithCode}")
                        Log.e("Alauddin","sessionManager.id-${sessionManager.id}")
                        Log.e("Alauddin","sessionManager.gender-${sessionManager.gender}")
                        Log.e("Alauddin","ssionManager.hospitalID-${sessionManager.hospitalID}")
                        Log.e("Alauddin","ssionManager.hospitalID-${sessionManager.hospitalID}")
                        Log.e("Alauddin","sessionManager.onlineStatus-${sessionManager.onlineStatus}")

                        val intent = Intent(applicationContext, MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                        finish()
                        startActivity(intent)

                    }else{
                        myToast(this@SignIn, response.body()!!.message)
                        progressDialog!!.dismiss()
                    }

                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    myToast(this@SignIn,"${t.message}")
                    progressDialog!!.dismiss()

                }

            })




        }
        binding.tvWelComeBack.setOnClickListener{
            startActivity(Intent(this,AddPrescription::class.java))
        }

    }
}