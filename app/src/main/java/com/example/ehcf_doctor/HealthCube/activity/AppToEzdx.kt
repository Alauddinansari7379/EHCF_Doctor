package com.example.ehcf_doctor.HealthCube.activity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.ehcf_doctor.databinding.ActivityAppToEzdxBinding
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

class AppToEzdx : AppCompatActivity() {
    var binding: ActivityAppToEzdxBinding? = null
    var secretKey = "a1fc979a-03d6-4ca6-9196-d7192873d4a8"
    var client_id = "b96214a0-2d05-4e65-be92-b83852227733"
    private val testList = arrayOf("WEIGHT", "BLOOD_PRESSURE")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAppToEzdxBinding.inflate(layoutInflater)
        val view: View = binding!!.root
        setContentView(view)
        binding!!.btnMove.setOnClickListener {
            try {
                 moveToEzdx()
            } catch (e: Exception) {
                throw RuntimeException(e)
            }
        }
    }

    var patientData = """ {"identifier":[{"system":"iprd","use":"usual","value":"24643-9246-1"}],"resourceType":"Patient","addr
        ess":[{"country":"India","type":"physical","use":"home"}],"birthDate":"2000-02-
        22","gender":"MALE","name":[{"given":["Alauddin
        doe"]}],"telecom":[{"rank":1,"system":"phone","use":"mobile","value":"+91 7379452259"}]}
        """.trimIndent()

    @Throws(Exception::class)
    private fun moveToEzdx() {
        var launcherIntent = packageManager.getLaunchIntentForPackage("com.healthcubed.ezdx.ezdx.demo")
        Log.e("launcherIntent",launcherIntent.toString())
        if (launcherIntent != null) {
            launcherIntent.setPackage(packageName)
            launcherIntent.action = AppToEzdx::class.java.canonicalName
            // patient data in h7 format json string (Refer section 3)
            launcherIntent.putExtra("PATIENT_DETAILS", patientData)
            // list of testnames string (Refer section 5)
           // launcherIntent.putExtra("TEST_DETAILS", ObjectMapper().writeValueAsString(testList))
             launcherIntent.putExtra("TEST_DETAILS",testList);
// login id and encrypted secret-key needs to sent every time (Refer section 4)
            // launcherIntent.putExtra("PARTNER_LOGIN_ID", "9246549300");
            launcherIntent.putExtra("PARTNER_LOGIN_ID", "9392595905")
            launcherIntent.putExtra("PARTNER_LOGIN_SECRET", encrypt(secretKey, this@AppToEzdx))
            startActivity(launcherIntent)
        } else {
            launcherIntent = Intent(Intent.ACTION_VIEW)
            launcherIntent.data = Uri.parse("market://details?id=" + "com.healthcubed.ezdx.ezdx.demo")
            startActivity(launcherIntent)
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        val result = intent.getStringExtra("RESULT")
        Log.e("RESULT", result!!)
    }

    companion object {
        @Throws(Exception::class)
        fun encrypt(secretKey: String, activity: Activity): String {
            val srcBuff = secretKey.toByteArray(charset("UTF8"))
            val skeySpec = SecretKeySpec((activity.packageName + "com.healthcubed.ezdx.ezdx.demo").substring(0, 16).toByteArray(), "AES")
            val ivSpec = IvParameterSpec((activity.packageName + "com.healthcubed.ezdx.ezdx.demo").substring(0,16).toByteArray())
            val ecipher = Cipher.getInstance("AES/CBC/PKCS7Padding")
            ecipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivSpec)
            val dstBuff = ecipher.doFinal(srcBuff)
            Log.e("encrypt", dstBuff.toString()+Base64.DEFAULT)

            return Base64.encodeToString(dstBuff, Base64.DEFAULT)

        }
    }


}