package com.example.ehcf_doctor.HealthCube.Adapter

import android.content.ClipData
import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.ehcf_doctor.HealthCube.Adapter.ItemAdapter.RecyclerViewHolder
import com.example.ehcf_doctor.R

class ItemAdapter(var context: Context,val testList:TestList) : RecyclerView.Adapter<RecyclerViewHolder>() {
    var inflater: LayoutInflater
    var items: MutableList<ClipData.Item>
    var selected: MutableList<ClipData.Item>


    var receiver: OnClickAction? = null

    interface OnClickAction {
        fun onClickAction()
    }

    inner class RecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tv: TextView
        var cardViewViewMember: CardView

        init {
            tv = itemView.findViewById<View>(R.id.familyMember) as TextView
            cardViewViewMember = itemView.findViewById<View>(R.id.cardViewViewMember) as CardView
        }
    }

    init {
        inflater = LayoutInflater.from(context)
        selected = ArrayList()
        items = ArrayList()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerViewHolder {
        val v =
            inflater.inflate(R.layout.single_row_diagnostic_list, parent, false)
        return RecyclerViewHolder(v)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
         val item = items[position]
        holder.tv.text = item.text
        holder.tv.tag = holder

        holder.itemView.setOnClickListener {
            testList.selctedTestList(item.text as String,)

            if (selected.contains(item)) {
                selected.remove(item)
                unhighlightView(holder)
            } else {
                selected.add(item)

                highlightView(holder)
            }
            receiver!!.onClickAction()
        }
        if((selected.contains(item)) )
        {
              highlightView(holder)
        }
        else{
            unhighlightView(holder)
        }

    }
    companion object{
        var selectedTestList=ArrayList<String>()
    }

    private fun highlightView(holder: RecyclerViewHolder) {

        holder.cardViewViewMember.setCardBackgroundColor(Color.parseColor("#FF4CAF50"))

        //  holder.itemView.setBackgroundColor(ContextCompat.getColor(context, android.support.wearable.R.color.green));
    }

    private fun unhighlightView(holder: RecyclerViewHolder) {
        // holder.itemView.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent));
        holder.cardViewViewMember.setCardBackgroundColor(Color.parseColor("#FFFFFFFF"))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun addAll(items: MutableList<ClipData.Item>) {
        clearAll(false)
        this.items = items

        notifyDataSetChanged()
    }

    private fun clearAll(isNotify: Boolean) {
        items.clear()
        selected.clear()
        if (isNotify) notifyDataSetChanged()
    }

    fun clearSelected() {
        selected.clear()
        notifyDataSetChanged()
    }
    fun selectAll() {
        selected.clear()
        selected.addAll(items)
        notifyDataSetChanged()
    }

//    fun getSelected(): List<ClipData.Item> {
//        return selected
//    }

    fun setActionModeReceiver(receiver: OnClickAction?) {
        this.receiver = receiver
    }

    interface TestList {
        fun selctedTestList(id: String)
    }
}


