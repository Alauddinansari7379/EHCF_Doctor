package com.example.ehcf_doctor.MySlots

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import com.example.ehcf_doctor.R
import com.example.ehcf_doctor.databinding.ActivityMySlotsTimingsNewBinding
import com.giphy.sdk.analytics.GiphyPingbacks.context
import rezwan.pstu.cse12.youtubeonlinestatus.recievers.NetworkChangeReceiver
import xyz.teamgravity.checkinternet.CheckInternet

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