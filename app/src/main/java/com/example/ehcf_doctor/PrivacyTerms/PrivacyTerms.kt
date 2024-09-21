package com.example.ehcf_doctor.PrivacyTerms

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.ehcf.Helper.myToast
import com.example.ehcf.Testing.Interface.ApiInterfaceHelthCube
import com.example.ehcf.sharedpreferences.SessionManager
import com.example.ehcf_doctor.Helper.AppProgressBar
import com.example.ehcf_doctor.PrivacyTerms.adapter.AdapterPrivacyPolicies
import com.example.ehcf_doctor.PrivacyTerms.model.ModelPrivacyPolicies
import com.example.ehcf_doctor.databinding.ActivityPrivacyTermsBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PrivacyTerms : AppCompatActivity() {
    private lateinit var binding: ActivityPrivacyTermsBinding
    private val context: Context = this@PrivacyTerms
    private lateinit var sessionManager: SessionManager
    private var count = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPrivacyTermsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.imgBack.setOnClickListener {
            onBackPressed()
        }

        sessionManager = SessionManager(this@PrivacyTerms)
        apiCallUpdateNameEmail()
//        binding.webView.webViewClient = WebViewClient()
//
//        // this will load the url of the website
        binding.webView.loadUrl("https://ehcf.thedemostore.in/privacy_policy")

        // this will enable the javascript settings, it can also allow xss vulnerabilities
        binding.webView.settings.javaScriptEnabled = true
//
//        // if you want to enable zoom feature
//        binding.webView.settings.setSupportZoom(true)
//    }
//
//    // if you press Back button this code will work
//    override fun onBackPressed() {
//        // if your webview can go back it will go back
//        if (binding.webView.canGoBack())
//            binding.webView.goBack()
//        // if your webview cannot go back
//        // it will exit the application
//        else
//            super.onBackPressed()
//    }
    }

    private fun apiCallUpdateNameEmail() {

        AppProgressBar.showLoaderDialog(context)
        val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            //.baseUrl("https://jsonplaceholder.typicode.com/")
            .baseUrl("https://ehcf.thedemostore.in/api/")
            .build()
            .create(ApiInterfaceHelthCube::class.java)

//        val retrofitData =retrofitBuilder.getUser()
//        retrofitData.enqueue(object : Callback<List<User>?> {
//            override fun onResponse(call: Call<List<User>?>, response: Response<List<User>?>) {
//                val recyclerView= findViewById<RecyclerView>(R.id.recyclerView)
//
//                recyclerView.apply {
//                    adapter=Adapter(context,response.body()!!)
//                }
//            }

        val retrofitData = retrofitBuilder.privacyPolicy("3")
        retrofitData.enqueue(object : Callback<ModelPrivacyPolicies> {
            override fun onResponse(
                call: Call<ModelPrivacyPolicies>,
                response: Response<ModelPrivacyPolicies>
            ) {
                if (response.code() == 500) {
                    myToast(this@PrivacyTerms, "Server Error")
                    AppProgressBar.hideLoaderDialog()

                } else if (response.code() == 200) {
                    count = 0
                    binding.rvUpcoming.apply {
                        adapter = AdapterPrivacyPolicies(this@PrivacyTerms, response.body()!!)
                        AppProgressBar.hideLoaderDialog()

                    }
                } else {

                }

            }

            override fun onFailure(call: Call<ModelPrivacyPolicies>, t: Throwable) {
                count++
                if (count <= 3) {
                    apiCallUpdateNameEmail()
                } else {
                    myToast(this@PrivacyTerms, "Something went wrong")
                }
                AppProgressBar.hideLoaderDialog()


            }

        })


    }

}