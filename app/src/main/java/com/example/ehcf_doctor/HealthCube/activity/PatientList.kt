package com.example.ehcf_doctor.HealthCube.activity

 import android.annotation.SuppressLint
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.ehcf.Helper.myToast
import com.example.ehcf.sharedpreferences.SessionManager
import com.example.ehcf_doctor.HealthCube.Adapter.AdapterPatientList
import com.example.ehcf_doctor.MyPatient.model.ModelMyPatient
import com.example.ehcf_doctor.R
import com.example.ehcf_doctor.databinding.ActivityPatientListBinding
import com.example.myrecyview.apiclient.ApiClient
import com.facebook.shimmer.ShimmerFrameLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class PatientList : AppCompatActivity() {
    private var context: Context = this@PatientList
    var dialog: Dialog? = null

    var progressDialog: ProgressDialog? = null
    private lateinit var sessionManager: SessionManager
    private lateinit var binding: ActivityPatientListBinding
    var shimmerFrameLayout: ShimmerFrameLayout? = null
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
companion object{
    var Diagnostic=""
    var Exsting=""
    var TestHistory=""
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
    private fun apiCallMyPatient() {
        progressDialog = ProgressDialog(this@PatientList)
        progressDialog!!.setMessage("Loading...")
        progressDialog!!.setTitle("Please Wait")
        progressDialog!!.isIndeterminate = false
        progressDialog!!.setCancelable(true)
        // progressDialog!!.show()

        ApiClient.apiService.healthcubePatientList(sessionManager.id.toString())
            .enqueue(object : Callback<ModelMyPatient> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelMyPatient>, response: Response<ModelMyPatient>
                ) {
                    try{
                    if (response.code() == 500) {
                        myToast(this@PatientList, "Server Error")
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
                                AdapterPatientList(this@PatientList, response.body()!!)
                            progressDialog!!.dismiss()

                        }
                    }
                }catch (e:Exception){
                        myToast(this@PatientList, "Something went wrong")
                        e.printStackTrace()
                }
                }


                override fun onFailure(call: Call<ModelMyPatient>, t: Throwable) {
                    myToast(this@PatientList, "Something went wrong")
                    binding.shimmerMyPatient.visibility = View.GONE
                    progressDialog!!.dismiss()

                }

            })
    }
     private fun apiCallSearchMyPatient() {
        progressDialog = ProgressDialog(this@PatientList)
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
                            myToast(this@PatientList, "Server Error")
                            binding.shimmerMyPatient.visibility = View.GONE
                            progressDialog!!.dismiss()

                        } else if (response.body()!!.status == 0) {
                            binding.tvNoDataFound.visibility = View.VISIBLE
                            binding.shimmerMyPatient.visibility = View.GONE
                            myToast(this@PatientList, "${response.body()!!.message}")
                            progressDialog!!.dismiss()

                        } else if (response.body()!!.result.isEmpty()) {
                            binding.recyclerView.adapter =
                                AdapterPatientList(this@PatientList, response.body()!!)
                            binding.recyclerView.adapter!!.notifyDataSetChanged()
                            binding.tvNoDataFound.visibility = View.VISIBLE
                            binding.shimmerMyPatient.visibility = View.GONE
                            myToast(this@PatientList, "No Patient Found")
                            progressDialog!!.dismiss()

                        } else {
                            binding.recyclerView.adapter =
                                AdapterPatientList(this@PatientList, response.body()!!)
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
                    }catch (e:Exception){
                        e.printStackTrace()
                        myToast(this@PatientList, "Something went wrong")

                    }
                }

                override fun onFailure(call: Call<ModelMyPatient>, t: Throwable) {
                    myToast(this@PatientList, "Something went wrong")
                    binding.shimmerMyPatient.visibility = View.GONE
                    progressDialog!!.dismiss()

                }

            })
    }


}