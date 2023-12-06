package com.example.ehcf_doctor.MyPatient.activity

import android.annotation.SuppressLint
import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.ehcf.Helper.myToast
import com.example.ehcf.Testing.Interface.ApiInterfaceHelthCube
import com.example.ehcf.sharedpreferences.SessionManager
import com.example.ehcf_doctor.Appointments.Upcoming.adapter.AdapterPatientHistory
import com.example.ehcf_doctor.MyPatient.model.ModelMyPatient
import com.example.ehcf_doctor.MyPatient.model.ModelPatientHistoryX
import com.example.ehcf_doctor.databinding.ActivityPatientHistoryBinding
import com.example.myrecyview.apiclient.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PatientHistory : AppCompatActivity() {
    private lateinit var binding: ActivityPatientHistoryBinding
    var progressDialog: ProgressDialog? = null
    var id = ""
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPatientHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sessionManager = SessionManager(this)

        binding.imgBack.setOnClickListener {
            onBackPressed()
        }
        id = intent.getStringExtra("Id").toString()
        apiCall()
    }

    private fun apiCall() {

        progressDialog = ProgressDialog(this@PatientHistory)
        progressDialog!!.setMessage("Loading..")
        progressDialog!!.setTitle("Please Wait")
        progressDialog!!.isIndeterminate = false
        progressDialog!!.setCancelable(true)
        progressDialog!!.show()

        ApiClient.apiService.patientHistory(id)
            .enqueue(object : Callback<ModelPatientHistoryX> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelPatientHistoryX>, response: Response<ModelPatientHistoryX>
                ) {

                try {

                    if (response.code() == 500) {
                        myToast(this@PatientHistory, "Server Error")

                    } else if (response.body()!!.result.isEmpty()) {
                        myToast(this@PatientHistory, "No Appointment Found")
                        progressDialog!!.dismiss()

                    } else {
                        binding.rvPtientHistory.apply {
                            response.body()!!.result.size.toString()
                            Log.e("Size", response.body()!!.result.size.toString())
                            //  myToast(requireActivity(),"Adapter")
                            adapter = AdapterPatientHistory(this@PatientHistory, response.body()!!)
                            progressDialog!!.dismiss()


                        }
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
                }
                    catch (e: Exception) {
                        progressDialog!!.dismiss()
                        myToast(this@PatientHistory, "Something went wrong")
                        e.printStackTrace()
                    }
            }

            override fun onFailure(call: Call<ModelPatientHistoryX>, t: Throwable) {
                t.message?.let { myToast(this@PatientHistory, it) }
            }
        })
    }
}


