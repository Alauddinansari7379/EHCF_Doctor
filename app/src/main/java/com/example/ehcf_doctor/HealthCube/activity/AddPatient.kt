package com.example.ehcf_doctor.HealthCube.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.ProgressDialog
import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ehcf.Helper.myToast
import com.example.ehcf.sharedpreferences.SessionManager
import com.example.ehcf_doctor.HealthCube.Adapter.ItemAdapter
import com.example.ehcf_doctor.HealthCube.Adapter.ItemAdapter.Companion.selectedTestList
import com.example.ehcf_doctor.HealthCube.Model.*
import com.example.ehcf_doctor.R
import com.example.ehcf_doctor.databinding.ActivityAddPatientBinding
import com.example.myrecyview.apiclient.ApiClient
import com.example.myrecyview.apiclient.ApiClientHelthCube
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec


class AddPatient : AppCompatActivity(), ItemAdapter.OnClickAction, ItemAdapter.TestList {
    private val context: Context = this@AddPatient
    private lateinit var sessionManager: SessionManager
    var progressDialog: ProgressDialog? = null
    var gender = ""
    var reportList = ArrayList<String>()
    var mydilaog: Dialog? = null
    private var countryName = ""
    private var selectedDate = ""
    private var testListNew = ""
    var hightlightTests = ""
    private var countryCode = ""
    var sampleUserId: String? = "+919392595905"
    var secretKey: String? = "a1fc979a-03d6-4ca6-9196-d7192873d4a8"
    var client_id: String? = "b96214a0-2d05-4e65-be92-b83852227733"
    var name = ""
    var city = ""
    var state = ""
    var postelCode = ""
    var phoneNumber = ""
    var phoneNumberNew = ""
    var email = ""
    var value = ""
    var unit = ""
    private val diagnosticTestList: MutableList<String> = ArrayList()
    val testList = kotlin.collections.ArrayList<String>()


    private var accessToken =
        "eyJhbGciOiJSUzI1NiIsImtpZCI6ImYwNmNhYjZhLTBmZGItNGVmMi1iMDMwLTZlZDY1Njg2ODVkZCJ9.eyJ1YV9oYXNoIjoiYWRlNTBhZmE2YTNjY2M3OWVmYWZjMjllNzYwN2FiZGMiLCJzaWQiOiIxZDU5NjM5Ni00ZjNhLTQyYTItYWZhYy1hYzllZDc0YWUzNDEiLCJzdWIiOiJBTk9OWU1PVVMiLCJhdWQiOiJiOTYyMTRhMC0yZDA1LTRlNjUtYmU5Mi1iODM4NTIyMjc3MzMiLCJpYXQiOjE2OTAyNjg2NjksImF1dGhfdGltZSI6MTY5MDI2ODY2OSwiaXNzIjoiaHR0cHM6Ly9kZW1vLWV6ZHgtZnJlZS5jaWRhYXMuZGUiLCJqdGkiOiI0ZTQ1MWZmNy03YjVjLTQ0OTktYmRlMi05YmE3NjM3NDk5YzciLCJzY29wZXMiOlsiY2lkYWFzOmZkc19zZXR0aW5nc193cml0ZSIsImNpZGFhczpmZHNfc2V0dGluZ3NfcmVhZCIsImNpZGFhczpzdHJpcGVfYWNjb3VudF9jcmVhdGUiLCJjaWRhYXM6Y2hlY2tvdXRfd3JpdGUiLCJjaWRhYXM6dHJhbnNhY3Rpb25fbG9nX3dyaXRlIiwiY2lkYWFzOnRyYW5zYWN0aW9uX2xvZ19yZWFkIiwiY2lkYWFzOmZpZWxkX3NldHVwX3JlYWQiLCJjaWRhYXM6cGF5bWVudGNvbmZpZ19yZWFkIiwiY2lkYWFzOnBheW1lbnRjb25maWdfZGVsZXRlIiwiY2lkYWFzOnBheW1lbnRjb25maWdfd3JpdGUiLCJjaWRhYXM6bmV3c2xldHRlcl9kZWxldGUiLCJjaWRhYXM6bmV3c2xldHRlcl93cml0ZSIsImNpZGFhczp1c2VyY3JlYXRlIiwiY2lkYWFzOndlYmhvb2siLCJjaWRhYXM6bG9naW4iLCJjaWRhYXM6cmVtb3ZlX3Nlc3Npb24iLCJvcGVuaWQiLCJwcm9maWxlIiwiZW1haWwiLCJhZGRyZXNzIiwicGhvbmUiLCJncm91cHMiLCJyb2xlcyIsIm9mZmxpbmVfYWNjZXNzIiwiY2lkYWFzOnJlZ2lzdGVyIiwiaWRlbnRpdGllcyIsImNpZGFhczppbnZpdGUiLCJjaWRhYXM6YWRtaW5fcmVhZCIsImNpZGFhczphcHBzX3JlYWQiLCJjaWRhYXM6YWRtaW5fZGVsZXRlIiwiY2lkYWFzOmFkbWluX3dyaXRlIiwiY2lkYWFzOnNjb3Blc19kZWxldGUiLCJjaWRhYXM6c2NvcGVzX3dyaXRlIiwiY2lkYWFzOnNjb3Blc19yZWFkIiwiY2lkYWFzOmFwcHNfZGVsZXRlIiwiY2lkYWFzOmFwcHNfd3JpdGUiLCJjaWRhYXM6dXNlcnNfd3JpdGUiLCJjaWRhYXM6dXNlcnNfcmVhZCIsImNpZGFhczpzZWN1cml0eV9rZXlfZGVsZXRlIiwiY2lkYWFzOnNlY3VyaXR5X2tleV93cml0ZSIsImNpZGFhczpzZWN1cml0eV9rZXlfcmVhZCIsImNpZGFhczpwYXNzd29yZF9yZWFkIiwiY2lkYWFzOmNvbnNlbnRfd3JpdGUiLCJjaWRhYXM6Y29uc2VudF9yZWFkIiwiY2lkYWFzOnB1YmxpY19wcm9maWxlIiwicGFzc2VzIiwiY2lkYWFzOnBhc3NfZGVsZXRlIiwiY2lkYWFzOmlkdmFsX3NldHRpbmdzX2RlbGV0ZSIsImNpZGFhczppZHZhbF9wZXJmb3JtIiwiY2lkYWFzOnBhc3NfcmVhZCIsImNpZGFhczpwYXNzX3dyaXRlIiwiY2lkYWFzOmlkdmFsX3NldHRpbmdzX3JlYWQiLCJjaWRhYXM6aWR2YWxfc2V0dGluZ3Nfd3JpdGUiLCJjaWRhYXM6aWR2YWxfY2FzZV9yZWFkIiwiY2lkYWFzOmlkdmFsX3VzZXJfY3R4X2NsZWFudXAiLCJjaWRhYXM6aWR2YWxfY2FzZV93cml0ZSIsImNpZGFhczppZHZhbF9tZWRpYV9yZWFkIiwiY2lkYWFzOmlkdmFsX2luaXQiLCJjaWRhYXM6c2Vzc2lvbl93cml0ZSIsImNpZGFhczp0b2tlbl9jcmVhdGUiLCJjaWRhYXM6c2Vzc2lvbl9kZWxldGUiLCJjaWRhYXM6Y29tbXVuaWNhdGlvbl9zZW5kIiwiY2lkYWFzOml2cl9zZW5kIiwiY2lkYWFzOnNtc19zZW5kIiwiY2lkYWFzOmVtYWlsX3NlbmQiLCJjaWRhYXM6cGFzc3dvcmRsZXNzX2NyZWF0ZSIsImNpZGFhczpzZXNzaW9uX3JlYWQiLCJjaWRhYXM6dXNlcmluZm8iLCJjaWRhYXM6aWRwcyIsImNpZGFhczpzaW5nbGVfZmFjdG9yX2F1dGhfZmFjZSIsImNpZGFhczp0ZW5hbnRfZG9jc19yZWFkIiwiY2lkYWFzOnRlbmFudF9kb2NzX2RlbGV0ZSIsImNpZGFhczp1c2VydXBkYXRlIiwiY2lkYWFzOmRlbGV0ZXVzZXIiLCJjaWRhYXM6dGVuYW50X2RvY3Nfd3JpdGUiLCJjaWRhYXM6ZGVsZXRlIiwiY2lkYWFzOmFwcF9kZXZlbG9wZXJzIiwiY2lkYWFzOmN1c3RvbV9zZWN1cml0eV9rZXlfcmVhZCIsImNpZGFhczpjdXN0b21fc2VjdXJpdHlfa2V5X2RlbGV0ZSIsImNpZGFhczpjdXN0b21fc2VjdXJpdHlfa2V5X3dyaXRlIiwiY2lkYWFzOmRldmljZXNfd3JpdGUiLCJjaWRhYXM6ZGV2aWNlc19yZWFkIiwiY2lkYWFzOndyaXRlIiwiY2lkYWFzOnJlYWQiLCJjaWRhYXM6cHVyZ2V1c2VyIiwiY2lkYWFzOnRlbmFudF9jb25zZW50X2RlbGV0ZSIsImNpZGFhczp2ZXJpZmljYXRpb25fZGVsZXRlIiwiY2lkYWFzOnJlcG9ydHNfd3JpdGUiLCJjaWRhYXM6cmVwb3J0c19yZWFkIiwiY2lkYWFzOnJlcG9ydHNfZGVsZXRlIiwiY2lkYWFzOnRlbmFudF9jb25zZW50X3dyaXRlIiwiY2lkYWFzOnRlbmFudF9jb25zZW50X3JlYWQiLCJjaWRhYXM6dmVyaWZpY2F0aW9uX3dyaXRlIiwiY2lkYWFzOnZlcmlmaWNhdGlvbl9yZWFkIiwiY2lkYWFzOmhvc3RlZF9wYWdlc19kZWxldGUiLCJjaWRhYXM6aG9zdGVkX3BhZ2VzX3dyaXRlIiwiY2lkYWFzOmdyb3Vwc191c2VyX21hcF9kZWxldGUiLCJjaWRhYXM6aG9zdGVkX3BhZ2VzX3JlYWQiLCJjaWRhYXM6Z3JvdXBzX3VzZXJfbWFwX3JlYWQiLCJjaWRhYXM6Z3JvdXBzX3VzZXJfbWFwX3dyaXRlIiwiY2lkYWFzOmdyb3Vwc19kZWxldGUiLCJjaWRhYXM6Z3JvdXBzX3JlYWQiLCJjaWRhYXM6Z3JvdXBzX3dyaXRlIiwiY2lkYWFzOmdyb3VwX3R5cGVfZGVsZXRlIiwiY2lkYWFzOmdyb3VwX3R5cGVfd3JpdGUiLCJjaWRhYXM6Z3JvdXBfdHlwZV9yZWFkIiwiY2lkYWFzOm9wdGluX2RlbGV0ZSIsImNpZGFhczpvcHRpbl93cml0ZSIsImNpZGFhczpvcHRpbl9yZWFkIiwiY2lkYWFzOmNhcHRjaGFfZGVsZXRlIiwiY2lkYWFzOmNhcHRjaGFfd3JpdGUiLCJjaWRhYXM6Y2FwdGNoYV9yZWFkIiwiY2lkYWFzOndlYmhvb2tfZGVsZXRlIiwiY2lkYWFzOndlYmhvb2tfd3JpdGUiLCJjaWRhYXM6d2ViaG9va19yZWFkIiwiY2lkYWFzOnBhc3N3b3JkX3BvbGljeV9kZWxldGUiLCJjaWRhYXM6cGFzc3dvcmRfcG9saWN5X3dyaXRlIiwiY2lkYWFzOnBhc3N3b3JkX3BvbGljeV9yZWFkIiwiY2lkYWFzOnRlbXBsYXRlc19kZWxldGUiLCJjaWRhYXM6dGVtcGxhdGVzX3dyaXRlIiwiY2lkYWFzOnRlbXBsYXRlc19yZWFkIiwiY2lkYWFzOnJlZ2lzdHJhdGlvbl9zZXR1cF9kZWxldGUiLCJjaWRhYXM6cmVnaXN0cmF0aW9uX3NldHVwX3dyaXRlIiwiY2lkYWFzOnJlZ2lzdHJhdGlvbl9zZXR1cF9yZWFkIiwiY2lkYWFzOnByb3ZpZGVyc19kZWxldGUiLCJjaWRhYXM6cHJvdmlkZXJzX3dyaXRlIiwiY2lkYWFzOnByb3ZpZGVyc19yZWFkIiwiY2lkYWFzOnJvbGVzX2RlbGV0ZSIsImNpZGFhczpyb2xlc193cml0ZSIsImNpZGFhczpyb2xlc19yZWFkIiwiY2lkYWFzOnVzZXJzX3NlYXJjaCIsImNpZGFhczp1c2Vyc19pbnZpdGUiLCJjaWRhYXM6dXNlcnNfZGVsZXRlIiwiY2lkYWFzOnVybF93cml0ZSJdLCJleHAiOjE2OTAzNTUwNjl9.YhL50IVuaZTxS2wV-R-TR5-vXiZjcDJ_SXFKMnPKtvwXh48q6SkehL1ygpCixvENicpkXRPmQ759Yev-JhxayTfytDE4EdyuJg5wNcwVb7x1lEbNsiWLP7RcthiUSTz5xPZtqJcxyw7KT46Aj0ByAYeSYFvoCnyCMa-Nx_to0OA"
    private var con = true
    private lateinit var binding: ActivityAddPatientBinding
    var activity: Activity = this@AddPatient
    var rv: RecyclerView? = null
    var adapter: ItemAdapter? = null
    var actionMode: ActionMode? = null


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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPatientBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this@AddPatient)

        val lm = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        adapter = ItemAdapter(this, this)
        rv = findViewById(R.id.recyclerViewTest)
        rv!!.adapter = adapter
        rv!!.setHasFixedSize(true)
        rv!!.layoutManager = lm
        val dividerItemDecoration = DividerItemDecoration(rv!!.context, lm.orientation)
        rv!!.addItemDecoration(dividerItemDecoration)
        val items: MutableList<ClipData.Item> = ArrayList()

        items.add(ClipData.Item("TEMPERATURE"))
        items.add(ClipData.Item("BLOOD_PRESSURE"))
        items.add(ClipData.Item("WEIGHT"))
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

        adapter!!.addAll(items)
        adapter!!.setActionModeReceiver(activity as ItemAdapter.OnClickAction)
        binding.recyclerViewTest.layoutManager = GridLayoutManager(context, 2)



        binding.appCompatTextView2.setOnClickListener {
            try {
                //  String JSON = "{\"LanguageLevels\":{\"1\":\"Pocz\\u0105tkuj\\u0105cy\",\"2\":\"\\u015arednioZaawansowany\",\"3\":\"Zaawansowany\",\"4\":\"Ekspert\"}}\n";
                val JSON =
                    "[{\"entry\":[{\"resource\":{\"identifier\":[{\"system\":\"Ezdx\",\"use\":\"usual\",\"value\":\"17588ae8-87e7-4847-84d5-9f56d8361a99\"}],\"" +
                            "resourceType\":\"Observation\",\"code\":{\"coding\":[{\"code\":\"8310-5\",\"display\":\"Body temperature\",\"system\":\"http://loinc.org\"}]},\"subject\":" +
                            "{\"identifier\":[{\"system\":\"Partner\",\"use\":\"usual\",\"value\":\"27912812780122\"}]},\"valueQuantity\":{\"code\":\"°F\",\"system\":\"http://unitsofmeasure.org\"," +
                            "\"unit\":\"°F\",\"value\":81.6}}}],\"resourceType\":\"Bundle\",\"type\":\"collection\"}]"

//             val unA = jsonObj.getJSONArray("valueQuantity")
//            for (i in 0 until unA.length()) {
//                val c = unA.getJSONObject(i)
//                val title = c.getString("code")
//                val body = c.getString("value")
//                Log.e("code",title)
//                Log.e("body",body)
//            }
                val reportData = ArrayList<String>()
                reportData.add(JSON)

                val test2 = JSON.substringAfterLast("unit\":\"°F")

                val value =test2.substring(9,15)

 //                val jsonString = "[{\"code\":\"°F\",\"system\":\"http://unitsofmeasure.org\",\"unit\":\"°F\",\"value\":81.6}}}],\"resourceType\":\"Bundle\",\"type\":\"collection\"}]"
//
//                val json = JSONObject(jsonString)
//                val code = json.getString("code")
//                val value = json.getString("value")
//                Log.e("code", code!!)
//                Log.e("value", value!!)



                Log.e("reportData", reportData.toString())
//                Log.e("test1", test1!!)
                Log.e("test2", test2!!.toString())
                Log.e("value", value!!.toString())
             } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        //apiCallFamilyListNew()

        if (PatientList.Diagnostic == "1") {
            binding.layoutRegister.visibility = View.GONE
            PatientList.Diagnostic = ""
            binding.btnMoveEzdx.visibility = View.VISIBLE
            binding.btnSave.visibility = View.GONE

            name = intent.getStringExtra("customer_name").toString()
            phoneNumber = intent.getStringExtra("phone_number").toString()
            email = intent.getStringExtra("email").toString()
            gender = intent.getStringExtra("gender").toString()


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
            // countryName = countryCode.substring(1)
            Log.e("countryName,", "$countryName")
        }

        binding.btnMoveEzdx.setOnClickListener {
            if (selectedDate.isEmpty()) {
                myToast(this@AddPatient, "Select Date Of Birth")
            } else if (selectedTestList.isEmpty()) {
                myToast(this@AddPatient, "Select Diagnostics Test")
            } else {
                selectedTestList.distinct()

                Log.e("dob", selectedDate.toString())
                Log.e("gender", gender.toString())
                Log.e("name", name.toString())
                Log.e("phoneNumber", phoneNumber.toString())

//            var patientDataInHl7String: String? =
//                "{\"address\":[{\"country\":\"india\",\"type\":\"physical\",\"use\":\"home\"}]," +
//                        "\"birthDate\":\"1986-04-19\",\"gender\":\"male\",\"identifier\":" +
//                        "[{\"system\":\"iprd\",\"use\":\"usual\",\"value\":\"27912812780122\"}],\"name\"" +
//                        ":[{\"given\":[\"Alauddin Ansari\"]}],\"resourceType\":\"Patient\",\"telecom\":[{\"rank\":\"1\"," +
//                        "\"system\":\"phone\",\"use\":\"mobile\",\"value\":\"7379452259\"}]}"

                var patientDataInHl7String: String? =
                    "{\"address\":[{\"country\":\"india\",\"type\":\"physical\",\"use\":\"home\"}]," +
                            "\"birthDate\":\"$selectedDate\",\"gender\":\"$gender\",\"identifier\":" +
                            "[{\"system\":\"iprd\",\"use\":\"usual\",\"value\":\"27912812780122\"}],\"name\"" +
                            ":[{\"given\":[\"$name\"]}],\"resourceType\":\"Patient\",\"telecom\":[{\"rank\":\"1\"," +
                            "\"system\":\"phone\",\"use\":\"mobile\",\"value\":\"$phoneNumber\"}]}"
                try {
                    val test1 = ItemAdapter.Companion.selectedTestList.find { it == "TEMPERATURE" }
                    val test2 =
                        ItemAdapter.Companion.selectedTestList.find { it == "BLOOD_PRESSURE" }
                    val test3 = ItemAdapter.Companion.selectedTestList.find { it == "WEIGHT" }

                    hightlightTests = "[\"$test1\",\"$test2\",\"$test3\"]"
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
        }



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
            // apiCallRegisterHealthCube()
        }
        binding.imgBack.setOnClickListener {
            onBackPressed()
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
                selectedDate =
                    SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(newDate.time)

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
            gender = "male"
            binding.tvMale.setTextColor(Color.parseColor("#9F367A"))
            binding.tvFemale.setTextColor(Color.parseColor("#A19398"))
            binding.tvOther.setTextColor(Color.parseColor("#A19398"))
        }

        binding.layWomen.setOnClickListener {
            gender = "female"
            binding.tvFemale.setTextColor(Color.parseColor("#9F367A"))
            binding.tvOther.setTextColor(Color.parseColor("#A19398"))
            binding.tvMale.setTextColor(Color.parseColor("#A19398"))
        }
        binding.layOther.setOnClickListener {
            gender = "other"
            binding.tvOther.setTextColor(Color.parseColor("#9F367A"))
            binding.tvFemale.setTextColor(Color.parseColor("#A19398"))
            binding.tvMale.setTextColor(Color.parseColor("#A19398"))
        }


    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        Toast.makeText(this, "OnNewIntent called", Toast.LENGTH_SHORT).show()

        Log.e("Result", intent.getStringExtra(ezdxPackageName + "RESULT").toString())

        try {


            val jsonStr: String? = intent.getStringExtra(ezdxPackageName + "RESULT")
            val jsonObj = JSONObject(jsonStr)


            //  String JSON = "{\"LanguageLevels\":{\"1\":\"Pocz\\u0105tkuj\\u0105cy\",\"2\":\"\\u015arednioZaawansowany\",\"3\":\"Zaawansowany\",\"4\":\"Ekspert\"}}\n";
            val JSON =
                "[{\"entry\":[{\"resource\":{\"identifier\":[{\"system\":\"Ezdx\",\"use\":\"usual\",\"value\":\"17588ae8-87e7-4847-84d5-9f56d8361a99\"}],\"" +
                        "resourceType\":\"Observation\",\"code\":{\"coding\":[{\"code\":\"8310-5\",\"display\":\"Body temperature\",\"system\":\"http://loinc.org\"}]},\"subject\":" +
                        "{\"identifier\":[{\"system\":\"Partner\",\"use\":\"usual\",\"value\":\"27912812780122\"}]},\"valueQuantity\":{\"code\":\"°F\",\"system\":\"http://unitsofmeasure.org\"," +
                        "\"unit\":\"°F\",\"value\":81.6}}}],\"resourceType\":\"Bundle\",\"type\":\"collection\"}]"

//             val unA = jsonObj.getJSONArray("valueQuantity")
//            for (i in 0 until unA.length()) {
//                val c = unA.getJSONObject(i)
//                val title = c.getString("code")
//                val body = c.getString("value")
//                Log.e("code",title)
//                Log.e("body",body)
//            }

            val reportData = ArrayList<String>()
            reportData.add(intent.getStringExtra(ezdxPackageName + "RESULT").toString())
            val jsonString = intent.getStringExtra(ezdxPackageName + "RESULT")
            val test1 = reportData.find { it == "valueQuantity" }

            //val json = JSONObject(jsonString)


            Log.e("unit", unit)
            Log.e("value", value.toString())
            Log.e("reportData", reportData.toString())
        } catch (e: Exception) {
            e.printStackTrace()
        }
        // ** In case of ECG test, rendering entire byte array data of PDF report in TextView will go out of memory
        // this needs to be handled by dumping the byte array in a file
        binding.tvResult.visibility = View.VISIBLE
        binding.tvResult!!.text =
            intent.getStringExtra(ezdxPackageName + "RESULT") // result from Ezdx
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

    override fun onClickAction() {
        val selected = adapter!!.selected.size
        if (actionMode == null) {
            //  actionMode = startActionMode(actionModeCallback)
            //  actionMode!!.title = "Selected: $selected"
        } else {
            if (selected == 0) {
                actionMode!!.finish()
            } else {
                //  actionMode!!.title = "Selected: $selected"
            }
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

        name = binding.edtName.text.toString()
        city = binding.edtCity.text.toString()
        state = binding.edtState.text.toString()
        postelCode = binding.edtPostelCode.text.toString()
        email = binding.edtEmail.text.toString()
        phoneNumber = binding.edtMobile.text.toString()
        val code = "+91 "
        phoneNumberNew = code + phoneNumber

        ApiClient.apiService.patientHealthcubeReg(
            name,
            gender,
            city,
            state,
            selectedDate,
            countryName,
            phoneNumberNew,
            "+91 9392595905",
            "24643-9246-${sessionManager.id}",
            testList,
            postelCode,
            email,
            sessionManager.id.toString()
        )
            .enqueue(object :
                Callback<ModelHealthCubeReg> {
                override fun onResponse(
                    call: Call<ModelHealthCubeReg>,
                    response: Response<ModelHealthCubeReg>
                ) {
                    if (response.body()!!.status == 1) {
                        progressDialog!!.dismiss()
                        binding.btnMoveEzdx.visibility = View.VISIBLE
                        binding.btnSave.visibility = View.GONE
                        binding.layoutRegister.visibility = View.GONE

                        //myToast(requireActivity(), response.body()!!.message)
                    } else {
                        myToast(this@AddPatient, response.body()!!.message)
                        progressDialog!!.dismiss()

                    }
                }

                override fun onFailure(call: Call<ModelHealthCubeReg>, t: Throwable) {
                    progressDialog!!.dismiss()
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


    private fun apiCallRegisterHealthCube() {

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
        Log.e("selectedDate", selectedDate)
        Log.e("gender", gender)
        Log.e("email", email)
        Log.e("city", city)
        Log.e("state", state)
        Log.e("postelCode", postelCode)
        Log.e("countryName", countryName)
        Log.e("phoneNumberNew", phoneNumberNew)
        Log.e("id", "24643-9246-${sessionManager.id}")
        ApiClientHelthCube.apiService.registrationHealthCube(
            accessToken, DataModal(
                "$name", "$selectedDate", "$gender",
                "$email", "$city", "$state", "$postelCode", "$countryName",
                "$phoneNumberNew", "+91 9392595905", "24643-9246-${sessionManager.id}"
            )
        )
            .enqueue(object :
                Callback<ModelRegister> {
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
                            myToast(this@AddPatient, "Unauthorized")
                            progressDialog!!.dismiss()
                        } else if (response.code() == 200) {
                            myToast(this@AddPatient, response.body()!!.message)
                            refresh()
                            progressDialog!!.dismiss()
                        } else {
                            Toast.makeText(
                                this@AddPatient,
                                "${response.body()!!.message}",
                                Toast.LENGTH_SHORT
                            ).show()

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

    @SuppressLint("SuspiciousIndentation")
    private fun apiCallFamilyListNew() {

        // this creates a vertical layout Manager

        // ArrayList of class ItemsViewModel
        val data = ArrayList<ItemsViewModel>()

        // This loop will create 20 Views containing
        // the image with the count of view

        data.add(ItemsViewModel("TEMPERATURE"))
        data.add(ItemsViewModel("BLOOD_PRESSURE"))
        data.add(ItemsViewModel("WEIGHT"))
        data.add(ItemsViewModel("HEIGHT"))
        data.add(ItemsViewModel("BMI"))
        data.add(ItemsViewModel("ECG"))
        data.add(ItemsViewModel("PULSE_OXIMETER"))
        data.add(ItemsViewModel("BLOOD_GLUCOSE"))
        data.add(ItemsViewModel("BLOOD_GROUPING"))
        data.add(ItemsViewModel("URINE"))
        data.add(ItemsViewModel("HEMOGLOBIN"))
        data.add(ItemsViewModel("CHOLESTEROL"))
        data.add(ItemsViewModel("URIC_ACID"))
        data.add(ItemsViewModel("DENGUE"))
        data.add(ItemsViewModel("HIV"))
        data.add(ItemsViewModel("MALARIA"))
        data.add(ItemsViewModel("PREGNANCY"))


        // This will pass the ArrayList to our Adapter
        //  val adapter = CustomAdapter(data)

        // Setting the Adapter with the recyclerview
        //  binding.recyclerViewTest.adapter = adapter
        //  binding.recyclerViewTest.layoutManager = GridLayoutManager(context, 2)

    }

    override fun selctedTestList(item: String) {
        selectedTestList.add(item)
        selectedTestList.distinct()
        Log.e("Item", item)
        Log.e("selectedTestList", selectedTestList.toString())

    }
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

