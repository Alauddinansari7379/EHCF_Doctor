package com.example.ehcf_doctor.HealthCube.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.ehcf.Helper.myToast
import com.example.ehcf.sharedpreferences.SessionManager
import com.example.ehcf_doctor.HealthCube.Adapter.AdapterTestList
import com.example.ehcf_doctor.HealthCube.Adapter.ItemAdapter
import com.example.ehcf_doctor.HealthCube.Adapter.ItemAdapter.Companion.selectedTestList
import com.example.ehcf_doctor.HealthCube.Model.*
import com.example.ehcf_doctor.databinding.ActivityAddPatientBinding
import com.example.ehcf_doctor.Retrofit.ApiClient
import com.example.ehcf_doctor.HealthCube.RetrofitHealthCube.ApiClientHealthCube
import com.example.ehcf_doctor.HealthCube.RetrofitHealthCube.ApiInterfaceHealthCube
import com.rajat.pdfviewer.PdfViewerActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec


class AddPatient : AppCompatActivity() {
    private val context: Context = this@AddPatient
    private lateinit var sessionManager: SessionManager
    var progressDialog: ProgressDialog? = null
    var gender = ""
    var dateOfBirth = ""
    var dateOfBirthNew = ""
    var dateOfBirthHealth = ""
    var patient_id = ""
    var helthCubePatientId = ""
    var externalPatientId = ""
    var reportList = ArrayList<String>()
    var mydilaog: Dialog? = null
    private var countryName = ""
    private var NewRegisteredpatientId = ""
    private var selectedDate = ""
    private var testListNew = ""
    var hightlightTests = ""
    private var countryCode = ""
    var sampleUserId: String? = "+919392595905"
//    var secretKey: String? = "a1fc979a-03d6-4ca6-9196-d7192873d4a8"
//    var client_id: String? = "b96214a0-2d05-4e65-be92-b83852227733"

    var secretKey: String? = "bce6f92b-c106-404e-b255-ffad5dc0e434"
    var client_id: String? = "a73a6a93-4fdb-4239-a8c7-e2eed5ae6853"
    var name = ""
    var city = ""
    var state = ""
    var postelCode = ""
    var phoneNumber = ""
    var phoneNumberNew = ""
    var email = ""
    var value = ""
    var unit = ""
    var temperature = ""
    var testName = ""
    var systolic = ""
    var spO2 = ""
    var bloodGlucose = ""
    var hemoglobin = ""
    var uricAcid = ""
    var malaria = ""
    var typhoid = ""
    var pulseRate = ""
    var imageString = ""
    var diastolic = ""
    var reportId = ""
    private val diagnosticTestList: MutableList<String> = ArrayList()
    val testList = kotlin.collections.ArrayList<String>()
    private var accessToken =""
     private var con = true
    private lateinit var binding: ActivityAddPatientBinding
    var activity: Activity = this@AddPatient
    var rv: RecyclerView? = null
    var adapter: ItemAdapter? = null
    var actionMode: ActionMode? = null
    var textView: TextView? = null
    lateinit var selectedLanguage: BooleanArray
    var langList: ArrayList<Int> = ArrayList()
    var langArray = arrayOf(
        "TEMPERATURE",
        "BLOOD_PRESSURE",
        "WEIGHT",
        "ECG",
        "PULSE_OXIMETER",
        "BLOOD_GLUCOSE",
        "HEMOGLOBIN",
        "URIC_ACID",
        "CHOLESTEROL",
        "MALARIA",
        "TYPHOID"
    )
    var testListNew1 = ArrayList<String>()

    private val actionModeCallback: ActionMode.Callback = object : ActionMode.Callback {

        override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
            return true
        }

        override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean {
            return false
        }

        //
        override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
            return when (item.itemId) {
                Log.e("itemitem", item.toString()) -> {
                    // Toast.makeText(activity, adapter!!.getSelected().size.toString() + " selected", Toast.LENGTH_SHORT).show()
                    mode.finish()
                    true
                }
                else -> false
            }
        }

        override fun onDestroyActionMode(mode: ActionMode) {
            actionMode = null
        }
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPatientBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this@AddPatient)

        selectedLanguage = BooleanArray(langArray.size)




        binding.layTest!!.setOnClickListener(View.OnClickListener { // Initialize alert dialog
            val builder = AlertDialog.Builder(this@AddPatient)
            // set title
            builder.setTitle("Select Language")

            // set dialog non cancelable
            builder.setCancelable(false)
            builder.setMultiChoiceItems(
                langArray, selectedLanguage
            ) { dialogInterface, i, b ->
                // check condition
                if (b) {
                    // when checkbox selected
                    // Add position in lang list
                    langList.add(i)
                    Log.e("selectedLanguage", selectedLanguage.toString())
                    // Sort array list
                    langList.sort()
                } else {
                    // when checkbox unselected
                    // Remove position from langList
                    langList.remove(Integer.valueOf(i))
                }
            }
            builder.setPositiveButton(
                "OK"
            ) { _, i -> // Initialize string builder
                val stringBuilder = StringBuilder()
                // use for loop
                for (j in langList.indices) {
                    // concat array value
                    stringBuilder.append(langArray[langList[j]])
                    // check condition
                    if (j != langList.size - 1) {
                        // When j value not equal
                        // to lang list size - 1
                        // add comma
                        stringBuilder.append(", ")
                    }
                }
                // set text on textView
                binding.tvTestName.text = stringBuilder.toString()
                Log.e("TestList", stringBuilder.toString())
                testListNew1.add(stringBuilder.toString())
                Log.e("TestList", testListNew1.toString())


            }
            builder.setNegativeButton(
                "Cancel"
            ) { dialogInterface, i -> // dismiss dialog
                dialogInterface.dismiss()
            }
            builder.setNeutralButton(
                "Clear All"
            ) { _, i ->
                // use for loop
                for (j in selectedLanguage.indices) {
                    // remove all selection
                    selectedLanguage[j] = false
                    // clear language list
                    langList.clear()
                    // clear text view value
                    binding.tvTestName!!.text = ""

                }
            }
            // show dialog
            builder.show()
        })

//        val lm = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
//        adapter = ItemAdapter(this, this)
//        rv = findViewById(R.id.recyclerViewTest)
//        rv!!.adapter = adapter
//        rv!!.setHasFixedSize(true)
//        rv!!.layoutManager = lm
//        val dividerItemDecoration = DividerItemDecoration(rv!!.context, lm.orientation)
//        rv!!.addItemDecoration(dividerItemDecoration)
//        val items: MutableList<ClipData.Item> = ArrayList()

//        items.add(ClipData.Item("TEMPERATURE"))
//        items.add(ClipData.Item("BLOOD_PRESSURE"))
//        items.add(ClipData.Item("WEIGHT"))
//        items.add(ClipData.Item("ECG"))
        //        items.add(ClipData.Item("ECG"))
//        items.add(ClipData.Item("PULSE_OXIMETER"))
//        items.add(ClipData.Item("BLOOD_GLUCOSE"))
//        items.add(ClipData.Item("URINE"))
//        items.add(ClipData.Item("HEMOGLOBIN"))
//        items.add(ClipData.Item("BLOOD_GROUPING"))
//        items.add(ClipData.Item("CHOLESTEROL"))
//        items.add(ClipData.Item("URIC_ACID"))
//        items.add(ClipData.Item("DENGUE"))
//        items.add(ClipData.Item("HIV"))
//        items.add(ClipData.Item("MALARIA"))
//        items.add(ClipData.Item("PREGNANCY"))

//        adapter!!.addAll(items)
//        adapter!!.setActionModeReceiver(activity as ItemAdapter.OnClickAction)
//        binding.recyclerViewTest.layoutManager = GridLayoutManager(context, 2)


        // ArrayList of class ItemsViewModel
        val data = ArrayList<ItemsViewModel>()

        // This loop will create 20 Views containing
        // the image with the count of view

        data.add(ItemsViewModel("TEMPERATURE"))
        data.add(ItemsViewModel("BLOOD_PRESSURE"))
        data.add(ItemsViewModel("WEIGHT"))
        data.add(ItemsViewModel("ECG"))
        data.add(ItemsViewModel("PULSE_OXIMETER"))
        data.add(ItemsViewModel("BLOOD_GLUCOSE"))
        data.add(ItemsViewModel("HEMOGLOBIN"))
        data.add(ItemsViewModel("URIC_ACID"))
        data.add(ItemsViewModel("CHOLESTEROL"))
        data.add(ItemsViewModel("MALARIA"))
        data.add(ItemsViewModel("TYPHOID"))


        // This will pass the ArrayList to our Adapter
        val adapter = AdapterTestList(data)
        binding.recyclerViewTest.layoutManager = GridLayoutManager(context, 2)


        // Setting the Adapter with the recyclerview
        binding.recyclerViewTest.adapter = adapter

        binding.imgBack.setOnClickListener {
            // convertStringToBitmap(imageString)
            onBackPressed()

        }
        if (PatientList.TestHistory == "1") {
            val id = intent.getStringExtra("id")
            val date = intent.getStringExtra("date")
            Log.e("iddd", id.toString())
            name = intent.getStringExtra("customer_name").toString()
            PatientList.TestHistory = ""
            startActivity(
                // Use 'launchPdfFromPath' if you want to use assets file (enable "fromAssets" flag) / internal directory
                PdfViewerActivity.launchPdfFromUrl(
                    context,
                    "https://ehcf.thedemostore.in/report/$id/$date",
                    name + "_HealthCubeReport",
                    "pdf directory to save",
                    enableDownload = true
                )
            )
        }


/*
        binding.appCompatTextView2.setOnClickListener {
             startActivity(Intent(this, AppToEdzxJava::class.java))

            try {

                val temperatureRepoonse =
                    "{\"entry\":[{\"resource\":{\"identifier\":[{\"system\":\"Ezdx\",\"use\":\"usual\",\"value\":\"17588ae8-87e7-4847-84d5-9f56d8361a99\"}],\"" +
                            "resourceType\":\"Observation\",\"code\":{\"coding\":[{\"code\":\"8310-5\",\"display\":\"Body temperature\",\"system\":\"http://loinc.org\"}]},\"subject\":" +
                            "{\"identifier\":[{\"system\":\"Partner\",\"use\":\"usual\",\"value\":\"27912812780122\"}]},\"valueQuantity\":{\"code\":\"°F\",\"system\":\"http://unitsofmeasure.org\"," +
                            "\"unit\":\"°F\",\"value\":81.6}}}],\"resourceType\":\"Bundle\",\"type\":\"collection\"}"
//
//                    val jsonArray = JSONArray(jsonString)
//
//                    if (jsonArray.length() > 0) {
//                        val jsonObject = jsonArray.getJSONObject(0)
//
//                        val entryArray = jsonObject.getJSONArray("entry")
//                        if (entryArray.length() > 0) {
//                            val entryObject = entryArray.getJSONObject(0)
//
//                            val resourceObject = entryObject.getJSONObject("resource")
//                          //  val resourceType = resourceObject.getString("resourceType")
//
//                           // val resourceObject = entryObject.getJSONObject("resourceType")
//                           // val resourceType = resourceObject.getString("subject")
//
////
//                           val identifierArray = resourceObject.getJSONArray("identifier")
////                            val identifierArray = resourceObject1.getJSONArray("valueQuantity")
//
//                            if (identifierArray.length() > 0) {
//                                val identifierObject = identifierArray.getJSONObject(0)
//                                val system = identifierObject.getString("system")
//                                val use = identifierObject.getString("use")
//                                val value = identifierObject.getString("value")
//
//                                Log.e("system", system!!)
//                                 Log.e("use", use!!)
//                                Log.e("value", value!!)
 //                             //   println("Resource Type: $resourceType")
//                                println("Identifier System: $system")
//                                println("Identifier Use: $use")
//                                println("Identifier Value: $value")
//                            }r
//
////                            val codeObject = resourceObject.getJSONObject("code")
////                            val codingArray = codeObject.getJSONArray("coding")
//
//                            val codeObject = resourceObject.getJSONObject("code")
//                            val codingArray = codeObject.getJSONArray("coding")
//                            if (codingArray.length() > 0) {
//                                val codingObject = codingArray.getJSONObject(0)
//                                val code = codingObject.getString("code")
//                                val display = codingObject.getString("display")
//                                val system = codingObject.getString("system")
//                             //   val unit = codingObject.getString("unit")
//                              //  val value = codingObject.getString("value")
//                              //  Log.e("code", code!!)
//                              //  Log.e("display", display!!)
//                                Log.e("code", code!!)
//                                Log.e("display", display!!)
//                                Log.e("system", system!!)
//
//                            }
//                        }
//                    }

                // String JSON = "{\"LanguageLevels\":{\"1\":\"Pocz\\u0105tkuj\\u0105cy\",\"2\":\"\\u015arednioZaawansowany\",\"3\":\"Zaawansowany\",\"4\":\"Ekspert\"}}\n";
//                val JSON =
//                    "[{\"entry\":[{\"resource\":{\"identifier\":[{\"system\":\"Ezdx\",\"use\":\"usual\",\"value\":\"17588ae8-87e7-4847-84d5-9f56d8361a99\"}],\"" +
//                            "resourceType\":\"Observation\",\"code\":{\"coding\":[{\"code\":\"8310-5\",\"display\":\"Body temperature\",\"system\":\"http://loinc.org\"}]},\"subject\":" +
//                            "{\"identifier\":[{\"system\":\"Partner\",\"use\":\"usual\",\"value\":\"27912812780122\"}]},\"valueQuantity\":{\"code\":\"°F\",\"system\":\"http://unitsofmeasure.org\"," +
//                            "\"unit\":\"°F\",\"value\":81.6}}}],\"resourceType\":\"Bundle\",\"type\":\"collection\"}]"

//             val unA = jsonObj.getJSONArray("valueQuantity")
//            for (i in 0 until unA.length()) {
//                val c = unA.getJSONObject(i)
//                val title = c.getString("code")
//                val body = c.getString("value")
//                Log.e("code",title)
//                Log.e("body",body)
//            }

                val reportData = ArrayList<String>()
                reportData.add(temperatureRepoonse)

                val test2 = temperatureRepoonse.substringAfterLast("unit\":\"°F")
                Log.e("test2", test2!!.toString())

                val value1 = test2.substring(10, 14)

                Log.e("value1", value1!!.toString())

                //test2.

//                 val jsonString = "[{\"code\":\"°F\",\"system\":\"http://unitsofmeasure.org\",\"unit\":\"°F\",\"value\":81.6}}}],\"resourceType\":\"Bundle\",\"type\":\"collection\"}]"
//
//                val json = JSONObject(jsonString)
//                val code = json.getString("code")
//                val valueNEw = json.getString("value")
//                Log.e("code", code!!)
//                Log.e("value", value!!)


                //   Log.e("reportData", reportData.toString())
                //   Log.e("test2", test2!!)
                // Log.e("value", value!!.toString())
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
*/


        //apiCallFamilyListNew()
        helthCubePatientId = intent.getStringExtra("id").toString()


        if (PatientList.Exsting == "1") {
            binding.layoutRegister.visibility = View.GONE

            binding.btnMoveEzdx.visibility = View.VISIBLE
            binding.btnSave.visibility = View.GONE
            patient_id = intent.getStringExtra("patient_id").toString()
            name = intent.getStringExtra("customer_name").toString()
            phoneNumber = intent.getStringExtra("phone_number").toString()
            email = intent.getStringExtra("email").toString()
            gender = intent.getStringExtra("gender").toString()
            dateOfBirth = intent.getStringExtra("dob").toString()
            helthCubePatientId = intent.getStringExtra("patient_id").toString()
            gender = when (gender) {
                "1" -> {
                    "male"
                }
                "2" -> {
                    "female"

                }
                else -> {
                    "other"
                }
            }
            apiCallRegisterPatient()
        }

        if (PatientList.Diagnostic == "1") {
            binding.layoutRegister.visibility = View.GONE
            PatientList.Diagnostic = ""
            binding.btnMoveEzdx.visibility = View.VISIBLE
            binding.btnSave.visibility = View.GONE

            name = intent.getStringExtra("customer_name").toString()
            phoneNumber = intent.getStringExtra("phone_number").toString()
            email = intent.getStringExtra("email").toString()
            gender = intent.getStringExtra("gender").toString()
            dateOfBirth = intent.getStringExtra("dob").toString()

            Log.e("dob", dateOfBirth)


            gender = when (gender) {
                "1" -> {
                    "male"
                }
                "2" -> {
                    "female"

                }
                else -> {
                    "other"
                }
            }

            Log.e("name", name)
            Log.e("phoneNumber", phoneNumber)
            Log.e("email", email)
            Log.e("genderNew", gender)
        }
        binding.spinnerCountryCode.setOnCountryChangeListener {
            countryName = binding.spinnerCountryCode.selectedCountryName
            // countryName = countryCode.substring(1)n
            Log.e("countryName,", "$countryName")
        }
//Alauddin
        binding.btnMoveEzdx.setOnClickListener {
            try {
                var launcherIntent = packageManager.getLaunchIntentForPackage(ezdxPackageName)
                if (launcherIntent != null) {
                    launcherIntent.setPackage(packageName)
                    launcherIntent.action = AddPatient::class.java.canonicalName
                    startActivity(launcherIntent)
                } else {
                    // this will launch playstore if app is not installed
                    launcherIntent = Intent(Intent.ACTION_VIEW)
                    launcherIntent.data = Uri.parse("market://details?id=${ezdxPackageName}")
                    startActivity(launcherIntent)

                }
            } catch (e: Exception) {
                Toast.makeText(this@AddPatient, "Error launching ezdx $e", Toast.LENGTH_SHORT)
                    .show()
            }

        }

/*
        binding.btnMoveEzdx.setOnClickListener {
//            if (AdapterTestList.TestName.isEmpty()) {
//                myToast(this@AddPatient, "Select Diagnostics Test")
//            } else {
                // selectedTestList.distinct()

                Log.e("dob", dateOfBirth)
                Log.e("gender", gender)
                Log.e("name", name.toString())
                Log.e("phoneNumber", phoneNumber.toString())

//            var patientDataInHl7String: String? =
//                "{\"address\":[{\"country\":\"india\",\"type\":\"physical\",\"use\":\"home\"}]," +
//                        "\"birthDate\":\"1986-04-19\",\"gender\":\"male\",\"identifier\":" +
//                        "[{\"system\":\"iprd\",\"use\":\"usual\",\"value\":\"27912812780122\"}],\"name\"" +
//                        ":[{\"given\":[\"Alauddin Ansari\"]}],\"resourceType\":\"Patient\",\"telecom\":[{\"rank\":\"1\"," +
//                        "\"system\":\"phone\",\"use\":\"mobile\",\"value\":\"7379452259\"}]}"

                val maleriya="{\"entry\":[{\"resource\":{\"identifier\":[{\"system\":\"Ezdx\",\"use\":\"usual\",\"value\":\"6bd9055c-9c96-44d0-a957-9dab4e2fbf20\"}],\"resourceType\":\"Observation\",\"code\":{\"coding\":[{\"code\":\"76772-3\",\"display\":\"MALARIA\",\"system\":\"http://loinc.org\"}]},\"subject\":{\"identifier\":[{\"system\":\"Partner\",\"use\":\"usual\",\"value\":\"27912812780122\"}]},\"valueString\":\"Negative\"}}],\"resourceType\":\"Bundle\",\"type\":\"collection\"}"

                val typhoid="{\"entry\":[{\"resource\":{\"identifier\":[{\"system\":\"Ezdx\",\"use\":\"usual\",\"value\":\"9528d07c-df25-46a0-81c5-0782a39d0c1a\"}],\"resourceType\":\"Observation\",\"code\":{\"coding\":[{\"code\":\"XTYPHOIDX\",\"display\":\"Typhoid\",\"system\":\"http://loinc.org\"}]},\"subject\":{\"identifier\":[{\"system\":\"Partner\",\"use\":\"usual\",\"value\":\"27912812780122\"}]},\"valueString\":\"Negative\"}}],\"resourceType\":\"Bundle\",\"type\":\"collection\"}"
                var patientDataInHl7String: String? =
                    "{\"address\":[{\"country\":\"india\",\"type\":\"physical\",\"use\":\"home\"}]," +
                            "\"birthDate\":\"$dateOfBirth\",\"gender\":\"$gender\",\"identifier\":" +
                            "[{\"system\":\"iprd\",\"use\":\"usual\",\"value\":\"27912812780122\"}],\"name\"" +
                            ":[{\"given\":[\"$name\"]}],\"resourceType\":\"Patient\",\"telecom\":[{\"rank\":\"1\"," +
                            "\"system\":\"phone\",\"use\":\"mobile\",\"value\":\"$phoneNumber\"}]}"
                try {
                    val test1 = ItemAdapter.Companion.selectedTestList.find { it == "TEMPERATURE" }
                    val test2 =
                        ItemAdapter.Companion.selectedTestList.find { it == "BLOOD_PRESSURE" }
                    val test3 = ItemAdapter.Companion.selectedTestList.find { it == "WEIGHT" }
                    // val test4 = ItemAdapter.Companion.selectedTestList.find { it == "ECG" }

                    hightlightTests = "[\"${AdapterTestList.TestName}\"]"
                    // hightlightTests = "[\"${selectedTestList[0]}\",\"${selectedTestList[1]}\",\"${selectedTestList[2]}\",\"${selectedTestList[3]}\",\"${selectedTestList[4]}\"]"

                    Log.e("test1", test1.toString())
                    Log.e("test1", test2.toString())
                    Log.e("test1", test3.toString())
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                try {
                    var launcherIntent = packageManager.getLaunchIntentForPackage(ezdxPackageName)
                    if (launcherIntent != null) {
                        launcherIntent.setPackage(packageName)
                        launcherIntent.action = AddPatient::class.java.canonicalName
                        if (patientDataInHl7String != null) {
                            launcherIntent.putExtra(
                                "PATIENT_DETAILS",
                                patientDataInHl7String
                            ) // patient data in h7 format json string
                            launcherIntent.putExtra(
                                "TEST_DETAILS",
                                hightlightTests
                            ) // list of testnames. pleae check R.string.testNames for all the test array Ezdx supports
                            if (sampleUserId != null && sampleUserId!!.isNotEmpty() && secretKey != null && secretKey!!.isNotEmpty()) { // login id and encrypted secret needs to sent every time
                                launcherIntent.putExtra("PARTNER_LOGIN_ID", sampleUserId) // user-id
                                launcherIntent.putExtra(
                                    "PARTNER_LOGIN_SECRET",
                                    encrypt(secretKey!!, this@AddPatient)
                                )
                            }
                            startActivity(launcherIntent)
                        } else {
                            Toast.makeText(
                                this@AddPatient,
                                "Please enter correct data",
                                Toast.LENGTH_SHORT
                            ).show()
                            return@setOnClickListener
                        }
                    } else {

                        // this will launch playstore if app is not installed
                        launcherIntent = Intent(Intent.ACTION_VIEW)
                        launcherIntent.data = Uri.parse("market://details?id=${ezdxPackageName}")
                        startActivity(launcherIntent)
                    }
                } catch (e: Exception) {
                    Toast.makeText(this@AddPatient, "Error launching ezdx $e", Toast.LENGTH_SHORT)
                        .show()
                }

            }
*/
        // }


        binding.btnSave.setOnClickListener {
            if (binding.edtName.text!!.isEmpty()) {
                binding.edtName.error = "Enter Name"
                binding.edtName.requestFocus()
                return@setOnClickListener
            }
            if (binding.edtMobile.text!!.isEmpty()) {
                binding.edtMobile.error = "Enter Mobile Number"
                binding.edtMobile.requestFocus()
                return@setOnClickListener
            }
            if (binding.tvDOB.text!!.isEmpty()) {
                myToast(this@AddPatient, "Select DOB")
                return@setOnClickListener
            }
            if (gender.isEmpty()) {
                myToast(this@AddPatient, "Select Gender")
                return@setOnClickListener
            }

            apiCallRegisterPatient()
        }

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
                binding.tvDOB.text = date
                dateOfBirth = SimpleDateFormat("ddMMyyyy", Locale.getDefault()).format(newDate.time)
                dateOfBirthHealth =
                    SimpleDateFormat("ddMMyyyy", Locale.getDefault()).format(newDate.time)
                dateOfBirthNew = date

                Log.e("selectedDate", selectedDate)
            },
            newCalendar1[Calendar.YEAR],
            newCalendar1[Calendar.MONTH],
            newCalendar1[Calendar.DAY_OF_MONTH]
        )

        binding.tvDOB.setOnClickListener {
            datePicker.show()
        }

        binding.tvAddMore.setOnClickListener {
            if (con) {
                binding.layoutOtherInformation.visibility = View.VISIBLE
                con = false
            } else {
                binding.layoutOtherInformation.visibility = View.GONE
                con = true
            }
        }

        binding.layMen.setOnClickListener {
            gender = "MALE"
            binding.tvMale.setTextColor(Color.parseColor("#9F367A"))
            binding.tvFemale.setTextColor(Color.parseColor("#A19398"))
            binding.tvOther.setTextColor(Color.parseColor("#A19398"))
        }

        binding.layWomen.setOnClickListener {
            gender = "FEMALE"
            binding.tvFemale.setTextColor(Color.parseColor("#9F367A"))
            binding.tvOther.setTextColor(Color.parseColor("#A19398"))
            binding.tvMale.setTextColor(Color.parseColor("#A19398"))
        }
        binding.layOther.setOnClickListener {
            gender = "OTHER"
            binding.tvOther.setTextColor(Color.parseColor("#9F367A"))
            binding.tvFemale.setTextColor(Color.parseColor("#A19398"))
            binding.tvMale.setTextColor(Color.parseColor("#A19398"))
        }


        binding.btnDowanloadReport.setOnClickListener {
            startActivity(
                // Use 'launchPdfFromPath' if you want to use assets file (enable "fromAssets" flag) / internal directory
                PdfViewerActivity.launchPdfFromUrl(
                    context,
                    "https://ehcf.thedemostore.in/report/$reportId",                                // PDF URL in String format
                    name + "_HealthCubeReport",                        // PDF Name/Title in String format
                    "pdf directory to save",                  // If nothing specific, Put "" it will save to Downloads
                    enableDownload = true                    // This param is true by defualt.
                )
            )
        }
        binding.btnGenerate.setOnClickListener {
            SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Are you sure want to Generate New Token?")
                .setCancelText("No")
                .setConfirmText("Yes")
                .showCancelButton(true)
                .setConfirmClickListener { sDialog ->
                    sDialog.cancel()
                    apiCallGenerateNewToken()
                }
                .setCancelClickListener { sDialog ->
                    sDialog.cancel()
                }
                .show()
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        Toast.makeText(this, "OnNewIntent called", Toast.LENGTH_SHORT).show()

        val result = intent.getStringExtra(ezdxPackageName + "RESULT").toString()

        binding.recyclerViewTest.visibility = View.GONE

        Log.e("Result", intent.getStringExtra(ezdxPackageName + "RESULT").toString())

        Log.e("sds", selectedTestList.contains("TEMPERATURE").toString())

//
//        if (selectedTestList.contains("TEMPERATURE")) {
//            temperatureResponse(result)
//            selectedTestList.clear()
//        } else if (selectedTestList.contains("BLOOD_PRESSURE")) {
//            bPResponse(result)
//            selectedTestList.clear()
//        } else if (selectedTestList.contains("WEIGHT")) {
//            weightResponse(result)
//            selectedTestList.clear()
//
//        }

        if (AdapterTestList.TestName.contains("TEMPERATURE")) {
            temperatureResponse(result)
        } else if (AdapterTestList.TestName.contains("BLOOD_PRESSURE")) {
            bPResponse(result)
        } else if (AdapterTestList.TestName.contains("WEIGHT")) {
            //  weightResponse(result)
            myToast(this@AddPatient, "Result not Found")

        } else if (AdapterTestList.TestName.contains("ECG")) {
            myToast(this@AddPatient, "Result not Found")

        } else if (AdapterTestList.TestName.contains("PULSE_OXIMETER")) {
            oximetryResponse(result)
        } else if (AdapterTestList.TestName.contains("BLOOD_GLUCOSE")) {
            glucoseResponse(result)
        } else if (AdapterTestList.TestName.contains("HEMOGLOBIN")) {
            hemoglobinResponse(result)
        } else if (AdapterTestList.TestName.contains("URIC_ACID")) {
            uricAcidResponse(result)
        } else if (AdapterTestList.TestName.contains("MALARIA")) {
            malariyaResponse(result)
        } else if (AdapterTestList.TestName.contains("TYPHOID")) {
            typhoidResponse(result)
        }

//        if (selectedTestList.containsAll(listOf("TEMPERATURE","BLOOD_PRESSURE"))){
//            temperatureResponse(result)
//            bPResponse(result)
//        }else{
//            temperatureResponse(result)
//            bPResponse(result)
//            weightResponse(result)
//
//        }


        //


        // ** In case of ECG test, rendering entire byte array data of PDF report in TextView will go out of memory
        // this needs to be handled by dumping the byte array in a file
//        binding.tvResult.visibility = View.VISIBLE
//        binding.tvResult!!.text = intent.getStringExtra(ezdxPackageName + "RESULT") // result from Ezdx
    }


    fun selectAll(v: View?) {
        adapter!!.selectAll()
        if (actionMode == null) {
            //  actionMode = startActionMode(actionModeCallback)
            // actionMode!!.title = "Selected: " + adapter!!.selected.size
        }
    }


    fun deselectAll(v: View?) {
        adapter!!.clearSelected()
        if (actionMode != null) {
            actionMode!!.finish()
            actionMode = null
        }
    }

//    override fun onClickAction() {
//        val selected = adapter!!.selected.size
//        if (actionMode == null) {
//            //  actionMode = startActionMode(actionModeCallback)
//            //  actionMode!!.title = "Selected: $selected"
//        } else {
//            if (selected == 0) {
//                actionMode!!.finish()
//            } else {
//                //  actionMode!!.title = "Selected: $selected"
//            }
//        }
//    }

    fun convertStringToBitmap(string: String?): Bitmap? {
        val byteArray1: ByteArray = Base64.decode(string, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(byteArray1, 0, byteArray1.size)

        binding.image.setImageBitmap(BitmapFactory.decodeByteArray(byteArray1, 0, byteArray1.size))

        Log.e("image", BitmapFactory.decodeByteArray(byteArray1, 0, byteArray1.size).toString())
    }

    private fun temperatureResponse(temperatureResponse: String) {

//        val temperatureRepoonse = "{\"entry\":[{\"resource\":{\"identifier\":[{\"system\":\"Ezdx\",\"use\":\"usual\",\"value\":\"17588ae8-87e7-4847-84d5-9f56d8361a99\"}],\"" +
//                "resourceType\":\"Observation\",\"code\":{\"coding\":[{\"code\":\"8310-5\",\"display\":\"Body temperature\",\"system\":\"http://loinc.org\"}]},\"subject\":" +
//                "{\"identifier\":[{\"system\":\"Partner\",\"use\":\"usual\",\"value\":\"27912812780122\"}]},\"valueQuantity\":{\"code\":\"°F\",\"system\":\"http://unitsofmeasure.org\"," +
//                "\"unit\":\"°F\",\"value\":81.6}}}],\"resourceType\":\"Bundle\",\"type\":\"collection\"}"
        try {
            if (temperatureResponse.isNotEmpty()) {
                val reportData = ArrayList<String>()
                reportData.add(temperatureResponse)

                val test2 = temperatureResponse.substringAfterLast("unit\":\"°F")
                Log.e("test2", test2!!.toString())

                temperature = test2.substring(10, 14)

                binding.cardTemperature.visibility = View.VISIBLE
                binding.tvTemp.text = temperature
                testName = "Temperature"
                apiCallSaveReportData()

                Log.e("temperature", temperature!!.toString())
            } else {
                myToast(this@AddPatient, "No Test Found")
            }
        } catch (e: Exception) {
            e.printStackTrace()

        }
    }

    private fun weightResponse(weightResponse: String) {

//        val temperatureRepoonse = "{\"entry\":[{\"resource\":{\"identifier\":[{\"system\":\"Ezdx\",\"use\":\"usual\",\"value\":\"17588ae8-87e7-4847-84d5-9f56d8361a99\"}],\"" +
//                "resourceType\":\"Observation\",\"code\":{\"coding\":[{\"code\":\"8310-5\",\"display\":\"Body temperature\",\"system\":\"http://loinc.org\"}]},\"subject\":" +
//                "{\"identifier\":[{\"system\":\"Partner\",\"use\":\"usual\",\"value\":\"27912812780122\"}]},\"valueQuantity\":{\"code\":\"°F\",\"system\":\"http://unitsofmeasure.org\"," +
//                "\"unit\":\"°F\",\"value\":81.6}}}],\"resourceType\":\"Bundle\",\"type\":\"collection\"}"

        try {
            if (weightResponse.isNotEmpty()) {
                val reportData = ArrayList<String>()
                reportData.add(weightResponse)

                val test2 = weightResponse.substringAfterLast("unit\":\"°F")
                Log.e("test2", test2!!.toString())

                //val value1 = test2.substring(10, 14)


                //   Log.e("value1", value1!!.toString())
                Log.e("reportData", reportData!!.toString())
            } else {
                myToast(this@AddPatient, "No Test found")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun bPResponse(bPResponse: String) {
        try {
//          val bPResponse="{\"entry\":[{\"resource\":{\"identifier\":[{\"system\":\"Ezdx\",\"use\":\"usual\",\"value\":\"35afe59f-e914-4971-a75a-f2213426ece4\"}]," +
//                 "\"resourceType\":\"Observation\",\"code\":{\"coding\":[{\"code\":\"85354-9\",\"display\":\"Blood pressure panel with all children optional\",\"system\":\"http://loinc.org\"}]}," +
//                 "\"component\":[{\"resource\":{\"code\":{\"coding\":[{\"code\":\"8480-6\",\"display\":\"Systolic blood pressure\",\"system\":\"http://loinc.org\"}]},\"valueQuantity\":{\"code\":\"mmHg\"," +
//                 "\"system\":\"http://unitsofmeasure.org\",\"unit\":\"mmHg\",\"value\":123.0}}},{\"resource\":{\"code\":{\"coding\":[{\"code\":\"8462-4\",\"display\":\"Diastolic blood pressure\",\"system\":\"http://loinc.org\"}]}," +
//                 "\"valueQuantity\":{\"code\":\"mmHg\",\"system\":\"http://unitsofmeasure.org\",\"unit\":\"mmHg\",\"value\":83.0}}}],\"subject\":{\"identifier\":[{\"system\":\"Partner\",\"use\":\"usual\"," +
//                 "\"value\":\"27912812780122\"}]}}}],\"resourceType\":\"Bundle\",\"type\":\"collection\"}"

            if (bPResponse.isNotEmpty()) {
                val reportData = ArrayList<String>()
                reportData.add(bPResponse)

                val test2 = bPResponse.substringAfterLast("Systolic blood pressure")

                systolic = test2.substring(123, 128)
                diastolic = test2.substring(338, 342)
                binding.cardBP.visibility = View.VISIBLE
                binding.tvSystolic.text = systolic
                binding.tvDiastolic.text = diastolic
                testName = "Blood Pressure"
                apiCallSaveReportData()


                Log.e("Systolic", systolic!!.toString())
                Log.e("Diastolic", diastolic!!.toString())
            } else {
                myToast(this@AddPatient, "No Test Found")
            }

        } catch (e: Exception) {
            myToast(this, "Something went wrong")
            e.printStackTrace()
        }
    }

    private fun oximetryResponse(oximetryResponse: String) {
        try {
//            val oximetryResponse =
//                "{\"entry\":[{\"resource\":{\"identifier\":[{\"system\":\"Ezdx\",\"use\":\"usual\",\"value\":" +
//                        "\"2b18ad96-e2c5-44d6-bc14-69c59c362b45\"}],\"resourceType\":\"Observation\",\"code\":{\"coding\":[{\"code\":" +
//                        "\"59408-5\",\"display\":\"Oxygen saturation in Arterial blood by Pulse oximetry\",\"system\":\"http://loinc.org\"}]}," +
//                        "\"subject\":{\"identifier\":[{\"system\":\"Partner\",\"use\":\"usual\",\"value\":\"27912812780122\"}]},\"valueQuantity\"" +
//                        ":{\"code\":\"%\",\"system\":\"http://unitsofmeasure.org\",\"unit\":\"%\",\"value\":97.0}}},{\"resource\":{\"identifier" +
//                        "\":[{\"system\":\"Ezdx\",\"use\":\"usual\",\"value\":\"2b18ad96-e2c5-44d6-bc14-69c59c362b45\"}],\"resourceType\":" +
//                        "\"Observation\",\"code\":{\"coding\":[{\"code\":\"8867-4\",\"display\":\"Heart rate\",\"system\":\"http://loinc.org\"}]}," +
//                        "\"subject\":{\"identifier\":[{\"system\":\"Partner\",\"use\":\"usual\",\"value\":\"27912812780122\"}]},\"valueQuantity\"" +
//                        ":{\"code\":\"bpm\",\"system\":\"http://unitsofmeasure.org\",\"unit\":\"bpm\",\"value\":74.0}}}],\"resourceType\":\"Bundle\"," +
//                        "\"type\":\"collection\"}"


            val glicose =
                "{\"entry\":[{\"resource\":{\"identifier\":[{\"system\":\"Ezdx\",\"use\":\"usual\",\"value\":\"f784d695-ed4b-408f-a937-5316370f2ede\"}],\"resourceType\":\"Observation\",\"code\":{\"coding\":[{\"code\":\"2339-0\",\"display\":\"Glucose [Mass/\u200Bvolume] in Blood\",\"system\":\"http://loinc.org\"}]},\"subject\":{\"identifier\":[{\"system\":\"Partner\",\"use\":\"usual\",\"value\":\"27912812780122\"}]},\"valueQuantity\":{\"code\":\"mg/dL\",\"system\":\"http://unitsofmeasure.org\",\"unit\":\"mg/dL\",\"value\":92.0}}}],\"resourceType\":\"Bundle\",\"type\":\"collection\"}"

            if (oximetryResponse.isNotEmpty()) {
                val reportData = ArrayList<String>()
                reportData.add(oximetryResponse)

                val test2 = oximetryResponse.substringAfterLast("unit\":\"%\",\"value")

                spO2 = test2.substring(2, 6)
                pulseRate = test2.substring(410, 414)
                binding.cardOximeter.visibility = View.VISIBLE
                binding.tvSpO2.text = spO2
                binding.tvPulseRate.text = pulseRate
                testName = "PULSE OXIMETRY"
                apiCallSaveReportData()


                Log.e("spO2", spO2!!.toString())
                Log.e("pulseRate", pulseRate!!.toString())
                Log.e("test2", test2!!.toString())
            } else {
                myToast(this@AddPatient, "No Test Found")
            }

        } catch (e: Exception) {
            myToast(this, "Something went wrong")
            e.printStackTrace()
        }
    }

    private fun glucoseResponse(glucoseResponse: String) {
        try {

//            val glucoseResponse =
//                "{\"entry\":[{\"resource\":{\"identifier\":[{\"system\":\"Ezdx\",\"use\":\"usual\",\"value\":" +
//                        "\"f784d695-ed4b-408f-a937-5316370f2ede\"}],\"resourceType\":\"Observation\",\"code\":{\"coding\":[{\"code\":" +
//                        "\"2339-0\",\"display\":\"Glucose [Mass/\u200Bvolume] in Blood\",\"system\":\"http://loinc.org\"}]},\"subject\"" +
//                        ":{\"identifier\":[{\"system\":\"Partner\",\"use\":\"usual\",\"value\":\"27912812780122\"}]},\"valueQuantity\":{" +
//                        "\"code\":\"mg/dL\",\"system\":\"http://unitsofmeasure.org\",\"unit\":\"mg/dL\",\"value\":92.0}}}],\"resourceType\":" +
//                        "\"Bundle\",\"type\":\"collection\"}"

            if (glucoseResponse.isNotEmpty()) {
                val reportData = ArrayList<String>()
                reportData.add(glucoseResponse)

                val test2 = glucoseResponse.substringAfterLast("mg/dL")

                bloodGlucose = test2.substring(10, 14)

                binding.cardGlucose.visibility = View.VISIBLE
                binding.tvGlucose.text = bloodGlucose
                testName = "Blood Glucose"
                apiCallSaveReportData()


                Log.e("bloodGlucose", bloodGlucose!!.toString())
                Log.e("test2", test2!!.toString())
            } else {
                myToast(this@AddPatient, "No Test Found")
            }

        } catch (e: Exception) {
            myToast(this, "Something went wrong")
            e.printStackTrace()
        }
    }

    private fun hemoglobinResponse(glucoseResponse: String) {
        try {

//            val glucoseResponse =
//            " {\"entry\":[{\"resource\":{\"identifier\":[{\"system\":\"Ezdx\",\"use\":\"usual\",\"value\":" +
//                    "\"92f0deb1-07dd-4280-b309-f633ec4cf2a0\"}],\"resourceType\":\"Observation\",\"code\":{\"coding\":[{\"code\":" +
//                    "\"XHEMOGLX\",\"display\":\"Hemoglobin [Mass/volume] in Blood\",\"system\":\"http://loinc.org\"}]},\"subject\"" +
//                    ":{\"identifier\":[{\"system\":\"Partner\",\"use\":\"usual\",\"value\":\"27912812780122\"}]},\"valueQuantity\"" +
//                    ":{\"code\":\"g/dL\",\"system\":\"http://unitsofmeasure.org\",\"unit\":\"g/dL\",\"value\":17.9}}}],\"resourceType\"" +
//                    ":\"Bundle\",\"type\":\"collection\"}"


            if (glucoseResponse.isNotEmpty()) {
                val reportData = ArrayList<String>()
                reportData.add(glucoseResponse)

                val test2 = glucoseResponse.substringAfterLast("unit\":\"g/dL")

                hemoglobin = test2.substring(10, 14).replace("}", "")

                binding.cardHemoglobin.visibility = View.VISIBLE
                binding.tvHemoglobin.text = hemoglobin
                testName = "Hemoglobin"
                apiCallSaveReportData()


                Log.e("hemoglobin", hemoglobin!!.toString())
                Log.e("test2", test2!!.toString())
            } else {
                myToast(this@AddPatient, "No Test Found")
            }

        } catch (e: Exception) {
            myToast(this, "Something went wrong")
            e.printStackTrace()
        }
    }

    private fun uricAcidResponse(uricAcidResponse: String) {
        try {
            val uricAcidResponse =
//                "{\"entry\":[{\"resource\":{\"identifier\":[{\"system\":\"Ezdx\",\"use\":\"usual\",\"value\":" +
//                        "\"dd0c5d90-bfcc-48b2-84f6-377fbcde5267\"}],\"resourceType\":\"Observation\",\"code\":{" +
//                        "\"coding\":[{\"code\":\"XURICACX\",\"display\":\"Uric Acid\",\"system\":\"http://loinc.org\"}]}," +
//                        "\"subject\":{\"identifier\":[{\"system\":\"Partner\",\"use\":\"usual\",\"value\":\"27912812780122\"}]}," +
//                        "\"valueQuantity\":{\"code\":\"mg/dL\",\"system\":\"http://unitsofmeasure.org\",\"unit\":\"mg/dL\",\"value\":0.2}}}],\"resourceType\":\"Bundle\",\"type\":\"collection\"}"
                if (uricAcidResponse.isNotEmpty()) {
                    val reportData = ArrayList<String>()
                    reportData.add(uricAcidResponse)

                    val test2 = uricAcidResponse.substringAfterLast("unit\":\"mg/dL")

                    uricAcid = test2.substring(10, 13)

                    binding.cardUricAcid.visibility = View.VISIBLE
                    binding.tvUricAcid.text = uricAcid
                    testName = "Uric Acid"
                    apiCallSaveReportData()


                    Log.e("uricAcid", uricAcid!!.toString())
                    Log.e("test2", test2!!.toString())
                } else {
                    myToast(this@AddPatient, "No Test Found")
                }

        } catch (e: Exception) {
            myToast(this, "Something went wrong")
            e.printStackTrace()
        }
    }

    private fun malariyaResponse(malariyaResponse: String) {
        try {
//            val malariyaResponse = "{\"entry\":[{\"resource\":{\"identifier\":[{\"system\":\"Ezdx\",\"use\":\"usual\"," +
//                    "\"value\":\"6bd9055c-9c96-44d0-a957-9dab4e2fbf20\"}],\"resourceType\":\"Observation\",\"code\":{" +
//                    "\"coding\":[{\"code\":\"76772-3\",\"display\":\"MALARIA\",\"system\":\"http://loinc.org\"}]},\"subject\":{" +
//                    "\"identifier\":[{\"system\":\"Partner\",\"use\":\"usual\",\"value\":\"27912812780122\"}]},\"valueString\":" +
//                    "\"Negative\"}}],\"resourceType\":\"Bundle\",\"type\":\"collection\"}"
            if (malariyaResponse.isNotEmpty()) {
                val reportData = ArrayList<String>()
                reportData.add(malariyaResponse)

                val test2 = malariyaResponse.substringAfterLast("valueString")

                malaria = test2.substring(3, 17).replace("}", "").replace("]", "")
                    .replace(",", "").replace('"', ' ')

                binding.cardMalaria.visibility = View.VISIBLE
                binding.tvMalaria.text = malaria
                testName = "Malaria"
                apiCallSaveReportData()


                Log.e("malaria", malaria!!.toString())
                Log.e("test2", test2!!.toString())
            } else {
                myToast(this@AddPatient, "No Test Found")
            }

        } catch (e: Exception) {
            myToast(this, "Something went wrong")
            e.printStackTrace()
        }
    }

    private fun typhoidResponse(typhoidResponse: String) {
        try {
//            val typhoidResponse = "{\"entry\":[{\"resource\":{\"identifier\":[{\"system\":\"Ezdx\",\"use\":\"usual\"," +
//                    "\"value\":\"6bd9055c-9c96-44d0-a957-9dab4e2fbf20\"}],\"resourceType\":\"Observation\",\"code\":{" +
//                    "\"coding\":[{\"code\":\"76772-3\",\"display\":\"MALARIA\",\"system\":\"http://loinc.org\"}]},\"subject\":{" +
//                    "\"identifier\":[{\"system\":\"Partner\",\"use\":\"usual\",\"value\":\"27912812780122\"}]},\"valueString\":" +
//                    "\"Negative\"}}],\"resourceType\":\"Bundle\",\"type\":\"collection\"}"
            if (typhoidResponse.isNotEmpty()) {
                val reportData = ArrayList<String>()
                reportData.add(typhoidResponse)

                val test2 = typhoidResponse.substringAfterLast("valueString")

                typhoid = test2.substring(3, 17).replace("}", "").replace("]", "")
                    .replace(",", "").replace('"', ' ')

                binding.cardTyphoid.visibility = View.VISIBLE
                binding.tvTyphoid.text = typhoid
                testName = "Typhoid"
                apiCallSaveReportData()


                Log.e("typhoid", typhoid!!.toString())
                Log.e("test2", test2!!.toString())
            } else {
                myToast(this@AddPatient, "No Test Found")
            }

        } catch (e: Exception) {
            myToast(this, "Something went wrong")
            e.printStackTrace()
        }
    }

    companion object {
        const val ezdxPackageName =
            "com.healthcubed.ezdx.ezdx.demo" // pointing to demo environment, For your testing

        // method to encrypt secret-key
        @Throws(Exception::class)
        fun encrypt(secretKey: String, activity: AppCompatActivity): String {
            val srcBuff = secretKey.toByteArray(charset("UTF8"))
            val skeySpec = SecretKeySpec(
                (activity.packageName + "ezdxandroid").substring(0, 16).toByteArray(),
                "AES"
            )
            val ivSpec = IvParameterSpec(
                (activity.packageName + "ezdxandroid").substring(0, 16).toByteArray()
            )
            val ecipher = Cipher.getInstance("AES/CBC/PKCS7Padding")
            ecipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivSpec)
            val dstBuff = ecipher.doFinal(srcBuff)
            return Base64.encodeToString(dstBuff, Base64.DEFAULT)
        }
    }

    private fun apiCallRegisterPatient() {
        progressDialog = ProgressDialog(this@AddPatient)
        progressDialog!!.setMessage("Loading..")
        progressDialog!!.setTitle("Please Wait")
        progressDialog!!.isIndeterminate = false
        progressDialog!!.setCancelable(true)
        progressDialog!!.show()

        if (PatientList.Exsting == "1") {
            PatientList.Exsting = ""

        } else {
            name = binding.edtName.text.toString()
            city = binding.edtCity.text.toString()
            state = binding.edtState.text.toString()
            postelCode = binding.edtPostelCode.text.toString()
            email = binding.edtEmail.text.toString()
            phoneNumber = binding.edtMobile.text.toString()
            val code = "+91 "
            phoneNumberNew = code + phoneNumber
        }


        ApiClient.apiService.patientHealthcubeReg(
            name,
            gender,
            city,
            state,
            dateOfBirthNew,
            countryName,
            phoneNumberNew,
            "+91 9392595905",
            "24643-9246-${sessionManager.id}",
            testList,
            postelCode,
            email,
            sessionManager.id.toString(), ""
        )
            .enqueue(object :
                Callback<ModelHealthCubeReg> {
                override fun onResponse(
                    call: Call<ModelHealthCubeReg>,
                    response: Response<ModelHealthCubeReg>
                ) {
                    try {
                        if (response.code() == 500) {
                            myToast(this@AddPatient, "Server Error")
                        } else if (response.body()!!.status == 1) {
                            progressDialog!!.dismiss()
                            binding.btnMoveEzdx.visibility = View.VISIBLE
                            binding.btnSave.visibility = View.GONE
                            binding.layoutRegister.visibility = View.GONE
                            binding.btnGenerate.visibility = View.GONE

                            helthCubePatientId = response.body()!!.result.id.toString()
                            externalPatientId =
                                response.body()!!.result.externalPatientId.toString()
                            // myToast(this@AddPatient, response.body()!!.message)
                            apiCallRegisterHealthCube(externalPatientId)
                        } else {
                            myToast(this@AddPatient, response.body()!!.message)
                            progressDialog!!.dismiss()

                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        myToast(this@AddPatient, "Something went wrong")
                        progressDialog!!.dismiss()

                    }
                }

                override fun onFailure(call: Call<ModelHealthCubeReg>, t: Throwable) {
                    progressDialog!!.dismiss()
                    // myToast(this@AddPatient, "Something went wrong")

                }

            })


    }

    private fun apiCallSaveReportData() {
        progressDialog = ProgressDialog(this@AddPatient)
        progressDialog!!.setMessage("Loading..")
        progressDialog!!.setTitle("Please Wait")
        progressDialog!!.isIndeterminate = false
        progressDialog!!.setCancelable(true)
        progressDialog!!.show()

        ApiClient.apiService.healthcubeReportInsert(
            helthCubePatientId, testName, sessionManager.id.toString(), temperature,
            "", "", "", "", "", "", "", "",
            "", "", "", "", "", diastolic, systolic, spO2, pulseRate, bloodGlucose, uricAcid,
            hemoglobin, malaria, typhoid
        )
            .enqueue(object :
                Callback<ModelHealthCubeReg> {
                override fun onResponse(
                    call: Call<ModelHealthCubeReg>,
                    response: Response<ModelHealthCubeReg>
                ) {
                    try {

                        if (response.body()!!.status == 1) {
                            progressDialog!!.dismiss()
                            binding.btnDowanloadReport.visibility = View.VISIBLE
                            reportId = response.body()!!.result.id.toString()
                            //myToast(requireActivity(), response.body()!!.message)
                        } else {
                            myToast(this@AddPatient, response.body()!!.message)
                            progressDialog!!.dismiss()

                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        myToast(this@AddPatient, "Error In Saving report Data")
                    }
                }

                override fun onFailure(call: Call<ModelHealthCubeReg>, t: Throwable) {
                    progressDialog!!.dismiss()
                    myToast(this@AddPatient, "Error In Saving report Data")

                    // myToast(this@AddPatient, "Something went wrong")

                }

            })


    }

    private fun apiCallRegisterPatientExisting() {
        val code = "+91 "
        phoneNumberNew = code + phoneNumber

        ApiClient.apiService.patientHealthcubeReg(
            name,
            gender,
            city,
            state,
            dateOfBirth,
            countryName,
            phoneNumberNew,
            "+91 9392595905",
            "24643-9246-${sessionManager.id}",
            testList,
            postelCode,
            email,
            sessionManager.id.toString(), patient_id
        )
            .enqueue(object :
                Callback<ModelHealthCubeReg> {
                override fun onResponse(
                    call: Call<ModelHealthCubeReg>,
                    response: Response<ModelHealthCubeReg>
                ) {
                    try {
                        if (response.body()!!.status == 1) {
                            myToast(this@AddPatient, response.body()!!.message)
                        } else {
                            myToast(this@AddPatient, response.body()!!.message)

                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        myToast(this@AddPatient, "Something went wrong")
                    }
                }

                override fun onFailure(call: Call<ModelHealthCubeReg>, t: Throwable) {
                    progressDialog!!.dismiss()
                    apiCallRegisterPatientExisting()
                    // myToast(this@AddPatient, "Something went wrong")

                }

            })
    }


    /*
        private fun apiCallMedicine() {
            val retrofitBuilder = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                //.baseUrl("https://jsonplaceholder.typicode.com/")
                .baseUrl("https://demo-ezdx.healthcubed.com/ezdx-partner-srv/")
                .build()
                .create(ApiInterfaceHelthCube::class.java)

            val name = binding.edtName.text.toString()
            val city = binding.edtCity.text.toString()
            val state = binding.edtState.text.toString()
            val postelCode = binding.edtPostelCode.text.toString()
            val email = binding.edtEmail.text.toString()
            val phoneNumber = binding.edtMobile.text.toString()
            val phoneNumberNew = "+91$phoneNumber"
            progressDialog = ProgressDialog(this@AddPatient)
            progressDialog!!.setMessage("Loading..")
            progressDialog!!.setTitle("Please Wait")
            progressDialog!!.isIndeterminate = false
            progressDialog!!.setCancelable(true)
            progressDialog!!.show()

            val retrofitData = retrofitBuilder.registrationHealthCube(
                accessToken,
                name,
                selectedDate,
                gender,
                email,
                city,
                state,
                postelCode,
                countryName,
                phoneNumberNew,
                "+91 9392595905",
                sessionManager.id.toString(), reportList
            )
            retrofitData.enqueue(object : Callback<ModelRegister> {
                override fun onResponse(
                    call: Call<ModelRegister>,
                    response: Response<ModelRegister>
                ) {
                    if (response.code() == 500) {
                        myToast(this@AddPatient, response.body()!!.message)
                        progressDialog!!.dismiss()
                    } else if (response.code() == 200) {
                        myToast(this@AddPatient, response.body()!!.message)
                        progressDialog!!.dismiss()
                    } else {
                        myToast(this@AddPatient, response.body()!!.message)
                        progressDialog!!.dismiss()

                    }
                }

                override fun onFailure(call: Call<ModelRegister>, t: Throwable) {
                    t.message?.let { myToast(this@AddPatient, it) }
                }
            })
        }
    */
    private fun apiCallGenerateNewToken() {

        progressDialog = ProgressDialog(this@AddPatient)
        progressDialog!!.setMessage("Loading..")
        progressDialog!!.setTitle("Please Wait")
        progressDialog!!.isIndeterminate = false
        progressDialog!!.setCancelable(true)
        progressDialog!!.show()

        val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            //.baseUrl("https://jsonplaceholder.typicode.com/")
            .baseUrl("https://kc-ezdx.healthcubed.com/auth/realms/partner/protocol/openid-connect/")
            .build()
            .create(ApiInterfaceHealthCube::class.java)

        val retrofitData = retrofitBuilder.createTokenNew(
                 "client_credentials",
                "a73a6a93-4fdb-4239-a8c7-e2eed5ae6853",
                "bce6f92b-c106-404e-b255-ffad5dc0e434",
        )


//        ApiClientHealthCube.apiService.createTokenNew(
//                "client_credentials",
//                "a73a6a93-4fdb-4239-a8c7-e2eed5ae6853",
//                "bce6f92b-c106-404e-b255-ffad5dc0e434",
//            )

        retrofitData.enqueue(object : Callback<ModelToken> {
                override fun onResponse(
                    call: Call<ModelToken>,
                    response: Response<ModelToken>
                ) {
                    try {
                        if (response.code() == 500) {
                            myToast(this@AddPatient, "Server Error")
                            progressDialog!!.dismiss()
                        } else if (response.code() == 404) {
                            myToast(this@AddPatient, response.body()!!.error_description)
                            progressDialog!!.dismiss()
                        }else if (response.code() == 400) {
                            myToast(this@AddPatient, response.body()!!.error)
                            progressDialog!!.dismiss()
                        } else if (response.code() == 200) {
                            myToast(this@AddPatient, "New Token Generated")
                            Log.e("response.body()!!", response.body()!!.toString())
                            accessToken="Bearer "+response.body()!!.access_token
                            //   refresh()
                            progressDialog!!.dismiss()
                        } else {
                            // myToast(this@AddPatient, response.body()!!.error)
                            Log.e("response.body()!!", response.body()!!.toString())
                            Log.e("response.code()!!", response.code()!!.toString())
                            progressDialog!!.dismiss()
                        }
                    } catch (re: Exception) {
                        re.printStackTrace()
                        progressDialog!!.dismiss()
                        myToast(this@AddPatient, "Something went wrong")
                    }
                }

                override fun onFailure(call: Call<ModelToken>, t: Throwable) {
                    t.message?.let {
                        myToast(this@AddPatient, it)
                        progressDialog!!.dismiss()

                    }
                }
            })
    }


    private fun apiCallRegisterHealthCube(externalPatientId: String) {

        progressDialog = ProgressDialog(this@AddPatient)
        progressDialog!!.setMessage("Loading..")
        progressDialog!!.setTitle("Please Wait")
        progressDialog!!.isIndeterminate = false
        progressDialog!!.setCancelable(true)
        progressDialog!!.show()
        val name = binding.edtName.text.toString()
        val city = binding.edtCity.text.toString()
        val state = binding.edtState.text.toString()
        val postelCode = binding.edtPostelCode.text.toString()
        val email = binding.edtEmail.text.toString()
        val phoneNumber = binding.edtMobile.text.toString()
        val code = "+91 "
        val phoneNumberNew = code + phoneNumber

        Log.e("NAme", name)
        Log.e("dateOfBirthNew", dateOfBirthNew)
        Log.e("gender", gender)
        Log.e("email", email)
        Log.e("city", city)
        Log.e("state", state)
        Log.e("postelCode", postelCode)
        Log.e("countryName", countryName)
        Log.e("phoneNumberNew", phoneNumberNew)
        Log.e("externalPatientId", externalPatientId)
        Log.e("testListNew1", testListNew1.toString())
        Log.e("dateOfBirthHealth", dateOfBirthHealth.toString())


        ApiClientHealthCube.apiService.postData(
            accessToken, DataModal(
                "$name", "$dateOfBirthHealth", "$gender",
                "",
                "+91 9392595905", "$externalPatientId",
                testListNew1,
            )
        )

            .enqueue(object : Callback<ModelRegister> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelRegister>,
                    response: Response<ModelRegister>
                ) {
                    try {
                        if (response.code() == 500) {
                            myToast(this@AddPatient, "Server Error")
                            progressDialog!!.dismiss()
                        } else if (response.code() == 401) {
                            myToast(this@AddPatient, "Token Expired")
                            // myToast(this@AddPatient, "Unauthorized")
                            progressDialog!!.dismiss()
                        } else if (response.code() == 200) {
                            myToast(this@AddPatient, response.body()!!.message)
                            //   refresh()
                            progressDialog!!.dismiss()
                        } else {
                            myToast(this@AddPatient, response.body()!!.message)
                            // myToast(this@AddPatient, response.body()!!.message)
                            progressDialog!!.dismiss()
                        }
                    } catch (e: Exception) {
                        myToast(this@AddPatient, "Something went wrong")

                        Log.e("Exception", e.printStackTrace().toString())
                        e.printStackTrace()
                        progressDialog!!.dismiss()

                    }
                }

                override fun onFailure(call: Call<ModelRegister>, t: Throwable) {
                    myToast(this@AddPatient, "Something went wrong")
                    progressDialog!!.dismiss()

                }

            })

    }


    fun refresh() {
        overridePendingTransition(0, 0)
        finish()
        startActivity(intent)
        overridePendingTransition(0, 0)
    }

    override fun onResume() {
        super.onResume()
        AdapterTestList.TestName = ""

    }

//    override fun onBackPressed() {
//        super.onBackPressed()
//
//
//    }


    //  @SuppressLint("SuspiciousIndentation")

//    override fun selctedTestList(item: String) {
//        selectedTestList.add(item)
//        selectedTestList.distinct()
//        Log.e("Item", item)
//        Log.e("selectedTestList", selectedTestList.toString())
//
//    }
}

// hightlightTests = "[\"PULSE_OXIMETER\",\"WEIGHT\",\"BLOOD_PRESSURE\",\"ECG\",\"TEMPERATURE\"]"


/*
    private fun postData() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://demo-ezdx.healthcubed.com/ezdx-partner-srv/") // as we are sending data in json format so
            // we have to add Gson converter factory
            .addConverterFactory(GsonConverterFactory.create()) // at last we are building our retrofit builder.
            .build()

        val retrofitAPI: ApiInterfaceHelthCube = retrofit.create(ApiInterfaceHelthCube::class.java)

        reportList.add("")
        reportList.add("")
        var listA = listOf("BLOOD_GLUCOSE", "TEMPERATURE")

        Log.e("List", listA.toString())

        val modal = DataModal(
            "NewRashmi", "461462400", "MALE",
            "MALE@gmail.com", "hyderabad", "telengana", "500001", "India",
            "7379452259", "+91 9392595905", "24643-9246-1"
        )

        // calling a method to create a post and passing our modal class.

        val call: Call<DataModal?>? = retrofitAPI.createPost(accessToken, modal)

        // on below line we are executing our method.
        call!!.enqueue(object : Callback<DataModal?> {
            override fun onResponse(call: Call<DataModal?>, response: Response<DataModal?>) {
                // this method is called when we get response from our api.
                Log.e("df", response.message())
                Log.e("code", response.code().toString())
                if (response.code() == 200) {
                    Toast.makeText(this@AddPatient, "Register", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@AddPatient, "Error", Toast.LENGTH_SHORT).show()

                }


                // below line is for hiding our progress bar.

                // on below line we are setting empty text


                // we are getting response from our body
                // and passing it to our modal class.
                val responseFromAPI: DataModal? = response.body()

                // on below line we are getting our data from modal class
                // and adding it to our string.
            }


            override fun onFailure(call: Call<DataModal?>, t: Throwable) {
                // setting text to our text view when
                // we get error response from API.
                //responseTV.setText("Error found is : " + t.message)
            }
        })
    }
*/

