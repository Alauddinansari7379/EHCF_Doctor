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
import com.example.ehcf_doctor.MyPatient.model.ModelAllReports
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

//    private fun apiCallGetAllReport() {
//        AppProgressBar.showLoaderDialog(context)
//
//        ApiClient.apiService.getReport(id)
//            .enqueue(object : Callback<ModelGetAllReport> {
//                @SuppressLint("LogNotTimber")
//                override fun onResponse(
//                    call: Call<ModelGetAllReport>, response: Response<ModelGetAllReport>
//                ) {
//                    try {
//                        if (response.body()!!.result.isEmpty()) {
//                            myToast(this@PatientReport, "No Report Found")
//                            AppProgressBar.hideLoaderDialog()
//                        } else {
//                            count = 0
//                            binding.recyclerView.apply {
//                                binding.tvNoDataFound.visibility = View.GONE
//                                adapter = AdapterViewReport(response.body()!!, this@PatientReport)
//                            }
//                            AppProgressBar.hideLoaderDialog()
//                        }
//
//
//                    } catch (e: Exception) {
//                        e.printStackTrace()
//                        myToast(this@PatientReport, "Something went wrong")
//                        AppProgressBar.hideLoaderDialog()
//                    }
//                }
//
//                override fun onFailure(call: Call<ModelGetAllReport>, t: Throwable) {
//                    count++
//                    if (count <= 3) {
//                        apiCallGetAllReport()
//                    } else {
//                        myToast(this@PatientReport, "Something went wrong")
//                    }
//                    AppProgressBar.hideLoaderDialog()
//
//                }
//
//            })
//    }

    private fun apiCallGetAllReport() {
        AppProgressBar.showLoaderDialog(context)

        ApiClient.apiService.getReport(id).enqueue(object : Callback<ModelAllReports> {
            @SuppressLint("LogNotTimber")
            override fun onResponse(call: Call<ModelAllReports>, response: Response<ModelAllReports>) {
                AppProgressBar.hideLoaderDialog()

                try {
                    if (response.code() == 500) {
                        myToast(this@PatientReport, "Server Error")
                        AppProgressBar.hideLoaderDialog()
                    } else if (response.code() == 404) {
                        myToast(this@PatientReport, "Something went wrong")
                        AppProgressBar.hideLoaderDialog()
                    } else {
                        if (response.isSuccessful) {
                            response.body()?.let { result ->
                                if (result.status == 1) {
                                    if (result.result.prescription.isEmpty()) {
                                        myToast(this@PatientReport, "No Report Found")
                                    } else {
                                        count = 0
                                        binding.recyclerView.apply {
                                            binding.tvNoDataFound.visibility = View.GONE
                                            adapter = AdapterViewReport(result.result.prescription, this@PatientReport)
                                        }
                                    }
                                } else {
                                    myToast(this@PatientReport, "Failed to retrieve report. Status: ${result.status}")
                                }
                            } ?: run {
                                myToast(this@PatientReport, "No data found in the response")
                            }
                        } else {
                            myToast(this@PatientReport, "Failed to get the report. Code: ${response.code()}")
                        }
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                    myToast(this@PatientReport, "Something went wrong. Please try again.")
                } finally {
                    AppProgressBar.hideLoaderDialog()
                }
            }

            override fun onFailure(call: Call<ModelAllReports>, t: Throwable) {
                AppProgressBar.hideLoaderDialog()

                count++
                if (count <= 3) {
                    apiCallGetAllReport()
                } else {
                    myToast(this@PatientReport, "Failed after multiple attempts. Please try again later.")
                }
            }
        })
    }

}