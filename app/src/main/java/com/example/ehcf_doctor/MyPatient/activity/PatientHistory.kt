package com.example.ehcf_doctor.MyPatient.activity

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.ehcf.Helper.myToast
import com.example.ehcf.Testing.Interface.apiInterface
import com.example.ehcf_doctor.Appointments.Upcoming.adapter.AdapterPatientHistory
import com.example.ehcf_doctor.MyPatient.model.ModelPatientHistoryX
import com.example.ehcf_doctor.databinding.ActivityPatientHistoryBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PatientHistory : AppCompatActivity() {
    private lateinit var binding: ActivityPatientHistoryBinding
    var progressDialog: ProgressDialog? = null
    var id = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPatientHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

        val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            //.baseUrl("https://jsonplaceholder.typicode.com/")
            .baseUrl("https://ehcf.thedemostore.in/api/customer/")
            .build()
            .create(apiInterface::class.java)

//        val retrofitData =retrofitBuilder.getUser()
//        retrofitData.enqueue(object : Callback<List<User>?> {
//            override fun onResponse(call: Call<List<User>?>, response: Response<List<User>?>) {
//                val recyclerView= findViewById<RecyclerView>(R.id.recyclerView)
//
//                recyclerView.apply {
//                    adapter=Adapter(context,response.body()!!)
//                }
//            }

        val retrofitData = retrofitBuilder.patientHistory(id)
        retrofitData.enqueue(object : Callback<ModelPatientHistoryX> {
            override fun onResponse(
                call: Call<ModelPatientHistoryX>,
                response: Response<ModelPatientHistoryX>
            ) {
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

            override fun onFailure(call: Call<ModelPatientHistoryX>, t: Throwable) {
                t.message?.let { myToast(this@PatientHistory, it) }
            }
        })
    }
}


