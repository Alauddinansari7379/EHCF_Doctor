package com.example.ehcf_doctor.Registration.activity

import android.R
import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.example.ehcf.Helper.myToast
import com.example.ehcf.sharedpreferences.SessionManager
import com.example.ehcf_doctor.Login.activity.SignIn
import com.example.ehcf_doctor.Registration.modelResponse.*
import com.example.ehcf_doctor.databinding.ActivityRegirstrationTestBinding
import com.example.myrecyview.apiclient.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import rezwan.pstu.cse12.youtubeonlinestatus.recievers.NetworkChangeReceiver
import xyz.teamgravity.checkinternet.CheckInternet

class RegirstrationTest : AppCompatActivity() {
    private lateinit var binding: ActivityRegirstrationTestBinding
    private val context: Context = this@RegirstrationTest
    private var bloodGroup = ""
    private var phoneNumberWithCode = ""
    var progressDialog: ProgressDialog? = null
    private var phoneNumberWithCodeNew = ""
    private var genderValue = 0
    var openingTimeList = ArrayList<ModelOpenTime>()
    var closingTimeList = ArrayList<ModelCloseTime>()
    var specialistList = ArrayList<ModelSpecialist>()
    var genderList = ArrayList<ModelGender>()
    var langaugeList = ArrayList<ModelLanguages>()
    var countryCodeNew = "91"
    var firstName = ""
    var lastName = ""
    var doctorName = ""
    var phoneNumber = ""
    var email = ""
    var qualification = ""
    var additionalQualification = ""
    var password = ""
    var confirmPassword = ""
    var fcmToken = ""
    var openningTime = ""
    var closeTime = ""
    var openTime = ""
    var lattitude = ""
    var longitude = ""
    var slotInterval = ""
    var exprince = ""
    var description = ""
    var langaugeId = ""
    var genderId = ""
    var specilistId = ""
    private var specilList = ModelSpecilList();
    private var languageList = ModelLanguage();
    var test_spinner: Spinner? = null

    private lateinit var sessionManager: SessionManager

    @SuppressLint("LogNotTimber")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegirstrationTestBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sessionManager = SessionManager(this)
        // test_spinner = findViewById<Spinner>(R.id.spinnerSpecialistTest)

        apiCallLanguageSpinner()
        apiCallSpecialistSpinner()

        binding.imgBack.setOnClickListener {
            onBackPressed()
        }

        openingTimeList.add(ModelOpenTime("01:00:00"))
        openingTimeList.add(ModelOpenTime("02:00:00"))
        openingTimeList.add(ModelOpenTime("03:00:00"))
        openingTimeList.add(ModelOpenTime("04:00:00"))
        openingTimeList.add(ModelOpenTime("05:00:00"))
        openingTimeList.add(ModelOpenTime("06:00:00"))
        openingTimeList.add(ModelOpenTime("07:00:00"))
        openingTimeList.add(ModelOpenTime("08:00:00"))
        openingTimeList.add(ModelOpenTime("09:00:00"))
        openingTimeList.add(ModelOpenTime("10:00:00"))
        openingTimeList.add(ModelOpenTime("11:00:00"))
        openingTimeList.add(ModelOpenTime("12:00:00"))
        openingTimeList.add(ModelOpenTime("13:00:00"))
        openingTimeList.add(ModelOpenTime("14:00:00"))
        openingTimeList.add(ModelOpenTime("15:00:00"))
        openingTimeList.add(ModelOpenTime("16:00:00"))
        openingTimeList.add(ModelOpenTime("17:00:00"))
        openingTimeList.add(ModelOpenTime("18:00:00"))
        openingTimeList.add(ModelOpenTime("19:00:00"))
        openingTimeList.add(ModelOpenTime("20:00:00"))
        openingTimeList.add(ModelOpenTime("21:00:00"))
        openingTimeList.add(ModelOpenTime("22:00:00"))
        openingTimeList.add(ModelOpenTime("23:00:00"))
        openingTimeList.add(ModelOpenTime("24:00:00"))

        closingTimeList.add(ModelCloseTime("01:00:00"))
        closingTimeList.add(ModelCloseTime("02:00:00"))
        closingTimeList.add(ModelCloseTime("03:00:00"))
        closingTimeList.add(ModelCloseTime("04:00:00"))
        closingTimeList.add(ModelCloseTime("05:00:00"))
        closingTimeList.add(ModelCloseTime("06:00:00"))
        closingTimeList.add(ModelCloseTime("07:00:00"))
        closingTimeList.add(ModelCloseTime("08:00:00"))
        closingTimeList.add(ModelCloseTime("09:00:00"))
        closingTimeList.add(ModelCloseTime("10:00:00"))
        closingTimeList.add(ModelCloseTime("11:00:00"))
        closingTimeList.add(ModelCloseTime("12:00:00"))
        closingTimeList.add(ModelCloseTime("13:00:00"))
        closingTimeList.add(ModelCloseTime("14:00:00"))
        closingTimeList.add(ModelCloseTime("15:00:00"))
        closingTimeList.add(ModelCloseTime("16:00:00"))
        closingTimeList.add(ModelCloseTime("17:00:00"))
        closingTimeList.add(ModelCloseTime("18:00:00"))
        closingTimeList.add(ModelCloseTime("19:00:00"))
        closingTimeList.add(ModelCloseTime("20:00:00"))
        closingTimeList.add(ModelCloseTime("21:00:00"))
        closingTimeList.add(ModelCloseTime("22:00:00"))
        closingTimeList.add(ModelCloseTime("23:00:00"))
        closingTimeList.add(ModelCloseTime("24:00:00"))


        specialistList.add(ModelSpecialist("PSYCHOLOGIST", 1))
        specialistList.add(ModelSpecialist("SEXOLOGIST", 2))
        specialistList.add(ModelSpecialist("DERMATOLOGIST", 3))
        specialistList.add(ModelSpecialist("GYNEACOLOGIST", 4))
        specialistList.add(ModelSpecialist("GENERAL PHYSICIAN", 5))
        specialistList.add(ModelSpecialist("ANESTHESIA", 6))
        specialistList.add(ModelSpecialist("GASTROENTEROLOGIST", 7))
        specialistList.add(ModelSpecialist("CARDIOLOGIST", 8))
        specialistList.add(ModelSpecialist("DENTIST", 9))
        specialistList.add(ModelSpecialist("DIABETOLOGIST", 10))
        specialistList.add(ModelSpecialist("ENT SPECIALIST", 11))
        specialistList.add(ModelSpecialist("GENERAL SURGEON", 12))
        specialistList.add(ModelSpecialist("IVF (TEST TUBE BABY)", 13))
        specialistList.add(ModelSpecialist("NEPHROLOGIST", 14))
        specialistList.add(ModelSpecialist("OPTHALMOLOGIST (EYE SPECIALIST)", 15))
        specialistList.add(ModelSpecialist("ORTHOPEDICS", 16))
        specialistList.add(ModelSpecialist("PAEDIATRICIAN", 17))
        specialistList.add(ModelSpecialist("PHYSIOTHERAPY", 18))
        specialistList.add(ModelSpecialist("UROLOGIST", 19))

        genderList.add(ModelGender("Male", 1))
        genderList.add(ModelGender("Female", 2))
        genderList.add(ModelGender("Other", 3))

        langaugeList.add(ModelLanguages("English", 1))
        langaugeList.add(ModelLanguages("French", 2))
        langaugeList.add(ModelLanguages("Spanish", 3))
        langaugeList.add(ModelLanguages("Arabic", 4))
        langaugeList.add(ModelLanguages("Persian", 5))
        langaugeList.add(ModelLanguages("German", 6))
        langaugeList.add(ModelLanguages("Russian", 7))
        langaugeList.add(ModelLanguages("Tamil", 8))
        langaugeList.add(ModelLanguages("Hindi", 9))
        langaugeList.add(ModelLanguages("Italian", 10))



        binding.spinnerOpen.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View, i: Int, l: Long) {
                if (openingTimeList.size > 0) {
                    openTime = openingTimeList[i].opentime

                    Log.e(ContentValues.TAG, "openTime: $openTime")
                }
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {}
        }

        binding.spinnerClose.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View, i: Int, l: Long) {
                if (closingTimeList.size > 0) {
                    closeTime = closingTimeList[i].closeTime

                    Log.e(ContentValues.TAG, "closeTime: $closeTime")
                }
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {}
        }

//        binding.spinnerSpecialist.onItemSelectedListener =
//            object : AdapterView.OnItemSelectedListener {
//                override fun onItemSelected(
//                    adapterView: AdapterView<*>?,
//                    view: View,
//                    i: Int,
//                    l: Long
//                ) {
//                    if (specialistList.size > 0) {
//                        specilistId = specialistList[i].id.toString()
//
//                        Log.e(ContentValues.TAG, "specilistId: $specilistId")
//                    }
//                }
//
//                override fun onNothingSelected(adapterView: AdapterView<*>?) {}
//            }

        binding.spinnerGender.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View, i: Int, l: Long) {
                if (genderList.size > 0) {
                    genderId = genderList[i].id.toString()

                    Log.e(ContentValues.TAG, "gender: $genderId")
                }
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {}
        }
        binding.spinnerLanguages.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    adapterView: AdapterView<*>?,
                    view: View,
                    i: Int,
                    l: Long
                ) {
                    if (langaugeList.size > 0) {
                        langaugeId = langaugeList[i].id.toString()

                        Log.e(ContentValues.TAG, "langaugeId: $langaugeId")
                    }
                }

                override fun onNothingSelected(adapterView: AdapterView<*>?) {}
            }


        binding.spinnerLanguages.adapter = ArrayAdapter<ModelLanguages>(context, R.layout.simple_list_item_1, langaugeList)

        binding.spinnerGender.adapter =
            ArrayAdapter<ModelGender>(context, R.layout.simple_list_item_1, genderList)

        binding.spinnerOpen.adapter =
            ArrayAdapter<ModelOpenTime>(context, R.layout.simple_list_item_1, openingTimeList)
        binding.spinnerClose.adapter =
            ArrayAdapter<ModelCloseTime>(context, R.layout.simple_list_item_1, closingTimeList)

        binding.spinnerCountryCode.setOnCountryChangeListener {
            val countryCode = binding.spinnerCountryCode.selectedCountryCodeWithPlus
            countryCodeNew = countryCode.substring(1)
        }

        binding.btnRegister.setOnClickListener {
            if (binding.edtFirstName.text.isEmpty()) {
                binding.edtFirstName.error = "First Name Required"
                binding.edtFirstName.requestFocus()
                return@setOnClickListener
            }
            if (binding.edtLastName.text.isEmpty()) {
                binding.edtLastName.error = "Last Name Required"
                binding.edtLastName.requestFocus()
                return@setOnClickListener
            }
            if (binding.edtPhoneNumber.text.isEmpty()) {
                binding.edtPhoneNumber.error = "Phone Number Required"
                binding.edtPhoneNumber.requestFocus()
                return@setOnClickListener
            }
            if (binding.edtEmail.text.isEmpty()) {
                binding.edtEmail.error = "Email Required"
                binding.edtEmail.requestFocus()
                return@setOnClickListener
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(binding.edtEmail.text.toString()).matches()) {
                binding.edtEmail.error = "Enter valid Email address !"
                binding.edtEmail.requestFocus()
                return@setOnClickListener
            }


            if (binding.edtQualification.text.isEmpty()) {
                binding.edtQualification.error = "Please enter Qualification"
                binding.edtQualification.requestFocus()
                return@setOnClickListener
            }

            if (binding.edtAdditionalQualification.text.isEmpty()) {
                binding.edtAdditionalQualification.error = "Please enter AdditionalQualification"
                binding.edtAdditionalQualification.requestFocus()
                return@setOnClickListener
            }
            if (binding.edtSlotInterval.text.isEmpty()) {
                binding.edtSlotInterval.error = "Please enter SlotInterval"
                binding.edtSlotInterval.requestFocus()
                return@setOnClickListener
            }

            if (binding.edtExperience.text.isEmpty()) {
                binding.edtExperience.error = "Please enter Exprince"
                binding.edtExperience.requestFocus()
                return@setOnClickListener
            }
            if (binding.spinnerGender.selectedItem == null) {
                myToast(this, "Select Gender")
            }
            if (binding.spinnerLanguages.selectedItem == null) {
                myToast(this, "Select Language")
            }
            if (binding.edtDescription.text.isEmpty()) {
                binding.edtDescription.error = "Please enter Description"
                binding.edtDescription.requestFocus()
                return@setOnClickListener
            }
            if (binding.spinnerOpen.selectedItem == null) {
                myToast(this, "Select Open Time")
            }
            if (binding.spinnerClose.selectedItem == null) {
                myToast(this, "Select Close Time")
            }
            password = binding.edtPassword.text.toString()
            confirmPassword = binding.edtConfirmPassword.text.toString()

            if (binding.edtPassword.text.isEmpty()) {
                binding.edtPassword.error = "Password Required"
                binding.edtPassword.requestFocus()
                return@setOnClickListener
            }
            if ((binding.edtPassword.text.toString().length < 8)) {
                binding.edtPassword.error = "Password must be at least 8 characters"
                binding.edtPassword.requestFocus()
                return@setOnClickListener
            }
            if (binding.edtConfirmPassword.text!!.isEmpty()) {
                binding.edtConfirmPassword.error = "Password Required"
                binding.edtConfirmPassword.requestFocus()
                return@setOnClickListener
            } else if (password != confirmPassword) {
                binding.edtConfirmPassword.error = "Password Miss Match"
                binding.edtConfirmPassword.requestFocus()
                return@setOnClickListener
            } else {
                firstName = binding.edtFirstName.text.toString().trim()
                lastName = binding.edtLastName.text.toString().trim()
                doctorName = "$firstName $lastName"
                phoneNumber = binding.edtPhoneNumber.text.toString().trim()
                phoneNumberWithCode = binding.edtPhoneNumberWithCode.text.toString().trim()
                email = binding.edtEmail.text.toString().trim()
                qualification = binding.edtQualification.text.toString().trim()
                additionalQualification = binding.edtAdditionalQualification.text.toString().trim()
                slotInterval = binding.edtSlotInterval.text.toString().trim()
                exprince = binding.edtExperience.text.toString().trim()
                description = binding.edtDescription.text.toString().trim()

                fcmToken = "fdsfsdfsdfsr4354fdsfsdgffdsgfd"
                lattitude = "11.854369"
                longitude = "32.856965"
                phoneNumberWithCodeNew = countryCodeNew + phoneNumberWithCode

                Log.e("Log", "countryCode-$countryCodeNew")
                Log.e("Ala", "doctorName-$doctorName")
                Log.e("Ala", "qualification-$qualification")
                Log.e("Ala", "additionalQualification-$additionalQualification")
                Log.e("Ala", "phoneNumber-$phoneNumber")
                Log.e("Ala", "phoneNumberWithCodeNew-$phoneNumberWithCodeNew")
                Log.e("Ala", "fcmToken-$fcmToken")
                Log.e("Ala", "confirmPassword-$confirmPassword")
                Log.e("Ala", "email-$email")

                apiCall()
            }
        }
    }

    private fun apiCall() {
        progressDialog = ProgressDialog(this@RegirstrationTest)
        progressDialog!!.setMessage("Loading..")
        progressDialog!!.setTitle("Please Wait")
        progressDialog!!.isIndeterminate = false
        progressDialog!!.setCancelable(true)

        progressDialog!!.show()

        ApiClient.apiService.register(
            doctorName,
            qualification,
            additionalQualification,
            phoneNumber,
            phoneNumberWithCodeNew,
            fcmToken,
            confirmPassword,
            email,
            openTime,
            closeTime,
            lattitude,
            longitude,
            slotInterval,
            specilistId,
            exprince,
            genderId,
            langaugeId,
            description
        )
            .enqueue(object : Callback<RegistationResponse> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<RegistationResponse>, response: Response<RegistationResponse>
                ) {

                    Log.e("Ala", "${response.body()!!.result}")
                    Log.e("Ala", response.body()!!.message)
                    Log.e("Ala", "${response.body()!!.status}")

                    if (response.body()!!.status == 1) {
                        myToast(this@RegirstrationTest, response.body()!!.message)
                        progressDialog!!.dismiss()
                        val intent = Intent(applicationContext, SignIn::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                        finish()
                        startActivity(intent)
                    } else {
                        myToast(this@RegirstrationTest, "${response.body()!!.message}")
                        progressDialog!!.dismiss()

                    }

                }

                override fun onFailure(call: Call<RegistationResponse>, t: Throwable) {
                    myToast(this@RegirstrationTest, "Something went wrong")
                    progressDialog!!.dismiss()

                }

            })
    }
    private fun apiCallSpecialistSpinner() {
        progressDialog = ProgressDialog(this@RegirstrationTest)
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

                    specilList = response.body()!!;
                    if (specilList != null) {

                        //spinner code start
                        val items = arrayOfNulls<String>(specilList.result!!.size)

                        for (i in specilList.result!!.indices) {
                            items[i] = specilList.result!![i].categoryName
                        }
                        val adapter: ArrayAdapter<String?> = ArrayAdapter(this@RegirstrationTest,R.layout.simple_list_item_1, items)
                        binding.spinnerSpecialistTest.adapter = adapter
                        progressDialog!!.dismiss()


                        binding.spinnerSpecialistTest.onItemSelectedListener =
                            object : AdapterView.OnItemSelectedListener {
                                override fun onItemSelected(adapterView: AdapterView<*>?, view: View, i: Int, l: Long) {
                                    val id = specilList.result!![i].id
                                    specilistId=id.toString()
                                 //   Toast.makeText(this@RegirstrationTest, "" + id, Toast.LENGTH_SHORT).show()
                                }

                                override fun onNothingSelected(adapterView: AdapterView<*>?) {}
                            }

                    }
                }

                override fun onFailure(call: Call<ModelSpecilList>, t: Throwable) {
                    myToast(this@RegirstrationTest, "Something went wrong")
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

        ApiClient.apiService.languageList()
            .enqueue(object : Callback<ModelLanguage> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelLanguage>, response: Response<ModelLanguage>
                ) {

                    languageList = response.body()!!;
                    if (languageList != null) {

                        //spinner code start
                        val items = arrayOfNulls<String>(languageList.result!!.size)

                        for (i in languageList.result!!.indices) {
                            items[i] = languageList.result!![i].name
                        }

                        val adapter: ArrayAdapter<String?> = ArrayAdapter(this@RegirstrationTest,R.layout.simple_list_item_1, items)
                        binding.spinnerLanguages.adapter = adapter
                      //  progressDialog!!.dismiss()


                        binding.spinnerLanguages.onItemSelectedListener =
                            object : AdapterView.OnItemSelectedListener {
                                override fun onItemSelected(adapterView: AdapterView<*>?, view: View, i: Int, l: Long) {
                                    val id = languageList.result!![i].id
                                    langaugeId=id.toString()
                                  //  Toast.makeText(this@RegirstrationTest, "langauge-" + id, Toast.LENGTH_SHORT).show()
                                }

                                override fun onNothingSelected(adapterView: AdapterView<*>?) {}
                            }

                    }
                }

                override fun onFailure(call: Call<ModelLanguage>, t: Throwable) {
                    myToast(this@RegirstrationTest, "Something went wrong")
                    //progressDialog!!.dismiss()

                }

            })
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

