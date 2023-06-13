package com.example.ehcf_doctor.Prescription.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ehcf_doctor.Prescription.model.ModelDigonsis
import com.example.ehcf_doctor.Prescription.model.ModelLabTest
import com.example.ehcf_doctor.Prescription.model.ModelOrderDetails
import com.example.ehcf_doctor.R

class AdapterLabTest (val context:Context, val list: List<ModelLabTest>):
    RecyclerView.Adapter<AdapterLabTest.MyViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.single_row_add_lab_test,parent,false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

      //  holder.SrNo.text= "${position+1}"
      //  holder.refrencecode.text= list[position].referenceCode
        holder.testName.text= list[position].testName
        holder.description.text= list[position].description
        holder.after.text= list[position].after

    }

    override fun getItemCount(): Int {
       return list.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val testName:TextView=itemView.findViewById(R.id.TestName)
        val after:TextView=itemView.findViewById(R.id.After)
        val description:TextView=itemView.findViewById(R.id.Description)
    }
}