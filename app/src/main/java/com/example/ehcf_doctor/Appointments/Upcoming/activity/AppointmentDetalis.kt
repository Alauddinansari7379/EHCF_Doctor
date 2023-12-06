package com.example.ehcf_doctor.Appointments.Upcoming.activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.ehcf.Helper.convertTo12Hour
import com.example.ehcf.Helper.myToast
import com.example.ehcf.sharedpreferences.SessionManager
import com.example.ehcf_doctor.Appointments.Upcoming.model.ModelAppointmentDatails
import com.example.ehcf_doctor.R
import com.example.ehcf_doctor.databinding.ActivityAppointmentDetalisBinding
import com.example.myrecyview.apiclient.ApiClient
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AppointmentDetalis : AppCompatActivity() {
    private val context: Context = this@AppointmentDetalis
    private lateinit var sessionManager: SessionManager
    var mydilaog: Dialog? = null
    var progressDialog: ProgressDialog? = null
    var dialog: Dialog? = null
    var bookingId = ""

    private lateinit var binding: ActivityAppointmentDetalisBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAppointmentDetalisBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.e("bookingId", bookingId)
        bookingId = intent.getStringExtra("bookingId").toString()

        binding.imgBack.setOnClickListener {
            onBackPressed()
        }
        apiCallAppointmentsDetails()

    }

    private fun apiCallAppointmentsDetails() {
        progressDialog = ProgressDialog(this@AppointmentDetalis)
        progressDialog!!.setMessage("Loading...")
        progressDialog!!.setTitle("Please Wait")
        progressDialog!!.isIndeterminate = false
        progressDialog!!.setCancelable(true)
        progressDialog!!.show()

        ApiClient.apiService.consultationDetails(bookingId)
            .enqueue(object : Callback<ModelAppointmentDatails> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelAppointmentDatails>, response: Response<ModelAppointmentDatails>
                ) {
                    try {
                        if (response.code() == 500) {
                            myToast(this@AppointmentDetalis, "Server Error")
                            progressDialog!!.dismiss()

                        } else if (response.body()!!.status == 0) {
                            myToast(this@AppointmentDetalis, response.body()!!.message)
                            progressDialog!!.dismiss()

                        } else {

                            binding.tvDate.text = response.body()!!.result.date
                            if (response.body()!!.result.start_time != null) {
                                binding.tvTime.text =
                                    convertTo12Hour(response.body()!!.result.start_time)

                            }
                            if (response.body()!!.result.member_name != null) {
                                binding.tvCustomerName.text = response.body()!!.result.member_name
                            } else {
                                binding.tvCustomerName.text = response.body()!!.result.customer_name

                            }
                            when (response.body()!!.result.consultation_type) {
                                "1" -> {
                                    binding.tvConsultationType.text = "Tele-Consultation"
                                }
                                "2" -> {
                                    binding.tvConsultationType.text = "Clinic-Visit"
                                }
                                "3" -> {
                                    binding.tvConsultationType.text = "Home-Visit"
                                }
                            }

                            when (response.body()!!.result.payment_mode) {
                                "1" -> {
                                    binding.tvPaymentMode.text = "Cash On Delivery"
                                }
                                "2" -> {
                                    binding.tvPaymentMode.text = "Online"
                                }
                                "5" -> {
                                    binding.tvPaymentMode.text = "Free"
                                }
                            }
                            binding.tvTotalAmount.text = response.body()!!.result.total
                            binding.tvBookingId.text = response.body()!!.result.id
                            binding.tvStatus.text = response.body()!!.result.status_for_doctor
                            if (response.body()!!.result.profile_picture != null) {
                                Picasso.get()
                                    .load("https://ehcf.thedemostore.in/uploads/${response.body()!!.result.profile_picture}")
                                    .placeholder(
                                        R.drawable.profile
                                    ).error(R.drawable.profile).into(binding.imgProfile);
                            }
                            progressDialog!!.dismiss()

//                    if (response.body()!!.result!=null){
//                        binding.tvNoDataFound.visibility = View.VISIBLE
//                        // myToast(requireActivity(),"No Appointment Found")
//                        progressDialog!!.dismiss()
//
//                    }else{
//                        binding.recyclerView.apply {
//                            binding.tvNoDataFound.visibility = View.GONE
//                            adapter = AdapterAppointmentsDetails(this@AppointmentDetails, response.body()!!)
//                            progressDialog!!.dismiss()
//
//                        }
//                    }

                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        myToast(this@AppointmentDetalis, "Something went wrong")
                        progressDialog!!.dismiss()
                    }
                }

                override fun onFailure(call: Call<ModelAppointmentDatails>, t: Throwable) {
                    myToast(this@AppointmentDetalis, "Something went wrong")
                    progressDialog!!.dismiss()

                }

            })
    }


}