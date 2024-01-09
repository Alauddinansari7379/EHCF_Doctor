package com.example.ehcf_doctor.Prescription.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.ProgressDialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.ehcf.Helper.changeDateFormat4
import com.example.ehcf.Helper.isOnline
import com.example.ehcf.Helper.myToast
import com.example.ehcf.sharedpreferences.SessionManager
import com.example.ehcf_doctor.AutoComplete.AutoSuggestAdapter
import com.example.ehcf_doctor.AutoComplete.AutoSuggestMedicineAdapter
import com.example.ehcf_doctor.Helper.DatePickerDialogWithMaxMinRange
import com.example.ehcf_doctor.Helper.Util
import com.example.ehcf_doctor.Prescription.adapter.AdapterDigonisis
import com.example.ehcf_doctor.Prescription.adapter.AdapterLabTest
import com.example.ehcf_doctor.Prescription.adapter.AdapterOrderDetails
import com.example.ehcf_doctor.Prescription.model.*
import com.example.ehcf_doctor.Profile.modelResponse.ModelCityList
import com.example.ehcf_doctor.Profile.modelResponse.ModelStateList
import com.example.ehcf_doctor.R
import com.example.ehcf_doctor.Rating.Rating
import com.example.ehcf_doctor.Registration.modelResponse.ModelGender
import com.example.ehcf_doctor.Registration.modelResponse.ModelSpecilList
import com.example.ehcf_doctor.databinding.ActivityAddPrescriptionBinding
import com.example.myrecyview.apiclient.ApiClient
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import rezwan.pstu.cse12.youtubeonlinestatus.recievers.NetworkChangeReceiver
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class AddPrescription : AppCompatActivity() {
    private lateinit var binding: ActivityAddPrescriptionBinding
    var bookingId = ""
    private var doctor_notes = ""
    var mydilaog: Dialog? = null
    private var subjective_information = ""
    private var objective_information = ""
    private var calendar: Calendar? = null
    private var specilList = ModelSpecilList();
    private var testListNew = ModelTestList();
    private var medicineListNew = ModelTestList();

    var assessment = ""
    var plan = ""
    var timing = ""
    var intake = ""
    var fraquency = ""
    var duration = ""
    var testName = ""
    var followUpDate = ""
    var registration = ""
    var currentDate = ""
    var timingList = ArrayList<ModelGender>()
    var intakeList = ArrayList<ModelGender>()
    var fraquencyList = ArrayList<ModelGender>()
    var DurationList = ArrayList<ModelGender>()
    var testList = ArrayList<ModelGender>()
    private val context: Context = this@AddPrescription
    private lateinit var sessionManager: SessionManager
    var progressDialog: ProgressDialog? = null
    var isTest = "0"
     private var listOfText= ArrayList<String>()


    private var maxDate: String? = null
    private var minDate: String? = null
    private val medicineList = ArrayList<ModelOrderDetails>()
    private val diagnosisList = ArrayList<ModelDigonsis>()
    private val labTestList = ArrayList<ModelLabTest>()
    var btnId = "0"

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPrescriptionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        calendar = Calendar.getInstance()

        val dateList = getCurrentDateAndNextTenDays()
        for (date in dateList) {
            Log.e("sd", date.toString())
        }
        maxDate = changeDateFormat4(dateList[5].toString())

        Log.e("NewFor", changeDateFormat4(dateList[5].toString()))

        currentDate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
        binding.tvFollowUPdate.text = currentDate
        minDate = currentDate


        CoroutineScope(IO).launch {
            apiCallTestName()
            apiCallMedicineName()
        }



//        runBlocking {
//            launch {
//                delay(10000)
//                apiCallSpecialistSpinner()
//
//                println("Hello from the launched coroutine. My thread is "
//                        + Thread.currentThread().name)
//            }
//            println("Hello from the top-level coroutine. My thread is "
//                    + Thread.currentThread().name)
//        }



        bookingId = intent.getStringExtra("bookingId").toString()
        doctor_notes = intent.getStringExtra("doctor_notes").toString()
        subjective_information = intent.getStringExtra("subjective_information").toString()
        objective_information = intent.getStringExtra("objective_information").toString()
        assessment = intent.getStringExtra("assessment").toString()
        plan = intent.getStringExtra("plan").toString()
        registration = intent.getStringExtra("registration").toString()
        btnId = intent.getStringExtra("btnId").toString()
        Log.e("bookingId", bookingId)



        if (doctor_notes != "null") {
            binding.edtDoctorNotes.setText(doctor_notes)
//            binding.edtSubjectiveInformation.setText(subjective_information)
//            binding.edtObjectiveInformation.setText(objective_information)
//            binding.edtAssessment.setText(assessment)
//            binding.edtPlan.setText(plan)
        }
        binding.imgBack.setOnClickListener {
            onBackPressed()
        }

        binding.checkTest.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.layoutTest.visibility = View.VISIBLE
            } else {
                binding.layoutTest.visibility = View.GONE

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

            timingList.add(ModelGender("Select Timing", 0))
            timingList.add(ModelGender("Morning", 7))
            timingList.add(ModelGender("Afternoon", 8))
            timingList.add(ModelGender("Evening", 9))
            timingList.add(ModelGender("Night", 10))

            binding.edtTiming.adapter =
                ArrayAdapter<ModelGender>(context, R.layout.simple_list_item_1, timingList)


            binding.edtTiming.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(adapterView: AdapterView<*>?, view: View, i: Int, l: Long) {
                    if (timingList.size > 0) {
                        timing = timingList[i].name.toString()

                        Log.e(ContentValues.TAG, "timing: $timing")
                    }
                }

                override fun onNothingSelected(adapterView: AdapterView<*>?) {}
            }


            intakeList.add(ModelGender("Select Intake", 0))
            intakeList.add(ModelGender("After Food", 7))
            intakeList.add(ModelGender("Before Food", 8))

            binding.edtIntake.adapter =
                ArrayAdapter<ModelGender>(context, R.layout.simple_list_item_1, intakeList)


            binding.edtIntake.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(adapterView: AdapterView<*>?, view: View, i: Int, l: Long) {
                    if (intakeList.size > 0) {
                        intake = intakeList[i].name.toString()

                        Log.e(ContentValues.TAG, "intake: $intake")
                    }
                }

                override fun onNothingSelected(adapterView: AdapterView<*>?) {}
            }

            fraquencyList.add(ModelGender("Select Frequency", 0))
            fraquencyList.add(ModelGender("Daily", 7))
            fraquencyList.add(ModelGender("After 1 Day", 8))
            fraquencyList.add(ModelGender("After 2 Day", 8))


            binding.edtFrequency.adapter =
                ArrayAdapter<ModelGender>(context, R.layout.simple_list_item_1, fraquencyList)


            binding.edtFrequency.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(adapterView: AdapterView<*>?, view: View, i: Int, l: Long) {
                    if (fraquencyList.size > 0) {
                        fraquency = fraquencyList[i].name.toString()

                        Log.e(ContentValues.TAG, "fraquency: $fraquency")
                    }
                }

                override fun onNothingSelected(adapterView: AdapterView<*>?) {}
            }
            DurationList.add(ModelGender("Select Duration", 0))
            DurationList.add(ModelGender("Till next consultation", 7))
            DurationList.add(ModelGender("After 1 Week", 8))
            DurationList.add(ModelGender("After 2 Week", 8))


            binding.edtDuration.adapter =
                ArrayAdapter<ModelGender>(context, R.layout.simple_list_item_1, DurationList)


            binding.edtDuration.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(adapterView: AdapterView<*>?, view: View, i: Int, l: Long) {
                    if (DurationList.size > 0) {
                        duration = DurationList[i].name.toString()

                        Log.e(ContentValues.TAG, "duration: $duration")
                    }
                }

                override fun onNothingSelected(adapterView: AdapterView<*>?) {}
            }


        testList.add(ModelGender("TEMPERATURE", 7))
        testList.add(ModelGender("BLOOD_PRESSURE", 8))
        testList.add(ModelGender("WEIGHT", 8))
        testList.add(ModelGender("HEIGHT", 8))
        testList.add(ModelGender("BMI", 8))
        testList.add(ModelGender("ECG", 8))
        testList.add(ModelGender("PULSE_OXIMETER", 8))
        testList.add(ModelGender("BLOOD_GLUCOSE", 8))
        testList.add(ModelGender("CHOLESTEROL", 8))
        testList.add(ModelGender("HEMOGLOBIN", 8))
        testList.add(ModelGender("URIC_ACID", 8))
        testList.add(ModelGender("CHIKUNGUNYA", 8))
        testList.add(ModelGender("DENGUE", 8))
        testList.add(ModelGender("HEP_C", 8))
        testList.add(ModelGender("HEP_B", 8))
        testList.add(ModelGender("HIV", 8))
        testList.add(ModelGender("MALARIA", 8))
        testList.add(ModelGender("SYPHILIS", 8))
        testList.add(ModelGender("TROPONIN_1", 8))
        testList.add(ModelGender("PREGNANCY", 8))
        testList.add(ModelGender("URINE", 8))


//            binding.edtTestName.adapter =
//                ArrayAdapter<ModelGender>(context, R.layout.simple_list_item_1, testList)

        listOfText.add("TEMPERATURE")
        listOfText.add("BLOOD_PRESSURE")
        listOfText.add("WEIGHT")
        listOfText.add("HEIGHT")
        listOfText.add("BMI")
        listOfText.add("ECG")
        listOfText.add("PULSE_OXIMETER")
        listOfText.add("BLOOD_GLUCOSE")
        listOfText.add("CHOLESTEROL")
        listOfText.add("HEMOGLOBIN")
        listOfText.add("URIC_ACID")
        listOfText.add("CHIKUNGUNYA")
        listOfText.add("DENGUE")
        listOfText.add("HEP_C")
        listOfText.add("HEP_B")
        listOfText.add("HIV")
        listOfText.add("MALARIA")
        listOfText.add("SYPHILIS")
        listOfText.add("TROPONIN_1")
        listOfText.add("PREGNANCY")
        listOfText.add("URINE")

//        val autoSuggestAdapter = AutoSuggestAdapter(this,
//            android.R.layout.simple_list_item_1, listOfText)


//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
//            readCityNameJSON(this@AddPrescription)
//        }
       // readSateNameJSON(this@AddPrescription)

        // val autotext = findViewById<AutoCompleteTextView>(R.id.edtAutoSuggest)
//        binding.edtTestName.setAdapter(autoSuggestAdapter)
//        binding.edtTestName.threshold = 1 //comparesion start from first character

//            binding.edtTestName.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//                override fun onItemSelected(adapterView: AdapterView<*>?, view: View, i: Int, l: Long) {
//                    if (testList.size > 0) {
//                        testName = testList[i].name.toString()
//
//                        Log.e(ContentValues.TAG, "duration: $duration")
//                    }
//                }
//
//                override fun onNothingSelected(adapterView: AdapterView<*>?) {}
//            }

//            if (binding.edtSubjectiveInformation.text.isEmpty()) {
//                binding.edtSubjectiveInformation.error = "Enter SubjectiveInformation"
//                binding.edtSubjectiveInformation.requestFocus()
//                return@setOnClickListener
//            }
//            if (binding.edtObjectiveInformation.text.isEmpty()) {
//                binding.edtObjectiveInformation.error = "Enter ObjectiveInformation"
//                binding.edtObjectiveInformation.requestFocus()
//                return@setOnClickListener
//            }
//            if (binding.edtAssessment.text.isEmpty()) {
//                binding.edtAssessment.error = "Enter Assessment"
//                binding.edtAssessment.requestFocus()
//                return@setOnClickListener
//            }
//            if (binding.edtPlan.text.isEmpty()) {
//                binding.edtPlan.error = "Enter Plan"
//                binding.edtPlan.requestFocus()
//                return@setOnClickListener
//            }



        binding.btnAddMedicine.setOnClickListener {
            setRecyclerDataMedicine(
                binding.edtMedicineName.text.toString(),
                timing,
                intake,
                fraquency,
                duration,
            )
            binding.llayout3.visibility = View.VISIBLE
            binding.edtMedicineName.text.clear()
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

        binding.btnAddTest.setOnClickListener {
            setRecyclerDataLabTest(
                binding.edtTestName.text.toString(),
                binding.edtInstructions.text.toString(),
                binding.edtAfter.text.toString(),
            )
            binding.edtTestName.requestFocus()
             binding.edtAfter.text.clear()
            binding.edtInstructions.text.clear()
            binding.edtTestName.text.clear()
        }


//        mydilaog?.setCanceledOnTouchOutside(false)
//        mydilaog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//        val newCalendar1 = Calendar.getInstance()
//        val datePicker = DatePickerDialog(
//            this,
//            { _, year, monthOfYear, dayOfMonth ->
//                val newDate = Calendar.getInstance()
//                newDate[year, monthOfYear] = dayOfMonth
//                DateFormat.getDateInstance().format(newDate.time)
//                // val Date = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(newDate.time)
//                 followUpDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(newDate.time)
//                binding.tvFollowUPdate.text = followUpDate
//
//                Log.e(ContentValues.TAG, "onCreate: >>>>>>>>>>>>>>>>>>>>>>$followUpDate")
//            },
//            newCalendar1[Calendar.YEAR],
//            newCalendar1[Calendar.MONTH],
//            newCalendar1[Calendar.DAY_OF_MONTH]
//        )
//
        binding.tvFollowUPdate.setOnClickListener {
            // datePicker.show()
            openDatePickerWithMaxAndMindate(minDate!!, maxDate!!)

        }

    }
    private fun readSateNameJSON(context: Context): java.util.ArrayList<ModelStateList> {

        lateinit var jsonString: String
        try {
            jsonString = context.assets.open("state_name.json")
                .bufferedReader()
                .use {
                    it.readText()
                }
        } catch (ioException: IOException) {
        }

        val listCountryType = object : TypeToken<java.util.ArrayList<ModelStateList>>() {}.type

        return Gson().fromJson(jsonString, listCountryType)

    }
    private fun apiCallTestName() {
//        progressDialog = ProgressDialog(this@AddPrescription)
//        progressDialog!!.setMessage("Loading..")
//        progressDialog!!.setTitle("Please Wait")
//        progressDialog!!.isIndeterminate = false
//        progressDialog!!.setCancelable(true)
//
//        progressDialog!!.show()

        ApiClient.apiService.testName()
            .enqueue(object : Callback<ModelTestList> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelTestList>, response: Response<ModelTestList>
                ) {
                    try {

                        testListNew = response.body()!!;
                        if (testListNew != null) {

                            //spinner code start
                            val items = arrayOfNulls<String>(testListNew.result!!.size)

                            for (i in testListNew.result!!.indices) {
                                items[i] = testListNew.result!![i].Test_Name
                            }
//                        val adapter: ArrayAdapter<String?> =
//                            ArrayAdapter(this@ProfileSetting, android.R.layout.simple_list_item_1, items)
//                        binding.spinnerSpecialist.adapter = adapter
//                        progressDialog!!.dismiss()


                            val autoSuggestAdapter = AutoSuggestAdapter(
                                this@AddPrescription,
                                android.R.layout.simple_list_item_1, items.toMutableList()
                            )

                            binding.edtTestName.setAdapter(autoSuggestAdapter)
                            binding.edtTestName.threshold = 1


                        }
                    }catch (e:Exception){
                        e.printStackTrace()
                        myToast(this@AddPrescription, "Something went wrong")
                        progressDialog!!.dismiss()
                    }
                }

                override fun onFailure(call: Call<ModelTestList>, t: Throwable) {
                    myToast(this@AddPrescription, "Something went wrong")
                    progressDialog!!.dismiss()

                }

            })
    }
    private fun apiCallMedicineName() {
//        progressDialog = ProgressDialog(this@AddPrescription)
//        progressDialog!!.setMessage("Loading..")
//        progressDialog!!.setTitle("Please Wait")
//        progressDialog!!.isIndeterminate = false
//        progressDialog!!.setCancelable(true)
//
//        progressDialog!!.show()

        ApiClient.apiService.medicineName()
            .enqueue(object : Callback<ModelTestList> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelTestList>, response: Response<ModelTestList>
                ) {
                    try {
                        medicineListNew = response.body()!!;
                        if (medicineListNew != null) {

                            //spinner code start
                            val items = arrayOfNulls<String>(medicineListNew.result!!.size)

                            for (i in medicineListNew.result!!.indices) {
                                items[i] = medicineListNew.result!![i].medicine
                            }
//                        val adapter: ArrayAdapter<String?> =
//                            ArrayAdapter(this@ProfileSetting, android.R.layout.simple_list_item_1, items)
//                        binding.spinnerSpecialist.adapter = adapter
//                        progressDialog!!.dismiss()


                            val autoSuggestAdapter = AutoSuggestMedicineAdapter(
                                this@AddPrescription,
                                android.R.layout.simple_list_item_1, items.toMutableList()
                            )

                            binding.edtMedicineName.setAdapter(autoSuggestAdapter)
                            binding.edtMedicineName.threshold = 1


                        }
                    }catch (e:Exception){
                        e.printStackTrace()
                        myToast(this@AddPrescription, "Something went wrong")
                        progressDialog!!.dismiss()

                    }
                }

                override fun onFailure(call: Call<ModelTestList>, t: Throwable) {
                    myToast(this@AddPrescription, "Something went wrong")
                    progressDialog!!.dismiss()

                }

            })
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun readCityNameJSON(context: Context):ArrayList<ModelCityList> {

        lateinit var jsonString: String
        try {
            jsonString = context.assets.open("city_name.json")
                .bufferedReader()
                .use {
                    it.readText()
                }.toString()
        } catch (ioException: IOException) {
        }

        var listCountryType = object : TypeToken<ArrayList<ModelCityList>>() {}.type

//        val autoSuggestAdapter = AutoSuggestAdapter(this,
//            android.R.layout.simple_list_item_1, listOfText)
//
//        binding.edtTestName.setAdapter(autoSuggestAdapter)
//        binding.edtTestName.threshold = 1

//        var listOfText= ArrayList<String>()
//        listOfText.add(it.toString())
//        Log.e("testName", it.toString())
        Log.e("testName", listCountryType.toString())

        return Gson().fromJson(jsonString, listCountryType)


        Log.e("listCountryType", listCountryType.toString())
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun getCurrentDateAndNextTenDays(): List<Date> {
        val currentDate = Calendar.getInstance().time
        val calendar = Calendar.getInstance()
        calendar.time = currentDate

        val dates = mutableListOf<Date>()

        for (i in 1..6) {
            calendar.add(Calendar.DATE, 1)
            val nextDate = calendar.time

            dates.add(nextDate)
        }
        return dates
    }

    private var datePickerListener =
        DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            calendar?.let {
                it[Calendar.YEAR] = year
                it[Calendar.MONTH] = monthOfYear
                it[Calendar.DAY_OF_MONTH] = dayOfMonth
            }
            binding.tvFollowUPdate!!.text = Util.getDate(calendar!!.time)
        }

    private fun openDatePickerWithMaxAndMindate(minDate: String, maxDate: String) {
        try {
            val minYear: Int
            val minMonth: Int
            val minDay: Int
            val maxYear: Int
            val maxMonth: Int
            val maxDay: Int
//            var emptyInvalidDate = true
//            emptyInvalidDate = !(! binding.tvSelectMaxDatePicker!!.text.toString().equals("", ignoreCase = true) || binding.tvSelectMinDatePicker!!.text.toString() != null
//                        && ! binding.tvSelectMinDatePicker!!.text.toString().equals("", ignoreCase = true))
//            emptyInvalidDate = binding.tvSelectMaxDatePicker!!.text.toString().equals(getString(R.string.lbl_select_date), ignoreCase = true) ||
//                    binding.tvSelectMinDatePicker!!.text.toString().equals(getString(R.string.lbl_select_date), ignoreCase = true)
//            if (emptyInvalidDate) {
//                val alert = AlertDialog.Builder(context!!).apply {
//                    setTitle(getString(R.string.app_name))
//                    setMessage(getString(R.string.tv_invalidDateMessage))
//                    setPositiveButton("Ok") { dialog, which -> }
//                }.show()

//            } else {
            minDay = minDate.split("-").toTypedArray()[0].toInt()
            minMonth = minDate.split("-").toTypedArray()[1].toInt() - 1
            minYear = minDate.split("-").toTypedArray()[2].toInt()
            maxDay = maxDate.split("-").toTypedArray()[0].toInt()
            maxMonth = maxDate.split("-").toTypedArray()[1].toInt() - 1
            maxYear = maxDate.split("-").toTypedArray()[2].toInt()

            val maxDateCalendar = Calendar.getInstance().also {
                it[Calendar.YEAR] = maxYear
                it[Calendar.MONTH] = maxMonth
                it[Calendar.DAY_OF_MONTH] = maxDay
            }

            val minDateCalendar = Calendar.getInstance().also {
                it[Calendar.YEAR] = minYear
                it[Calendar.MONTH] = minMonth
                it[Calendar.DAY_OF_MONTH] = minDay
            }

            if (binding.tvFollowUPdate!!.text.toString()
                    .equals(getString(R.string.lbl_select_date), ignoreCase = true)
            ) {
                calendar = Calendar.getInstance()
            } else {
                val selectedDate = binding.tvFollowUPdate!!.text.toString()
                calendar?.let {
                    it[Calendar.DAY_OF_MONTH] = selectedDate.split("-").toTypedArray()[0].toInt()
                    it[Calendar.MONTH] = selectedDate.split("-").toTypedArray()[1].toInt() - 1
                    it[Calendar.YEAR] = selectedDate.split("-").toTypedArray()[2].toInt()
                }

            }
            if (minDateCalendar.after(calendar)) {
                DatePickerDialogWithMaxMinRange(
                    context,
                    datePickerListener,
                    minDateCalendar,
                    maxDateCalendar,
                    minDateCalendar
                ).show()
            } else if (maxDateCalendar.before(calendar)) {
                DatePickerDialogWithMaxMinRange(
                    context,
                    datePickerListener,
                    minDateCalendar,
                    maxDateCalendar,
                    maxDateCalendar
                ).show()
            } else {
                DatePickerDialogWithMaxMinRange(
                    context,
                    datePickerListener,
                    minDateCalendar,
                    maxDateCalendar,
                    calendar!!
                ).show()
            }

        } catch (e: Throwable) {
            // Have suppressed the exception
            e.printStackTrace()
        }
    }

//        followUpList.add(ModelGender("Select FollowUp Day", 0))
//        followUpList.add(ModelGender("7", 7))
//        followUpList.add(ModelGender("8", 8))
//        followUpList.add(ModelGender("9", 9))
//        followUpList.add(ModelGender("10", 10))
//
//        binding.spinnerFollowUp.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(adapterView: AdapterView<*>?, view: View, i: Int, l: Long) {
//                if (followUpList.size > 0) {
//                    followUpDay = followUpList[i].id.toString()
//
//                    Log.e(ContentValues.TAG, "followUpDay: $followUpDay")
//                }
//            }
//
//            override fun onNothingSelected(adapterView: AdapterView<*>?) {}
//        }
//        binding.spinnerFollowUp.adapter =
//            ArrayAdapter<ModelGender>(context, R.layout.simple_list_item_1, followUpList)
//    }

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
        diagnosisList.add(ModelDigonsis(dignosis, desctption))
        binding.rvrecyclerViewDigonis.adapter = AdapterDigonisis(this, diagnosisList)

    }

    private fun setRecyclerDataLabTest(
        dignosis: String,
        desctption: String,
        after: String,
    ) {
        labTestList.add(ModelLabTest(dignosis, desctption, after))
        binding.rvrecyclerViewLabTest.adapter = AdapterLabTest(this, labTestList)

    }

    private fun apiCall() {
        progressDialog = ProgressDialog(this@AddPrescription)
        progressDialog!!.setMessage("Loading...")
        progressDialog!!.setTitle("Please Wait")
        progressDialog!!.isIndeterminate = false
        progressDialog!!.setCancelable(true)
        progressDialog!!.show()

//        val subjective = binding.edtSubjectiveInformation.text.toString()
//        val objective = binding.edtObjectiveInformation.text.toString()
//        val assessment = binding.edtAssessment.text.toString()
//        val plan = binding.edtPlan.text.toString()
        val doctorNotes = binding.edtDoctorNotes.text.toString()
        Log.e("test", isTest)


        ApiClient.apiService.createPrescription(
            bookingId,
            "null",
            "null",
            "null",
            "null",
            doctorNotes,
            isTest,
            binding.edtTestName.toString(),
            binding.edtInstructions.text.toString(),
            "5",
            currentDate,
            binding.tvFollowUPdate!!.text.toString()
        )
            .enqueue(object : Callback<ModelPreJava> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelPreJava>, response: Response<ModelPreJava>
                ) {
                    try {

                        if (response.code() == 500) {
                            myToast(this@AddPrescription, "Server Error")
                            progressDialog!!.dismiss()
                        } else if (response.code() == 200) {
                            myToast(this@AddPrescription, response.body()!!.result)
                            val preId = response.body()!!.data.id.toString()
                            // Log.e("Iddddddd",preId)
                            progressDialog!!.dismiss()
                            apiCallDiagnosis(preId)

                        } else {
                            myToast(this@AddPrescription, "${response.body()!!.result}")
                            progressDialog!!.dismiss()

                        }
                    } catch (e: Exception) {
                        myToast(this@AddPrescription, "Something went wrong")
                        e.printStackTrace()
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
        apiCallLabTest(preId)

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
                        try {

                            if (response.code() == 500) {
                                myToast(this@AddPrescription, "Server Error")
                                // progressDialog!!.dismiss()
                            } else if (response.code() == 200) {
                                val intent = Intent(context as Activity, Rating::class.java)
                                    .putExtra("meetingId", bookingId)
                                (context as Activity).startActivity(intent)
                                //  myToast(this@AddPrescription, response.body()!!.result)
                                //  progressDialog!!.dismiss()

                            } else {
                                myToast(this@AddPrescription, "${response.body()!!.message}")
                                progressDialog!!.dismiss()

                            }

                        }catch (e:Exception){
                            e.printStackTrace()
                            myToast(this@AddPrescription, "Something went wrong")
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
                        try {

                            if (response.code() == 500) {
                                myToast(this@AddPrescription, "Server Error")
                                // progressDialog!!.dismiss()
                            } else if (response.code() == 200) {
                                myToast(this@AddPrescription, response.body()!!.message)
                                onBackPressed()
                                //  progressDialog!!.dismiss()

                            } else {
                                myToast(this@AddPrescription, "${response.body()!!.message}")
                                progressDialog!!.dismiss()

                            }
                        }catch (e:Exception){
                            e.printStackTrace()
                            myToast(this@AddPrescription, "Something went wrong")
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

    private fun apiCallLabTest(preId: String) {
        progressDialog = ProgressDialog(this@AddPrescription)
        progressDialog!!.setMessage("Loading...")
        progressDialog!!.setTitle("Please Wait")
        progressDialog!!.isIndeterminate = false
        progressDialog!!.setCancelable(true)
        // progressDialog!!.show()

        for (i in labTestList) {
            ApiClient.apiService.createLabTest(
                preId,
                i.testName,
                i.after,
                i.description,
            )
                .enqueue(object : Callback<ModeMedicine> {
                    @SuppressLint("LogNotTimber")
                    override fun onResponse(
                        call: Call<ModeMedicine>,
                        response: Response<ModeMedicine>
                    ) {

                        try {
                            if (response.code() == 500) {
                                myToast(this@AddPrescription, "Server Error")
                                // progressDialog!!.dismiss()
                            } else if (response.code() == 200) {
                                myToast(this@AddPrescription, response.body()!!.message)
                                val intent = Intent(context as Activity, Rating::class.java)
                                    .putExtra("meetingId", bookingId)
                                (context as Activity).startActivity(intent)
                                //  progressDialog!!.dismiss()

                            } else {
                                myToast(this@AddPrescription, "${response.body()!!.message}")
                                progressDialog!!.dismiss()

                            }

                        }catch (e:java.lang.Exception){
                            e.printStackTrace()
                            myToast(this@AddPrescription, "Something went wrong")
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

//        val subjective = binding.edtSubjectiveInformation.text.toString()
//        val objective = binding.edtObjectiveInformation.text.toString()
//        val assessment = binding.edtAssessment.text.toString()
//        val plan = binding.edtPlan.text.toString()
        val doctorNotes = binding.edtDoctorNotes.text.toString()
        Log.e("test", isTest)


        ApiClient.apiService.modifyPrescrption(
            bookingId,
            "null",
            "null",
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
                    try {

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

                    }catch (e:java.lang.Exception){
                        e.printStackTrace()
                        myToast(this@AddPrescription, "Something went wrong")
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