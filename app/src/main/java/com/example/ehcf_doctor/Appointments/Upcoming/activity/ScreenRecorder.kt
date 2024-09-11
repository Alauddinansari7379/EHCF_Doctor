package com.example.ehcf_doctor.Appointments.Upcoming.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.media.projection.MediaProjectionManager
import android.os.Build
import android.os.Bundle
import android.widget.Chronometer
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import com.example.ehcf.Helper.myToast
import com.example.ehcf_doctor.Appointments.Appointments
import com.example.ehcf_doctor.databinding.ActivityMainRecordingBinding
import com.hbisoft.hbrecorder.HBRecorder
import com.hbisoft.hbrecorder.HBRecorderListener
import org.jitsi.meet.sdk.JitsiMeetActivity
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions
import java.io.IOException
import java.net.MalformedURLException
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

class ScreenRecorder : AppCompatActivity(), HBRecorderListener{
    private val calendar = Calendar.getInstance()
    private lateinit var binding:ActivityMainRecordingBinding
    var hbRecorder: HBRecorder? = null
    var startTime = ""
    private var is_recording = false
    private val recording_permission = Manifest.permission.RECORD_AUDIO
    private var mediaRecorder: MediaRecorder? = null
    private var record_file: String? = "file"
    private var chronoTimer: Chronometer? = null
    var bookingId = ""
    private var record_file_name: TextView? = null
    private var navController: NavController? = null
    private var list_btn: ImageButton? = null
    private var record_btn: ImageButton? = null
    var ratingPage = false


    @RequiresApi(api = Build.VERSION_CODES.O)
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainRecordingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        hbRecorder = HBRecorder(this, this)

        startTime = intent.getStringExtra("startTime").toString()
        bookingId = intent.getStringExtra("bookingId").toString()
        //   (RecordFragment).


          //startRecordingScreen()
        start_recording()
//        if (checkAudioPermission()) {
//            //Start Recording Method
//            RecordFragment.start_recording()
//            start_re
//            record_btn!!.setImageDrawable(resources.getDrawable(R.drawable.record_btn_recording))
//            is_recording = true
//        }
        //  hbRecorder.stopScreenRecording();
    }

    fun startRecordingScreen() {
        val mediaProjectionManager =
            getSystemService(MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
        val permissionIntent = mediaProjectionManager?.createScreenCaptureIntent()
        startActivityForResult(permissionIntent!!, SCREEN_RECORD_REQUEST_CODE)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SCREEN_RECORD_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                //Start screen recording
                hbRecorder!!.startScreenRecording(data, resultCode)
            }
        }
    }
    override fun onResume() {
        super.onResume()
        if (ratingPage) {
             // hbRecorder!!.stopScreenRecording()
            stop_recording()

            val intent = Intent(this@ScreenRecorder, Appointments::class.java)
                .putExtra("bookingId", bookingId)
                .putExtra("vale", "1")
            startActivity(intent)
            ratingPage = false
        }

    }

    private fun videoCallFun() {
        try {
            val options: JitsiMeetConferenceOptions = JitsiMeetConferenceOptions.Builder()
                .setServerURL(URL("https://meet.jit.si"))
                .setRoom(startTime)
                .setAudioMuted(false)
                .setVideoMuted(false)
                .build()
            ratingPage = true
            JitsiMeetActivity.launch(this, options)
            // refreshValue=true
            //  ratingPage = true
        } catch (e: MalformedURLException) {
            e.printStackTrace();
        }
    }

    override fun HBRecorderOnStart() {
       // videoCallFun()

    }
    override fun HBRecorderOnComplete() {
        Toast.makeText(this, "Meeting Recording Save in Gallery", Toast.LENGTH_SHORT).show()
    }

    override fun HBRecorderOnError(errorCode: Int, reason: String) {}
    override fun HBRecorderOnPause() {}
    override fun HBRecorderOnResume() {} //    // Set the output path as a Strin




    private fun start_recording() {
//        chronoTimer!!.base = SystemClock.elapsedRealtime()
//        chronoTimer!!.start()
        val rec_path =this@ScreenRecorder.getExternalFilesDir("/")!!.absolutePath
        var simpleDateFormat: SimpleDateFormat? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            simpleDateFormat = SimpleDateFormat("dd_MM_YYYY_hh_mm_ss", Locale.CANADA)
        }
        val date = Date()
        record_file = "voxRec" + simpleDateFormat!!.format(date) + ".3gp"
//        record_file_name!!.text = "Recording File Name: \n$record_file"
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

        myToast(this@ScreenRecorder,"Meeting Recording Started")
        mediaRecorder!!.start()
        videoCallFun()

    }

    private fun stop_recording() {
     //   chronoTimer!!.stop()
        is_recording = false
       // record_file_name!!.text = "Recording Stopped, File Saved: \n$record_file"
        mediaRecorder!!.stop()
        mediaRecorder!!.release()
        mediaRecorder = null
        myToast(this@ScreenRecorder,"Meeting Recording Save Successfully")

    }

    private fun checkAudioPermission(): Boolean {
        return if (ActivityCompat.checkSelfPermission(
               this@ScreenRecorder,
                recording_permission
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            true
        } else {
            ActivityCompat.requestPermissions(
                this@ScreenRecorder,
                arrayOf(recording_permission),
                AUDIO_PERMISSION_CODE
            )
            false
        }
    }

    override fun onStop() {
        super.onStop()
//        if (is_recording) {
//            stop_recording()
//        }
    }

    companion object {
        const val SCREEN_RECORD_REQUEST_CODE = 777
        private const val PERMISSION_REQ_ID_RECORD_AUDIO = 22
        private const val PERMISSION_REQ_POST_NOTIFICATIONS = 33
        const val AUDIO_PERMISSION_CODE = 89

        private const val PERMISSION_REQ_ID_WRITE_EXTERNAL_STORAGE =

            PERMISSION_REQ_ID_RECORD_AUDIO + 1
    }
}