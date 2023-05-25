package com.example.ehcf_doctor.Registration.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ehcf_doctor.Profile.activity.Education
import com.example.ehcf_doctor.databinding.ActivityClinicInfoBinding

class ClinicInfo : AppCompatActivity() {
    private lateinit var binding:ActivityClinicInfoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityClinicInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnNext.setOnClickListener{
           startActivity(Intent(this, Education::class.java))
        }
        binding.imgBack.setOnClickListener{
            onBackPressed()
        }
    }
}