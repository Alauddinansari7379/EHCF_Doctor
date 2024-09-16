package com.example.ehcf_doctor

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.ehcf.Helper.myToast
import com.example.ehcf.sharedpreferences.SessionManager
import com.example.ehcf_doctor.Helper.AppProgressBar
import com.example.ehcf_doctor.Login.activity.SignIn
import com.example.ehcf_doctor.Profile.modelResponse.ResetPassResponse
import com.example.ehcf_doctor.databinding.ActivityResetPasswordBinding
import com.example.ehcf_doctor.Retrofit.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ResetPassword : AppCompatActivity() {
    private lateinit var binding: ActivityResetPasswordBinding
    private lateinit var sessionManager: SessionManager
    var id = ""
    private val context: Context = this@ResetPassword
    private var count = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        id = intent.getStringExtra("id").toString()
        binding.imgBack.setOnClickListener {
            onBackPressed()
        }

        binding.btnChangePass.setOnClickListener {
            if (binding.edtNewPass.text!!.isEmpty()) {
                binding.edtNewPass.error = "Enter New Password"
                binding.edtNewPass.requestFocus()
                return@setOnClickListener
            }
            if (binding.edtConfirmPassword.text!!.isEmpty()) {
                binding.edtConfirmPassword.error = "Enter Confirm Password"
                binding.edtConfirmPassword.requestFocus()
                return@setOnClickListener
            }
            val password = binding.edtNewPass.text.toString().trim()
            val confirmPassword = binding.edtConfirmPassword.text.toString().trim()

            if (password != confirmPassword) {
                binding.edtNewPass.error = "Password Miss Match"
                binding.edtConfirmPassword.requestFocus()
                return@setOnClickListener
            } else {
                apiCallChangePass(confirmPassword)
            }
        }

    }

    private fun apiCallChangePass(confirmPassword: String) {
       AppProgressBar.showLoaderDialog(context)
        ApiClient.apiService.resetPassword(id, confirmPassword)
            .enqueue(object :
                Callback<ResetPassResponse> {
                override fun onResponse(
                    call: Call<ResetPassResponse>,
                    response: Response<ResetPassResponse>
                ) {
                    if (response.body()!!.status == 1) {
                        count = 0
                        AppProgressBar.hideLoaderDialog()
                        alretDilogChanged()
                        //myToast(requireActivity(), response.body()!!.message)
                    } else {
                        myToast(this@ResetPassword, response.body()!!.message)
                        AppProgressBar.hideLoaderDialog()

                    }
                }

                override fun onFailure(call: Call<ResetPassResponse>, t: Throwable) {
                    count++
                    if (count <= 3) {
                        apiCallChangePass(confirmPassword)
                    } else {
                        myToast(this@ResetPassword, "Something went wrong")
                    }
                    AppProgressBar.hideLoaderDialog()

                }

            })


    }

    private fun alretDilogChanged() {
        SweetAlertDialog(this@ResetPassword, SweetAlertDialog.SUCCESS_TYPE)
            .setTitleText("Your Password has been changed!")
            .setConfirmText("Ok")
            .showCancelButton(true)
            .setConfirmClickListener { sDialog ->
                sDialog.cancel()
                val intent = Intent(applicationContext, SignIn::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                finish()
                startActivity(intent)

            }
            .setCancelClickListener { sDialog ->
                sDialog.cancel()
            }
            .show()
    }


}