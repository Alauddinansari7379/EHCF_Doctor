package com.example.ehcf_doctor.AudioRecording

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.ehcf_doctor.AudioRecording.Fragment.RecordFragment
import com.example.ehcf_doctor.R
import com.example.ehcf_doctor.databinding.FragmentRecordListBinding

class AudioRecordingList : AppCompatActivity() {
    private var navController: NavController? = null
private lateinit var binding:FragmentRecordListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=FragmentRecordListBinding.inflate(layoutInflater)
        setContentView(binding.root)
     //   navController = Navigation.findNavController(view)

     //   navController!!.navigate(R.id.action_recordFragment_to_recordListFragment)
//
//        val fm = supportFragmentManager
//        val fragment = RecordFragment()
//        fm.beginTransaction().add(R.id.fragmentcontainer, fragment).commit()
    }
}