package com.example.ehcf_doctor.Invoice.adapter

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
import com.example.ehcf_doctor.Invoice.InvoiceDetail
import com.example.ehcf_doctor.Invoice.model.ModelInvoice
import com.example.ehcf_doctor.R


class AdapterInvoice(val context: Context, private val list: ModelInvoice) :
    RecyclerView.Adapter<AdapterInvoice.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(R.layout.single_row_invoice, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // holder.SrNo.text= "${position+1}"

        holder.invoiceNo.text = list.result[position].invoice_id
        holder.date.text = list.result[position].date
        holder.patientName.text = list.result[position].customer_name
//        holder.name.text = list.result[position].category_name.toString()
//        Picasso.get().load(list.result[position].category_image).into(holder.image)
        holder.btnView.setOnClickListener {
            val intent = Intent(context as Activity, InvoiceDetail::class.java)
                .putExtra("invoiceId", list.result[position].invoice_id.toString())

            context.startActivity(intent)
        }

        // Glide.with(hol der.image).load(list[position].url).into(holder.image)

    }


    override fun getItemCount(): Int {
        return list.result.size

    }

    open class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var invoiceNo: TextView = itemView.findViewById(R.id.tvInvoiceInv);
        var date: TextView = itemView.findViewById(R.id.tvDateInv);
        var patientName: TextView = itemView.findViewById(R.id.tvDoctorNameInv);
        var btnView: Button = itemView.findViewById(R.id.btnViewInv);
        var cardView: CardView = itemView.findViewById(R.id.cardInvoice);

    }
}