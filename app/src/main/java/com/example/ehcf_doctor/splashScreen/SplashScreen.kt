package com.example.ehcf_doctor.splashScreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import com.example.ehcf_doctor.Login.activity.SignIn
import com.example.ehcf_doctor.MainActivity.activity.MainActivity
import com.example.ehcf_doctor.R
import com.example.ehcf_doctor.databinding.ActivitySplashScreenBinding

class SplashScreen : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // This is used to hide the status bar and make
        // the splash screen as a full screen activity.
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        normalLaunch()
    }


    private fun normalLaunch() {

        binding.animationView.setAnimation(R.raw.doctoranimation)
        binding.animationView.animate().startDelay = 3000
        binding.animationView.playAnimation()
        Handler().postDelayed({
            startActivity(Intent(this@SplashScreen, SignIn::class.java))
            finish()
        }, 3000)
    }
}