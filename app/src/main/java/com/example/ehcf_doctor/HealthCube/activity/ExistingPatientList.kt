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
import com.example.ehcf_doctor.HealthCube.Adapter.AdapterPatientList
import com.example.ehcf_doctor.MyPatient.model.ModelMyPatient
import com.example.ehcf_doctor.R
import com.example.ehcf_doctor.databinding.ActivityExistingPatientListBinding
import com.example.ehcf_doctor.databinding.ActivityPatientListBinding
import com.example.myrecyview.apiclient.ApiClient
import com.facebook.shimmer.ShimmerFrameLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ExistingPatientList : AppCompatActivity() {
    private var context: Context = this@ExistingPatientList
    var dialog: Dialog? = null
    var shimmerFrameLayout: ShimmerFrameLayout? = null

    var progressDialog: ProgressDialog? = null
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

//        binding.btnRegisterNewP.setOnClickListener {
//            startActivity(Intent(this@PatientList, AddPatient::class.java))
//        }
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

    //    fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<String?>?, grantResults: IntArray
//    ) {
//        if (permissions != null) {
//            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        }
//        when (requestCode) {
//            MY_PERMISSIONS_REQUEST_CALL_PHONE -> {
//
//                // If request is cancelled, the result arrays are empty.
//                if (grantResults.size > 0
//                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
//                ) {
//
//                    // permission was granted, yay! Do the phone call
//                } else {
//
//                    // permission denied, boo! Disable the
//                    // functionality that depends on this permission.
//                }
//                return
//            }
//        }
//    }
//
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
        progressDialog = ProgressDialog(this@ExistingPatientList)
        progressDialog!!.setMessage("Loading...")
        progressDialog!!.setTitle("Please Wait")
        progressDialog!!.isIndeterminate = false
        progressDialog!!.setCancelable(true)
        // progressDialog!!.show()

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
                            progressDialog!!.dismiss()

                        } else if (response.body()!!.result.isEmpty()) {
                            binding.tvNoDataFound.visibility = View.VISIBLE
                            binding.shimmerMyPatient.visibility = View.GONE
                            // myToast(requireActivity(),"No Data Found")
                            progressDialog!!.dismiss()

                        } else {
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
                                progressDialog!!.dismiss()

                            }
                        }
                    } catch (e: Exception) {
                        myToast(this@ExistingPatientList, "Something went wrong")

                        e.printStackTrace()
                    }
                }

                override fun onFailure(call: Call<ModelMyPatient>, t: Throwable) {
                    myToast(this@ExistingPatientList, "Something went wrong")
                    binding.shimmerMyPatient.visibility = View.GONE
                    progressDialog!!.dismiss()

                }

            })
    }

    private fun apiCallSearchMyPatient() {
        progressDialog = ProgressDialog(this@ExistingPatientList)
        progressDialog!!.setMessage("Loading...")
        progressDialog!!.setTitle("Please Wait")
        progressDialog!!.isIndeterminate = false
        progressDialog!!.setCancelable(true)
        progressDialog!!.show()
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
                            progressDialog!!.dismiss()

                        } else if (response.body()!!.status == 0) {
                            binding.tvNoDataFound.visibility = View.VISIBLE
                            binding.shimmerMyPatient.visibility = View.GONE
                            myToast(this@ExistingPatientList, "${response.body()!!.message}")
                            progressDialog!!.dismiss()

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
                            progressDialog!!.dismiss()

                        } else {
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
                            progressDialog!!.dismiss()
//                        binding.rvManageSlot.apply {
//                            binding.tvNoDataFound.visibility = View.GONE
//                            shimmerFrameLayout?.startShimmer()
//                            binding.rvManageSlot.visibility = View.VISIBLE
//                            binding.shimmerMySlot.visibility = View.GONE
//                            // myToast(this@ShuduleTiming, response.body()!!.message)
//                            adapter = AdapterSlotsList(this@MySlot, response.body()!!, this@MySlot)
//                            progressDialog!!.dismiss()
//
//                        }
                        }
                    } catch (e: Exception) {
                        myToast(this@ExistingPatientList, "Something went wrong")
                        e.printStackTrace()
                    }
                }

                override fun onFailure(call: Call<ModelMyPatient>, t: Throwable) {
                    myToast(this@ExistingPatientList, "Something went wrong")
                    binding.shimmerMyPatient.visibility = View.GONE
                    progressDialog!!.dismiss()

                }

            })
    }


}