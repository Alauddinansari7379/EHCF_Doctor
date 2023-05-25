package com.example.ehcf_doctor.Prescription.activity

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.ehcf.Helper.isOnline
import com.example.ehcf.Helper.myToast
import com.example.ehcf.sharedpreferences.SessionManager
import com.example.ehcf_doctor.Prescription.adapter.AdapterDigonisis
import com.example.ehcf_doctor.Prescription.adapter.AdapterOrderDetails
import com.example.ehcf_doctor.Prescription.model.*
import com.example.ehcf_doctor.databinding.ActivityAddPrescriptionBinding
import com.example.myrecyview.apiclient.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import rezwan.pstu.cse12.youtubeonlinestatus.recievers.NetworkChangeReceiver
import java.util.ArrayList

class AddPrescription : AppCompatActivity() {
    private lateinit var binding: ActivityAddPrescriptionBinding
    var bookingId = ""
    private val context: Context = this@AddPrescription
    private lateinit var sessionManager: SessionManager
    var progressDialog: ProgressDialog? = null
    var isTest = "0"
    private val medicineList = ArrayList<ModelOrderDetails>()
    private val diagnosisList = ArrayList<ModelDigonsis>()
    var btnId = "0"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPrescriptionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bookingId = intent.getStringExtra("bookingId").toString()
        btnId = intent.getStringExtra("btnId").toString()
        Log.e("bookingId", bookingId)

        binding.imgBack.setOnClickListener {
            onBackPressed()
        }

        binding.checkTest.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked){
                binding.layoutTest.visibility=View.VISIBLE
            } else{
                binding.layoutTest.visibility=View.GONE

            }
        }

        if (btnId == "1") {
            binding.btnAddPrescription.text = "Modify Prescription"
            binding.tvTitle.text = "Modify Prescription"
        }


        binding.btnAddPrescription.setOnClickListener {
            if (binding.checkTest.isChecked) {
                isTest = "1"
            }
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
            } else if (btnId == "1") {
                apiCallModifyPrescrption()
            } else {
                apiCall()

            }

        }
        binding.tvTitle.setOnClickListener {
            //apiCallDiagnosis()
        }
        binding.btnAddMedicine.setOnClickListener {
            setRecyclerDataMedicine(
                binding.edtMedicineName.text.toString(),
                binding.edtTiming.text.toString(),
                binding.edtIntake.text.toString(),
                binding.edtFrequency.text.toString(),
                binding.edtDuration.text.toString(),
            )
            binding.llayout3.visibility = View.VISIBLE
            binding.edtMedicineName.text.clear()
            binding.edtTiming.text.clear()
            binding.edtIntake.text.clear()
            binding.edtFrequency.text.clear()
            binding.edtDuration.text.clear()
            binding.edtMedicineName.requestFocus()
        }
        binding.btnAddDiagnosis.setOnClickListener {
            setRecyclerDataDiagnosis(
                binding.edtDiagnosis.text.toString(),
                binding.edtdescription.text.toString(),
            )
            binding.layoutDiagnosis.visibility = View.VISIBLE
            binding.edtDiagnosis.requestFocus()
            binding.edtdescription.text.clear()
            binding.edtDiagnosis.text.clear()
        }

    }

    private fun setRecyclerDataMedicine(
        medicineName: String,
        timing: String,
        intake: String,
        frequency: String,
        duration: String,
    ) {
        medicineList.add(ModelOrderDetails(medicineName, timing, intake, frequency, duration))
        binding.rvrecyclerView.adapter = AdapterOrderDetails(this, medicineList)

    }
    private fun setRecyclerDataDiagnosis(
        dignosis: String,
        desctption: String,
    ) {
        diagnosisList.add(ModelDigonsis(dignosis,desctption))
        binding.rvrecyclerViewDigonis.adapter = AdapterDigonisis(this, diagnosisList)

    }

    private fun apiCall() {
        progressDialog = ProgressDialog(this@AddPrescription)
        progressDialog!!.setMessage("Loading...")
        progressDialog!!.setTitle("Please Wait")
        progressDialog!!.isIndeterminate = false
        progressDialog!!.setCancelable(true)
        progressDialog!!.show()

        val subjective = binding.edtSubjectiveInformation.text.toString()
        val objective = binding.edtObjectiveInformation.text.toString()
        val assessment = binding.edtAssessment.text.toString()
        val plan = binding.edtPlan.text.toString()
        val doctorNotes = binding.edtDoctorNotes.text.toString()
        Log.e("test", isTest)


        ApiClient.apiService.createPrescription(
            bookingId,
            subjective,
            objective,
            assessment,
            plan,
            doctorNotes,
            isTest,
            binding.edtTestName.text.toString(),
            binding.edtInstructions.text.toString(),
            "5"
        )
            .enqueue(object : Callback<ModelPreJava> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelPreJava>, response: Response<ModelPreJava>
                ) {

                    if (response.code() == 500) {
                        myToast(this@AddPrescription, "Server Error")
                        progressDialog!!.dismiss()
                    } else if (response.code() == 200) {
                        myToast(this@AddPrescription, response.body()!!.result)
                        val preId=response.body()!!.data.id.toString()
                       // Log.e("Iddddddd",preId)
                        progressDialog!!.dismiss()
                        apiCallDiagnosis(preId)

                    } else {
                        myToast(this@AddPrescription, "${response.body()!!.result}")
                        progressDialog!!.dismiss()

                    }

                }

                override fun onFailure(call: Call<ModelPreJava>, t: Throwable) {
                    myToast(this@AddPrescription, "Something went wrong")
                    progressDialog!!.dismiss()

                }

            })
    }

    private fun apiCallMedicine(preId: String) {
        progressDialog = ProgressDialog(this@AddPrescription)
        progressDialog!!.setMessage("Loading...")
        progressDialog!!.setTitle("Please Wait")
        progressDialog!!.isIndeterminate = false
        progressDialog!!.setCancelable(true)
        // progressDialog!!.show()

        for (i in medicineList) {

            ApiClient.apiService.createMedicine(
                preId,
                i.medicineName,
                i.medicineTiming,
                i.intake,
                i.frequency,
                i.duration,

                )
                .enqueue(object : Callback<ModeMedicine> {
                    @SuppressLint("LogNotTimber")
                    override fun onResponse(
                        call: Call<ModeMedicine>,
                        response: Response<ModeMedicine>
                    ) {

                        if (response.code() == 500) {
                            myToast(this@AddPrescription, "Server Error")
                            // progressDialog!!.dismiss()
                        } else if (response.code() == 200) {
                            //  myToast(this@AddPrescription, response.body()!!.result)
                            //  progressDialog!!.dismiss()

                        } else {
                            myToast(this@AddPrescription, "${response.body()!!.message}")
                            progressDialog!!.dismiss()

                        }

                    }

                    override fun onFailure(call: Call<ModeMedicine>, t: Throwable) {
                        myToast(this@AddPrescription, "Something went wrong")
                        // progressDialog!!.dismiss()

                    }

                })
        }
    }
    private fun apiCallDiagnosis(preId: String) {
        progressDialog = ProgressDialog(this@AddPrescription)
        progressDialog!!.setMessage("Loading...")
        progressDialog!!.setTitle("Please Wait")
        progressDialog!!.isIndeterminate = false
        progressDialog!!.setCancelable(true)
        // progressDialog!!.show()
        apiCallMedicine(preId)

        for (i in diagnosisList) {
            ApiClient.apiService.createDiagnosis(
                preId,
                i.digonicName,
                i.description,

                )
                .enqueue(object : Callback<ModeMedicine> {
                    @SuppressLint("LogNotTimber")
                    override fun onResponse(
                        call: Call<ModeMedicine>,
                        response: Response<ModeMedicine>
                    ) {

                        if (response.code() == 500) {
                            myToast(this@AddPrescription, "Server Error")
                            // progressDialog!!.dismiss()
                        } else if (response.code() == 200) {
                              myToast(this@AddPrescription, response.body()!!.message)
                            //  progressDialog!!.dismiss()
                            onBackPressed()

                        } else {
                            myToast(this@AddPrescription, "${response.body()!!.message}")
                            progressDialog!!.dismiss()

                        }

                    }

                    override fun onFailure(call: Call<ModeMedicine>, t: Throwable) {
                        myToast(this@AddPrescription, "Something went wrong")
                        // progressDialog!!.dismiss()

                    }

                })
        }
    }

    private fun apiCallModifyPrescrption() {
        progressDialog = ProgressDialog(this@AddPrescription)
        progressDialog!!.setMessage("Loading...")
        progressDialog!!.setTitle("Please Wait")
        progressDialog!!.isIndeterminate = false
        progressDialog!!.setCancelable(true)
        progressDialog!!.show()

        val subjective = binding.edtSubjectiveInformation.text.toString()
        val objective = binding.edtObjectiveInformation.text.toString()
        val assessment = binding.edtAssessment.text.toString()
        val plan = binding.edtPlan.text.toString()
        val doctorNotes = binding.edtDoctorNotes.text.toString()
        Log.e("test", isTest)


        ApiClient.apiService.modifyPrescrption(
            bookingId,
            subjective,
            objective,
            assessment,
            plan,
            isTest,
            doctorNotes,

            )
            .enqueue(object : Callback<ModelModify> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelModify>, response: Response<ModelModify>
                ) {

                    if (response.code() == 500) {
                        myToast(this@AddPrescription, "Server Error")
                        progressDialog!!.dismiss()
                    } else if (response.code() == 200) {
                        myToast(this@AddPrescription, response.body()!!.result)
                        progressDialog!!.dismiss()
                        onBackPressed()

                    } else {
                        myToast(this@AddPrescription, "${response.body()!!.result}")
                        progressDialog!!.dismiss()

                    }

                }

                override fun onFailure(call: Call<ModelModify>, t: Throwable) {
                    myToast(this@AddPrescription, "Something went wrong")
                    progressDialog!!.dismiss()

                }

            })
    }


    override fun onStart() {
        super.onStart()
        if (isOnline(this)) {
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