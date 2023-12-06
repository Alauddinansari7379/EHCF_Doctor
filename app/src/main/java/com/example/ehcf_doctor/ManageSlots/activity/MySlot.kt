package com.example.ehcf_doctor.ManageSlots.activity

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.postDelayed
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.ehcf.Helper.myToast
import com.example.ehcf.sharedpreferences.SessionManager
import com.example.ehcf_doctor.ManageSlots.adapter.AdapterSlotsList
import com.example.ehcf_doctor.ManageSlots.adapter.AdapterSwitchButton
import com.example.ehcf_doctor.ManageSlots.model.*
import com.example.ehcf_doctor.databinding.ActivityManageSlotsSeassionBinding
import com.example.myrecyview.apiclient.ApiClient
import com.facebook.shimmer.ShimmerFrameLayout
import okhttp3.internal.toImmutableList
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import rezwan.pstu.cse12.youtubeonlinestatus.recievers.NetworkChangeReceiver
import xyz.teamgravity.checkinternet.CheckInternet


class MySlot : AppCompatActivity(), AdapterSlotsList.DeleteSlot,
    AdapterSwitchButton.ActiveInactiveSlot {
    private val context: Context = this@MySlot
    var progressDialog: ProgressDialog? = null
    var relationList = ArrayList<String>()
    private lateinit var sessionManager: SessionManager
    var dayList = ArrayList<ModelDay>()
    var con = true
    var shimmerFrameLayout: ShimmerFrameLayout? = null
    // var Allocationlist = java.util.ArrayList<>()


    private lateinit var binding: ActivityManageSlotsSeassionBinding

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityManageSlotsSeassionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sessionManager = SessionManager(this)
        shimmerFrameLayout = findViewById(com.example.ehcf_doctor.R.id.shimmer_mySlot)
        shimmerFrameLayout!!.startShimmer();

        binding.imgBack.setOnClickListener {
            onBackPressed()
        }
        Handler(Looper.getMainLooper()).postDelayed(300) {
            apiCallSwitchButton()

        }

        // apiCall(dayId)


//        binding.mondaySwitch.setOnClickListener {
//            val dayCode="1"
//            if (binding.mondaySwitch.isEnabled){
//                apiCallActiveSlot(dayCode)
//            }else{
//                apiCallInActiveSlot(dayCode)
//            }
//        }


        dayList.add(ModelDay("Monday", "1"))
        dayList.add(ModelDay("Tuesday", "2"))
        dayList.add(ModelDay("Wednesday", "3"))
        dayList.add(ModelDay("Thursday", "4"))
        dayList.add(ModelDay("Friday", "5"))
        dayList.add(ModelDay("Saturday", "6"))
        dayList.add(ModelDay("Sunday", "7"))
        dayList.add(ModelDay("Sunday", "8"))

        binding.cardActiveInactive.setOnClickListener {
//            binding.layoutActiveInactive.visibility =View.VISIBLE
//            con=false

            if (con) {
                binding.layoutActiveInactive.visibility = View.VISIBLE
                binding.cardActiveInactive.backgroundTintList =
                    ColorStateList.valueOf(Color.parseColor("#EC4C4C"))
                con = false
            } else {
                binding.layoutActiveInactive.visibility = View.GONE
                binding.cardActiveInactive.backgroundTintList =
                    ColorStateList.valueOf(Color.parseColor("#4CAF50"))
                con = true
            }

        }


        binding.spinnerDay.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    adapterView: AdapterView<*>?,
                    view: View?,
                    i: Int,
                    l: Long
                ) {
                    if (dayList.size > 0) {
                        dayId = dayList[i].id
                        day = dayList[i].day
                        index = i


                        Log.e("Dayid", dayId)
                        apiCall(dayId)
                    }
                }

                override fun onNothingSelected(adapterView: AdapterView<*>?) {

                }
            }
        binding.spinnerDay.adapter =
            ArrayAdapter<ModelDay>(context, android.R.layout.simple_list_item_1, dayList)
        // binding.spinnerDay.setSelection(dayList.indexOf(sessionManager.registrationYear.toString())
        Log.e("DayidspinnerDay", dayId)

        //  binding.spinnerDay.setSelection(dayList.indexOf(ModelDay(day, dayId)))

//        val cls: ModelDay = dayList[dayId.toInt()]
//        binding.spinnerDay.setSelection(dayList.indexOf(cls), true)

        binding.spinnerDay.setSelection(index)


        //  val selectionPosition: Int = adapter.getPosition("YOUR_VALUE")

        val refreshListener = SwipeRefreshLayout.OnRefreshListener {
            overridePendingTransition(0, 0)
            finish()
            startActivity(intent)
            overridePendingTransition(0, 0)
        }
        binding.swipeRefreshLayout.setOnRefreshListener(refreshListener)

    }

    companion object {
        var day = ""
        var dayId = "0"
        var index = 0
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun apiCall(dayId: String) {
        progressDialog = ProgressDialog(this@MySlot)
        progressDialog!!.setMessage("Loading..")
        progressDialog!!.setTitle("Please Wait")
        progressDialog!!.isIndeterminate = false
        progressDialog!!.setCancelable(true)
        // progressDialog!!.show()

        ApiClient.apiService.getTimeSlot(sessionManager.id.toString(), dayId)
            .enqueue(object : Callback<ModelSlotList> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onResponse(
                    call: Call<ModelSlotList>,
                    response: Response<ModelSlotList>
                ) {
                    try {
                        if (response.code() == 500) {
                            myToast(this@MySlot, "Server Error")
                            binding.shimmerMySlot.visibility = View.GONE
                        } else if (response.body()!!.status == 0) {
                            binding.tvNoDataFound.visibility = View.VISIBLE
                            binding.shimmerMySlot.visibility = View.GONE
                            myToast(this@MySlot, "${response.body()!!.message}")
                            progressDialog!!.dismiss()

                        } else if (response.body()!!.result.isEmpty()) {
                            binding.rvManageSlot.adapter =
                                AdapterSlotsList(this@MySlot, response.body()!!, this@MySlot)
                            binding.rvManageSlot.adapter!!.notifyDataSetChanged()
                            binding.tvNoDataFound.visibility = View.VISIBLE
                            binding.shimmerMySlot.visibility = View.GONE
                            myToast(this@MySlot, "No Slot Found")
                            progressDialog!!.dismiss()

                        } else {
                            binding.rvManageSlot.adapter =
                                AdapterSlotsList(this@MySlot, response.body()!!, this@MySlot)
                            binding.rvManageSlot.adapter!!.notifyDataSetChanged()
                            binding.tvNoDataFound.visibility = View.GONE
                            shimmerFrameLayout?.startShimmer()
                            binding.rvManageSlot.visibility = View.VISIBLE
                            binding.shimmerMySlot.visibility = View.GONE
                            progressDialog!!.dismiss()
//                        binding.rvManageSlot.apply {
//                            binding.tvNoDataFound.visibility = View.GONE
//                            shimmerFrameLayout?.startShimmer()
//                            binding.rvManageSlot.visibility = View.VISIBLE
//                            binding.shimmerMySlot.visibility = View.GONE
//                            // myToast(this@ShuduleTiming, response.body()!!.message)
//                            adapter = AdapterSlotsList(this@MySlot, response.body()!!, this@MySlot)
//                            progressDialog!!.dismiss()
//
//                        }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        myToast(this@MySlot, "Something went wrong")
                        progressDialog!!.dismiss()

                    }
                }

                override fun onFailure(call: Call<ModelSlotList>, t: Throwable) {
                    myToast(this@MySlot, "Something went wrong")
                    binding.shimmerMySlot.visibility = View.GONE
                    progressDialog!!.dismiss()

                }


            })
    }


    private fun apiCallSwitchButton() {
        progressDialog = ProgressDialog(this@MySlot)
        progressDialog!!.setMessage("Loading..")
        progressDialog!!.setTitle("Please Wait")
        progressDialog!!.isIndeterminate = false
        progressDialog!!.setCancelable(true)
        //  progressDialog!!.show()

        ApiClient.apiService.switchButton(sessionManager.id.toString())
            .enqueue(object : Callback<ModelSwitechButton> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onResponse(
                    call: Call<ModelSwitechButton>,
                    response: Response<ModelSwitechButton>
                ) {
                    if (response.code() == 500) {
                        myToast(this@MySlot, "Server Error")
                        binding.shimmerMySlot.visibility = View.GONE
                    } else if (response.body()!!.status == 0) {
                        myToast(this@MySlot, "${response.body()!!.message}")
                        progressDialog!!.dismiss()

                    } else if (response.body()!!.result.isEmpty()) {
                        binding.rvSwitchButton.adapter =
                            AdapterSwitchButton(this@MySlot, response.body()!!, this@MySlot)
                        progressDialog!!.dismiss()

                    } else {
                        binding.rvSwitchButton.adapter =
                            AdapterSwitchButton(this@MySlot, response.body()!!, this@MySlot)
                        progressDialog!!.dismiss()
                    }

                }

                override fun onFailure(call: Call<ModelSwitechButton>, t: Throwable) {
                    myToast(this@MySlot, "Something went wrong")

                }


            })


    }

    private fun callAPIDelete(slotId: String) {
        progressDialog = ProgressDialog(this@MySlot)
        progressDialog!!.setMessage("Loading..")
        progressDialog!!.setTitle("Please Wait")
        progressDialog!!.isIndeterminate = false
        progressDialog!!.setCancelable(true)
        progressDialog!!.show()


        ApiClient.apiService.deleteSlot(slotId).enqueue(object : Callback<ModelDeleteSlot> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call: Call<ModelDeleteSlot>,
                response: Response<ModelDeleteSlot>
            ) {
                try {
                    if (response.body()!!.status == 1) {
                        myToast(this@MySlot, response.body()!!.message)
                        overridePendingTransition(0, 0)
                        finish()
                        startActivity(intent)
                        overridePendingTransition(0, 0)
                        //   binding.rvManageSlot.adapter!!.notifyDataSetChanged()
                        // myToast(requireActivity(),"No Data Found")
                        progressDialog!!.dismiss()

                    } else {
                        myToast(this@MySlot, response.body()!!.message)
                    }
                }catch (e:Exception){
                    e.printStackTrace()
                    progressDialog!!.dismiss()
                    myToast(this@MySlot, "Something went wrong")

                }
            }


            override fun onFailure(call: Call<ModelDeleteSlot>, t: Throwable) {
                myToast(this@MySlot, "Something went wrong")
                progressDialog!!.dismiss()
            }
        })
    }

    override fun deleteSlotApi(slotId: String) {
        SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
            .setTitleText("Are you sure want to Delete?")
            .setCancelText("No")
            .setConfirmText("Yes")
            .showCancelButton(true)
            .setConfirmClickListener { sDialog ->
                sDialog.cancel()
                callAPIDelete(slotId)

            }
            .setCancelClickListener { sDialog ->
                sDialog.cancel()
            }
            .show()

    }

    override fun updateSlotApi(slotId: String) {

        progressDialog = ProgressDialog(this@MySlot)
        progressDialog!!.setMessage("Loading..")
        progressDialog!!.setTitle("Please Wait")
        progressDialog!!.isIndeterminate = false
        progressDialog!!.setCancelable(true)
        progressDialog!!.show()


        ApiClient.apiService.deleteSlot(slotId).enqueue(object : Callback<ModelDeleteSlot> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call: Call<ModelDeleteSlot>,
                response: Response<ModelDeleteSlot>
            ) {
                try {
                    if (response.body()!!.status == 1) {
                        myToast(this@MySlot, response.body()!!.message)
                        // myToast(requireActivity(),"No Data Found")
                        progressDialog!!.dismiss()
                        apiCall(dayId)
                    } else {
                        myToast(this@MySlot, response.body()!!.message)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    myToast(this@MySlot, "Something went wrong")
                    progressDialog!!.dismiss()
                }

            }

            override fun onFailure(call: Call<ModelDeleteSlot>, t: Throwable) {
                myToast(this@MySlot, "Something went wrong")
                progressDialog!!.dismiss()

            }


        })
    }

    override fun activeASlot(dayCode: String, active: String) {
        progressDialog = ProgressDialog(this@MySlot)
        progressDialog!!.setMessage("Loading..")
        progressDialog!!.setTitle("Please Wait")
        progressDialog!!.isIndeterminate = false
        progressDialog!!.setCancelable(true)
        //  progressDialog!!.show()


        ApiClient.apiService.activeASlot(dayCode, active).enqueue(object : Callback<ModelActive> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call: Call<ModelActive>,
                response: Response<ModelActive>
            ) {
                try {
                    if (response.body()!!.status == 1) {
                        myToast(this@MySlot, response.body()!!.message)
                        // myToast(requireActivity(),"No Data Found")
                        progressDialog!!.dismiss()
                    } else {
                        myToast(this@MySlot, response.body()!!.message)
                    }
                } catch (e: Exception) {
                    myToast(this@MySlot, "Something went wrong")
                    progressDialog!!.dismiss()
                    e.printStackTrace()
                }
            }


            override fun onFailure(call: Call<ModelActive>, t: Throwable) {
                myToast(this@MySlot, "Something went wrong")
                progressDialog!!.dismiss()

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

    override fun activeSlot(dayCode: String) {
        progressDialog = ProgressDialog(this@MySlot)
        progressDialog!!.setMessage("Loading..")
        progressDialog!!.setTitle("Please Wait")
        progressDialog!!.isIndeterminate = false
        progressDialog!!.setCancelable(true)
        progressDialog!!.show()

        ApiClient.apiService.inactiveSlot(sessionManager.id.toString(), dayCode)
            .enqueue(object : Callback<ModelActive> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onResponse(
                    call: Call<ModelActive>,
                    response: Response<ModelActive>
                ) {
                    try {
                        if (response.body()!!.status == 1) {
                            myToast(this@MySlot, response.body()!!.message)
                            refresh()

//                    overridePendingTransition(0, 0)
//                    finish()
//                    startActivity(intent)
//                    overridePendingTransition(0, 0)
//                    //   binding.rvManageSlot.adapter!!.notifyDataSetChanged()
//                    // myToast(requireActivity(),"No Data Found")
                            progressDialog!!.dismiss()

                        } else {
                            myToast(this@MySlot, response.body()!!.message)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        myToast(this@MySlot, "Something went wrong")
                        progressDialog!!.dismiss()

                    }
                }


                override fun onFailure(call: Call<ModelActive>, t: Throwable) {
                    myToast(this@MySlot, "Something went wrong")
                    progressDialog!!.dismiss()

                }


            })


    }

    override fun inactiveSlot(dayCode: String) {

        progressDialog = ProgressDialog(this@MySlot)
        progressDialog!!.setMessage("Loading..")
        progressDialog!!.setTitle("Please Wait")
        progressDialog!!.isIndeterminate = false
        progressDialog!!.setCancelable(true)
        progressDialog!!.show()

        ApiClient.apiService.activeSlot(sessionManager.id.toString(), dayCode)
            .enqueue(object : Callback<ModelActive> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onResponse(
                    call: Call<ModelActive>,
                    response: Response<ModelActive>
                ) {
                    try {
                        if (response.body()!!.status == 1) {
                            myToast(this@MySlot, response.body()!!.message)
                            refresh()
//                    overridePendingTransition(0, 0)
//                    finish()
//                    startActivity(intent)
//                    overridePendingTransition(0, 0)
//                    //   binding.rvManageSlot.adapter!!.notifyDataSetChanged()
//                    // myToast(requireActivity(),"No Data Found")
                            progressDialog!!.dismiss()

                        } else {
                            myToast(this@MySlot, response.body()!!.message)
                            progressDialog!!.dismiss()

                        }
                    }catch (e:Exception){
                        e.printStackTrace()
                        myToast(this@MySlot, "Something went wrong")
                        progressDialog!!.dismiss()
                    }
                }


                override fun onFailure(call: Call<ModelActive>, t: Throwable) {
                    myToast(this@MySlot, "Something went wrong")
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

}
