package com.example.ehcf_doctor.AyuSynk.usb

import android.content.Intent
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.ayudevice.ayusynksdk.AyuSynk
import com.ayudevice.ayusynksdk.ble.constants.DeviceConnectionState
import com.ayudevice.ayusynksdk.denoise.constants.FilterType
import com.ayudevice.ayusynksdk.onlineLiveStreaming.OnlineLiveStreamListener
import com.ayudevice.ayusynksdk.playback.AyuFileGenerator
import com.ayudevice.ayusynksdk.playback.listener.RecorderListener
import com.ayudevice.ayusynksdk.report.SoundData
import com.ayudevice.ayusynksdk.report.SoundFile
import com.ayudevice.ayusynksdk.report.constants.LocationType
import com.ayudevice.ayusynksdk.report.constants.SoundType
import com.ayudevice.ayusynksdk.report.listener.DiagnosisReportUpdateListener
import com.ayudevice.ayusynksdk.usb.listener.AyuDeviceListener
import com.ayudevice.ayusynksdk.utils.logs.AyuLogsListener
import com.example.ehcf_doctor.AyuSynk.utils.GenUtil
import com.example.ehcf_doctor.R
import com.example.ehcf_doctor.databinding.FragmentMainBinding
import java.io.File
import java.io.IOException
import java.util.*

class MainUsbFragment : Fragment(), AdapterView.OnItemSelectedListener, View.OnClickListener,
    OnlineLiveStreamListener, AyuDeviceListener, RecorderListener, DiagnosisReportUpdateListener,
    AyuLogsListener {
    private var binding: FragmentMainBinding? = null
    private var lastRecordedData: ShortArray? = null
    private var recordID = -1
    val usb=0
    private var isRecordingPaused = false
    private var isPlayingRecordedSound = false
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        binding!!.customBatteryMeter.visibility = View.GONE
        binding!!.deviceStrength.visibility = View.GONE
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
        AyuSynk.getUsbInstance().setAyuVisualizerView(binding!!.ayuVisualizerView)
        AyuSynk.getUsbInstance().setRecordingTimeLimit(10)

        if (usb==0){
            binding!!.appCompatTextView2.text="USB"
        } else{
            binding!!.appCompatTextView2.text="Bluetooth"

        }


        binding!!.imgBack.setOnClickListener {
            NavHostFragment.findNavController(this@MainUsbFragment).navigate(R.id.SelectModuleFragment)

        }
        binding!!.btnConnect.setOnClickListener { view1: View? ->
            if (binding!!.btnConnect.tag as Int == 2) {
                AyuSynk.getUsbInstance().disconnect()
            } else {
                if (AyuSynk.getUsbInstance().isAllUSBPermissionGranted) AyuSynk.getUsbInstance()
                    .connect() else AyuSynk.getUsbInstance().requestRecordAudioPermission(
                    activity, 123
                )
            }
        }
        binding!!.btnRecord.setOnClickListener(this)
        binding!!.btnPause.setOnClickListener(this)
        binding!!.btnPlay1x.setOnClickListener(this)
        binding!!.btnShare.setOnClickListener(this)
        binding!!.btnReport.setOnClickListener(this)
        binding!!.btnReportShare.setOnClickListener(this)
      //  binding!!.btnUpdateDevice.visibility = View.INVISIBLE
        binding!!.btnOnlineStream.text = getString(R.string.onlineLiveText, "Start")
        binding!!.btnOnlineStream.setOnClickListener(this)
        binding!!.btnShareUrl.setOnClickListener(this)
       // binding!!.notifications.movementMethod = ScrollingMovementMethod()
        AyuSynk.getUsbInstance().setOnlineStreamerListener(this)
        AyuSynk.getUsbInstance().setLogsListener(this)
        AyuSynk.getUsbInstance().showLogs(true)
//        binding!!.btnLogs.text = getString(R.string.logsText, "Disable")
//        binding!!.btnLogs.setOnClickListener(this)
//        binding!!.btnShareLogs.setOnClickListener(this)
        AyuSynk.getUsbInstance().registerUsbDeviceConnectionReceiver()
    }

    override fun onResume() {
        super.onResume()
        AyuSynk.getUsbInstance().setAyuDeviceListener(this)
        AyuSynk.getUsbInstance().setRecorderListener(this)
        AyuSynk.getUsbInstance().setDiagnosisReportUpdateListener(this)
        if (AyuSynk.getUsbInstance().isDeviceConnected == DeviceConnectionState.DEVICE_CONNECTED) {
            onDeviceConnected()
        } else {
            onDeviceDisconnected()
        }
    }

    override fun onConnectError(error: String) {
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
    }

    override fun deviceConnectionState(state: DeviceConnectionState) {
        if (state != DeviceConnectionState.DEVICE_CONNECTED) {
            onDeviceDisconnected()
        } else {
            onDeviceConnected()
        }
    }

    private fun onDeviceConnected() {
        binding!!.btnRecord.isEnabled = true
        binding!!.btnOnlineStream.isEnabled = true
        binding!!.deviceState.setText(R.string.device_connected)
        binding!!.btnConnect.setText(R.string.disconnect)
        binding!!.btnConnect.tag = 2
    }

    private fun onDeviceDisconnected() {
        binding!!.btnRecord.isEnabled = false
        binding!!.btnOnlineStream.isEnabled = false
        binding!!.deviceState.setText(R.string.device_disconnected)
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
        binding!!.btnRecord.isEnabled = true
        binding!!.btnPause.isEnabled = false
        binding!!.btnShare.isEnabled = true
        binding!!.btnPlay1x.isEnabled = true
        binding!!.btnReport.isEnabled = true
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
        var filterType: FilterType? = null
        if (position == 0) filterType = FilterType.NO_FILTER else if (position == 1) filterType =
            FilterType.HEART else if (position == 2) filterType = FilterType.LUNG
        if (filterType != null) AyuSynk.getUsbInstance().changeFilter(filterType)
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
            AyuSynk.getUsbInstance().playAudio(recordID) { setUIForPlayingComplete() }
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
                AyuSynk.getUsbInstance().startRecording()
                setUiForRecording()
            }
            R.id.btn_pause -> {
                if (isPlayingRecordedSound) {
                    AyuSynk.getUsbInstance().pausePlayback() // Pause playing
                    isPlayingRecordedSound = false
                } else {
                    AyuSynk.getUsbInstance().pauseRecording()
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
              //  binding!!.progressBarReport.visibility = View.VISIBLE
                binding!!.btnReportShare.isEnabled = false
                val soundData = SoundData(fileFromLastRecordedAudio, LocationType.Heart.aortic)
                AyuSynk.getUsbInstance()
                    .generateDiagnosisReport(SoundFile(soundData, SoundType.HEART))
            }
            R.id.btn_shareUrl -> shareMessage(AyuSynk.getUsbInstance().liveStreamUrl)
            R.id.btn_reportShare -> shareReports(binding!!.btnReportShare.tag as SoundFile)
            R.id.btn_onlineStream -> {
                binding!!.progressBarStream.visibility = View.VISIBLE
                binding!!.btnOnlineStream.isEnabled = false
                if (!AyuSynk.getUsbInstance().isStreamingOnline) {
                    AyuSynk.getUsbInstance().startOnlineStreaming()
                } else {
                    AyuSynk.getUsbInstance().stopOnlineStreaming()
                }
            }
//            R.id.btn_logs -> if (binding!!.btnLogs.text.toString().lowercase(Locale.getDefault())
//                    .contains("enable")
//            ) {
//                binding!!.btnLogs.text = getString(R.string.logsText, "Disable")
//                AyuSynk.getUsbInstance().showLogs(true)
//            } else {
//                binding!!.btnLogs.text = getString(R.string.logsText, "Enable")
//                AyuSynk.getUsbInstance().showLogs(false)
//            }
//            R.id.btn_shareLogs -> shareMessage(AyuSynk.getBleInstance().logs)
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
        lastRecordedData = AyuSynk.getUsbInstance().getAudioData(recordID) //Get recorder data.
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
      //  binding!!.progressBarReport.visibility = View.GONE
        binding!!.btnReportShare.isEnabled = true
        binding!!.btnReportShare.tag = soundFile
        Toast.makeText(context, "Reports generated", Toast.LENGTH_SHORT).show()
    }

    override fun onReportGenerationError(error: String) {
        binding!!.btnReport.isEnabled = true
      //  binding!!.progressBarReport.visibility = View.GONE
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
    }

    override fun logs(message: String) {
//        if (binding != null) {
//            binding!!.notifications.append(message)
//        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        AyuSynk.getUsbInstance().unRegisterUsbDeviceConnectionReceiver()
        binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        AyuSynk.getUsbInstance().close()
    }
}