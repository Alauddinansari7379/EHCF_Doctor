package com.example.ehcf_doctor.Prescription.adapter

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
 import com.example.ehcf.report.model.ModelGetTest
import com.example.ehcf_doctor.Prescription.activity.ViewReport
import com.example.ehcf_doctor.R
import java.text.SimpleDateFormat
import java.util.*


class AdapterViewReportTest(
    val context: Context,
    private val list: ModelGetTest,
) :
    RecyclerView.Adapter<AdapterViewReportTest.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(R.layout.single_row_view_report, parent, false)
        )
    }

    var currentDate: String =
        SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        // holder.SrNo.text= "${position+1}"
        try {
            holder.tvTestNAme.text = list.result[position].test_name
            holder.tvAfter.text = list.result[position].after
            holder.tvInstraction.text = list.result[position].instructions
            //holder.tvReportMemberName.text = list.result[position].
            if (list.result[position].test_report==null){
                holder.btnViewReport.visibility=View.GONE
                holder.tvReportNotUploaded.visibility=View.VISIBLE
            }

//        holder.doctorName.text = list.result[position].doctor_name.toString()
//        holder.startTime.text = list.result[position].time
//        holder.tvStatus.text = list.result[position].status_for_customer
//        holder.consultationType.text = list.result[position].consultation_type

//        if (list.result[position].is_test=="0") {
//                holder.cardView.visibility = View.GONE
//
//        }

//        holder.bookingId.text = list.result.upcoming[position].id.toString()
//        holder.title.text = list.result[position].title.toString()
//        holder.status.text = list.result[position].status_name.toString()
            //   Picasso.get().load(list.result.upcoming[position].profile_image).into(holder.profile)
//
//        holder.btnCheck.setOnClickListener {
//            showPopUp.showPopup()
//
//        }
//        holder.selectImage.setOnClickListener {
//         //   uploadAdd.selectImage()
//            holder.btnUpload.isEnabled = true
//            holder.btnUpload.text = "Upload"
//
//        }

            holder.btnViewReport.setOnClickListener {
                val intent = Intent(context as Activity, ViewReport::class.java)
                    .putExtra("report", list.result[position].test_report.toString())
                context.startActivity(intent)
            }

//        holder.btnJoinMeeting.setOnClickListener {
//            showPopUp.videoCall(list.result[position].time)
//
//        }
//        holder.btnUpload.setOnClickListener {
//            val intent = Intent(context as Activity, AppointmentDetails::class.java)
//                .putExtra("bookingId",list.result[position].id.toString())
//            context.startActivity(intent)
//        }
//        holder.btnOkDialog.setOnClickListener {
//            showPopUp.dismissPopup()
//
//        }

            // Glide.with(hol der.image).load(list[position].url).into(holder.image)

        }catch (e:Exception){
            e.printStackTrace()
        }
    }


    override fun getItemCount(): Int {
        return list.result.size

    }

    open class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val btnViewReport: TextView = itemView.findViewById(R.id.btnViewReportViewAll)
        val tvTestNAme: TextView = itemView.findViewById(R.id.tvTestNAme)
        val tvAfter: TextView = itemView.findViewById(R.id.tvAfter)
        val tvInstraction: TextView = itemView.findViewById(R.id.tvInstraction)
        val tvReportNotUploaded: TextView = itemView.findViewById(R.id.tvReportNotUploaded)
        val cardView: CardView = itemView.findViewById(R.id.cardViewViewRe)


    }

    interface UploadImage {
        fun selectImage()
        fun upload(id: String)
        //  fun dismissPopup()

    }
}