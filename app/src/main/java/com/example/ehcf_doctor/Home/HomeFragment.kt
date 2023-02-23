package com.example.ehcf_doctor.Home

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.ehcf.sharedpreferences.SessionManager
import com.example.ehcf_doctor.R
import com.example.ehcf_doctor.databinding.FragmentHomeBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.ktx.messaging
import java.io.IOException

class HomeFragment : Fragment() {
    private lateinit var binding:FragmentHomeBinding
    private lateinit var sessionManager: SessionManager
    var doctorname=""
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

}
