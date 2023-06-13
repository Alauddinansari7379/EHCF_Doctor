package com.example.ehcf_doctor.Prescription.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.ehcf.Helper.convertTo12Hour
import com.example.ehcf.Helper.myToast
import com.example.ehcf_doctor.Appointments.Upcoming.adapter.AdapterUpComing
import com.example.ehcf_doctor.Prescription.activity.PrescriptionDetails
import com.example.ehcf_doctor.Prescription.activity.ViewReport
import com.example.ehcf_doctor.Prescription.model.ModelPrescribed
import com.example.ehcf_doctor.R
import com.rajat.pdfviewer.PdfViewerActivity
import java.text.SimpleDateFormat
import java.util.*


class AdapterPrescribed(
    val context: Context, private val list: ModelPrescribed,
) :
    RecyclerView.Adapter<AdapterPrescribed.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(R.layout.single_row_prescribed, parent, false)
        )
    }
    var currentDate: String = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())



    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // holder.SrNo.text= "${position+1}"
        holder.customerName.text = list.result[position].customer_name
        holder.startTime.text = convertTo12Hour(list.result[position].start_time)
        holder.endTime.text = convertTo12Hour(list.result[position].end_time)
        holder.bookingDate.text = list.result[position].date
        holder.specialitiesNamePrescribed.text = list.result[position].category_name
        //  holder.doctorName.text = list.result[position].n.toString()

        holder.btnViewReport.setOnClickListener {
            val intent = Intent(context as Activity, ViewReport::class.java)
            .putExtra("report",list.result[position].report.toString())
            .putExtra("clickId","1")
            context.startActivity(intent)
////

        }

        holder.btnViewPrescription.setOnClickListener {
            val intent = Intent(context as Activity, PrescriptionDetails::class.java)
            .putExtra("Id",list.result[position].pid)
            .putExtra("customerName",list.result[position].customer_name)
            .putExtra("date",list.result[position].date)
            context.startActivity(intent)

        }

    }




    override fun getItemCount(): Int {
        return list.result.size

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


    }
    interface PFDViewer {

        fun pDFViewer(reportData: String)
    }

}