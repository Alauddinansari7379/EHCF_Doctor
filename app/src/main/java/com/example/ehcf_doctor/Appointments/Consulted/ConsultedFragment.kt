package com.example.ehcf_doctor.Appointments.Consulted

import android.annotation.SuppressLint
import android.app.Dialog
import android.app.ProgressDialog
import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.ehcf.sharedpreferences.SessionManager
import com.example.ehcf_doctor.R
import com.example.ehcf_doctor.databinding.FragmentConsultedBinding
import com.example.ehcf_doctor.databinding.FragmentUpComingBinding
import org.jitsi.meet.sdk.JitsiMeetActivity
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions
import java.net.MalformedURLException
import java.net.URL


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
        binding.btnCall.setOnClickListener {
            try {
                val options: JitsiMeetConferenceOptions = JitsiMeetConferenceOptions.Builder()
                    .setServerURL(URL("https://meet.jit.si"))
                    .setRoom("test123456")
                    .setAudioMuted(false)
                    .setVideoMuted(false)
                    .build()
                JitsiMeetActivity.launch(requireContext(), options)
            } catch (e: MalformedURLException) {
                e.printStackTrace();
            }
        }


        binding.btnCheck.setOnClickListener {
            dialog=   Dialog(requireContext())
            val btnOkDialog = view.findViewById<Button>(R.id.btnOkDialog)
            if (view.parent != null) {
                (view.parent as ViewGroup).removeView(view) // <- fix
            }
            dialog!!.setContentView(view)
            dialog?.setCancelable(false)
            // dialog?.setContentView(view)

            dialog?.show()
            timeCounter()
        }
        btnOkDialog.setOnClickListener {
            dialog?.dismiss()
        }

    }

    private fun timeCounter() {
        object : CountDownTimer(30000, 1000) {
            // Callback function, fired on regular interval
            @SuppressLint("SetTextI18n")
            override fun onTick(millisUntilFinished: Long) {
                tvTimeCounter!!.text = "00" + ":05:" + millisUntilFinished / 1000 + " " + ""
            }

            override fun onFinish() {
//                binding.tvResend.isClickable = true
//                binding.tvResend.setTextColor(Color.parseColor("#9F367A"))

            }
        }.start()
    }
}