package com.example.ehcf_doctor.HealthCube.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ehcf_doctor.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AppToEdzxJava extends AppCompatActivity {
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
    String patientDataInHl7String = "{\"address\":[{\"country\":\"india\",\"type\":\"physical\",\"use\":\"home\"}],\"birthDate\":\"1986-04-19\",\"gender\":\"male\",\"identifier\":[{\"system\":\"iprd\",\"use\":\"usual\",\"value\":\"27912812780122\"}],\"name\":[{\"given\":[\"Aravind A\"]}],\"resourceType\":\"Patient\",\"telecom\":[{\"rank\":\"1\",\"system\":\"phone\",\"use\":\"mobile\",\"value\":\"9988779988\"}]}";

    // these tests will be highlighted in the Ezdx app
    String hightlightTests = "[\"PULSE_OXIMETER\",\"WEIGHT\",\"BLOOD_PRESSURE\",\"CHOLESTEROL\",\"CHIKUNGUNYA\",\"SYPHILIS\",\"LIPID_PROFILE\",\"HBA1C\"]";

    String sampleUserId = "+919246549300";  // TODO Add your user-id here sample id (+91XXXXXXXXXX). Get this from HC Admin ** DONT GIVE SPACE BETWEEN THE CODE AND PHONE NO. **
    String secretKey = "a1fc979a-03d6-4ca6-9196-d7192873d4a8";   // TODO Add your secret-key here. Get this from HC admin

    //    public static final String ezdxPackageName = "com.healthcubed.ezdx.ezdx.demo";  // pointing to demo environment, For your testing
    public static final String ezdxPackageName = "com.healthcubed.ezdx.ezdx";  // pointing to production environment, Your production app needs to point to this package

    Button btnlaunchEzdx;
    TextView tvResult;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_to_edzx_java);

        btnlaunchEzdx = findViewById(R.id.btn_launch);
        tvResult = findViewById(R.id.tv_result);


        // handling on click
        btnlaunchEzdx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {

                    Intent launcherIntent = getPackageManager().getLaunchIntentForPackage(ezdxPackageName);

                    if (launcherIntent != null) {

                        launcherIntent.setPackage(getPackageName());
                        launcherIntent.setAction(AppToEdzxJava.class.getCanonicalName());

                        if (patientDataInHl7String != null) {

                            launcherIntent.putExtra("PATIENT_DETAILS", patientDataInHl7String); // patient data in h7 format json string
                            launcherIntent.putExtra("TEST_DETAILS", hightlightTests); // list of testnames. pleae check R.string.testNames for all the test array Ezdx supports

                            if (sampleUserId != null && !sampleUserId.isEmpty() && secretKey != null && !secretKey.isEmpty()) { // login id and encrypted secret needs to sent every time
                                launcherIntent.putExtra("PARTNER_LOGIN_ID", sampleUserId); // user-id
                                launcherIntent.putExtra("PARTNER_LOGIN_SECRET", encrypt(secretKey, AppToEdzxJava.this));
                            }

                            startActivity(launcherIntent);
                        } else {
                            Toast.makeText(AppToEdzxJava.this, "Please enter correct data", Toast.LENGTH_SHORT).show();
                            return;
                        }

                    } else {

                        // this will launch playstore if app is not installed
                        launcherIntent = new Intent(Intent.ACTION_VIEW);
                        launcherIntent.setData(Uri.parse("market://details?id=" + ezdxPackageName));
                        startActivity(launcherIntent);

                    }

                } catch (Exception e) {
                    Toast.makeText(AppToEdzxJava.this, "Error launching ezdx " + e, Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        Toast.makeText(this, "OnNewIntent called", Toast.LENGTH_SHORT).show();

        // ** In case of ECG test, rendering entire byte array data of PDF report in TextView will go out of memory
        // this needs to be handled by dumping the byte array in a file
        tvResult.setText(intent.getStringExtra(ezdxPackageName + "RESULT"));  // result from Ezdx

    }

    // method to encrypt secret-key
    public static String encrypt(String secretKey, AppCompatActivity activity) throws Exception {

        byte[] srcBuff = secretKey.getBytes("UTF8");
        SecretKeySpec skeySpec = new
                SecretKeySpec((activity.getPackageName() + "ezdxandroid").substring(0, 16).getBytes(), "AES");
        IvParameterSpec ivSpec = new
                IvParameterSpec((activity.getPackageName() + "ezdxandroid").substring(0, 16).getBytes());
        Cipher ecipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
        ecipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivSpec);
        byte[] dstBuff = ecipher.doFinal(srcBuff);
        String base64 = Base64.encodeToString(dstBuff, Base64.DEFAULT);
        return base64;
    }


}