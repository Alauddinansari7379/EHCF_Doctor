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
import com.example.ehcf.Helper.convertTo12Hour
import com.example.ehcf_doctor.Booking.model.ModelGetConsultation
import com.example.ehcf_doctor.Prescription.activity.AddPrescription
import com.example.ehcf_doctor.Prescription.model.ModelPendingPre
import com.example.ehcf_doctor.R
import java.text.SimpleDateFormat
import java.util.*


class AdapterPrescription(
    val context: Context, private val list: ModelPendingPre
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
        Log.e("startTime","${list.result[position].time.toString()}")
        holder.startTime.text = convertTo12Hour(list.result[position].start_time)
        holder.endTime.text = convertTo12Hour(list.result[position].end_time)
       // holder.endTime.text = list.result[position].e
        holder.bookingDate.text = list.result[position].date
        holder.customerName.text = list.result[position].customer_name.toString()
        holder.specialitiesName.text = list.result[position].category_name.toString()

//        when (list.result[position].slug) {
//            "completed" -> {
//                // holder.visibility(View.INVISIBLE);
//
//                holder.cardView.visibility = View.VISIBLE
//            }
//            else -> {
//
//                holder.cardView.visibility = View.GONE
//
//           }
   //     }
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
          val specialitiesName: TextView = itemView.findViewById(R.id.tvSpecialitiesName)
          val startTime: TextView = itemView.findViewById(R.id.tvStartTimePPending)
          val endTime: TextView = itemView.findViewById(R.id.tvEndTimePending)
          val customerName: TextView = itemView.findViewById(R.id.tvCoustmorNmaePPending)
          val btnAddPrescription: Button = itemView.findViewById(R.id.btnAddPrescriptionPPending)
          val cardView: CardView = itemView.findViewById(R.id.cardViewPre)


    }

}