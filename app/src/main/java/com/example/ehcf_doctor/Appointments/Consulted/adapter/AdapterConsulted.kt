package com.example.ehcf_doctor.Appointments.Consulted.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.ehcf_doctor.Appointments.Upcoming.model.ModelUpComingResponse
import com.example.ehcf_doctor.R


class AdapterConsulted(
    val context: Context, private val list: ModelUpComingResponse,
) :
    RecyclerView.Adapter<AdapterConsulted.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(R.layout.single_row_upcoming, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // holder.SrNo.text= "${position+1}"

        holder.startTime.text = list.result[position].start_time.toString()
        holder.coustmerName.text = list.result[position].customer_name.toString()
        holder.bookingId.text = list.result[position].id.toString()
        holder.title.text = list.result[position].title.toString()
        holder.status.text = list.result[position].status_for_doctor.toString()


//        Picasso.get().load(list.result[position].category_image).into(holder.image)
//        when (list.result[position].slug) {
//            "booking_confirmed" -> {
//                holder.btnCheck.visibility = View.VISIBLE
//                holder.btnConfirm.visibility = View.GONE
//                holder.btnReject.visibility = View.GONE
//            }
////            "booking_rejected" -> {
////                holder.cardView.visibility = View.GONE
////            }
//            "waiting_for_confirmation" -> {
//                holder.btnConfirm.visibility = View.VISIBLE
//                holder.btnReject.visibility = View.VISIBLE
//                holder.btnCheck.visibility = View.GONE
//




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
          val btnReject: Button = itemView.findViewById(R.id.btnReject)
          val cardView: CardView = itemView.findViewById(R.id.cardView)
//        val image: ImageView = itemView.findViewById(R.id.cardSpecia)
//        val cardView: CardView = itemView.findViewById(R.id.cardView)

    }
    interface ConfirmSlot{

        fun alretDilogConfirm(bookingId:String,slug:String)
        fun alretDilogReject(bookingId:String,slug:String)
        fun showPopup()

    }
}