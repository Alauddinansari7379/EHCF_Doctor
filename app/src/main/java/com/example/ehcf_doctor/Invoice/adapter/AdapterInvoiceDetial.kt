package com.example.ehcf_doctor.Invoice.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ehcf.Helper.convertTo12Hour
import com.example.ehcf_doctor.Invoice.model.ModelInvoice
import com.example.ehcf_doctor.Invoice.model.ModelInvoiceDetial
import com.example.ehcf_doctor.R


class AdapterInvoiceDetial(val context: Context, private val list: ModelInvoiceDetial) :
    RecyclerView.Adapter<AdapterInvoiceDetial.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(R.layout.single_row_invoice_detial, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // holder.SrNo.text= "${position+1}"

        holder.hospitalName.text = list.result[position].hos_name
        holder.invoiceNo.text = list.result[position].invoice_id
        holder.date.text = list.result[position].date
        holder.time.text = convertTo12Hour(list.result[position].time)
        holder.doctorName.text = list.result[position].doctor_name
        holder.patientName.text = list.result[position].customer_name
        holder.amount.text = list.result[position].amount

//        holder.name.text = list.result[position].category_name.toString()
//        Picasso.get().load(list.result[position].category_image).into(holder.image)


        // Glide.with(hol der.image).load(list[position].url).into(holder.image)

    }


    override fun getItemCount(): Int {
        return list.result.size

    }

    open class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var hospitalName: TextView = itemView.findViewById(R.id.tvHospitalNameInvD)
        var invoiceNo: TextView = itemView.findViewById(R.id.tvInvoiceNoInvD)
        var date: TextView = itemView.findViewById(R.id.tvDateInvD)
        var time: TextView = itemView.findViewById(R.id.tvTimeInvD)
        var doctorName: TextView = itemView.findViewById(R.id.tvDoctorNameInvD)
        var patientName: TextView = itemView.findViewById(R.id.tvPatientNameInvD)
        var amount: TextView = itemView.findViewById(R.id.tvAmountInvD)

    }
}