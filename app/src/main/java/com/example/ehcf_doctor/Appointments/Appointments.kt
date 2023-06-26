package com.example.ehcf_doctor.Appointments

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager.widget.ViewPager
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.ehcf.Helper.isOnline
import com.example.ehcf.Helper.myToast
import com.example.ehcf.sharedpreferences.SessionManager
import com.example.ehcf_doctor.Appointments.Cancelled.activity.CancelledFragment
import com.example.ehcf_doctor.Appointments.Consulted.activity.ConsultedFragment
import com.example.ehcf_doctor.Appointments.Upcoming.activity.UpComingFragment
import com.example.ehcf_doctor.Appointments.Upcoming.model.ModelConfirmSlotRes
import com.example.ehcf_doctor.Login.activity.SignIn
import com.example.ehcf_doctor.MainActivity.activity.MainActivity
import com.example.ehcf_doctor.databinding.ActivityApointmentsBinding
import com.example.myrecyview.apiclient.ApiClient
import com.giphy.sdk.analytics.GiphyPingbacks.context
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import rezwan.pstu.cse12.youtubeonlinestatus.recievers.NetworkChangeReceiver
import xyz.teamgravity.checkinternet.CheckInternet


class Appointments : AppCompatActivity() {
    private var context: Context = this
    private lateinit var binding: ActivityApointmentsBinding
    private lateinit var sessionManager: SessionManager
    private lateinit var pager: ViewPager // creating object of ViewPager
    private lateinit var tab: TabLayout  // creating object of TabLayout
    private lateinit var bar: Toolbar    // creating object of ToolBar
    private var vale = ""
    var bookingId = ""

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityApointmentsBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.imgBack.setOnClickListener {
            startActivity(Intent(this@Appointments, MainActivity::class.java))
        }
        sessionManager = SessionManager(this)
        pager = findViewById(com.example.ehcf_doctor.R.id.viewPager)
        tab = findViewById(com.example.ehcf_doctor.R.id.tabs)
        val adapter = ViewPagerAdapter(supportFragmentManager)

        vale = intent.getStringExtra("vale").toString()
        bookingId = intent.getStringExtra("bookingId").toString()

        if (vale=="1"){
            completeSlot(bookingId)
        }

        val refreshListener = SwipeRefreshLayout.OnRefreshListener {
            overridePendingTransition(0, 0)
            finish()
            startActivity(intent)
            overridePendingTransition(0, 0)
        }

        binding.swipeRefreshLayout.setOnRefreshListener(refreshListener)


        val tabs = findViewById<View>(com.example.ehcf_doctor.R.id.tabs) as TabLayout

        tab.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                pager.currentItem = tab.position
                when (tab.position) {
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

        binding.imgLogout.setOnClickListener {
            SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Are you sure want to logout?")
                .setCancelText("No")
                .setConfirmText("Yes")
                .showCancelButton(true)
                .setConfirmClickListener { sDialog ->
                    sDialog.cancel()
                    sessionManager.logout()
                    val intent = Intent(applicationContext, SignIn::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                    finish()
                    startActivity(intent)
                }
                .setCancelClickListener { sDialog ->
                    sDialog.cancel()
                }
                .show()
        }
    }

    fun refresh() {
        overridePendingTransition(0, 0)
        finish()
        startActivity(intent)
        overridePendingTransition(0, 0)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this@Appointments, MainActivity::class.java))

    }
    private fun completeSlot(bookingId: String) {
        val slug = "completed"
        ApiClient.apiService.confirmSlot(bookingId, slug)
            .enqueue(object : Callback<ModelConfirmSlotRes> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelConfirmSlotRes>,
                    response: Response<ModelConfirmSlotRes>
                ) {
                    if (response.code() == 500) {
                       // myToast(this@Appointments,response.body()!!.message)

                    } else if (response.body()!!.status == 1) {
                        vale=""
                        val intent = Intent(context as Activity, Appointments::class.java)
                        (context as Activity).startActivity(intent)
                     //   refresh()
                        myToast(this@Appointments,response.body()!!.message)

                        //  myToast(requireActivity(),response.body()!!.message)
                        //  apiCall()

                    } else {
                        myToast(this@Appointments,response.body()!!.message)

                    }
                }

                override fun onFailure(call: Call<ModelConfirmSlotRes>, t: Throwable) {
                    myToast(this@Appointments,t.message.toString())


                }

            })
    }

    override fun onStart() {
        super.onStart()
        if (isOnline(this)) {
            //  myToast(requireActivity(), "Connected")
        } else {
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