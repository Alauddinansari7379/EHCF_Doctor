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
import com.example.ehcf_doctor.Helper.AppProgressBar
import com.example.ehcf_doctor.ManageSlots.adapter.AdapterSlotsList
import com.example.ehcf_doctor.ManageSlots.adapter.AdapterSwitchButton
import com.example.ehcf_doctor.ManageSlots.model.*
import com.example.ehcf_doctor.databinding.ActivityManageSlotsSeassionBinding
import com.example.ehcf_doctor.Retrofit.ApiClient
import com.facebook.shimmer.ShimmerFrameLayout
import id.zelory.compressor.calculateInSampleSize
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import rezwan.pstu.cse12.youtubeonlinestatus.recievers.NetworkChangeReceiver
import xyz.teamgravity.checkinternet.CheckInternet


class MySlot : AppCompatActivity(), AdapterSlotsList.DeleteSlot,
    AdapterSwitchButton.ActiveInactiveSlot {
    private val context: Context = this@MySlot
    var relationList = ArrayList<String>()
    private lateinit var sessionManager: SessionManager
    var dayList = ArrayList<ModelDay>()
    var con = true
    private var count = 0
    private var count2 = 0
    private var count3 = 0
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
        dayList.add(ModelDay("Monday", "1"))
        dayList.add(ModelDay("Tuesday", "2"))
        dayList.add(ModelDay("Wednesday", "3"))
        dayList.add(ModelDay("Thursday", "4"))
        dayList.add(ModelDay("Friday", "5"))
        dayList.add(ModelDay("Saturday", "6"))
        dayList.add(ModelDay("Sunday", "7"))
        // dayList.add(ModelDay("Sunday", "8"))

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
        AppProgressBar.showLoaderDialog(context)

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
                            AppProgressBar.hideLoaderDialog()

                        } else if (response.body()!!.result.isEmpty()) {
                            count = 0
                            binding.rvManageSlot.adapter =
                                AdapterSlotsList(this@MySlot, response.body()!!, this@MySlot)
                            binding.rvManageSlot.adapter!!.notifyDataSetChanged()
                            binding.tvNoDataFound.visibility = View.VISIBLE
                            binding.shimmerMySlot.visibility = View.GONE
                            myToast(this@MySlot, "No Slot Found")
                            AppProgressBar.hideLoaderDialog()

                        } else {
                            count = 0
                            binding.rvManageSlot.adapter =
                                AdapterSlotsList(this@MySlot, response.body()!!, this@MySlot)
                            binding.rvManageSlot.adapter!!.notifyDataSetChanged()
                            binding.tvNoDataFound.visibility = View.GONE
                            shimmerFrameLayout?.startShimmer()
                            binding.rvManageSlot.visibility = View.VISIBLE
                            binding.shimmerMySlot.visibility = View.GONE
                            AppProgressBar.hideLoaderDialog()
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        myToast(this@MySlot, "Something went wrong")
                        AppProgressBar.hideLoaderDialog()

                    }
                }

                override fun onFailure(call: Call<ModelSlotList>, t: Throwable) {
                    count++
                    if (count <= 3) {
                        apiCall(dayId)
                    } else {
                        myToast(this@MySlot, "Something went wrong")
                        binding.shimmerMySlot.visibility = View.GONE
                    }
                    AppProgressBar.hideLoaderDialog()

                }


            })
    }


    private fun apiCallSwitchButton() {
        AppProgressBar.showLoaderDialog(context)
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
                       AppProgressBar.hideLoaderDialog()

                    } else if (response.body()!!.result.isEmpty()) {
                        binding.rvSwitchButton.adapter =
                            AdapterSwitchButton(this@MySlot, response.body()!!, this@MySlot)
                        AppProgressBar.hideLoaderDialog()

                    } else {
                        binding.rvSwitchButton.adapter =
                            AdapterSwitchButton(this@MySlot, response.body()!!, this@MySlot)
                       AppProgressBar.hideLoaderDialog()
                    }

                }

                override fun onFailure(call: Call<ModelSwitechButton>, t: Throwable) {
                    myToast(this@MySlot, "Something went wrong")

                }


            })


    }

    private fun callAPIDelete(slotId: String) {
        AppProgressBar.showLoaderDialog(context)
        ApiClient.apiService.deleteSlot(slotId).enqueue(object : Callback<ModelDeleteSlot> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call: Call<ModelDeleteSlot>,
                response: Response<ModelDeleteSlot>
            ) {
                try {
                    if (response.body()!!.status == 1) {
                        count2 = 0
                        myToast(this@MySlot, response.body()!!.message)
                        overridePendingTransition(0, 0)
                        finish()
                        startActivity(intent)
                        overridePendingTransition(0, 0)
                        //   binding.rvManageSlot.adapter!!.notifyDataSetChanged()
                        // myToast(requireActivity(),"No Data Found")
                        AppProgressBar.hideLoaderDialog()

                    } else {
                        myToast(this@MySlot, response.body()!!.message)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    AppProgressBar.hideLoaderDialog()
                    myToast(this@MySlot, "Something went wrong")

                }
            }


            override fun onFailure(call: Call<ModelDeleteSlot>, t: Throwable) {
                count2++
                if (count2 <= 3) {
                    callAPIDelete(slotId)
                } else {
                    myToast(this@MySlot, "Something went wrong")
                }
                AppProgressBar.hideLoaderDialog()
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

        AppProgressBar.showLoaderDialog(context)


        ApiClient.apiService.deleteSlot(slotId).enqueue(object : Callback<ModelDeleteSlot> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call: Call<ModelDeleteSlot>,
                response: Response<ModelDeleteSlot>
            ) {
                try {
                    if (response.body()!!.status == 1) {
                        count3 = 0
                        myToast(this@MySlot, response.body()!!.message)
                        // myToast(requireActivity(),"No Data Found")
                        AppProgressBar.hideLoaderDialog()
                        apiCall(dayId)
                    } else {
                        myToast(this@MySlot, response.body()!!.message)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    myToast(this@MySlot, "Something went wrong")
                    AppProgressBar.hideLoaderDialog()
                }

            }

            override fun onFailure(call: Call<ModelDeleteSlot>, t: Throwable) {

                AppProgressBar.hideLoaderDialog()
                count3++
                if (count3 <= 3) {
                    updateSlotApi(slotId)
                } else {
                    myToast(this@MySlot, "Something went wrong")
                }
                AppProgressBar.hideLoaderDialog()

            }


        })
    }

    override fun activeASlot(dayCode: String, active: String) {
       AppProgressBar.showLoaderDialog(context)


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
                        AppProgressBar.hideLoaderDialog()
                    } else {
                        myToast(this@MySlot, response.body()!!.message)
                    }
                } catch (e: Exception) {
                    myToast(this@MySlot, "Something went wrong")
                    AppProgressBar.hideLoaderDialog()
                    e.printStackTrace()
                }
            }


            override fun onFailure(call: Call<ModelActive>, t: Throwable) {
                myToast(this@MySlot, "Something went wrong")
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

    override fun activeSlot(dayCode: String) {
        AppProgressBar.showLoaderDialog(context)

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
                            AppProgressBar.hideLoaderDialog()

                        } else {
                            myToast(this@MySlot, response.body()!!.message)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        myToast(this@MySlot, "Something went wrong")
                        AppProgressBar.hideLoaderDialog()

                    }
                }


                override fun onFailure(call: Call<ModelActive>, t: Throwable) {
                    myToast(this@MySlot, "Something went wrong")
                    AppProgressBar.hideLoaderDialog()

                }


            })
    }

    override fun inactiveSlot(dayCode: String) {

AppProgressBar.showLoaderDialog(context)
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
                            AppProgressBar.hideLoaderDialog()

                        } else {
                            myToast(this@MySlot, response.body()!!.message)
                            AppProgressBar.hideLoaderDialog()

                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        myToast(this@MySlot, "Something went wrong")
                        AppProgressBar.hideLoaderDialog()
                    }
                }


                override fun onFailure(call: Call<ModelActive>, t: Throwable) {
                    myToast(this@MySlot, "Something went wrong")
                    AppProgressBar.hideLoaderDialog()

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
