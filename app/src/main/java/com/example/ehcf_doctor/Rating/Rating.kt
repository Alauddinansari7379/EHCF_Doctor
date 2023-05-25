package com.example.ehcf.RatingAndReviews

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.RatingBar
import androidx.appcompat.app.AppCompatActivity
import com.example.ehcf.sharedpreferences.SessionManager
import com.example.ehcf_doctor.R
import com.example.ehcf_doctor.databinding.ActivityRatingBinding
import rezwan.pstu.cse12.youtubeonlinestatus.recievers.NetworkChangeReceiver
import xyz.teamgravity.checkinternet.CheckInternet


class Rating : AppCompatActivity() {
    private val context: Context = this@Rating
    private lateinit var binding: ActivityRatingBinding
    lateinit var ratingBar: RatingBar
    lateinit var button: Button
    var meetingId = ""
    var rating = ""
    private lateinit var sessionManager: SessionManager
    var progressDialog: ProgressDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRatingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.imgBack.setOnClickListener {
            onBackPressed()
        }
        meetingId = intent.getStringExtra("meetingId").toString()
        Log.e("meetingId", meetingId)

        binding.tvAddPhoto.setOnClickListener {
            val i = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            val selectImage = 1234
            startActivityForResult(i, selectImage)
        }

        ratingBar = findViewById(R.id.ratingBar)
        ratingBar.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
        ratingBar.numStars = 5
        ratingBar.onRatingBarChangeListener =
            RatingBar.OnRatingBarChangeListener { _, rating, _ ->
                this.rating = rating.toInt().toString()
                //  Toast.makeText(this@Rating, "Stars: " + rating.toInt(), Toast.LENGTH_SHORT).show()
            }
        binding.btnSendReview.setOnClickListener {
            if (binding.edtComment.text.isEmpty()){
                binding.edtComment.error="Enter Your Review"
                return@setOnClickListener
            }else{
               // apiCallRating()
            }
        }

    }


//    private fun apiCallRating() {
//        val comment =binding.edtComment.text.toString()
//        progressDialog = ProgressDialog(this@Rating)
//        progressDialog!!.setMessage("Loading...")
//        progressDialog!!.setTitle("Please Wait")
//        progressDialog!!.isIndeterminate = false
//        progressDialog!!.setCancelable(true)
//        progressDialog!!.show()
//
//        ApiClient.apiService.rating(meetingId, rating, comment)
//            .enqueue(object : Callback<ModelRating> {
//                @SuppressLint("LogNotTimber")
//                override fun onResponse(
//                    call: Call<ModelRating>, response: Response<ModelRating>
//                ) {
//                    Log.e("Ala", "${response.body()!!}")
//                    Log.e("Ala", "${response.body()!!.status}")
//                    if (response.body()!!.status == 1) {
//                        myToast(this@Rating, response.body()!!.message)
//                        startActivity(Intent(this@Rating, Appointments::class.java))
//
//                    } else {
//                        myToast(this@Rating, response.body()!!.message)
//
//                    }
//
//
//                }
//
//                override fun onFailure(call: Call<ModelRating>, t: Throwable) {
//                    myToast(this@Rating,"Something went wrong")
//                    progressDialog!!.dismiss()
//
//                }
//
//            })
//    }
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