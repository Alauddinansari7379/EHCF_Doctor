package com.example.ehcf_doctor.ManageSlots.activity

import android.app.ProgressDialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.ehcf.Helper.myToast
import com.example.ehcf.sharedpreferences.SessionManager
import com.example.ehcf_doctor.ManageSlots.adapter.AdapterManageSlot
import com.example.ehcf_doctor.ManageSlots.model.ModelManageSlotRes
import com.example.ehcf_doctor.R
import com.example.ehcf_doctor.Retrofit.ApiInterface
import com.example.ehcf_doctor.databinding.ActivityManageSlotsSeassionBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ManageSlotsSeassion : AppCompatActivity() {
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
       // apiCall()
        relationList.add("Sunday")
        relationList.add("Monday")
        relationList.add("Tuesday")
        relationList.add("Wednesday")
        relationList.add("Thursday")
        relationList.add("Friday")
        relationList.add("Saturday")
        binding.spinnerDays.adapter =
            ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, relationList)
    }

    private fun apiCall(){

        progressDialog = ProgressDialog(this@ManageSlotsSeassion)
        progressDialog!!.setMessage("Loading..")
        progressDialog!!.setTitle("Please Wait")
        progressDialog!!.isIndeterminate = false
        progressDialog!!.setCancelable(true)
        progressDialog!!.show()

        val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            //.baseUrl("https://jsonplaceholder.typicode.com/")
            .baseUrl("https://ehcf.thedemostore.in/api/customer/")
            .build()
            .create(ApiInterface::class.java)

        val retrofitData = retrofitBuilder.slotManagement(sessionManager.id.toString())
        retrofitData.enqueue(object : Callback<ModelManageSlotRes> {
            override fun onResponse(
                call: Call<ModelManageSlotRes>,
                response: Response<ModelManageSlotRes>
            )
            {
                val recyclerView = findViewById<RecyclerView>(R.id.rvManageSlot)
                recyclerView.apply {
                    adapter = AdapterManageSlot(context, response.body()!!)
                    progressDialog!!.dismiss()

                }
            }

            override fun onFailure(call: Call<ModelManageSlotRes>, t: Throwable) {
                t.message?.let { myToast(this@ManageSlotsSeassion, it)
                    progressDialog!!.dismiss()

                }
            }
        })
    }

}
