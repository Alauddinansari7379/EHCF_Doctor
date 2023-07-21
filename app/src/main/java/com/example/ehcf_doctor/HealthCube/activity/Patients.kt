package com.example.ehcf_doctor.HealthCube.activity

import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.example.ehcf_doctor.R
import com.example.ehcf_doctor.databinding.ActivityPatientsBinding

class Patients : AppCompatActivity() {
    private lateinit var binding: ActivityPatientsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPatientsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.cardSearch.setOnClickListener {
            binding.cardSearch.backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(
                    this@Patients,
                    R.color.purple_500
                )
            )
            binding.tvSearch.setTextColor(Color.parseColor("#FFFFFFFF"))

            binding.cardByDate.backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(
                    this@Patients,
                    R.color.white
                )
            )
            binding.tvByDate.setTextColor(Color.parseColor("#FF000000"))

            binding.cardScan.backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(
                    this@Patients,
                    R.color.white
                )
            )
            binding.tvSacn.setTextColor(Color.parseColor("#FF000000"))


        }

        binding.cardByDate.setOnClickListener {
            binding.cardByDate.backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(
                    this@Patients,
                    R.color.purple_500
                )
            )
            binding.tvByDate.setTextColor(Color.parseColor("#FFFFFFFF"))

            binding.cardSearch.backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(
                    this@Patients,
                    R.color.white
                )
            )
            binding.tvSearch.setTextColor(Color.parseColor("#FF000000"))



            binding.cardScan.backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(
                    this@Patients,
                    R.color.white
                )
            )
            binding.tvSacn.setTextColor(Color.parseColor("#FF000000"))


        }

        binding.cardScan.setOnClickListener {
            binding.cardScan.backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(
                    this@Patients,
                    R.color.purple_500
                )
            )
            binding.tvSacn.setTextColor(Color.parseColor("#FFFFFFFF"))

            binding.cardSearch.backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(
                    this@Patients,
                    R.color.white
                )
            )
            binding.tvSearch.setTextColor(Color.parseColor("#FF000000"))

            binding.cardByDate.backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(
                    this@Patients,
                    R.color.white
                )
            )
            binding.tvByDate.setTextColor(Color.parseColor("#FF000000"))


        }
        binding.imgBack.setOnClickListener {
            onBackPressed()
        }
    }


}