package com.example.ehcf_doctor.Prescription.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toolbar
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import com.example.ehcf.Helper.isOnline
import com.example.ehcf_doctor.Appointments.ViewPagerAdapter
import com.example.ehcf_doctor.R
import com.example.ehcf_doctor.databinding.ActivityMain2Binding
import com.giphy.sdk.analytics.GiphyPingbacks.context
import com.google.android.material.tabs.TabLayout
import rezwan.pstu.cse12.youtubeonlinestatus.recievers.NetworkChangeReceiver

class PrescriptionMainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMain2Binding
    private lateinit var pager: ViewPager // creating object of ViewPager
    private lateinit var tab: TabLayout  // creating object of TabLayout
    private lateinit var bar: Toolbar    // creating object of ToolBar

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)



        binding.imgBack.setOnClickListener {
            onBackPressed()
        }


//            val callIntent = Intent(Intent.ACTION_CALL)
//            callIntent.data = Uri.parse("tel:123456789")
//
//            if (ContextCompat.checkSelfPermission(
//                    this,
//                    Manifest.permission.CALL_PHONE
//                ) == PackageManager.PERMISSION_GRANTED
//            ) {
//                startActivity(callIntent)
//            } else {
//                requestPermissions(arrayOf(Manifest.permission.CALL_PHONE), 1)
//            }
//        }

//        if (binding.tabs.isFocusable){
//            binding.tabs.backgroundTintList=RED.toInt().toString()
//        }

        pager = findViewById(R.id.viewPager)
        tab = findViewById(R.id.tabs)
        val adapter = ViewPagerAdapter(supportFragmentManager)

        adapter.addFragment(PrescriptionPendingFragment(), "Prescription Pending")
        adapter.addFragment(PrescribedFragment(), "Prescribed")

        pager.adapter = adapter


        tab.setupWithViewPager(pager)
    }
    override fun onStart() {
        super.onStart()
        if (isOnline(this)){
            //  myToast(requireActivity(), "Connected")
        }else{
            val changeReceiver = NetworkChangeReceiver(context)
            changeReceiver.build()
            //  myToast(requireActivity(), "Not C")

        }
//        CheckInternet().check { connected ->
//            if (connected) {
//             //    myToast(requireActivity(),"Connected")
//            }
//            else {
//                val changeReceiver = NetworkChangeReceiver(context)
//                changeReceiver.build()
//                //  myToast(requireActivity(),"Check Internet")
//            }
//        }
    }

}