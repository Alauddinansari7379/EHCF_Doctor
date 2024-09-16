//package com.example.ehcf.Helper
//
//import android.content.Context
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.TextView
//import androidx.cardview.widget.CardView
//import androidx.recyclerview.widget.RecyclerView
//import com.example.ehcf.CreateSlot.activity.MyAvailableSlot
//import com.example.ehcf.CreateSlot.model.ModelSlotResNew
//import com.example.ehcf_doctor.R
//
//
//class AdapterShuduleTiming( var context: Context,var arraylist: ModelSlotResNew, val showBookPopUp: MyAvailableSlot
//): RecyclerView.Adapter<AdapterShuduleTiming.Myholder>() {
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Myholder {
//
//        return Myholder(LayoutInflater.from(context).inflate(R.layout.single_row_slot_timing,parent,false))
//
//    }
//
//    override fun onBindViewHolder(holder: Myholder, position: Int) {
//
////        Glide.with(context).load(arraylist.get(position).languageImg.toString()).into(holder.data)
//
//
//      //  holder.date.text= arraylist.result[position].date
//        holder.startTime.text= arraylist.result[position].start_time
//        holder.endTime.text= arraylist.result[position].end_time
//        holder.slotId.text= arraylist.result[position].id.toString()
//        holder.tvSlotAddress.text= arraylist.result[position].address.toString()
//
//        holder.cardView.setOnClickListener {
//            showBookPopUp.showPopup(
//                arraylist.result?.get(position)?.start_time.toString(),
//                arraylist.result?.get(position)?.end_time.toString(),
//                arraylist.result?.get(position)?.id.toString(),
//            )
//           // holder.textname.setBackgroundColor(Color.parseColor("#451DE4"))
//
//        }
//
//    }
//
//
//    class Myholder(itemview: View) :RecyclerView.ViewHolder(itemview){
////
//        //var data:ImageView  =itemview.findViewById(R.id.shayari_rl);
//
//     //   var date:TextView  =itemview.findViewById(R.id.tvtDateSTime);
//        var startTime:TextView  =itemview.findViewById(R.id.tvStartTimeStime);
//        var endTime:TextView  =itemview.findViewById(R.id.tvEndTimeStime);
//        var slotId:TextView  =itemview.findViewById(R.id.tvSlotIdSTime)
//        var tvSlotAddress:TextView  =itemview.findViewById(R.id.tvSlotAddress)
//        var cardView:CardView  =itemview.findViewById(R.id.cardSpecia);
//
//    }
//
//
//    override fun getItemCount(): Int {
//
//        return arraylist.result!!.size;
//
//    }
//    interface BookPopUp{
//        fun showPopup(slotTime:String,slotTimeValue:String)
//    }
//}
//
