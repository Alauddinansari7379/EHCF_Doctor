package com.example.ehcf_doctor.HealthCube.activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.example.ehcf.Helper.myToast
import com.example.ehcf.sharedpreferences.SessionManager
import com.example.ehcf_doctor.HealthCube.Adapter.AdapterPatientList
import com.example.ehcf_doctor.Helper.AppProgressBar
import com.example.ehcf_doctor.MyPatient.model.ModelMyPatient
import com.example.ehcf_doctor.MyPatient.model.ResultMyPatient
import com.example.ehcf_doctor.R
import com.example.ehcf_doctor.databinding.ActivityPatientListBinding
import com.example.ehcf_doctor.Retrofit.ApiClient
import com.facebook.shimmer.ShimmerFrameLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList


class PatientList : AppCompatActivity() {
    private var context: Context = this@PatientList
    var dialog: Dialog? = null

    private lateinit var sessionManager: SessionManager
    private lateinit var binding: ActivityPatientListBinding
    var shimmerFrameLayout: ShimmerFrameLayout? = null
    private lateinit var mainData: ArrayList<ResultMyPatient>
    private var count = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPatientListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sessionManager = SessionManager(this)
        shimmerFrameLayout = findViewById(R.id.shimmer_myPatient)
        shimmerFrameLayout!!.startShimmer();

        binding.imgBack.setOnClickListener {
            onBackPressed()
        }

        binding.edtSearch.addTextChangedListener { str ->
            setRecyclerViewAdapter(mainData.filter {
                it.name!!.contains(str.toString(), ignoreCase = true)
            } as ArrayList<ResultMyPatient>)
        }
        binding.imgRefresh.setOnClickListener {
            overridePendingTransition(0, 0)
            finish()
            startActivity(intent)
            overridePendingTransition(0, 0)
        }

        apiCallMyPatient()
    }

    companion object {
        var Diagnostic = ""
        var Exsting = ""
        var TestHistory = ""
    }


    private fun apiCallMyPatient() {
        AppProgressBar.showLoaderDialog(context)

        ApiClient.apiService.healthcubePatientList(sessionManager.id.toString())
            .enqueue(object : Callback<ModelMyPatient> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelMyPatient>, response: Response<ModelMyPatient>
                ) {
                    try {
                        if (response.code() == 200) {
                            count = 0
                            mainData = response.body()!!.result
                        }
                        if (response.code() == 500) {
                            myToast(this@PatientList, "Server Error")
                            binding.shimmerMyPatient.visibility = View.GONE
                            AppProgressBar.hideLoaderDialog()
                        } else if (response.body()!!.result.isEmpty()) {
                            binding.tvNoDataFound.visibility = View.VISIBLE
                            binding.shimmerMyPatient.visibility = View.GONE
                            // myToast(requireActivity(),"No Data Found")
                            AppProgressBar.hideLoaderDialog()

                        } else {
                            setRecyclerViewAdapter(mainData)
                            AppProgressBar.hideLoaderDialog()


                        }
                    } catch (e: Exception) {
                        myToast(this@PatientList, "Something went wrong")
                        e.printStackTrace()
                    }
                }


                override fun onFailure(call: Call<ModelMyPatient>, t: Throwable) {
                    count++
                    if (count <= 3) {
                        apiCallMyPatient()
                    } else {
                        myToast(this@PatientList, "Something went wrong")
                        binding.shimmerMyPatient.visibility = View.GONE
                    }
                    AppProgressBar.hideLoaderDialog()

                }

            })
    }

    private fun setRecyclerViewAdapter(data: ArrayList<ResultMyPatient>) {
        binding.recyclerView.apply {
            shimmerFrameLayout?.startShimmer()
            binding.recyclerView.visibility = View.VISIBLE
            binding.shimmerMyPatient.visibility = View.GONE
            binding.tvNoDataFound.visibility = View.GONE
            adapter = AdapterPatientList(this@PatientList, data)
            AppProgressBar.hideLoaderDialog()

        }
    }

}