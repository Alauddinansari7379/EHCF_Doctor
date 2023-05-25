package com.example.ehcf_doctor.Appointments.Upcoming.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.media.projection.MediaProjectionManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.ehcf.Helper.myToast
import com.example.ehcf_doctor.Appointments.Appointments
import com.example.ehcf_doctor.Appointments.Upcoming.model.ModelConfirmSlotRes
import com.example.myrecyview.apiclient.ApiClient
import com.hbisoft.hbrecorder.HBRecorder
import com.hbisoft.hbrecorder.HBRecorderListener
import org.jitsi.meet.sdk.JitsiMeetActivity
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.MalformedURLException
import java.net.URL
import java.util.*

class ScreenRecorder : AppCompatActivity(), HBRecorderListener {
    private val calendar = Calendar.getInstance()
    var hbRecorder: HBRecorder? = null
    var startTime=""
    var bookingId=""
    var ratingPage = false


    @RequiresApi(api = Build.VERSION_CODES.O)
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hbRecorder = HBRecorder(this, this)

        startTime = intent.getStringExtra("startTime").toString()
        bookingId = intent.getStringExtra("bookingId").toString()


        startRecordingScreen()

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
              hbRecorder!!.stopScreenRecording()
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
        videoCallFun()

    }
    override fun HBRecorderOnComplete() {
        Toast.makeText(this, "Meeting Recording Save in Gallery", Toast.LENGTH_SHORT).show()
    }

    override fun HBRecorderOnError(errorCode: Int, reason: String) {}
    override fun HBRecorderOnPause() {}
    override fun HBRecorderOnResume() {} //    // Set the output path as a String

    //// Only use this on devices running Android 9 and lower or you have to add android:requestLegacyExternalStorage="true" in your manifest
    //// Defaults to - Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES)
    //hbrecorder.setOutputPath(String);
    //// Set output Uri
    //// Only use this on devices running Android 10>
    //// When setting a Uri ensure you pass the same name to HBRecorder as what you set in ContentValues (DISPLAY_NAME and TITLE)
    //hbRecorder.setOutputUri(Uri);
    //// Set file name as String
    //// Defaults to - quality + time stamp. For example HD-2019-08-14-10-09-58.mp4
    //hbrecorder.setFileName(String);
    //// Set audio bitrate as int
    //// Defaults to - 128000
    //hbrecorder.setAudioBitrate(int);
    //// Set audio sample rate as int
    //// Defaults to - 44100
    //hbrecorder.setAudioSamplingRate(int);
    //// Enable/Disable audio
    //// Defaults to true
    //hbrecorder.isAudioEnabled(boolean);
    //// Enable/Disable HD Video
    //// Defaults to true
    //hbrecorder.recordHDVideo(boolean);
    //// Get file path as String
    //hbrecorder.getFilePath();
    //// Get file name as String
    //hbrecorder.getFileName();
    //// Start recording screen by passing it as Intent inside onActivityResult
    //hbrecorder.startScreenRecording(Intent);
    //// Pause screen recording (only available for devices running 24>)
    //hbrecorder.pauseScreenRecording();
    //// Resume screen recording
    //hbreccorder.resumeScreenRecording();
    //// Stop screen recording
    //hbrecorder.stopScreenRecording();
    //// Check if recording is in progress
    //hbrecorder.isBusyRecording();
    //// Set notification icon by passing, for example R.drawable.myicon
    //// Defaults to R.drawable.icon
    //hbrecorder.setNotificationSmallIcon(int);
    //// Set notification icon using byte array
    //hbrecorder.setNotificationSmallIcon(byte[]);
    //// Set notification icon using vector drawable
    //hbRecorder.setNotificationSmallIconVector(vector);
    //// Set notification title
    //// Defaults to "Recording your screen"
    //hbrecorder.setNotificationTitle(String);
    //// Set notification description
    //// Defaults to "Drag down to stop the recording"
    //hbrecorder.setNotificationDescription(String);
    //// Set notification stop button text
    //// Defaults to "STOP RECORDING"
    //hbrecorder.setNotificationButtonText(String);
    //// Set output orientation (in degrees)
    //hbrecorder.setOrientationHint(int);
    //// Set max output file size
    //hbrecorder.setMaxFileSize(long);
    //// Set max time (in seconds)
    //hbRecorder.setMaxDuration(int);
    companion object {
        const val SCREEN_RECORD_REQUEST_CODE = 777
        private const val PERMISSION_REQ_ID_RECORD_AUDIO = 22
        private const val PERMISSION_REQ_POST_NOTIFICATIONS = 33
        private const val PERMISSION_REQ_ID_WRITE_EXTERNAL_STORAGE =
            PERMISSION_REQ_ID_RECORD_AUDIO + 1
    }
}