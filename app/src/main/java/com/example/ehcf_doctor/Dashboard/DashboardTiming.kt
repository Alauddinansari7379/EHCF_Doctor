package com.example.ehcf_doctor.Dashboard

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ehcf_doctor.R
import com.example.ehcf_doctor.databinding.ActivityDashboardTimingBinding

class DashboardTiming : AppCompatActivity() {
    private lateinit var binding:ActivityDashboardTimingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityDashboardTimingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.imgBack.setOnClickListener {
            onBackPressed()
        }
    }
}