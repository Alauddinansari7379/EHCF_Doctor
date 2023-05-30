package com.example.ehcf_doctor.Prescription.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ehcf.sharedpreferences.SessionManager
import com.example.ehcf_doctor.ManageSlots.activity.UpdateSlot
import com.example.ehcf_doctor.Prescription.activity.AddPrescription
import com.example.ehcf_doctor.Prescription.model.ModelPreDetails
import com.example.ehcf_doctor.R


class AdapterPrescriptionDetial(
    val context: Context, private val list: ModelPreDetails
) :
    RecyclerView.Adapter<AdapterPrescriptionDetial.MyViewHolder>() {
    private lateinit var sessionManager: SessionManager


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context)
                .inflate(R.layout.single_row_prescription_detial, parent, false)
        )

        sessionManager = SessionManager(context)
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // holder.SrNo.text= "${position+1}"
        sessionManager = SessionManager(context)
        holder.note.text = list.result[position].doctor_notes
        holder.addressPreDetial.text = list.result[position].address
        holder.tvDoctorPreDeat.text = list.result[position].customer_name
        holder.coustmorName.text = sessionManager.doctorName.toString()
        holder.tvDate.text = list.result[position].date
        holder.subjectiveInformation.text = list.result[position].subjective_information
        holder.objectiveInformation.text = list.result[position].objective_information
        holder.assessment.text = list.result[position].assessment
        holder.plan.text = list.result[position].plan
        holder.tvRegistrationPreDetial.text = list.result[position].registration
        // holder.endTime.text = list.result[position].end_time
        holder.btnModify.setOnClickListener {
            val intent = Intent(context as Activity, AddPrescription::class.java)
                .putExtra("bookingId", list.result[position].id)
                .putExtra("doctor_notes", list.result[position].doctor_notes)
                .putExtra("subjective_information", list.result[position].subjective_information)
                .putExtra("objective_information", list.result[position].objective_information)
                .putExtra("assessment", list.result[position].assessment)
                .putExtra("plan", list.result[position].plan)
                .putExtra("registration", list.result[position].registration)
                .putExtra("btnId", "1")
            context.startActivity(intent)
        }
        when (list.result[position].gender) {
            "1" -> {
                holder.tvMalePreDet.text = "Male"
            }
            else -> {
                holder.tvMalePreDet.text = "Female"

            }
        }

    }

    override fun getItemCount(): Int {
        return list.result.size

    }

    open class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val registration: TextView = itemView.findViewById(R.id.tvRegistrationPreDetial)
        val gender: TextView = itemView.findViewById(R.id.tvMalePreDet)
        val note: TextView = itemView.findViewById(R.id.doctor_notes)
        val subjectiveInformation: TextView = itemView.findViewById(R.id.subjectiveInformation)
        val objectiveInformation: TextView = itemView.findViewById(R.id.objectiveInformation)
        val assessment: TextView = itemView.findViewById(R.id.assessment)
        val plan: TextView = itemView.findViewById(R.id.plan)
        val tvDate: TextView = itemView.findViewById(R.id.tvDate)
        val tvRegistrationPreDetial: TextView = itemView.findViewById(R.id.tvRegistrationPreDetial)
        val coustmorName: TextView = itemView.findViewById(R.id.tvCoustmorNamePreDet)
        val tvDoctorPreDeat: TextView = itemView.findViewById(R.id.tvDoctorPreDeat)
        val btnModify: TextView = itemView.findViewById(R.id.btnModifyPrescptionDet)
        val addressPreDetial: TextView = itemView.findViewById(R.id.tvAddressPreDetial)
        val tvMalePreDet: TextView = itemView.findViewById(R.id.tvMalePreDet)
//        val startTime: TextView = itemView.findViewById(R.id.tvStartTimePPending)
//        val endTime: TextView = itemView.findViewById(R.id.tvEndTimePending)


    }

}