package com.example.ehcf_doctor.MyPatient.activity

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.ehcf.Helper.myToast
import com.example.ehcf.sharedpreferences.SessionManager
import com.example.ehcf_doctor.Appointments.Upcoming.adapter.AdapterPatientHistory
import com.example.ehcf_doctor.Helper.AppProgressBar
import com.example.ehcf_doctor.MyPatient.model.ModelPatientHistoryX
import com.example.ehcf_doctor.databinding.ActivityPatientHistoryBinding
import com.example.ehcf_doctor.Retrofit.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PatientHistory : AppCompatActivity() {
    private lateinit var binding: ActivityPatientHistoryBinding
    private var context: Context = this@PatientHistory
    var id = ""
    private var count = 0
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPatientHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sessionManager = SessionManager(this)

        binding.imgBack.setOnClickListener {
            onBackPressed()
        }
        id = intent.getStringExtra("Id").toString()
        apiCall()
    }

    private fun apiCall() {
        AppProgressBar.showLoaderDialog(context)

        ApiClient.apiService.patientHistory(id)
            .enqueue(object : Callback<ModelPatientHistoryX> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelPatientHistoryX>, response: Response<ModelPatientHistoryX>
                ) {

                    try {

                        if (response.code() == 500) {
                            myToast(this@PatientHistory, "Server Error")

                        } else if (response.body()!!.result.isEmpty()) {
                            myToast(this@PatientHistory, "No Appointment Found")
                            AppProgressBar.hideLoaderDialog()
                        } else {
                            count = 0
                            binding.rvPtientHistory.apply {
                                response.body()!!.result.size.toString()
                                Log.e("Size", response.body()!!.result.size.toString())
                                //  myToast(requireActivity(),"Adapter")
                                adapter =
                                    AdapterPatientHistory(this@PatientHistory, response.body()!!)
                                AppProgressBar.hideLoaderDialog()


                            }
                        }
                    } catch (e: Exception) {
                        AppProgressBar.hideLoaderDialog()
                        myToast(this@PatientHistory, "Something went wrong")
                        e.printStackTrace()
                    }
                }

                override fun onFailure(call: Call<ModelPatientHistoryX>, t: Throwable) {
                    count++
                    if (count <= 3) {
                        apiCall()
                    } else {
                        t.message?.let { myToast(this@PatientHistory, it) }
                    }
                }
            })
    }
}


