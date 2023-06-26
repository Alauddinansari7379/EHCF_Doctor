package com.example.ehcf_doctor.MyPatient.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import com.example.ehcf_doctor.MyPatient.model.ModelCommentList
import com.example.ehcf_doctor.R


class AdapterCommentList
    (
    val context: Context,
    private val list: ModelCommentList,
 ) :
    RecyclerView.Adapter<AdapterCommentList.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(R.layout.single_comment_list, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

             holder.comment.text= list.result[position].customer_comments
              holder.tvOverAllRatingComment.text= list.result[position].customer_rating.toString()
              holder.tvDoctorNameComment.text= list.result[position].doctor_name.toString()
            holder.ratingBarComment.rating= list.result[position].customer_rating.toFloat()



        }



    override fun getItemCount(): Int {
        return list.result.size

    }

    open class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val comment: TextView = itemView.findViewById(R.id.tvComment)
        val tvOverAllRatingComment: TextView = itemView.findViewById(R.id.tvOverAllRatingComment)
        val tvDoctorNameComment: TextView = itemView.findViewById(R.id.tvDoctorNameComment)
        val ratingBarComment: RatingBar = itemView.findViewById(R.id.ratingBarComment)



    }
}