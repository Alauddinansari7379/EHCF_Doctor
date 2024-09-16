package com.example.ehcf_doctor.Booking.activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.ehcf.Helper.isOnline
import com.example.ehcf.Helper.myToast
import com.example.ehcf.sharedpreferences.SessionManager
import com.example.ehcf_doctor.Booking.adapter.AdapterBooking
import com.example.ehcf_doctor.Booking.model.ModelGetConsultation
import com.example.ehcf_doctor.Helper.AppProgressBar
import com.example.ehcf_doctor.R
import com.example.ehcf_doctor.databinding.FragmentBookingBinding
import com.example.ehcf_doctor.Retrofit.ApiClient
import com.facebook.shimmer.ShimmerFrameLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import rezwan.pstu.cse12.youtubeonlinestatus.recievers.NetworkChangeReceiver

class BookingFragment : Fragment() {
    private lateinit var binding: FragmentBookingBinding
    private lateinit var sessionManager: SessionManager
    var mydilaog: Dialog? = null
    private var tvTimeCounter: TextView? = null
    var shimmerFrameLayout: ShimmerFrameLayout? = null

    var patientName=""
    private var count = 0
    private var count2 = 0
    private var count3 = 0




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_booking, container, false)
    }

    @SuppressLint("LogNotTimber")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentBookingBinding.bind(view)
        sessionManager = SessionManager(requireContext())
        shimmerFrameLayout = view.findViewById(R.id.shimmer)
        shimmerFrameLayout!!.startShimmer();
        apiCallGetConsultation()

        binding.imgSearch.setOnClickListener {
            if (binding.edtSearch.text.toString().isEmpty()){
                binding.edtSearch.error="Enter Patient Name"
                binding.edtSearch.requestFocus()
            }else{
                patientName= binding.edtSearch.text.toString()
                apiCallSearchAppointments(patientName)
            }

        }
        binding.imgRefresh.setOnClickListener {
            binding.edtSearch.text.clear()
            apiCallGetConsultation1()
        }
        CoroutineScope(Dispatchers.IO).launch {
            Log.d("", ": coroutine   h start")



        }
    }
    private fun apiCallSearchAppointments(patientName: String) {
        AppProgressBar.showLoaderDialog(context)


        ApiClient.apiService.searchAppointments(patientName,sessionManager.id.toString(),"")
            .enqueue(object : Callback<ModelGetConsultation> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelGetConsultation>, response: Response<ModelGetConsultation>
                ) {
                    try {
                        if (response.code() == 500) {
                            myToast(requireActivity(), "Server Error")
                            binding.shimmer.visibility = View.GONE
                        } else if (response.body()!!.status == 0) {
                            binding.tvNoDataFound.visibility = View.VISIBLE
                            binding.shimmer.visibility = View.GONE
                            binding.edtSearch.text.clear()
                            myToast(requireActivity(), "${response.body()!!.message}")
                            AppProgressBar.hideLoaderDialog()

                        } else if (response.body()!!.result.isEmpty()) {
                            binding.recyclerView.adapter =
                                activity?.let { AdapterBooking(it, response.body()!!) }
                            binding.recyclerView.adapter!!.notifyDataSetChanged()
                            binding.tvNoDataFound.visibility = View.VISIBLE
                            binding.shimmer.visibility = View.GONE
                            binding.edtSearch.text.clear()
                            myToast(requireActivity(), "No Appointment Found")
                            AppProgressBar.hideLoaderDialog()

                        } else {
                            count = 0
                            binding.recyclerView.adapter =
                                activity?.let { AdapterBooking(it, response.body()!!) }
                            binding.recyclerView.adapter!!.notifyDataSetChanged()
                            binding.tvNoDataFound.visibility = View.GONE
                            shimmerFrameLayout?.startShimmer()
                            binding.recyclerView.visibility = View.VISIBLE
                            binding.shimmer.visibility = View.GONE
                            binding.edtSearch.text.clear()
                            AppProgressBar.hideLoaderDialog()
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
                    }catch (e:Exception){
                       e.printStackTrace()
                        myToast(requireActivity(), "Something went wrong")
                        binding.shimmer.visibility = View.GONE
                        AppProgressBar.hideLoaderDialog()
                    }
                }

                override fun onFailure(call: Call<ModelGetConsultation>, t: Throwable) {
                    count++
                    if (count <= 3) {
                        apiCallSearchAppointments(patientName)
                    } else {
                        myToast(requireActivity(), "Something went wrong")
                        binding.shimmer.visibility = View.GONE
                    }
                    AppProgressBar.hideLoaderDialog()

                }

            })
    }

    private fun apiCallGetConsultation() {
        AppProgressBar.showLoaderDialog(context)


        ApiClient.apiService.getConsultation(sessionManager.id.toString(), "")
            .enqueue(object : Callback<ModelGetConsultation> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelGetConsultation>, response: Response<ModelGetConsultation>
                ) {
                    try {

                        response.body()!!.result.size
                        Log.e("Size", response.body()!!.result.size.toString())

                        if (response.body()!!.result.isEmpty()) {
                            binding.tvNoDataFound.visibility = View.VISIBLE
                            binding.shimmer.visibility = View.GONE
                            // myToast(requireActivity(),"No Data Found")
                            AppProgressBar.hideLoaderDialog()

                        } else {
                            count2 = 0
                            binding.recyclerView.apply {
                                shimmerFrameLayout?.startShimmer()
                                binding.recyclerView.visibility = View.VISIBLE
                                binding.shimmer.visibility = View.GONE
                                binding.tvNoDataFound.visibility = View.GONE
                                adapter = activity?.let { AdapterBooking(it, response.body()!!) }
                                AppProgressBar.hideLoaderDialog()


                            }
                        }
                    }catch (e:Exception){
                        e.printStackTrace()
                        myToast(requireActivity(), "Something went wrong")
                        binding.shimmer.visibility = View.GONE
                        AppProgressBar.hideLoaderDialog()
                    }

                }

                override fun onFailure(call: Call<ModelGetConsultation>, t: Throwable) {
                    count2++
                    if (count2 <= 3) {
                        apiCallGetConsultation()
                    } else {
                        myToast(requireActivity(), "Something went wrong")
                        binding.shimmer.visibility = View.GONE
                    }
                    AppProgressBar.hideLoaderDialog()

                }

            })
    }
    private fun apiCallGetConsultation1() {
      AppProgressBar.showLoaderDialog(context)

        ApiClient.apiService.getConsultation(sessionManager.id.toString(), "")
            .enqueue(object : Callback<ModelGetConsultation> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelGetConsultation>, response: Response<ModelGetConsultation>
                ) {
                    try {

                        response.body()!!.result.size
                        Log.e("Size", response.body()!!.result.size.toString())

                        if (response.body()!!.result.isEmpty()) {
                            binding.tvNoDataFound.visibility = View.VISIBLE
                            binding.shimmer.visibility = View.GONE
                            // myToast(requireActivity(),"No Data Found")
                            AppProgressBar.hideLoaderDialog()

                        } else {
                            count3 = 0
                            binding.recyclerView.apply {
                                shimmerFrameLayout?.startShimmer()
                                binding.recyclerView.visibility = View.VISIBLE
                                binding.shimmer.visibility = View.GONE
                                binding.tvNoDataFound.visibility = View.GONE
                                adapter = activity?.let { AdapterBooking(it, response.body()!!) }
                                AppProgressBar.hideLoaderDialog()


                            }
                        }

                    }catch (e:Exception){
                        e.printStackTrace()
                        myToast(requireActivity(), "Something went wrong")
                        binding.shimmer.visibility = View.GONE
                        AppProgressBar.hideLoaderDialog()
                    }
                }
                override fun onFailure(call: Call<ModelGetConsultation>, t: Throwable) {
                    count3++
                    if (count3 <= 3) {
                        apiCallGetConsultation1()
                    } else {
                        myToast(requireActivity(), "Something went wrong")
                        binding.shimmer.visibility = View.GONE
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


}