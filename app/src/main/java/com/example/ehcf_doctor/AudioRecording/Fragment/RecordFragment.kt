package com.example.ehcf_doctor.AudioRecording.Fragment

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Chronometer
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import com.example.ehcf.Helper.myToast
import com.example.ehcf_doctor.R
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class RecordFragment : Fragment(), View.OnClickListener {
    private var navController: NavController? = null
    private var list_btn: ImageButton? = null
    private var record_btn: ImageButton? = null
    private var is_recording = false
    private val recording_permission = Manifest.permission.RECORD_AUDIO
    private var mediaRecorder: MediaRecorder? = null
    private var record_file: String? = null
    private var chronoTimer: Chronometer? = null
    private var record_file_name: TextView? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController(view)

        list_btn = view.findViewById(R.id.record_list_button)
        record_btn = view.findViewById(R.id.record_button)
        chronoTimer = view.findViewById(R.id.record_timer)
        record_file_name = view.findViewById(R.id.record_filename)
        list_btn!!.setOnClickListener(this)
        record_btn!!.setOnClickListener(this)


        // start_recording()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_record, container, false)


    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.record_list_button -> if (is_recording) {
                val alertDialog = AlertDialog.Builder(context)
                alertDialog.setPositiveButton("Ok") {
                        dialogInterface, i ->
                    navController!!.navigate(R.id.action_recordFragment_to_recordListFragment)
                }
                alertDialog.setNegativeButton("Cancel", null)
                alertDialog.setTitle("Audio still recording")
                alertDialog.setMessage("Are you sure, you want to stop recording?")
                alertDialog.create().show()
            } else {
                navController!!.navigate(R.id.action_recordFragment_to_recordListFragment)
            }
            R.id.record_button -> if (is_recording) { //Stop Recording

                //Stop Recording Method
                stop_recording()
                record_btn!!.setImageDrawable(resources.getDrawable(R.drawable.record_btn_stopped))
                is_recording = false
            } else {
                //Start Recording
                if (checkAudioPermission()) {
                    //Start Recording Method
                    start_recording()
                    record_btn!!.setImageDrawable(resources.getDrawable(R.drawable.record_btn_recording))
                    is_recording = true
                }
            }
        }
    }

    private fun start_recording() {
        chronoTimer!!.base = SystemClock.elapsedRealtime()
        chronoTimer!!.start()
        val rec_path = requireActivity().getExternalFilesDir("/")!!.absolutePath
        var simpleDateFormat: SimpleDateFormat? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            simpleDateFormat = SimpleDateFormat("dd_MM_YYYY_hh_mm_ss", Locale.CANADA)
        }
        val date = Date()
        record_file = "voxRec" + simpleDateFormat!!.format(date) + ".3gp"
        record_file_name!!.text = "Recording File Name: \n$record_file"
        mediaRecorder = MediaRecorder()
        mediaRecorder!!.setAudioSource(MediaRecorder.AudioSource.MIC)
        mediaRecorder!!.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
        mediaRecorder!!.setOutputFile("$rec_path/$record_file")
        mediaRecorder!!.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)

        try {
            mediaRecorder!!.prepare()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        myToast(requireActivity(),"Meeting Recording Started")

        mediaRecorder!!.start()
    }

    private fun stop_recording() {
        chronoTimer!!.stop()
        is_recording = false
        record_file_name!!.text = "Recording Stopped, File Saved: \n$record_file"
        mediaRecorder!!.stop()
        mediaRecorder!!.release()
        mediaRecorder = null
    }

    private fun checkAudioPermission(): Boolean {
        return if (ActivityCompat.checkSelfPermission(
                requireContext(),
                recording_permission
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            true
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(recording_permission),
                AUDIO_PERMISSION_CODE
            )
            false
        }
    }

    override fun onStop() {
        super.onStop()
        if (is_recording) {
            stop_recording()
        }
    }

    companion object {
         const val AUDIO_PERMISSION_CODE = 89
    }
}