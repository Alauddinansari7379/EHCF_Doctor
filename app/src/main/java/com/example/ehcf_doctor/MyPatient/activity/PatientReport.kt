package com.example.ehcf_doctor.MyPatient.activity

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.ehcf.Helper.myToast
import com.example.ehcf.Upload.model.ModelGetAllReport
import com.example.ehcf.sharedpreferences.SessionManager
import com.example.ehcf_doctor.Helper.AppProgressBar
import com.example.ehcf_doctor.MyPatient.adapter.AdapterViewReport
import com.example.ehcf_doctor.databinding.ActivityPatientReportBinding
import com.example.ehcf_doctor.Retrofit.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PatientReport : AppCompatActivity() {
    private lateinit var binding: ActivityPatientReportBinding
    private var context: Context = this@PatientReport
    var id = ""
    private var count = 0
    private lateinit var sessionManager: SessionManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPatientReportBinding.inflate(layoutInflater)
        setContentView(binding.root)
        id = intent.getStringExtra("Id").toString()
        apiCallGetAllReport()
        // apiCallDoctorProfile()
        binding.imgBack.setOnClickListener {
            onBackPressed()
        }


    }

    private fun apiCallGetAllReport() {
        AppProgressBar.showLoaderDialog(context)

        ApiClient.apiService.getReport(id)
            .enqueue(object : Callback<ModelGetAllReport> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelGetAllReport>, response: Response<ModelGetAllReport>
                ) {
                    try {
                        if (response.body()!!.result.isEmpty()) {
                            myToast(this@PatientReport, "No Report Found")
                            AppProgressBar.hideLoaderDialog()
                        } else {
                            count = 0
                            binding.recyclerView.apply {
                                binding.tvNoDataFound.visibility = View.GONE
                                adapter = AdapterViewReport(response.body()!!, this@PatientReport)
                            }
                            AppProgressBar.hideLoaderDialog()
                        }


                    } catch (e: Exception) {
                        e.printStackTrace()
                        myToast(this@PatientReport, "Something went wrong")
                        AppProgressBar.hideLoaderDialog()
                    }
                }

                override fun onFailure(call: Call<ModelGetAllReport>, t: Throwable) {
                    count++
                    if (count <= 3) {
                        apiCallGetAllReport()
                    } else {
                        myToast(this@PatientReport, "Something went wrong")
                    }
                    AppProgressBar.hideLoaderDialog()

                }

            })
    }


}