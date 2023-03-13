package com.example.ehcf_doctor.Appointments.Upcoming.activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.app.ProgressDialog
import android.os.Bundle
import android.support.wearable.view.RecyclerViewMergeAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.ehcf.Helper.isOnline
import com.example.ehcf.Helper.myToast
import com.example.ehcf.sharedpreferences.SessionManager
import com.example.ehcf_doctor.Appointments.Upcoming.adapter.AdapterUpComing
import com.example.ehcf_doctor.Appointments.Upcoming.adapter.AdapterUpComingAccepted
import com.example.ehcf_doctor.Appointments.Upcoming.model.ModelConfirmSlotRes
import com.example.ehcf_doctor.Appointments.Upcoming.model.ModelUpComingResponse
import com.example.ehcf_doctor.Booking.model.ModelGetConsultation
import com.example.ehcf_doctor.Retrofit.ApiInterface
import com.example.ehcf_doctor.databinding.FragmentUpComingBinding
import com.example.myrecyview.apiclient.ApiClient
import org.jitsi.meet.sdk.JitsiMeetActivity
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import rezwan.pstu.cse12.youtubeonlinestatus.recievers.NetworkChangeReceiver
import java.net.MalformedURLException
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class UpComingFragment : Fragment(),AdapterUpComing.ConfirmSlot,AdapterUpComingAccepted.ConfirmSlot {
    private lateinit var binding:FragmentUpComingBinding
    private lateinit var sessionManager: SessionManager
    var mydilaog: Dialog? = null
    var progressDialog : ProgressDialog?=null
    var dialog: Dialog?= null
    var currentTime=""
    var mergeAdapter =RecyclerViewMergeAdapter()
    var refreshValue=false


    private var tvTimeCounter: TextView?=null

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            return inflater.inflate(com.example.ehcf_doctor.R.layout.fragment_up_coming, container, false)
        }
        @SuppressLint("LogNotTimber")
        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            binding = FragmentUpComingBinding.bind(view)
            sessionManager = SessionManager(requireContext())


            //apiCall()
            apiCallGetConsultationWating()
            apiCallGetConsultationAccepted()
            binding.imgRefresh.setOnClickListener {
                //apiCall()


//                apiCallGetConsultationWating()
//                apiCallGetConsultationAccepted()


            }


    }

 private fun videoCallFun(startTime: String, bookingId: String){
     try {
         val options: JitsiMeetConferenceOptions = JitsiMeetConferenceOptions.Builder()
             .setServerURL(URL("https://meet.jit.si"))
             .setRoom(startTime)
             .setAudioMuted(false)
             .setVideoMuted(false)
             .build()
         JitsiMeetActivity.launch(requireContext(), options)
         completeSlot(bookingId)
        // refreshValue=true
     } catch (e: MalformedURLException) {
         e.printStackTrace();
     }
 }

    private fun apiCallGetConsultationCompleter() {
        progressDialog = ProgressDialog(requireContext())
        progressDialog!!.setMessage("Loading..")
        progressDialog!!.setTitle("Please Wait")
        progressDialog!!.isIndeterminate = false
        progressDialog!!.setCancelable(true)

        progressDialog!!.show()

        ApiClient.apiService.getConsultation(sessionManager.id.toString(),"completed")
            .enqueue(object : Callback<ModelGetConsultation> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelGetConsultation>, response: Response<ModelGetConsultation>
                ) {
                    if (response.body()!!.result.isEmpty()) {
                        binding.tvNoDataFound.visibility = View.VISIBLE
                        // myToast(requireActivity(),"No Data Found")
                        progressDialog!!.dismiss()

                    }else {

                    }

                }

                override fun onFailure(call: Call<ModelGetConsultation>, t: Throwable) {
                    myToast(requireActivity(), "Something went wrong")
                    progressDialog!!.dismiss()

                }

            })
    }


    private fun timeCalculater(){
        val dateStart = "01/14/2012 09:29:58"
        val dateStop = "01/15/2012 10:31:48"
        val format = SimpleDateFormat("MM/dd/yyyy HH:mm:ss")
        var d1: Date? = null
        var d2: Date? = null

        try {
            d1 = format.parse(dateStart)
            d2 = format.parse(dateStop)

            //in milliseconds

            //in milliseconds
            val diff = d2.getTime() - d1.getTime()

            val diffSeconds = diff / 1000 % 60
            val diffMinutes = diff / (60 * 1000) % 60
            val diffHours = diff / (60 * 60 * 1000) % 24
            val diffDays = diff / (24 * 60 * 60 * 1000)

            print("$diffDays days, ")
            print("$diffHours hours, ")
            print("$diffMinutes minutes, ")
            print("$diffSeconds seconds.")

        }
        finally {
            myToast(requireActivity(),"Finelly")
        }

    }
/*
    private fun apiCallGetConsultation() {
        progressDialog = ProgressDialog(requireContext())
        progressDialog!!.setMessage("Loading..")
        progressDialog!!.setTitle("Please Wait")
        progressDialog!!.isIndeterminate = false
        progressDialog!!.setCancelable(true)

        progressDialog!!.show()

        ApiClient.apiService.getConsultation(sessionManager.id.toString(),"waiting_for_accept")
            .enqueue(object : Callback<ModelGetConsultation> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelGetConsultation>, response: Response<ModelGetConsultation>
                ) {
                    if (response.body()!!.result.isEmpty()) {
                        binding.tvNoDataFound.visibility = View.VISIBLE
                        progressDialog!!.dismiss()


                    } else {
                        binding.rvUpcoming.apply {

                            ApiClient.apiService.getConsultation(sessionManager.id.toString(),"accepted")
                                .enqueue(object : Callback<ModelGetConsultation> {
                                    @SuppressLint("LogNotTimber")
                                    override fun onResponse(
                                        call: Call<ModelGetConsultation>, response: Response<ModelGetConsultation>
                                    ) {
                                        if (response.body()!!.result.isEmpty()) {
                                            // myToast(requireActivity(),"No Data Found")
                                            progressDialog!!.dismiss()

                                        } else {
                                            binding.rvUpcomingAccpted.apply {
                                               // adapter = AdapterUpComingAccepted(requireContext(), response.body()!!)

                                                val subAdapter2 = AdapterUpComingAccepted(requireContext(), response.body()!!)

                                                mergeAdapter.addAdapter(subAdapter2)
                                                progressDialog!!.dismiss()

                                            }
                                        }

                                    }

                                    override fun onFailure(call: Call<ModelGetConsultation>, t: Throwable) {
                                        myToast(requireActivity(), "Something went wrong")
                                        progressDialog!!.dismiss()

                                    }

                                })

                            val subAdapter1 = AdapterUpComing(requireContext(), response.body()!!, this@UpComingFragment)

                            mergeAdapter.addAdapter(subAdapter1)


                            binding.tvNoDataFound.visibility = View.GONE
                            adapter = mergeAdapter
                            progressDialog!!.dismiss()

                        }
                    }

                }

                override fun onFailure(call: Call<ModelGetConsultation>, t: Throwable) {
                    myToast(requireActivity(), "Something went wrong")
                    progressDialog!!.dismiss()

                }

            })
    }
*/
    private fun apiCallGetConsultationWating() {
    //mergeAdapter?.addAdapter(null)

        ApiClient.apiService.getConsultation(sessionManager.id.toString(),"waiting_for_accept")
            .enqueue(object : Callback<ModelGetConsultation> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelGetConsultation>, response: Response<ModelGetConsultation>
                ) {
                    if (response.body()!!.result.isEmpty()) {
                        // myToast(requireActivity(),"No Data Found")
                        progressDialog!!.dismiss()

                    } else {
                    //    mergeAdapter.removeAdapter(AdapterUpComing(requireContext(), response.body()!!,this@UpComingFragment));

                       val adapter1 = AdapterUpComing(requireContext(), response.body()!!,this@UpComingFragment)

                        // Setting the Adapter with the recyclerview
                       // binding.rvUpcoming.adapter = adapter

                        mergeAdapter.addAdapter(adapter1)
//                        binding.rvUpcoming.apply {
//                            binding.tvNoDataFound.visibility = View.GONE
//                            adapter = AdapterUpComing(requireContext(), response.body()!!,this@UpComingFragment)
//                            progressDialog!!.dismiss()
//
//                        }
                    }

                }

                override fun onFailure(call: Call<ModelGetConsultation>, t: Throwable) {
                    myToast(requireActivity(), "Something went wrong")
                    progressDialog!!.dismiss()

                }

            })
    }
    private fun apiCallGetConsultationAccepted() {
        progressDialog = ProgressDialog(requireContext())
        progressDialog!!.setMessage("Loading..")
        progressDialog!!.setTitle("Please Wait")
        progressDialog!!.isIndeterminate = false
        progressDialog!!.setCancelable(true)
        progressDialog!!.show()


        ApiClient.apiService.getConsultation(sessionManager.id.toString(),"accepted")
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

                        // Setting the Adapter with the recyclerview
                      //


                        binding.rvUpcoming.apply {
//                            binding.rvUpcoming.adapter = null;

                            binding.tvNoDataFound.visibility = View.GONE
                            val adapter = AdapterUpComingAccepted(requireContext(), response.body()!!,this@UpComingFragment)

                            mergeAdapter?.addAdapter(adapter)
                            //binding.rvUpcoming.adapter = adapter

                            binding.rvUpcoming.adapter = mergeAdapter
                            progressDialog!!.dismiss()
                           // adapter = mergeAdapter
                            progressDialog!!.dismiss()

                        }
                    }

                }

                override fun onFailure(call: Call<ModelGetConsultation>, t: Throwable) {
                    myToast(requireActivity(), "Something went wrong")
                    progressDialog!!.dismiss()

                }

            })
    }

    private fun apiCall(){

        progressDialog = ProgressDialog(requireContext())
        progressDialog!!.setMessage("Loading..")
        progressDialog!!.setTitle("Please Wait")
        progressDialog!!.isIndeterminate = false
        progressDialog!!.setCancelable(true)
        progressDialog!!.show()
        val id="1"

        val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            //.baseUrl("https://jsonplaceholder.typicode.com/")
            .baseUrl("https://ehcf.thedemostore.in/api/doctor/")
            .build()
            .create(ApiInterface::class.java)




        val retrofitData = retrofitBuilder.getBooking(sessionManager.id.toString())
        retrofitData.enqueue(object : Callback<ModelUpComingResponse> {
            override fun onResponse(
                call: Call<ModelUpComingResponse>,
                response: Response<ModelUpComingResponse>
            ) {
                if (response.body()!!.result.isEmpty()) {
                    binding.tvNoDataFound.visibility = View.VISIBLE
                   // myToast(requireActivity(),"No Data Found")
                    progressDialog!!.dismiss()

                } else {
                    binding.rvUpcoming.apply {
                        binding.tvNoDataFound.visibility = View.GONE
                    //    adapter = AdapterUpComing(requireContext(), response.body()!!, this@UpComingFragment)
                        progressDialog!!.dismiss()

                    }
//                myToast(requireActivity(),response.body()!!.message)
//                progressDialog!!.dismiss()

                    // val recyclerView = findViewById<RecyclerView>(R.id.rvCancled)
                }
            }

            override fun onFailure(call: Call<ModelUpComingResponse>, t: Throwable) {
                myToast(requireActivity(), "Something went wrong")
                    progressDialog!!.dismiss()
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
        System.out.printf("%d days, %d hours, %d minutes, %d seconds%n", elapsedDays, elapsedHours, elapsedMinutes, elapsedSeconds
        )
    }


    @SuppressLint("LogNotTimber")
    override fun showPopup(startTime:String){
        var view = layoutInflater.inflate(com.example.ehcf_doctor.R.layout.time_dialognew, null)
        dialog = Dialog(requireContext())
        currentTime = SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.getDefault()).format(Date())

        val btnOkDialog = view!!.findViewById<Button>(com.example.ehcf_doctor.R.id.btnOkDialogNew)
        dialog=   Dialog(requireContext())
//        val dateStart = "01/14/2012 09:29:58"
//        val dateStop = "01/15/2012 10:31:48"
//        val format = SimpleDateFormat("MM/dd/yyyy HH:mm:ss")
//        var d1: Date? = null
//        var d2: Date? = null
//       val startTimeNew= startTime.replace("-","/")
//
//        Log.e("currentTime","$currentTime")
//        Log.e("startTime","$startTime")
//        Log.e("startTimeNew","$startTimeNew")
//        try {
//            d1 = format.parse(currentTime)
//            d2 = format.parse(startTimeNew)
//
//            //in milliseconds
//
//            //in milliseconds
//            val diff = d1.time - d2.time
//
//            val diffSeconds = diff / 1000 % 60
//            val diffMinutes = diff / (60 * 1000) % 60
//            val diffHours = diff / (60 * 60 * 1000) % 24
//            val diffDays = diff / (24 * 60 * 60 * 1000)
//
//            print("$diffDays days, ")
//            print("$diffHours hours, ")
//            myToast(requireActivity(), "remaningTime $diffHours hours$diffMinutes minuts $diffSeconds second")
//           // myToast(requireActivity(), "remaningTime$diff minuts")
//            print("$diffMinutes minutes, ")
//            print("$diffSeconds seconds.")
//        }
//        catch (e: Exception) {
//            Log.e("IO","IO"+e);
//        }


        if (view.parent != null) {
            (view.parent as ViewGroup).removeView(view) // <- fix
        }
        dialog!!.setContentView(view)
        dialog?.setCancelable(true)
        // dialog?.setContentView(view)

        dialog?.show()
        btnOkDialog.setOnClickListener {
            dialog?.dismiss()
        }
    }
    override fun alretDilogConfirm(bookingId: String, slug: String){
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

                confirmSlot(bookingId,slug)
//                apiCallGetConsultationWating()
//                apiCallGetConsultationAccepted()
            }
            .setCancelClickListener { sDialog ->
                sDialog.cancel()
            }
            .show()
    }
    override fun videoCall(startTime: String,bookingId:String){
        SweetAlertDialog(requireContext(), SweetAlertDialog.WARNING_TYPE)
            .setTitleText("Are you sure want to Start Meeting?")
            .setCancelText("No")
            .setConfirmText("Yes")
            .showCancelButton(true)
            .setConfirmClickListener { sDialog ->
                sDialog.cancel()
//                val intent = Intent(applicationContext, SignIn::class.java)
//                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
//                finish()
//                startActivity(intent)

                videoCallFun(startTime,bookingId)

            }
            .setCancelClickListener { sDialog ->
                sDialog.cancel()
            }
            .show()
    }

    override fun alretDilogReject(bookingId: String, slug: String){
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

                rejectSlot(bookingId,slug)
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

        ApiClient.apiService.confirmSlot(bookingId,slug).enqueue(object : Callback<ModelConfirmSlotRes> {
            @SuppressLint("LogNotTimber")
            override fun onResponse(
                call: Call<ModelConfirmSlotRes>,
                response: Response<ModelConfirmSlotRes>
            ) {
                if (response.code()==200) {
                    myToast(requireActivity(),response.body()!!.message)

                  //  startActivity(Intent(requireContext(),UpComingFragment::class.java))
                    progressDialog!!.dismiss()

                    //    apiCall()
                }
                else if(response.code()==500)
                {
                    myToast(requireActivity(), "Server Error")
                    progressDialog!!.dismiss()

                }
                else{
                    myToast(requireActivity(), "Something went wrong")
                    progressDialog!!.dismiss()
                }
            }
            override fun onFailure(call: Call<ModelConfirmSlotRes>, t: Throwable) {
                myToast(requireActivity(), "Something went wrong")
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

        ApiClient.apiService.confirmSlot(bookingId,slug).enqueue(object : Callback<ModelConfirmSlotRes> {
            @SuppressLint("LogNotTimber")
            override fun onResponse(
                call: Call<ModelConfirmSlotRes>,
                response: Response<ModelConfirmSlotRes>
            ) {
                if (response.body()!!.status==200){
                    myToast(requireActivity(),response.body()!!.message)
                    progressDialog!!.dismiss()
                  //  startActivity(Intent(requireContext(),UpComingFragment::class.java))
                  //  apiCall()
                    progressDialog!!.dismiss()
                }else{
                    myToast(requireActivity(), response.body()!!.message)
                    progressDialog!!.dismiss()
                }
            }
            override fun onFailure(call: Call<ModelConfirmSlotRes>, t: Throwable) {
                myToast(requireActivity(), "Something went wrong")
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
        progressDialog!!.show()
        val slug="completed"
        ApiClient.apiService.confirmSlot(bookingId,slug).enqueue(object : Callback<ModelConfirmSlotRes> {
            @SuppressLint("LogNotTimber")
            override fun onResponse(
                call: Call<ModelConfirmSlotRes>,
                response: Response<ModelConfirmSlotRes>
            ) {
                if (response.body()!!.status==1){
                  //  myToast(requireActivity(),response.body()!!.message)
                    progressDialog!!.dismiss()
                    //  apiCall()

                }else{
                    myToast(requireActivity(), response.body()!!.message)
                    progressDialog!!.dismiss()
                }
            }
            override fun onFailure(call: Call<ModelConfirmSlotRes>, t: Throwable) {
                myToast(requireActivity(), "Something went wrong")
                progressDialog!!.dismiss()

            }

        })
    }

    override fun onResume() {
        super.onResume()


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