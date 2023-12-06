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
import com.example.ehcf_doctor.HealthCube.Model.ModelTestHistory
import com.example.ehcf_doctor.HealthCube.activity.AddPatient
import com.example.ehcf_doctor.HealthCube.activity.PatientList
import com.example.ehcf_doctor.MyPatient.model.ModelMyPatient
import com.example.ehcf_doctor.R
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*


class AdapterTestHistory(
    val context: Context, private val list: ModelTestHistory,
) :
    RecyclerView.Adapter<AdapterTestHistory.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(R.layout.single_row_test_history, parent, false)
        )
    }
    var currentDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        try {

            // holder.SrNo.text= "${position+1}"
            holder.name.text = list.result[position].name
            holder.testName.text = list.result[position].test_name
            holder.tvTestDateHistory.text = list.result[position].date
             //  holder.doctorName.text = list.result[position].n.toString()


            holder.viewReport.setOnClickListener {
                PatientList.TestHistory ="1"
                val intent = Intent(context as Activity, AddPatient::class.java)
                    .putExtra("id", list.result[position].externalPatientId)
                    .putExtra("date", list.result[position].date)
                    .putExtra("customer_name", list.result[position].name)

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
        val name: TextView = itemView.findViewById(R.id.tvNameMyPatientHistory)
          val testName: TextView = itemView.findViewById(R.id.tvTestNameHistory)
          val viewReport: Button = itemView.findViewById(R.id.btnViewReportHistory)
          val tvTestDateHistory: TextView = itemView.findViewById(R.id.tvTestDateHistory)

    }
    interface CommentList {

        fun commentList(id: String)
    }
}