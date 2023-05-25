package com.example.ehcf_doctor.Registration.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ehcf_doctor.databinding.ActivityRegistrationBinding

class Registration : AppCompatActivity() {
    private lateinit var binding:ActivityRegistrationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnNext.setOnClickListener{
            startActivity(Intent(this, ClinicInfo::class.java))
        }

        binding.imgBack.setOnClickListener{
            onBackPressed()
        }


//        binding.btnNext.setOnClickListener {
// 
//            val doctor_name= "Alauddin Ansari"
//            val qualification= "MBBS"
//            val additional_qualification= "MCHJ"
//            val phone_number= "7379452213"
//            val phone_with_code= "917379452213"
//            val fcm_token= "sasdasdasdfsdsads"
//            val password= "12345678"
//            val email= "alauddinansari73713@gmail.com"
//
//            ApiClient.apiService.register(doctor_name,qualification,additional_qualification,phone_number,phone_with_code
//            ,fcm_token,password,email).enqueue(object :Callback<RegistationResponse>{
//                override fun onResponse(
//                    call: Call<RegistationResponse>,
//                    response: Response<RegistationResponse>
//                ) {
//                    if (response.body()!!.status == 1) {
//                        myToast(this@Registration, response.body()!!.message)
//
//                    }
//                    else {
//                        myToast(this@Registration, "${response.body()!!.message}")
//                    }
//                }
//
//                override fun onFailure(call: Call<RegistationResponse>, t: Throwable) {
//                    myToast(this@Registration,t.message.toString())
//                }
//
//            })
//
//        }
    }
}