package com.example.ehcf_doctor.Appointments.Cancelled.activity

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import com.example.ehcf.Helper.isOnline
import com.example.ehcf.Helper.myToast
import com.example.ehcf.sharedpreferences.SessionManager
import com.example.ehcf_doctor.Appointments.Cancelled.adapter.AdapterCancelled
import com.example.ehcf_doctor.Booking.model.ModelGetConsultation
import com.example.ehcf_doctor.Booking.model.ResultUpcoming
import com.example.ehcf_doctor.Helper.AppProgressBar
import com.example.ehcf_doctor.R
import com.example.ehcf_doctor.databinding.FragmentCancelledBinding
import com.example.ehcf_doctor.Retrofit.ApiClient
import com.facebook.shimmer.ShimmerFrameLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import rezwan.pstu.cse12.youtubeonlinestatus.recievers.NetworkChangeReceiver
import java.util.ArrayList

class CancelledFragment : Fragment() {
    private lateinit var binding: FragmentCancelledBinding
    private lateinit var sessionManager: SessionManager
    var shimmerFrameLayout: ShimmerFrameLayout? = null
    private var mainData = ArrayList<ResultUpcoming>()
    private var count = 0

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
        Log.e("Id", "${sessionManager.id}")
        shimmerFrameLayout = view.findViewById(R.id.shimmer)
        shimmerFrameLayout!!.startShimmer();
        mainData = ArrayList<ResultUpcoming>()
        //apiCall()
        apiCallGetConsultation()
        binding.imgRefresh.setOnClickListener {
            // apiCall()
            apiCallGetConsultation()
        }
        binding.edtSearch.addTextChangedListener { str ->
            setRecyclerViewAdapter(mainData.filter {
                it.customer_name!!.contains(str.toString(), ignoreCase = true)
            } as ArrayList<ResultUpcoming>)
        }
        /*    binding.imgSearch.setOnClickListener {
                if (binding.edtSearch.text.toString().isEmpty()) {
                    binding.edtSearch.error = "Enter Patient Name"
                    binding.edtSearch.requestFocus()
                } else {
                    val search = binding.edtSearch.text.toString()
                    apiCallSearchAppointments(search)
                }
            }*/
    }
    /*
        private fun apiCallSearchAppointments(patientName: String) {
            progressDialog = ProgressDialog(requireContext())
            progressDialog!!.setMessage("Loading..")
            progressDialog!!.setTitle("Please Wait")
            progressDialog!!.isIndeterminate = false
            progressDialog!!.setCancelable(true)
            progressDialog!!.show()


            ApiClient.apiService.searchAppointments(patientName,sessionManager.id.toString(),"rejected")
                .enqueue(object : Callback<ModelGetConsultation> {
                    @SuppressLint("LogNotTimber")
                    override fun onResponse(
                        call: Call<ModelGetConsultation>, response: Response<ModelGetConsultation>
                    ) {
                        if (response.code() == 500) {
                            myToast(requireActivity(), "Server Error")
                            binding.shimmer.visibility = View.GONE
                        } else if (response.body()!!.status == 0) {
                            binding.tvNoDataFound.visibility = View.VISIBLE
                            binding.shimmer.visibility = View.GONE
                            binding.edtSearch.text.clear()
                            myToast(requireActivity(), "${response.body()!!.message}")
                            progressDialog!!.dismiss()

                        } else if (response.body()!!.result.isEmpty()) {
                            binding.rvCancled.adapter =
                                activity?.let { AdapterCancelled(it, response.body()!!) }
                            binding.rvCancled.adapter!!.notifyDataSetChanged()
                            binding.tvNoDataFound.visibility = View.VISIBLE
                            binding.shimmer.visibility = View.GONE
                            binding.edtSearch.text.clear()
                            myToast(requireActivity(), "No Appointment Found")
                            progressDialog!!.dismiss()

                        } else {
                            binding.rvCancled.adapter =
                                activity?.let { AdapterCancelled(it, response.body()!!) }
                            binding.rvCancled.adapter!!.notifyDataSetChanged()
                            binding.tvNoDataFound.visibility = View.GONE
                            shimmerFrameLayout?.startShimmer()
                            binding.rvCancled.visibility = View.VISIBLE
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
                        myToast(requireActivity(), "Something went wrong")
                        binding.shimmer.visibility = View.GONE
                        progressDialog!!.dismiss()

                    }

                })
        }
    */

    private fun apiCallGetConsultation() {
        AppProgressBar.showLoaderDialog(context)
        ApiClient.apiService.getConsultation(sessionManager.id.toString(), "rejected")
            .enqueue(object : Callback<ModelGetConsultation> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelGetConsultation>, response: Response<ModelGetConsultation>
                ) {
                    try {
                        if (response.code() == 200) {
                            count = 0
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
                            binding.rvCancled.apply {
                                setRecyclerViewAdapter(mainData)
                                AppProgressBar.hideLoaderDialog()

                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        myToast(requireActivity(), "Something went wrong")
                        binding.shimmer.visibility = View.GONE
                        AppProgressBar.hideLoaderDialog()
                    }

                }

                override fun onFailure(call: Call<ModelGetConsultation>, t: Throwable) {
                    count++
                    if (count <= 3) {
                        apiCallGetConsultation()
                    } else {
                        myToast(requireActivity(), "Something went wrong")
                        binding.shimmer.visibility = View.GONE
                    }
                    AppProgressBar.hideLoaderDialog()

                }

            })

    }

    private fun setRecyclerViewAdapter(data: ArrayList<ResultUpcoming>) {
        binding.rvCancled.apply {
            shimmerFrameLayout?.startShimmer()
            binding.rvCancled.visibility = View.VISIBLE
            binding.shimmer.visibility = View.GONE
            binding.tvNoDataFound.visibility = View.GONE
            adapter = AdapterCancelled(requireContext(), data)
            AppProgressBar.hideLoaderDialog()

        }
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

    /* private fun apiCall(){

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
             )
             {
              if (response.body()!!.result.isEmpty()) {
                     binding.tvNoDataFound.visibility = View.VISIBLE
                     // myToast(requireActivity(),"No Data Found")
                     progressDialog!!.dismiss()
                 }
                 binding.rvCancled.apply {
                     adapter = AdapterCancelled(requireContext(), response.body()!!)
                     binding.tvNoDataFound.visibility = View.GONE
                     progressDialog!!.dismiss()

                 }
             }

             override fun onFailure(call: Call<ModelUpComingResponse>, t: Throwable) {
                 t.message?.let { myToast(requireActivity(), it)
                     progressDialog!!.dismiss()

                 }
             }
         })
     }*/

}