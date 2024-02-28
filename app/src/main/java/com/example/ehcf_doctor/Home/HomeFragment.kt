package com.example.ehcf_doctor.Home

import android.Manifest
import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.bluetooth.BluetoothDevice
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import com.example.easywaylocation.Listener

 import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.easywaylocation.EasyWayLocation
import com.example.easywaylocation.GetLocationDetail
import com.example.easywaylocation.LocationData
import com.example.ehcf.Helper.isOnline
import com.example.ehcf.Helper.myToast
import com.example.ehcf.sharedpreferences.SessionManager
import com.example.ehcf_doctor.Appointments.Appointments
import com.example.ehcf_doctor.Appointments.Upcoming.adapter.AdapterHome
import com.example.ehcf_doctor.Booking.model.ModelGetConsultation
import com.example.ehcf_doctor.MainActivity.activity.MainActivity
import com.example.ehcf_doctor.R
import com.example.ehcf_doctor.databinding.FragmentHomeBinding
import com.example.myrecyview.apiclient.ApiClient
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import rezwan.pstu.cse12.youtubeonlinestatus.recievers.NetworkChangeReceiver
import java.io.IOException
import java.util.*


 class HomeFragment : Fragment(), Listener, LocationData.AddressCallBack {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var sessionManager: SessionManager
    var doctorname = ""
    var id = ""
    var progressDialog: ProgressDialog? = null
    var shimmerFrameLayout: ShimmerFrameLayout? = null
    private val senderID = "YOUR_SENDER_ID"
    private var fusedLocationProviderClient: FusedLocationProviderClient? = null
    private lateinit var easyWayLocation: EasyWayLocation
    private lateinit var getLocationDetail: GetLocationDetail
    lateinit var lm: LocationManager
    private val REQUEST_CODE = 100
    private var currentAddress = ""



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    @SuppressLint("LogNotTimber")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentHomeBinding.bind(view)
        sessionManager = SessionManager(requireContext())
        shimmerFrameLayout = view.findViewById(R.id.shimmer)
        shimmerFrameLayout!!.startShimmer();
//
//        if (sessionManager.clinicAddress=="null"){
//
//        }
// Setup our BluetoothManager
        // Setup our BluetoothManager
        val refreshListener = SwipeRefreshLayout.OnRefreshListener {

            (activity as MainActivity).refreshMain()
        }
        binding.swipeRefreshLayout.setOnRefreshListener(refreshListener)

        binding.cardTotalBooking.setOnClickListener {
            startActivity(Intent(requireContext(), Appointments::class.java))
        }

        binding.CardCompleted.setOnClickListener {
            startActivity(Intent(requireContext(), Appointments::class.java))

        }
        binding.CardRejected.setOnClickListener {
            startActivity(Intent(requireContext(), Appointments::class.java))

        }

        binding.cardTotalPending.setOnClickListener {
            startActivity(Intent(requireContext(), Appointments::class.java))

        }
        // audioRecorder.startRecording(requireContext())
        //   audioRecorder.pauseRecording()
        // audioRecorder.resumeRecording()


        getLocationDetail = GetLocationDetail(this, requireContext())
        easyWayLocation = EasyWayLocation(requireContext(), false, false, this)


        lm =requireContext().getSystemService(AppCompatActivity.LOCATION_SERVICE) as LocationManager

        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())
        getLastLocation()


        doctorname = sessionManager.doctorName.toString()
        //  id = sessionManager.id.toString()
        binding.tvDoctorName.text = doctorname
        binding.tvDoctorId.text = id
        Log.e("DoctorNAme,", "$doctorname")
        Log.e("DoctorId,", "${sessionManager.id}")
//
        apiCallGetConsultationWating()

//        CoroutineScope(Dispatchers.IO).launch {
//            Log.d("", ": coroutine start")
//            apiCallTotalBooking()
//            apiCallCompletedBooking()
//            apiCallRejectedBooking()
//        }

        lifecycleScope.launch {
            apiCallTotalBooking()
            apiCallCompletedBooking()
            apiCallRejectedBooking()
        }

//        CheckInternet().check { connected ->
//            if (connected) {
//               // myToast(requireActivity(), "")
//            }
//        }
    }

    @SuppressLint("SetTextI18n", "LogNotTimber")
    private fun getLastLocation() {
        if (ContextCompat.checkSelfPermission(
                requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationProviderClient!!.lastLocation
                .addOnSuccessListener { location ->
                    if (location != null) {
                        try {
                            val geocoder = Geocoder(requireActivity(), Locale.getDefault())
                            val addresses =
                                geocoder.getFromLocation(location.latitude, location.longitude, 1)
//                            lattitude!!.text = "Lattitude: " + addresses[0].latitude
//                            longitude!!.text = "Longitude: " + addresses[0].longitude
                            Log.e(
                                ContentValues.TAG,
                                " addresses[0].latitude${addresses?.get(0)?.latitude}"
                            )
                            Log.e(
                                ContentValues.TAG,
                                " addresses[0].latitude${addresses?.get(0)?.longitude}"
                            )
                            sessionManager.latitude = addresses?.get(0)?.latitude.toString()
                            sessionManager.longitude = addresses?.get(0)?.longitude.toString()

                            addresses?.get(0)?.getAddressLine(0)

                            val locality = addresses?.get(0)?.locality
                            val countryName = addresses?.get(0)?.countryName
                            val countryCode = addresses?.get(0)?.countryCode
                            val postalCode = addresses?.get(0)?.postalCode
                            val subLocality = addresses?.get(0)?.subLocality
                            val subAdminArea = addresses?.get(0)?.subAdminArea

                            currentAddress = "$subLocality, $locality, $countryName"

                           // binding.tvLocation.text = currentAddress
                            binding.tvLocation.text = addresses?.get(0)?.getAddressLine(0)

                            Log.e(ContentValues.TAG, "locality-$locality")
                            Log.e(ContentValues.TAG, "countryName-$countryName")
                            Log.e(ContentValues.TAG, "countryCode-$countryCode")
                            Log.e(ContentValues.TAG, "postalCode-$postalCode")
                            Log.e(ContentValues.TAG, "subLocality-$subLocality")
                            Log.e(ContentValues.TAG, "subAdminArea-$subAdminArea")

                            Log.e(
                                ContentValues.TAG,
                                " addresses[0].Address${addresses?.get(0)?.getAddressLine(0)}"
                            )

                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    }
                }
        } else {
            askPermission()
        }
    }

    private fun askPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
            REQUEST_CODE
        )
    }



    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation()
            } else {
                Toast.makeText(
                    requireContext(), "Please provide the required permission", Toast.LENGTH_SHORT
                ).show()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions!!, grantResults)
    }

    override fun locationOn() {
        getLastLocation()
    }

    @OptIn(DelicateCoroutinesApi::class)
    @SuppressLint("LogNotTimber")
    override fun currentLocation(location: Location?) {
        val latitude = location!!.latitude
        val longitude = location.longitude

        Log.e("getCurrentLocation", ">>>>>>>>>>:$latitude\n$longitude ")
        GlobalScope.launch {
            // getLocationDetail.getAddress(location.latitude, location.longitude, "xyz")
            getLocationDetail.getAddress(location.latitude, location.longitude, "AAA")
            getLastLocation()

        }

    }

    override fun locationCancelled() {
        TODO("Not yet implemented")
    }

    @SuppressLint("LogNotTimber")
    override fun locationData(locationData: LocationData?) {
        locationData?.full_address.toString()
//        val currentAddress1 = locationData?.country.toString()
//        val Address = currentAddress+currentAddress1

    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            EasyWayLocation.LOCATION_SETTING_REQUEST_CODE -> easyWayLocation.onActivityResult(
                resultCode
            )
        }
    }
    private fun isNetworkConnected(): Boolean {
        val cm = requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo != null
    }


    private fun apiCallRejectedBooking() {
        progressDialog = ProgressDialog(requireContext())
        progressDialog!!.setMessage("Loading..")
        progressDialog!!.setTitle("Please Wait")
        progressDialog!!.isIndeterminate = false
        progressDialog!!.setCancelable(true)

        //progressDialog!!.show()

        ApiClient.apiService.getConsultation(sessionManager.id.toString(), "rejected")
            .enqueue(object : Callback<ModelGetConsultation> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelGetConsultation>, response: Response<ModelGetConsultation>
                ) {
                    try {
                        if (response.code() == 500) {

                        } else if (response.body()!!.result.isEmpty()) {
                            response.body()!!.result.size.toString()
                            Log.e("Size", response.body()!!.result.size.toString())
                            binding.tvRejectedBooking.text =
                                response.body()!!.result.size.toString()

                        } else {
                            response.body()!!.result.size.toString()
                            Log.e("Size", response.body()!!.result.size.toString())
                            binding.tvRejectedBooking.text =
                                response.body()!!.result.size.toString()
                        }


                    }catch (e:Exception){
                        e.printStackTrace()
                        myToast(requireActivity(), "Something went wrong")
                        binding.shimmer.visibility = View.GONE
                        progressDialog!!.dismiss()
                    }
                }

                override fun onFailure(call: Call<ModelGetConsultation>, t: Throwable) {
                    myToast(requireActivity(), "Something went wrong")
                    binding.shimmer.visibility = View.GONE
                    progressDialog!!.dismiss()

                }

            })

    }

    private fun apiCallCompletedBooking() {
        progressDialog = ProgressDialog(requireContext())
        progressDialog!!.setMessage("Loading..")
        progressDialog!!.setTitle("Please Wait")
        progressDialog!!.isIndeterminate = false
        progressDialog!!.setCancelable(true)

        //  progressDialog!!.show()

        ApiClient.apiService.getConsultation(sessionManager.id.toString(), "completed")
            .enqueue(object : Callback<ModelGetConsultation> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelGetConsultation>, response: Response<ModelGetConsultation>
                ) {
                    try {
                        if (response.code() == 500) {

                        } else if (response.body()!!.result.isEmpty()) {
                            binding.shimmer.visibility = View.GONE
                            response.body()!!.result.size.toString()
                            Log.e("Size", response.body()!!.result.size.toString())
                            binding.tvCompletedBooking.text =
                                response.body()!!.result.size.toString()
                            // myToast(requireActivity(),"No Data Found")

                        } else {
                            response.body()!!.result.size.toString()
                            Log.e("Size", response.body()!!.result.size.toString())
                            binding.tvCompletedBooking.text =
                                response.body()!!.result.size.toString()
                        }


                    }catch (e:Exception){
                        e.printStackTrace()
                        progressDialog!!.dismiss()

                    }
                }

                override fun onFailure(call: Call<ModelGetConsultation>, t: Throwable) {
                    progressDialog!!.dismiss()

                }

            })
    }

    private fun apiCallTotalBooking() {
        progressDialog = ProgressDialog(requireContext())
        progressDialog!!.setMessage("Loading..")
        progressDialog!!.setTitle("Please Wait")
        progressDialog!!.isIndeterminate = false
        progressDialog!!.setCancelable(true)

        //  progressDialog!!.show()

        ApiClient.apiService.getConsultation(sessionManager.id.toString(), "")
            .enqueue(object : Callback<ModelGetConsultation> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelGetConsultation>, response: Response<ModelGetConsultation>
                ) {
                    try {
                        if (response.code() == 500) {

                        } else if (response.body()!!.result.isEmpty()) {
                            response.body()!!.result.size.toString()
                            Log.e("Size", response.body()!!.result.size.toString())
                            binding.tvTotalBooking.text = response.body()!!.result.size.toString()

                        } else {
                            response.body()!!.result.size.toString()
                            Log.e("Size", response.body()!!.result.size.toString())
                            binding.tvTotalBooking.text = response.body()!!.result.size.toString()
                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                        progressDialog!!.dismiss()

                    }



                }

                override fun onFailure(call: Call<ModelGetConsultation>, t: Throwable) {
                    myToast(requireActivity(), "Something went wrong")
                    binding.shimmer.visibility = View.GONE
                    progressDialog!!.dismiss()

                }

            })
    }

    private fun apiCallGetConsultationWating() {

        ApiClient.apiService.getConsultation(sessionManager.id.toString(), "waiting_for_accept")
            .enqueue(object : Callback<ModelGetConsultation> {
                @SuppressLint("LogNotTimber")
                override fun onResponse(
                    call: Call<ModelGetConsultation>, response: Response<ModelGetConsultation>
                ) {
                    try {
                        if (response.code() == 500) {
                            myToast(requireActivity(), "Server Error")
                            binding.shimmer.visibility = View.GONE

                        } else if (response.body()!!.result.isEmpty()) {
                            binding.shimmer.visibility = View.GONE

                            // myToast(requireActivity(),"No Data Found")

                        } else {
                            binding.rvUpcoming.apply {
                                shimmerFrameLayout?.startShimmer()
                                response.body()!!.result.size.toString()
                                Log.e("Size", response.body()!!.result.size.toString())
                                binding.tvPendingBooking.text = response.body()!!.result.size.toString()
                                binding.rvUpcoming.visibility = View.VISIBLE
                                binding.shimmer.visibility = View.GONE
                                //  myToast(requireActivity(),"Adapter")
                                adapter = activity?.let { AdapterHome(it, response.body()!!) }
                            }
                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                        e.localizedMessage?.let { Log.e("CatchError", it) }
                        progressDialog!!.dismiss()

                    }


                }

                override fun onFailure(call: Call<ModelGetConsultation>, t: Throwable) {
                    // myToast(requireActivity(), "Something went wrong")
                    binding.shimmer.visibility = View.GONE
                    progressDialog!!.dismiss()

                }

            })
    }

     override fun onResume() {
         super.onResume()
         easyWayLocation.startLocation()
     }

    override fun onStart() {
        super.onStart()

        if (isOnline(requireContext())) {
            //  myToast(requireActivity(), "Connected")
        } else {
            val changeReceiver = NetworkChangeReceiver(context)
            changeReceiver.build()
            //  myToast(requireActivity(), "Not C")

        }
//        CheckInternet().check { connected ->
//            if (connected) {
//             //    myToast(requireActivity(),"Connected")
//            }
//            else {
//                val changeReceiver = NetworkChangeReceiver(context)
//                changeReceiver.build()
//                //  myToast(requireActivity(),"Check Internet")
//            }
//        }
    }

}
