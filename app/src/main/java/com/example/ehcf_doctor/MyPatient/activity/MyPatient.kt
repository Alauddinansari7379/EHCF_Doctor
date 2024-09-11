package com.example.ehcf_doctor.MyPatient.activity

import android.Manifest.permission.CALL_PHONE
import android.annotation.SuppressLint
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.ehcf.Helper.myToast
import com.example.ehcf.sharedpreferences.SessionManager
import com.example.ehcf_doctor.MyPatient.adapter.AdapterCommentList
import com.example.ehcf_doctor.MyPatient.adapter.AdapterMyPatient
import com.example.ehcf_doctor.MyPatient.model.ModelCommentList
import com.example.ehcf_doctor.MyPatient.model.ModelMyPatient
import com.example.ehcf_doctor.MyPatient.model.ResultMyPatient
import com.example.ehcf_doctor.R
import com.example.ehcf_doctor.databinding.ActivityMyPatientBinding
import com.example.ehcf_doctor.Retrofit.ApiClient
import com.facebook.shimmer.ShimmerFrameLayout
import org.jitsi.meet.sdk.JitsiMeetActivity
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions
import org.jitsi.meet.sdk.JitsiMeetUserInfo
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.MalformedURLException
import java.net.URL
import java.util.*


class MyPatient : AppCompatActivity(), AdapterMyPatient.CommentList {
    private var context: Context = this@MyPatient
    var dialog: Dialog? = null

    var progressDialog: ProgressDialog? = null
    private lateinit var sessionManager: SessionManager
    private lateinit var binding: ActivityMyPatientBinding
    var shimmerFrameLayout: ShimmerFrameLayout? = null
    private lateinit var mainData: ArrayList<ResultMyPatient>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyPatientBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sessionManager = SessionManager(this)
        shimmerFrameLayout = findViewById(R.id.shimmer_myPatient)
        shimmerFrameLayout!!.startShimmer();
        mainData=ArrayList<ResultMyPatient>()

        binding.imgBack.setOnClickListener {
            onBackPressed()
        }

        callPermission()

        binding.edtSearch.addTextChangedListener { str ->
            setRecyclerViewAdapter(mainData.filter {
                it.customer_name!!.contains(str.toString(), ignoreCase = true)
            } as ArrayList<ResultMyPatient>)
        }

        binding.imgRefresh.setOnClickListener {
            overridePendingTransition(0, 0)
            finish()
            startActivity(intent)
            overridePendingTransition(0, 0)
        }


        apiCallMyPatient()
    }

    private fun callPermission() {
        if (ContextCompat.checkSelfPermission(this@MyPatient, CALL_PHONE) === PackageManager.PERMISSION_GRANTED
        ) {
            //  startActivity(callIntent)
            //  myToast(this,"Call Permission Granted")
        } else {
            requestPermissions(arrayOf(CALL_PHONE), 1)
        }
    }

    private fun apiCallMyPatient() {
        progressDialog = ProgressDialog(this@MyPatient)
        progressDialog!!.setMessage("Loading...")
        progressDialog!!.setTitle("Please Wait")
        progressDialog!!.isIndeterminate = false
        progressDialog!!.setCancelable(true)
        // progressDialog!!.show()

        ApiClient.apiService.getPatients(sessionManager.id.toString())
            .enqueue(object : Callback<ModelMyPatient> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelMyPatient>, response: Response<ModelMyPatient>
                ) {
                    try {
                        if (response.code() == 200) {
                            mainData = response.body()!!.result
                        }
                        if (response.code() == 500) {
                            myToast(this@MyPatient, "Server Error")
                            binding.shimmerMyPatient.visibility = View.GONE
                            progressDialog!!.dismiss()

                        } else if (response.body()!!.result.isEmpty()) {
                            binding.tvNoDataFound.visibility = View.VISIBLE
                            binding.shimmerMyPatient.visibility = View.GONE
                            // myToast(requireActivity(),"No Data Found")
                            progressDialog!!.dismiss()

                        } else {
                            setRecyclerViewAdapter(mainData)
                            progressDialog!!.dismiss()

                        }
                    } catch (e: Exception)
                    {
                        myToast(this@MyPatient, "Something went wrong")
                        progressDialog!!.dismiss()

                        e.printStackTrace()
                    }
                }

                override fun onFailure(call: Call<ModelMyPatient>, t: Throwable) {
                    myToast(this@MyPatient, "Something went wrong")
                    binding.shimmerMyPatient.visibility = View.GONE
                    progressDialog!!.dismiss()

                }

            })
    }

    private fun setRecyclerViewAdapter(data: ArrayList<ResultMyPatient>) {
        binding.recyclerView.apply {
            shimmerFrameLayout?.startShimmer()
            binding.recyclerView.visibility = View.VISIBLE
            binding.shimmerMyPatient.visibility = View.GONE
            binding.tvNoDataFound.visibility = View.GONE
            adapter = AdapterMyPatient(this@MyPatient, data,this@MyPatient)
            progressDialog!!.dismiss()

        }
    }



    override fun commentList(id: String) {
        progressDialog = ProgressDialog(this@MyPatient)
        progressDialog!!.setMessage("Loading..")
        progressDialog!!.setTitle("Please Wait")
        progressDialog!!.isIndeterminate = false
        progressDialog!!.setCancelable(true)
        progressDialog!!.show()

        var view = layoutInflater.inflate(R.layout.comment_list_dialog, null)
        dialog = Dialog(this)
        val btnOkDialog = view!!.findViewById<Button>(R.id.btnOkDialogNew)
        val recyclerViewCommentList =
            view!!.findViewById<RecyclerView>(R.id.recyclerViewCommentList)
        dialog = Dialog(this)


        ApiClient.apiService.customerAllComments(id)
            .enqueue(object :
                Callback<ModelCommentList> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelCommentList>,
                    response: Response<ModelCommentList>
                ) {
                    try {
                        if (response.body()!!.result.isEmpty()) {
                            myToast(this@MyPatient, "No Review Found")
                            progressDialog!!.dismiss()
                        } else {
                            recyclerViewCommentList.apply {
                                shimmerFrameLayout?.startShimmer()
                                adapter = AdapterCommentList(
                                    this@MyPatient,
                                    response.body()!!,
                                )
                                progressDialog!!.dismiss()
                            }

                        }
                    } catch (e: Exception) {
                        myToast(this@MyPatient, "Something went wrong")
                        e.printStackTrace()
                        progressDialog!!.dismiss()


                    }
                }

                override fun onFailure(call: Call<ModelCommentList>, t: Throwable) {
                    myToast(this@MyPatient, "Something went wrong")
                    progressDialog!!.dismiss()

                }

            })



        if (view.parent != null) {
            (view.parent as ViewGroup).removeView(view) // <- fix
        }
        dialog!!.setContentView(view)
        dialog?.setCancelable(true)
        // dialog?.setContentView(view)
        // val d1 = format.parse("2023/03/29 11:04:00")
//        Log.e("currentDate", currentTime)
//        Log.e("EndTime", startTime)


        dialog?.show()
        btnOkDialog.setOnClickListener {
            dialog?.dismiss()
        }

    }

    override fun videoCall(id: String) {
        SweetAlertDialog(this@MyPatient, SweetAlertDialog.WARNING_TYPE)
            .setTitleText("Are you sure want to Call?")
            .setCancelText("No")
            .setConfirmText("Yes")
            .showCancelButton(true)
            .setConfirmClickListener { sDialog ->
                sDialog.cancel()
//                val intent = Intent(applicationContext, SignIn::class.java)
//                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
//                finish()
//                startActivity(intent)

                videoCallFun( id)
            }
            .setCancelClickListener { sDialog ->
                sDialog.cancel()
            }
            .show()
    }

    private fun videoCallFun(id: String) {
        val jitsiMeetUserInfo = JitsiMeetUserInfo()
        jitsiMeetUserInfo.displayName = sessionManager.doctorName
        jitsiMeetUserInfo.email = sessionManager.email
        try {
            val defaultOptions: JitsiMeetConferenceOptions = JitsiMeetConferenceOptions.Builder()
                .setServerURL(URL("https://jvc.ethicalhealthcare.in/"))
                .setRoom(id)
                .setAudioMuted(false)
                .setVideoMuted(true)
                .setAudioOnly(false)
                .setUserInfo(jitsiMeetUserInfo)
                .setConfigOverride("enableInsecureRoomNameWarning", false)
                .setFeatureFlag("readOnlyName", true)
                .setFeatureFlag("prejoinpage.enabled", false)
                //  .setFeatureFlag("lobby-mode.enabled", false)
                // .setToken("123") // Set the meeting password
                //.setFeatureFlag("autoKnockLobby", false) // Disable lobby mode
                //.setFeatureFlag("disableModeratorIndicator", false)
                //.setFeatureFlag("chat.enabled",false)
                .setConfigOverride("requireDisplayName", true)
                .build()
            JitsiMeetActivity.launch(this@MyPatient, defaultOptions)
            //  startActivity(Intent(requireContext(),Rating::class.java))
        } catch (e: MalformedURLException) {
            e.printStackTrace();
        }
    }


}