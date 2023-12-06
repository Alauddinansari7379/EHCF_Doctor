package com.example.ehcf_doctor.Home

import android.Manifest
import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
 import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.ehcf.Helper.isOnline
import com.example.ehcf.Helper.myToast
import com.example.ehcf.sharedpreferences.SessionManager
import com.example.ehcf_doctor.Appointments.Appointments
import com.example.ehcf_doctor.Appointments.Upcoming.adapter.AdapterHome
import com.example.ehcf_doctor.Booking.model.ModelGetConsultation
import com.example.ehcf_doctor.MainActivity.activity.MainActivity
import com.example.ehcf_doctor.R
import com.example.ehcf_doctor.databinding.FragmentHomeBinding
import com.example.myrecyview.apiclient.ApiClient
import com.facebook.shimmer.ShimmerFrameLayout
 import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import rezwan.pstu.cse12.youtubeonlinestatus.recievers.NetworkChangeReceiver
import java.util.*


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var sessionManager: SessionManager
    var doctorname = ""
    var id = ""
    var progressDialog: ProgressDialog? = null
    var shimmerFrameLayout: ShimmerFrameLayout? = null
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
        shimmerFrameLayout = view.findViewById(R.id.shimmer)
        shimmerFrameLayout!!.startShimmer();
//
//        if (sessionManager.clinicAddress=="null"){
//
//        }
// Setup our BluetoothManager
        // Setup our BluetoothManager
        val refreshListener = SwipeRefreshLayout.OnRefreshListener {

            (activity as MainActivity).refreshMain()
        }
        binding.swipeRefreshLayout.setOnRefreshListener(refreshListener)

        binding.cardTotalBooking.setOnClickListener {
            startActivity(Intent(requireContext(), Appointments::class.java))
        }

        binding.CardCompleted.setOnClickListener {
            startActivity(Intent(requireContext(), Appointments::class.java))

        }
        binding.CardRejected.setOnClickListener {
            startActivity(Intent(requireContext(), Appointments::class.java))

        }

        binding.cardTotalPending.setOnClickListener {
            startActivity(Intent(requireContext(), Appointments::class.java))

        }
        // audioRecorder.startRecording(requireContext())
        //   audioRecorder.pauseRecording()
        // audioRecorder.resumeRecording()


        doctorname = sessionManager.doctorName.toString()
        //  id = sessionManager.id.toString()
        binding.tvDoctorName.text = doctorname
        binding.tvDoctorId.text = id
        Log.e("DoctorNAme,", "$doctorname")
        Log.e("DoctorId,", "${sessionManager.id}")
//
        apiCallGetConsultationWating()

//        CoroutineScope(Dispatchers.IO).launch {
//            Log.d("", ": coroutine start")
//            apiCallTotalBooking()
//            apiCallCompletedBooking()
//            apiCallRejectedBooking()
//        }

        lifecycleScope.launch {
            apiCallTotalBooking()
            apiCallCompletedBooking()
            apiCallRejectedBooking()
        }

//        CheckInternet().check { connected ->
//            if (connected) {
//               // myToast(requireActivity(), "")
//            }
//        }
    }


    private fun isNetworkConnected(): Boolean {
        val cm = requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo != null
    }


    private fun apiCallRejectedBooking() {
        progressDialog = ProgressDialog(requireContext())
        progressDialog!!.setMessage("Loading..")
        progressDialog!!.setTitle("Please Wait")
        progressDialog!!.isIndeterminate = false
        progressDialog!!.setCancelable(true)

        //progressDialog!!.show()

        ApiClient.apiService.getConsultation(sessionManager.id.toString(), "rejected")
            .enqueue(object : Callback<ModelGetConsultation> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelGetConsultation>, response: Response<ModelGetConsultation>
                ) {
                    try {
                        if (response.code() == 500) {

                        } else if (response.body()!!.result.isEmpty()) {
                            response.body()!!.result.size.toString()
                            Log.e("Size", response.body()!!.result.size.toString())
                            binding.tvRejectedBooking.text =
                                response.body()!!.result.size.toString()

                        } else {
                            response.body()!!.result.size.toString()
                            Log.e("Size", response.body()!!.result.size.toString())
                            binding.tvRejectedBooking.text =
                                response.body()!!.result.size.toString()
                        }


                    }catch (e:Exception){
                        e.printStackTrace()
                        myToast(requireActivity(), "Something went wrong")
                        binding.shimmer.visibility = View.GONE
                        progressDialog!!.dismiss()
                    }
                }

                override fun onFailure(call: Call<ModelGetConsultation>, t: Throwable) {
                    myToast(requireActivity(), "Something went wrong")
                    binding.shimmer.visibility = View.GONE
                    progressDialog!!.dismiss()

                }

            })

    }

    private fun apiCallCompletedBooking() {
        progressDialog = ProgressDialog(requireContext())
        progressDialog!!.setMessage("Loading..")
        progressDialog!!.setTitle("Please Wait")
        progressDialog!!.isIndeterminate = false
        progressDialog!!.setCancelable(true)

        //  progressDialog!!.show()

        ApiClient.apiService.getConsultation(sessionManager.id.toString(), "completed")
            .enqueue(object : Callback<ModelGetConsultation> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelGetConsultation>, response: Response<ModelGetConsultation>
                ) {
                    try {
                        if (response.code() == 500) {

                        } else if (response.body()!!.result.isEmpty()) {
                            binding.shimmer.visibility = View.GONE
                            response.body()!!.result.size.toString()
                            Log.e("Size", response.body()!!.result.size.toString())
                            binding.tvCompletedBooking.text =
                                response.body()!!.result.size.toString()
                            // myToast(requireActivity(),"No Data Found")

                        } else {
                            response.body()!!.result.size.toString()
                            Log.e("Size", response.body()!!.result.size.toString())
                            binding.tvCompletedBooking.text =
                                response.body()!!.result.size.toString()
                        }


                    }catch (e:Exception){
                        e.printStackTrace()
                        progressDialog!!.dismiss()

                    }
                }

                override fun onFailure(call: Call<ModelGetConsultation>, t: Throwable) {
                    progressDialog!!.dismiss()

                }

            })
    }

    private fun apiCallTotalBooking() {
        progressDialog = ProgressDialog(requireContext())
        progressDialog!!.setMessage("Loading..")
        progressDialog!!.setTitle("Please Wait")
        progressDialog!!.isIndeterminate = false
        progressDialog!!.setCancelable(true)

        //  progressDialog!!.show()

        ApiClient.apiService.getConsultation(sessionManager.id.toString(), "")
            .enqueue(object : Callback<ModelGetConsultation> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelGetConsultation>, response: Response<ModelGetConsultation>
                ) {
                    try {
                        if (response.code() == 500) {

                        } else if (response.body()!!.result.isEmpty()) {
                            response.body()!!.result.size.toString()
                            Log.e("Size", response.body()!!.result.size.toString())
                            binding.tvTotalBooking.text = response.body()!!.result.size.toString()

                        } else {
                            response.body()!!.result.size.toString()
                            Log.e("Size", response.body()!!.result.size.toString())
                            binding.tvTotalBooking.text = response.body()!!.result.size.toString()
                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                        progressDialog!!.dismiss()

                    }



                }

                override fun onFailure(call: Call<ModelGetConsultation>, t: Throwable) {
                    myToast(requireActivity(), "Something went wrong")
                    binding.shimmer.visibility = View.GONE
                    progressDialog!!.dismiss()

                }

            })
    }

    private fun apiCallGetConsultationWating() {

        ApiClient.apiService.getConsultation(sessionManager.id.toString(), "waiting_for_accept")
            .enqueue(object : Callback<ModelGetConsultation> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelGetConsultation>, response: Response<ModelGetConsultation>
                ) {
                    try {
                        if (response.code() == 500) {
                            myToast(requireActivity(), "Server Error")
                            binding.shimmer.visibility = View.GONE

                        } else if (response.body()!!.result.isEmpty()) {
                            binding.shimmer.visibility = View.GONE

                            // myToast(requireActivity(),"No Data Found")

                        } else {
                            binding.rvUpcoming.apply {
                                shimmerFrameLayout?.startShimmer()
                                response.body()!!.result.size.toString()
                                Log.e("Size", response.body()!!.result.size.toString())
                                binding.tvPendingBooking.text = response.body()!!.result.size.toString()
                                binding.rvUpcoming.visibility = View.VISIBLE
                                binding.shimmer.visibility = View.GONE
                                //  myToast(requireActivity(),"Adapter")
                                adapter = activity?.let { AdapterHome(it, response.body()!!) }
                            }
                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                        e.localizedMessage?.let { Log.e("CatchError", it) }
                        progressDialog!!.dismiss()

                    }


                }

                override fun onFailure(call: Call<ModelGetConsultation>, t: Throwable) {
                    // myToast(requireActivity(), "Something went wrong")
                    binding.shimmer.visibility = View.GONE
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

}
