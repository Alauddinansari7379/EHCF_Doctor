package com.example.ehcf_doctor.MainActivity.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
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
import com.example.ehcf_doctor.AudioRecording.Fragment.RecordListFragment
import com.example.ehcf_doctor.AyuSynk.MainActivity
import com.example.ehcf_doctor.Dashboard.Dashboard
import com.example.ehcf_doctor.HealthCube.activity.Bluetooth
import com.example.ehcf_doctor.Helper.CustomDatepickerdemo
import com.example.ehcf_doctor.Invoice.Invoice
import com.example.ehcf_doctor.Login.activity.SignIn
import com.example.ehcf_doctor.MainActivity.model.ModelOnline
import com.example.ehcf_doctor.ManageSlots.activity.CreateSlot
import com.example.ehcf_doctor.ManageSlots.activity.MySlot
 import com.example.ehcf_doctor.MyPatient.activity.MyPatient
import com.example.ehcf_doctor.Prescription.activity.PrescriptionMainActivity
import com.example.ehcf_doctor.PrivacyTerms.PrivacyTerms
import com.example.ehcf_doctor.Profile.activity.ProfileSetting
import com.example.ehcf_doctor.R
import com.example.ehcf_doctor.ResetPassword
import com.example.ehcf_doctor.databinding.ActivityMainDoctorBinding
import com.example.myrecyview.apiclient.ApiClient
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import me.ibrahimsn.lib.SmoothBottomBar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import rezwan.pstu.cse12.youtubeonlinestatus.recievers.NetworkChangeReceiver


class MainActivity : AppCompatActivity() {
    private  var context:Context=this@MainActivity
    private lateinit var binding: ActivityMainDoctorBinding
    private lateinit var sessionManager: SessionManager
    lateinit var navController: NavController
    var progressDialog : ProgressDialog?=null
    lateinit var bottomNav: SmoothBottomBar
    var onlineid=1
    private lateinit var navigationView: NavigationView
    private lateinit var appBarConfiguration: AppBarConfiguration
    lateinit var drawerLayout: DrawerLayout

    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    private val notificationManager: NotificationManager by lazy {
        getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    @SuppressLint("UseCompatLoadingForDrawables", "SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainDoctorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sessionManager = SessionManager(this)
        bottomNav = binding.bottomNavigation1




        requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            refreshUI()
            if (it) {
              //  myToast(this@MainActivity,"Permission Granted")
                //  showDummyNotification()
            } else {
                Snackbar.make(
                    findViewById<View>(android.R.id.content).rootView, "Please grant Notification permission from App Settings",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }


        if (sessionManager.pricing.isNullOrEmpty()) {
            val d=  SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
            d.titleText = "Please Update Profile!"
            d.confirmText = "Ok"
            //  .setCancelable(false)
            // .setCanceledOnTouchOutside(false)
            d.setConfirmClickListener { obj: SweetAlertDialog ->
                obj.dismiss()
                finish()
                startActivity(intent)
                startActivity(Intent(this,ProfileSetting::class.java))
            }
            d.setCancelable(false)
            d.show()
        }


        if (sessionManager.onlineStatus.toString().toInt()==1){
            binding.btnOnline.visibility=View.VISIBLE
            binding.btnOffline.visibility=View.GONE
        } else{
            binding.btnOnline.visibility=View.GONE
            binding.btnOffline.visibility=View.VISIBLE
        }

        binding.tvTitle.setOnClickListener {
            startActivity(Intent(this@MainActivity, CustomDatepickerdemo::class.java))

        }

//        val refreshListener = SwipeRefreshLayout.OnRefreshListener {
//            overridePendingTransition(0, 0)
//            finish()
//            startActivity(intent)
//            overridePendingTransition(0, 0)
//        }
//        binding.swipeRefreshLayout.setOnRefreshListener(refreshListener)



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
                //myToast(this@MainActivity,"Work On Progress")
                startActivity(Intent(this, Dashboard::class.java))
                drawerLayout.closeDrawer(GravityCompat.START)
            }
            binding.includedrawar1.tvMySlots.setOnClickListener {
                startActivity(Intent(this, MySlot::class.java))
                drawerLayout.closeDrawer(GravityCompat.START)
            }
            binding.includedrawar1.tvManageSlots.setOnClickListener {
               // startActivity(Intent(this, ManageSlots::class.java))
                startActivity(Intent(this, CreateSlot::class.java))
                drawerLayout.closeDrawer(GravityCompat.START)
            }
            binding.includedrawar1.tvProfileSetting.setOnClickListener {
                startActivity(Intent(this, ProfileSetting::class.java))
                drawerLayout.closeDrawer(GravityCompat.START)
            }

            binding.includedrawar1.tvAyuSynk.setOnClickListener {
                startActivity(Intent(this, MainActivity::class.java))
                drawerLayout.closeDrawer(GravityCompat.START)
            }

            binding.includedrawar1.tvHealthCube.setOnClickListener {
                startActivity(Intent(this, Bluetooth::class.java))
                drawerLayout.closeDrawer(GravityCompat.START)
            }

            binding.includedrawar1.tvRecordList.setOnClickListener {
                startActivity(Intent(this, RecordListFragment::class.java))
                drawerLayout.closeDrawer(GravityCompat.START)
            }
            binding.includedrawar1.tvAppointments.setOnClickListener {
                startActivity(Intent(this, Appointments::class.java))
                drawerLayout.closeDrawer(GravityCompat.START)
            }
            binding.includedrawar1.tvPrescripition.setOnClickListener {
                startActivity(Intent(this, PrescriptionMainActivity::class.java))
                drawerLayout.closeDrawer(GravityCompat.START)
            }
            binding.includedrawar1.tvMyPatients.setOnClickListener {
                startActivity(Intent(this, MyPatient::class.java))
                drawerLayout.closeDrawer(GravityCompat.START)
            }
            binding.includedrawar1.tvInvoice.setOnClickListener {
                startActivity(Intent(this, Invoice::class.java))
                drawerLayout.closeDrawer(GravityCompat.START)
            }
            binding.includedrawar1.tvPrivacyTerms.setOnClickListener {
                startActivity(Intent(this, PrivacyTerms::class.java))
                drawerLayout.closeDrawer(GravityCompat.START)
            }

            binding.includedrawar1.tvPasswordChange.setOnClickListener {
                val intent = Intent(context as Activity, ResetPassword::class.java)
                intent.putExtra("id", sessionManager.id.toString())
                context.startActivity(intent)
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

    @SuppressLint("MissingPermission")
    private fun showDummyNotification() {
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Congratulations! 🎉🎉🎉")
            .setContentText("Notification Enabled in the Doctor App")
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        with(NotificationManagerCompat.from(this)) {
            notify(1, builder.build())
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            "Important Notification Channel",
            NotificationManager.IMPORTANCE_HIGH,
        ).apply {
            description = "This notification contains important announcement, etc."
        }
        notificationManager.createNotificationChannel(channel)
    }
    companion object {
        const val CHANNEL_ID = "dummy_channel"
    }
    @RequiresApi(Build.VERSION_CODES.N)
    private fun refreshUI() {
             if (notificationManager.areNotificationsEnabled()) "TRUE" else "FALSE"
    }

    var doubleBackToExitPressedOnce = false

    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }
        doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Please click Back again to exit", Toast.LENGTH_SHORT).show()
        Handler(Looper.getMainLooper()).postDelayed(Runnable {
            doubleBackToExitPressedOnce = false
        }, 2000)
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
    ApiClient.apiService.changeOnlineStatus(sessionManager.id.toString(), "1").enqueue(object :
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

    fun refreshMain() {
        val intent = Intent(this@MainActivity, com.example.ehcf_doctor.MainActivity.activity.MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        finish()
        startActivity(intent)
    }

    private fun apiCallOffline(){
    progressDialog = ProgressDialog(this@MainActivity)
    progressDialog!!.setMessage("Loading..")
    progressDialog!!.setTitle("Please Wait")
    progressDialog!!.isIndeterminate = false
    progressDialog!!.setCancelable(true)
    progressDialog!!.show()

    ApiClient.apiService.changeOnlineStatus(sessionManager.id.toString(), "0").enqueue(object :
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