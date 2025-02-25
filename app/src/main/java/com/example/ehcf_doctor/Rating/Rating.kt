package com.example.ehcf_doctor.Rating

import android.annotation.SuppressLint
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
import com.example.ehcf.Helper.myToast
import com.example.ehcf.sharedpreferences.SessionManager
import com.example.ehcf_doctor.Appointments.Appointments
import com.example.ehcf_doctor.Helper.AppProgressBar
import com.example.ehcf_doctor.R
import com.example.ehcf_doctor.Retrofit.ApiClient
import com.example.ehcf_doctor.databinding.ActivityRatingNewBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import rezwan.pstu.cse12.youtubeonlinestatus.recievers.NetworkChangeReceiver
import xyz.teamgravity.checkinternet.CheckInternet

class Rating : AppCompatActivity() {
    private val context: Context = this@Rating
    private lateinit var binding: ActivityRatingNewBinding
    lateinit var ratingBar: RatingBar
    lateinit var button: Button
    var meetingId = ""
    var rating = "1"
    private var count = 0
    private lateinit var sessionManager: SessionManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRatingNewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.imgBack.setOnClickListener {
            startActivity(Intent(this@Rating, Appointments::class.java))
            // onBackPressed()
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
            if (binding.edtComment.text.isEmpty()) {
                binding.edtComment.error = "Enter Your Review"
                return@setOnClickListener
            } else {
                apiCallRating()
            }
        }

    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        startActivity(Intent(this@Rating, Appointments::class.java))

    }

    private fun apiCallRating() {
        val comment = binding.edtComment.text.toString()
        AppProgressBar.showLoaderDialog(context)

        ApiClient.apiService.rating(meetingId, rating, comment)
            .enqueue(object : Callback<ModelRating> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelRating>, response: Response<ModelRating>
                ) {
                    Log.e("Ala", "${response.body()!!}")
                    Log.e("Ala", "${response.body()!!.status}")
                    if (response.body()!!.status == 1) {
                        count = 0
                        myToast(this@Rating, "Review Submitted")
                        AppProgressBar.hideLoaderDialog()
                        binding.edtComment.text.clear()
                        //  binding.btnSendReview.backgroundTintBlendMode.
                        startActivity(Intent(this@Rating, Appointments::class.java))

                    } else {
                        AppProgressBar.hideLoaderDialog()
                        myToast(this@Rating, response.body()!!.message)

                    }


                }

                override fun onFailure(call: Call<ModelRating>, t: Throwable) {
                    count++
                    if (count <= 3) {
                        apiCallRating()
                    } else {
                        myToast(this@Rating, "Something went wrong")
                    }
                    AppProgressBar.hideLoaderDialog()

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

}
