package com.example.ehcf_doctor.MyPatient.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.ehcf.Helper.myToast
import com.example.ehcf.Upload.model.ModelGetAllReport
import com.example.ehcf.sharedpreferences.SessionManager
import com.example.ehcf_doctor.MyPatient.adapter.AdapterViewReport
import com.example.ehcf_doctor.MyPatient.model.ModelAllReport
import com.example.ehcf_doctor.Prescription.activity.ViewReport
import com.example.ehcf_doctor.databinding.ActivityPatientReportBinding
import com.example.myrecyview.apiclient.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PatientReport : AppCompatActivity() {
    private lateinit var binding: ActivityPatientReportBinding
    var progressDialog: ProgressDialog? = null
    private var context: Context = this@PatientReport
    var id=""
    private lateinit var sessionManager: SessionManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPatientReportBinding.inflate(layoutInflater)
        setContentView(binding.root)
        id=intent.getStringExtra("Id").toString()
        apiCallGetAllReport()
       // apiCallDoctorProfile()
        binding.imgBack.setOnClickListener {
            onBackPressed()
        }


    }

    private fun apiCallGetAllReport() {
        progressDialog = ProgressDialog(this@PatientReport)
        progressDialog!!.setMessage("Loading..")
        progressDialog!!.setTitle("Please Wait")
        progressDialog!!.isIndeterminate = false
        progressDialog!!.setCancelable(true)
        progressDialog!!.show()

        ApiClient.apiService.getReport(id)
            .enqueue(object : Callback<ModelGetAllReport> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelGetAllReport>, response: Response<ModelGetAllReport>
                ) {
                    try {
                        if (response.body()!!.result.isEmpty()) {
                             myToast(this@PatientReport,"No Report Found")
                            progressDialog!!.dismiss()

                        } else {
                            binding.recyclerView.apply {
                                binding.tvNoDataFound.visibility = View.GONE
                                adapter = AdapterViewReport(response.body()!!, this@PatientReport)
                            }
                            progressDialog!!.dismiss()

                        }


                    }catch (e:Exception){
                        e.printStackTrace()
                        myToast(this@PatientReport, "Something went wrong")
                        progressDialog!!.dismiss()
                    }
                }

                override fun onFailure(call: Call<ModelGetAllReport>, t: Throwable) {
                    myToast(this@PatientReport, "Something went wrong")
                    progressDialog!!.dismiss()

                }

            })
    }


}