package com.example.ehcf_doctor.HealthCube.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.ehcf.Helper.myToast
import com.example.ehcf.sharedpreferences.SessionManager
import com.example.ehcf_doctor.HealthCube.Model.DataModal
import com.example.ehcf_doctor.HealthCube.Model.ModelRegister
import com.example.ehcf_doctor.databinding.ActivityAddPatientBinding
import com.example.myrecyview.apiclient.ApiClientHelthCube
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import kotlin.collections.ArrayList


class AddPatient : AppCompatActivity() {
    private val context: Context = this@AddPatient
    private lateinit var sessionManager: SessionManager
    var progressDialog: ProgressDialog? = null
    var gender = ""
    var reportList = ArrayList<String>()
    var mydilaog: Dialog? = null
    private var countryName = ""
    private var selectedDate = ""
    private var countryCode = ""
    private var accessToken = "eyJhbGciOiJSUzI1NiIsImtpZCI6ImYwNmNhY" +
            "jZhLTBmZGItNGVmMi1iMDMwLTZlZDY1Njg2ODVkZCJ9.eyJ1YV9oYXNoIjoiYWRlNTBhZmE2YTNjY" +
            "2M3OWVmYWZjMjllNzYwN2FiZGMiLCJzaWQiOiJjN2ZkMjU1Yi1mNzhlLTQ0YzMtYWY0My01Y2ExY" +
            "zI5OTYzNTAiLCJzdWIiOiJBTk9OWU1PVVMiLCJhdWQiOiJiOTYyMTRhMC0yZDA1LTRlNjUt" +
            "YmU5Mi1iODM4NTIyMjc3MzMiLCJpYXQiOjE2ODk1OTA4NTgsImF1dGhfdGltZSI6MTY" +
            "4OTU5MDg1OCwiaXNzIjoiaHR0cHM6Ly9kZW1vLWV6ZHgtZnJlZS5jaWRhYXMuZGUiLCJ" +
            "qdGkiOiJiMjk1NGFkYy05MjAxLTQyZjgtYWM2Ny1jY2U2M2I2NTIwMDYiLCJzY29wZXMiOlsi" +
            "Y2lkYWFzOmZkc19zZXR0aW5nc193cml0ZSIsImNpZGFhczpmZHNfc2V0dGluZ3NfcmVhZCIsImNp" +
            "ZGFhczpzdHJpcGVfYWNjb3VudF9jcmVhdGUiLCJjaWRhYXM6Y2hlY2tvdXRfd3JpdGUiLCJjaWRhYXM" +
            "6dHJhbnNhY3Rpb25fbG9nX3dyaXRlIiwiY2lkYWFzOnRyYW5zYWN0aW9uX2xvZ19yZWFkIiwiY2lkYWFz" +
            "OmZpZWxkX3NldHVwX3JlYWQiLCJjaWRhYXM6cGF5bWVudGNvbmZpZ19yZWFkIiwiY2lkYWFzOnBhe" +
            "W1lbnRjb25maWdfZGVsZXRlIiwiY2lkYWFzOnBheW1lbnRjb25maWdfd3JpdGUiLCJjaWRhYXM6bm" +
            "V3c2xldHRlcl9kZWxldGUiLCJjaWRhYXM6bmV3c2xldHRlcl93cml0ZSIsImNpZGFhczp1c2VyY3JlY" +
            "XRlIiwiY2lkYWFzOndlYmhvb2siLCJjaWRhYXM6bG9naW4iLCJjaWRhYXM6cmVtb3ZlX3Nlc3Npb24i" +
            "LCJvcGVuaWQiLCJwcm9maWxlIiwiZW1haWwiLCJhZGRyZXNzIiwicGhvbmUiLCJncm91cHMiLCJyb" +
            "2xlcyIsIm9mZmxpbmVfYWNjZXNzIiwiY2lkYWFzOnJlZ2lzdGVyIiwiaWRlbnRpdGllcyIsImNpZGF" +
            "hczppbnZpdGUiLCJjaWRhYXM6YWRtaW5fcmVhZCIsImNpZGFhczphcHBzX3JlYWQiLCJjaWRhYXM" +
            "6YWRtaW5fZGVsZXRlIiwiY2lkYWFzOmFkbWluX3dyaXRlIiwiY2lkYWFzOnNjb3Blc19kZWxldG" +
            "UiLCJjaWRhYXM6c2NvcGVzX3dyaXRlIiwiY2lkYWFzOnNjb3Blc19yZWFkIiwiY2lkYWFzOmFwc" +
            "HNfZGVsZXRlIiwiY2lkYWFzOmFwcHNfd3JpdGUiLCJjaWRhYXM6dXNlcnNfd3JpdGUiLCJjaWRh" +
            "YXM6dXNlcnNfcmVhZCIsImNpZGFhczpzZWN1cml0eV9rZXlfZGVsZXRlIiwiY2lkYWFzOnNlY3V" +
            "yaXR5X2tleV93cml0ZSIsImNpZGFhczpzZWN1cml0eV9rZXlfcmVhZCIsImNpZGFhczpwYXNzd2" +
            "9yZF9yZWFkIiwiY2lkYWFzOmNvbnNlbnRfd3JpdGUiLCJjaWRhYXM6Y29uc2VudF9yZWFkIiwiY2lkYWFzOnB1YmxpY19wcm9maWxlIiwicGFzc2VzIiwiY2lkYWFzOnBhc3NfZGVsZXRlIiwiY2lkYWFzOmlkdmFsX3NldHRpbmdzX2RlbGV0ZSIsImNpZGFhczppZHZhbF9wZXJmb3JtIiwiY2lkYWFzOnBhc3NfcmVhZCIsImNpZGFhczpwYXNzX3dyaXRlIiwiY2lkYWFzOmlkdmFsX3NldHRpbmdzX3JlYWQiLCJjaWRhYXM6aWR2YWxfc2V0dGluZ3Nfd3JpdGUiLCJjaWRhYXM6aWR2YWxfY2FzZV9yZWFkIiwiY2lkYWFzOmlkdmFsX3VzZXJfY3R4X2NsZWFudXAiLCJjaWRhYXM6aWR2YWxfY2FzZV93cml0ZSIsImNpZGFhczppZHZhbF9tZWRpYV9yZWFkIiwiY2lkYWFzOmlkdmFsX2luaXQiLCJjaWRhYXM6c2Vzc2lvbl93cml0ZSIsImNpZGFhczp0b2tlbl9jcmVhdGUiLCJjaWRhYXM6c2Vzc2lvbl9kZWxldGUiLCJjaWRhYXM6Y29tbXVuaWNhdGlvbl9zZW5kIiwiY2lkYWFzOml2cl9zZW5kIiwiY2lkYWFzOnNtc19zZW5kIiwiY2lkYWFzOmVtYWlsX3NlbmQiLCJjaWRhYXM6cGFzc3dvcmRsZXNzX2NyZWF0ZSIsImNpZGFhczpzZXNzaW9uX3JlYWQiLCJjaWRhYXM6dXNlcmluZm8iLCJjaWRhYXM6aWRwcyIsImNpZGFhczpzaW5nbGVfZmFjdG9yX2F1dGhfZmFjZSIsImNpZGFhczp0ZW5hbnRfZG9jc19yZWFkIiwiY2lkYWFzOnRlbmFudF9kb2NzX2RlbGV0ZSIsImNpZGFhczp1c2VydXBkYXRlIiwiY2lkYWFzOmRlbGV0ZXVzZXIiLCJjaWRhYXM6dGVuYW50X2RvY3Nfd3JpdGUiLCJjaWRhYXM6ZGVsZXRlIiwiY2lkYWFzOmFwcF9kZXZlbG9wZXJzIiwiY2lkYWFzOmN1c3RvbV9zZWN1cml0eV9rZXlfcmVhZCIsImNpZGFhczpjdXN0b21fc2VjdXJpdHlfa2V5X2RlbGV0ZSIsImNpZGFhczpjdXN0b21fc2VjdXJpdHlfa2V5X3dyaXRlIiwiY2lkYWFzOmRldmljZXNfd3JpdGUiLCJjaWRhYXM6ZGV2aWNlc19yZWFkIiwiY2lkYWFzOndyaXRlIiwiY2lkYWFzOnJlYWQiLCJjaWRhYXM6cHVyZ2V1c2VyIiwiY2lkYWFzOnRlbmFudF9jb25zZW50X2RlbGV0ZSIsImNpZGFhczp2ZXJpZmljYXRpb25fZGVsZXRlIiwiY2lkYWFzOnJlcG9ydHNfd3JpdGUiLCJjaWRhYXM6cmVwb3J0c19yZWFkIiwiY2lkYWFzOnJlcG9ydHNfZGVsZXRlIiwiY2lkYWFzOnRlbmFudF9jb25zZW50X3dyaXRlIiwiY2lkYWFzOnRlbmFudF9jb25zZW50X3JlYWQiLCJjaWRhYXM6dmVyaWZpY2F0aW9uX3dyaXRlIiwiY2lkYWFzOnZlcmlmaWNhdGlvbl9yZWFkIiwiY2lkYWFzOmhvc3RlZF9wYWdlc19kZWxldGUiLCJjaWRhYXM6aG9zdGVkX3BhZ2VzX3dyaXRlIiwiY2lkYWFzOmdyb3Vwc191c2VyX21hcF9kZWxldGUiLCJjaWRhYXM6aG9zdGVkX3BhZ2VzX3JlYWQiLCJjaWRhYXM6Z3JvdXBzX3VzZXJfbWFwX3JlYWQiLCJjaWRhYXM6Z3JvdXBzX3VzZXJfbWFwX3dyaXRlIiwiY2lkYWFzOmdyb3Vwc19kZWxldGUiLCJjaWRhYXM6Z3JvdXBzX3JlYWQiLCJjaWRhYXM6Z3JvdXBzX3dyaXRlIiwiY2lkYWFzOmdyb3VwX3R5cGVfZGVsZXRlIiwiY2lkYWFzOmdyb3VwX3R5cGVfd3JpdGUiLCJjaWRhYXM6Z3JvdXBfdHlwZV9yZWFkIiwiY2lkYWFzOm9wdGluX2RlbGV0ZSIsImNpZGFhczpvcHRpbl93cml0ZSIsImNpZGFhczpvcHRpbl9yZWFkIiwiY2lkYWFzOmNhcHRjaGFfZGVsZXRlIiwiY2lkYWFzOmNhcHRjaGFfd3JpdGUiLCJjaWRhYXM6Y2FwdGNoYV9yZWFkIiwiY2lkYWFzOndlYmhvb2tfZGVsZXRlIiwiY2lkYWFzOndlYmhvb2tfd3JpdGUiLCJjaWRhYXM6d2ViaG9va19yZWFkIiwiY2lkYWFzOnBhc3N3b3JkX3BvbGljeV9kZWxldGUiLCJjaWRhYXM6cGFzc3dvcmRfcG9saWN5X3dyaXRlIiwiY2lkYWFzOnBhc3N3b3JkX3BvbGljeV9yZWFkIiwiY2lkYWFzOnRlbXBsYXRlc19kZWxldGUiLCJjaWRhYXM6dGVtcGxhdGVzX3dyaXRlIiwiY2lkYWFzOnRlbXBsYXRlc19yZWFkIiwiY2lkYWFzOnJlZ2lzdHJhdGlvbl9zZXR1cF9kZWxldGUiLCJjaWRhYXM6cmVnaXN0cmF0aW9uX3NldHVwX3dyaXRlIiwiY2lkYWFzOnJlZ2lzdHJhdGlvbl9zZXR1cF9yZWFkIiwiY2lkYWFzOnByb3ZpZGVyc19kZWxldGUiLCJjaWRhYXM6cHJvdmlkZXJzX3dyaXRlIiwiY2lkYWFzOnByb3ZpZGVyc19yZWFkIiwiY2lkYWFzOnJvbGVzX2RlbGV0ZSIsImNpZGFhczpyb2xlc193cml0ZSIsImNpZGFhczpyb2xlc19yZWFkIiwiY2lkYWFzOnVzZXJzX3NlYXJjaCIsImNpZGFhczp1c2Vyc19pbnZpdGUiLCJjaWRhYXM6dXNlcnNfZGVsZXRlIiwiY2lkYWFzOnVybF93cml0ZSJdLCJleHAiOjE2ODk2NzcyNTh9.2BDmlxbNXtzo_sUrdDuFGfyNxpSyC4VluzXA40DRjYSj3GxSa9Fb1UO-LA3Ea8mmTNfQ2FFCFqNqJ8kW0R4XXDxWREHf_Kqkcfvr6_XXbsxeQYzjZNaEr4CfpYof3pQhrVMLIBmbU1XW9PYbwmhxua6q1K_d_aNWofWcw3w97cM"
    private var con = true
    private lateinit var binding: ActivityAddPatientBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPatientBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this@AddPatient)

        binding.spinnerCountryCode.setOnCountryChangeListener {
            countryName = binding.spinnerCountryCode.selectedCountryName
            // countryName = countryCode.substring(1)
            Log.e("countryName,", "$countryName")
        }


        binding.imgBack.setOnClickListener {
            onBackPressed()
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
            apiCallRegister()
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
                     selectedDate = SimpleDateFormat("ddMMyyyy", Locale.getDefault()).format(newDate.time)

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


    private fun apiCallRegister() {

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

//        Log.e("NAme", name)
//        Log.e("selectedDate", selectedDate)
//        Log.e("gender", gender)
//        Log.e("email", email)
//        Log.e("city", city)
//        Log.e("state", state)
//        Log.e("postelCode", postelCode)
//        Log.e("countryName", countryName)
//        Log.e("phoneNumberNew", phoneNumberNew)
//        Log.e("id", "24643-9246-${sessionManager.id}")
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
                        } else if (response.code() == 200) {
                            myToast(this@AddPatient, response.body()!!.message)
                            refresh()
                            progressDialog!!.dismiss()
                        } else {
                            myToast(this@AddPatient, response.body()!!.message)
                            progressDialog!!.dismiss()
                        }
                    } catch (e: Exception) {
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

}