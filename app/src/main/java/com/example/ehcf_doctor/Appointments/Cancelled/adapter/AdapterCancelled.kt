package com.example.ehcf_doctor.Appointments.Cancelled.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.ehcf_doctor.Appointments.Upcoming.model.ModelUpComingResponse
import com.example.ehcf_doctor.R


class AdapterCancelled(
    val context: Context, private val list: ModelUpComingResponse
) :
    RecyclerView.Adapter<AdapterCancelled.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(R.layout.single_row_cancelled, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // holder.SrNo.text= "${position+1}"

        holder.appointmentDate.text = list.result[position].start_time!!.substring(0,10)
        holder.totalAmount.text = list.result[position].total_amount.toString()
        holder.tvStatus.text = list.result[position].status_for_doctor.toString()
        holder.description.text = list.result[position].description.toString()
        holder.coustmorName.text = list.result[position].customer_name.toString()

        when (list.result[position].slug) {
            "booking_rejected" -> {
                holder.cardView.visibility = View.VISIBLE
            }else->{
            holder.cardView.visibility = View.GONE

        }
        }

        // Glide.with(hol der.image).load(list[position].url).into(holder.image)

    }


    override fun getItemCount(): Int {
        return list.result.size

    }

    open class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val appointmentDate: TextView = itemView.findViewById(R.id.tvAppointmentDateCan)
        val coustmorName: TextView = itemView.findViewById(R.id.tvCoustmorNameCan)
        val startTime: TextView = itemView.findViewById(R.id.tvStartTimeCan)
        val description: TextView = itemView.findViewById(R.id.tvDescriptionCan)
        val totalAmount: TextView = itemView.findViewById(R.id.tvTotalAmountCan)
        val tvStatus: TextView = itemView.findViewById(R.id.tvStatusCan)
        val profile: ImageView = itemView.findViewById(R.id.imgProfile)
        val cardView: CardView = itemView.findViewById(R.id.cardViewCan)

    }

}