package com.example.ehcf_doctor.Prescription.activity

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.ehcf.Helper.myToast
import com.example.ehcf.Prescription.adapter.AdapterPrescriptionDetial
import com.example.ehcf.Prescription.adapter.AdapterPrescriptionDetialDiagonsis
import com.example.ehcf.Prescription.adapter.AdapterPrescriptionDetialLabTest
import com.example.ehcf.sharedpreferences.SessionManager
import com.example.ehcf_doctor.Helper.AppProgressBar
import com.example.ehcf_doctor.Prescription.model.ModelPreDetJava
import com.example.ehcf_doctor.Retrofit.ApiClient
import com.example.ehcf_doctor.databinding.ActivityPrescriptionDetailsBinding
import com.rajat.pdfviewer.PdfViewerActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat

class PrescriptionDetails : AppCompatActivity() {
    private lateinit var binding: ActivityPrescriptionDetailsBinding
    private var context: Context = this@PrescriptionDetails
    var id = ""
    var customerName = ""
    var memberName = ""
    var resultDate = ""
    var objective = ""
    var subjectiv = ""
    var doctorNote = ""
    var assesment = ""
    var plan = ""
    var followUp = ""
    private var count = 0
    private lateinit var sessionManager: SessionManager

    var date = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPrescriptionDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sessionManager = SessionManager(this@PrescriptionDetails)

        binding.imgBack.setOnClickListener {
            onBackPressed()
        }
        id = intent.getStringExtra("Id").toString()
        customerName = intent.getStringExtra("customerName").toString()
        Log.e("predetialid", id)

        Log.e("NewDate", resultDate)

        apiCallPreDet()
        binding.tvCoustmorNamePreDet.text = customerName

        binding.tvDoctorNamePreDetial.text = sessionManager.doctorName
        binding.UHID.text = id
        val refreshListener = SwipeRefreshLayout.OnRefreshListener {
            overridePendingTransition(0, 0)
            finish()
            startActivity(intent)
            overridePendingTransition(0, 0)
        }
        binding.btnDownloadPrescription.setOnClickListener {
            startActivity(
                // Use 'launchPdfFromPath' if you want to use assets file (enable "fromAssets" flag) / internal directory
                PdfViewerActivity.launchPdfFromUrl(           //PdfViewerActivity.Companion.launchPdfFromUrl(..   :: incase of JAVA
                    context,
                    "https://ehcf.thedemostore.in/print/$id",                                // PDF URL in String format
                    "Prescription",                        // PDF Name/Title in String format
                    "Prescription Save to directory",                  // If nothing specific, Put "" it will save to Downloads
                    enableDownload = true                    // This param is true by defualt.
                )
            )
        }

    }

    private fun changeDateFormatFromAnother(date: String?): String? {
        @SuppressLint("SimpleDateFormat")
        val inputFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd")

        @SuppressLint("SimpleDateFormat")
        val outputFormat: DateFormat = SimpleDateFormat("dd MMMM yyyy")
        resultDate = ""
        try {
            resultDate = outputFormat.format(inputFormat.parse(date))
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return resultDate
    }

    private fun apiCallPreDet() {
        AppProgressBar.showLoaderDialog(context)

        ApiClient.apiService.getPrescriptionDetial(id)
            .enqueue(object : Callback<ModelPreDetJava> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelPreDetJava>, response: Response<ModelPreDetJava>
                ) {
                    try {

                        if (response.code() == 500) {
                            myToast(this@PrescriptionDetails, "Server Error")
                            AppProgressBar.hideLoaderDialog()
                        } else {
                            count = 0
                            binding.recyclerView.apply {
                                adapter = AdapterPrescriptionDetial(
                                    this@PrescriptionDetails,
                                    response.body()!!
                                )

                                for (i in response.body()!!.doctorNotes) {
                                    doctorNote = i.doctorNotes
                                    date = i.date
                                    assesment = i.assessment
                                    subjectiv = i.subjectiveInformation
                                    objective = i.objectiveInformation
                                    plan = i.plan
                                    if (i.end_follow_up_date != null) {
                                        followUp = i.end_follow_up_date
                                    }

                                }
                                changeDateFormatFromAnother(date)
                                binding.tvDatePreDetialPreDetial.text = resultDate
                                binding.tvPublicNote.text = doctorNote
                                binding.tvSubjective.text = subjectiv
                                binding.tvObjective.text = objective
                                binding.Plan.text = plan
                                binding.Assessment.text = assesment
                                binding.followUpdate.text = followUp


                            }
                            binding.recyclerViewDiagonosis.apply {
                                adapter = AdapterPrescriptionDetialDiagonsis(
                                    this@PrescriptionDetails,
                                    response.body()!!
                                )

                                AppProgressBar.hideLoaderDialog()

                            }

                            binding.recyclerViewLabTest.apply {
                                adapter = AdapterPrescriptionDetialLabTest(
                                    this@PrescriptionDetails,
                                    response.body()!!
                                )

                                AppProgressBar.hideLoaderDialog()

                            }

                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        AppProgressBar.hideLoaderDialog()

                    }


                }

                override fun onFailure(call: Call<ModelPreDetJava>, t: Throwable) {
                    count++
                    if (count <= 3) {
                        apiCallPreDet()
                    } else {
                        myToast(this@PrescriptionDetails, t.message.toString())
                    }
                    AppProgressBar.hideLoaderDialog()
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