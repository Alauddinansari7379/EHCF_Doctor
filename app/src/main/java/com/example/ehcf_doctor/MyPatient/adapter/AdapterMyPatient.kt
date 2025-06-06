package com.example.ehcf_doctor.MyPatient.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.ehcf.sharedpreferences.SessionManager
import com.example.ehcf_doctor.MyPatient.activity.PatientHistory
import com.example.ehcf_doctor.MyPatient.activity.PatientReport
import com.example.ehcf_doctor.MyPatient.model.ModelMyPatient
import com.example.ehcf_doctor.MyPatient.model.ResultMyPatient
import com.example.ehcf_doctor.R
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class AdapterMyPatient(
    val context: Context, private val list: ArrayList<ResultMyPatient>,    val comment:CommentList
) :
    RecyclerView.Adapter<AdapterMyPatient.MyViewHolder>() {
        lateinit var sessionManager: SessionManager
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(R.layout.single_row_my_patient, parent, false)
        )
    }
    var currentDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        try {
            sessionManager = SessionManager(context)
            if (list[position].profile_picture!!.isNotEmpty()) {
                Picasso.get()
                    .load("${sessionManager.imageUrl}${list[position].profile_picture}")
                    .placeholder(R.drawable.profile).error(R.drawable.profile)
                    .into(holder.imgProfile);


            }
            // holder.SrNo.text= "${position+1}"
            holder.name.text = list[position].customer_name
            holder.tvMobileNumberPHis.text = list[position].phone_number
            holder.email.text = list[position].email
            //  holder.doctorName.text = list.result[position].n.toString()

            if (list[position].overall_ratings != null) {
                holder.ratingBar.rating = list[position].overall_ratings.toFloat()
                holder.overAllRating.text = list[position].overall_ratings
                holder.tvNoOfrating.text = list[position].no_of_ratings
            }

            holder.viewHistory.setOnClickListener {
                val intent = Intent(context as Activity, PatientHistory::class.java)
                    .putExtra("Id", list[position].id.toString())
                context.startActivity(intent)
            }

            holder.tvRatingReview.setOnClickListener {
                comment.commentList(list[position].id)
            }
            holder.imgPhone.setOnClickListener {

                comment.videoCall(list[position].doctor_id.toString()+"EHCFHIS"+list[position].id)
//             val phone_intent = Intent(Intent.ACTION_CALL)
//            phone_intent.data = Uri.parse("tel:${list.result[position].phone_number}")
//            context.startActivity(phone_intent)


//                val intent = Intent(
//                    Intent.ACTION_CALL,
//                    Uri.parse("tel:${list[position].phone_number}")
//                )
//                context.startActivity(intent)
            }
            holder.viewReport.setOnClickListener {
                val intent = Intent(context as Activity, PatientReport::class.java)
                    .putExtra("Id", list[position].id.toString())
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
        val name: TextView = itemView.findViewById(R.id.tvNameMyPatient)
         val email: TextView = itemView.findViewById(R.id.tvEmailMyPatient)
        val overAllRating: TextView = itemView.findViewById(R.id.tvOverAllRating)
        val tvNoOfrating: TextView = itemView.findViewById(R.id.tvNoOfrating)
        val tvMobileNumberPHis: TextView = itemView.findViewById(R.id.tvMobileNumberPHis)
        val imgPhone: ImageView = itemView.findViewById(R.id.imgPhone)
        val tvRatingReview: TextView = itemView.findViewById(R.id.tvRatingReview)
        val cardView: CardView = itemView.findViewById(R.id.cardViewPre)
        val viewHistory: Button = itemView.findViewById(R.id.btnViewHistory)
        val viewReport: Button = itemView.findViewById(R.id.btnViewReports)
        val imgProfile: ImageView = itemView.findViewById(R.id.imgProfile)
        val ratingBar: RatingBar = itemView.findViewById(R.id.ratingBarDProfile)

    }
    interface CommentList {
        fun commentList(id: String)
        fun videoCall(id: String)
    }
}