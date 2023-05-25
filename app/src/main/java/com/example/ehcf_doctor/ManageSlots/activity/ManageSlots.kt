package com.example.ehcf_doctor.ManageSlots.activity

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.postDelayed
import com.example.ehcf.Helper.myToast
import com.example.ehcf.sharedpreferences.SessionManager
import com.example.ehcf_doctor.ManageSlots.adapter.AdapterManageSlot
import com.example.ehcf_doctor.ManageSlots.adapter.AdapterSlotsList
import com.example.ehcf_doctor.ManageSlots.model.ModelSlotList
import com.example.ehcf_doctor.databinding.ActivityManageSlotsBinding
import com.example.myrecyview.apiclient.ApiClient
import okhttp3.internal.notify
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import rezwan.pstu.cse12.youtubeonlinestatus.recievers.NetworkChangeReceiver
import xyz.teamgravity.checkinternet.CheckInternet

class ManageSlots : AppCompatActivity() {
    private val context: Context = this@ManageSlots
    var progressDialog: ProgressDialog? = null
    private lateinit var sessionManager: SessionManager

    private lateinit var binding:ActivityManageSlotsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityManageSlotsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sessionManager = SessionManager(this)

        binding.btnCreateSlot.setOnClickListener {
           // startActivity(Intent(this@ManageSlots,CreateSlot::class.java))
            startActivity(Intent(this@ManageSlots,CreateSlot::class.java))
        }
        binding.imgBack.setOnClickListener {
            onBackPressed()
        }
        Handler(Looper.getMainLooper()).postDelayed(300) {
            apiCall()
        }



    }
    private fun apiCall() {
        progressDialog = ProgressDialog(this@ManageSlots)
        progressDialog!!.setMessage("Loading..")
        progressDialog!!.setTitle("Please Wait")
        progressDialog!!.isIndeterminate = false
        progressDialog!!.setCancelable(true)
        progressDialog!!.show()


        ApiClient.apiService.getTimeSlot(sessionManager.id.toString(),"2").enqueue(object :
            Callback<ModelSlotList>
        {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call: Call<ModelSlotList>,
                response: Response<ModelSlotList>
            ) {

                if (response.body()!!.result.isEmpty()) {
                    binding.tvNoDataFound.visibility = View.VISIBLE
                    myToast(this@ManageSlots,"No Slot Found")
                    progressDialog!!.dismiss()

                } else {
                    binding.rvSlotTiming.apply {
                        binding.tvNoDataFound.visibility = View.GONE
                        adapter = AdapterManageSlot(this@ManageSlots, response.body()!!)
                        progressDialog!!.dismiss()

                    }
                }
            }


            override fun onFailure(call: Call<ModelSlotList>, t: Throwable) {
                myToast(this@ManageSlots, "Something went wrong")

            }


        })
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