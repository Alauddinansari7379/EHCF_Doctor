package com.example.ehcf_doctor.ManageSlots.activity

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.core.os.postDelayed
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.ehcf.Helper.myToast
import com.example.ehcf.sharedpreferences.SessionManager
import com.example.ehcf_doctor.Login.activity.SignIn
import com.example.ehcf_doctor.ManageSlots.adapter.AdapterSlotsList
import com.example.ehcf_doctor.ManageSlots.model.ModelDeleteSlot
import com.example.ehcf_doctor.ManageSlots.model.ModelSlotList
import com.example.ehcf_doctor.databinding.ActivityManageSlotsSeassionBinding
import com.example.myrecyview.apiclient.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ManageSlotsSeassion : AppCompatActivity(),AdapterSlotsList.DeleteSlot {
    private val context: Context =this@ManageSlotsSeassion
    var progressDialog : ProgressDialog?=null
    var relationList = ArrayList<String>()
    private lateinit var sessionManager: SessionManager


    private lateinit var binding: ActivityManageSlotsSeassionBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityManageSlotsSeassionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sessionManager = SessionManager(this)
        binding.imgBack.setOnClickListener {
            onBackPressed()
        }
        Handler(Looper.getMainLooper()).postDelayed(300) {
            apiCall()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun apiCall() {

        progressDialog = ProgressDialog(this@ManageSlotsSeassion)
        progressDialog!!.setMessage("Loading..")
        progressDialog!!.setTitle("Please Wait")
        progressDialog!!.isIndeterminate = false
        progressDialog!!.setCancelable(true)
        progressDialog!!.show()


        ApiClient.apiService.getTimeSlot(sessionManager.id.toString()).enqueue(object :Callback<ModelSlotList>
        {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call: Call<ModelSlotList>,
                response: Response<ModelSlotList>
            ) {
                if (response.body()!!.result.isEmpty()) {
                    binding.tvNoDataFound.visibility = View.VISIBLE
                    myToast(this@ManageSlotsSeassion,"No Slot Found")
                    progressDialog!!.dismiss()

                } else {
                    binding.rvManageSlot.apply {
                        binding.tvNoDataFound.visibility = View.GONE
                        // myToast(this@ShuduleTiming, response.body()!!.message)
                        adapter = AdapterSlotsList(this@ManageSlotsSeassion, response.body()!!,this@ManageSlotsSeassion)
                        progressDialog!!.dismiss()

                    }
                }
            }

            override fun onFailure(call: Call<ModelSlotList>, t: Throwable) {

            }


        })
    }

    private fun callAPIDelete(slotId: String){
        progressDialog = ProgressDialog(this@ManageSlotsSeassion)
        progressDialog!!.setMessage("Loading..")
        progressDialog!!.setTitle("Please Wait")
        progressDialog!!.isIndeterminate = false
        progressDialog!!.setCancelable(true)
        progressDialog!!.show()


        ApiClient.apiService.deleteSlot(slotId).enqueue(object :Callback<ModelDeleteSlot>
        {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call: Call<ModelDeleteSlot>,
                response: Response<ModelDeleteSlot>
            ) {
                if (response.body()!!.status==1) {
                    myToast(this@ManageSlotsSeassion,response.body()!!.message)
                    overridePendingTransition(0, 0)
                    finish()
                    startActivity(intent)
                    overridePendingTransition(0, 0)
                 //   binding.rvManageSlot.adapter!!.notifyDataSetChanged()
                    // myToast(requireActivity(),"No Data Found")
                    progressDialog!!.dismiss()

                } else {
                    myToast(this@ManageSlotsSeassion,response.body()!!.message)
                }
            }


            override fun onFailure(call: Call<ModelDeleteSlot>, t: Throwable) {

            }


        })


    }
     override fun deleteSlotApi(slotId: String) {
         SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
             .setTitleText("Are you sure want to Delete?")
             .setCancelText("No")
             .setConfirmText("Yes")
             .showCancelButton(true)
             .setConfirmClickListener { sDialog ->
                 sDialog.cancel()
                 callAPIDelete(slotId)

             }
             .setCancelClickListener { sDialog ->
                 sDialog.cancel()
             }
             .show()

     }
     override fun updateSlotApi(slotId: String) {

         progressDialog = ProgressDialog(this@ManageSlotsSeassion)
         progressDialog!!.setMessage("Loading..")
         progressDialog!!.setTitle("Please Wait")
         progressDialog!!.isIndeterminate = false
         progressDialog!!.setCancelable(true)
         progressDialog!!.show()


         ApiClient.apiService.deleteSlot(slotId).enqueue(object :Callback<ModelDeleteSlot>
         {
             @SuppressLint("NotifyDataSetChanged")
             override fun onResponse(
                 call: Call<ModelDeleteSlot>,
                 response: Response<ModelDeleteSlot>
             ) {
                 if (response.body()!!.status==1) {
                     myToast(this@ManageSlotsSeassion,response.body()!!.message)
                     // myToast(requireActivity(),"No Data Found")
                     progressDialog!!.dismiss()
                     apiCall()
                 } else {
                     myToast(this@ManageSlotsSeassion,response.body()!!.message)
                 }
             }


             override fun onFailure(call: Call<ModelDeleteSlot>, t: Throwable) {

             }


         })
     }

 }
