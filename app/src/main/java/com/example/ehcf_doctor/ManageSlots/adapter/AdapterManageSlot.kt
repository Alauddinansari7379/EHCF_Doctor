package com.example.ehcf_doctor.ManageSlots.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ehcf_doctor.ManageSlots.model.ModelManageSlotRes
import com.example.ehcf_doctor.R


class AdapterManageSlot(val context: Context, private val list: ModelManageSlotRes) :
    RecyclerView.Adapter<AdapterManageSlot.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(R.layout.single_row_manage_slot, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // holder.SrNo.text= "${position+1}"

//        holder.name.text = list.result[position].category_name.toString()
//        Picasso.get().load(list.result[position].category_image).into(holder.image)


        // Glide.with(hol der.image).load(list[position].url).into(holder.image)

    }


    override fun getItemCount(): Int {
        return list.result.size

    }

    open class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val name: TextView = itemView.findViewById(R.id.tvNameSpeci)
//        val image: ImageView = itemView.findViewById(R.id.cardSpecia)
//        val cardView: CardView = itemView.findViewById(R.id.cardView)


    }
}