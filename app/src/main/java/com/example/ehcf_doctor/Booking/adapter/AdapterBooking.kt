package com.example.ehcf_doctor.Booking.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ehcf.Helper.convertTo12Hour
import com.example.ehcf_doctor.Booking.model.ModelGetConsultation
import com.example.ehcf_doctor.R
import java.text.SimpleDateFormat
import java.util.*


class AdapterBooking(
    val context: Context, private val list: ModelGetConsultation
) :
    RecyclerView.Adapter<AdapterBooking.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(R.layout.single_row_booking, parent, false)
        )
    }
    var currentDate: String = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // holder.SrNo.text= "${position+1}"
        Log.e("currentDate","$currentDate")
        Log.e("startTime","${list.result[position].time.toString()}")

        holder.time.text = convertTo12Hour(list.result[position].start_time.toString())
        holder.bookingDate.text = list.result[position].date
        holder.customerName.text = list.result[position].customer_name.toString()
        holder.paymentMode.text = list.result[position].payment_name
        holder.totalAmount.text = list.result[position].total
        holder.email.text = list.result[position].email
        holder.phoneNumber.text = list.result[position].phone_number.toString()
        holder.bookingId.text = list.result[position].id
        holder.status.text = list.result[position].status_for_doctor

        when(list.result[position].consultation_type){
            "1"->{
                holder.consultationType.text="Tele-Consultation"
            }
            "2"->{
                holder.consultationType.text="Clinic-Visit"
            }
            "3"->{
                holder.consultationType.text="Home-Visit"
            }
        }


//        holder.btnAddPrescription.setOnClickListener {
//            val intent = Intent(context as Activity, AddPrescription::class.java)
//                .putExtra("bookingId",list.result[position].id.toString())
//            context.startActivity(intent)
//        }

    }


    override fun getItemCount(): Int {
        return list.result.size

    }

    open class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
          val bookingDate: TextView = itemView.findViewById(R.id.tvDateBooking)
          val time: TextView = itemView.findViewById(R.id.tvTimeBooking)
          val customerName: TextView = itemView.findViewById(R.id.tvCoustmerNameBooking)
          val consultationType: TextView = itemView.findViewById(R.id.tvConsultationTypeBooking)
          val paymentMode: TextView = itemView.findViewById(R.id.tvPaymentModeBooking)
          val totalAmount: TextView = itemView.findViewById(R.id.tvTotalAmountBooking)
          val email: TextView = itemView.findViewById(R.id.tvEmailBooking)
          val phoneNumber: TextView = itemView.findViewById(R.id.tvPhoneNumberBooking)
          val bookingId: TextView = itemView.findViewById(R.id.tvBookingIdBooking)
          val status: TextView = itemView.findViewById(R.id.tvStatusBooking)





    }

}