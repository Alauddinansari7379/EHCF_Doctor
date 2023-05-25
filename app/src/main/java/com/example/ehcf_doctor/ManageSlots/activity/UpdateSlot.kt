package com.example.ehcf_doctor.ManageSlots.activity

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.TimePicker
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.ehcf.Helper.myToast
import com.example.ehcf.sharedpreferences.SessionManager
import com.example.ehcf_doctor.ManageSlots.model.ModelCreateSlot
import com.example.ehcf_doctor.ManageSlots.model.ModelUpdateSlot
import com.example.ehcf_doctor.R
import com.example.ehcf_doctor.databinding.ActivityCreateSlotBinding
import com.example.ehcf_doctor.databinding.ActivityUpdateSlotBinding
import com.example.myrecyview.apiclient.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import rezwan.pstu.cse12.youtubeonlinestatus.recievers.NetworkChangeReceiver
import xyz.teamgravity.checkinternet.CheckInternet
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class UpdateSlot : AppCompatActivity() {
    private val context: Context = this@UpdateSlot
    private lateinit var binding: ActivityUpdateSlotBinding
    private lateinit var sessionManager: SessionManager
    var mydilaog: Dialog? = null
    var datePicker: Dialog? = null
    var progressDialog: ProgressDialog? = null
    var dialog: Dialog? = null
    var selectedDate = ""
    var slotId = ""
    private var startTime = "00:00:00"
    private var endTime = "00:00:00"
    var openingTimeList = ArrayList<String>()
    private var tvTimeCounter: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityUpdateSlotBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)

        var view = layoutInflater.inflate(R.layout.time_picker_dialog, null)
        binding.imgBack.setOnClickListener {
            onBackPressed()
        }
        val currentDate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
        selectedDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

        Log.e("sessionManager",sessionManager.id.toString())

        slotId = intent.getStringExtra("slotId").toString()
        Log.e("DoctorId", slotId)

        Log.e("startTime",startTime)
        Log.e("endTime",endTime)
        Log.e("SelectedDate",selectedDate)

        binding.tvDate.text=currentDate

        mydilaog?.setCanceledOnTouchOutside(false)
        mydilaog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val newCalendar1 = Calendar.getInstance()
        val datePicker = DatePickerDialog(
            this,
            { _, year, monthOfYear, dayOfMonth ->
                val newDate = Calendar.getInstance()
                newDate[year, monthOfYear] = dayOfMonth
                DateFormat.getDateInstance().format(newDate.time)
                val date = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(newDate.time)
                binding.tvDate.text = date
                selectedDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(newDate.time)
                Log.e("selectedDate", selectedDate)
            },
            newCalendar1[Calendar.YEAR],
            newCalendar1[Calendar.MONTH],
            newCalendar1[Calendar.DAY_OF_MONTH]
        )

        binding.btnDate.setOnClickListener {
            datePicker.show()
        }

        binding.btnStartTime.setOnClickListener {
            startTime()
        }
        binding.btnEndTime.setOnClickListener {
            endTime()
        }

        binding.btnUpdate.setOnClickListener {
            if (binding.tvStartTime.text=="00:00:00") {
                SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Please select Start Time !")
                    .setConfirmText("Ok")
                    .showCancelButton(true)
                    .setConfirmClickListener { sDialog ->
                        sDialog.cancel()

                    }
                    .setCancelClickListener { sDialog ->
                        sDialog.cancel()
                    }
                    .show()
                return@setOnClickListener
            }
            if  (binding.tvEndTime.text=="00:00:00") {
                SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Please select End Time !")
                    .setConfirmText("Ok")
                    .showCancelButton(true)
                    .setConfirmClickListener { sDialog ->
                        sDialog.cancel()

                    }
                    .setCancelClickListener { sDialog ->
                        sDialog.cancel()
                    }
                    .show()
                return@setOnClickListener

            }
            else {
                apiCall()
            }
        }
    }


    private fun apiCall() {
        progressDialog = ProgressDialog(this@UpdateSlot)
        progressDialog!!.setMessage("Loading..")
        progressDialog!!.setTitle("Please Wait")
        progressDialog!!.isIndeterminate = false
        progressDialog!!.setCancelable(true)

        progressDialog!!.show()
        Log.e("SelectedDate",selectedDate)

        ApiClient.apiService.updateSlot(sessionManager.id.toString(),startTime,endTime,"1",slotId)
            .enqueue(object : Callback<ModelUpdateSlot> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelUpdateSlot>, response: Response<ModelUpdateSlot>
                ) {

                    if (response.code()==500){
                        myToast(this@UpdateSlot,"Server Error")
                    }
                   else if (response.body()!!.status==1){
                        progressDialog!!.dismiss()
                        SweetAlertDialog(this@UpdateSlot, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Slot Updated")
                            .setConfirmText("Ok")
                            //.setCancelText("Ok")
                            .showCancelButton(true)
                            .setConfirmClickListener { sDialog ->
                                sDialog.cancel()
                                val intent = Intent(applicationContext, ManageSlots::class.java)
//                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
//                                finish()
                                startActivity(intent)
                            }
                            .setCancelClickListener { sDialog ->
                                sDialog.cancel()
                            }
                            .show()
                    }
                    else{
                        myToast(this@UpdateSlot,"${response.body()!!.message}")
                        progressDialog!!.dismiss()

                    }

                }

                override fun onFailure(call: Call<ModelUpdateSlot>, t: Throwable) {
                    myToast(this@UpdateSlot, "Something went wrong")
                    progressDialog!!.dismiss()

                }

            })
    }

    @SuppressLint("SetTextI18n", "LogNotTimber")
    private fun startTime() {
        var view = layoutInflater.inflate(R.layout.time_picker_dialog, null)
        dialog = Dialog(this)
        val btnOkTimePicker = view.findViewById<Button>(R.id.btnOkTimePicker)

        // val btnOkDialog = view!!.findViewById<Button>(R.id.btnOkDialogNew)

        dialog=   Dialog(this)
        if (view.parent != null) {
            (view.parent as ViewGroup).removeView(view) // <- fix
        }
        dialog!!.setContentView(view)
        dialog?.setCancelable(true)
        // dialog?.setContentView(view)

        dialog?.show()
        btnOkTimePicker.setOnClickListener {
            dialog?.dismiss()
        }
        val timePicker = view.findViewById<TimePicker>(R.id.timePicker)
        val tvTimeTimePicker = view.findViewById<TextView>(R.id.tvTimeTimePicker)
        timePicker.setIs24HourView(true);
        timePicker.setOnTimeChangedListener { _, hour, minute -> var hour = hour
            var am_pm = ""
            // AM_PM decider logic
//            when {hour == 0 -> { hour += 12
//                am_pm = "AM"
//            }
//                hour == 12 -> am_pm = "PM"
//                hour > 12 -> { hour -= 12
//                    am_pm = "PM"
//                }
//                else -> am_pm = "AM"
//            }
            if (tvTimeTimePicker != null) {
                val hour = if (hour < 10) "0" + hour else hour
                val min = if (minute < 10) "0" + minute else minute
                // display format of time
                val zero="00"
                val msg =  "$hour:$min$am_pm"
                // binding.tvStartTime.text = "$hour : $min $zero"

                tvTimeTimePicker.text = msg
                binding.tvStartTime.text= "$msg:00"
                startTime=binding.tvStartTime.text.toString()
                Log.e("SelecteedTime",startTime)
                // textView.visibility = ViewGroup.VISIBLE
            }
        }
    }
    private fun endTime() {
        var view = layoutInflater.inflate(R.layout.time_picker_dialog, null)
        dialog = Dialog(this)
        val btnOkTimePicker = view.findViewById<Button>(R.id.btnOkTimePicker)

        // val btnOkDialog = view!!.findViewById<Button>(R.id.btnOkDialogNew)

        dialog=   Dialog(this)
        if (view.parent != null) {
            (view.parent as ViewGroup).removeView(view) // <- fix
        }
        dialog!!.setContentView(view)
        dialog?.setCancelable(true)
        // dialog?.setContentView(view)

        dialog?.show()
        btnOkTimePicker.setOnClickListener {
            dialog?.dismiss()
        }
        val timePicker = view.findViewById<TimePicker>(R.id.timePicker)
        val tvTimeTimePicker = view.findViewById<TextView>(R.id.tvTimeTimePicker)
        timePicker.setIs24HourView(true);
        timePicker.setOnTimeChangedListener { _, hour, minute -> var hour = hour
            var am_pm = ""
            // AM_PM decider logic
//            when {hour == 0 -> { hour += 12
//                am_pm = "AM"
//            }
//                hour == 12 -> am_pm = "PM"
//                hour > 12 -> { hour -= 12
//                    am_pm = "PM"
//                }
//                else -> am_pm = "AM"
//            }
            if (tvTimeTimePicker != null) {
                val hour = if (hour < 10) "0" + hour else hour
                val min = if (minute < 10) "0" + minute else minute
                // display format of time
                val zero="00"
                val msg =  "$hour:$min$am_pm"
                // binding.tvStartTime.text = "$hour : $min $zero"

                tvTimeTimePicker.text = msg
                binding.tvEndTime.text= "$msg:00"
                endTime=binding.tvEndTime.text.toString()

                Log.e("StartTime",startTime)
                Log.e("endTime",endTime)
                // textView.visibility = ViewGroup.VISIBLE
            }
        }
    }
    override fun onStart() {
        super.onStart()
        CheckInternet().check { connected ->
            if (connected) {

                // myToast(requireActivity(),"Connected")
            }
            else {
                val changeReceiver = NetworkChangeReceiver(context)
                changeReceiver.build()
                //  myToast(requireActivity(),"Check Internet")
            }
        }
    }

}