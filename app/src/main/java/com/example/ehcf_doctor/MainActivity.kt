package com.example.ehcf_doctor

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.PopupMenu
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.ehcf.sharedpreferences.SessionManager
import com.example.ehcf_doctor.Appointments.Appointments
import com.example.ehcf_doctor.Dashboard.Dashboard
import com.example.ehcf_doctor.Login.activity.SignIn
import com.example.ehcf_doctor.ManageSlots.ManageSlotsSeassion
import com.example.ehcf_doctor.MySlots.MySlotsTimingsNew
import com.example.ehcf_doctor.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView
import me.ibrahimsn.lib.SmoothBottomBar

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var sessionManager: SessionManager
    lateinit var navController: NavController

    lateinit var bottomNav: SmoothBottomBar

    private lateinit var navigationView: NavigationView
    private lateinit var appBarConfiguration: AppBarConfiguration

    lateinit var drawerLayout: DrawerLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sessionManager = SessionManager(this)


        bottomNav = binding.bottomNavigation1

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
                startActivity(Intent(this, MySlotsTimingsNew::class.java))
                drawerLayout.closeDrawer(GravityCompat.START)
            }
            binding.includedrawar1.tvManageSlots.setOnClickListener {
                startActivity(Intent(this, ManageSlotsSeassion::class.java))
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
                        intent.flags =
                            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
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

//    private fun setupBottomNavigation() {
//        bottomNav.setupWithNavController(navController)
//    }

}