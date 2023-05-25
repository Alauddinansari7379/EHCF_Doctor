package com.example.ehcf_doctor.ManageSlots.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Switch
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.ehcf_doctor.ManageSlots.activity.UpdateSlot
import com.example.ehcf_doctor.ManageSlots.model.ModelSlotList
import com.example.ehcf_doctor.ManageSlots.model.ModelSwitechButton
import com.example.ehcf_doctor.R


class AdapterSwitchButton(
    val context: Context, private val list: ModelSwitechButton,
    val ActiveSlot: AdapterSwitchButton.ActiveInactiveSlot

) :
    RecyclerView.Adapter<AdapterSwitchButton.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(R.layout.single_row_switch_button, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
       // holder.switch.text = list.result[position].active
        // holder.slotId.text= list.result[position].id.toString()

        when (list.result[position].active) {
            "1" -> {
                holder.switch.isChecked = true
                holder.switch.text="Active"

            }
            "0" -> {
                holder.switch.isChecked = false
                holder.switch.text="Inactive"

            }
        }

        when (list.result[position].day) {
            "1" -> {

                holder.day.text = "Monday"
            }
            "2" -> {
                holder.day.text = "Tuesday"
            }
            "3" -> {
                holder.day.text = "Wednesday"
            }
            "4" -> {
                holder.day.text = "Thursday"
            }
            "5" -> {
                holder.day.text = "Friday"
            }
            "6" -> {
                holder.day.text = "Saturday"
            }
            "7" -> {
                holder.day.text = "Sunday"
            }
        }

        holder.switch.setOnClickListener {
            if (holder.switch.isChecked) {
                ActiveSlot.inactiveSlot(list.result[position].day)

            } else {
                ActiveSlot.activeSlot(list.result[position].day)
            }
        }

        // Glide.with(hol der.image).load(list[position].url).into(holder.image)

    }


    override fun getItemCount(): Int {
        return list.result.size

    }

    open class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var day: TextView = itemView.findViewById(R.id.tvDaySwitchButton);
        var switch: Switch = itemView.findViewById(R.id.btnSwitch);


    }

    interface ActiveInactiveSlot {
        fun activeSlot(dayCode: String)
        fun inactiveSlot(dayCode: String)

    }

}