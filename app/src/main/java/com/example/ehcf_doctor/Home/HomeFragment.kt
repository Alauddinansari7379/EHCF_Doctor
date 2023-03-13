package com.example.ehcf_doctor.Home

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.ContentValues.TAG
import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.ehcf.Helper.isOnline
import com.example.ehcf.Helper.myToast
import com.example.ehcf.sharedpreferences.SessionManager
import com.example.ehcf_doctor.Appointments.Upcoming.adapter.AdapterHome
import com.example.ehcf_doctor.Appointments.Upcoming.adapter.AdapterUpComing
import com.example.ehcf_doctor.Booking.model.ModelGetConsultation
import com.example.ehcf_doctor.R
import com.example.ehcf_doctor.databinding.FragmentHomeBinding
import com.example.myrecyview.apiclient.ApiClient
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.ktx.messaging
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import rezwan.pstu.cse12.youtubeonlinestatus.recievers.NetworkChangeReceiver


class HomeFragment : Fragment(){
    private lateinit var binding:FragmentHomeBinding
    private lateinit var sessionManager: SessionManager
    var doctorname=""
    var progressDialog : ProgressDialog?=null

    private val senderID = "YOUR_SENDER_ID"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    @SuppressLint("LogNotTimber")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentHomeBinding.bind(view)
        sessionManager = SessionManager(requireContext())

        doctorname= sessionManager.doctorName.toString()
       binding.tvDoctorName.text= doctorname
        Log.e("DoctorNAme,", "$doctorname")
        Log.e("DoctorId,", "${sessionManager.id}")
//
        apiCallGetConsultationWating()
//        CheckInternet().check { connected ->
//            if (connected) {
//               // myToast(requireActivity(), "")
//            }
//        }


        Firebase.messaging.subscribeToTopic("Doctor")
            .addOnCompleteListener { task ->
                var msg = "Subscribed"
                if (!task.isSuccessful) {


                    msg = "Subscribe failed"
                }
                Log.d(TAG, msg)
            }

        getToken()

    }
    private fun isNetworkConnected(): Boolean {
        val cm =
            requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo != null
    }
    @SuppressLint("StringFormatInvalid")
    private fun getToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            // Log and toast
            val msg = getString(R.string.channel_id, token)
            Log.d(TAG, token)
           // Toast.makeText(requireContext(), token, Toast.LENGTH_SHORT).show()
        })
    }
    private fun apiCallGetConsultationWating() {

        ApiClient.apiService.getConsultation(sessionManager.id.toString(),"waiting_for_accept")
            .enqueue(object : Callback<ModelGetConsultation> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelGetConsultation>, response: Response<ModelGetConsultation>
                ) {
                    if (response.body()!!.result.isEmpty()) {
                        // myToast(requireActivity(),"No Data Found")

                    } else {
                        binding.rvUpcoming.apply {
                          //  myToast(requireActivity(),"Adapter")
                            adapter = AdapterHome(requireContext(), response.body()!!,)

                        }
                    }

                }

                override fun onFailure(call: Call<ModelGetConsultation>, t: Throwable) {
                   // myToast(requireActivity(), "Something went wrong")
                    progressDialog!!.dismiss()

                }

            })
    }

    override fun onStart() {
        super.onStart()
        if (isOnline(requireContext())){
          //  myToast(requireActivity(), "Connected")
        }else{
            val changeReceiver = NetworkChangeReceiver(context)
            changeReceiver.build()
          //  myToast(requireActivity(), "Not C")

        }
//        CheckInternet().check { connected ->
//            if (connected) {
//             //    myToast(requireActivity(),"Connected")
//            }
//            else {
//                val changeReceiver = NetworkChangeReceiver(context)
//                changeReceiver.build()
//                //  myToast(requireActivity(),"Check Internet")
//            }
//        }
    }

}
