package com.example.ehcf_doctor.Dashboard.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.ehcf.Helper.convertTo12Hour
import com.example.ehcf_doctor.Dashboard.model.ModelDashboard
import com.example.ehcf_doctor.Invoice.InvoiceDetail
import com.example.ehcf_doctor.Invoice.model.ModelInvoice
import com.example.ehcf_doctor.R


class AdapterDashboard(val context: Context, private val list: ModelDashboard) :
    RecyclerView.Adapter<AdapterDashboard.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(R.layout.single_row_dashboard, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // holder.SrNo.text= "${position+1}"

        holder.time.text = convertTo12Hour(list.result[position].start_time)

//        holder.name.text = list.result[position].category_name.toString()
//        Picasso.get().load(list.result[position].category_image).into(holder.image)
//        holder.btnView.setOnClickListener {
//            val intent = Intent(context as Activity, InvoiceDetail::class.java)
//            context.startActivity(intent)
//        }

        // Glide.with(hol der.image).load(list[position].url).into(holder.image)

    }


    override fun getItemCount(): Int {
        return list.result.size

    }

    open class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var time: TextView = itemView.findViewById(R.id.TimeDashboard);

    }
}