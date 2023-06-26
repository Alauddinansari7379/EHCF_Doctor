package com.example.ehcf_doctor.Appointments.Upcoming.adapter

import android.annotation.SuppressLint
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
import com.example.ehcf.Helper.convertTo12Hour
import com.example.ehcf_doctor.Appointments.Appointments
import com.example.ehcf_doctor.Appointments.Upcoming.activity.AppointmentDetalis
import com.example.ehcf_doctor.Appointments.Upcoming.model.ModelUpComingResponse
import com.example.ehcf_doctor.Booking.model.ModelGetConsultation
import com.example.ehcf_doctor.R
import java.text.SimpleDateFormat
import java.util.*


class AdapterHome(
    val context: Context, private val list: ModelGetConsultation,
) :
    RecyclerView.Adapter<AdapterHome.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(R.layout.single_row_home, parent, false)
        )
    }
    var currentDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())


    @SuppressLint("LogNotTimber")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // holder.SrNo.text= "${position+1}"
//        Log.e("currentDate","$currentDate")
//        Log.e("startTime","${list.result[position].date.toString()}")
        holder.date.text = list.result[position].date.toString()
        holder.coustmerName.text = list.result[position].customer_name.toString()
       // holder.bookingId.text = list.result[position].id.toString()
       // holder.title.text = list.result[position].title.toString()
        if(list.result[position].start_time.toString()!="null"){
            holder.startTime.text = convertTo12Hour(list.result[position].start_time.toString())
            holder.endTime.text = convertTo12Hour(list.result[position].end_time.toString())
        }

        holder.status.text = list.result[position].status_for_doctor.toString()

        holder.cardView.setOnClickListener {
            val intent = Intent(context as Activity, Appointments::class.java)
            context.startActivity(intent)
        }



        when (list.result[position].consultation_type) {
            "1" -> {
                holder.consaltationType.text = "Tele-Consultation"


            }
            "2" -> {
                holder.consaltationType.text = "Clinic-Visit"


            }
            "3" -> {
                holder.consaltationType.text = "Home-Visit"


            }
        }
//        Picasso.get().load(list.result[position].category_image).into(holder.image)





        Log.e("currentDate", currentDate)
        Log.e("startTime", list.result[position].date+" "+list.result[position].time)

        // Glide.with(hol der.image).load(list[position].url).into(holder.image)

    }


    override fun getItemCount(): Int {
        return list.result.size

    }

    open class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
          val date: TextView = itemView.findViewById(R.id.tvAppointmentDateHome)
          val coustmerName: TextView = itemView.findViewById(R.id.tvCoustmerNameHome)
         // val bookingId: TextView = itemView.findViewById(R.id.tvBookingId)
        //  val title: TextView = itemView.findViewById(R.id.tvTitleCan)
          val startTime: TextView = itemView.findViewById(R.id.tvStartTimeHome)
          val endTime: TextView = itemView.findViewById(R.id.tvEndTimeHome)
          val cardView: CardView = itemView.findViewById(R.id.cardUpcomingHome)
          val status: TextView = itemView.findViewById(R.id.tvStatusHome)
          val consaltationType: TextView = itemView.findViewById(R.id.tvConsaltationTypeHome)

//          val btnConfirm: Button = itemView.findViewById(R.id.btnConfirmUpcoming)
//          val btnCheck: Button = itemView.findViewById(R.id.btnCheckUpcoming)
//          val btnStartMeeting: Button = itemView.findViewById(R.id.btnStartMeetingUpcoming)
//          val btnReject: Button = itemView.findViewById(R.id.btnRejectUpcoming)
//          val cardView: CardView = itemView.findViewById(R.id.cardUpcoming)
//          val btnView: Button = itemView.findViewById(R.id.btnViewUpcoming)
////        val image: ImageView = itemView.findViewById(R.id.cardSpecia)
////        val cardView: CardView = itemView.findViewById(R.id.cardView)

    }

}