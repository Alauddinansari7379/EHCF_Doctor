package com.example.ehcf_doctor.Prescription.activity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager.widget.ViewPager
import com.example.ehcf.Helper.isOnline
import com.example.ehcf.Helper.myToast
import com.example.ehcf_doctor.R
import com.example.ehcf_doctor.databinding.FragmentPrecriscptionBinding
import com.google.android.material.tabs.TabLayout
import rezwan.pstu.cse12.youtubeonlinestatus.recievers.NetworkChangeReceiver


class PrecriscptionFragment : Fragment(){
    private lateinit var binding: FragmentPrecriscptionBinding
    private lateinit var pager: ViewPager // creating object of ViewPager
    private lateinit var tab: TabLayout  // creating object of TabLayout
    private lateinit var bar: Toolbar
    private lateinit var btnAddReport: TextView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPrecriscptionBinding.inflate(inflater)

        pager = binding.viewPager
        tab = binding.tabs
        val adapter = ViewPagerAdapter1(childFragmentManager)
        // Inflate the layout for this fragment



        adapter.addFragment(PrescriptionPendingFragment(), "Prescription Pending")
        adapter.addFragment(PrescribedFragment(), "Prescribed")
        pager.adapter = adapter
        tab.setupWithViewPager(pager)



        return (binding.root)
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

//    override fun onRefresh() {
//        requireActivity().overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
//    }

}