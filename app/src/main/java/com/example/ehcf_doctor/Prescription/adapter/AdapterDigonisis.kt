package com.example.ehcf_doctor.Prescription.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ehcf_doctor.Prescription.model.ModelDigonsis
import com.example.ehcf_doctor.Prescription.model.ModelOrderDetails
import com.example.ehcf_doctor.R

class AdapterDigonisis (val context:Context, val list: List<ModelDigonsis>):
    RecyclerView.Adapter<AdapterDigonisis.MyViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.single_row_add_diagnosis,parent,false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

      //  holder.SrNo.text= "${position+1}"
      //  holder.refrencecode.text= list[position].referenceCode
        holder.digonicName.text= list[position].digonicName
        holder.description.text= list[position].description

    }

    override fun getItemCount(): Int {
       return list.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val digonicName:TextView=itemView.findViewById(R.id.tvDigonicName)
        val description:TextView=itemView.findViewById(R.id.tvDescription)
    }
}