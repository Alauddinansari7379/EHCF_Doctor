package com.example.ehcf_doctor.ManageSlots.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.ehcf.Helper.convertTo12Hour
import com.example.ehcf_doctor.ManageSlots.model.ModelManageSlotRes
import com.example.ehcf_doctor.ManageSlots.model.ModelSlotList
import com.example.ehcf_doctor.R


class AdapterManageSlot(val context: Context, private val list: ModelSlotList) :
    RecyclerView.Adapter<AdapterManageSlot.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(R.layout.single_row_manage_slot, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // holder.SrNo.text= "${position+1}"

        holder.slotId.text= "${position+1}"
        holder.startTime.text= convertTo12Hour(list.result[position].start_time)
        holder.endTime.text= convertTo12Hour(list.result[position].end_time)
//        holder.name.text = list.result[position].category_name.toString()
//        Picasso.get().load(list.result[position].category_image).into(holder.image)


        // Glide.with(hol der.image).load(list[position].url).into(holder.image)

    }


    override fun getItemCount(): Int {
        return list.result.size

    }

    open class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var startTime: Button =itemView.findViewById(R.id.btnStartTimeManage);
        var endTime: Button =itemView.findViewById(R.id.btnEndTimeManage);
        var slotId: TextView =itemView.findViewById(R.id.tvSlotNumberManage);
        var cardView: CardView =itemView.findViewById(R.id.cardManage);

    }
}