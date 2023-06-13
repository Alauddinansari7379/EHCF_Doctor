package com.example.ehcf.Prescription.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
 import com.example.ehcf.sharedpreferences.SessionManager
import com.example.ehcf_doctor.Prescription.model.ModelPreDetJava
import com.example.ehcf_doctor.R


class AdapterPrescriptionDetial(val context: Context, private val list: ModelPreDetJava
) :
    RecyclerView.Adapter<AdapterPrescriptionDetial.MyViewHolder>() {
    private lateinit var sessionManager: SessionManager


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context)
                .inflate(R.layout.transation_item, parent, false)
        )

    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // holder.SrNo.text= "${position+1}"
        sessionManager = SessionManager(context)
        holder.amt.text = list.medicine[position].medicineName
        holder.paymentStatus.text = list.medicine[position].timing
        holder.payMethod.text = list.medicine[position].intake
        holder.orderNum.text = list.medicine[position].frequency
        holder.tansNum.text = list.medicine[position].duration
//        holder.address.text = list.result[position].address
//        holder.registration.text = list.result[position].registration
//        holder.note.text = list.result[position].doctorNotes
//        holder.coustmorName.text = sessionManager.customerName.toString()
//        holder.date.text = list.result[position].date
//        holder.subjectiveInformation.text = list.result[position].subjectiveInformation
//        holder.objectiveInformation.text = list.result[position].objectiveInformation
//        holder.assessment.text = list.result[position].assessment
//        holder.plan.text = list.result[position].plan
//        holder.tvRegistrationPreDetial.text = list.result[position].registration

       // holder.endTime.text = list.result[position].end_time

//        when(list.result[position].gender){
//            "1"->{
//                holder.gender.text="Male"
//            }
//            "2"->{
//                holder.gender.text="Female"
//            }
//            else->{
//                holder.gender.text="Other"
//            }
//        }

    }

    override fun getItemCount(): Int {
        return list.medicine.size

    }

    open class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val medicines: TextView = itemView.findViewById(R.id.Medicines)
//        val timing: TextView = itemView.findViewById(R.id.Timing)
//        val intake: TextView = itemView.findViewById(R.id.Intake)
//        val frequency: TextView = itemView.findViewById(R.id.Frequency)
//        val duration: TextView = itemView.findViewById(R.id.Duration)

        val amt: TextView = itemView.findViewById(R.id.amt)
        val paymentStatus: TextView = itemView.findViewById(R.id.paymentStatus)
        val payMethod: TextView = itemView.findViewById(R.id.payMethod)
        val orderNum: TextView = itemView.findViewById(R.id.orderNum)
        val tansNum: TextView = itemView.findViewById(R.id.tansNum)

        //      val doctorName: TextView = itemView.findViewById(R.id.tvDoctorNamePreDetial)
//        val address: TextView = itemView.findViewById(R.id.tvAddressPreDetial)
//        val registration: TextView = itemView.findViewById(R.id.tvRegistrationPreDetial)
//        val gender: TextView = itemView.findViewById(R.id.tvGenderPreDetial)
//        val note: TextView = itemView.findViewById(R.id.doctor_notes)
//        val subjectiveInformation: TextView = itemView.findViewById(R.id.subjectiveInformation)
//        val objectiveInformation: TextView = itemView.findViewById(R.id.objectiveInformation)
//        val assessment: TextView = itemView.findViewById(R.id.assessment)
//        val plan: TextView = itemView.findViewById(R.id.plan)
//        val tvRegistrationPreDetial: TextView = itemView.findViewById(R.id.tvRegistrationPreDetial)
//        val coustmorName: TextView = itemView.findViewById(R.id.tvCoustmorNamePreDet)
//        val date: TextView = itemView.findViewById(R.id.tvDatePreDetialPreDetial)
//        val startTime: TextView = itemView.findViewById(R.id.tvStartTimePPending)
//        val endTime: TextView = itemView.findViewById(R.id.tvEndTimePending)



    }

}