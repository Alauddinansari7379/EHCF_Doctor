package com.example.ehcf_doctor.ManageSlots.activity

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.ProgressDialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.*
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.ehcf.Helper.myToast
import com.example.ehcf.sharedpreferences.SessionManager
import com.example.ehcf_doctor.ManageSlots.model.ModelConsaltation
import com.example.ehcf_doctor.ManageSlots.model.ModelCreateSlot
import com.example.ehcf_doctor.ManageSlots.model.ModelDay
import com.example.ehcf_doctor.ManageSlots.model.My_Model
import com.example.ehcf_doctor.R
import com.example.ehcf_doctor.databinding.ActivityCreateSlotBinding
import com.example.myrecyview.apiclient.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import rezwan.pstu.cse12.youtubeonlinestatus.recievers.NetworkChangeReceiver
import xyz.teamgravity.checkinternet.CheckInternet
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class CreateSlot : AppCompatActivity() {
    private val context: Context = this@CreateSlot
    private lateinit var binding: ActivityCreateSlotBinding
    private lateinit var sessionManager: SessionManager
    var mydilaog: Dialog? = null
    var datePicker: Dialog? = null
    var progressDialog: ProgressDialog? = null
    var dialog: Dialog? = null
    var selectedDate = ""
    var dayId = ""
    var consultationTypeId = ""
    private var startTime = "00:00:00"
    var dayList = ArrayList<ModelDay>()
    var consaltationList = ArrayList<ModelConsaltation>()


    private var endTime = "00:00:00"
    var openingTimeList = ArrayList<String>()
    private var tvTimeCounter: TextView? = null


    @SuppressLint("LogNotTimber")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateSlotBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sessionManager = SessionManager(this)

        var view = layoutInflater.inflate(R.layout.time_picker_dialog, null)
        binding.imgBack.setOnClickListener {
            onBackPressed()
        }
        val currentDate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
        selectedDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

        Log.e("sessionManager", sessionManager.id.toString())

        Log.e("startTime", startTime)
        Log.e("endTime", endTime)
        Log.e("SelectedDate", selectedDate)

        dayList.add(ModelDay("Monday", "1"))
        dayList.add(ModelDay("Tuesday", "2"))
        dayList.add(ModelDay("Wednesday", "3"))
        dayList.add(ModelDay("Thursday", "4"))
        dayList.add(ModelDay("Friday", "5"))
        dayList.add(ModelDay("Saturday", "6"))
        dayList.add(ModelDay("Sunday", "7"))

        consaltationList.add(ModelConsaltation("Select Consultation Type ", "0"))
        consaltationList.add(ModelConsaltation("Tele Consultation", "1"))
        consaltationList.add(ModelConsaltation("Clinic Visit", "2"))
        consaltationList.add(ModelConsaltation("Home Visit", "3"))



        binding.spinnerDay.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View, i: Int, l: Long) {
                if (dayList.size > 0) {
                    dayId = dayList[i].id.toString()
                    Log.e(ContentValues.TAG, "gender: $dayId")
                }
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {}
        }

        binding.spinnerDay.adapter = ArrayAdapter<ModelDay>(context, android.R.layout.simple_list_item_1, dayList)

        binding.spinnerConsaltationType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View, i: Int, l: Long) {
                if (consaltationList.size > 0) {
                    consultationTypeId = consaltationList[i].id.toString()
                    Log.e(ContentValues.TAG, "consultationTypeId: $consultationTypeId")
                }
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {}
        }
        binding.spinnerConsaltationType.adapter = ArrayAdapter<ModelConsaltation>(context, android.R.layout.simple_list_item_1, consaltationList)




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
                val date = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(newDate.time)
                binding.tvDate.text = date

                selectedDate = SimpleDateFormat("E MMMM dd,yyyy hh:mm a", Locale.getDefault()).format(newDate.time)
                Log.e("selectedDate", selectedDate)
            },
            newCalendar1[Calendar.YEAR],
            newCalendar1[Calendar.MONTH],
            newCalendar1[Calendar.DAY_OF_MONTH]
        )
        //datePicker.datePicker.minDate = System.currentTimeMillis() - 1000
        datePicker.datePicker.minDate = System.currentTimeMillis()


        binding.btnDate.setOnClickListener {
            datePicker.show()
        }

        binding.btnStartTime.setOnClickListener {
            startTime()
        }
        binding.btnEndTime.setOnClickListener {
            endTime()
        }







        binding.btnCreate.setOnClickListener {
            val startT = binding.tvStartTime.text.toString()
            val endT = binding.tvEndTime.text.toString()
            if (binding.tvStartTime.text == "00:00:00") {
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
            if (binding.tvEndTime.text == "00:00:00") {
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

            } else if (startT == endT && startT < endT) {
                SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Start Time must be grater End Time !")
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
            } else if (startT > endT) {
                SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("End Time must be grater Start Time !")
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
            else if (consultationTypeId=="0") {
                myToast(this@CreateSlot,"Select Consultation Type")
                return@setOnClickListener
            } else {
                apiCall()

            }
        }
    }

    private fun apiCall() {
        progressDialog = ProgressDialog(this@CreateSlot)
        progressDialog!!.setMessage("Loading..")
        progressDialog!!.setTitle("Please Wait")
        progressDialog!!.isIndeterminate = false
        progressDialog!!.setCancelable(true)
        progressDialog!!.show()

        Log.e("StartTime",startTime)
        Log.e("endTime",endTime)
        Log.e("dayId", dayId.toString())

        ApiClient.apiService.createSlot(sessionManager.id.toString(), startTime, endTime, dayId,consultationTypeId)
            .enqueue(object : Callback<My_Model> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    //   Call<List<model>>
                    call: Call<My_Model>, response: Response<My_Model>
                ) {

                    if (response.code()==500){
                        myToast(this@CreateSlot,"Server Error")
                        progressDialog!!.dismiss()
                    }
                    else if (response.body()!!.status==1) {
                        myToast(this@CreateSlot, "${response.body()!!.message}")
                        progressDialog!!.dismiss()
                        SweetAlertDialog(this@CreateSlot, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText(response.body()!!.message)
                            .setConfirmText("Ok")
                            //.setCancelText("Ok")
                            .showCancelButton(true)
                            .setConfirmClickListener { sDialog ->
                                sDialog.cancel()
//                                val intent = Intent(applicationContext, ManageSlots::class.java)
//                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
//                                finish()
                                // startActivity(intent)
                                startActivity(Intent(this@CreateSlot, MySlot::class.java))
                            }
                            .setCancelClickListener { sDialog ->
                                sDialog.cancel()
                            }
                            .show()
                    } else {
                        myToast(this@CreateSlot, "${response.body()!!.message}")
                        progressDialog!!.dismiss()

                    }

                }
                override fun onFailure(call: Call<My_Model>, t: Throwable) {
                    myToast(this@CreateSlot, "${t.message}")
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

        dialog = Dialog(this)
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
        timePicker.setIs24HourView(false);
        timePicker.setOnTimeChangedListener { _, hour, minute ->
            var hour = hour
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
                val zero = "00"
                val msg = "$hour:$min$am_pm"
                // binding.tvStartTime.text = "$hour : $min $zero"

                tvTimeTimePicker.text = msg
                binding.tvStartTime.text = "$msg:00"
                startTime = binding.tvStartTime.text.toString()
                Log.e("SelecteedTime", startTime)
                // textView.visibility = ViewGroup.VISIBLE
            }
        }
    }

    private fun endTime() {
        var view = layoutInflater.inflate(R.layout.time_picker_dialog, null)
        dialog = Dialog(this)
        val btnOkTimePicker = view.findViewById<Button>(R.id.btnOkTimePicker)

        // val btnOkDialog = view!!.findViewById<Button>(R.id.btnOkDialogNew)

        dialog = Dialog(this)
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
        timePicker.setIs24HourView(false);
        timePicker.setOnTimeChangedListener { _, hour, minute ->
            var hour = hour
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
                val zero = "00"
                val msg = "$hour:$min$am_pm"
                // binding.tvStartTime.text = "$hour : $min $zero"

                tvTimeTimePicker.text = msg
                binding.tvEndTime.text = "$msg:00"
                endTime = binding.tvEndTime.text.toString()

                Log.e("StartTime", startTime)
                Log.e("endTime", endTime)
                // textView.visibility = ViewGroup.VISIBLE
            }
        }
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