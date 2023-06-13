package com.example.ehcf_doctor.AyuSynk.bluetooth

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.ayudevice.ayusynksdk.AyuSynk
import com.ayudevice.ayusynksdk.ble.constants.DeviceConnectionState
import com.ayudevice.ayusynksdk.ble.constants.DeviceStrength
import com.ayudevice.ayusynksdk.ble.listener.AyuDeviceListener
import com.ayudevice.ayusynksdk.denoise.constants.FilterType
import com.ayudevice.ayusynksdk.onlineLiveStreaming.OnlineLiveStreamListener
import com.ayudevice.ayusynksdk.playback.AyuFileGenerator
import com.ayudevice.ayusynksdk.playback.listener.RecorderListener
import com.ayudevice.ayusynksdk.report.SoundData
import com.ayudevice.ayusynksdk.report.SoundFile
import com.ayudevice.ayusynksdk.report.constants.LocationType
import com.ayudevice.ayusynksdk.report.constants.SoundType
import com.ayudevice.ayusynksdk.report.listener.DiagnosisReportUpdateListener
import com.ayudevice.ayusynksdk.utils.logs.AyuLogsListener
import com.example.ehcf_doctor.AyuSynk.utils.GenUtil
import com.example.ehcf_doctor.R
import com.example.ehcf_doctor.databinding.FragmentMainBinding
import java.io.File
import java.io.IOException
import java.util.*

class MainBleFragment : Fragment(), AyuDeviceListener, AdapterView.OnItemSelectedListener,
    View.OnClickListener, RecorderListener, OnlineLiveStreamListener, DiagnosisReportUpdateListener,
    AyuLogsListener {
    private  var binding: FragmentMainBinding?=null
    private var lastRecordedData: ShortArray? = null
    private var recordID = -1
    private var isRecordingPaused = false
    val usb=1
    private var isPlayingRecordedSound = false
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.filters_array, android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding!!.spinnerFilter.adapter = adapter
        binding!!.spinnerFilter.onItemSelectedListener = this
        AyuSynk.getBleInstance().setAyuVisualizerView(binding!!.ayuVisualizerView)
        AyuSynk.getBleInstance().setRecordingTimeLimit(10)

        binding!!.imgBack.setOnClickListener {
            NavHostFragment.findNavController(this@MainBleFragment).navigate(R.id.SelectModuleFragment)

        }
        if (usb==1){
            binding!!.appCompatTextView2.text="Bluetooth"
        }else{
            binding!!.appCompatTextView2.text="USB"

        }

        //  AyuSynk.getBleInstance().showLogs(false);
        binding!!.btnConnect.setOnClickListener { view1: View? ->
            if (binding!!.btnConnect.tag as Int == 2) AyuSynk.getBleInstance()
                .disconnect() else navigateToConnectScreen(this@MainBleFragment, view)
        }
        binding!!.btnRecord.setOnClickListener(this)
        binding!!.btnPause.setOnClickListener(this)
        binding!!.btnPlay1x.setOnClickListener(this)
        binding!!.btnShare.setOnClickListener(this)
        binding!!.btnReport.setOnClickListener(this)
        binding!!.btnReportShare.setOnClickListener(this)
        binding!!.btnOnlineStream.text = getString(R.string.onlineLiveText, "Start")
        binding!!.btnOnlineStream.setOnClickListener(this)
        binding!!.btnShareUrl.setOnClickListener(this)
        binding!!.notifications.movementMethod = ScrollingMovementMethod()
        AyuSynk.getBleInstance().setOnlineStreamerListener(this)
        AyuSynk.getBleInstance().setLogsListener(this)
        AyuSynk.getBleInstance().showLogs(true)
        binding!!.btnLogs.text = getString(R.string.logsText, "Disable")
        binding!!.btnLogs.setOnClickListener(this)
        binding!!.btnShareLogs.setOnClickListener(this)
        binding!!.btnUpdateDevice.setOnClickListener(this)
    }

    fun navigateToConnectScreen(fragment: Fragment?, view: View?) {
        // Check if Location services are on because they are required to make scanning work for SDK < 31
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
            if (checkLocationServices()) {
                if (!AyuSynk.getBleInstance().isAllBluetoothPermissionGranted) AyuSynk.getBleInstance()
                    .requestBluetoothPermission(
                        activity, 11
                    ) else {
                    NavHostFragment.findNavController(fragment!!)
                        .navigate(R.id.action_BLEFragment_to_ConnectFragment)
                }
            }
        } else {
            if (!AyuSynk.getBleInstance().isAllBluetoothPermissionGranted) AyuSynk.getBleInstance()
                .requestBluetoothPermission(
                    activity, 11
                ) else {
                NavHostFragment.findNavController(fragment!!)
                    .navigate(R.id.action_BLEFragment_to_ConnectFragment)
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

    fun navigateToOTAScreen(fragment: Fragment?) {
        if (AyuSynk.getBleInstance().isDeviceConnected == DeviceConnectionState.DEVICE_CONNECTED) {
            NavHostFragment.findNavController(fragment!!)
                .navigate(R.id.action_BLEFragment_to_OTAUpdateFragment)
        }
    }

    override fun onResume() {
        super.onResume()
        AyuSynk.getBleInstance().setAyuDeviceListener(this)
        AyuSynk.getBleInstance().setRecorderListener(this)
        AyuSynk.getBleInstance().setDiagnosisReportUpdateListener(this)
        if (AyuSynk.getBleInstance().isDeviceConnected == DeviceConnectionState.DEVICE_CONNECTED) {
            onDeviceConnected()
        } else {
            onDeviceDisconnected()
        }
    }

    override fun onPause() {
        super.onPause()
        AyuSynk.getBleInstance().setAyuDeviceListener(null)
        AyuSynk.getBleInstance().setRecorderListener(null)
        AyuSynk.getBleInstance().setDiagnosisReportUpdateListener(null)
    }

    override fun deviceConnectionStrength(strength: DeviceStrength) {
        setDeviceStrength(strength)
    }

    override fun deviceConnectionState(state: DeviceConnectionState) {
        if (state != DeviceConnectionState.DEVICE_CONNECTED) {
            onDeviceDisconnected()
        } else {
            onDeviceConnected()
        }
    }

    override fun deviceBatteryUpdate(batteryUpdate: Int) {
        if (binding != null) {
            if (binding!!.customBatteryMeter.visibility != View.VISIBLE) binding!!.customBatteryMeter.visibility =
                View.VISIBLE
            binding!!.customBatteryMeter.setBatteryLevel(batteryUpdate)
        }
    }

    private fun setDeviceStrength(strength: DeviceStrength) {
        if (binding != null) {
            if (strength == DeviceStrength.DEVICE_SIGNAL_WEAK) binding!!.deviceStrength.text =
                getString(R.string.device_strength, "weak") else binding!!.deviceStrength.text =
                getString(R.string.device_strength, "strong")
        }
    }

    private fun onDeviceConnected() {
        setDeviceStrength(AyuSynk.getBleInstance().deviceStrength)
        deviceBatteryUpdate(AyuSynk.getBleInstance().currentBatteryLevel)
        binding!!.btnRecord.isEnabled = true
        binding!!.btnOnlineStream.isEnabled = true
        binding!!.btnUpdateDevice.isEnabled = true
        binding!!.customBatteryMeter.visibility = View.VISIBLE
        binding!!.deviceState.setText(R.string.device_connected)
        binding!!.deviceStrength.visibility = View.VISIBLE
        binding!!.btnConnect.setText(R.string.disconnect)
        binding!!.btnConnect.tag = 2
    }

    private fun onDeviceDisconnected() {
        binding!!.btnRecord.isEnabled = false
        binding!!.btnOnlineStream.isEnabled = false
        binding!!.btnUpdateDevice.isEnabled = false
        binding!!.customBatteryMeter.visibility = View.INVISIBLE
        binding!!.deviceState.setText(R.string.device_disconnected)
        binding!!.deviceStrength.visibility = View.INVISIBLE
        binding!!.btnConnect.setText(R.string.connect)
        binding!!.btnConnect.tag = 1
    }

    private fun setUiForRecording() {
        if (!isRecordingPaused) binding!!.ayuVisualizerView.clear() // Clear visualizer view before starting new recording
        binding!!.btnRecord.isEnabled = false
        binding!!.btnShare.isEnabled = false
        binding!!.btnPlay1x.isEnabled = false
        binding!!.btnReport.isEnabled = false
        binding!!.btnPause.isEnabled = true
    }

    private fun setUIForPlaying() {
        binding!!.ayuVisualizerView.clear() // Clear visualizer view before starting new recording
        binding!!.btnRecord.isEnabled = false
        binding!!.btnPause.isEnabled = true
    }

    private fun setUIForPlayingComplete() {
        isPlayingRecordedSound = false
        binding!!.btnRecord.isEnabled =
            AyuSynk.getBleInstance().isDeviceConnected == DeviceConnectionState.DEVICE_CONNECTED
        binding!!.btnPause.isEnabled = false
        binding!!.btnShare.isEnabled = true
        binding!!.btnPlay1x.isEnabled = true
        binding!!.btnReport.isEnabled = true
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
        var filterType: FilterType? = null
        if (position == 0) filterType = FilterType.NO_FILTER else if (position == 1) filterType =
            FilterType.HEART else if (position == 2) filterType = FilterType.LUNG
        if (filterType != null) AyuSynk.getBleInstance().changeFilter(filterType)
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {}

    /**
     * Stop should be called before starting streaming.
     */
    private fun playLastRecordedData() {
        if (recordID != -1) {
            isPlayingRecordedSound = true
            isRecordingPaused = false
            setUIForPlaying()
            Log.d("TTT", "Stream data")
            AyuSynk.getBleInstance().playAudio(recordID) { setUIForPlayingComplete() }
        } else {
            Toast.makeText(activity, "No Recording available\nRecord first", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private val fileFromLastRecordedAudio: File?
        private get() {
            if (lastRecordedData != null) {
                val file = File(GenUtil.getSaveDir("1", context), "recorded.wav")
                try {
                    return AyuFileGenerator.saveFile(lastRecordedData, file)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            } else {
                Toast.makeText(context, "No Recording available\nRecord first", Toast.LENGTH_SHORT)
                    .show()
            }
            return null
        }

    private fun shareFile(file: File?) {
        val uri = FileProvider.getUriForFile(
            requireContext(),
            requireContext().applicationContext.packageName + ".provider",
            file!!
        )
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "audio/wav"
        intent.putExtra(Intent.EXTRA_STREAM, uri)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        startActivity(Intent.createChooser(intent, "Share"))
    }

    private fun shareMessage(shareLinkMessage: String) {
        if (!shareLinkMessage.isEmpty()) {
            val waIntent = Intent(Intent.ACTION_SEND)
            waIntent.type = "text/plain"
            waIntent.putExtra(Intent.EXTRA_TEXT, shareLinkMessage)
            startActivity(Intent.createChooser(waIntent, "Share with"))
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn_record -> {
                AyuSynk.getBleInstance().startRecording()
                setUiForRecording()
            }
            R.id.btn_pause -> {
                if (isPlayingRecordedSound) {
                    AyuSynk.getBleInstance().pausePlayback() // Pause playing
                    isPlayingRecordedSound = false
                } else {
                    AyuSynk.getBleInstance().pauseRecording()
                    isRecordingPaused = true
                }
                if (lastRecordedData != null) setUIForPlayingComplete() else {
                    binding!!.btnRecord.isEnabled = true
                    binding!!.btnPause.isEnabled = false
                }
            }
            R.id.btn_play1x -> playLastRecordedData()
            R.id.btn_share -> {
                val file = fileFromLastRecordedAudio
                shareFile(file)
            }
            R.id.btn_report -> {
                binding!!.btnReport.isEnabled = false
                binding!!.progressBarReport.visibility = View.VISIBLE
                binding!!.btnReportShare.isEnabled = false
                val soundData = SoundData(fileFromLastRecordedAudio, LocationType.unknown)
                AyuSynk.getBleInstance()
                    .generateDiagnosisReport(SoundFile(soundData, SoundType.HEART))
            }
            R.id.btn_updateDevice -> navigateToOTAScreen(this@MainBleFragment)
            R.id.btn_shareUrl -> shareMessage(AyuSynk.getBleInstance().liveStreamUrl)
            R.id.btn_reportShare -> shareReports(binding!!.btnReportShare.tag as SoundFile)
            R.id.btn_onlineStream -> {
                binding!!.progressBarStream.visibility = View.VISIBLE
                binding!!.btnOnlineStream.isEnabled = false
                if (!AyuSynk.getBleInstance().isStreamingOnline) {
                    AyuSynk.getBleInstance().startOnlineStreaming()
                } else {
                    AyuSynk.getBleInstance().stopOnlineStreaming()
                }
            }
            R.id.btn_logs -> if (binding!!.btnLogs.text.toString().lowercase(Locale.getDefault())
                    .contains("enable")
            ) {
                binding!!.btnLogs.text = getString(R.string.logsText, "Disable")
                AyuSynk.getBleInstance().showLogs(true)
            } else {
                binding!!.btnLogs.text = getString(R.string.logsText, "Enable")
                AyuSynk.getBleInstance().showLogs(false)
            }
            R.id.btn_shareLogs -> shareMessage(AyuSynk.getBleInstance().logs)
        }
    }

    private fun shareReports(soundFile: SoundFile) {
        val link = StringBuilder()
        for (reportLink in soundFile.reports) {
            link.append(reportLink)
            link.append("\n")
        }
        shareMessage(link.toString())
    }

    override fun onOnlineStreamChangeListener(isStreaming: Boolean) {
        if (isStreaming) {
            binding!!.btnOnlineStream.text = getString(R.string.onlineLiveText, "Stop")
            binding!!.btnShareUrl.isEnabled = true
        } else {
            binding!!.btnOnlineStream.text = getString(R.string.onlineLiveText, "Start")
            binding!!.btnShareUrl.isEnabled = false
        }
        binding!!.progressBarStream.visibility = View.GONE
        binding!!.btnOnlineStream.isEnabled = true
    }

    override fun onOnlineStreamError(error: String) {
        binding!!.progressBarStream.visibility = View.GONE
        binding!!.btnOnlineStream.isEnabled = true
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
    }

    /**
     * Elapsed time of recording/streaming
     */
    override fun elapsedTime(min: Long, secs: Long) {
        if (binding != null) binding!!.progressBar.progress = secs.toInt()
    }

    /**
     * Called when recording is completed
     */
    override fun recordingComplete(recordID: Int) {
        this.recordID = recordID
        lastRecordedData = AyuSynk.getBleInstance().getAudioData(recordID) //Get recorder data.
        setUIForPlayingComplete()
    }

    /**
     * Called when playing is completed
     */
    override fun playingComplete() {
        setUIForPlayingComplete()
    }

    override fun reportGenerated(soundFile: SoundFile) {
        binding!!.btnReport.isEnabled = true
        binding!!.progressBarReport.visibility = View.GONE
        binding!!.btnReportShare.isEnabled = true
        binding!!.btnReportShare.tag = soundFile
        Toast.makeText(context, "Reports generated", Toast.LENGTH_SHORT).show()
    }

    override fun onReportGenerationError(error: String) {
        binding!!.btnReport.isEnabled = true
        binding!!.progressBarReport.visibility = View.GONE
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
    }

    override fun logs(message: String) {
        if (binding != null) {
            binding!!.notifications.append(message)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding!!.root
    }

    override fun onDestroy() {
        super.onDestroy()
        AyuSynk.getBleInstance().close()
    }
}