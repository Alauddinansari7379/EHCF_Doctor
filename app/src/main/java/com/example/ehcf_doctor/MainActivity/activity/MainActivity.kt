package com.example.ehcf_doctor.MainActivity.activity

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.PopupMenu
import android.widget.ToggleButton
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.ehcf.Helper.isOnline
import com.example.ehcf.Helper.myToast
import com.example.ehcf.sharedpreferences.SessionManager
import com.example.ehcf_doctor.Appointments.Appointments
import com.example.ehcf_doctor.Dashboard.Dashboard
import com.example.ehcf_doctor.Login.activity.SignIn
import com.example.ehcf_doctor.Login.modelResponse.LoginResponse
import com.example.ehcf_doctor.MainActivity.model.ModelOnline
import com.example.ehcf_doctor.ManageSlots.activity.ManageSlots
import com.example.ehcf_doctor.ManageSlots.activity.ManageSlotsSeassion
import com.example.ehcf_doctor.Profile.activity.ProfileSetting
import com.example.ehcf_doctor.R
import com.example.ehcf_doctor.databinding.ActivityMainBinding
import com.example.myrecyview.apiclient.ApiClient
import com.giphy.sdk.analytics.GiphyPingbacks.context
import com.google.android.material.navigation.NavigationView
import me.ibrahimsn.lib.SmoothBottomBar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import rezwan.pstu.cse12.youtubeonlinestatus.recievers.NetworkChangeReceiver
import xyz.teamgravity.checkinternet.CheckInternet

class MainActivity : AppCompatActivity() {
    private  var context:Context=this@MainActivity
    private lateinit var binding: ActivityMainBinding
    private lateinit var sessionManager: SessionManager
    lateinit var navController: NavController
    var progressDialog : ProgressDialog?=null
    lateinit var bottomNav: SmoothBottomBar
    var onlineid=1

    private lateinit var navigationView: NavigationView
    private lateinit var appBarConfiguration: AppBarConfiguration

    lateinit var drawerLayout: DrawerLayout
    @SuppressLint("UseCompatLoadingForDrawables", "SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sessionManager = SessionManager(this)


        bottomNav = binding.bottomNavigation1

        if (sessionManager.onlineStatus.toString().toInt()==1){
            binding.btnOnline.visibility=View.VISIBLE
            binding.btnOffline.visibility=View.GONE
        } else{
            binding.btnOnline.visibility=View.GONE
            binding.btnOffline.visibility=View.VISIBLE
        }

        binding.btnOnline.setOnClickListener {
            SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Are you sure want to Offline?")
                .setCancelText("No")
                .setConfirmText("Yes")
                .showCancelButton(true)
                .setConfirmClickListener { sDialog ->
                    sDialog.cancel()
                    apiCallOffline()


                }
                .setCancelClickListener { sDialog ->
                    sDialog.cancel()
                }
                .show()
        }
        binding.btnOffline.setOnClickListener {
            SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Are you sure want to Online?")
                .setCancelText("No")
                .setConfirmText("Yes")
                .showCancelButton(true)
                .setConfirmClickListener { sDialog ->
                    sDialog.cancel()
                    apiCallOnline()

                }
                .setCancelClickListener { sDialog ->
                    sDialog.cancel()
                }
                .show()
        }
        // binding.toggleBtn.setBackgroundColor(resources.getColor(R.color.Red))

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.hostFragment)
        val navController = navHostFragment!!.findNavController()
        val popupMenu = PopupMenu(this, null)
        popupMenu.inflate(R.menu.bootom_nav_menu)

        binding.bottomNavigation1.setupWithNavController(popupMenu.menu, navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            binding.tvTitle.text = when (destination.id) {
                R.id.fragment_Home -> "Home"
                R.id.fragment_booking -> "Booking"
                R.id.fragment_prescription -> "Prescription"
                else -> "Profile"
            }
        }
        binding.drawerClick.setOnClickListener {
            binding.drawerlayout1.openDrawer(GravityCompat.START)
            binding.includedrawar1.tvDashboard.setOnClickListener {
                startActivity(Intent(this, Dashboard::class.java))
                drawerLayout.closeDrawer(GravityCompat.START)
            }
            binding.includedrawar1.tvMySlots.setOnClickListener {
                startActivity(Intent(this, ManageSlotsSeassion::class.java))
                drawerLayout.closeDrawer(GravityCompat.START)
            }
            binding.includedrawar1.tvManageSlots.setOnClickListener {
                startActivity(Intent(this, ManageSlots::class.java))
                drawerLayout.closeDrawer(GravityCompat.START)
            }
            binding.includedrawar1.tvProfileSetting.setOnClickListener {
                startActivity(Intent(this, ProfileSetting::class.java))
                drawerLayout.closeDrawer(GravityCompat.START)
            }
            binding.includedrawar1.tvAppointments.setOnClickListener {
                startActivity(Intent(this, Appointments::class.java))
                drawerLayout.closeDrawer(GravityCompat.START)
            }
            binding.includedrawar1.tvLogOut.setOnClickListener {
                SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Are you sure want to logout?")
                    .setCancelText("No")
                    .setConfirmText("Yes")
                    .showCancelButton(true)
                    .setConfirmClickListener { sDialog ->
                        sDialog.cancel()
                        sessionManager.logout()
                        val intent = Intent(applicationContext, SignIn::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                        finish()
                        startActivity(intent)
                    }
                    .setCancelClickListener { sDialog ->
                        sDialog.cancel()
                    }
                    .show()
                drawerLayout.closeDrawer(GravityCompat.START)
            }
//            binding.includedrawar1.tvRefunds.setOnClickListener {
//                startActivity(Intent(this, AddNewFamily::class.java))
//                drawerLayout.closeDrawer(GravityCompat.START)
//            }

        }
//
//        navController = findNavController(R.id.hostFragment)
//        setupBottomNavigation()

        // navigationView = findViewById(R.id.navigationview1)
        // drawerLayout = findViewById(R.id.drawerlayout1)
//
//        val toggle = ActionBarDrawerToggle(this, drawerLayout, binding.toolBar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
//        toggle.drawerArrowDrawable.color = resources.getColor(R.color.white)
//        drawerLayout.addDrawerListener(toggle)
//        toggle.syncState()
//
//        navigationView.setNavigationItemSelectedListener(this)
//
//        val hView = navigationView.getHeaderView(0)

        drawerLayout = binding.drawerlayout1
        // For Navigation UP
        appBarConfiguration = AppBarConfiguration(navController.graph, drawerLayout)
        // NavigationUI.setupActionBarWithNavController(this,navController,drawerLayout)

        //   NavigationUI.setupWithNavController(binding.navigationview1,navController)


    }

    override fun onSupportNavigateUp(): Boolean {
        //return navController.navigateUp()
        return NavigationUI.navigateUp(navController, appBarConfiguration)

    }

private fun apiCallOnline(){
    progressDialog = ProgressDialog(this@MainActivity)
    progressDialog!!.setMessage("Loading..")
    progressDialog!!.setTitle("Please Wait")
    progressDialog!!.isIndeterminate = false
    progressDialog!!.setCancelable(true)
    progressDialog!!.show()
    ApiClient.apiService.changeOnlineStatus(sessionManager.id.toString(),"1",).enqueue(object :
        Callback<ModelOnline> {
        @SuppressLint("LogNotTimber")
        override fun onResponse(
            call: Call<ModelOnline>,
            response: Response<ModelOnline>
        ) {

            if (response.code()==200){
                myToast(this@MainActivity, "Now you are Online !")
                progressDialog!!.dismiss()
                sessionManager.onlineStatus ="1"
                binding.btnOnline.visibility=View.VISIBLE
                binding.btnOffline.visibility=View.GONE

            }else if (response.code()==500){
                myToast(this@MainActivity, "Server Error")
                progressDialog!!.dismiss()
            }

        }

        override fun onFailure(call: Call<ModelOnline>, t: Throwable) {
            myToast(this@MainActivity, "Something went wrong")
            progressDialog!!.dismiss()

        }

    })
}
private fun apiCallOffline(){
    progressDialog = ProgressDialog(this@MainActivity)
    progressDialog!!.setMessage("Loading..")
    progressDialog!!.setTitle("Please Wait")
    progressDialog!!.isIndeterminate = false
    progressDialog!!.setCancelable(true)
    progressDialog!!.show()

    ApiClient.apiService.changeOnlineStatus(sessionManager.id.toString(),"0",).enqueue(object :
        Callback<ModelOnline> {
        @SuppressLint("LogNotTimber")
        override fun onResponse(
            call: Call<ModelOnline>,
            response: Response<ModelOnline>
        )
        {
                if (response.code()==200) {
                    myToast(this@MainActivity, "Now you are Offline !")
                    progressDialog!!.dismiss()
                    sessionManager.onlineStatus = "0"
                    binding.btnOnline.visibility = View.GONE
                    binding.btnOffline.visibility = View.VISIBLE
                }
                    else if(response.code()==500)
                    {
                        myToast(this@MainActivity, "Server Error")
                        progressDialog!!.dismiss()

                    }
                    else{
                        myToast(this@MainActivity, "Something went wrong")
                        progressDialog!!.dismiss()
                    }



        }
        override fun onFailure(call: Call<ModelOnline>, t: Throwable) {
            myToast(this@MainActivity, "Something went wrong")
            progressDialog!!.dismiss()

        }

    })
}
    override fun onStart() {
        super.onStart()
        if (isOnline(context)){
            //  myToast(requireActivity(), "Connected")
        }else{
            val changeReceiver = NetworkChangeReceiver(context)
            changeReceiver.build()
            //  myToast(requireActivity(), "Not C")

        }
//        CheckInternet().check { connected ->
//            if (connected) {
//             //    myToast(requireActivity(),"Connected")
//            }
//            else {
//                val changeReceiver = NetworkChangeReceiver(context)
//                changeReceiver.build()
//                //  myToast(requireActivity(),"Check Internet")
//            }
//        }
    }


}