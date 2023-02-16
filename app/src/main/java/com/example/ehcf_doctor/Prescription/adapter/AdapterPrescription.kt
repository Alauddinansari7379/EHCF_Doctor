package com.example.ehcf_doctor.Prescription.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.ehcf.Helper.changeDateFormat2
import com.example.ehcf.Helper.changeDateFormat3
import com.example.ehcf_doctor.Appointments.Upcoming.model.ModelUpComingResponse
import com.example.ehcf_doctor.ManageSlots.activity.UpdateSlot
import com.example.ehcf_doctor.Prescription.AddPrescription
import com.example.ehcf_doctor.R
import java.text.SimpleDateFormat
import java.util.*


class AdapterPrescription(
    val context: Context, private val list: ModelUpComingResponse
) :
    RecyclerView.Adapter<AdapterPrescription.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(R.layout.single_row_pending_prescription, parent, false)
        )
    }
    var currentDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // holder.SrNo.text= "${position+1}"
        Log.e("currentDate","$currentDate")
        Log.e("startTime","${list.result[position].start_time.toString()}")
        holder.startTime.text = list.result[position].start_time.toString().substring(10,)
        holder.bookingDate.text = list.result[position].start_time.toString().substring(0,10)
        holder.coustmerName.text = list.result[position].customer_name.toString()
        holder.title.text = list.result[position].title.toString()

        holder.btnAddPrescription.setOnClickListener {
            val intent = Intent(context as Activity, AddPrescription::class.java)
                .putExtra("bookingId",list.result[position].id.toString())
            context.startActivity(intent)
        }

    }


    override fun getItemCount(): Int {
        return list.result.size

    }

    open class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
          val bookingDate: TextView = itemView.findViewById(R.id.tvBookingDatePPending)
          val startTime: TextView = itemView.findViewById(R.id.tvStartTimePPending)
          val coustmerName: TextView = itemView.findViewById(R.id.tvCoustmorNmaePPending)
          val title: TextView = itemView.findViewById(R.id.tvTitlePPending)
          val btnAddPrescription: Button = itemView.findViewById(R.id.btnAddPrescriptionPPending)


    }

}