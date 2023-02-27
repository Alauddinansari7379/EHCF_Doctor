package com.example.ehcf_doctor.Appointments.Cancelled.activity

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ehcf.Helper.myToast
import com.example.ehcf.sharedpreferences.SessionManager
import com.example.ehcf_doctor.Appointments.Cancelled.adapter.AdapterCancelled
import com.example.ehcf_doctor.Appointments.Upcoming.adapter.AdapterUpComing
import com.example.ehcf_doctor.Appointments.Upcoming.model.ModelUpComingResponse
import com.example.ehcf_doctor.Booking.model.ModelGetConsultation
import com.example.ehcf_doctor.R
import com.example.ehcf_doctor.Retrofit.ApiInterface
import com.example.ehcf_doctor.databinding.FragmentCancelledBinding
import com.example.myrecyview.apiclient.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CancelledFragment : Fragment() {
    private lateinit var binding:FragmentCancelledBinding
    private lateinit var sessionManager: SessionManager
    var progressDialog : ProgressDialog?=null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_cancelled, container, false)
    }
    @SuppressLint("LogNotTimber")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCancelledBinding.bind(view)
        sessionManager = SessionManager(requireContext())
        Log.e("Id","${sessionManager.id}")

        //apiCall()
        apiCallGetConsultation()
        binding.imgRefresh.setOnClickListener {
           // apiCall()
            apiCallGetConsultation()
        }

    }

    private fun apiCallGetConsultation() {
        progressDialog = ProgressDialog(requireContext())
        progressDialog!!.setMessage("Loading..")
        progressDialog!!.setTitle("Please Wait")
        progressDialog!!.isIndeterminate = false
        progressDialog!!.setCancelable(true)

        progressDialog!!.show()

        ApiClient.apiService.getConsultation(sessionManager.id.toString())
            .enqueue(object : Callback<ModelGetConsultation> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelGetConsultation>, response: Response<ModelGetConsultation>
                ) {
                    if (response.body()!!.result.isEmpty()) {
                        binding.tvNoDataFound.visibility = View.VISIBLE
                        // myToast(requireActivity(),"No Data Found")
                        progressDialog!!.dismiss()

                    } else {
                        binding.rvCancled.apply {
                            binding.tvNoDataFound.visibility = View.GONE
                            adapter = AdapterCancelled(requireContext(), response.body()!!)
                            progressDialog!!.dismiss()

                        }
                    }

                }

                override fun onFailure(call: Call<ModelGetConsultation>, t: Throwable) {
                    myToast(requireActivity(), t.message.toString())
                    progressDialog!!.dismiss()

                }

            })
    }

   /* private fun apiCall(){

        progressDialog = ProgressDialog(requireContext())
        progressDialog!!.setMessage("Loading..")
        progressDialog!!.setTitle("Please Wait")
        progressDialog!!.isIndeterminate = false
        progressDialog!!.setCancelable(true)
        progressDialog!!.show()
        val id="1"

        val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            //.baseUrl("https://jsonplaceholder.typicode.com/")
            .baseUrl("https://ehcf.thedemostore.in/api/doctor/")
            .build()
            .create(ApiInterface::class.java)

        val retrofitData = retrofitBuilder.getBooking(sessionManager.id.toString())
        retrofitData.enqueue(object : Callback<ModelUpComingResponse> {
            override fun onResponse(
                call: Call<ModelUpComingResponse>,
                response: Response<ModelUpComingResponse>
            )
            {
             if (response.body()!!.result.isEmpty()) {
                    binding.tvNoDataFound.visibility = View.VISIBLE
                    // myToast(requireActivity(),"No Data Found")
                    progressDialog!!.dismiss()
                }
                binding.rvCancled.apply {
                    adapter = AdapterCancelled(requireContext(), response.body()!!)
                    binding.tvNoDataFound.visibility = View.GONE
                    progressDialog!!.dismiss()

                }
            }

            override fun onFailure(call: Call<ModelUpComingResponse>, t: Throwable) {
                t.message?.let { myToast(requireActivity(), it)
                    progressDialog!!.dismiss()

                }
            }
        })
    }*/

}