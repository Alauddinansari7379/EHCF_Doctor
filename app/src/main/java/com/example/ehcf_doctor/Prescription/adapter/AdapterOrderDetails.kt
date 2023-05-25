package com.example.ehcf_doctor.Prescription.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ehcf_doctor.Prescription.model.ModelOrderDetails
import com.example.ehcf_doctor.R

class AdapterOrderDetails (val context:Context,val list: List<ModelOrderDetails>):
    RecyclerView.Adapter<AdapterOrderDetails.MyViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.single_row_add_medicine,parent,false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

      //  holder.SrNo.text= "${position+1}"
      //  holder.refrencecode.text= list[position].referenceCode
        holder.medicineName.text= list[position].medicineName
        holder.medicineTiming.text= list[position].medicineTiming
        holder.frequency.text= list[position].frequency
        holder.duration.text= list[position].duration

    }

    override fun getItemCount(): Int {
       return list.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val medicineName:TextView=itemView.findViewById(R.id.tvMedicineName)
        val medicineTiming:TextView=itemView.findViewById(R.id.tvMedicinetiming)
        val frequency:TextView=itemView.findViewById(R.id.Frequency)
        val duration:TextView=itemView.findViewById(R.id.tvDuration)
    }
}