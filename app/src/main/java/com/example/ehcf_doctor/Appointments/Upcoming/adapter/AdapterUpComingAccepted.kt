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
import com.example.ehcf_doctor.Appointments.Upcoming.activity.AppointmentDetalis
import com.example.ehcf_doctor.Booking.model.ModelGetConsultation
import com.example.ehcf_doctor.R
import java.text.SimpleDateFormat
import java.util.*


class AdapterUpComingAccepted(
    val context: Context, private val list: ModelGetConsultation, val confirmSlot: ConfirmSlot
) :
    RecyclerView.Adapter<AdapterUpComingAccepted.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(R.layout.single_row_upcoming, parent, false)
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
        holder.startTime.text = list.result[position].time.toString()
        holder.status.text = list.result[position].status_for_doctor.toString()
        holder.consultationType.text = list.result[position].consultation_type.toString()

        when (list.result[position].consultation_type) {
            "1" -> {
                holder.consultationType.text = "Tele-Consultation"


            }
            "2" -> {
                holder.consultationType.text = "Clinic-Visit"


            }
            "3" -> {
                holder.consultationType.text = "Home-Visit"


            }


//            1 -> {
//                holder.btnStart.setBackgroundColor(Color.parseColor("#FF0000"))
//                holder.btnStart.text = "Stop"
//            }
//            2 -> {
//                holder.btnStart.setBackgroundColor(Color.parseColor("#119241"))
//                holder.btnStart.text = "Done"
//            }
        }

        holder.btnConfirm.setOnClickListener {
            val slug="accepted"
            confirmSlot.alretDilogConfirm(list.result[position].id.toString(),slug)
        }
        holder.btnReject.setOnClickListener {
           // val slug="booking_rejected"
            val slug="rejected"
            confirmSlot.alretDilogReject(list.result[position].id.toString(),slug)
        }
        holder.btnCheck.setOnClickListener {
            confirmSlot.popupRemainingTime(list.result[position].time)
        }
//        Picasso.get().load(list.result[position].category_image).into(holder.image)
        Log.e("currentDate", currentDate)
        Log.e("startTime", list.result[position].date+" "+list.result[position].time)

        if (list.result[position].date+" "+list.result[position].time<=currentDate && list.result[position].slug=="accepted")
        {
                holder.btnStartMeeting.visibility = View.VISIBLE
                holder.btnCheck.visibility = View.GONE
                holder.btnConfirm.visibility = View.GONE
                holder.btnReject.visibility = View.GONE
            }
        if (list.result[position].date+" "+list.result[position].time<=currentDate && list.result[position].slug=="accepted"
            && list.result[position].consultation_type=="2")
        {
                holder.btnStartMeeting.visibility = View.GONE
                holder.btnCheck.visibility = View.VISIBLE
                holder.btnConfirm.visibility = View.GONE
                holder.btnReject.visibility = View.GONE
            }
        if (list.result[position].date+" "+list.result[position].time<=currentDate && list.result[position].slug=="accepted"
            && list.result[position].consultation_type=="3")
        {
                holder.btnStartMeeting.visibility = View.GONE
                holder.btnCheck.visibility = View.VISIBLE
                holder.btnConfirm.visibility = View.GONE
                holder.btnReject.visibility = View.GONE
            }
        holder.btnStartMeeting.setOnClickListener {
            confirmSlot.videoCall(list.result[position].date+" "+list.result[position].time,list.result[position].id)
        }
        holder.btnView.setOnClickListener {
            val intent = Intent(context as Activity, AppointmentDetalis::class.java)
                .putExtra("bookingId",list.result[position].id.toString())
            context.startActivity(intent)
        }
        // Glide.with(hol der.image).load(list[position].url).into(holder.image)

    }


    override fun getItemCount(): Int {
        return list.result.size

    }

    open class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
          val date: TextView = itemView.findViewById(R.id.tvAppointmentDateUpcoming)
          val coustmerName: TextView = itemView.findViewById(R.id.tvCoustmerNameUpcoming)
         // val bookingId: TextView = itemView.findViewById(R.id.tvBookingId)
        //  val title: TextView = itemView.findViewById(R.id.tvTitleCan)
          val startTime: TextView = itemView.findViewById(R.id.tvStartTimeUpcoming)
          val status: TextView = itemView.findViewById(R.id.tvStatusUpcoming)
          val consultationType: TextView = itemView.findViewById(R.id.tvConsaltationTypeUpcoming)

          val btnConfirm: Button = itemView.findViewById(R.id.btnConfirmUpcoming)
          val btnCheck: Button = itemView.findViewById(R.id.btnCheckUpcoming)
          val btnStartMeeting: Button = itemView.findViewById(R.id.btnStartMeetingUpcoming)
          val btnReject: Button = itemView.findViewById(R.id.btnRejectUpcoming)
          val cardView: CardView = itemView.findViewById(R.id.cardUpcoming)
          val btnView: Button = itemView.findViewById(R.id.btnViewUpcoming)
//        val image: ImageView = itemView.findViewById(R.id.cardSpecia)
//        val cardView: CardView = itemView.findViewById(R.id.cardView)

    }
    interface ConfirmSlot{

        fun alretDilogConfirm(bookingId:String,slug:String)
        fun alretDilogReject(bookingId:String,slug:String)
        fun popupRemainingTime(startTime:String)
        fun videoCall(startTime:String,bookingId:String)

    }
}