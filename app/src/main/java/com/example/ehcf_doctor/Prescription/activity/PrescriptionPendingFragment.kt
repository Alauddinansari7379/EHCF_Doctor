package com.example.ehcf_doctor.Prescription.activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.example.ehcf.Helper.isOnline
import com.example.ehcf.Helper.myToast
import com.example.ehcf.sharedpreferences.SessionManager
import com.example.ehcf_doctor.Prescription.adapter.AdapterPrescription
import com.example.ehcf_doctor.Prescription.model.ModelPendingPre
import com.example.ehcf_doctor.Prescription.model.ResultPrePending
import com.example.ehcf_doctor.R
import com.example.ehcf_doctor.databinding.FragmentPrescriptionPendingBinding
import com.example.ehcf_doctor.Retrofit.ApiClient
import com.facebook.shimmer.ShimmerFrameLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import rezwan.pstu.cse12.youtubeonlinestatus.recievers.NetworkChangeReceiver
import java.util.ArrayList


class PrescriptionPendingFragment : Fragment() {
    private lateinit var binding: FragmentPrescriptionPendingBinding
    private lateinit var sessionManager: SessionManager
    var mydilaog: Dialog? = null
    var progressDialog: ProgressDialog? = null
    private var tvTimeCounter: TextView? = null
    var shimmerFrameLayout: ShimmerFrameLayout? = null
    var patientName = ""

    private var mainData = ArrayList<ResultPrePending>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_prescription_pending, container, false)
    }

    @SuppressLint("LogNotTimber")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPrescriptionPendingBinding.bind(view)
        sessionManager = SessionManager(requireContext())
        shimmerFrameLayout = view.findViewById(R.id.shimmerPrePending)
        shimmerFrameLayout!!.startShimmer();

        mainData = ArrayList<ResultPrePending>()

        binding.imgRefresh.setOnClickListener {
            binding.edtSearch.text.clear()
            apiCallGetPrePending1()
        }

        apiCallGetPrePending()
        binding.edtSearch.addTextChangedListener { str ->
            setRecyclerViewAdapter(mainData.filter {
                it.customer_name!!.contains(str.toString(), ignoreCase = true)
            } as ArrayList<ResultPrePending>)
        }
   /*     binding.imgSearch.setOnClickListener {
            if (binding.edtSearch.text.toString().isEmpty()){
                binding.edtSearch.error="Enter Patient Name"
                binding.edtSearch.requestFocus()
            }else{
                patientName= binding.edtSearch.text.toString()
                apiCallSearchPrescription(patientName)
            }

        }*/


//        val refreshListener = SwipeRefreshLayout.OnRefreshListener {
//            requireActivity().overridePendingTransition(0,0);
//             requireActivity().finish()
//            requireFragmentManager().beginTransaction().detach(this).attach(this).commit();
//
//            requireActivity().overridePendingTransition(0,0)
//            myToast(requireActivity(), "refresh")
////            requireFragmentManager().beginTransaction().detach(PrescriptionPendingFragment())
////                .attach(PrescriptionPendingFragment()).commit()
//        }
//        binding.swipeRefreshLayout.setOnRefreshListener(refreshListener)


        //  apiCall()


    }

    private fun apiCallGetPrePending() {
        progressDialog = ProgressDialog(requireContext())
        progressDialog!!.setMessage("Loading..")
        progressDialog!!.setTitle("Please Wait")
        progressDialog!!.isIndeterminate = false
        progressDialog!!.setCancelable(true)
        // progressDialog!!.show()

        ApiClient.apiService.pendingPre(sessionManager.id.toString())
            .enqueue(object : Callback<ModelPendingPre> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelPendingPre>, response: Response<ModelPendingPre>
                ) {
                    try {
                        if (response.code() == 200) {
                            mainData = response.body()!!.result!!

                        }
                            if (response.code() == 500) {
                                myToast(requireActivity(), "Server Error")
                                binding.shimmerPrePending.visibility = View.GONE
                                progressDialog!!.dismiss()


                            } else if (response.body()!!.result.isEmpty()) {
                                binding.tvNoDataFound.visibility = View.VISIBLE
                                binding.shimmerPrePending.visibility = View.GONE
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

                override fun onFailure(call: Call<ModelPendingPre>, t: Throwable) {
                    myToast(requireActivity(), "Something went wrong")
                    binding.shimmerPrePending.visibility = View.GONE
                    progressDialog!!.dismiss()

                }

            })
    }
    private fun setRecyclerViewAdapter(data: ArrayList<ResultPrePending>) {
        binding.rvCancled.apply {
            shimmerFrameLayout?.startShimmer()
            binding.rvCancled.visibility = View.VISIBLE
            binding.shimmerPrePending.visibility = View.GONE
            binding.tvNoDataFound.visibility = View.GONE
            adapter = AdapterPrescription(requireContext(), data)
            progressDialog!!.dismiss()

        }
    }

    private fun apiCallGetPrePending1() {
        progressDialog = ProgressDialog(requireContext())
        progressDialog!!.setMessage("Loading..")
        progressDialog!!.setTitle("Please Wait")
        progressDialog!!.isIndeterminate = false
        progressDialog!!.setCancelable(true)
         progressDialog!!.show()

        ApiClient.apiService.pendingPre(sessionManager.id.toString())
            .enqueue(object : Callback<ModelPendingPre> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelPendingPre>, response: Response<ModelPendingPre>
                ) {

                    try {
                        if (response.code() == 200) {
                            mainData = response.body()!!.result!!

                        }
                            if (response.code() == 500) {
                                myToast(requireActivity(), "Server Error")
                                binding.shimmerPrePending.visibility = View.GONE
                                progressDialog!!.dismiss()


                            } else if (response.body()!!.result.isEmpty()) {
                                binding.tvNoDataFound.visibility = View.VISIBLE
                                binding.shimmerPrePending.visibility = View.GONE
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

                override fun onFailure(call: Call<ModelPendingPre>, t: Throwable) {
                    myToast(requireActivity(), "Something went wrong")
                    binding.shimmerPrePending.visibility = View.GONE
                    progressDialog!!.dismiss()

                }

            })
    }

/*
    private fun apiCallSearchPrescription(patientName: String) {
        progressDialog = ProgressDialog(requireContext())
        progressDialog!!.setMessage("Loading..")
        progressDialog!!.setTitle("Please Wait")
        progressDialog!!.isIndeterminate = false
        progressDialog!!.setCancelable(true)
         progressDialog!!.show()

        ApiClient.apiService.searchPrescription(patientName,sessionManager.id.toString())
            .enqueue(object : Callback<ModelPendingPre> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelPendingPre>, response: Response<ModelPendingPre>
                ) {
                    if (response.code() == 500) {
                        myToast(requireActivity(), "Server Error")
                        binding.shimmerPrePending.visibility = View.GONE
                    } else if (response.body()!!.status == 0) {
                        binding.tvNoDataFound.visibility = View.VISIBLE
                        binding.shimmerPrePending.visibility = View.GONE
                        binding.edtSearch.text.clear()
                        myToast(requireActivity(), "${response.body()!!.message}")
                        progressDialog!!.dismiss()

                    } else if (response.body()!!.result.isEmpty()) {
                        binding.rvCancled.adapter =
                            AdapterPrescription(requireContext(), response.body()!!)
                        binding.rvCancled.adapter!!.notifyDataSetChanged()
                        binding.tvNoDataFound.visibility = View.VISIBLE
                        binding.shimmerPrePending.visibility = View.GONE
                        binding.edtSearch.text.clear()
                        myToast(requireActivity(), "No Appointment Found")
                        progressDialog!!.dismiss()

                    } else {
                        binding.rvCancled.adapter =
                            AdapterPrescription(requireContext(), response.body()!!)
                        binding.rvCancled.adapter!!.notifyDataSetChanged()
                        binding.tvNoDataFound.visibility = View.GONE
                        shimmerFrameLayout?.startShimmer()
                        binding.rvCancled.visibility = View.VISIBLE
                        binding.shimmerPrePending.visibility = View.GONE
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

                override fun onFailure(call: Call<ModelPendingPre>, t: Throwable) {
                    myToast(requireActivity(), "Something went wrong")
                    binding.shimmerPrePending.visibility = View.GONE
                    progressDialog!!.dismiss()

                }

            })
    }
*/

    override fun onStart() {
        super.onStart()
        apiCallGetPrePending()

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