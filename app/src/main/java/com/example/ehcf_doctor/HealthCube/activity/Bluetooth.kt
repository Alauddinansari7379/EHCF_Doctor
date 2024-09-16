package com.example.ehcf_doctor.HealthCube.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.app.ProgressDialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.*
import android.util.Log
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.ehcf.Helper.currentDate
import com.example.ehcf.Helper.myToast
import com.example.ehcf.sharedpreferences.SessionManager
import com.example.ehcf_doctor.HealthCube.Model.ModelTotalCount
import com.example.ehcf_doctor.Helper.AppProgressBar
import com.example.ehcf_doctor.MyPatient.model.ModelMyPatient
import com.example.ehcf_doctor.R
import com.example.ehcf_doctor.databinding.ActivityMainBluethootBinding
import com.example.ehcf_doctor.Retrofit.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.util.*


class Bluetooth : AppCompatActivity() {
    private val TAG = Bluetooth::class.java.simpleName
    private var context: Context = this@Bluetooth
    private lateinit var sessionManager: SessionManager
    private lateinit var binding: ActivityMainBluethootBinding

    // GUI Components
    private var mBluetoothStatus: TextView? = null
    private var mReadBuffer: TextView? = null
    private var mScanBtn: Button? = null
    var dialog: Dialog? = null

    private var mOffBtn: Button? = null
    private var mListPairedDevicesBtn: Button? = null
    private var mDiscoverBtn: TextView? = null
    private var mDevicesListView: ListView? = null
    private var mDevicesListViewNew: ListView? = null
    private val mLED1: CheckBox? = null
    private var count = 0
    private var count2 = 0
    private var mBTAdapter: BluetoothAdapter? = null
    private var mPairedDevices: Set<BluetoothDevice>? = null
    private var mBTArrayAdapter: ArrayAdapter<String>? = null
    private var mBTArrayAdapterNew: ArrayAdapter<String>? = null
    private var mHandler // Our main handler that will receive callback notifications
            : Handler? = null
    private var mConnectedThread // bluetooth background worker thread to send and receive data
            : ConnectedThread? = null
    private var mBTSocket: BluetoothSocket? = null // bi-directional client-to-client data path
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBluethootBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sessionManager = SessionManager(this)

        mBluetoothStatus = findViewById<View>(R.id.bluetooth_status) as TextView
        mReadBuffer = findViewById<View>(R.id.read_buffer) as TextView
        mScanBtn = findViewById<View>(R.id.scan) as Button
        mOffBtn = findViewById<View>(R.id.off) as Button
        mDiscoverBtn = findViewById<View>(R.id.discover) as TextView
        mListPairedDevicesBtn = findViewById<View>(R.id.paired_btn) as Button
        mBTArrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1)
        mBTArrayAdapterNew = ArrayAdapter(this, android.R.layout.simple_list_item_1)
        mBTAdapter = BluetoothAdapter.getDefaultAdapter() // get a handle on the bluetooth radio
        mDevicesListView = findViewById<View>(R.id.devices_list_view) as ListView
        mDevicesListView!!.adapter = mBTArrayAdapter // assign model to view
        mDevicesListView!!.onItemClickListener = mDeviceClickListener

        binding.imgBack.setOnClickListener {
            onBackPressed()
        }

        binding.tvupdateDate.text = currentDate
        //   bluethootPermission()

//        binding.cardSearch.setOnClickListener {
//            val a = listOf("a", "b", "ab", "ba", "abc")
//            val b = a.distinctBy { it.length } // ["a", "ab", "abc"
//            Log.e("List",b.toString())
//        }
        apiCallMyPatient()
        apiCallTestCount()
        binding.cardRegister.setOnClickListener {
            startActivity(Intent(this@Bluetooth, AddPatient::class.java))
        }

        binding.cardExixting.setOnClickListener {
            startActivity(Intent(this@Bluetooth, ExistingPatientList::class.java))
        }

        binding.cardSearch.setOnClickListener {
            startActivity(Intent(this@Bluetooth, PatientList::class.java))
        }
//        mBluetoothStatus!!.setOnClickListener {
//            if (mBluetoothStatus!!.text == "Disconnect") {
//                refresh()
//                bluetoothOff()
//
//            }
//
//        }
        // btCancel()
//        mDevicesListViewNew!!.adapter = mBTArrayAdapterNew // assign model to view
//        mDevicesListViewNew!!.onItemClickListener = mDeviceClickListener
//
//        var view = layoutInflater.inflate(R.layout.comment_list_dialog, null)
//        dialog = Dialog(this)
//        val btnOkDialog = view!!.findViewById<Button>(R.id.btnOkDialogNew)
//        mDevicesListViewNew = view!!.findViewById<RecyclerView>(R.id.devices_list_view_new) as ListView
//        dialog = Dialog(this)
//        if (view.parent != null) {
//            (view.parent as ViewGroup).removeView(view) // <- fix
//        }
//        dialog!!.setContentView(view)
//        dialog?.setCancelable(true)
//        dialog?.show()
//        btnOkDialog.setOnClickListener {
//            dialog?.dismiss()
//        }
        //   checkBTPermissions()

//        if (!mBTAdapter!!.isEnabled) {
//            bluetoothOn()
//        } else {
//            //  Toast.makeText(applicationContext, getString(R.string.BTisON), Toast.LENGTH_SHORT).show()
//        }
//        // Ask for location permission if not already allowed
//        if (ContextCompat.checkSelfPermission(
//                this,
//                Manifest.permission.ACCESS_COARSE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED
//        ) ActivityCompat.requestPermissions(
//            this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), 1
//        )
//
//        mHandler = object : Handler(Looper.getMainLooper()) {
//            @SuppressLint("SuspiciousIndentation")
//            override fun handleMessage(msg: Message) {
//                if (msg.what == MESSAGE_READ) {
//                    var readMessage: String? = null
//                    readMessage = String((msg.obj as ByteArray), StandardCharsets.UTF_8)
//                    mReadBuffer!!.text = readMessage
//                }
//                if (msg.what == CONNECTING_STATUS) {
//                    var sConnected: CharArray
//                    if (msg.arg1 == 1) {
//                        mBluetoothStatus!!.text = getString(R.string.BTDisConnected)
//                        mBluetoothStatus!!.setTextColor(Color.parseColor("#F44336"))
//                        //+ msg.obj
//                        Log.e(" msg.obj", msg.obj.toString())
//
//                        binding.discover.visibility = View.GONE
//                        binding.devicesListView.visibility = View.GONE
//                        binding.tvDeviceName.text = msg.obj.toString()
//                        binding.tvSrNo.text = msg.obj.toString()
//                        binding.imgBlue.backgroundTintList = ColorStateList.valueOf(
//                            ContextCompat.getColor(
//                                this@Bluetooth,
//                                R.color.main_color
//                            )
//                        )
//                        binding.imgInfo.backgroundTintList = ColorStateList.valueOf(
//                            ContextCompat.getColor(
//                                this@Bluetooth,
//                                R.color.main_color
//                            )
//                        )
//                        binding.imgBell.backgroundTintList = ColorStateList.valueOf(
//                            ContextCompat.getColor(
//                                this@Bluetooth,
//                                R.color.main_color
//                            )
//                        )
//
//                    } else
//                    //mBluetoothStatus!!.text = getString(R.string.BTconnFail)
//                        myToast(this@Bluetooth, "${getString(R.string.BTconnFail)}")
//                }
//            }
//        }
//        if (mBTArrayAdapter == null) {
//            // Device does not support Bluetooth
//            mBluetoothStatus!!.text = getString(R.string.sBTstaNF)
//            Toast.makeText(applicationContext, getString(R.string.sBTdevNF), Toast.LENGTH_SHORT)
//                .show()
//        } else {
////            mLED1!!.setOnClickListener {
////                if (mConnectedThread != null) //First check to make sure thread created
////                    mConnectedThread!!.write("1")
////            }
//            mScanBtn!!.setOnClickListener {
//                bluetoothOn()
//            }
//            mOffBtn!!.setOnClickListener {
//                bluetoothOff()
//            }
//            mListPairedDevicesBtn!!.setOnClickListener {
//                listPairedDevices()
//            }
//            mDiscoverBtn!!.setOnClickListener {
//                discover()
//            }
//        }
    }

    private fun apiCallMyPatient() {
        AppProgressBar.showLoaderDialog(context)

        ApiClient.apiService.healthcubePatientList(sessionManager.id.toString())
            .enqueue(object : Callback<ModelMyPatient> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelMyPatient>, response: Response<ModelMyPatient>
                ) {
                    try {
                        if (response.code() == 500) {
                            AppProgressBar.hideLoaderDialog()

                        } else if (response.body()!!.result.isEmpty()) {
                            binding.tvTotal.text = response.body()!!.result.size.toString()
                            Log.e("Size", response.body()!!.result.size.toString())
                            // myToast(requireActivity(),"No Data Found")
                            AppProgressBar.hideLoaderDialog()

                        } else {
                            count = 0
                            binding.tvTotal.text = response.body()!!.result.size.toString()
                            Log.e("Size", response.body()!!.result.size.toString())
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }


                override fun onFailure(call: Call<ModelMyPatient>, t: Throwable) {
                    count++
                    if (count <= 3) {
                        apiCallMyPatient()
                    } else {
                        myToast(context as Activity, t.message.toString())
                    }
                    AppProgressBar.hideLoaderDialog()

                }

            })
    }

    private fun apiCallTestCount() {
        AppProgressBar.showLoaderDialog(context)

        ApiClient.apiService.healthcubeReportCount(sessionManager.id.toString())
            .enqueue(object : Callback<ModelTotalCount> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelTotalCount>, response: Response<ModelTotalCount>
                ) {
                    try {
                        if (response.code() == 500) {
                            AppProgressBar.hideLoaderDialog()

                        } else if (response.code() == 200) {
                            count2 = 0
                            binding.tvTotelTest.text = response.body()!!.result.toString()
                            // myToast(requireActivity(),"No Data Found")
                            AppProgressBar.hideLoaderDialog()

                        } else {
                            binding.tvTotelTest.text = response.body()!!.result.toString()
                            Log.e("Size", response.body()!!.result.toString())
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }


                override fun onFailure(call: Call<ModelTotalCount>, t: Throwable) {
                    count2++
                    if (count2 <= 3) {
                        apiCallTestCount()
                    } else {
                        myToast(context as Activity, t.message.toString())
                    }
                    AppProgressBar.hideLoaderDialog()
                }

            })
    }

    private var requestBluetooth =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                //granted
                bluetoothOn()
            } else {
                //deny
            }
        }
    private val requestMultiplePermissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            permissions.entries.forEach {
                Log.d("test006", "${it.key} = ${it.value}")
            }
        }

    private fun bluethootPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            requestMultiplePermissions.launch(
                arrayOf(
                    Manifest.permission.BLUETOOTH_SCAN,
                    Manifest.permission.BLUETOOTH_CONNECT
                )
            )
        } else {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            requestBluetooth.launch(enableBtIntent)
        }
    }

    private fun refresh() {
        overridePendingTransition(0, 0)
        finish()
        startActivity(intent)
        overridePendingTransition(0, 0)
    }

    @SuppressLint("MissingPermission")
    private fun bluetoothOn() {
        if (!mBTAdapter!!.isEnabled) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
            mBluetoothStatus!!.text = getString(R.string.BTEnable)
            Toast.makeText(applicationContext, getString(R.string.sBTturON), Toast.LENGTH_SHORT)
                .show()
        } else {
            Toast.makeText(applicationContext, getString(R.string.BTisON), Toast.LENGTH_SHORT)
                .show()
        }
    }

    // Enter here after user selects "yes" or "no" to enabling radio
    override fun onActivityResult(requestCode: Int, resultCode: Int, Data: Intent?) {
        // Check which request we're responding to
        super.onActivityResult(requestCode, resultCode, Data)
        if (requestCode == REQUEST_ENABLE_BT) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                // The user picked a contact.
                // The Intent's data Uri identifies which contact was selected.
                mBluetoothStatus!!.text = getString(R.string.sEnabled)
            } else mBluetoothStatus!!.text = getString(R.string.sDisabled)
        }
    }

    private fun bluetoothOff() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.BLUETOOTH_CONNECT
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            mBTAdapter!!.disable() // turn off
            mBluetoothStatus!!.text = getString(R.string.sBTdisabl)
            Toast.makeText(applicationContext, "Bluetooth turned Off", Toast.LENGTH_SHORT).show()
            return
        }
    }

    @SuppressLint("MissingPermission")
    private fun discover() {
        //mDevicesListViewNew = findViewById<View>(R.id.devices_list_view_new) as ListView
        binding.devicesListView.visibility = View.VISIBLE
        // Check if the device is already discovering
        if (mBTAdapter!!.isDiscovering) {
            mBTAdapter!!.cancelDiscovery()
            Toast.makeText(applicationContext, getString(R.string.DisStop), Toast.LENGTH_SHORT)
                .show()
        } else {
            if (mBTAdapter!!.isEnabled) {
                mBTArrayAdapter!!.clear() // clear items
                mBTAdapter!!.startDiscovery()
                Toast.makeText(applicationContext, getString(R.string.DisStart), Toast.LENGTH_SHORT)
                    .show()
                registerReceiver(blReceiver, IntentFilter(BluetoothDevice.ACTION_FOUND))
            } else {
                Toast.makeText(applicationContext, getString(R.string.BTnotOn), Toast.LENGTH_SHORT)
                    .show()
                bluetoothOn()
            }
        }
    }

    private val blReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        @SuppressLint("MissingPermission")
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            if (BluetoothDevice.ACTION_FOUND == action) {
                var device =
                    intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                // add the name to the list
                mBTArrayAdapter!!.add(""" ${device!!.name} ${device.address}""".trimIndent())
                mBTArrayAdapter!!.notifyDataSetChanged()
                //   mNewDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());


            }


//            if (action == BluetoothDevice.ACTION_FOUND) {
//                val device: BluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
//                mBTDevices.add(device)
//                Log.d(TAG, "onReceive: " + device.name + ": " + device.address)
//                mDeviceListAdapter = DeviceListAdapter(context, R.layout.device_adapter_view, mBTDevices)
//                lvNewDevices.setAdapter(mDeviceListAdapter)
//            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun listPairedDevices() {

        mBTArrayAdapter!!.clear()
        mPairedDevices = mBTAdapter!!.bondedDevices
        if (mBTAdapter!!.isEnabled) {
            // put it's one to the adapter
            for (device in (mPairedDevices as MutableSet<BluetoothDevice>?)!!) mBTArrayAdapter!!.add(
                device.name + "\n" + device.address
            )
            Toast.makeText(
                applicationContext,
                getString(R.string.show_paired_devices),
                Toast.LENGTH_SHORT
            ).show()
        } else Toast.makeText(applicationContext, getString(R.string.BTnotOn), Toast.LENGTH_SHORT)
            .show()


    }

    @SuppressLint("MissingPermission")
    private val mDeviceClickListener = OnItemClickListener { parent, view, position, id ->
        if (!mBTAdapter!!.isEnabled) {
            Toast.makeText(baseContext, getString(R.string.BTnotOn), Toast.LENGTH_SHORT).show()
            return@OnItemClickListener
        }
        mBluetoothStatus!!.text = getString(R.string.cConnet)
        // Get the device MAC address, which is the last 17 chars in the View
        val info = (view as TextView).text.toString()
        val address = info.substring(info.length - 17)
        val name = info.substring(0, info.length - 17)

        // Spawn a new thread to avoid blocking the GUI one
        object : Thread() {
            override fun run() {
                var fail = false
                val device = mBTAdapter!!.getRemoteDevice(address)
                try {
                    mBTSocket = createBluetoothSocket(device)
                } catch (e: IOException) {
                    fail = true
                    Toast.makeText(baseContext, getString(R.string.ErrSockCrea), Toast.LENGTH_SHORT)
                        .show()
                }
                // Establish the Bluetooth socket connection.
                try {
                    mBTSocket!!.connect()
                } catch (e: IOException) {
                    try {
                        fail = true
                        mBTSocket!!.close()
                        mHandler!!.obtainMessage(CONNECTING_STATUS, -1, -1)
                            .sendToTarget()
                    } catch (e2: IOException) {
                        //insert code to deal with this
                        Toast.makeText(
                            baseContext,
                            getString(R.string.ErrSockCrea),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                if (!fail) {
                    mConnectedThread = ConnectedThread(mBTSocket!!, mHandler!!)
                    mConnectedThread!!.start()
                    mHandler!!.obtainMessage(CONNECTING_STATUS, 1, -1, name)
                        .sendToTarget()
                }
            }
        }.start()
    }

    private fun checkBTPermissions() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            var permissionCheck = checkSelfPermission("Manifest.permission.ACCESS_FINE_LOCATION")
            permissionCheck += checkSelfPermission("Manifest.permission.ACCESS_COARSE_LOCATION")
            if (permissionCheck != 0) {
                requestPermissions(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ), 1001
                ) //Any number
            }
        } else {
            Log.d(TAG, "checkBTPermissions: No need to check permissions. SDK version < LOLLIPOP.")
        }
    }

    @SuppressLint("MissingPermission")
    @Throws(IOException::class)
    private fun createBluetoothSocket(device: BluetoothDevice): BluetoothSocket {
        try {
            val m = device.javaClass.getMethod(
                "createInsecureRfcommSocketToServiceRecord", UUID::class.java
            )
            return m.invoke(device, BT_MODULE_UUID) as BluetoothSocket
        } catch (e: Exception) {
            Log.e(TAG, "Could not create Insecure RFComm Connection", e)
        }
        return device.createRfcommSocketToServiceRecord(BT_MODULE_UUID)
    }

    companion object {
        private val BT_MODULE_UUID =
            UUID.fromString("00001101-0000-1000-8000-00805F9B34FB") // "random" unique identifier

        // #defines for identifying shared types between calling functions
        private const val REQUEST_ENABLE_BT = 1 // used to identify adding bluetooth names
        const val MESSAGE_READ = 2 // used in bluetooth handler to identify message update
        private const val CONNECTING_STATUS =
            3 // used in bluetooth handler to identify message status
    }
}