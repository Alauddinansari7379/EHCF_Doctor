package com.example.ehcf_doctor.Appointments.Upcoming.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.ehcf.Helper.isOnline
import com.example.ehcf.Helper.myToast
import com.example.ehcf.sharedpreferences.SessionManager
import com.example.ehcf_doctor.Appointments.Appointments
import com.example.ehcf_doctor.Appointments.Upcoming.adapter.AdapterUpComing
import com.example.ehcf_doctor.Appointments.Upcoming.adapter.AdapterUpComing.Companion.CompanionCoustmorName
import com.example.ehcf_doctor.Appointments.Upcoming.adapter.AdapterUpComingAccepted
import com.example.ehcf_doctor.Appointments.Upcoming.model.ModelConfirmSlotRes
import com.example.ehcf_doctor.Booking.model.ModelGetConsultation
import com.example.ehcf_doctor.Booking.model.ResultUpcoming
import com.example.ehcf_doctor.Helper.AppProgressBar
import com.example.ehcf_doctor.Prescription.activity.AddPrescription
import com.example.ehcf_doctor.R
import com.example.ehcf_doctor.databinding.FragmentUpComingBinding
import com.example.ehcf_doctor.Retrofit.ApiClient
import com.facebook.shimmer.ShimmerFrameLayout
import com.hbisoft.hbrecorder.HBRecorder
import com.hbisoft.hbrecorder.HBRecorderListener
import org.jitsi.meet.sdk.JitsiMeetActivity
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions
import org.jitsi.meet.sdk.JitsiMeetUserInfo
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import rezwan.pstu.cse12.youtubeonlinestatus.recievers.NetworkChangeReceiver
import java.io.IOException
import java.net.MalformedURLException
import java.net.URL
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


class UpComingFragment : Fragment(), AdapterUpComing.ConfirmSlot,
    AdapterUpComingAccepted.ConfirmSlot, HBRecorderListener {
    var hbRecorder: HBRecorder? = null
    private val SCREEN_RECORD_REQUEST_CODE = 777
    private val PERMISSION_REQ_ID_RECORD_AUDIO = 22
    private var is_recording = false
    private val recording_permission = Manifest.permission.RECORD_AUDIO
    private var mediaRecorder: MediaRecorder? = null
    private var record_file: String? = "file"
    private var chronoTimer: Chronometer? = null
    private var record_file_name: TextView? = null
    private var navController: NavController? = null
    private var list_btn: ImageButton? = null
    private var record_btn: ImageButton? = null
    private val PERMISSION_REQ_POST_NOTIFICATIONS = 33
    private val RESULT_OK = 100
    private val MY_PERMISSIONS_RECORD_AUDIO = 1

    private val PERMISSION_REQ_ID_WRITE_EXTERNAL_STORAGE = PERMISSION_REQ_ID_RECORD_AUDIO + 1
    private val hasPermissions = false
    private var count = 0
    private var count2 = 0
    private var count3 = 0
    private var count4 = 0
    private var count5 = 0

    private lateinit var binding: FragmentUpComingBinding
    private lateinit var sessionManager: SessionManager
    var mydilaog: Dialog? = null
    var dialog: Dialog? = null
    private var currentTime = ""
    var endTime = ""
    var hours = ""
    var minutes = ""
    val RECORD_AUDIO_PERMISSION_REQUEST_CODE = 1

    var secondsNew = ""
    private var diffTime = 0L
    private var diffTimeSeconds = 0L
    var bookingId = ""
    var d1: Date? = null
    var d2: Date? = null
    var ratingPage = false
    var shimmerFrameLayout: ShimmerFrameLayout? = null
    var refreshValue = false
    private var tvTimeCounter: TextView? = null
    private var mainData = ArrayList<ResultUpcoming>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            com.example.ehcf_doctor.R.layout.fragment_up_coming,
            container,
            false
        )
    }

    @SuppressLint("LogNotTimber")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentUpComingBinding.bind(view)
        sessionManager = SessionManager(requireContext())
        shimmerFrameLayout = view.findViewById(R.id.shimmer)
        shimmerFrameLayout!!.startShimmer();
        mainData = ArrayList<ResultUpcoming>()
        hbRecorder = HBRecorder(requireContext(), this)
        apiCallGetConsultationAccepted()
        binding.imgRefresh.setOnClickListener {
            apiCallGetConsultationAccepted()
        }
        binding.edtSearch.addTextChangedListener { str ->
            setRecyclerViewAdapter(mainData.filter {
                it.customer_name!!.contains(str.toString(), ignoreCase = true)
            } as ArrayList<ResultUpcoming>)
        }
    }

    private fun stop_recording() {
        //   chronoTimer!!.stop()
        is_recording = false
        // record_file_name!!.text = "Recording Stopped, File Saved: \n$record_file"
        mediaRecorder!!.stop()
        mediaRecorder!!.release()
        mediaRecorder = null
        myToast(requireActivity(), "Recording Stopped")
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
//        if (is_recording) {
//           // stop_recording()
//        }
    }

    companion object {
        const val AUDIO_PERMISSION_CODE = 89
    }

    override fun onResume() {
        super.onResume()
        if (ratingPage) {
            if (is_recording) {
                stop_recording()

            }
            completeSlot(bookingId)
        }

    }

    private fun remainingTime() {
        val format = SimpleDateFormat("yy/MM/dd HH:m:ss")

        try {
            d1 = format.parse(currentTime)
            d2 = format.parse(endTime)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        diffTime = (d2!!.time - d1!!.time)
        diffTimeSeconds = TimeUnit.MILLISECONDS.toSeconds(diffTime)

        Log.e("minnew1", diffTimeSeconds.toString())

    }

    override fun popupRemainingTime(startTime: String) {
        var view = layoutInflater.inflate(R.layout.time_dialognew, null)
        dialog = Dialog(requireContext())
        endTime = startTime
        val btnOkDialog = view!!.findViewById<Button>(R.id.btnOkDialogNew)
        val hour = view.findViewById<TextView>(R.id.tvHourTime)
        val minute = view!!.findViewById<TextView>(R.id.tvMinuteTime)
        val second = view!!.findViewById<TextView>(R.id.tvSecondTime)
        dialog = Dialog(requireContext())

        currentTime = SimpleDateFormat("yy/MM/dd HH:m:ss", Locale.getDefault()).format(Date())

        if (view.parent != null) {
            (view.parent as ViewGroup).removeView(view) // <- fix
        }
        dialog!!.setContentView(view)

        dialog?.setCancelable(true)

        remainingTime()
        @SuppressLint("SuspiciousIndentation")
        fun timeCalculator(seconds: Long) {
            print(seconds)
            hours = (seconds / 3600).toInt().toString()
            minutes = (seconds % 3600 / 60).toInt().toString()
            secondsNew = (seconds % 3600 % 60).toInt().toString()

            hour.text = hours
            minute.text = minutes
            second.text = secondsNew
            if (second.text.contains("-")) {
                apiCallGetConsultationAccepted()

            }



            println("Hours: $hours")
            println("Minutes: $minutes")
            println("Seconds: $seconds")
        }

        timeCalculator(diffTimeSeconds)

        dialog?.show()
        btnOkDialog.setOnClickListener {
            dialog?.dismiss()
        }
    }

    override fun videoCall(startTime: String, bookingId: String) {
        TODO("Not yet implemented")
    }


    private fun requestAudioPermissions(startTime: String, bookingId: String, patientId: String) {

// Check if the microphone permission is granted
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Request the permission
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.RECORD_AUDIO),
                RECORD_AUDIO_PERMISSION_REQUEST_CODE
            )
        } else {
            start_recording(startTime, bookingId, patientId)
            // Permission is already granted
            // You can perform microphone related operations here
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            RECORD_AUDIO_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted, you can perform microphone related operations here
                } else {
                    requestAudioPermissions("startTime", bookingId, "")
                    // Permission denied, handle accordingly (e.g., show a message or disable microphone functionality)
                }
                return
            }
        }
    }


    private fun videoCallFun(startTime: String, bookingId: String) {
        this.bookingId = bookingId

        val jitsiMeetUserInfo = JitsiMeetUserInfo()
        jitsiMeetUserInfo.displayName = sessionManager.doctorName
        jitsiMeetUserInfo.email = sessionManager.email
        try {
            val defaultOptions: JitsiMeetConferenceOptions = JitsiMeetConferenceOptions.Builder()
                .setServerURL(URL("https://jvc.ethicalhealthcare.in/"))
                .setRoom(startTime)
                .setAudioMuted(false)
                .setVideoMuted(true)
                .setAudioOnly(false)
                .setUserInfo(jitsiMeetUserInfo)
                .setConfigOverride("enableInsecureRoomNameWarning", false)
                .setFeatureFlag("readOnlyName", true)
                .setFeatureFlag("prejoinpage.enabled", false)
                // .setFeatureFlag("lobby-mode.enabled", false)
                // .setFeatureFlag("lobby-mode", false) // Disable lobby mode
                //.setFeatureFlag("chat.enabled",false)
                .setConfigOverride("requireDisplayName", true)
                .build()
            JitsiMeetActivity.launch(requireContext(), defaultOptions)
            // refreshValue=true
            ratingPage = true
        } catch (e: MalformedURLException) {
            e.printStackTrace();
        }
    }

    private fun apiCallGetConsultationAccepted() {
        AppProgressBar.showLoaderDialog(context)
        ApiClient.apiService.getConsultation(sessionManager.id.toString(), "accepted")
            .enqueue(object : Callback<ModelGetConsultation> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelGetConsultation>, response: Response<ModelGetConsultation>
                ) {
                    try {
                        if (response.code() == 200) {
                            mainData = response.body()!!.result!!

                        }
                        if (response.code() == 500) {
                            myToast(requireActivity(), "Server error")
                            AppProgressBar.hideLoaderDialog()

                        } else if (response.body()!!.result.isEmpty()) {
                            binding.tvNoDataFound.visibility = View.VISIBLE
                            binding.shimmer.visibility = View.GONE
                            // myToast(requireActivity(),"No Data Found")
                            AppProgressBar.hideLoaderDialog()
                        } else {
                            binding.rvUpcoming.apply {
                                setRecyclerViewAdapter(mainData)
                                AppProgressBar.hideLoaderDialog()

                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        activity?.let { myToast(it, "Something went wrong") }
                        binding.shimmer.visibility = View.GONE
                        AppProgressBar.hideLoaderDialog()
                    }

                }

                override fun onFailure(call: Call<ModelGetConsultation>, t: Throwable) {
                    activity?.let { myToast(it, "Something went wrong") }
                    binding.shimmer.visibility = View.GONE
                    AppProgressBar.hideLoaderDialog()

                }

            })
    }

    private fun apiCallNotifyToPatient(patientId: String) {

        ApiClient.apiService.notify(patientId)
            .enqueue(object : Callback<ModelGetConsultation> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelGetConsultation>, response: Response<ModelGetConsultation>
                ) {
                    try {
                        if (response.code() == 200) {
                            count4 = 0
                        }
                        if (response.code() == 500) {
                            myToast(requireActivity(), "Server error")

                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                }

                override fun onFailure(call: Call<ModelGetConsultation>, t: Throwable) {
                    count4++
                    if (count4 <= 3) {
                        apiCallNotifyToPatient(patientId)
                    } else {
                        activity?.let { myToast(it, "Something went wrong") }
                    }

                }

            })
    }

    private fun setRecyclerViewAdapter(data: ArrayList<ResultUpcoming>) {
        binding.rvUpcoming.apply {
            shimmerFrameLayout?.startShimmer()
            binding.rvUpcoming.visibility = View.VISIBLE
            binding.shimmer.visibility = View.GONE
            binding.tvNoDataFound.visibility = View.GONE

            adapter = AdapterUpComing(requireContext(), data, this@UpComingFragment)
            AppProgressBar.hideLoaderDialog()

        }
    }


    private fun apiCallGetConsultationAccepted1() {

        ApiClient.apiService.getConsultation(sessionManager.id.toString(), "accepted")
            .enqueue(object : Callback<ModelGetConsultation> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelGetConsultation>, response: Response<ModelGetConsultation>
                ) {
                    try {
                        if (response.code() == 200) {
                            count5 = 0
                            mainData = response.body()!!.result!!

                        }
                        if (response.code() == 500) {
                            myToast(requireActivity(), "Server error")
                            AppProgressBar.hideLoaderDialog()

                        } else if (response.body()!!.result.isEmpty()) {
                            binding.tvNoDataFound.visibility = View.VISIBLE
                            binding.shimmer.visibility = View.GONE
                            val intent = Intent(context as Activity, Appointments::class.java)
                            (context as Activity).startActivity(intent)
                            // myToast(requireActivity(),"No Data Found")
                        } else {
                            binding.rvUpcoming.apply {
                                setRecyclerViewAdapter(mainData)
                                AppProgressBar.hideLoaderDialog()
                                val intent = Intent(context as Activity, Appointments::class.java)
                                (context as Activity).startActivity(intent)
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        activity?.let { myToast(it, "Something went wrong") }
                        binding.shimmer.visibility = View.GONE
                        AppProgressBar.hideLoaderDialog()
                    }
                }

                override fun onFailure(call: Call<ModelGetConsultation>, t: Throwable) {
                    count5++
                    if (count5 <= 3) {
                        completeSlot(bookingId)
                    } else {
                        activity?.let { myToast(it, "Something went wrong") }
                    }
                    AppProgressBar.hideLoaderDialog()

                }

            })
    }

    fun printDifference(startDate: Date, endDate: Date) {

        //milliseconds
        var different = endDate.time - startDate.time
        println("startDate : $startDate")
        println("endDate : $endDate")
        println("different : $different")
        val secondsInMilli: Long = 1000
        val minutesInMilli = secondsInMilli * 60
        val hoursInMilli = minutesInMilli * 60
        val daysInMilli = hoursInMilli * 24
        val elapsedDays = different / daysInMilli
        different %= daysInMilli
        val elapsedHours = different / hoursInMilli
        different %= hoursInMilli
        val elapsedMinutes = different / minutesInMilli
        different %= minutesInMilli
        val elapsedSeconds = different / secondsInMilli
        System.out.printf(
            "%d days, %d hours, %d minutes, %d seconds%n",
            elapsedDays,
            elapsedHours,
            elapsedMinutes,
            elapsedSeconds
        )
    }

    private fun recordMeeting(startTime: String, bookingId: String) {
//    SweetAlertDialog(requireContext(), SweetAlertDialog.WARNING_TYPE)
//        .setTitleText("Are you sure want to Record Meeting?")
//        .setCancelText("No")
//        .setConfirmText("Yes")
//        .showCancelButton(true)
//        .setConfirmClickListener { sDialog ->
//            sDialog.cancel()

//    val intent = Intent(requireContext(), ScreenRecorder::class.java)
//
//                startActivity(intent)


        val intent = Intent(context as Activity, ScreenRecorder::class.java)
            .putExtra("startTime", startTime)
            .putExtra("bookingId", bookingId)
        (context as Activity).startActivity(intent)

    }

    private fun start_recording(startTime: String, bookingId: String, patientId: String) {
//        chronoTimer!!.base = SystemClock.elapsedRealtime()
//        chronoTimer!!.start()
        val rec_path = requireActivity().getExternalFilesDir("/")!!.absolutePath
        var simpleDateFormat: SimpleDateFormat? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            simpleDateFormat = SimpleDateFormat("dd_MM_YYYY_hh_mm_ss", Locale.CANADA)
        }
        val date = Date()
        record_file = CompanionCoustmorName + " " + simpleDateFormat!!.format(date) + ".3gp"
//        record_file_name!!.text = "Recording File Name: \n$record_file"
        mediaRecorder = MediaRecorder()
        mediaRecorder!!.setAudioSource(MediaRecorder.AudioSource.MIC)
        mediaRecorder!!.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
        mediaRecorder!!.setOutputFile("$rec_path/$record_file")
        mediaRecorder!!.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)

        // Log.e("AudiorFile",mediaRecorder!!.setOutputFile("$rec_path/$record_file").toString())
        try {
            mediaRecorder!!.prepare()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        mediaRecorder!!.start()
        is_recording = true
        myToast(requireActivity(), "Meeting Recording Started")
        apiCallNotifyToPatient(patientId)
        videoCallFun(startTime, bookingId)

    }

    @SuppressLint("LogNotTimber")

    override fun alretDilogConfirm(bookingId: String, slug: String) {
        SweetAlertDialog(requireContext(), SweetAlertDialog.WARNING_TYPE)
            .setTitleText("Are you sure want to Confirm?")
            .setCancelText("No")
            .setConfirmText("Yes")
            .showCancelButton(true)
            .setConfirmClickListener { sDialog ->
                sDialog.cancel()

                confirmSlot(bookingId, slug)
            }
            .setCancelClickListener { sDialog ->
                sDialog.cancel()
            }
            .show()
    }

    override fun videoCall(startTime: String, bookingId: String, patientId: String) {
        SweetAlertDialog(requireContext(), SweetAlertDialog.WARNING_TYPE)
            //  .setTitleText("Are you sure want to Start Meeting?")
            .setTitleText("Are you want to Start Meeting?")
            .setCancelText("No")
            .setConfirmText("Yes")
            .showCancelButton(true)
            .setConfirmClickListener { sDialog ->
                sDialog.cancel()
                requestAudioPermissions(startTime, bookingId, patientId)

            }
            .setCancelClickListener { sDialog ->
                sDialog.cancel()
                // videoCallFun(startTime, bookingId)

            }
            .show()
    }

    override fun alretDilogReject(bookingId: String, slug: String) {
        SweetAlertDialog(requireContext(), SweetAlertDialog.WARNING_TYPE)
            .setTitleText("Are you sure want to Reject?")
            .setCancelText("No")
            .setConfirmText("Yes")
            .showCancelButton(true)
            .setConfirmClickListener { sDialog ->
                sDialog.cancel()
                rejectSlot(bookingId, slug)
            }
            .setCancelClickListener { sDialog ->
                sDialog.cancel()
            }
            .show()
    }

    override fun alretDilogCompleted(bookingId: String) {
        SweetAlertDialog(requireContext(), SweetAlertDialog.WARNING_TYPE)
            .setTitleText("Are you sure want to Completed?")
            .setCancelText("No")
            .setConfirmText("Yes")
            .showCancelButton(true)
            .setConfirmClickListener { sDialog ->
                sDialog.cancel()

                completeSlot(bookingId)
//                apiCallGetConsultationWating()
//                apiCallGetConsultationAccepted()
            }
            .setCancelClickListener { sDialog ->
                sDialog.cancel()
            }
            .show()
    }

    private fun confirmSlot(bookingId: String, slug: String) {
        AppProgressBar.showLoaderDialog(context)
        ApiClient.apiService.confirmSlot(bookingId, slug)
            .enqueue(object : Callback<ModelConfirmSlotRes> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelConfirmSlotRes>,
                    response: Response<ModelConfirmSlotRes>
                ) {
                    try {
                        if (response.code() == 200) {
                            count = 0
                            myToast(requireActivity(), response.body()!!.message)
                            AppProgressBar.hideLoaderDialog()
                            //  apiCallGetConsultationAccepted1()
                            (activity as Appointments).refresh()
                            //  startActivity(Intent(requireContext(),UpComingFragment::class.java))

                            //    apiCall()
                        } else if (response.code() == 500) {
                            activity?.let { myToast(it, "Server Error") }
                            AppProgressBar.hideLoaderDialog()

                        } else {
                            activity?.let { myToast(it, "Something went wrong") }
                            AppProgressBar.hideLoaderDialog()
                        }
                    } catch (e: Exception) {
                        activity?.let { myToast(it, "Something went wrong") }
                        binding.shimmer.visibility = View.GONE
                        AppProgressBar.hideLoaderDialog()
                    }
                }

                override fun onFailure(call: Call<ModelConfirmSlotRes>, t: Throwable) {
                    count++
                    if (count <= 3) {
                        confirmSlot(bookingId, slug)
                    } else {
                        activity?.let { myToast(it, "Something went wrong") }
                    }
                    AppProgressBar.hideLoaderDialog()

                }

            })
    }

    private fun rejectSlot(bookingId: String, slug: String) {
        AppProgressBar.showLoaderDialog(context)
        ApiClient.apiService.confirmSlot(bookingId, slug)
            .enqueue(object : Callback<ModelConfirmSlotRes> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelConfirmSlotRes>,
                    response: Response<ModelConfirmSlotRes>
                ) {
                    try {
                        if (response.code() == 200) {
                            count2 = 0
                            activity?.let { myToast(it, response.body()!!.message) }
                            AppProgressBar.hideLoaderDialog()
                            apiCallGetConsultationAccepted1()
                            (activity as Appointments).refresh()

                            //  startActivity(Intent(requireContext(),UpComingFragment::class.java))
                            //  apiCall()
                            AppProgressBar.hideLoaderDialog()
                        } else if (response.code() == 500) {
                            activity?.let { myToast(it, "Server Error") }
                            AppProgressBar.hideLoaderDialog()

                        } else {
                            activity?.let { myToast(it, response.body()!!.message) }
                            AppProgressBar.hideLoaderDialog()
                        }
                    } catch (e: java.lang.Exception) {
                        activity?.let { myToast(it, "Something went wrong") }
                        binding.shimmer.visibility = View.GONE
                        AppProgressBar.hideLoaderDialog()
                    }
                }

                override fun onFailure(call: Call<ModelConfirmSlotRes>, t: Throwable) {
                    count2++
                    if (count2 <= 3) {
                        rejectSlot(bookingId, slug)
                    } else {
                        activity?.let { myToast(it, "Something went wrong") }
                    }
                    AppProgressBar.hideLoaderDialog()


                }

            })
    }

    private fun completeSlot(bookingId: String) {
        AppProgressBar.showLoaderDialog(context)
//        progressDialog!!.show()
        val slug = "completed"
        ApiClient.apiService.confirmSlot(bookingId, slug)
            .enqueue(object : Callback<ModelConfirmSlotRes> {
                @SuppressLint("LogNotTimber", "SuspiciousIndentation")
                override fun onResponse(
                    call: Call<ModelConfirmSlotRes>,
                    response: Response<ModelConfirmSlotRes>
                ) {
                    try {
                        if (response.code() == 500) {
                            activity?.let { myToast(it, "Server Error") }
                            AppProgressBar.hideLoaderDialog()

                        } else if (response.body()!!.status == 1) {
                            count3 = 0
                            // apiCallGetConsultationAccepted1()
                            // (activity as Appointments).refresh()
                            (activity as Appointments).refresh()
                            val intent = Intent(context as Activity, AddPrescription::class.java)
                                .putExtra("bookingId", bookingId)
                            (context as Activity).startActivity(intent)
                            ratingPage = false
                            myToast(requireActivity(), response.body()!!.message)
                            AppProgressBar.hideLoaderDialog()
                            //  apiCall()

                        } else {
                            activity?.let { myToast(it, response.body()!!.message) }
                            AppProgressBar.hideLoaderDialog()
                        }
                    } catch (e: Exception) {
                        activity?.let { myToast(it, "Something went wrong") }
                        AppProgressBar.hideLoaderDialog()
                    }
                }

                override fun onFailure(call: Call<ModelConfirmSlotRes>, t: Throwable) {
                    AppProgressBar.hideLoaderDialog()
                    count3++
                    if (count3 <= 3) {
                        completeSlot(bookingId)
                    } else {
                        activity?.let { myToast(it, "Something went wrong") }
                    }
                    AppProgressBar.hideLoaderDialog()

                }

            })
    }


    override fun onStart() {
        super.onStart()

        if (isOnline(requireContext())) {
            //  myToast(requireActivity(), "Connected")
        } else {
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


    override fun HBRecorderOnStart() {
        TODO("Not yet implemented")
    }

    override fun HBRecorderOnComplete() {
        TODO("Not yet implemented")
    }

    override fun HBRecorderOnError(errorCode: Int, reason: String?) {
        TODO("Not yet implemented")
    }

    override fun HBRecorderOnPause() {
        TODO("Not yet implemented")
    }

    override fun HBRecorderOnResume() {
        TODO("Not yet implemented")
    }

}