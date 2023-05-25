package com.example.ehcf_doctor.Prescription.activity

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.ehcf.Helper.isOnline
import com.example.ehcf.Helper.myToast
import com.example.ehcf_doctor.Prescription.adapter.AdapterPrescribed
import com.example.ehcf_doctor.Prescription.adapter.AdapterPrescriptionDetial
import com.example.ehcf_doctor.Prescription.model.ModelPreDetails
import com.example.ehcf_doctor.Prescription.model.ModelPrescribed
import com.example.ehcf_doctor.databinding.ActivityPrescriptionDetailsBinding
import com.example.myrecyview.apiclient.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import rezwan.pstu.cse12.youtubeonlinestatus.recievers.NetworkChangeReceiver

class PrescriptionDetails : AppCompatActivity() {
    private lateinit var binding: ActivityPrescriptionDetailsBinding
    private var context: Context = this@PrescriptionDetails
    private var progressDialog: ProgressDialog? = null
    var id = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPrescriptionDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.imgBack.setOnClickListener {
            onBackPressed()
        }
        id = intent.getStringExtra("Id").toString()
        Log.e("predetialid", id)
        apiCallGetPrePending()

        val refreshListener = SwipeRefreshLayout.OnRefreshListener {
            overridePendingTransition(0, 0)
            finish()
            startActivity(intent)
            overridePendingTransition(0, 0)
        }
        binding.swipeRefreshLayout.setOnRefreshListener(refreshListener)

    }


    private fun apiCallGetPrePending() {
        progressDialog = ProgressDialog(this@PrescriptionDetails)
        progressDialog!!.setMessage("Loading..")
        progressDialog!!.setTitle("Please Wait")
        progressDialog!!.isIndeterminate = false
        progressDialog!!.setCancelable(true)
        progressDialog!!.show()

        ApiClient.apiService.getPrescriptionDetial(id)
            .enqueue(object : Callback<ModelPreDetails> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelPreDetails>, response: Response<ModelPreDetails>
                ) {

                    if (response.body()!!.result.isEmpty()) {
                        myToast(this@PrescriptionDetails,"No Data Found")
                        progressDialog!!.dismiss()

                    } else {
                        binding.recyclerView.apply {
                            adapter = AdapterPrescriptionDetial(this@PrescriptionDetails, response.body()!!)
                            progressDialog!!.dismiss()

                        }
                    }

                }

                override fun onFailure(call: Call<ModelPreDetails>, t: Throwable) {
                    myToast(this@PrescriptionDetails, "Something went wrong")
                    progressDialog!!.dismiss()

                }

            })
    }
    override fun onResume() {
        super.onResume()
      //  refresh()
        }
    fun refresh() {
        overridePendingTransition(0, 0)
        finish()
        startActivity(intent)
        overridePendingTransition(0, 0)
    }
}