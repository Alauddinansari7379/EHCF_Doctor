package com.example.ehcf_doctor.Appointments

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.example.ehcf_doctor.Appointments.Cancelled.activity.CancelledFragment
import com.example.ehcf_doctor.Appointments.Consulted.activity.ConsultedFragment
import com.example.ehcf_doctor.Appointments.Upcoming.activity.UpComingFragment
import com.example.ehcf_doctor.databinding.ActivityApointmentsBinding
import com.giphy.sdk.analytics.GiphyPingbacks.context
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import rezwan.pstu.cse12.youtubeonlinestatus.recievers.NetworkChangeReceiver
import xyz.teamgravity.checkinternet.CheckInternet


class Appointments : AppCompatActivity() {
    private lateinit var binding:ActivityApointmentsBinding
    private lateinit var pager: ViewPager // creating object of ViewPager
    private lateinit var tab: TabLayout  // creating object of TabLayout
    private lateinit var bar: Toolbar    // creating object of ToolBar

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityApointmentsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.imgBack.setOnClickListener {
            onBackPressed()
        }
        pager = findViewById(com.example.ehcf_doctor.R.id.viewPager)
        tab = findViewById(com.example.ehcf_doctor.R.id.tabs)
        val adapter = ViewPagerAdapter(supportFragmentManager)

        val tabs = findViewById<View>(com.example.ehcf_doctor.R.id.tabs) as TabLayout

        tab.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                pager.currentItem = tab.position
              when(tab.position) {
                    0 -> tabs.setSelectedTabIndicatorColor(Color.parseColor("#45369F"))
                    1 -> tabs.setSelectedTabIndicatorColor(Color.parseColor("#3A97C5"))
                    2 -> tabs.setSelectedTabIndicatorColor(Color.parseColor("#FF0413"))
                }

//                if (tab.position == 0) {
//                    tabs.setSelectedTabIndicatorColor(Color.parseColor("#45369F"))
//                }
//                else if (tab.position == 1) {
//                    tabs.setSelectedTabIndicatorColor(Color.parseColor("#3A97C5"))
//                }
//                else {
//                    tabs.setSelectedTabIndicatorColor(Color.parseColor("#FF0413"))
//                }
           }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        adapter.addFragment(UpComingFragment(), "Upcoming")
        adapter.addFragment(ConsultedFragment(), "Consulted")
        adapter.addFragment(CancelledFragment(), "Cancelled")

        pager.adapter = adapter


        tab.setupWithViewPager(pager)
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