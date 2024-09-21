package com.example.ehcf_doctor.Prescription.activity

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.ehcf.Helper.myToast
import com.example.ehcf.report.model.ModelGetTest
import com.example.ehcf_doctor.Helper.AppProgressBar
import com.example.ehcf_doctor.Prescription.adapter.AdapterViewReportTest
import com.example.ehcf_doctor.Retrofit.ApiClient
import com.example.ehcf_doctor.databinding.ActivityReportListBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReportList : AppCompatActivity() {
    private lateinit var binding: ActivityReportListBinding
    private var context: Context = this@ReportList
    var id = ""
    private var count = 0

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
        AppProgressBar.showLoaderDialog(context)

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
                            AppProgressBar.hideLoaderDialog()

                        } else {
                            count = 0
                            binding.recyclerView.apply {
                                binding.tvNoDataFound.visibility = View.GONE
                                adapter = AdapterViewReportTest(this@ReportList, response.body()!!)
                            }
                            AppProgressBar.hideLoaderDialog()

                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        myToast(this@ReportList, "Something went wrong")
                        AppProgressBar.hideLoaderDialog()

                    }
                }


                override fun onFailure(call: Call<ModelGetTest>, t: Throwable) {
                    count++
                    if (count <= 3) {
                        apiCallGetTest()
                    } else {
                        myToast(this@ReportList, "Something went wrong")
                    }
                    AppProgressBar.hideLoaderDialog()

                }

            })
    }

}