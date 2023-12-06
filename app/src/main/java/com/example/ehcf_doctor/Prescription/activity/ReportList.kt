package com.example.ehcf_doctor.Prescription.activity

import android.annotation.SuppressLint
import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.ehcf.Helper.myToast
import com.example.ehcf.report.model.ModelGetTest
import com.example.ehcf_doctor.MyPatient.adapter.AdapterViewReport
import com.example.ehcf_doctor.Prescription.adapter.AdapterViewReportTest
import com.example.ehcf_doctor.R
import com.example.ehcf_doctor.databinding.ActivityReportListBinding
import com.example.myrecyview.apiclient.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReportList : AppCompatActivity() {
    private lateinit var binding: ActivityReportListBinding
    var progressDialog: ProgressDialog? = null
    var id = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReportListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        id = intent.getStringExtra("id").toString()
        binding.imgBack.setOnClickListener {
            onBackPressed()
        }

        apiCallGetTest()
    }


    private fun apiCallGetTest() {
        progressDialog = ProgressDialog(this@ReportList)
        progressDialog!!.setMessage("Loading..")
        progressDialog!!.setTitle("Please Wait")
        progressDialog!!.isIndeterminate = false
        progressDialog!!.setCancelable(true)
        progressDialog!!.show()

        ApiClient.apiService.getTest(id)
            .enqueue(object : Callback<ModelGetTest> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelGetTest>, response: Response<ModelGetTest>
                ) {
                    try {
                        if (response.body()!!.result.isEmpty()) {
                            binding.tvNoDataFound.visibility = View.VISIBLE
                            // myToast(requireActivity(),"No Data Found")
                            progressDialog!!.dismiss()

                        } else {
                            binding.recyclerView.apply {
                                binding.tvNoDataFound.visibility = View.GONE
                                adapter = AdapterViewReportTest(this@ReportList, response.body()!!)
                            }
                            progressDialog!!.dismiss()

                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        myToast(this@ReportList, "Something went wrong")
                        progressDialog!!.dismiss()

                    }
                }


                override fun onFailure(call: Call<ModelGetTest>, t: Throwable) {
                    myToast(this@ReportList, "Something went wrong")
                    progressDialog!!.dismiss()

                }

            })
    }

}