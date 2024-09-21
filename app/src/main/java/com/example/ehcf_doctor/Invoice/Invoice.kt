package com.example.ehcf_doctor.Invoice

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.ContentValues
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.ehcf.Helper.myToast
import com.example.ehcf.sharedpreferences.SessionManager
import com.example.ehcf_doctor.Helper.AppProgressBar
import com.example.ehcf_doctor.Invoice.adapter.AdapterInvoice
import com.example.ehcf_doctor.Invoice.model.ModelInvoice
import com.example.ehcf_doctor.R
import com.example.ehcf_doctor.Retrofit.ApiClient
import com.example.ehcf_doctor.databinding.ActivityInvoiceBinding
import com.facebook.shimmer.ShimmerFrameLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class Invoice : AppCompatActivity() {
    private lateinit var binding: ActivityInvoiceBinding
    private val context: Context = this@Invoice
    var mydilaog: Dialog? = null
    var search = ""
    var selectedDate = ""
    var relationList = ArrayList<String>()
    var shimmerFrameLayout: ShimmerFrameLayout? = null
    private var count = 0
    private var count2 = 0

    private lateinit var sessionManager: SessionManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInvoiceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sessionManager = SessionManager(this)
        shimmerFrameLayout = findViewById(R.id.shimmerInvoice)
        shimmerFrameLayout!!.startShimmer()
        apiCallInvoiceList()

        binding.imgBack.setOnClickListener {
            onBackPressed()
        }
        binding.layoutShowAll.setOnClickListener {
            overridePendingTransition(0, 0)
            finish()
            startActivity(intent)
            overridePendingTransition(0, 0)
        }
        binding.imgSearch.setOnClickListener {
            if (binding.edtSearch.text.toString().isEmpty()) {
                binding.edtSearch.error = "Enter Patient Name"
                binding.edtSearch.requestFocus()
            } else {
                selectedDate = ""
                search = binding.edtSearch.text.toString()
                apiCallFilterInvoiceList(search, selectedDate)
            }

        }


        val currentDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
        binding.tvDate.text = currentDate

        mydilaog?.setCanceledOnTouchOutside(false)
        mydilaog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val newCalendar1 = Calendar.getInstance()
        val datePicker = DatePickerDialog(
            this,
            { _, year, monthOfYear, dayOfMonth ->
                val newDate = Calendar.getInstance()
                newDate[year, monthOfYear] = dayOfMonth
                DateFormat.getDateInstance().format(newDate.time)
                // val Date = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(newDate.time)
                selectedDate =
                    SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(newDate.time)
                val date = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(newDate.time)
                binding.tvDate.text = date
                search = ""
                apiCallFilterInvoiceList(search, selectedDate)
                Log.e(ContentValues.TAG, "onCreate: >>>>>>>>>>>>>>>>>>>>>>$date")
                Log.e(ContentValues.TAG, "selectedDate: >>$selectedDate")
            },
            newCalendar1[Calendar.YEAR],
            newCalendar1[Calendar.MONTH],
            newCalendar1[Calendar.DAY_OF_MONTH]
        )
        binding.layoutFilter.setOnClickListener {
            datePicker.show()

        }


    }

    private fun apiCallInvoiceList() {
        AppProgressBar.showLoaderDialog(context)

        ApiClient.apiService.invoiceList(sessionManager.id.toString())
            .enqueue(object : Callback<ModelInvoice> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onResponse(
                    call: Call<ModelInvoice>,
                    response: Response<ModelInvoice>
                ) {
                    try {
                        if (response.code() == 500) {
                            myToast(this@Invoice, "Server Error")
                            binding.shimmerInvoice.visibility = View.GONE
                        } else if (response.body()!!.status == 0) {
                            binding.tvNoDataFound.visibility = View.VISIBLE
                            binding.shimmerInvoice.visibility = View.GONE
                            myToast(this@Invoice, "${response.body()!!.message}")
                            AppProgressBar.hideLoaderDialog()
                        } else if (response.body()!!.result.isEmpty()) {
                            binding.rvManageSlot.adapter =
                                AdapterInvoice(this@Invoice, response.body()!!)
                            binding.rvManageSlot.adapter!!.notifyDataSetChanged()
                            binding.tvNoDataFound.visibility = View.VISIBLE
                            binding.shimmerInvoice.visibility = View.GONE
                            myToast(this@Invoice, "No Invoice Found")
                            AppProgressBar.hideLoaderDialog()

                        } else {
                            count = 0
                            binding.rvManageSlot.adapter =
                                AdapterInvoice(this@Invoice, response.body()!!)
                            binding.rvManageSlot.adapter!!.notifyDataSetChanged()
                            binding.tvNoDataFound.visibility = View.GONE
                            shimmerFrameLayout?.startShimmer()
                            binding.rvManageSlot.visibility = View.VISIBLE
                            binding.shimmerInvoice.visibility = View.GONE
                            AppProgressBar.hideLoaderDialog()
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        myToast(this@Invoice, "Something went wrong")
                        binding.shimmerInvoice.visibility = View.GONE
                        AppProgressBar.hideLoaderDialog()
                    }
                }

                override fun onFailure(call: Call<ModelInvoice>, t: Throwable) {

                    count++
                    if (count <= 3) {
                        apiCallInvoiceList()
                    } else {
                        myToast(this@Invoice, "Something went wrong")
                        binding.shimmerInvoice.visibility = View.GONE
                    }
                    AppProgressBar.hideLoaderDialog()
                }


            })
    }

    private fun apiCallFilterInvoiceList(search: String, date: String?) {
        AppProgressBar.showLoaderDialog(context)

        ApiClient.apiService.searchPatientsDate(search, date, sessionManager.id.toString())
            .enqueue(object : Callback<ModelInvoice> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onResponse(
                    call: Call<ModelInvoice>,
                    response: Response<ModelInvoice>
                ) {
                    try {
                        if (response.code() == 500) {
                            myToast(this@Invoice, "Server Error")
                            binding.shimmerInvoice.visibility = View.GONE
                        } else if (response.body()!!.status == 0) {
                            binding.tvNoDataFound.visibility = View.VISIBLE
                            binding.shimmerInvoice.visibility = View.GONE
                            binding.edtSearch.text.clear()
                            myToast(this@Invoice, "${response.body()!!.message}")
                            AppProgressBar.hideLoaderDialog()

                        } else if (response.body()!!.result.isEmpty()) {
                            binding.rvManageSlot.adapter =
                                AdapterInvoice(this@Invoice, response.body()!!)
                            binding.rvManageSlot.adapter!!.notifyDataSetChanged()
                            binding.tvNoDataFound.visibility = View.VISIBLE
                            binding.shimmerInvoice.visibility = View.GONE
                            binding.edtSearch.text.clear()
                            myToast(this@Invoice, "No Invoice Found")
                            AppProgressBar.hideLoaderDialog()

                        } else {
                            count2 = 0
                            binding.rvManageSlot.adapter =
                                AdapterInvoice(this@Invoice, response.body()!!)
                            binding.rvManageSlot.adapter!!.notifyDataSetChanged()
                            binding.tvNoDataFound.visibility = View.GONE
                            shimmerFrameLayout?.startShimmer()
                            binding.rvManageSlot.visibility = View.VISIBLE
                            binding.shimmerInvoice.visibility = View.GONE
                            binding.edtSearch.text.clear()
                            AppProgressBar.hideLoaderDialog()
                        }
                    } catch (e: Exception) {
                        myToast(this@Invoice, "Something went wrong")
                        binding.shimmerInvoice.visibility = View.GONE
                        AppProgressBar.hideLoaderDialog()
                    }
                }

                override fun onFailure(call: Call<ModelInvoice>, t: Throwable) {
                    count2++
                    if (count2 <= 3) {
                        apiCallFilterInvoiceList(search, date)
                    } else {
                        myToast(this@Invoice, "Something went wrong")
                        binding.shimmerInvoice.visibility = View.GONE
                    }
                    AppProgressBar.hideLoaderDialog()

                }


            })
    }


}