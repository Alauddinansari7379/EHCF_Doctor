package com.example.ehcf_doctor.HealthCube.Adapter

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
import com.example.ehcf_doctor.HealthCube.activity.AddPatient
import com.example.ehcf_doctor.HealthCube.activity.HealthCubeTestHistory
import com.example.ehcf_doctor.HealthCube.activity.PatientList
 import com.example.ehcf_doctor.MyPatient.model.ResultMyPatient
import com.example.ehcf_doctor.R
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*


class AdapterPatientList(
    val context: Context, private val list: ArrayList<ResultMyPatient>,
) :
    RecyclerView.Adapter<AdapterPatientList.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(R.layout.single_row_my_patient_list, parent, false)
        )
    }
    var currentDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        try {
            if (list[position].profile_picture!=null) {
                Picasso.get()
                    .load("https://ehcf.thedemostore.in/uploads/${list[position].profile_picture}")
                    .placeholder(R.drawable.profile).error(R.drawable.profile)
                    .into(holder.imgProfile);
            }
            // holder.SrNo.text= "${position+1}"
            holder.name.text = list[position].name
            holder.tvMobileNumberPHis.text = list[position].phone
            holder.email.text = list[position].email
            //  holder.doctorName.text = list.result[position].n.toString()


            holder.viewReport.setOnClickListener {
                PatientList.Diagnostic ="1"
                val intent = Intent(context as Activity, AddPatient::class.java)
                    .putExtra("id", list[position].id)
                    .putExtra("customer_name", list[position].name)
                    .putExtra("phone_number", list[position].phone)
                    .putExtra("email", list[position].email)
                    .putExtra("gender", list[position].gender)
                    .putExtra("dob", list[position].dob)
                context.startActivity(intent)
            }

            holder.btnViewHistoryHealth.setOnClickListener {
                 val intent = Intent(context as Activity, HealthCubeTestHistory::class.java)
                    .putExtra("id", list[position].id)
                    .putExtra("customer_name", list[position].name)
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
         val tvMobileNumberPHis: TextView = itemView.findViewById(R.id.tvMobileNumberPHis)
          val cardView: CardView = itemView.findViewById(R.id.cardViewPre)
         val viewReport: Button = itemView.findViewById(R.id.btnViewReports)
         val btnViewHistoryHealth: Button = itemView.findViewById(R.id.btnViewHistoryHealth)
        val imgProfile: ImageView = itemView.findViewById(R.id.imgProfile)

    }
    interface CommentList {
        fun commentList(id: String)
    }
}