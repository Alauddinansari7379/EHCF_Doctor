package com.example.ehcf_doctor.Appointments.Upcoming.adapter

import android.content.Context
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
import com.example.ehcf_doctor.R
import java.text.SimpleDateFormat
import java.util.*


class AdapterUpComing(
    val context: Context, private val list: ModelUpComingResponse, val confirmSlot: ConfirmSlot
) :
    RecyclerView.Adapter<AdapterUpComing.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(R.layout.single_row_upcoming, parent, false)
        )
    }
    var currentDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // holder.SrNo.text= "${position+1}"
        Log.e("currentDate","$currentDate")
        Log.e("startTime","${list.result[position].start_time.toString()}")
        holder.startTime.text = list.result[position].start_time.toString()
        holder.coustmerName.text = list.result[position].customer_name.toString()
        holder.bookingId.text = list.result[position].id.toString()
        holder.title.text = list.result[position].title.toString()
        holder.status.text = list.result[position].status_for_doctor.toString()

        holder.btnConfirm.setOnClickListener {
            val slug="booking_confirmed"
            confirmSlot.alretDilogConfirm(list.result[position].id.toString(),slug)
        }
        holder.btnReject.setOnClickListener {
            val slug="booking_rejected"
            confirmSlot.alretDilogReject(list.result[position].id.toString(),slug)
        }
        holder.btnCheck.setOnClickListener {
            confirmSlot.showPopup(list.result[position].start_time.toString())
        }
//        Picasso.get().load(list.result[position].category_image).into(holder.image)
        when (list.result[position].slug) {
            "booking_confirmed" -> {
                holder.btnCheck.visibility = View.VISIBLE
                holder.btnConfirm.visibility = View.GONE
                holder.btnReject.visibility = View.GONE
            }
            "booking_rejected" -> {
                holder.cardView.visibility = View.GONE
            }
            "waiting_for_confirmation" -> {
                holder.btnConfirm.visibility = View.VISIBLE
                holder.btnReject.visibility = View.VISIBLE
                holder.btnCheck.visibility = View.GONE

            }
        }
        if (list.result[position].start_time.toString()<=currentDate)
        {
                holder.btnStartMeeting.visibility = View.VISIBLE
                holder.btnCheck.visibility = View.GONE
                holder.btnConfirm.visibility = View.GONE
                holder.btnReject.visibility = View.GONE
            }
        holder.btnStartMeeting.setOnClickListener {
            confirmSlot.videoCall(list.result[position].start_time.toString())
        }

        // Glide.with(hol der.image).load(list[position].url).into(holder.image)

    }


    override fun getItemCount(): Int {
        return list.result.size

    }

    open class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
          val startTime: TextView = itemView.findViewById(R.id.tvStartTime)
          val coustmerName: TextView = itemView.findViewById(R.id.tvCoustmorNmae)
          val bookingId: TextView = itemView.findViewById(R.id.tvBookingId)
          val title: TextView = itemView.findViewById(R.id.tvTitleCan)
          val status: TextView = itemView.findViewById(R.id.tvStatus)
          val btnConfirm: Button = itemView.findViewById(R.id.btnConfirm)
          val btnCheck: Button = itemView.findViewById(R.id.btnCheck)
          val btnStartMeeting: Button = itemView.findViewById(R.id.btnStartMetting)
          val btnReject: Button = itemView.findViewById(R.id.btnReject)
          val cardView: CardView = itemView.findViewById(R.id.cardView)
//        val image: ImageView = itemView.findViewById(R.id.cardSpecia)
//        val cardView: CardView = itemView.findViewById(R.id.cardView)

    }
    interface ConfirmSlot{

        fun alretDilogConfirm(bookingId:String,slug:String)
        fun alretDilogReject(bookingId:String,slug:String)
        fun showPopup(startTime:String)
        fun videoCall(startTime:String)

    }
}