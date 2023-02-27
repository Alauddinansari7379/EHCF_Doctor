package com.example.ehcf_doctor.Appointments.Consulted.activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.app.ProgressDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.ehcf.Helper.myToast
import com.example.ehcf.sharedpreferences.SessionManager
import com.example.ehcf_doctor.Appointments.Cancelled.adapter.AdapterCancelled
import com.example.ehcf_doctor.Appointments.Consulted.adapter.AdapterConsulted
import com.example.ehcf_doctor.Appointments.Upcoming.model.ModelUpComingResponse
import com.example.ehcf_doctor.Booking.model.ModelGetConsultation
import com.example.ehcf_doctor.R
import com.example.ehcf_doctor.Retrofit.ApiInterface
import com.example.ehcf_doctor.databinding.FragmentConsultedBinding
import com.example.myrecyview.apiclient.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class ConsultedFragment : Fragment() {
    private lateinit var binding: FragmentConsultedBinding
    private lateinit var sessionManager: SessionManager
    var mydilaog: Dialog? = null
    var progressDialog: ProgressDialog? = null
    var dialog: Dialog? = null
    private var tvTimeCounter: TextView? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_consulted, container, false)
    }

    @SuppressLint("LogNotTimber")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentConsultedBinding.bind(view)
        sessionManager = SessionManager(requireContext())

        val view = layoutInflater.inflate(R.layout.time_dialog, null)

        val btnOkDialog = view.findViewById<Button>(R.id.btnOkDialog)
        tvTimeCounter = view.findViewById<TextView>(R.id.tvTimeCounter)


        apiCallGetConsultation()
//        binding.btnCall.setOnClickListener {
//            try {
//                val options: JitsiMeetConferenceOptions = JitsiMeetConferenceOptions.Builder()
//                    .setServerURL(URL("https://meet.jit.si"))
//                    .setRoom("test123456")
//                    .setAudioMuted(false)
//                    .setVideoMuted(false)
//                    .build()
//                JitsiMeetActivity.launch(requireContext(), options)
//            } catch (e: MalformedURLException) {
//                e.printStackTrace();
//            }
//        }
//
//    }


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
                            adapter = AdapterConsulted(requireContext(), response.body()!!)
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

}