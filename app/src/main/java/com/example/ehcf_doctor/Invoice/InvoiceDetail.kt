package com.example.ehcf_doctor.Invoice

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.ehcf.Helper.myToast
import com.example.ehcf.sharedpreferences.SessionManager
import com.example.ehcf_doctor.Helper.AppProgressBar
import com.example.ehcf_doctor.Invoice.adapter.AdapterInvoiceDetial
import com.example.ehcf_doctor.Invoice.model.ModelInvoiceDetial
import com.example.ehcf_doctor.Retrofit.ApiClient
import com.example.ehcf_doctor.databinding.ActivityInvoiceDetialBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.OutputStream

class InvoiceDetail : AppCompatActivity() {
    private lateinit var binding: ActivityInvoiceDetialBinding
    private val context: Context = this@InvoiceDetail
    var relationList = ArrayList<String>()
    var invoiceId = ""
    private val PERMISSION_REQUEST_CODE = 1
    private var count = 0
    private lateinit var sessionManager: SessionManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInvoiceDetialBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sessionManager = SessionManager(this)
        invoiceId = intent.getStringExtra("invoiceId").toString()

        apiCallInvoiceDetial()
        binding.imgBack.setOnClickListener {
            onBackPressed()
        }
        binding.btnDwnload.setOnClickListener {
            SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("You want to Download Invoice?")
                .setConfirmText("Yes")
                .setCancelText("No")
                .showCancelButton(true)
                .setConfirmClickListener { sDialog ->
                    sDialog.cancel()
                    captureCardViewAsImage(binding.cardView)
                }
                .setCancelClickListener { sDialog ->
                    sDialog.cancel()
                }
                .show()

        }


    }

    private fun captureCardViewAsImage(view: View) {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                PERMISSION_REQUEST_CODE
            )
            return
        }

        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)

        val filename = "INVOICE2023$invoiceId.png"
        val contentResolver = contentResolver
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(
                    MediaStore.MediaColumns.RELATIVE_PATH,
                    Environment.DIRECTORY_PICTURES + "/Invoices"
                )
                put(MediaStore.MediaColumns.IS_PENDING, 1)
            }
        }

        val collection = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        val imageUri = contentResolver.insert(collection, contentValues)

        imageUri?.let {
            try {
                val outputStream: OutputStream? = contentResolver.openOutputStream(imageUri)
                outputStream?.use {
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
                    it.flush()
                    it.close()
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    contentValues.clear()
                    contentValues.put(MediaStore.MediaColumns.IS_PENDING, 0)
                    contentResolver.update(imageUri, contentValues, null, null)
                    Toast.makeText(context, "Invoice Downloaded Successfully", Toast.LENGTH_SHORT)
                        .show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun apiCallInvoiceDetial() {
      AppProgressBar.showLoaderDialog(context)

        ApiClient.apiService.invoiceDetails(invoiceId)
            .enqueue(object : Callback<ModelInvoiceDetial> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onResponse(
                    call: Call<ModelInvoiceDetial>,
                    response: Response<ModelInvoiceDetial>
                ) {
                    try {
                        if (response.code() == 500) {
                            myToast(this@InvoiceDetail, "Server Error")
                        } else if (response.body()!!.status == 0) {
                            myToast(this@InvoiceDetail, "${response.body()!!.message}")
                            AppProgressBar.hideLoaderDialog()

                        } else if (response.body()!!.result.isEmpty()) {
                            binding.rvManageSlot.adapter =
                                AdapterInvoiceDetial(this@InvoiceDetail, response.body()!!)
                            binding.rvManageSlot.adapter!!.notifyDataSetChanged()
                            AppProgressBar.hideLoaderDialog()

                        } else {
                            count = 0
                            binding.rvManageSlot.adapter =
                                AdapterInvoiceDetial(this@InvoiceDetail, response.body()!!)
                            binding.rvManageSlot.adapter!!.notifyDataSetChanged()
                            binding.rvManageSlot.visibility = View.VISIBLE
                            AppProgressBar.hideLoaderDialog()
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
                    } catch (e: Exception) {
                        myToast(this@InvoiceDetail, "Something went wrong")
                        AppProgressBar.hideLoaderDialog()
                    }
                }

                override fun onFailure(call: Call<ModelInvoiceDetial>, t: Throwable) {
                    count++
                    if (count <= 3) {
                        apiCallInvoiceDetial()
                    } else {
                        myToast(this@InvoiceDetail, "Something went wrong")
                    }
                    AppProgressBar.hideLoaderDialog()

                }


            })
    }

}