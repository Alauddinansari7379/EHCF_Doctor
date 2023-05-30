package com.example.ehcf_doctor.Prescription.activity

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.ehcf.Helper.myToast
import com.example.ehcf_doctor.MainActivity.activity.MainActivity
import com.example.ehcf_doctor.ManageSlots.activity.ManageSlots
import com.example.ehcf_doctor.Prescription.adapter.AdapterPrescribed
import com.example.ehcf_doctor.R
import com.example.ehcf_doctor.databinding.ActivityViewReportJavaBinding
import com.rajat.pdfviewer.PdfViewerActivity
import java.io.IOException
import java.io.InputStream
import java.net.URL

class ViewReport : AppCompatActivity() {
    private lateinit var binding:ActivityViewReportJavaBinding
    private val context: Context = this@ViewReport

    var setView = 1
    var clickId = "0"
    var imageView: ImageView? = null
    var left: ImageView? = null
    var etURL: EditText? = null
    var clearbtn: Button? = null
    var fetchbtn: Button? = null
    var mainHandler = Handler()
    var progressDialog: ProgressDialog? = null
    var reportData=""

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityViewReportJavaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.imgBack.setOnClickListener {
            if (clickId=="1"){
                startActivity(Intent(this,MainActivity::class.java))
            }
            onBackPressed()
        }
        binding.btnDashboard.setOnClickListener {
            onBackPressed()
        }

        imageView = findViewById<View>(R.id.imageView2) as ImageView
//        etURL = findViewById<View>(R.id.etURL) as EditText

//        String url = etURL.getText().toString();



        reportData=intent.getStringExtra("report").toString()
        clickId=intent.getStringExtra("clickId").toString()
        Log.e("reportData",reportData)
        val baseUrl="https://ehcf.thedemostore.in/uploads/"

        if (reportData=="null"){
            SweetAlertDialog(this@ViewReport, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("No Report Found")
                .setConfirmText("Ok")
                //.setCancelText("Ok")
                .showCancelButton(true)
                .setConfirmClickListener { sDialog ->
                    sDialog.cancel()
                   onBackPressed()
                }
                .setCancelClickListener { sDialog ->
                    sDialog.cancel()
                }
                .show()
           // myToast(this,"No Report Found")
        }else{
            startActivity(
                // Use 'launchPdfFromPath' if you want to use assets file (enable "fromAssets" flag) / internal directory
                PdfViewerActivity.launchPdfFromUrl(           //PdfViewerActivity.Companion.launchPdfFromUrl(..   :: incase of JAVA
                    context,
                    "https://ehcf.thedemostore.in/uploads/$reportData",                                // PDF URL in String format
                    "Report",                        // PDF Name/Title in String format
                    "pdf directory to save",                  // If nothing specific, Put "" it will save to Downloads
                    enableDownload = true                    // This param is true by defualt.
                )
            )
//            val url= baseUrl+reportData
//            FetchImage(url).start()

        }


        // url1 = "https://ehcf.thedemostore.in/uploads/prescriptions/1679551088.jpg"
    }

    override fun onResume() {
        super.onResume()
    }
    internal inner class FetchImage(var URL: String) : Thread() {
        var bitmap: Bitmap? = null
        override fun run() {
            mainHandler.post {
                progressDialog = ProgressDialog(this@ViewReport)
                progressDialog!!.setMessage("Getting Report Image....")
                progressDialog!!.setCancelable(true)
                progressDialog!!.show()
            }
            var inputStream: InputStream? = null
            try {
                inputStream = URL(URL).openStream()
                bitmap = BitmapFactory.decodeStream(inputStream)
            } catch (e: IOException) {
                e.printStackTrace()
            }
            mainHandler.post {
                if (progressDialog!!.isShowing) progressDialog!!.dismiss()
                imageView!!.setImageBitmap(bitmap)
            }
        }
    }


}