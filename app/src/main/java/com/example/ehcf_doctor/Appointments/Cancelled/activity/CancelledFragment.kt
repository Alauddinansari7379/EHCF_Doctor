package com.example.ehcf_doctor.Appointments.Cancelled.activity

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ehcf.sharedpreferences.SessionManager
import com.example.ehcf_doctor.R
import com.example.ehcf_doctor.databinding.FragmentCancelledBinding

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


    }

}