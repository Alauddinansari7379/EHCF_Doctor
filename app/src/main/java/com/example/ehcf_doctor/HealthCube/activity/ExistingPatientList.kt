package com.example.ehcf_doctor.HealthCube.activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.ehcf.Helper.myToast
import com.example.ehcf.sharedpreferences.SessionManager
import com.example.ehcf_doctor.HealthCube.Adapter.AdapterExixtingPatientList
import com.example.ehcf_doctor.Helper.AppProgressBar
import com.example.ehcf_doctor.MyPatient.model.ModelMyPatient
import com.example.ehcf_doctor.R
import com.example.ehcf_doctor.databinding.ActivityExistingPatientListBinding
import com.example.ehcf_doctor.Retrofit.ApiClient
import com.facebook.shimmer.ShimmerFrameLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ExistingPatientList : AppCompatActivity() {
    private var context: Context = this@ExistingPatientList
    var dialog: Dialog? = null
    var shimmerFrameLayout: ShimmerFrameLayout? = null

    private var count = 0
    private var count2 = 0
    private lateinit var sessionManager: SessionManager
    private lateinit var binding: ActivityExistingPatientListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExistingPatientListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sessionManager = SessionManager(this)
        shimmerFrameLayout = findViewById(R.id.shimmer_myPatient)
        shimmerFrameLayout!!.startShimmer();

        binding.imgBack.setOnClickListener {
            onBackPressed()
        }

        binding.imgRefresh.setOnClickListener {
            overridePendingTransition(0, 0)
            finish()
            startActivity(intent)
            overridePendingTransition(0, 0)
        }
        binding.imgSearch.setOnClickListener {
            if (binding.edtSearch.text.isEmpty()) {
                binding.edtSearch.error = "Enter Patient Name"
                binding.edtSearch.requestFocus()
            } else {
                apiCallSearchMyPatient()

            }
        }

        apiCallMyPatient()
    }

    companion object {
        var Diagnostic = ""
    }


    override fun onResume() {
        super.onResume()
        if (PatientList.Exsting == "1") {
            overridePendingTransition(0, 0)
            finish()
            startActivity(intent)
            overridePendingTransition(0, 0)
            PatientList.Exsting = ""
        }

    }

    private fun apiCallMyPatient() {
        AppProgressBar.showLoaderDialog(context)

        ApiClient.apiService.healthcubeExsistingpatient(sessionManager.id.toString())
            .enqueue(object : Callback<ModelMyPatient> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelMyPatient>, response: Response<ModelMyPatient>
                ) {
                    try {
                        if (response.code() == 500) {
                            myToast(this@ExistingPatientList, "Server Error")
                            binding.shimmerMyPatient.visibility = View.GONE
                            AppProgressBar.hideLoaderDialog()
                        } else if (response.body()!!.result.isEmpty()) {
                            binding.tvNoDataFound.visibility = View.VISIBLE
                            binding.shimmerMyPatient.visibility = View.GONE
                            // myToast(requireActivity(),"No Data Found")
                            AppProgressBar.hideLoaderDialog()
                            binding.recyclerView.apply {
                                shimmerFrameLayout?.startShimmer()
                                binding.recyclerView.visibility = View.VISIBLE
                                binding.shimmerMyPatient.visibility = View.GONE
                                adapter =
                                    AdapterExixtingPatientList(
                                        this@ExistingPatientList,
                                        response.body()!!
                                    )
                                AppProgressBar.hideLoaderDialog()

                            }

                        } else {
                            count = 0
                            binding.recyclerView.apply {
                                shimmerFrameLayout?.startShimmer()
                                binding.recyclerView.visibility = View.VISIBLE
                                binding.shimmerMyPatient.visibility = View.GONE
                                binding.tvNoDataFound.visibility = View.GONE
                                adapter =
                                    AdapterExixtingPatientList(
                                        this@ExistingPatientList,
                                        response.body()!!
                                    )
                                AppProgressBar.hideLoaderDialog()

                            }
                        }
                    } catch (e: Exception) {
                        myToast(this@ExistingPatientList, "Something went wrong")

                        e.printStackTrace()
                    }
                }

                override fun onFailure(call: Call<ModelMyPatient>, t: Throwable) {
                    count++
                    if (count <= 3) {
                        apiCallMyPatient()
                    } else {
                        myToast(this@ExistingPatientList, "Something went wrong")
                        binding.shimmerMyPatient.visibility = View.GONE
                    }
                    AppProgressBar.hideLoaderDialog()

                }

            })
    }

    private fun apiCallSearchMyPatient() {
        AppProgressBar.showLoaderDialog(context)
        val patientName = binding.edtSearch.text.toString()
        ApiClient.apiService.searchPatient(sessionManager.id.toString(), patientName)
            .enqueue(object : Callback<ModelMyPatient> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelMyPatient>, response: Response<ModelMyPatient>
                ) {
                    try {
                        if (response.code() == 500) {
                            myToast(this@ExistingPatientList, "Server Error")
                            binding.shimmerMyPatient.visibility = View.GONE
                            AppProgressBar.hideLoaderDialog()

                        } else if (response.body()!!.status == 0) {
                            binding.tvNoDataFound.visibility = View.VISIBLE
                            binding.shimmerMyPatient.visibility = View.GONE
                            myToast(this@ExistingPatientList, "${response.body()!!.message}")
                            AppProgressBar.hideLoaderDialog()

                        } else if (response.body()!!.result.isEmpty()) {
                            binding.recyclerView.adapter =
                                AdapterExixtingPatientList(
                                    this@ExistingPatientList,
                                    response.body()!!
                                )
                            binding.recyclerView.adapter!!.notifyDataSetChanged()
                            binding.tvNoDataFound.visibility = View.VISIBLE
                            binding.shimmerMyPatient.visibility = View.GONE
                            myToast(this@ExistingPatientList, "No Patient Found")
                            AppProgressBar.hideLoaderDialog()

                        } else {
                            count2 = 0
                            binding.recyclerView.adapter =
                                AdapterExixtingPatientList(
                                    this@ExistingPatientList,
                                    response.body()!!
                                )
                            binding.recyclerView.adapter!!.notifyDataSetChanged()
                            binding.tvNoDataFound.visibility = View.GONE
                            shimmerFrameLayout?.startShimmer()
                            binding.recyclerView.visibility = View.VISIBLE
                            binding.shimmerMyPatient.visibility = View.GONE
                            AppProgressBar.hideLoaderDialog()

                        }
                    } catch (e: Exception) {
                        myToast(this@ExistingPatientList, "Something went wrong")
                        e.printStackTrace()
                    }
                }

                override fun onFailure(call: Call<ModelMyPatient>, t: Throwable) {

                    count2++
                    if (count2 <= 3) {
                        apiCallSearchMyPatient()
                    } else {
                        myToast(this@ExistingPatientList, "Something went wrong")
                        binding.shimmerMyPatient.visibility = View.GONE
                    }
                    AppProgressBar.hideLoaderDialog()
                }

            })
    }


}