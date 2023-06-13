package com.example.ehcf_doctor.Prescription.activity

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.ehcf.Helper.myToast
import com.example.ehcf.Prescription.adapter.AdapterPrescriptionDetial
import com.example.ehcf.Prescription.adapter.AdapterPrescriptionDetialDiagonsis
import com.example.ehcf.Prescription.adapter.AdapterPrescriptionDetialLabTest
import com.example.ehcf.sharedpreferences.SessionManager
import com.example.ehcf_doctor.Prescription.model.ModelPreDetJava
import com.example.ehcf_doctor.databinding.ActivityPrescriptionDetailsBinding
import com.example.myrecyview.apiclient.ApiClient
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
    private var progressDialog: ProgressDialog? = null
    var id = ""
    var customerName = ""
    var resultDate = ""
    var objective = ""
    var subjectiv = ""
    var doctorNote = ""
    var assesment = ""
    var plan = ""
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
        binding.tvDoctorNamePreDetial.text = sessionManager.doctorName
        binding.tvCoustmorNamePreDet.text = customerName
        binding.UHID.text = id
        val refreshListener = SwipeRefreshLayout.OnRefreshListener {
            overridePendingTransition(0, 0)
            finish()
            startActivity(intent)
            overridePendingTransition(0, 0)
        }
        binding.btnDownloadPrescription.setOnClickListener {
            SweetAlertDialog(this@PrescriptionDetails, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Are you sure want to Download?")
                .setCancelText("No")
                .setConfirmText("Yes")
                .showCancelButton(true)
                .setConfirmClickListener { sDialog ->
                    sDialog.cancel()

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
                    // completeSlot(bookingId)
//                    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://ehcf.thedemostore.in/print/$id"))
//                    startActivity(browserIntent)
                }
                .setCancelClickListener { sDialog ->
                    sDialog.cancel()
                }
                .show()


            //  startActivity(Intent(this@PrescriptionDetails,DownloadPrescription::class.java))
        }



//        binding.btnModifyPrescptionDet.setOnClickListener {
////            val intent = Intent(context as Activity, ViewReport::class.java)
////                .putExtra("report",)
////                .putExtra("clickId","1")
////            context.startActivity(intent)
//        }


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
        progressDialog = ProgressDialog(this@PrescriptionDetails)
        progressDialog!!.setMessage("Loading..")
        progressDialog!!.setTitle("Please Wait")
        progressDialog!!.isIndeterminate = false
        progressDialog!!.setCancelable(true)
        progressDialog!!.show()

        ApiClient.apiService.getPrescriptionDetial(id)
            .enqueue(object : Callback<ModelPreDetJava> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelPreDetJava>, response: Response<ModelPreDetJava>
                ) {

                    if (response.code() == 500) {
                        myToast(this@PrescriptionDetails, "Server Error")
                        progressDialog!!.dismiss()

                    } else {
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

                            }
                            changeDateFormatFromAnother(date)
                            binding.tvDatePreDetialPreDetial.text = resultDate
                            binding.tvPublicNote.text = doctorNote
                            binding.tvSubjective.text = subjectiv
                            binding.tvObjective.text = objective
                            binding.Plan.text = plan
                            binding.Assessment.text = assesment


                        }
                        binding.recyclerViewDiagonosis.apply {
                            adapter = AdapterPrescriptionDetialDiagonsis(
                                this@PrescriptionDetails,
                                response.body()!!
                            )

                            progressDialog!!.dismiss()

                        }
//                        binding.recyclerViewNote.apply {
//                            adapter = AdapterPrescriptionDetialDoctorNote(
//                                this@PrescriptionDetails,
//                                response.body()!!
//                            )
//
//                            progressDialog!!.dismiss()

                        //          }

                        binding.recyclerViewLabTest.apply {
                            adapter = AdapterPrescriptionDetialLabTest(
                                this@PrescriptionDetails,
                                response.body()!!
                            )

                            progressDialog!!.dismiss()

                        }

//
//                        Log.e("noteeeeee",response.body()!!.doctorNotes.doctorNotes)
//                        Log.e("Tag", response.body()!!.Result().doctorNotes.toString())
//                        binding.Note.text= response.body()!!.Result().doctorNotes.toString()
//                        progressDialog!!.dismiss()

                    }


                }

                override fun onFailure(call: Call<ModelPreDetJava>, t: Throwable) {
                    myToast(this@PrescriptionDetails, t.message.toString())
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