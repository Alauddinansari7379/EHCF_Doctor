package com.example.ehcf_doctor.Prescription.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.ehcf.Helper.convertTo12Hour
import com.example.ehcf.Helper.myToast
import com.example.ehcf_doctor.Appointments.Upcoming.adapter.AdapterUpComing
import com.example.ehcf_doctor.Prescription.activity.PrescriptionDetails
import com.example.ehcf_doctor.Prescription.activity.ReportList
import com.example.ehcf_doctor.Prescription.activity.ViewReport
import com.example.ehcf_doctor.Prescription.model.ModelPrescribed
import com.example.ehcf_doctor.Prescription.model.ResultPrePrescribed
import com.example.ehcf_doctor.R
import com.rajat.pdfviewer.PdfViewerActivity
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class AdapterPrescribed(
    val context: Context, private val list: ArrayList<ResultPrePrescribed>,
) :
    RecyclerView.Adapter<AdapterPrescribed.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(R.layout.single_row_prescribed, parent, false)
        )
    }
    var currentDate: String = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())



    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        try {
            if (list[position].profile_picture!!.isNotEmpty()) {
                Picasso.get()
                    .load("https://ehcf.thedemostore.in/uploads/${list[position].profile_picture}")
                    .placeholder(R.drawable.profile).error(R.drawable.profile)
                    .into(holder.imgProfile);


            }
            // holder.SrNo.text= "${position+1}"
            var custmorName = ""

            if (list[position].member_name != null) {
                custmorName = list[position].member_name
                holder.customerName.text = list[position].member_name

            } else {
                holder.customerName.text = list[position].customer_name.toString()
                custmorName = list[position].customer_name

            }
            if (list[position].start_time != null) {
                holder.startTime.text = convertTo12Hour(list[position].start_time)
                holder.endTime.text = convertTo12Hour(list[position].end_time)
            }

            holder.bookingDate.text = list[position].date
            holder.specialitiesNamePrescribed.text = list[position].category_name
            //  holder.doctorName.text = list.result[position].n.toString()

           // if (list.result[position].te)

            holder.btnViewReport.setOnClickListener {
                val intent = Intent(context as Activity, ReportList::class.java)
                    .putExtra("report", list[position].report.toString())
                    .putExtra("id", list[position].pid.toString())
                    .putExtra("clickId", "1")
                context.startActivity(intent)
////

            }

            holder.btnViewPrescription.setOnClickListener {
                val intent = Intent(context as Activity, PrescriptionDetails::class.java)
                    .putExtra("Id", list[position].pid)
                    .putExtra("customerName", custmorName)
                    .putExtra("date", list[position].date)
                context.startActivity(intent)

            }

        }catch (e:Exception){
            e.printStackTrace()
        }


    }
    override fun getItemCount(): Int {
        return list.size

    }

    open class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val bookingDate: TextView = itemView.findViewById(R.id.tvBookingDatePrescribed)
        val specialitiesNamePrescribed: TextView = itemView.findViewById(R.id.tvSpecialitiesNamePrescribed)
        val customerName: TextView = itemView.findViewById(R.id.tvDoctorNamePrescribed)
        val startTime: TextView = itemView.findViewById(R.id.tvStartTimePrescribed)
        val endTime: TextView = itemView.findViewById(R.id.tvEndTimePrescribed)
        val btnViewReport: Button = itemView.findViewById(R.id.btnAddReportPrescribedPre)
        val btnViewPrescription: Button = itemView.findViewById(R.id.btnViewPrescriptionPrescribed)
        //  val btnAddPrescription: Button = itemView.findViewById(R.id.btnAddPrescriptionPPending)
        val cardView: CardView = itemView.findViewById(R.id.cardViewPre)
        val imgProfile: ImageView = itemView.findViewById(R.id.imgProfile)


    }
    interface PFDViewer {

        fun pDFViewer(reportData: String)
    }

}