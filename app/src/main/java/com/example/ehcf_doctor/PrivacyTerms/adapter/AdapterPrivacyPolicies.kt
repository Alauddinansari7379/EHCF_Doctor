package com.example.ehcf_doctor.PrivacyTerms.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ehcf_doctor.MyPatient.model.ModelMyPatient
import com.example.ehcf_doctor.PrivacyTerms.model.ModelPrivacyPolicies
import com.example.ehcf_doctor.R
import java.text.SimpleDateFormat
import java.util.*


class AdapterPrivacyPolicies(
    val context: Context, private val list: ModelPrivacyPolicies
) :
    RecyclerView.Adapter<AdapterPrivacyPolicies.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(R.layout.single_row_privacy_policy, parent, false)
        )
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // holder.SrNo.text= "${position+1}"
        holder.about.text = list.result[position].title
        holder.aboutDescription.text = list.result[position].description

   

    }


    override fun getItemCount(): Int {
        return list.result.size

    }

    open class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val about: TextView = itemView.findViewById(R.id.tvAboutEHCFPrivacy)
        val aboutDescription: TextView = itemView.findViewById(R.id.tvAboutDescriptionPrivacy)
        val title: TextView = itemView.findViewById(R.id.tvTitlePrivacy)
        val titleDescription: TextView = itemView.findViewById(R.id.tvTitleDescriptionPrivacy)

    }

}