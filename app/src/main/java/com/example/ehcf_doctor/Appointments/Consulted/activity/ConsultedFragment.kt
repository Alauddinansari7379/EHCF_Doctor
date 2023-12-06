package com.example.ehcf_doctor.Appointments.Consulted.activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.app.ProgressDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import com.example.ehcf.Helper.isOnline
import com.example.ehcf.Helper.myToast
import com.example.ehcf.sharedpreferences.SessionManager
import com.example.ehcf_doctor.Appointments.Consulted.adapter.AdapterConsulted
import com.example.ehcf_doctor.Appointments.Upcoming.adapter.AdapterUpComing
import com.example.ehcf_doctor.Booking.model.ModelGetConsultation
import com.example.ehcf_doctor.Booking.model.ResultUpcoming
import com.example.ehcf_doctor.R
import com.example.ehcf_doctor.databinding.FragmentConsultedBinding
import com.example.myrecyview.apiclient.ApiClient
import com.facebook.shimmer.ShimmerFrameLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import rezwan.pstu.cse12.youtubeonlinestatus.recievers.NetworkChangeReceiver
import java.util.ArrayList


class ConsultedFragment : Fragment() {
    private lateinit var binding: FragmentConsultedBinding
    private lateinit var sessionManager: SessionManager
    var mydilaog: Dialog? = null
    var progressDialog: ProgressDialog? = null
    var dialog: Dialog? = null
    private var tvTimeCounter: TextView? = null
    var shimmerFrameLayout: ShimmerFrameLayout? = null
    private var  mainData = ArrayList<ResultUpcoming>()

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
        shimmerFrameLayout = view.findViewById(R.id.shimmer)
        shimmerFrameLayout!!.startShimmer();
        mainData = ArrayList<ResultUpcoming>()

        val view = layoutInflater.inflate(R.layout.time_dialog, null)

        val btnOkDialog = view.findViewById<Button>(R.id.btnOkDialog)
        tvTimeCounter = view.findViewById<TextView>(R.id.tvTimeCounter)
        binding.imgRefresh.setOnClickListener {
            apiCallGetConsultation()

        }
        apiCallGetConsultation()
        binding.edtSearch.addTextChangedListener { str ->
            setRecyclerViewAdapter(mainData.filter {
                it.customer_name!!.contains(str.toString(), ignoreCase = true)
            } as ArrayList<ResultUpcoming>)
        }
       /* binding.imgSearch.setOnClickListener {
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


        ApiClient.apiService.searchAppointments(patientName,sessionManager.id.toString(),"completed")
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
                            activity?.let { AdapterConsulted(it, response.body()!!) }
                        binding.rvCancled.adapter!!.notifyDataSetChanged()
                        binding.tvNoDataFound.visibility = View.VISIBLE
                        binding.shimmer.visibility = View.GONE
                        binding.edtSearch.text.clear()
                        myToast(requireActivity(), "No Appointment Found")
                        progressDialog!!.dismiss()

                    } else {
                        binding.rvCancled.adapter =
                            activity?.let {
                                AdapterConsulted(
                                    it,
                                    response.body()!!
                                )
                            }
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
        progressDialog = ProgressDialog(requireContext())
        progressDialog!!.setMessage("Loading..")
        progressDialog!!.setTitle("Please Wait")
        progressDialog!!.isIndeterminate = false
        progressDialog!!.setCancelable(true)

        progressDialog!!.show()

        ApiClient.apiService.getConsultation(sessionManager.id.toString(), "completed")
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
                            progressDialog!!.dismiss()

                        } else if (response.body()!!.result.isEmpty()) {
                            binding.tvNoDataFound.visibility = View.VISIBLE
                            binding.shimmer.visibility = View.GONE
                            // myToast(requireActivity(),"No Data Found")
                            progressDialog!!.dismiss()

                        } else {
                            binding.rvCancled.apply {
                                setRecyclerViewAdapter(mainData)
                                progressDialog!!.dismiss()

                            }
                        }
                    }catch (e:Exception){
                        e.printStackTrace()
                    }

                }

                override fun onFailure(call: Call<ModelGetConsultation>, t: Throwable) {
                    myToast(requireActivity(), "Something went wrong")
                    binding.shimmer.visibility = View.GONE
                    progressDialog!!.dismiss()

                }

            })
    }
    private fun setRecyclerViewAdapter(data: ArrayList<ResultUpcoming>) {
        binding.rvCancled.apply {
            shimmerFrameLayout?.startShimmer()
            binding.rvCancled.visibility = View.VISIBLE
            binding.shimmer.visibility = View.GONE
            binding.tvNoDataFound.visibility = View.GONE
            adapter = AdapterConsulted(requireContext(), data)
            progressDialog!!.dismiss()

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

}