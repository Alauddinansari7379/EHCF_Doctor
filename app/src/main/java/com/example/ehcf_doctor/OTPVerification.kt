package com.example.ehcf_doctor

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.ContentValues
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.core.widget.addTextChangedListener
import com.example.ehcf.Helper.myToast
import com.example.ehcf.PhoneNumber.ModelReponse.ForgotPasswordResponse
import com.example.ehcf_doctor.Registration.activity.Registration
import com.example.ehcf_doctor.databinding.ActivityOtpverificationBinding
import com.example.myrecyview.apiclient.ApiClient
import com.giphy.sdk.analytics.GiphyPingbacks.context
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import rezwan.pstu.cse12.youtubeonlinestatus.recievers.NetworkChangeReceiver
import xyz.teamgravity.checkinternet.CheckInternet

class OTPVerification : AppCompatActivity() {
    private lateinit var binding:ActivityOtpverificationBinding
    var progressDialog: ProgressDialog? =null
    var phoneNumber=""
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityOtpverificationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        timeCounter()

        progressDialog = ProgressDialog(this@OTPVerification)
        progressDialog!!.setMessage("Loading..")
        progressDialog!!.setTitle("Please Wait")
        progressDialog!!.isIndeterminate = false
        progressDialog!!.setCancelable(true)

        val intent = intent
        phoneNumber = intent.getStringExtra("Mobilenumber").toString()
        binding.tvMobileNumber.text = "+91$phoneNumber";

        Log.e(ContentValues.TAG, "Mobilenumber-----:---$phoneNumber")

        binding.btnVerify.setOnClickListener {
            startActivity(Intent(this, Registration::class.java))

        }
        binding.imgBack.setOnClickListener {
            onBackPressed()
        }

        binding.otpBox1.addTextChangedListener {
            if (binding.otpBox1.length() == 1) {
                binding.otpBox2.requestFocus()
            }
        }
        binding.otpBox2.addTextChangedListener {
            if (binding.otpBox2.length() == 1)
                binding.otpBox3.requestFocus()
        }
        binding.otpBox3.addTextChangedListener {
            if (binding.otpBox3.length() == 1)
                binding.otpBox4.requestFocus()
        }
        binding.tvResend.setOnClickListener {
            binding.tvResend.setTextColor(Color.parseColor("#858284"))
            progressDialog!!.show()

            ApiClient.apiService.forgotPassword(phoneNumber).enqueue(object :
                Callback<ForgotPasswordResponse> {

                override fun onResponse(
                    call: Call<ForgotPasswordResponse>, response: Response<ForgotPasswordResponse>
                ) {

                    // Log.e("Ala","${response.body()!!.result}")
                    Log.e("Ala","${response.body()!!.message}")
                    Log.e("Ala","${response.body()!!.status}")
                    if (response.body()!!.status==1){
                        myToast(this@OTPVerification,response.body()!!.message)
                        progressDialog!!.dismiss()
                        timeCounter()
                    }
                    else{
                        myToast(this@OTPVerification,"${response.body()!!.message}")
                        progressDialog!!.dismiss()

                    }

                }

                override fun onFailure(call: Call<ForgotPasswordResponse>, t: Throwable) {
                    myToast(this@OTPVerification,"Something went wrong")
                    progressDialog!!.dismiss()

                }

            })
        }


    }


    private fun timeCounter() {
        object : CountDownTimer(30000, 1000) {

            // Callback function, fired on regular interval
            @SuppressLint("SetTextI18n")
            override fun onTick(millisUntilFinished: Long) {
                binding.tvResend.isClickable = false
                binding.tvSecond.text =
                    "OTP Resend In : " + millisUntilFinished / 1000 + " " + " Seconds"
            }

            override fun onFinish() {
                binding.tvResend.isClickable = true
                binding.tvResend.setTextColor(Color.parseColor("#9F367A"))

            }
        }.start()
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