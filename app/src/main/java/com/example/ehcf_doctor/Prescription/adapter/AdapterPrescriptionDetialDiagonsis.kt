package com.example.ehcf.Prescription.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
 import com.example.ehcf.sharedpreferences.SessionManager
import com.example.ehcf_doctor.Prescription.model.ModelPreDetJava
import com.example.ehcf_doctor.R


class AdapterPrescriptionDetialDiagonsis(val context: Context, private val list: ModelPreDetJava
) :
    RecyclerView.Adapter<AdapterPrescriptionDetialDiagonsis.MyViewHolder>() {
    private lateinit var sessionManager: SessionManager


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context)
                .inflate(R.layout.single_row_prescription_detial_diagnosis, parent, false)
        )

    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // holder.SrNo.text= "${position+1}"
        sessionManager = SessionManager(context)
        holder.diagnosis.text = list.diagnosis[position].diagnosis_name
        holder.description.text = list.diagnosis[position].description


    }

    override fun getItemCount(): Int {
        return list.diagnosis.size

    }

    open class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val diagnosis: TextView = itemView.findViewById(R.id.Diagnosis)
        val description: TextView = itemView.findViewById(R.id.Description)





    }

}