package com.example.ehcf_doctor.Prescription

import android.annotation.SuppressLint
import android.app.Dialog
import android.app.ProgressDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.ehcf.Helper.myToast
import com.example.ehcf.sharedpreferences.SessionManager
import com.example.ehcf_doctor.Appointments.Consulted.adapter.AdapterConsulted
import com.example.ehcf_doctor.Appointments.Upcoming.model.ModelUpComingResponse
import com.example.ehcf_doctor.Booking.model.ModelGetConsultation
import com.example.ehcf_doctor.Prescription.adapter.AdapterPrescription
import com.example.ehcf_doctor.R
import com.example.ehcf_doctor.Retrofit.ApiInterface
import com.example.ehcf_doctor.databinding.FragmentPrescriptionPendingBinding
import com.example.ehcf_doctor.databinding.FragmentUpComingBinding
import com.example.myrecyview.apiclient.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import rezwan.pstu.cse12.youtubeonlinestatus.recievers.NetworkChangeReceiver
import xyz.teamgravity.checkinternet.CheckInternet


class PrescriptionPendingFragment : Fragment() {
    private lateinit var binding:FragmentPrescriptionPendingBinding
    private lateinit var sessionManager: SessionManager
    var mydilaog: Dialog? = null
    var progressDialog : ProgressDialog?=null
    private var tvTimeCounter: TextView?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_prescription_pending, container, false)
    }
    @SuppressLint("LogNotTimber")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPrescriptionPendingBinding.bind(view)
        sessionManager = SessionManager(requireContext())


      //  apiCall()
        apiCallGetConsultation()



    }
/*
    private fun apiCall(){

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
            ) {
                if (response.body()!!.result.isEmpty()) {
                    binding.tvNoDataFound.visibility = View.VISIBLE
                    // myToast(requireActivity(),"No Data Found")
                    progressDialog!!.dismiss()

                } else {
                    binding.rvCancled.apply {
                        binding.tvNoDataFound.visibility = View.GONE
                        adapter = AdapterPrescription(requireContext(), response.body()!!)
                        progressDialog!!.dismiss()

                    }
//                myToast(requireActivity(),response.body()!!.message)
//                progressDialog!!.dismiss()

                    // val recyclerView = findViewById<RecyclerView>(R.id.rvCancled)
                }
            }

            override fun onFailure(call: Call<ModelUpComingResponse>, t: Throwable) {
                t.message?.let { myToast(requireActivity(), it)
                    progressDialog!!.dismiss()

                }
            }
        })
    }
*/
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
                        adapter = AdapterPrescription(requireContext(), response.body()!!)
                        progressDialog!!.dismiss()

                    }
                }

            }

            override fun onFailure(call: Call<ModelGetConsultation>, t: Throwable) {
                myToast(requireActivity(), "Something went wrong")
                progressDialog!!.dismiss()

            }

        })
}
    override fun onStart() {
        super.onStart()
        CheckInternet().check { connected ->
            if (connected) {

                // myToast(requireActivity(),"Connected")
            }
            else {
                val changeReceiver = NetworkChangeReceiver(context)
                changeReceiver.build()
                //  myToast(requireActivity(),"Check Internet")
            }
        }
    }


}