package com.example.ehcf_doctor.Prescription

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toolbar
import androidx.viewpager.widget.ViewPager
import com.example.ehcf_doctor.R
import com.example.ehcf_doctor.databinding.FragmentPrecriscptionBinding
import com.example.ehcf_doctor.databinding.FragmentPrescribedBinding
import com.google.android.material.tabs.TabLayout

class PrecriscptionFragment : Fragment() {
    private lateinit var binding:FragmentPrecriscptionBinding
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

            adapter.addFragment(PrescriptionPendingFragment(), "PrescriptionPending")
            adapter.addFragment(PrescribedFragment(), "Prescribed")
            pager.adapter = adapter
            tab.setupWithViewPager(pager)

            return (binding.root)
    }

}