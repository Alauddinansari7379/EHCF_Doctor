package com.example.ehcf_doctor.HealthCube.activity

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.ehcf.Helper.myToast
import com.example.ehcf.sharedpreferences.SessionManager
import com.example.ehcf_doctor.HealthCube.Adapter.AdapterTestHistory
import com.example.ehcf_doctor.HealthCube.Model.ModelTestHistory
import com.example.ehcf_doctor.Helper.AppProgressBar
import com.example.ehcf_doctor.R
import com.example.ehcf_doctor.databinding.ActivityHealthcubeTestHistoryBinding
import com.example.ehcf_doctor.Retrofit.ApiClient
import com.facebook.shimmer.ShimmerFrameLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HealthCubeTestHistory : AppCompatActivity() {
    private lateinit var binding: ActivityHealthcubeTestHistoryBinding
    private val context: Context = this@HealthCubeTestHistory
    private lateinit var sessionManager: SessionManager
    var shimmerFrameLayout: ShimmerFrameLayout? = null
    private var count = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHealthcubeTestHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sessionManager = SessionManager(this)
        shimmerFrameLayout = findViewById(R.id.shimmer_myPatient)
        shimmerFrameLayout!!.startShimmer()
        binding.imgBack.setOnClickListener {
            onBackPressed()
        }


        apiCallTestHistory(intent.getStringExtra("id").toString())
    }


    private fun apiCallTestHistory(id: String) {
        AppProgressBar.showLoaderDialog(context)

        ApiClient.apiService.healthCubeReportHistory(id)
            .enqueue(object : Callback<ModelTestHistory> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelTestHistory>, response: Response<ModelTestHistory>
                ) {
                    try {
                        if (response.code() == 500) {
                            myToast(this@HealthCubeTestHistory, "Server Error")
                            binding.shimmerMyPatient.visibility = View.GONE
                            AppProgressBar.hideLoaderDialog()
                        } else if (response.body()!!.result.isEmpty()) {
                            binding.tvNoDataFound.visibility = View.VISIBLE
                            binding.shimmerMyPatient.visibility = View.GONE
                            // myToast(requireActivity(),"No Data Found")
                            AppProgressBar.hideLoaderDialog()

                        } else {
                            count = 0
                            binding.recyclerView.apply {
                                shimmerFrameLayout?.startShimmer()
                                binding.recyclerView.visibility = View.VISIBLE
                                binding.shimmerMyPatient.visibility = View.GONE
                                binding.tvNoDataFound.visibility = View.GONE
                                adapter = AdapterTestHistory(
                                    this@HealthCubeTestHistory,
                                    response.body()!!
                                )
                                AppProgressBar.hideLoaderDialog()

                            }
                        }
                    } catch (e: Exception) {
                        myToast(this@HealthCubeTestHistory, "Something went wrong")
                        e.printStackTrace()
                    }
                }


                override fun onFailure(call: Call<ModelTestHistory>, t: Throwable) {
                    count++
                    if (count <= 3) {
                        apiCallTestHistory(id)
                    } else {
                        myToast(this@HealthCubeTestHistory, "Something went wrong")
                        binding.shimmerMyPatient.visibility = View.GONE
                    }
                    AppProgressBar.hideLoaderDialog()

                }

            })
    }

}