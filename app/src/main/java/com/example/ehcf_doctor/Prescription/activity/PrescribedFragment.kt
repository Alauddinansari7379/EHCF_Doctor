package com.example.ehcf_doctor.Prescription.activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.app.ProgressDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toolbar
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.viewpager.widget.ViewPager
import com.example.ehcf.Helper.myToast
import com.example.ehcf.sharedpreferences.SessionManager
import com.example.ehcf_doctor.Helper.AppProgressBar
import com.example.ehcf_doctor.Prescription.adapter.AdapterPrescribed
import com.example.ehcf_doctor.Prescription.model.*
import com.example.ehcf_doctor.R
import com.example.ehcf_doctor.databinding.FragmentPrescribedBinding
import com.example.ehcf_doctor.Retrofit.ApiClient
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList


class PrescribedFragment : Fragment() {
    private lateinit var binding: FragmentPrescribedBinding
    private lateinit var sessionManager: SessionManager
    var mydilaog: Dialog? = null
    private lateinit var pager: ViewPager // creating object of ViewPager
    private lateinit var tab: TabLayout  // creating object of TabLayout
    private lateinit var bar: Toolbar
    private lateinit var btnAddReport: TextView
    var shimmerFrameLayout: ShimmerFrameLayout? = null
    var patientName = ""
    private var count = 0
    private var count2 = 0
    private lateinit var mainData: ArrayList<ResultPrePrescribed>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPrescribedBinding.inflate(inflater)
        return inflater.inflate(R.layout.fragment_prescribed, container, false)
    }

    @SuppressLint("LogNotTimber")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPrescribedBinding.bind(view)
        sessionManager = SessionManager(requireContext())
        shimmerFrameLayout = view.findViewById(R.id.shimmerPrescribed)
        shimmerFrameLayout!!.startShimmer();
        mainData = ArrayList<ResultPrePrescribed>()
        //  apiCall()
        //  apiCallGetConsultation()

        binding.edtSearch.addTextChangedListener { str ->
            setRecyclerViewAdapter(mainData.filter {
                it.customer_name!!.contains(str.toString(), ignoreCase = true)
            } as ArrayList<ResultPrePrescribed>)
        }

        binding.imgRefresh.setOnClickListener {
            binding.edtSearch.text.clear()
            apiCallGetPrePending1()
        }



        lifecycleScope.launch {
            apiCallGetPrePending()

        }
    }
    /*
        private fun apiCallSearchPrescription(patientName: String) {
            progressDialog = ProgressDialog(requireContext())
            progressDialog!!.setMessage("Loading..")
            progressDialog!!.setTitle("Please Wait")
            progressDialog!!.isIndeterminate = false
            progressDialog!!.setCancelable(true)
            progressDialog!!.show()

            ApiClient.apiService.searchPrescribed(patientName,sessionManager.id.toString())
                .enqueue(object : Callback<ModelPrescribed> {
                    @SuppressLint("LogNotTimber")
                    override fun onResponse(
                        call: Call<ModelPrescribed>, response: Response<ModelPrescribed>
                    ) {
                        if (response.code() == 500) {
                            myToast(requireActivity(), "Server Error")
                            binding.shimmerPrescribed.visibility = View.GONE
                        } else if (response.body()!!.status == 0) {
                            binding.tvNoDataFound.visibility = View.VISIBLE
                            binding.shimmerPrescribed.visibility = View.GONE
                            binding.edtSearch.text.clear()
                            myToast(requireActivity(), "${response.body()!!.message}")
                            progressDialog!!.dismiss()

                        } else if (response.body()!!.result.isEmpty()) {
                            binding.rvCancled.adapter =
                                AdapterPrescribed(requireContext(), response.body()!!)
                            binding.rvCancled.adapter!!.notifyDataSetChanged()
                            binding.tvNoDataFound.visibility = View.VISIBLE
                            binding.shimmerPrescribed.visibility = View.GONE
                            binding.edtSearch.text.clear()
                            myToast(requireActivity(), "No Appointment Found")
                            progressDialog!!.dismiss()

                        } else {
                            binding.rvCancled.adapter =
                                AdapterPrescribed(requireContext(), response.body()!!)
                            binding.rvCancled.adapter!!.notifyDataSetChanged()
                            binding.tvNoDataFound.visibility = View.GONE
                            shimmerFrameLayout?.startShimmer()
                            binding.rvCancled.visibility = View.VISIBLE
                            binding.shimmerPrescribed.visibility = View.GONE
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

                    override fun onFailure(call: Call<ModelPrescribed>, t: Throwable) {
                        myToast(requireActivity(), "Something went wrong")
                        binding.shimmerPrescribed.visibility = View.GONE
                        progressDialog!!.dismiss()

                    }

                })
        }
    */

    private fun apiCallGetPrePending() {
       AppProgressBar.showLoaderDialog(context)

        ApiClient.apiService.getPrescribed(sessionManager.id.toString())
            .enqueue(object : Callback<ModelPrescribed> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelPrescribed>, response: Response<ModelPrescribed>
                ) {
                    try {
                        if (response.code() == 200) {
                            count = 0
                            mainData = response.body()!!.result!!

                        }
                        if (response.code() == 500) {
                            myToast(requireActivity(), "Server Error")
                            binding.shimmerPrescribed.visibility = View.GONE
                            AppProgressBar.hideLoaderDialog()


                        } else if (response.body()!!.result.isEmpty()) {
                            binding.tvNoDataFound.visibility = View.VISIBLE
                            binding.shimmerPrescribed.visibility = View.GONE
                            // myToast(requireActivity(),"No Data Found")
                            AppProgressBar.hideLoaderDialog()

                        } else {
                            setRecyclerViewAdapter(mainData)
                            AppProgressBar.hideLoaderDialog()

                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        AppProgressBar.hideLoaderDialog()

                    }

                }

                override fun onFailure(call: Call<ModelPrescribed>, t: Throwable) {
                    count++
                    if (count <= 3) {
                        apiCallGetPrePending()
                    } else {
                        myToast(requireActivity(), "Something went wrong")
                        binding.shimmerPrescribed.visibility = View.GONE
                    }
                    AppProgressBar.hideLoaderDialog()

                }

            })
    }

    private fun setRecyclerViewAdapter(data: ArrayList<ResultPrePrescribed>) {
        binding.rvCancled.apply {
            shimmerFrameLayout?.startShimmer()
            binding.rvCancled.visibility = View.VISIBLE
            binding.shimmerPrescribed.visibility = View.GONE
            binding.tvNoDataFound.visibility = View.GONE
            adapter = AdapterPrescribed(requireContext(), data)
            AppProgressBar.hideLoaderDialog()

        }
    }

    private fun apiCallGetPrePending1() {
        AppProgressBar.showLoaderDialog(context)

        ApiClient.apiService.getPrescribed(sessionManager.id.toString())
            .enqueue(object : Callback<ModelPrescribed> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelPrescribed>, response: Response<ModelPrescribed>
                ) {
                    try {
                        if (response.code() == 200) {
                            count2 = 0
                            mainData = response.body()!!.result!!

                        }
                        if (response.code() == 500) {
                            myToast(requireActivity(), "Server Error")
                            binding.shimmerPrescribed.visibility = View.GONE
                            AppProgressBar.hideLoaderDialog()


                        } else if (response.body()!!.result.isEmpty()) {
                            binding.tvNoDataFound.visibility = View.VISIBLE
                            binding.shimmerPrescribed.visibility = View.GONE
                            // myToast(requireActivity(),"No Data Found")
                            AppProgressBar.hideLoaderDialog()

                        } else {
                            setRecyclerViewAdapter(mainData)
                            AppProgressBar.hideLoaderDialog()

                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        AppProgressBar.hideLoaderDialog()

                    }

                }

                override fun onFailure(call: Call<ModelPrescribed>, t: Throwable) {
                    count2++
                    if (count2 <= 3) {
                        apiCallGetPrePending1()
                    } else {
                        myToast(requireActivity(), "Something went wrong")
                        binding.shimmerPrescribed.visibility = View.GONE
                    }
                    AppProgressBar.hideLoaderDialog()

                }

            })
    }
}