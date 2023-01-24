package com.example.ehcf_doctor.ManageSlots

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import com.example.ehcf_doctor.R
import com.example.ehcf_doctor.databinding.ActivityManageSlotsSeassionBinding

class ManageSlotsSeassion : AppCompatActivity() {
    var relationList = ArrayList<String>()

    private lateinit var binding:ActivityManageSlotsSeassionBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityManageSlotsSeassionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.imgBack.setOnClickListener {
            onBackPressed()
        }

        relationList.add("Sunday")
        relationList.add("Monday")
        relationList.add("Tuesday")
        relationList.add("Wednesday")
        relationList.add("Thursday")
        relationList.add("Friday")
        relationList.add("Saturday")
        binding.spinnerDays.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, relationList)
    }
    }
