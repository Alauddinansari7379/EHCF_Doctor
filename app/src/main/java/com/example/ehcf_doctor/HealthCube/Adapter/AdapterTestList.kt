package com.example.ehcf_doctor.HealthCube.Adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.ehcf_doctor.HealthCube.Model.ItemsViewModel
import com.example.ehcf_doctor.R

class AdapterTestList(private val mList: List<ItemsViewModel>) : RecyclerView.Adapter<AdapterTestList.ViewHolder>() {

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.single_row_diagnostic_list, parent, false)

        return ViewHolder(view)
    }
    private var lastCheckedPosition = -1


    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {

        val ItemsViewModel = mList[position]

        if (position == lastCheckedPosition) {
            holder.cardViewViewMember.setCardBackgroundColor(Color.parseColor("#FF4CAF50"));
        } else {
            holder.cardViewViewMember.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
        }


        holder.itemView.setOnClickListener {
            Log.e("lastCheckedPosition", lastCheckedPosition.toString())
            Log.e("position", position.toString())
            if (lastCheckedPosition==position){
                holder.cardViewViewMember.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                lastCheckedPosition=100
                 TestName =""
            }
            else{
                 TestName = ItemsViewModel.text
                lastCheckedPosition = position
                notifyItemRangeChanged(0, mList.size)
            }

        }
        // sets the image to the imageview from our itemHolder class

        // sets the text to the textview from our itemHolder class
        holder.textView.text = ItemsViewModel.text

    }
    companion object{
        var TestName=""
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
         val textView: TextView = itemView.findViewById(R.id.familyMember)
        val cardViewViewMember: CardView = itemView.findViewById(R.id.cardViewViewMember)

    }
}
