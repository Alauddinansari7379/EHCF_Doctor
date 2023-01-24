package com.example.ehcf_doctor.MySlots

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import com.example.ehcf_doctor.R
import com.example.ehcf_doctor.databinding.ActivityMySlotsTimingsNewBinding

class MySlotsTimingsNew : AppCompatActivity() {

    private lateinit var binding:ActivityMySlotsTimingsNewBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMySlotsTimingsNewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.imgBack.setOnClickListener {
            onBackPressed()
        }

    }
}