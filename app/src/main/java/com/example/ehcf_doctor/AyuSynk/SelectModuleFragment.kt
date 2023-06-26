package com.example.ehcf_doctor.AyuSynk

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import cn.pedant.SweetAlert.SweetAlertDialog
import com.ayudevice.ayusynksdk.AyuSynk
import com.example.ehcf_doctor.AyuSynk.NewUI.RecordHeartSound
import com.example.ehcf_doctor.R
import com.example.ehcf_doctor.databinding.FragmentSelectModuleBinding

class SelectModuleFragment : Fragment() {
    var binding: FragmentSelectModuleBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSelectModuleBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding!!.btnBle.setOnClickListener { v: View? ->
//            NavHostFragment.findNavController(this@SelectModuleFragment)
//                .navigate(R.id.action_SelectModuleFragment_to_BLEFragment)
            navigateToConnectScreen(this@SelectModuleFragment, view)
        }
        binding!!.btnUsb.setOnClickListener { v: View? ->

            NavHostFragment.findNavController(this@SelectModuleFragment)
                .navigate(R.id.action_SelectModuleFragment_to_USBFragment)
        }
        binding!!.imgBack.setOnClickListener {
            SweetAlertDialog(requireContext(), SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Are you want to exit ?")
                .setConfirmText("Yes")
                .setCancelText("No")
                .showCancelButton(true)
                .setConfirmClickListener { sDialog ->
                    sDialog.cancel()
                    startActivity(Intent(requireContext(),com.example.ehcf_doctor.MainActivity.activity.MainActivity::class.java))
                }
                .setCancelClickListener { sDialog ->
                    sDialog.cancel()
                }
                .show()
        }
     }
    fun navigateToConnectScreen(fragment: Fragment?, view: View?) {
        RecordHeartSound.FragmentValue.recordHeartSound ="1"
        // Check if Location services are on because they are required to make scanning work for SDK < 31
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
            if (checkLocationServices()) {
                if (!AyuSynk.getBleInstance().isAllBluetoothPermissionGranted) AyuSynk.getBleInstance()
                    .requestBluetoothPermission(
                        activity, 11
                    ) else {
                    NavHostFragment.findNavController(fragment!!)
                        .navigate(R.id.action_SelectModuleFragment_to_ConnectFragment)
                }
            }
        } else {
            if (!AyuSynk.getBleInstance().isAllBluetoothPermissionGranted) AyuSynk.getBleInstance()
                .requestBluetoothPermission(
                    activity, 11
                ) else {
                NavHostFragment.findNavController(fragment!!)
                    .navigate(R.id.action_SelectModuleFragment_to_ConnectFragment)
            }
        }
    }
    private fun checkLocationServices(): Boolean {
        return if (!AyuSynk.getBleInstance().isLocationEnabled) {
            AlertDialog.Builder(requireContext())
                .setTitle("Location services are not enabled")
                .setMessage("Scanning for Bluetooth peripherals requires locations services to be enabled.") // Want to enable?
                .setPositiveButton("Enable") { dialogInterface, i ->
                    dialogInterface.cancel()
                    startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                }
                .setNegativeButton("Cancel") { dialog, which -> // if this button is clicked, just close
                    // the dialog box and do nothing
                    dialog.cancel()
                }
                .create()
                .show()
            false
        } else {
            true
        }
    }

}