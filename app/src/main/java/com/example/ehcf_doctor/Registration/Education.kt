package com.example.ehcf_doctor.Registration

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ehcf_doctor.R
import com.example.ehcf_doctor.databinding.ActivityEducationBinding

class Education : AppCompatActivity() {
    private lateinit var binding: ActivityEducationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEducationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnNext.setOnClickListener {
            startActivity(Intent(this, TermsAndConditions::class.java))
        }

        binding.imgBack.setOnClickListener {
            onBackPressed()
        }
    }
}