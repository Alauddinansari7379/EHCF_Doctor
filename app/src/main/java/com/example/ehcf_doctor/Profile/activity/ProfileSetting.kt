package com.example.ehcf_doctor.Profile.activity

import android.R
import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.postDelayed
import com.example.ehcf.Helper.myToast
import com.example.ehcf.sharedpreferences.SessionManager
import com.example.ehcf_doctor.AutoComplete.AutoSuggestAdapter
import com.example.ehcf_doctor.Profile.modelResponse.ModelCity
import com.example.ehcf_doctor.Profile.modelResponse.ModelCityList
import com.example.ehcf_doctor.Profile.modelResponse.ModelState
import com.example.ehcf_doctor.Profile.modelResponse.ModelStateList
import com.example.ehcf_doctor.Registration.modelResponse.*
import com.example.ehcf_doctor.databinding.ActivityProfileSettingBinding
import com.example.myrecyview.apiclient.ApiClient
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import rezwan.pstu.cse12.youtubeonlinestatus.recievers.NetworkChangeReceiver
import xyz.teamgravity.checkinternet.CheckInternet
import java.io.IOException


class ProfileSetting : AppCompatActivity() {
    private val context: Context = this@ProfileSetting
    private lateinit var sessionManager: SessionManager
    var progressDialog: ProgressDialog? = null
    private var specilList = ModelSpecilList();
    private var languageList = ModelLanguage();
    var genderList = ArrayList<ModelGender>()
    var stateList = ModelState()
    var cityList = ModelCity()
    var specilistId = ""
    var genderId = ""
    var description = ""
    var experience = ""
    var clinicName = ""
    var clinicAddress = ""
    var clinicAddress1 = ""
    var clinicAddress2 = ""
    var address = ""
    var street = ""
    var city = ""
    var state = ""
    private var pricing = ""
    var countryName = "India"
    var services = ""
    var postalCode = ""

    private lateinit var binding: ActivityProfileSettingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileSettingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sessionManager = SessionManager(this)



        genderList.add(ModelGender("Select Gender", 0))
        genderList.add(ModelGender("Male", 1))
        genderList.add(ModelGender("Female", 2))
        genderList.add(ModelGender("Other", 3))

        binding.spinnerGender.adapter =
            ArrayAdapter<ModelGender>(context, R.layout.simple_list_item_1, genderList)
        binding.spinnerGender.setSelection(sessionManager.gender.toString().toInt())


        Log.e("cityList", cityList.toString())
        binding.edtExperience.setText(sessionManager.experience.toString())
        if (sessionManager.pricing!!.isNotEmpty()) {
            binding.edtExperience.setText(sessionManager.experience.toString())
            binding.edtClinicName.setText(sessionManager.clinicName.toString())
            binding.edtClinicAddress.setText(sessionManager.clinicAddress.toString())
            binding.edtClinicAddress1.setText(sessionManager.clinicAddressOne.toString())
            binding.edtClinicAddress2.setText(sessionManager.clinicAddressTwo.toString())
            binding.edtAddress.setText(sessionManager.address.toString())
            binding.edtPricing.setText(sessionManager.pricing.toString())
            binding.edtServices.setText(sessionManager.services.toString())
            binding.edtPostalCode.setText(sessionManager.postalCode.toString())

        }



        binding.imgBack.setOnClickListener {
            onBackPressed()
        }


        Handler(Looper.getMainLooper()).postDelayed(300) {
            apiCallStateSpinner()
            apiCallCitySpinner()
            apiCallSpecialistSpinner()
        }

        binding.spinnerCountryName.setOnCountryChangeListener {
            countryName = binding.spinnerCountryName.selectedCountryEnglishName

            Log.e("Log", "countryName-$countryName")

        }
        binding.spinnerGender.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View, i: Int, l: Long) {
                if (genderList.size > 0) {
                    genderId = genderList[i].id.toString()

                    Log.e(ContentValues.TAG, "gender: $genderId")
                }
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {}
        }



        binding.btnNext.setOnClickListener {
            if (binding.edtExperience.text.isEmpty()) {
                binding.edtExperience.error = "Enter Experience"
                binding.edtExperience.requestFocus()
                return@setOnClickListener
            }
            if (binding.edtClinicName.text!!.isEmpty()) {
                binding.edtClinicName.error = "Enter ClinicName"
                binding.edtClinicName.requestFocus()
                return@setOnClickListener
            }

            if (binding.edtAddress.text.isEmpty()) {
                binding.edtAddress.error = "Enter Address"
                binding.edtAddress.requestFocus()
                return@setOnClickListener
            }

            if (binding.edtStreet.text!!.isEmpty()) {
                binding.edtStreet.error = "Enter Street"
                binding.edtStreet.requestFocus()
                return@setOnClickListener
            }
            if (binding.spinnerState.selectedItem.toString() == "Select Your State") {
                myToast(this@ProfileSetting, "Please Select Your State!")
                binding.spinnerState.requestFocus()
                return@setOnClickListener
            }

            if (binding.spinnerCity.selectedItem.toString() == "Select Your City") {
                myToast(this@ProfileSetting, "Please Select Your City!")
                binding.spinnerCity.requestFocus()
                return@setOnClickListener
            }
            if (binding.edtPostalCode.text.isEmpty()) {
                binding.edtPostalCode.error = "Enter Postal Code"
                binding.edtPostalCode.requestFocus()
                return@setOnClickListener
            }
            if (binding.edtPricing.text.isEmpty()) {
                binding.edtPricing.error = "Enter Pricing"
                binding.edtPricing.requestFocus()
                return@setOnClickListener
            }
            if (binding.edtServices.text.isEmpty()) {
                binding.edtServices.error = "Enter Services"
                binding.edtServices.requestFocus()
                return@setOnClickListener
            } else {
                specilistId
                experience = binding.edtExperience.text.toString().trim()
                genderId
                clinicName = binding.edtClinicName.text.toString().trim()
                clinicAddress = binding.edtClinicAddress.text.toString().trim()
                clinicAddress1 = binding.edtClinicAddress1.text.toString().trim()
                clinicAddress2 = binding.edtClinicAddress2.text.toString().trim()
                address = binding.edtAddress.text.toString().trim()
                street = binding.edtStreet.text.toString().trim()
                pricing = binding.edtPricing.text.toString().trim()
                services = binding.edtServices.text.toString().trim()
                services = binding.edtServices.text.toString().trim()
                postalCode = binding.edtPostalCode.text.toString().trim()

                val intent = Intent(context as Activity, Education::class.java)
                intent.putExtra("specilistId", specilistId).toString()
                intent.putExtra("experience", experience).toString()
                intent.putExtra("genderId", genderId).toString()
                intent.putExtra("clinicName", clinicName).toString()
                intent.putExtra("clinicAddress", clinicAddress).toString()
                intent.putExtra("clinicAddress1", clinicAddress1).toString()
                intent.putExtra("clinicAddress2", clinicAddress2).toString()
                intent.putExtra("address", address).toString()
                intent.putExtra("street", street).toString()
                intent.putExtra("city", city).toString()
                intent.putExtra("country", countryName).toString()
                intent.putExtra("state", state).toString()
                intent.putExtra("pricing", pricing).toString()
                intent.putExtra("services", services).toString()
                intent.putExtra("postalCode", postalCode).toString()
                context.startActivity(intent)
            }
        }

    }

    private fun readCityNameJSON(context: Context): java.util.ArrayList<ModelCityList> {

        lateinit var jsonString: String
        try {
            jsonString = context.assets.open("city_name.json")
                .bufferedReader()
                .use {
                    it.readText()
                }
        } catch (ioException: IOException) {
        }

        val listCountryType = object : TypeToken<java.util.ArrayList<ModelCityList>>() {}.type
        return Gson().fromJson(jsonString, listCountryType)

        Log.e("listCountryType", listCountryType.toString())
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

        Log.e("listCountryType", listCountryType.toString())
    }

    private fun apiCallSpecialistSpinner() {
        progressDialog = ProgressDialog(this@ProfileSetting)
        progressDialog!!.setMessage("Loading..")
        progressDialog!!.setTitle("Please Wait")
        progressDialog!!.isIndeterminate = false
        progressDialog!!.setCancelable(true)

        progressDialog!!.show()

        ApiClient.apiService.specialistCategoryTest()
            .enqueue(object : Callback<ModelSpecilList> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelSpecilList>, response: Response<ModelSpecilList>
                ) {
                    try {

                        specilList = response.body()!!;
                        if (specilList != null) {

                            //spinner code start
                            val items = arrayOfNulls<String>(specilList.result!!.size)

                            for (i in specilList.result!!.indices) {
                                items[i] = specilList.result!![i].categoryName
                            }
                            val adapter: ArrayAdapter<String?> =
                                ArrayAdapter(
                                    this@ProfileSetting,
                                    R.layout.simple_list_item_1,
                                    items
                                )
                            binding.spinnerSpecialist.adapter = adapter
                            progressDialog!!.dismiss()

                            binding.spinnerSpecialist.setSelection(
                                sessionManager.specialist.toString().toInt() - 1
                            )


                            binding.spinnerSpecialist.onItemSelectedListener =
                                object : AdapterView.OnItemSelectedListener {
                                    override fun onItemSelected(
                                        adapterView: AdapterView<*>?,
                                        view: View,
                                        i: Int,
                                        l: Long
                                    ) {
                                        val id = specilList.result!![i].id
                                        specilistId = id.toString()
                                        //   Toast.makeText(this@RegirstrationTest, "" + id, Toast.LENGTH_SHORT).show()
                                    }

                                    override fun onNothingSelected(adapterView: AdapterView<*>?) {}
                                }

                        }
                    } catch (e: Exception) {
                        myToast(this@ProfileSetting, "Something went wrong")
                        progressDialog!!.dismiss()
                    }
                }

                override fun onFailure(call: Call<ModelSpecilList>, t: Throwable) {
                    myToast(this@ProfileSetting, "Something went wrong")
                    progressDialog!!.dismiss()

                }

            })
    }

    private fun apiCallStateSpinner() {
        progressDialog = ProgressDialog(this@ProfileSetting)
        progressDialog!!.setMessage("Loading..")
        progressDialog!!.setTitle("Please Wait")
        progressDialog!!.isIndeterminate = false
        progressDialog!!.setCancelable(true)

        // progressDialog!!.show()

        ApiClient.apiService.getState()
            .enqueue(object : Callback<ModelState> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelState>, response: Response<ModelState>
                ) {
                    try {

                        stateList = response.body()!!;
                        if (specilList != null) {

                            //spinner code start
                            val items = arrayOfNulls<String>(stateList.result!!.size)

                            for (i in stateList.result!!.indices) {
                                items[i] = stateList.result!![i].name
                            }
                            val adapter: ArrayAdapter<String?> =
                                ArrayAdapter(
                                    this@ProfileSetting,
                                    R.layout.simple_list_item_1,
                                    items
                                )
                            binding.spinnerState.adapter = adapter

                            binding.spinnerState.setSelection(items.indexOf(sessionManager.state.toString()));
                            Log.e("sdsdsd", sessionManager.state.toString())

                            progressDialog!!.dismiss()


                            binding.spinnerState.onItemSelectedListener =
                                object : AdapterView.OnItemSelectedListener {
                                    override fun onItemSelected(
                                        adapterView: AdapterView<*>?,
                                        view: View,
                                        i: Int,
                                        l: Long
                                    ) {
                                        val id = stateList.result!![i].name
                                        state = id.toString()
                                        //   Toast.makeText(this@RegirstrationTest, "" + id, Toast.LENGTH_SHORT).show()
                                    }

                                    override fun onNothingSelected(adapterView: AdapterView<*>?) {}
                                }

                        }
                    } catch (e: Exception) {
                        myToast(this@ProfileSetting, "Something went wrong")
                        progressDialog!!.dismiss()
                    }
                }

                override fun onFailure(call: Call<ModelState>, t: Throwable) {
                    myToast(this@ProfileSetting, "Something went wrong")
                    progressDialog!!.dismiss()

                }

            })
    }

    private fun apiCallCitySpinner() {
        progressDialog = ProgressDialog(this@ProfileSetting)
        progressDialog!!.setMessage("Loading..")
        progressDialog!!.setTitle("Please Wait")
        progressDialog!!.isIndeterminate = false
        progressDialog!!.setCancelable(true)

        // progressDialog!!.show()

        ApiClient.apiService.getCity()
            .enqueue(object : Callback<ModelCity> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelCity>, response: Response<ModelCity>
                ) {


                    try {
                        cityList = response.body()!!;
                        if (cityList != null) {

                            //spinner code start
                            val items = arrayOfNulls<String>(cityList.result!!.size)

                            for (i in cityList.result!!.indices) {
                                items[i] = cityList.result!![i].city
                            }
                            val adapter: ArrayAdapter<String?> =
                                ArrayAdapter(
                                    this@ProfileSetting,
                                    R.layout.simple_list_item_1,
                                    items
                                )
                            binding.spinnerCity.adapter = adapter
                            binding.spinnerCity.setSelection(items.indexOf(sessionManager.city.toString()));

                            progressDialog!!.dismiss()



                            binding.spinnerCity.onItemSelectedListener =
                                object : AdapterView.OnItemSelectedListener {
                                    override fun onItemSelected(
                                        adapterView: AdapterView<*>?,
                                        view: View,
                                        i: Int,
                                        l: Long
                                    ) {
                                        val id = cityList.result!![i].city
                                        city = id.toString()
                                        //   Toast.makeText(this@RegirstrationTest, "" + id, Toast.LENGTH_SHORT).show()
                                    }

                                    override fun onNothingSelected(adapterView: AdapterView<*>?) {}
                                }

                        }
                    }catch (e:Exception){
                        myToast(this@ProfileSetting, "Something went wrong")
                        progressDialog!!.dismiss()
                    }
                }

                override fun onFailure(call: Call<ModelCity>, t: Throwable) {
                    myToast(this@ProfileSetting, "Something went wrong")
                    progressDialog!!.dismiss()

                }

            })
    }

    private fun apiCallLanguageSpinner() {
//        progressDialog = ProgressDialog(this@RegirstrationTest)
//        progressDialog!!.setMessage("Loading..")
//        progressDialog!!.setTitle("Please Wait")
//        progressDialog!!.isIndeterminate = false
//        progressDialog!!.setCancelable(true)
//
//        progressDialog!!.show()
//
//        ApiClient.apiService.languageList()
//            .enqueue(object : Callback<ModelLanguage> {
//                @SuppressLint("LogNotTimber")
//                override fun onResponse(
//                    call: Call<ModelLanguage>, response: Response<ModelLanguage>
//                ) {
//
//                    languageList = response.body()!!;
//                    if (languageList != null) {
//
//                        //spinner code start
//                        val items = arrayOfNulls<String>(languageList.result!!.size)
//
//                        for (i in languageList.result!!.indices) {
//                            items[i] = languageList.result!![i].name
//                        }
//
//                        val adapter: ArrayAdapter<String?> =
//                            ArrayAdapter(this@ProfileSetting, R.layout.simple_list_item_1, items)
//                        binding.spinnerLanguages.adapter = adapter
//                        //  progressDialog!!.dismiss()
//
//
//                        binding.spinnerLanguages.onItemSelectedListener =
//                            object : AdapterView.OnItemSelectedListener {
//                                override fun onItemSelected(
//                                    adapterView: AdapterView<*>?,
//                                    view: View,
//                                    i: Int,
//                                    l: Long
//                                ) {
//                                    val id = languageList.result!![i].id
//                                    langaugeId = id.toString()
//                                    //  Toast.makeText(this@RegirstrationTest, "langauge-" + id, Toast.LENGTH_SHORT).show()
//                                }
//
//                                override fun onNothingSelected(adapterView: AdapterView<*>?) {}
//                            }
//
//                    }
//                }
//
//                override fun onFailure(call: Call<ModelLanguage>, t: Throwable) {
//                    myToast(this@ProfileSetting, "Something went wrong")
//                    //progressDialog!!.dismiss()
//
//                }
//
//            })
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