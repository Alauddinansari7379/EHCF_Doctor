package com.example.ehcf_doctor.Profile.activity

import android.R
import android.annotation.SuppressLint
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
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.postDelayed
import com.example.ehcf.Helper.myToast
import com.example.ehcf.sharedpreferences.SessionManager
import com.example.ehcf_doctor.MainActivity.activity.MainActivity
import com.example.ehcf_doctor.Profile.modelResponse.ModelUpdateQulification
import com.example.ehcf_doctor.Registration.modelResponse.ModelGender
import com.example.ehcf_doctor.Registration.modelResponse.ModelLanguage
import com.example.ehcf_doctor.Registration.modelResponse.ModelSpecilList
import com.example.ehcf_doctor.databinding.ActivityProfileSettingBinding
import com.example.myrecyview.apiclient.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import rezwan.pstu.cse12.youtubeonlinestatus.recievers.NetworkChangeReceiver
import xyz.teamgravity.checkinternet.CheckInternet

class ProfileSetting : AppCompatActivity() {
    private val context: Context =this@ProfileSetting
    private lateinit var sessionManager: SessionManager
    var progressDialog: ProgressDialog? =null
    private var specilList = ModelSpecilList();
    private var languageList = ModelLanguage();
    var genderList = ArrayList<ModelGender>()
    var specilistId=""
    var genderId=""
    var langaugeId=""
    var description=""
    var experience=""

    private lateinit var binding:ActivityProfileSettingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileSettingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sessionManager = SessionManager(this)

        genderList.add(ModelGender("Male", 1))
        genderList.add(ModelGender("Female", 2))
        genderList.add(ModelGender("Other", 3))
        binding.spinnerGender.adapter = ArrayAdapter<ModelGender>(context, R.layout.simple_list_item_1, genderList)

    binding.imgBack.setOnClickListener {
        onBackPressed()
    }
        Handler(Looper.getMainLooper()).postDelayed(300) {
            apiCallLanguageSpinner()
            apiCallSpecialistSpinner()
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

        binding.btnUpdate.setOnClickListener {
            if (binding.edtDescription.text.isEmpty()){
                myToast(this,"Enter Description")
                binding.edtDescription.requestFocus()
                return@setOnClickListener
            }
            if (binding.edtExperience.text.isEmpty()){
                myToast(this,"Enter Experience")
                binding.edtExperience.requestFocus()
                return@setOnClickListener
            }else
            {
                experience=binding.edtExperience.text.toString().trim()
                description=binding.edtDescription.text.toString().trim()

                apiCallUpdateQualification()
            }
        }


//

    }
    private fun apiCallUpdateQualification() {
        progressDialog = ProgressDialog(this@ProfileSetting)
        progressDialog!!.setMessage("Loading..")
        progressDialog!!.setTitle("Please Wait")
        progressDialog!!.isIndeterminate = false
        progressDialog!!.setCancelable(true)

        progressDialog!!.show()

        ApiClient.apiService.updateQualification(specilistId,"0",experience,genderId,langaugeId,sessionManager.id.toString())
            .enqueue(object : Callback<ModelUpdateQulification> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelUpdateQulification>, response: Response<ModelUpdateQulification>
                ) {

                  if (response.body()!!.status==1){
                      myToast(this@ProfileSetting,response.body()!!.message)
                      startActivity(Intent(this@ProfileSetting, MainActivity::class.java))
                  }else{
                      myToast(this@ProfileSetting,response.body()!!.message)
                  }

                    }


                override fun onFailure(call: Call<ModelUpdateQulification>, t: Throwable) {
                    myToast(this@ProfileSetting, "Something went wrong")
                    progressDialog!!.dismiss()

                }

            })
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

                    specilList = response.body()!!;
                    if (specilList != null) {

                        //spinner code start
                        val items = arrayOfNulls<String>(specilList.result!!.size)

                        for (i in specilList.result!!.indices) {
                            items[i] = specilList.result!![i].categoryName
                        }
                        val adapter: ArrayAdapter<String?> = ArrayAdapter(this@ProfileSetting,R.layout.simple_list_item_1, items)
                        binding.spinnerSpecialist.adapter = adapter
                        progressDialog!!.dismiss()


                        binding.spinnerSpecialist.onItemSelectedListener =
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

                        val adapter: ArrayAdapter<String?> = ArrayAdapter(this@ProfileSetting,R.layout.simple_list_item_1, items)
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
                    myToast(this@ProfileSetting, "Something went wrong")
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