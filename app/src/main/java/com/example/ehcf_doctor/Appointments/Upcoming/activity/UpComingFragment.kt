package com.example.ehcf_doctor.Appointments.Upcoming.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.media.projection.MediaProjectionManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.ehcf.Helper.isOnline
import com.example.ehcf.Helper.myToast
import com.example.ehcf.sharedpreferences.SessionManager
import com.example.ehcf_doctor.Appointments.Appointments
import com.example.ehcf_doctor.Appointments.Upcoming.adapter.AdapterUpComing
import com.example.ehcf_doctor.Appointments.Upcoming.adapter.AdapterUpComingAccepted
import com.example.ehcf_doctor.Appointments.Upcoming.model.ModelConfirmSlotRes
import com.example.ehcf_doctor.Booking.model.ModelGetConsultation
import com.example.ehcf_doctor.R
import com.example.ehcf_doctor.databinding.FragmentUpComingBinding
import com.example.myrecyview.apiclient.ApiClient
import com.facebook.shimmer.ShimmerFrameLayout
import com.hbisoft.hbrecorder.HBRecorder
import com.hbisoft.hbrecorder.HBRecorderListener
import org.jitsi.meet.sdk.JitsiMeetActivity
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import rezwan.pstu.cse12.youtubeonlinestatus.recievers.NetworkChangeReceiver
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
    private val PERMISSION_REQ_POST_NOTIFICATIONS = 33
    private val RESULT_OK = 100
    private val PERMISSION_REQ_ID_WRITE_EXTERNAL_STORAGE = PERMISSION_REQ_ID_RECORD_AUDIO + 1
    private val hasPermissions = false

    private lateinit var binding: FragmentUpComingBinding
    private lateinit var sessionManager: SessionManager
    var mydilaog: Dialog? = null
    var progressDialog: ProgressDialog? = null
    var dialog: Dialog? = null
    private var currentTime = ""
    var endTime = ""
    var hours = ""
    var minutes = ""
    var secondsNew = ""
    private var diffTime = 0L
    private var diffTimeSeconds = 0L
    var bookingId = ""
    var d1: Date? = null
    var d2: Date? = null
     var ratingPage = false
    var shimmerFrameLayout: ShimmerFrameLayout? = null

    // var mergeAdapter =RecyclerViewMergeAdapter()
    var refreshValue = false


    private var tvTimeCounter: TextView? = null

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

        hbRecorder = HBRecorder(requireContext(), this)



        //apiCall()
        //  apiCallGetConsultationWating()
        apiCallGetConsultationAccepted()
        binding.imgRefresh.setOnClickListener {
            apiCallGetConsultationAccepted()
        }

        binding.imgSearch.setOnClickListener {
            if (binding.edtSearch.text.toString().isEmpty()) {
                binding.edtSearch.error = "Enter Patient Name"
                binding.edtSearch.requestFocus()
            } else {
               val search = binding.edtSearch.text.toString()
                apiCallSearchAppointments(search)
            }
        }
    }
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == ScreenRecorder.SCREEN_RECORD_REQUEST_CODE) {
//            if (resultCode == AppCompatActivity.RESULT_OK) {
//                //Start screen recording
//                hbRecorder!!.startScreenRecording(data, resultCode)
//            }
//        }
//    }

    override fun onResume() {
        super.onResume()
        if (ratingPage) {
            completeSlot(bookingId)
          //  hbRecorder!!.stopScreenRecording()
            //  val intent = Intent(context as Activity, RatingNew::class.java)
           // apiCallGetConsultationAccepted1()
         //   (activity as Appointments).refresh()

            //.putExtra("meetingId", meetingId)
            //  (context as Activity).startActivity(intent)
            // startActivity(Intent(requireContext(),Rating::class.java))
            ratingPage = false
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
        val hour = view!!.findViewById<TextView>(R.id.tvHourTime)
        val minute = view!!.findViewById<TextView>(R.id.tvMinuteTime)
        val second = view!!.findViewById<TextView>(R.id.tvSecondTime)
        dialog = Dialog(requireContext())

        currentTime = SimpleDateFormat("yy/MM/dd HH:m:ss", Locale.getDefault()).format(Date())

        if (view.parent != null) {
            (view.parent as ViewGroup).removeView(view) // <- fix
        }
        dialog!!.setContentView(view)
        dialog?.setCancelable(true)
        // dialog?.setContentView(view)
        // val d1 = format.parse("2023/03/29 11:04:00")
//        Log.e("currentDate", currentTime)
//        Log.e("EndTime", startTime)

        remainingTime()
        fun timeCalculator(seconds: Long) {
            print(seconds)
            hours = (seconds / 3600).toInt().toString()
            minutes = (seconds % 3600 / 60).toInt().toString()
            secondsNew = (seconds % 3600 % 60).toInt().toString()

            hour.text = hours
            minute.text = minutes
            second.text = secondsNew

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


    private fun videoCallFun(startTime: String, bookingId: String) {
        this.bookingId = bookingId
        try {
            val options: JitsiMeetConferenceOptions = JitsiMeetConferenceOptions.Builder()
                .setServerURL(URL("https://meet.jit.si"))
                .setRoom(startTime)
                .setAudioMuted(false)
                .setVideoMuted(false)
                .build()
            ratingPage = true
            JitsiMeetActivity.launch(requireContext(), options)
            // refreshValue=true
          //  ratingPage = true
        } catch (e: MalformedURLException) {
            e.printStackTrace();
        }
    }

    private fun apiCallGetConsultationAccepted() {
        progressDialog = ProgressDialog(requireContext())
        progressDialog!!.setMessage("Loading..")
        progressDialog!!.setTitle("Please Wait")
        progressDialog!!.isIndeterminate = false
        progressDialog!!.setCancelable(true)
        progressDialog!!.show()


        ApiClient.apiService.getConsultation(sessionManager.id.toString(), "accepted")
            .enqueue(object : Callback<ModelGetConsultation> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelGetConsultation>, response: Response<ModelGetConsultation>
                ) {
                    if (response.body()!!.result.isEmpty()) {
                        binding.tvNoDataFound.visibility = View.VISIBLE
                        binding.shimmer.visibility = View.GONE
                        // myToast(requireActivity(),"No Data Found")
                        progressDialog!!.dismiss()
                    } else {
                        binding.rvUpcoming.apply {
                            shimmerFrameLayout?.startShimmer()
                            binding.rvUpcoming.visibility = View.VISIBLE
                            binding.shimmer.visibility = View.GONE
                            binding.tvNoDataFound.visibility = View.GONE
                            adapter = AdapterUpComing(
                                requireContext(),
                                response.body()!!,
                                this@UpComingFragment
                            )
                            progressDialog!!.dismiss()

                        }
                    }

                }

                override fun onFailure(call: Call<ModelGetConsultation>, t: Throwable) {
                    activity?.let { myToast(it, "Something went wrong") }
                    binding.shimmer.visibility = View.GONE
                    progressDialog!!.dismiss()

                }

            })
    }


    private fun apiCallSearchAppointments(patientName: String) {
        progressDialog = ProgressDialog(requireContext())
        progressDialog!!.setMessage("Loading..")
        progressDialog!!.setTitle("Please Wait")
        progressDialog!!.isIndeterminate = false
        progressDialog!!.setCancelable(true)
        progressDialog!!.show()


        ApiClient.apiService.searchAppointments(patientName,sessionManager.id.toString(),"accepted")
            .enqueue(object : Callback<ModelGetConsultation> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelGetConsultation>, response: Response<ModelGetConsultation>
                ) {
                    if (response.code() == 500) {
                        activity?.let { myToast(it, "Server Error") }
                        binding.shimmer.visibility = View.GONE
                    } else if (response.body()!!.status == 0) {
                        binding.tvNoDataFound.visibility = View.VISIBLE
                        binding.shimmer.visibility = View.GONE
                        binding.edtSearch.text.clear()
                        activity?.let { myToast(it, "${response.body()!!.message}") }
                        progressDialog!!.dismiss()

                    } else if (response.body()!!.result.isEmpty()) {
                        binding.rvUpcoming.adapter =
                            activity?.let { AdapterUpComing(it, response.body()!!,this@UpComingFragment) }
                        binding.rvUpcoming.adapter!!.notifyDataSetChanged()
                        binding.tvNoDataFound.visibility = View.VISIBLE
                        binding.shimmer.visibility = View.GONE
                        binding.edtSearch.text.clear()
                        activity?.let { myToast(it, "No Appointment Found") }
                        progressDialog!!.dismiss()

                    } else {
                        binding.rvUpcoming.adapter =
                            activity?.let { AdapterUpComing(it, response.body()!!,this@UpComingFragment) }
                        binding.rvUpcoming.adapter!!.notifyDataSetChanged()
                        binding.tvNoDataFound.visibility = View.GONE
                        shimmerFrameLayout?.startShimmer()
                        binding.rvUpcoming.visibility = View.VISIBLE
                        binding.shimmer.visibility = View.GONE
                        binding.edtSearch.text.clear()
                        progressDialog!!.dismiss()
//                        binding.rvManageSlot.apply {
//                            binding.tvNoDataFound.visibility = View.GONE
//                            shimmerFrameLayout?.startShimmer()
//                            binding.rvManageSlot.visibility = View.VISIBLE
//                            binding.shimmerMySlot.visibility = View.GONE
//                            // myToast(this@ShuduleTiming, response.body()!!.message)
//                            adapter = AdapterSlotsList(this@MySlot, response.body()!!, this@MySlot)
//                            progressDialog!!.dismiss()
//
//                        }
                    }
                }

                override fun onFailure(call: Call<ModelGetConsultation>, t: Throwable) {
                    activity?.let { myToast(it, "Something went wrong") }
                    binding.shimmer.visibility = View.GONE
                    progressDialog!!.dismiss()

                }

            })
    }

    private fun apiCallGetConsultationAccepted1() {



        ApiClient.apiService.getConsultation(sessionManager.id.toString(), "accepted")
            .enqueue(object : Callback<ModelGetConsultation> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelGetConsultation>, response: Response<ModelGetConsultation>
                ) {
                    if (response.body()!!.result.isEmpty()) {
                        binding.tvNoDataFound.visibility = View.VISIBLE
                        binding.shimmer.visibility = View.GONE
                        val intent = Intent(context as Activity, Appointments::class.java)
                        (context as Activity).startActivity(intent)
                    // myToast(requireActivity(),"No Data Found")
                    } else {
                        binding.rvUpcoming.apply {
                            shimmerFrameLayout?.startShimmer()
                            binding.rvUpcoming.visibility = View.VISIBLE
                            binding.shimmer.visibility = View.GONE
                            binding.tvNoDataFound.visibility = View.GONE
                            adapter = activity?.let { AdapterUpComing(it, response.body()!!, this@UpComingFragment) }
                            val intent = Intent(context as Activity, Appointments::class.java)
                            (context as Activity).startActivity(intent)
                        }
                    }

                }

                override fun onFailure(call: Call<ModelGetConsultation>, t: Throwable) {
                    activity?.let { myToast(it, "Something went wrong") }
                    binding.shimmer.visibility = View.GONE

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
private fun recordMeeting(startTime: String, bookingId: String)
{
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

//startActivity(Intent(requireContext(),ScreenRecorder::class.java))
   // videoCallFun(startTime, bookingId)

//        }
//        .setCancelClickListener { sDialog ->
//            sDialog.cancel()
//        }
//        .show()
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
//                val intent = Intent(applicationContext, SignIn::class.java)
//                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
//                finish()
//                startActivity(intent)

                confirmSlot(bookingId, slug)
//                apiCallGetConsultationWating()
//                apiCallGetConsultationAccepted()
            }
            .setCancelClickListener { sDialog ->
                sDialog.cancel()
            }
            .show()
    }

    override fun videoCall(startTime: String, bookingId: String) {
        SweetAlertDialog(requireContext(), SweetAlertDialog.WARNING_TYPE)
          //  .setTitleText("Are you sure want to Start Meeting?")
            .setTitleText("Are you want to Record Meeting?")
            .setCancelText("No")
            .setConfirmText("Yes")
            .showCancelButton(true)
            .setConfirmClickListener { sDialog ->
                sDialog.cancel()
//                val intent = Intent(applicationContext, SignIn::class.java)
//                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
//                finish()
//                startActivity(intent)
                recordMeeting(startTime,bookingId)

            }
            .setCancelClickListener { sDialog ->
                sDialog.cancel()
                videoCallFun(startTime, bookingId)

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
//                val intent = Intent(applicationContext, SignIn::class.java)
//                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
//                finish()
//                startActivity(intent)

                rejectSlot(bookingId, slug)
//                apiCallGetConsultationWating()
//                apiCallGetConsultationAccepted()
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
//                val intent = Intent(applicationContext, SignIn::class.java)
//                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
//                finish()
//                startActivity(intent)

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
        progressDialog = ProgressDialog(requireContext())
        progressDialog!!.setMessage("Loading..")
        progressDialog!!.setTitle("Please Wait")
        progressDialog!!.isIndeterminate = false
        progressDialog!!.setCancelable(true)
        progressDialog!!.show()

        ApiClient.apiService.confirmSlot(bookingId, slug)
            .enqueue(object : Callback<ModelConfirmSlotRes> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelConfirmSlotRes>,
                    response: Response<ModelConfirmSlotRes>
                ) {
                    if (response.code() == 200) {
                        myToast(requireActivity(), response.body()!!.message)
                        progressDialog!!.dismiss()
                      //  apiCallGetConsultationAccepted1()
                        (activity as Appointments).refresh()
                        //  startActivity(Intent(requireContext(),UpComingFragment::class.java))

                        //    apiCall()
                    } else if (response.code() == 500) {
                        activity?.let { myToast(it, "Server Error") }
                        progressDialog!!.dismiss()

                    } else {
                        activity?.let { myToast(it, "Something went wrong") }
                        progressDialog!!.dismiss()
                    }
                }

                override fun onFailure(call: Call<ModelConfirmSlotRes>, t: Throwable) {
                    activity?.let { myToast(it, "Something went wrong") }
                    progressDialog!!.dismiss()

                }

            })
    }

    private fun rejectSlot(bookingId: String, slug: String) {
        progressDialog = ProgressDialog(requireContext())
        progressDialog!!.setMessage("Loading..")
        progressDialog!!.setTitle("Please Wait")
        progressDialog!!.isIndeterminate = false
        progressDialog!!.setCancelable(true)
        progressDialog!!.show()

        ApiClient.apiService.confirmSlot(bookingId, slug)
            .enqueue(object : Callback<ModelConfirmSlotRes> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelConfirmSlotRes>,
                    response: Response<ModelConfirmSlotRes>
                ) {
                    if (response.code() == 200) {
                        activity?.let { myToast(it, response.body()!!.message) }
                        progressDialog!!.dismiss()
                        apiCallGetConsultationAccepted1()
                        (activity as Appointments).refresh()

                        //  startActivity(Intent(requireContext(),UpComingFragment::class.java))
                        //  apiCall()
                        progressDialog!!.dismiss()
                    } else if (response.code() == 500) {
                        activity?.let { myToast(it, "Server Error") }
                        progressDialog!!.dismiss()

                    } else {
                        activity?.let { myToast(it, response.body()!!.message) }
                        progressDialog!!.dismiss()
                    }
                }

                override fun onFailure(call: Call<ModelConfirmSlotRes>, t: Throwable) {
                    activity?.let { myToast(it, "Something went wrong") }
                    progressDialog!!.dismiss()

                }

            })
    }

    private fun completeSlot(bookingId: String) {
        progressDialog = ProgressDialog(requireContext())
        progressDialog!!.setMessage("Loading..")
        progressDialog!!.setTitle("Please Wait")
        progressDialog!!.isIndeterminate = false
        progressDialog!!.setCancelable(true)
//        progressDialog!!.show()
        val slug = "completed"
        ApiClient.apiService.confirmSlot(bookingId, slug)
            .enqueue(object : Callback<ModelConfirmSlotRes> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelConfirmSlotRes>,
                    response: Response<ModelConfirmSlotRes>
                ) {
                    if (response.code() == 500) {
                        activity?.let { myToast(it, "Server Error") }
                        progressDialog!!.dismiss()

                    } else if (response.body()!!.status == 1) {
                        apiCallGetConsultationAccepted1()
//                        (activity as Appointments).refresh()

                        //  myToast(requireActivity(),response.body()!!.message)
                        progressDialog!!.dismiss()
                        //  apiCall()

                    } else {
                        activity?.let { myToast(it, response.body()!!.message) }
                        progressDialog!!.dismiss()
                    }
                }

                override fun onFailure(call: Call<ModelConfirmSlotRes>, t: Throwable) {
                    activity?.let { myToast(it, "Something went wrong") }
                    progressDialog!!.dismiss()

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