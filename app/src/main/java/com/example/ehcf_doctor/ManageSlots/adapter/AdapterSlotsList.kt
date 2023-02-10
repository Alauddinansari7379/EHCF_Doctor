package com.example.ehcf_doctor.ManageSlots.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.ehcf_doctor.ManageSlots.activity.UpdateSlot
import com.example.ehcf_doctor.ManageSlots.model.ModelSlotList
import com.example.ehcf_doctor.R


class AdapterSlotsList(
    val context: Context, private val list: ModelSlotList,
    val deleteSlotApi: DeleteSlot
) :
    RecyclerView.Adapter<AdapterSlotsList.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(R.layout.single_row_slot_list, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
         holder.slotId.text= "${position+1}"
        holder.startTime.text= list.result[position].start_time
        holder.endTime.text= list.result[position].end_time
        holder.date.text= list.result[position].date
       // holder.slotId.text= list.result[position].id.toString()

        holder.delete.setOnClickListener {
            deleteSlotApi.deleteSlotApi(list.result[position].id.toString())

            // holder.textname.setBackgroundColor(Color.parseColor("#451DE4"))

        }
        holder.btnupdate.setOnClickListener {
            SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Are you sure want to Update?")
                .setCancelText("No")
                .setConfirmText("Yes")
                .showCancelButton(true)
                .setConfirmClickListener { sDialog ->
                    sDialog.cancel()
                    val intent = Intent(context as Activity, UpdateSlot::class.java)
                        .putExtra("slotId",list.result[position].id.toString())
                    context.startActivity(intent)

                }
                .setCancelClickListener { sDialog ->
                    sDialog.cancel()
                }
                .show()


        }

        // Glide.with(hol der.image).load(list[position].url).into(holder.image)

    }


    override fun getItemCount(): Int {
        return list.result.size

    }

    open class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var startTime:Button  =itemView.findViewById(R.id.btnStartTimeSlotL);
        var endTime:Button  =itemView.findViewById(R.id.btnEndTimeSlotL);
        var delete:TextView  =itemView.findViewById(R.id.imgDelete);
        var slotId:TextView  =itemView.findViewById(R.id.tvSlotNumber);
        var date:TextView  =itemView.findViewById(R.id.tvDateSlotList);
        var cardView:CardView  =itemView.findViewById(R.id.cardSlotList);
        var btnupdate:Button  =itemView.findViewById(R.id.btnUpdateSlotList);

    }
    interface DeleteSlot{
        fun deleteSlotApi(slotId:String)
        fun updateSlotApi(slotId:String)
        //  fun dismissPopup()

    }
}