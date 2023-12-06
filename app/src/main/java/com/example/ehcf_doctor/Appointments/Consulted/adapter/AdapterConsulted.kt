package com.example.ehcf_doctor.Appointments.Consulted.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.ehcf.Helper.convertTo12Hour
import com.example.ehcf_doctor.Appointments.Upcoming.activity.AppointmentDetalis
import com.example.ehcf_doctor.Booking.model.ModelGetConsultation
import com.example.ehcf_doctor.Booking.model.ResultUpcoming
import com.example.ehcf_doctor.R
import com.example.ehcf_doctor.Rating.Rating
import com.squareup.picasso.Picasso


class AdapterConsulted(
    val context: Context, private val list: ArrayList<ResultUpcoming>,
) :
    RecyclerView.Adapter<AdapterConsulted.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(R.layout.single_row_cancelled, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // holder.SrNo.text= "${position+1}"
        try {
            if (list[position].profile_picture!!.isNotEmpty()) {
                Picasso.get()
                    .load("https://ehcf.thedemostore.in/uploads/${list[position].profile_picture}")
                    .placeholder(R.drawable.profile).error(R.drawable.profile).into(holder.profile);


            }

            holder.appointmentDate.text = list[position].date!!
            holder.totalAmount.text = list[position].total.toString()
            holder.tvStatus.text = list[position].status_name.toString()
            if (list[position].member_name != null) {
                holder.coustmorName.text = list[position].member_name

            } else {
                holder.coustmorName.text = list[position].customer_name.toString()
            }
            if (list[position].start_time != null) {
                holder.startTime.text = convertTo12Hour(list[position].start_time.toString())
                holder.endTime.text = convertTo12Hour(list[position].end_time.toString())
            }

            // holder.description.text = list.result[position].description.toString()
          //  holder.slag.text = list.result[position].slug.toString()

            if (list[position].customer_rating == "0") {
                holder.btnSubmitReview.visibility = View.VISIBLE
            }
            holder.btnSubmitReview.setOnClickListener {
                val intent = Intent(context as Activity, Rating::class.java)
                    .putExtra("meetingId", list[position].id.toString())
                context.startActivity(intent)
            }
            when (list[position].consultation_type) {
                "1" -> {
                    holder.consultationTypeCan.text = "Tele-Consultation"
                }
                "2" -> {
                    holder.consultationTypeCan.text = "Clinic-Visit"
                }
                "3" -> {
                    holder.consultationTypeCan.text = "Home-Visit"
                }
            }
            holder.cardView.setOnClickListener {
                val intent = Intent(context as Activity, AppointmentDetalis::class.java)
                    .putExtra("bookingId", list[position].id.toString())
                context.startActivity(intent)
            }

        }catch (e:Exception){
            e.printStackTrace()
        }
        // Glide.with(hol der.image).load(list[position].url).into(holder.image)


    }


    override fun getItemCount(): Int {
        return list.size

    }

    open class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val appointmentDate: TextView = itemView.findViewById(R.id.tvAppointmentDateCan)
        val coustmorName: TextView = itemView.findViewById(R.id.tvCoustmorNameCan)
        val startTime: TextView = itemView.findViewById(R.id.tvStartTimeCan)
        val endTime: TextView = itemView.findViewById(R.id.tvEndTimeCan)
        val consultationTypeCan: TextView = itemView.findViewById(R.id.tvConsultationTypeCan)
         //val description: TextView = itemView.findViewById(R.id.tvDescriptionCan)
        val totalAmount: TextView = itemView.findViewById(R.id.tvTotalAmountCan)
        val tvStatus: TextView = itemView.findViewById(R.id.tvStatusCan)
        val btnSubmitReview: Button = itemView.findViewById(R.id.btnSubmitReview)
        val profile: ImageView = itemView.findViewById(R.id.imgProfile)
        val cardView: CardView = itemView.findViewById(R.id.cardViewCan)

    }
}