package com.example.ehcf_doctor.Registration.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.net.Uri
import android.os.*
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.anurag.multiselectionspinner.MultiSelectionSpinnerDialog
import com.chivorn.smartmaterialspinner.SmartMaterialSpinner
import com.example.ehcf.Helper.myToast
import com.example.ehcf.sharedpreferences.SessionManager
import com.example.ehcf_doctor.Login.activity.SignIn
import com.example.ehcf_doctor.R
import com.example.ehcf_doctor.Registration.modelResponse.*
import com.example.ehcf_doctor.databinding.ActivityRegirstrationTestBinding
import com.example.myrecyview.apiclient.ApiClient
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.ktx.messaging
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import rezwan.pstu.cse12.youtubeonlinestatus.recievers.NetworkChangeReceiver
import xyz.teamgravity.checkinternet.CheckInternet
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.regex.Pattern

class RegirstrationTest : AppCompatActivity(), UploadRequestBody.UploadCallback,
    MultiSelectionSpinnerDialog.OnMultiSpinnerSelectionListener {
    private lateinit var binding: ActivityRegirstrationTestBinding
    private val context: Context = this@RegirstrationTest
    private var bloodGroup = ""
    private var phoneNumberWithCode = ""
    var progressDialog: ProgressDialog? = null
    private var phoneNumberWithCodeNew = ""
    private var selectedImageUri: Uri? = null
    private var genderValue = 0
    var openingTimeList = ArrayList<ModelOpenTime>()
    var closingTimeList = ArrayList<ModelCloseTime>()
    var specialistList = ArrayList<ModelSpecialist>()
    var genderList = ArrayList<ModelGender>()
    var followUpList = ArrayList<ModelGender>()
    var countryCodeNew = "91"
    var firstName = ""
    var milddleName = ""
    var lastName = ""
    private var countryCode = ""
    var doctorName = ""
    private var responseOTP = "0"
    private var varifyed = false
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
    var regstrationNumber = ""
    var langaugeId = ""
    var genderId = ""
    var followUpDay = ""
    var specilistId = ""
    var degree = ""
    var langauge1 = ""
    var langaugeList = ArrayList<String>()
    var langauges2 = ""
    var langauges3 = ""
    var langauges4 = ""
    var selectValue = ""
    var degreeList = ModelDegreeJava()
    private var specilList = ModelSpecilList();
    private var languageList = ModelLanguage();
    var test_spinner: Spinner? = null
    val contentList: MutableList<String> = ArrayList()

    private lateinit var sessionManager: SessionManager

    @SuppressLint("LogNotTimber")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegirstrationTestBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sessionManager = SessionManager(this)
        // test_spinner = findViewById<Spinner>(R.id.spinnerSpecialistTest)


//        lifecycleScope.launch {
//
//        }

        Handler(Looper.getMainLooper()).postDelayed({
            apiCallLanguageSpinner()
            apiCallSpecialistSpinner()
            apiCallQulificationSpinner()
            getToken()
        }, 1000)


        binding.imgBack.setOnClickListener {
            onBackPressed()
        }

        Log.e("Log", "countryCode-$countryCodeNew")
        Log.e("Ala", "doctorName-$specilistId")
        Log.e("Ala", "qualification-$degree")

        //List for storing text content for displaying it in the Spinner.

        //Setting Multi Selection Spinner without image.
        binding.spinnerMultiSpinner.setAdapterWithOutImage(this, contentList, this)
        binding.spinnerMultiSpinner.initMultiSpinner(this, binding.spinnerMultiSpinner)


        binding.btnVerify.setOnClickListener {
            if (binding.edtEnterOTP.text.toString() == responseOTP) {
                binding.layoutPhoneWithCode.setBackgroundResource(R.drawable.corner_green);
                // binding.edtPhoneNumberWithCode.setBackgroundColor(Color.parseColor("#FF4CAF50"))
                myToast(this, "Phone Number Verified")
                varifyed = true
                binding.layoutOTP.visibility = View.GONE
                binding.edtPhoneNumberWithCode.isEnabled = false

            } else if (binding.edtEnterOTP.text.isEmpty()) {
                binding.edtEnterOTP.error = "Enter OTP"
                binding.edtEnterOTP.requestFocus()

            } else {
                binding.edtEnterOTP.error = "Wrong OTP"
                binding.edtEnterOTP.requestFocus()
            }

        }
        binding.btnSendOTP.setOnClickListener {
            countryCode = binding.spinnerCountryCode.selectedCountryCodeWithPlus
            val phoneWithCode = binding.edtPhoneNumberWithCode.text.toString()
            val phoneWithCodeNew = countryCode + phoneWithCode
            val phoneWithCodeNew1 = phoneWithCodeNew.substring(1);

            Log.e("Alla,", "phoneWithCodeNew-$phoneWithCodeNew1")

            apiCallOTP(phoneWithCodeNew1)

        }

        binding.edtPhoneNumberWithCode.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {

                if (binding.edtPhoneNumberWithCode.length() >= 10) {
                    binding.layoutOTP.visibility = View.VISIBLE
                } else {
                    binding.layoutOTP.visibility = View.GONE

                }
            }
        })

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


//            val langauges = "$langauge1,$langauges2,$langauges3"
//            Log.e("log,", langauges)
//        degreeList.add(ModelDegree("Select Your Degree"))
//        degreeList.add(ModelDegree("MBBS"))
//        degreeList.add(ModelDegree("MS"))
//        degreeList.add(ModelDegree("MD"))
//        degreeList.add(ModelDegree("BAMS"))
//        degreeList.add(ModelDegree("BHMS"))
//        degreeList.add(ModelDegree("BPT"))
//        degreeList.add(ModelDegree("B.VSc"))
//        degreeList.add(ModelDegree("BUMS"))
//        degreeList.add(ModelDegree("BSMS"))
//        degreeList.add(ModelDegree("BNYS"))
//        degreeList.add(ModelDegree("DBS"))
//
//        binding.spinnerQualification.adapter =
//            ArrayAdapter<ModelDegree>(context, R.layout.simple_list_item_1, degreeList)
//
//        binding.spinnerQualification.onItemSelectedListener =
//            object : AdapterView.OnItemSelectedListener {
//                override fun onItemSelected(
//                    adapterView: AdapterView<*>?,
//                    view: View,
//                    i: Int,
//                    l: Long
//                ) {
//                    if (degreeList.size > 0) {
//                        qualification = degreeList[i].name
//                        Log.e(ContentValues.TAG, "qualification: $qualification")
//                    }
//                }
//
//                override fun onNothingSelected(adapterView: AdapterView<*>?) {}
//            }


        genderList.add(ModelGender("Select Gender", 0))
        genderList.add(ModelGender("Male", 1))
        genderList.add(ModelGender("Female", 2))
        genderList.add(ModelGender("Other", 3))

        followUpList.add(ModelGender("Select FollowUp Day", 0))
        followUpList.add(ModelGender("7", 7))
        followUpList.add(ModelGender("8", 8))
        followUpList.add(ModelGender("9", 9))
        followUpList.add(ModelGender("10", 10))

//        langaugeList.add(ModelLanguages("Select Language", 0))
//        langaugeList.add(ModelLanguages("English", 1))
//        langaugeList.add(ModelLanguages("French", 2))
//        langaugeList.add(ModelLanguages("Spanish", 3))
//        langaugeList.add(ModelLanguages("Arabic", 4))
//        langaugeList.add(ModelLanguages("Persian", 5))
//        langaugeList.add(ModelLanguages("German", 6))
//        langaugeList.add(ModelLanguages("Russian", 7))
//        langaugeList.add(ModelLanguages("Tamil", 8))
//        langaugeList.add(ModelLanguages("Hindi", 9))
//        langaugeList.add(ModelLanguages("Italian", 10))


        binding.layoutCamera.setOnClickListener {
       //     myToast(this,"Work on Progress")

            selectValue = "1"
            ImagePicker.with(this).cameraOnly()
//                                            .createIntent { intent ->
//                                startForProfileImageResult.launch(intent)
//                            }
                .start(REQUEST_CODE_IMAGE)
        }
        binding.layoutGallery.setOnClickListener {
            selectValue="2"
            opeinImageChooserNew()
         }

        binding.layoutPDF.setOnClickListener {
            selectValue="3"
            opeinImagePDF()
         }



        binding.spinnerOpen.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View, i: Int, l: Long) {
                if (openingTimeList.size > 0) {
                    openTime = openingTimeList[i].opentime
                    sessionManager.openTime = openingTimeList[i].opentime


                    Log.e(ContentValues.TAG, "openTime: $openTime")
                }
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {}
        }

        binding.spinnerClose.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View, i: Int, l: Long) {
                if (closingTimeList.size > 0) {
                    closeTime = closingTimeList[i].closeTime
                    sessionManager.closeTime = closingTimeList[i].closeTime
                    Log.e(ContentValues.TAG, "closeTime: $closeTime")
                }
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {}
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

        binding.spinnerFollowUp.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    adapterView: AdapterView<*>?,
                    view: View,
                    i: Int,
                    l: Long
                ) {
                    if (followUpList.size > 0) {
                        followUpDay = followUpList[i].id.toString()
                        Log.e(ContentValues.TAG, "followUpDay: $followUpDay")
                    }
                }

                override fun onNothingSelected(adapterView: AdapterView<*>?) {}
            }
        binding.edtPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                // get the password when we start typing
                val password: String = binding.edtPassword.text.toString()
                validatepass(password)
            }

            override fun afterTextChanged(editable: Editable) {}
        })

        binding.spinnerGender.adapter =
            ArrayAdapter<ModelGender>(context, R.layout.simple_list_item_1, genderList)

        binding.spinnerOpen.adapter =
            ArrayAdapter<ModelOpenTime>(context, R.layout.simple_list_item_1, openingTimeList)
        binding.spinnerClose.adapter =
            ArrayAdapter<ModelCloseTime>(context, R.layout.simple_list_item_1, closingTimeList)

        binding.spinnerFollowUp.adapter =
            ArrayAdapter<ModelGender>(context, R.layout.simple_list_item_1, followUpList)

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
            if (binding.edtPhoneNumberWithCode.text.isEmpty()) {
                binding.edtPhoneNumberWithCode.error = "Verify Phone Number"
                binding.edtPhoneNumberWithCode.requestFocus()
                binding.layoutOTP.visibility = View.VISIBLE
                return@setOnClickListener
            }
            if (!varifyed) {
                binding.edtPhoneNumberWithCode.error = "Verify Phone Number"
                binding.edtPhoneNumberWithCode.requestFocus()
                binding.layoutOTP.visibility = View.VISIBLE
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

            if (binding.edtRegistrationNumber.text.isEmpty()) {
                binding.edtRegistrationNumber.error = "Please enter RegistrationNumber"
                binding.edtRegistrationNumber.requestFocus()
                return@setOnClickListener
            }


//            if (binding.spinnerQualification.selectedItem.toString() == "Select Your Degree") {
//                myToast(this@RegirstrationTest, "Please Select Your Qualification!")
//                binding.spinnerQualification.requestFocus()
//                return@setOnClickListener
//            }

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
            if (binding.spinnerGender.selectedItem.toString() == "Select Gender") {
                myToast(this, "Select Gender")
            }
//            if (binding.spinnerMultiSpinner.epm== "Select Language") {
//                myToast(this, "Select Language")
//            }
            if (selectedImageUri == null) {
                binding.layoutRoot.snackbar("Select Registration Certificate First")
                return@setOnClickListener
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

            if (binding.edtPassword.text!!.isEmpty()) {
                binding.edtPassword.error = "Password Required"
                binding.edtPassword.requestFocus()
                return@setOnClickListener
            }

            if ((binding.edtPassword.text!!.isEmpty())) {
                validatePassword()
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
                milddleName = binding.edtMiddelName.text.toString().trim()
                lastName = binding.edtLastName.text.toString().trim()
                doctorName = "$firstName $milddleName $lastName"
                phoneNumberWithCode = binding.edtPhoneNumberWithCode.text.toString().trim()
                email = binding.edtEmail.text.toString().trim()
                additionalQualification = binding.edtAdditionalQualification.text.toString().trim()
                slotInterval = binding.edtSlotInterval.text.toString().trim()
                exprince = binding.edtExperience.text.toString().trim()
                description = binding.edtDescription.text.toString().trim()
                regstrationNumber = binding.edtRegistrationNumber.text.toString().trim()

                lattitude = "11.854369"
                longitude = "32.856965"
                phoneNumberWithCodeNew = countryCodeNew + phoneNumberWithCode

                Log.e("Log", "countryCode-$countryCodeNew")
                Log.e("Ala", "doctorName-$doctorName")
                Log.e("Ala", "qualification-$qualification")
                Log.e("Ala", "additionalQualification-$additionalQualification")
                Log.e("Ala", "phoneNumberWithCodeNew-$phoneNumberWithCodeNew")
                Log.e("Ala", "fcmToken-$fcmToken")
                Log.e("Ala", "confirmPassword-$confirmPassword")
                Log.e("Ala", "email-$email")

                if(selectValue=="1"){
                    apiCallRegisterCamera()

                }
                 else{
                     apiCallRegister()

                 }

            }
        }
    }

    private fun opeinImagePDF() {
//        Intent(Intent.ACTION_PICK).also {
//            it.type = "image/*"
//            (MediaStore.ACTION_IMAGE_CAPTURE)
//            val mimeTypes = arrayOf("image/jpeg", "image/png")
//            it.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
//            startActivityForResult(it, REQUEST_CODE_IMAGE)

        val pdfIntent = Intent(Intent.ACTION_GET_CONTENT)
        pdfIntent.type = "application/pdf"
        pdfIntent.addCategory(Intent.CATEGORY_OPENABLE)
        startActivityForResult(pdfIntent, REQUEST_CODE_IMAGE)

        //   }
    }

    private fun opeinImageChooserNew() {
        Intent(Intent.ACTION_PICK).also {
            it.type = "image/*"
            (MediaStore.ACTION_IMAGE_CAPTURE)
            val mimeTypes = arrayOf("image/jpeg", "image/png")
            it.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
            startActivityForResult(it, REQUEST_CODE_IMAGE)

//            val pdfIntent = Intent(Intent.ACTION_GET_CONTENT)
//            pdfIntent.type = "application/pdf"
//            pdfIntent.addCategory(Intent.CATEGORY_OPENABLE)
//            startActivityForResult(pdfIntent, REQUEST_CODE_IMAGE)

        }
    }

    @SuppressLint("StringFormatInvalid")
    private fun getToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(ContentValues.TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            fcmToken = task.result

            // Log and toast
            val msg = getString(R.string.channel_id, fcmToken)
            Log.e("Token", fcmToken)
            // Toast.makeText(requireContext(), token, Toast.LENGTH_SHORT).show()
        })
    }

    private fun subscribed() {
        Firebase.messaging.subscribeToTopic("Doctor")
            .addOnCompleteListener { task ->
                var msg = "Subscribed"
                if (!task.isSuccessful) {
                    msg = "Subscribe failed"
                }
                Log.d(ContentValues.TAG, msg)
            }
    }

    fun validatepass(password: String) {

        // check for pattern
        val uppercase = Pattern.compile("[A-Z]")
        val lowercase = Pattern.compile("[a-z]")
        val digit = Pattern.compile("[0-9]")

        // if lowercase character is not present
        if (!lowercase.matcher(password).find()) {
            binding.edtPassword.setBackgroundResource(R.drawable.corner_red);

        } else {
            binding.edtPassword.setBackgroundResource(R.drawable.corner_green)
            // if lowercase character is  present

        }

        // if uppercase character is not present
        if (!uppercase.matcher(password).find()) {
            binding.edtPassword.setBackgroundResource(R.drawable.corner_red);

        } else {
            binding.edtPassword.setBackgroundResource(R.drawable.corner_green)

            // if uppercase character is  present
        }
        // if digit is not present
        if (!digit.matcher(password).find()) {
            binding.edtPassword.setBackgroundResource(R.drawable.corner_red);

        } else {
            binding.edtPassword.setBackgroundResource(R.drawable.corner_green)

            // if digit is present
        }
        // if password length is less than 8
        if (password.length < 8) {
            binding.edtPassword.setBackgroundResource(R.drawable.corner_red);

        } else {
            binding.edtPassword.setBackgroundResource(R.drawable.corner_green)


        }
    }

    private val PASSWORD_PATTERN: Pattern = Pattern.compile(
        "^" +
                "(?=.*[@#$%^&+=])" +  // at least 1 special character
                "(?=\\S+$)" +  // no white spaces
                ".{8,}" +  // at least 4 characters
                "$"

    )

    private fun validatePassword(): Boolean {
        val passwordInput: String = binding.edtPassword.text.toString().trim()
        // if password field is empty
        // it will display error message "Field can not be empty"
        return if (passwordInput.isEmpty()) {
            binding.edtPassword.error = "Field can not be empty"
            false
        } else if (!PASSWORD_PATTERN.matcher(passwordInput).matches()) {
            binding.edtPassword.error = "At least 1 special character and least 8 characters"
            false
        } else {
            binding.edtPassword.error = null
            true
        }
    }

    private fun isValidPassword(password: String): Boolean {
        if (password.length < 8) return false
        if (password.firstOrNull { it.isDigit() } == null) return false
        if (password.filter { it.isLetter() }.firstOrNull { it.isUpperCase() } == null) return false
        if (password.filter { it.isLetter() }.firstOrNull { it.isLowerCase() } == null) return false
        if (password.firstOrNull { !it.isLetterOrDigit() } == null) return false

        return true
    }

    private fun apiCallOTP(phoneWithCodeNew: String) {

        progressDialog = ProgressDialog(this@RegirstrationTest)
        progressDialog!!.setMessage("Loading..")
        progressDialog!!.setTitle("Please Wait")
        progressDialog!!.isIndeterminate = false
        progressDialog!!.setCancelable(true)
        progressDialog!!.show()


        ApiClient.apiService.checkPhone(phoneWithCodeNew)
            .enqueue(object :
                Callback<ModelOTP> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelOTP>,
                    response: Response<ModelOTP>
                ) {
                    if (response.code() == 500) {
                        myToast(this@RegirstrationTest, "Server Error")
                        progressDialog!!.dismiss()
                    } else if (response.body()!!.status == 1) {
                        responseOTP = response.body()!!.result.otp
                        myToast(this@RegirstrationTest, "OTP Send Successfully")
                        binding.edtEnterOTP.requestFocus()
                        binding.btnSendOTP.text = "Resend OTP"
                        binding.btnSendOTP.isClickable = false
                        binding.btnSendOTP.backgroundTintList =
                            ColorStateList.valueOf(resources.getColor(R.color.shimmer_color));
                        //  binding.btnSendOTP.setBackgroundColor(Color.parseColor("#9F367A"))
                        timeCounter()
                        progressDialog!!.dismiss()
                    } else {

                        myToast(this@RegirstrationTest, response.body()!!.message)
                        progressDialog!!.dismiss()


                    }
                }

                override fun onFailure(call: Call<ModelOTP>, t: Throwable) {
                    myToast(this@RegirstrationTest, "Something went wrong")
                    progressDialog!!.dismiss()

                }

            })

    }

    private fun timeCounter() {
        object : CountDownTimer(30000, 1000) {

            // Callback function, fired on regular interval
            @SuppressLint("SetTextI18n")
            override fun onTick(millisUntilFinished: Long) {
                binding.btnSendOTP.backgroundTintList =
                    ColorStateList.valueOf(resources.getColor(R.color.shimmer_color));
                binding.tvSecond.text =
                    "OTP Resend In : " + millisUntilFinished / 1000 + " " + " Seconds"
            }

            override fun onFinish() {
                binding.btnSendOTP.isClickable = true
                binding.btnSendOTP.setBackgroundColor(Color.parseColor("#9F367A"))

            }
        }.start()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_IMAGE -> {
                    selectedImageUri = data?.data
                    when(selectValue){
                        "1"->{
                            binding.imageView.visibility = View.VISIBLE
                            binding.layoutchoise.visibility = View.VISIBLE
                            binding.imageView.setImageURI(selectedImageUri)
                        }
                        "2"->{
                            binding.layoutchoise.visibility = View.VISIBLE
                            binding.imageView.visibility = View.VISIBLE
                            binding.imageView.setImageURI(selectedImageUri)
                        }
                        "3"->{
                            binding.layoutchoise.visibility = View.VISIBLE
                            binding.imageViewPDF.visibility = View.VISIBLE
                        }
                    }
                 }
            }
        }
    }


    companion object {
        const val REQUEST_CODE_IMAGE = 101
    }

    override fun onProgressUpdate(percentage: Int) {
        // binding.progressBar.progress = percentage
    }

    private fun ContentResolver.getFileName(selectedImageUri: Uri): String {
        var name = ""
        val returnCursor = this.query(selectedImageUri, null, null, null, null)
        if (returnCursor != null) {
            val nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            returnCursor.moveToFirst()
            name = returnCursor.getString(nameIndex)
            returnCursor.close()


        }

        return name
    }

    private fun View.snackbar(message: String) {
        Snackbar.make(this, message, Snackbar.LENGTH_LONG).also { snackbar ->
            snackbar.setAction("OK") {
                snackbar.dismiss()
            }
        }.show()

    }

    @SuppressLint("SuspiciousIndentation")
    private fun apiCallRegister() {
        progressDialog = ProgressDialog(this@RegirstrationTest)
        progressDialog!!.setMessage("Loading..")
        progressDialog!!.setTitle("Please Wait")
        progressDialog!!.isIndeterminate = false
        progressDialog!!.setCancelable(true)
        progressDialog!!.show()

        val parcelFileDescriptor =
            contentResolver.openFileDescriptor(selectedImageUri!!, "r", null) ?: return

        val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)

        val file = File(cacheDir, contentResolver.getFileName(selectedImageUri!!))
        val outputStream = FileOutputStream(file)
        inputStream.copyTo(outputStream)


        //  binding.progressBar.progress = 0
        val body = UploadRequestBody(file, "image", this)
        val langauges = "$langauge1,$langauges2,$langauges3"
        Log.e("log,", langauges)
        ApiClient.apiService.register(
            doctorName,
            degree,
            additionalQualification,
            binding.edtPhoneNumberWithCode.text.toString(),
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
            langauges,
            description,
            regstrationNumber,
            MultipartBody.Part.createFormData("reg_cer", file.name, body),
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), "json"),
            followUpDay,
            binding.edtPostalCodenew.text.toString(),

        )
            .enqueue(object : Callback<ModelRegistrationNew> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelRegistrationNew>, response: Response<ModelRegistrationNew>
                ) {
                    try {
                        if (response.code() == 500) {
                            myToast(this@RegirstrationTest, "Server Error")
                        } else if (response.body()!!.status == 1) {
                            subscribed()
                            progressDialog!!.dismiss()
                            myToast(this@RegirstrationTest, response.body()!!.message)
                            val intent = Intent(applicationContext, SignIn::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                            finish()
                            startActivity(intent)
                        } else {
                            myToast(this@RegirstrationTest, response.body()!!.message)
                            progressDialog!!.dismiss()

                        }

                    }catch (e:Exception){
                        e.printStackTrace()
                        progressDialog!!.dismiss()
                    }
                }

                override fun onFailure(call: Call<ModelRegistrationNew>, t: Throwable) {
                    myToast(this@RegirstrationTest, "Something went wrong")
                    progressDialog!!.dismiss()

                }

            })
    }
    private fun apiCallRegisterCamera() {
        progressDialog = ProgressDialog(this@RegirstrationTest)
        progressDialog!!.setMessage("Loading..")
        progressDialog!!.setTitle("Please Wait")
        progressDialog!!.isIndeterminate = false
        progressDialog!!.setCancelable(true)
        progressDialog!!.show()

        val file: File = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                .absolutePath + "/myAppImages/")
        if (!file.exists()) {
            file.mkdirs()
        }
        val file1: File = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).absolutePath + "/myAppImages/" + selectedImageUri!!.lastPathSegment
        )

        // val fos = FileOutputStream(file1)

        val parcelFileDescriptor = contentResolver.openFileDescriptor(selectedImageUri!!, "r", null) ?: return

        val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
        // val file = File(cacheDir, contentResolver.getFileName(selectedImageUri!!))
        val outputStream = FileOutputStream(file1)
        inputStream.copyTo(outputStream)


        //  binding.progressBar.progress = 0
        val body = UploadRequestBody(file1, "image", this)
        val langauges = "$langauge1,$langauges2,$langauges3"
        Log.e("log,", langauges)
        ApiClient.apiService.register(
            doctorName,
            degree,
            additionalQualification,
            binding.edtPhoneNumberWithCode.text.toString(),
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
            langauges,
            description,
            regstrationNumber,
            MultipartBody.Part.createFormData("reg_cer", file1.name, body),
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), "json"),
            followUpDay,
            binding.edtPostalCodenew.text.toString(),
        )
            .enqueue(object : Callback<ModelRegistrationNew> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelRegistrationNew>, response: Response<ModelRegistrationNew>
                ) {
                    if (response.code() == 500) {
                        myToast(this@RegirstrationTest, "Server Error")
                    } else if (response.body()!!.status == 1) {
                        myToast(this@RegirstrationTest, response.body()!!.message)
                        subscribed()
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

                override fun onFailure(call: Call<ModelRegistrationNew>, t: Throwable) {
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

        //  progressDialog!!.show()

        ApiClient.apiService.specialistCategoryTest()
            .enqueue(object : Callback<ModelSpecilList> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelSpecilList>, response: Response<ModelSpecilList>
                ) {

                    if (response.code() == 500) {
                        myToast(this@RegirstrationTest, "Specialist getting field")
                    } else {
                        specilList = response.body()!!;
                        if (specilList != null) {

                            //spinner code start
                            val items = arrayOfNulls<String>(specilList.result!!.size)

                            for (i in specilList.result!!.indices) {

                                items[i] = specilList.result!![i].categoryName
                            }
                            val adapter: ArrayAdapter<String?> =
                                ArrayAdapter(
                                    this@RegirstrationTest,
                                    R.layout.simple_list_item_1,
                                    items
                                )
                            var spProvince: SmartMaterialSpinner<String>? = null
                            var spEmptyItem: SmartMaterialSpinner<String>? = null
                            binding.spinnerSpecialistTest.item = items.toMutableList() as List<Any>?



                            progressDialog!!.dismiss()

                            binding.spinnerSpecialistTest.onItemSelectedListener =
                                object : AdapterView.OnItemSelectedListener {
                                    override fun onItemSelected(
                                        adapterView: AdapterView<*>?,
                                        view: View,
                                        i: Int,
                                        l: Long
                                    ) {
                                        val id = specilList.result!![i].id
                                        specilistId = id.toString()
                                        // Toast.makeText(this@RegirstrationTest, "" + id, Toast.LENGTH_SHORT).show()
                                    }

                                    override fun onNothingSelected(adapterView: AdapterView<*>?) {}
                                }

                        }
                    }
                }

                override fun onFailure(call: Call<ModelSpecilList>, t: Throwable) {
                    myToast(this@RegirstrationTest, "Something went wrong")
                    progressDialog!!.dismiss()

                }

            })
    }

    private fun apiCallQulificationSpinner() {
        progressDialog = ProgressDialog(this@RegirstrationTest)
        progressDialog!!.setMessage("Loading..")
        progressDialog!!.setTitle("Please Wait")
        progressDialog!!.isIndeterminate = false
        progressDialog!!.setCancelable(true)

        //  progressDialog!!.show()

        ApiClient.apiService.getDegree()
            .enqueue(object : Callback<ModelDegreeJava> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelDegreeJava>, response: Response<ModelDegreeJava>
                ) {
                    if (response.code() == 500) {
                        myToast(this@RegirstrationTest, "Degree list getting field")
                    } else {
                        degreeList = response.body()!!;
                        if (degreeList != null) {

                            //spinner code start
                            val items = arrayOfNulls<String>(degreeList.result!!.size)

                            for (i in degreeList.result!!.indices) {

                                items[i] = degreeList.result!![i].degree
                            }
                            val adapter: ArrayAdapter<String?> =
                                ArrayAdapter(
                                    this@RegirstrationTest,
                                    R.layout.simple_list_item_1,
                                    items
                                )
                            var spProvince: SmartMaterialSpinner<String>? = null
                            var spEmptyItem: SmartMaterialSpinner<String>? = null
                            binding.spinnerDegree.item = items.toMutableList() as List<Any>?



                            progressDialog!!.dismiss()

                            binding.spinnerDegree.onItemSelectedListener =
                                object : AdapterView.OnItemSelectedListener {
                                    override fun onItemSelected(
                                        adapterView: AdapterView<*>?,
                                        view: View,
                                        i: Int,
                                        l: Long
                                    ) {
                                        degree = degreeList.result!![i].degree
                                        degree = degree
                                        // Toast.makeText(this@RegirstrationTest, "" + id, Toast.LENGTH_SHORT).show()
                                    }

                                    override fun onNothingSelected(adapterView: AdapterView<*>?) {}
                                }

                        }
                    }
                }

                override fun onFailure(call: Call<ModelDegreeJava>, t: Throwable) {
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
                    if (response.code() == 500) {
                        myToast(this@RegirstrationTest, "Language list getting field")
                    } else {
                        languageList = response.body()!!

                        if (languageList != null) {

                            //spinner code start
                            val items = arrayOfNulls<String>(languageList.result!!.size)

                            for (i in languageList.result!!.indices) {
                                items[i] = languageList.result!![i].name
                                languageList.result!![i].name?.let { contentList.add(it) }
                            }

                            val adapter: ArrayAdapter<String?> =
                                ArrayAdapter(
                                    this@RegirstrationTest,
                                    R.layout.simple_list_item_1,
                                    items
                                )
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
            } else {
                val changeReceiver = NetworkChangeReceiver(context)
                changeReceiver.build()
                //  myToast(requireActivity(),"Check Internet")
            }
        }
    }

    override fun OnMultiSpinnerItemSelected(chosenItems: MutableList<String>?) {
        for (i in chosenItems!!.indices) {
//            langauge1 = chosenItems[0]
//            langauges2 = chosenItems[1]
//            langauges3 = chosenItems[2]
//            langauges4 = chosenItems[4]
//            langauges5 = chosenItems[5]


        }
        langaugeList.add(chosenItems.toString())


        Log.e("langauges", langauge1)
        Log.e("langauges", langauges2)
        Log.e("langaugeList", langaugeList.toString())
    }

}

