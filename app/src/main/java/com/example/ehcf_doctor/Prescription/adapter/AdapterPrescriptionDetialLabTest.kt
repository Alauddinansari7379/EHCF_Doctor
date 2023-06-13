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


class AdapterPrescriptionDetialLabTest(val context: Context, private val list: ModelPreDetJava
) :
    RecyclerView.Adapter<AdapterPrescriptionDetialLabTest.MyViewHolder>() {
    private lateinit var sessionManager: SessionManager


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context)
                .inflate(R.layout.single_row_prescription_lab_test, parent, false)
        )

    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // holder.SrNo.text= "${position+1}"
        sessionManager = SessionManager(context)
        holder.labTest.text = list.labtest[position].testName as CharSequence?
        holder.labInstraction.text = list.labtest[position].instructions as CharSequence?
        holder.After.text = list.labtest[position].after as CharSequence?

    }

    override fun getItemCount(): Int {
        return list.labtest.size

    }

    open class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val labTest: TextView = itemView.findViewById(R.id.LabTest)
        val labInstraction: TextView = itemView.findViewById(R.id.LabInstraction)
        val After: TextView = itemView.findViewById(R.id.After)





    }

}