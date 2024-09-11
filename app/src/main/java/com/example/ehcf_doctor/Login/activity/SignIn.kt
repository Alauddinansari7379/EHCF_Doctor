package com.example.ehcf_doctor.Login.activity

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.ContentValues
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
import com.example.ehcf_doctor.R
import com.example.ehcf_doctor.Registration.activity.RegirstrationTest
import com.example.ehcf_doctor.databinding.ActivitySignInBinding
import com.example.ehcf_doctor.Retrofit.ApiClient
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import rezwan.pstu.cse12.youtubeonlinestatus.recievers.NetworkChangeReceiver
import xyz.teamgravity.checkinternet.CheckInternet

class SignIn : AppCompatActivity() {
    private val context: Context =this@SignIn
    var progressDialog : ProgressDialog?=null
    private lateinit var sessionManager: SessionManager
    private lateinit var binding:ActivitySignInBinding
    var countryCodeNew="91"
    var fcmToken=""
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
        getToken()

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


            progressDialog!!.show()

            ApiClient.apiService.login(phoneNumberNew,password,fcmToken).enqueue(object :
                Callback<LoginResponse> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {

                    if (response.code()==500){
                        myToast(this@SignIn,"Server Error")
                    }
                    else if (response.body()!!.status==1){
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
                        sessionManager.specialist =response.body()!!.result.specialist
                        sessionManager.pricing =response.body()!!.result.pricing
                        sessionManager.wallet =response.body()!!.result.wallet

                        sessionManager.clinicAddress = response.body()!!.result.clinic_address
                        sessionManager.clinicAddressOne = response.body()!!.result.clinic_address_one
                        sessionManager.clinicAddressTwo = response.body()!!.result.clinic_address_two
                        sessionManager.clinicAddress = response.body()!!.result.clinic_address
                        sessionManager.pricing = response.body()!!.result.pricing
                        sessionManager.experience =response.body()!!.result.experience
                        sessionManager.clinicName =response.body()!!.result.clinic_name
                        sessionManager.address =response.body()!!.result.address
                        sessionManager.services =response.body()!!.result.services
                        sessionManager.college =response.body()!!.result.college
                        sessionManager.hospitalName =response.body()!!.result.hos_name
                        sessionManager.hospitalAddress =response.body()!!.result.hos_address
                        sessionManager.registration =response.body()!!.result.reg_no
                        sessionManager.openTime =response.body()!!.result.opening_time
                        sessionManager.closeTime =response.body()!!.result.closing_time
                        sessionManager.postalCode =response.body()!!.result.postal_code.toString()

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
                        Log.e("Alauddin","sessionManager.registration-${sessionManager.registration}")
                        Log.e("Alauddin","sessionManager.specialist-${sessionManager.specialist}")

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
                    myToast(this@SignIn, "Something went wrong")
                    progressDialog!!.dismiss()

                }

            })




        }
//        binding.tvWelComeBack.setOnClickListener{
//            startActivity(Intent(this, AddPrescription::class.java))
//        }

    }
    @SuppressLint("StringFormatInvalid")
    private fun getToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(ContentValues.TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            fcmToken = task.result

            // Log and toast
            val msg = getString(R.string.channel_id, fcmToken)
            Log.e("Token", fcmToken)
            // Toast.makeText(requireContext(), token, Toast.LENGTH_SHORT).show()
        })
    }
    override fun onStart() {
        super.onStart()
        CheckInternet().check { connected ->
            if (connected) {

                // myToast(requireActivity(),"Connected")
            }
            else {
                val changeReceiver = NetworkChangeReceiver(context)
                changeReceiver.build()
                //  myToast(requireActivity(),"Check Internet")
            }
        }
    }

}