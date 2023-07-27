package com.example.ehcf_doctor.HealthCube.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ehcf_doctor.R
import com.example.ehcf_doctor.databinding.ActivityApptoedzxBinding
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class AppToEzdxNew : AppCompatActivity() {
    private lateinit var binding: ActivityApptoedzxBinding

    // ** This application is for development purpose only with sample user-id and secret-key **
    // TODO partner applicationId / package-name needs to be registered in Healthcubed. Give the app package-name to HC-Admin to register
    // TODO ** make sure to add below lines in activity manifest **
    // TODO android:launchMode="singleTask"
    // TODO android:exported="true"
    //Sample patient data in HL7 FHIR (v4.0.0) json string format
    // todo note
    //  **DO NOT CHANGE THE SYSTEM** i.e. 'iprd'
    //  **DONT SEND OTHER EXTRA INFORMATION
    //  **Replace with ur patient data, name and details
    var patientDataInHl7String: String? =
        "{\"address\":[{\"country\":\"india\",\"type\":\"physical\",\"use\":\"home\"}]," +
                "\"birthDate\":\"1986-04-19\",\"gender\":\"male\",\"identifier\":" +
                "[{\"system\":\"iprd\",\"use\":\"usual\",\"value\":\"27912812780122\"}],\"name\"" +
                ":[{\"given\":[\"Alauddin Ansari\"]}],\"resourceType\":\"Patient\",\"telecom\":[{\"rank\":\"1\"," +
                "\"system\":\"phone\",\"use\":\"mobile\",\"value\":\"7379452259\"}]}"

    // these tests will be highlighted in the Ezdx app
    var hightlightTests = "[\"PULSE_OXIMETER\",\"WEIGHT\",\"BLOOD_PRESSURE\",\"ECG\",\"TEMPERATURE\"]"

   // "[\"PULSE_OXIMETER\",\"WEIGHT\",\"BLOOD_PRESSURE\",\"CHOLESTEROL\",\"CHIKUNGUNYA\",\"SYPHILIS\",\"TEMPERATURE\",\"HBA1C\"]"
    var sampleUserId: String? =
        "+919392595905" // TODO Add your user-id here sample id (+91XXXXXXXXXX). Get this from HC Admin ** DONT GIVE SPACE BETWEEN THE CODE AND PHONE NO. **
    var secretKey: String? = "a1fc979a-03d6-4ca6-9196-d7192873d4a8" // TODO Add your secret-key here. Get this from HC admin
    var client_id: String? = "b96214a0-2d05-4e65-be92-b83852227733"

    //  public static final String ezdxPackageName = "com.healthcubed.ezdx.ezdx";  // pointing to production environment, Your production app needs to point to this package
    var btnlaunchEzdx: Button? = null
    var tvResult: TextView? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityApptoedzxBinding.inflate(layoutInflater)
        setContentView(binding.root)

        btnlaunchEzdx = findViewById(R.id.btn_launch)
        tvResult = findViewById(R.id.tv_result)


        // handling on click
        binding.btnLaunch.setOnClickListener(View.OnClickListener {

            try {
                var launcherIntent = packageManager.getLaunchIntentForPackage(ezdxPackageNameNew)
                if (launcherIntent != null) {
                    launcherIntent.setPackage(packageName)
                    launcherIntent.action = AppToEzdxNew::class.java.canonicalName
                    if (patientDataInHl7String != null) {
                        launcherIntent.putExtra("PATIENT_DETAILS", patientDataInHl7String) // patient data in h7 format json string
                        launcherIntent.putExtra("TEST_DETAILS", hightlightTests) // list of testnames. pleae check R.string.testNames for all the test array Ezdx supports
                        if (sampleUserId != null && sampleUserId!!.isNotEmpty() && secretKey != null && secretKey!!.isNotEmpty()) { // login id and encrypted secret needs to sent every time
                            launcherIntent.putExtra("PARTNER_LOGIN_ID", sampleUserId) // user-id
                            launcherIntent.putExtra("PARTNER_LOGIN_SECRET", encrypt(secretKey!!, this@AppToEzdxNew))
                        }
                        startActivity(launcherIntent)
                    } else {
                        Toast.makeText(this@AppToEzdxNew, "Please enter correct data", Toast.LENGTH_SHORT).show()
                        return@OnClickListener
                    }
                } else {

                    // this will launch playstore if app is not installed
                    launcherIntent = Intent(Intent.ACTION_VIEW)
                    launcherIntent.data = Uri.parse("market://details?id=$ezdxPackageNameNew")
                    startActivity(launcherIntent)
                }
            } catch (e: Exception) {
                Toast.makeText(this@AppToEzdxNew, "Error launching ezdx $e", Toast.LENGTH_SHORT).show()
            }
        })
    }

//    override fun onNewIntent(intent: Intent) {
//        super.onNewIntent(intent)
//        Toast.makeText(this, "OnNewIntent called", Toast.LENGTH_SHORT).show()
//
//        Log.e("Result",intent.getStringExtra(ezdxPackageNameNew + "RESULT").toString())
//
//        // ** In case of ECG test, rendering entire byte array data of PDF report in TextView will go out of memory
//        // this needs to be handled by dumping the byte array in a file
//        tvResult!!.text = intent.getStringExtra(ezdxPackageNameNew + "RESULT") // result from Ezdx
//    }

    companion object {
        const val ezdxPackageNameNew = "com.healthcubed.ezdx.ezdx.demo" // pointing to demo environment, For your testing

        // method to encrypt secret-key
        @Throws(Exception::class)
        fun encrypt(secretKey: String, activity: AppCompatActivity): String {
            val srcBuff = secretKey.toByteArray(charset("UTF8"))
            val skeySpec = SecretKeySpec((activity.packageName + "ezdxandroid").substring(0, 16).toByteArray(), "AES")
            val ivSpec = IvParameterSpec((activity.packageName + "ezdxandroid").substring(0, 16).toByteArray())
            val ecipher = Cipher.getInstance("AES/CBC/PKCS7Padding")
            ecipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivSpec)
            val dstBuff = ecipher.doFinal(srcBuff)
            return Base64.encodeToString(dstBuff, Base64.DEFAULT)
        }
    }
}