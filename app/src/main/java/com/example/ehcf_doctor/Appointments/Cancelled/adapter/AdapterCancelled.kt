package com.example.ehcf_doctor.Appointments.Cancelled.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.ehcf.Helper.convertTo12Hour
import com.example.ehcf_doctor.Appointments.Upcoming.activity.AppointmentDetalis
import com.example.ehcf_doctor.Booking.model.ModelGetConsultation
import com.example.ehcf_doctor.R
import com.squareup.picasso.Picasso


class AdapterCancelled(
    val context: Context, private val list: ModelGetConsultation
) :
    RecyclerView.Adapter<AdapterCancelled.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(R.layout.single_row_cancelled, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // holder.SrNo.text= "${position+1}"
        try {
            if (list.result[position].profile_picture!!.isNotEmpty()) {
                Picasso.get()
                    .load("https://ehcf.thedemostore.in/uploads/${list.result[position].profile_picture}")
                    .placeholder(R.drawable.profile).error(R.drawable.profile).into(holder.profile);


            }

            holder.appointmentDate.text = list.result[position].date
            holder.totalAmount.text = list.result[position].total.toString()
            holder.tvStatus.text = list.result[position].status_for_doctor
            if (list.result[position].start_time != null) {
                holder.startTime.text = convertTo12Hour(list.result[position].start_time.toString())
                holder.endTime.text = convertTo12Hour(list.result[position].end_time.toString())
            }

            // holder.description.text = list.result[position].description.toString()
            if (list.result[position].member_name != null) {
                holder.coustmorName.text = list.result[position].member_name

            } else {
                holder.coustmorName.text = list.result[position].customer_name.toString()
            }
            when (list.result[position].consultation_type) {
                "1" -> {
                    holder.tvConsultationTypeCan.text = "Tele-Consultation"
                }
                "2" -> {
                    holder.tvConsultationTypeCan.text = "Clinic-Visit"
                }
                "3" -> {
                    holder.tvConsultationTypeCan.text = "Home-Visit"
                }
            }
//        when (list.result[position].slug) {
//            "rejected" -> {
//               // holder.visibility(View.INVISIBLE);
//
//                holder.cardView.visibility = View.VISIBLE
//            }else->{
//
//            holder.cardView.visibility = View.GONE
//
//        }
//        }

            holder.cardView.setOnClickListener {
                val intent = Intent(context as Activity, AppointmentDetalis::class.java)
                    .putExtra("bookingId", list.result[position].id.toString())
                context.startActivity(intent)
            }

            // Glide.with(hol der.image).load(list[position].url).into(holder.image)

        }catch (e:Exception){
            e.printStackTrace()
        }

    }
    override fun getItemCount(): Int {
        return list.result.size

    }

    open class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val appointmentDate: TextView = itemView.findViewById(R.id.tvAppointmentDateCan)
        val coustmorName: TextView = itemView.findViewById(R.id.tvCoustmorNameCan)
        val startTime: TextView = itemView.findViewById(R.id.tvStartTimeCan)
        val endTime: TextView = itemView.findViewById(R.id.tvEndTimeCan)
        //val description: TextView = itemView.findViewById(R.id.tvDescriptionCan)
        val totalAmount: TextView = itemView.findViewById(R.id.tvTotalAmountCan)
        val tvConsultationTypeCan: TextView = itemView.findViewById(R.id.tvConsultationTypeCan)
        val tvStatus: TextView = itemView.findViewById(R.id.tvStatusCan)
        val profile: ImageView = itemView.findViewById(R.id.imgProfile)
        val cardView: CardView = itemView.findViewById(R.id.cardViewCan)

    }

}