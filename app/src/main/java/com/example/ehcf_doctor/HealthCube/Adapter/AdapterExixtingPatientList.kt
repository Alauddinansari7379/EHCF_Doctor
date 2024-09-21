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
import com.example.ehcf.sharedpreferences.SessionManager
import com.example.ehcf_doctor.HealthCube.activity.AddPatient
import com.example.ehcf_doctor.HealthCube.activity.PatientList
import com.example.ehcf_doctor.MyPatient.model.ModelMyPatient
import com.example.ehcf_doctor.R
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*


class AdapterExixtingPatientList(
    val context: Context, private val list: ModelMyPatient,
) :
    RecyclerView.Adapter<AdapterExixtingPatientList.MyViewHolder>() {
        lateinit var sessionManager: SessionManager
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(R.layout.single_row_my_patient_list, parent, false)
        )
    }
    var currentDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        try {
            sessionManager = SessionManager(context)
            if (list.result[position].profile_picture!=null) {
                Picasso.get()
                    .load("${sessionManager.imageUrl}${list.result[position].profile_picture}")
                    .placeholder(R.drawable.profile).error(R.drawable.profile)
                    .into(holder.imgProfile);
            }
            // holder.SrNo.text= "${position+1}"
            holder.name.text = list.result[position].name
            holder.tvMobileNumberPHis.text = list.result[position].phone
            holder.email.text = list.result[position].email
            //  holder.doctorName.text = list.result[position].n.toString()


            holder.viewReport.setOnClickListener {
                PatientList.Exsting ="1"
                val intent = Intent(context as Activity, AddPatient::class.java)
                    .putExtra("patient_id", list.result[position].patient_id)
                    .putExtra("customer_name", list.result[position].name)
                    .putExtra("phone_number", list.result[position].phone)
                    .putExtra("email", list.result[position].email)
                    .putExtra("gender", list.result[position].gender)
                    .putExtra("dob", list.result[position].dob)
                    .putExtra("patient_id", list.result[position].patient_id)
                context.startActivity(intent)
            }
        }catch (e:Exception){
            e.printStackTrace()
        }
    }


    override fun getItemCount(): Int {
        return list.result.size

    }

    open class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.tvNameMyPatient)
         val email: TextView = itemView.findViewById(R.id.tvEmailMyPatient)
         val tvMobileNumberPHis: TextView = itemView.findViewById(R.id.tvMobileNumberPHis)
          val cardView: CardView = itemView.findViewById(R.id.cardViewPre)
         val viewReport: Button = itemView.findViewById(R.id.btnViewReports)
        val imgProfile: ImageView = itemView.findViewById(R.id.imgProfile)

    }
    interface CommentList {
        fun commentList(id: String)
    }
}