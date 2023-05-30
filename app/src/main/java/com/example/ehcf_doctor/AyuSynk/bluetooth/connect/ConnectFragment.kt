package com.example.ehcf_doctor.AyuSynk.bluetooth.connect

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.ayudevice.ayusynksdk.AyuSynk
import com.ayudevice.ayusynksdk.ble.Device
import com.ayudevice.ayusynksdk.ble.constants.DeviceConnectionState
import com.ayudevice.ayusynksdk.ble.constants.DeviceStrength
import com.ayudevice.ayusynksdk.ble.listener.AyuDeviceListener
import com.ayudevice.ayusynksdk.ble.listener.DeviceScanListener
import com.example.ehcf.Helper.myToast
import com.example.ehcf_doctor.AyuSynk.bluetooth.connect.DeviceListAdapter.OnListInteractionListener
import com.example.ehcf_doctor.R
import com.example.ehcf_doctor.databinding.FragmentConnectBinding

class ConnectFragment : Fragment(), DeviceScanListener, OnListInteractionListener {
    private var binding: FragmentConnectBinding? = null
    private var deviceListAdapter: DeviceListAdapter? = null
    var navController: NavController? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentConnectBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        deviceListAdapter = DeviceListAdapter(context, this)
        binding!!.deviceList.adapter = deviceListAdapter
        AyuSynk.getBleInstance().startScan(this)
        AyuSynk.getBleInstance().setDeviceScanListener(this)
        AyuSynk.getBleInstance().setAyuDeviceListener(object : AyuDeviceListener {
            override fun deviceConnectionStrength(strength: DeviceStrength) {}
            override fun deviceConnectionState(state: DeviceConnectionState) {
                if (state == DeviceConnectionState.DEVICE_CONNECTED) {
                    myToast(requireActivity(),"Device Connected")

                    NavHostFragment.findNavController(this@ConnectFragment).navigate(R.id.BLEFragment)

                    //   if (activity != null) activity!!.onBackPressed()

//                    navController!!.navigate(R.id.mainblu)
//                    //  handler.postDelayed(() ->
//                    //  handler.postDelayed(() ->
//                    navController.navigate(R.id.reportFragment, null, getNavOptions())

//                    val intent = Intent(context as Activity, MainActivity::class.java)
//                    (context as Activity).startActivity(intent)
                }
            }

            override fun deviceBatteryUpdate(batteryUpdate: Int) {}
        })

        binding!!.imgBack.setOnClickListener {
            NavHostFragment.findNavController(this@ConnectFragment).navigate(R.id.BLEFragment)

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        AyuSynk.getBleInstance().stopScan()
        binding = null
    }

    override fun onScanStart() {}
    override fun onDeviceFound(device: Device) {
        if (activity != null && !requireActivity().isFinishing) {
            requireActivity().runOnUiThread {
                binding!!.progressBar.visibility = View.GONE
                binding!!.deviceList.visibility = View.VISIBLE
                deviceListAdapter!!.addDevice(DeviceListObject(device.name, device.address))
            }
        }
    }

    override fun onScanFinish() {}
    override fun onScanFailed(state: Int) {}
    override fun onBLEConnectClicked(device: DeviceListObject) {
        AyuSynk.getBleInstance().connect(device.bleAddress)
    }
}