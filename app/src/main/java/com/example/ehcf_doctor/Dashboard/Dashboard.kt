package com.example.ehcf_doctor.Dashboard

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.ProgressDialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.ehcf.Helper.myToast
import com.example.ehcf.sharedpreferences.SessionManager
import com.example.ehcf_doctor.Appointments.Upcoming.activity.ScreenRecorder
import com.example.ehcf_doctor.Dashboard.adapter.AdapterDashboard
import com.example.ehcf_doctor.Dashboard.model.ModelDashboard
import com.example.ehcf_doctor.Helper.AppProgressBar
import com.example.ehcf_doctor.Login.activity.SignIn
import com.example.ehcf_doctor.databinding.ActivityDashboardBinding
import com.example.ehcf_doctor.Retrofit.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import rezwan.pstu.cse12.youtubeonlinestatus.recievers.NetworkChangeReceiver
import xyz.teamgravity.checkinternet.CheckInternet
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class Dashboard : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardBinding
    var mydilaog: Dialog? = null
    private lateinit var sessionManager: SessionManager
    private val context: Context = this@Dashboard
    var selectedDate = ""
    private var count = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sessionManager = SessionManager(this)

        binding.imgBack.setOnClickListener {
            onBackPressed()
        }
        selectedDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        Log.e(ContentValues.TAG, "selectedDate: >>$selectedDate")
        apiCallInvoiceDetial(selectedDate)

        binding.tvTitle.setOnClickListener {
            startActivity(Intent(this@Dashboard, ScreenRecorder::class.java))
        }

        val currentDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
        binding.tvDateTotalPatients.text = currentDate
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
                val selectedDate =
                    SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(newDate.time)
                val date = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(newDate.time)
                binding.tvDate.text = date
                binding.tvDateTotalPatients.text = date
                apiCallInvoiceDetial(selectedDate)
                Log.e(ContentValues.TAG, "onCreate: >>>>>>>>>>>>>>>>>>>>>>$date")
                Log.e(ContentValues.TAG, "selectedDate: >>$selectedDate")
            },
            newCalendar1[Calendar.YEAR],
            newCalendar1[Calendar.MONTH],
            newCalendar1[Calendar.DAY_OF_MONTH]
        )
        binding.cardCalander.setOnClickListener {
            datePicker.show()

        }
        binding.imgLogout.setOnClickListener {
            SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Are you sure want to logout?")
                .setCancelText("No")
                .setConfirmText("Yes")
                .showCancelButton(true)
                .setConfirmClickListener { sDialog ->
                    sDialog.cancel()
                    sessionManager.logout()
                    val intent = Intent(applicationContext, SignIn::class.java)
                    intent.flags =
                        Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                    finish()
                    startActivity(intent)
                }
                .setCancelClickListener { sDialog ->
                    sDialog.cancel()
                }
                .show()
        }
    }

    private fun apiCallInvoiceDetial(selectedDate: String) {
        AppProgressBar.showLoaderDialog(context)

        ApiClient.apiService.filterAppointmentByDate(
            sessionManager.id.toString(),
            selectedDate,
            "accepted"
        )
            .enqueue(object : Callback<ModelDashboard> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onResponse(
                    call: Call<ModelDashboard>,
                    response: Response<ModelDashboard>
                ) {
                    if (response.code() == 500) {
                        myToast(this@Dashboard, "Server Error")
                    } else if (response.body()!!.status == 0) {
                        myToast(this@Dashboard, "${response.body()!!.message}")
                        AppProgressBar.hideLoaderDialog()
                    } else if (response.body()!!.result.isEmpty()) {
                        binding.recyclerView.adapter =
                            AdapterDashboard(this@Dashboard, response.body()!!)
                        binding.tvTotalPatient.text = response.body()!!.result.size.toString()
                        myToast(this@Dashboard, "No Appointment Found")
                        binding.recyclerView.adapter!!.notifyDataSetChanged()
                        AppProgressBar.hideLoaderDialog()

                    } else {
                        count = 0
                        binding.recyclerView.adapter =
                            AdapterDashboard(this@Dashboard, response.body()!!)
                        binding.recyclerView.layoutManager =
                            GridLayoutManager(context, 3)

                        binding.recyclerView.adapter!!.notifyDataSetChanged()
                        binding.tvTotalPatient.text = response.body()!!.result.size.toString()

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
                }

                override fun onFailure(call: Call<ModelDashboard>, t: Throwable) {
                    count++
                    if (count <= 3) {
                        apiCallInvoiceDetial(selectedDate)
                    } else {
                        myToast(this@Dashboard, "Something went wrong")
                    }
                    AppProgressBar.hideLoaderDialog()

                }


            })
    }

    override fun onStart() {
        super.onStart()
        CheckInternet().check { connected ->
            if (connected) {

                // myToast(requireActivity(),"Connected")
            } else {
                val changeReceiver = NetworkChangeReceiver(context)
                changeReceiver.build()
                //  myToast(requireActivity(),"Check Internet")
            }
        }
    }

}