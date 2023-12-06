package com.example.ehcf_doctor.ForgotPassword

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.ehcf.Helper.myToast
import com.example.ehcf.sharedpreferences.SessionManager
import com.example.ehcf_doctor.ForgotPassword.modelReponse.ModelForgotPass
import com.example.ehcf_doctor.Login.activity.SignIn
import com.example.ehcf_doctor.OTPVerification
import com.example.ehcf_doctor.databinding.ActivityMobileNumberBinding
import com.example.myrecyview.apiclient.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MobileNumber : AppCompatActivity() {
    private lateinit var binding: ActivityMobileNumberBinding
    private val context: Context = this@MobileNumber
    var progressDialog: ProgressDialog? = null
    var phoneNumber = ""
    private lateinit var sessionManager: SessionManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMobileNumberBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvSignIn.setOnClickListener {
            startActivity(Intent(this, SignIn::class.java))
        }



        binding.tvSignIn.setOnClickListener {
            startActivity(Intent(context, SignIn::class.java))
        }

        binding.btnProcess.setOnClickListener {
            if (binding.edtPhone.text.isEmpty()) {
                binding.edtPhone.error = "Enter Phone Number"
                binding.edtPhone.requestFocus()
                return@setOnClickListener
            }
            apiCallForgotPassword()


        }
    }

    private fun apiCallForgotPassword() {
        val   phoneNumberNew = binding.edtPhone.text.toString()
        val code="91"
        phoneNumber = code+phoneNumberNew
        progressDialog = ProgressDialog(this@MobileNumber)
        progressDialog!!.setMessage("Loading..")
        progressDialog!!.setTitle("Please Wait")
        progressDialog!!.isIndeterminate = false
        progressDialog!!.setCancelable(true)
        progressDialog!!.show()

        ApiClient.apiService.forgotPassword(phoneNumber).enqueue(object :
            Callback<ModelForgotPass> {

            @SuppressLint("LogNotTimber")
            override fun onResponse(
                call: Call<ModelForgotPass>, response: Response<ModelForgotPass>
            ) {
                if (response.body()!!.status == 1) {

                    myToast(this@MobileNumber, response.body()!!.message)
                    progressDialog!!.dismiss()
                    val otp = response.body()!!.result.otp
                    val id = response.body()!!.result.id

                    val intent = Intent(context as Activity, OTPVerification::class.java)
                    intent.putExtra("Mobilenumber", phoneNumber)
                    intent.putExtra("id", id.toString())
                    intent.putExtra("otp", otp.toString())
                    Log.e("otp", response.body()!!.result.otp.toString())
                    Log.e("id", response.body()!!.result.id.toString())
                    context.startActivity(intent)
                } else {
                    myToast(this@MobileNumber, "Please enter valid phone number")
                    progressDialog!!.dismiss()
                }


            }
            override fun onFailure(call: Call<ModelForgotPass>, t: Throwable) {
                myToast(this@MobileNumber,t.message.toString())
               // myToast(this@MobileNumber, "Something went wrong")
                progressDialog!!.dismiss()

            }

        })
    }
}

