package com.example.ehcf_doctor

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.core.widget.addTextChangedListener
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.ehcf.Helper.myToast
import com.example.ehcf_doctor.ForgotPassword.modelReponse.ModelForgotPass
import com.example.ehcf_doctor.Helper.AppProgressBar
import com.example.ehcf_doctor.databinding.ActivityOtpverificationBinding
import com.example.ehcf_doctor.Retrofit.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import rezwan.pstu.cse12.youtubeonlinestatus.recievers.NetworkChangeReceiver
import xyz.teamgravity.checkinternet.CheckInternet

class OTPVerification : AppCompatActivity() {
    private lateinit var binding:ActivityOtpverificationBinding
    private val context: Context = this@OTPVerification
    var phoneNumber=""
    var id=""
    private var otp=""
    var finalOTP=""
    private var count = 0
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityOtpverificationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        timeCounter()

       AppProgressBar.showLoaderDialog(context)

        val intent = intent
        phoneNumber = intent.getStringExtra("Mobilenumber").toString()
        id = intent.getStringExtra("id").toString()
        otp = intent.getStringExtra("otp").toString()
        binding.tvMobileNumber.text = "+$phoneNumber";

        Log.e(ContentValues.TAG, "Mobilenumber-----:---$phoneNumber")

        Log.e("otp", otp)
        Log.e("id", id)
       //  otpPopup(otp)



        binding.btnVerify.setOnClickListener {
             val otp1=binding.otpBox1.text.toString()
             val otp2=binding.otpBox2.text.toString()
             val otp3=binding.otpBox3.text.toString()
             val otp4=binding.otpBox4.text.toString()
            Log.e("otp1", otp1)

             finalOTP="$otp1$otp2$otp3$otp4"

            Log.e("finalOTP", finalOTP.toString())
            Log.e("otp", otp)


            if (finalOTP==otp){
                val intent = Intent(context as Activity, ResetPassword::class.java)
                intent.putExtra("Mobilenumber", phoneNumber)
                intent.putExtra("id", id)
                context.startActivity(intent)
            }else{
                myToast(this@OTPVerification,"You are entered wrong OTP")
            }

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
            AppProgressBar.showLoaderDialog(context)
            apiCallForgotPassword()
        }


    }

    private fun apiCallForgotPassword() {
        ApiClient.apiService.forgotPassword(phoneNumber).enqueue(object :
            Callback<ModelForgotPass> {

            override fun onResponse(
                call: Call<ModelForgotPass>, response: Response<ModelForgotPass>
            ) {

                // Log.e("Ala","${response.body()!!.result}")
                Log.e("Ala","${response.body()!!.message}")
                Log.e("Ala","${response.body()!!.status}")
                if (response.body()!!.status==1){
                    count = 0
                    myToast(this@OTPVerification,response.body()!!.message)

                    val otp = response.body()!!.result.otp.toString()
                    val id = response.body()!!.result.id
                    this@OTPVerification.otp =otp
                    otpPopup(otp)
                    AppProgressBar.hideLoaderDialog()
                    timeCounter()
                }
                else{
                    myToast(this@OTPVerification,"${response.body()!!.message}")
                    AppProgressBar.hideLoaderDialog()

                }

            }

            override fun onFailure(call: Call<ModelForgotPass>, t: Throwable) {
                count++
                if (count <= 3) {
                    apiCallForgotPassword()
                } else {
                    myToast(this@OTPVerification,"Something went wrong")
                }
                AppProgressBar.hideLoaderDialog()

            }

        })
    }

    private fun otpPopup(otp: String) {
        SweetAlertDialog(this@OTPVerification, SweetAlertDialog.SUCCESS_TYPE)
            .setTitleText("Your OTP is $otp")
            .setConfirmText("Ok")
            .showCancelButton(true)
            .setConfirmClickListener { sDialog ->
                sDialog.cancel()
            }
            .setCancelClickListener { sDialog ->
                sDialog.cancel()
            }
            .show()
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