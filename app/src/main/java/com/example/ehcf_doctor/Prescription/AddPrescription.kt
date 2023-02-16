package com.example.ehcf_doctor.Prescription

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.example.ehcf.Helper.myToast
import com.example.ehcf.sharedpreferences.SessionManager
import com.example.ehcf_doctor.Login.activity.SignIn
import com.example.ehcf_doctor.MainActivity.model.ModelOnline
import com.example.ehcf_doctor.R
import com.example.ehcf_doctor.Registration.modelResponse.ModelSpecilList
import com.example.ehcf_doctor.Registration.modelResponse.RegistationResponse
import com.example.ehcf_doctor.databinding.ActivityAddPrescriptionBinding
import com.example.myrecyview.apiclient.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddPrescription : AppCompatActivity() {
    private lateinit var binding:ActivityAddPrescriptionBinding
    var bookingId=""
    private val context: Context = this@AddPrescription
    private lateinit var sessionManager: SessionManager
    var progressDialog: ProgressDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityAddPrescriptionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bookingId = intent.getStringExtra("bookingId").toString()
        Log.e("bookingId", bookingId)

        binding.imgBack.setOnClickListener {
            onBackPressed()
        }
    }
    private fun apiCall() {
        progressDialog = ProgressDialog(this@AddPrescription)
        progressDialog!!.setMessage("Loading...")
        progressDialog!!.setTitle("Please Wait")
        progressDialog!!.isIndeterminate = false
        progressDialog!!.setCancelable(true)
        progressDialog!!.show()

        val subjective=binding.edtSubjectiveInformation.text.toString()
        val objective=binding.edtObjectiveInformation.text.toString()
        val assessment=binding.edtAssessment.text.toString()
        val plan=binding.edtPlan.text.toString()
        val doctorNotes=binding.edtDoctorNotes.text.toString()


        ApiClient.apiService.createPrescription(bookingId,subjective,objective,assessment,plan,doctorNotes)
            .enqueue(object : Callback<ModelOnline> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelOnline>, response: Response<ModelOnline>
                ) {
                    Log.e("Ala", "${response.body()!!}")
                    Log.e("Ala", response.body()!!.message)
                    Log.e("Ala", "${response.body()!!.status}")

                    if (response.body()!!.status == 1) {
                        myToast(this@AddPrescription, response.body()!!.message)
                        progressDialog!!.dismiss()
                        val intent = Intent(applicationContext, SignIn::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                        finish()
                        startActivity(intent)
                    } else {
                        myToast(this@AddPrescription, "${response.body()!!.message}")
                        progressDialog!!.dismiss()

                    }

                }

                override fun onFailure(call: Call<ModelOnline>, t: Throwable) {
                    myToast(this@AddPrescription, t.message.toString())
                    progressDialog!!.dismiss()

                }

            })
    }


}