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
import com.example.ehcf_doctor.Prescription.model.ModelCreatePrescription
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
        binding.btnAddPrescription.setOnClickListener {
            if (binding.edtSubjectiveInformation.text.isEmpty()) {
                binding.edtSubjectiveInformation.error = "Enter SubjectiveInformation"
                binding.edtSubjectiveInformation.requestFocus()
                return@setOnClickListener
            }
            if (binding.edtObjectiveInformation.text.isEmpty()) {
                binding.edtObjectiveInformation.error = "Enter ObjectiveInformation"
                binding.edtObjectiveInformation.requestFocus()
                return@setOnClickListener
            }
            if (binding.edtAssessment.text.isEmpty()) {
                binding.edtAssessment.error = "Enter Assessment"
                binding.edtAssessment.requestFocus()
                return@setOnClickListener
            }
            if (binding.edtPlan.text.isEmpty()) {
                binding.edtPlan.error = "Enter Plan"
                binding.edtPlan.requestFocus()
                return@setOnClickListener
            }
            if (binding.edtDoctorNotes.text.isEmpty()) {
                binding.edtDoctorNotes.error = "Enter Doctor Notes"
                binding.edtDoctorNotes.requestFocus()
                return@setOnClickListener
            }else{
                apiCall()

            }

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
            .enqueue(object : Callback<ModelCreatePrescription> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelCreatePrescription>, response: Response<ModelCreatePrescription>
                ) {
                    Log.e("Ala", "${response.body()!!}")
                    Log.e("Ala", "${response.body()!!.status}")

                    if (response.body()!!.status == 1) {
                        myToast(this@AddPrescription, response.body()!!.result)
                        progressDialog!!.dismiss()
                        onBackPressed()
                    } else {
                        myToast(this@AddPrescription, "${response.body()!!.result}")
                        progressDialog!!.dismiss()

                    }

                }

                override fun onFailure(call: Call<ModelCreatePrescription>, t: Throwable) {
                    myToast(this@AddPrescription, t.message.toString())
                    progressDialog!!.dismiss()

                }

            })
    }


}