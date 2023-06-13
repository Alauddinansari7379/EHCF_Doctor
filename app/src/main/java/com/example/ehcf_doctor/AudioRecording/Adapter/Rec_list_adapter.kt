package com.example.ehcf_doctor.AudioRecording.Adapter

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.ehcf_doctor.AudioRecording.Adapter.Rec_list_adapter.Rec_ViewHolder
import com.example.ehcf_doctor.AudioRecording.Fragment.RecordListFragment
import com.example.ehcf_doctor.AudioRecording.TimeAgo
import com.example.ehcf_doctor.R
import java.io.File
import kotlin.coroutines.coroutineContext

class Rec_list_adapter(
    private val allFiles: Array<File>,
    private val onitemList_click: onItemList_click,
    val reresh: RecordListFragment
) : RecyclerView.Adapter<Rec_ViewHolder>() {

    private var timeAgo: TimeAgo? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Rec_ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.record_list_item, parent, false)
        timeAgo = TimeAgo()
        return Rec_ViewHolder(view)
    }

    override fun onBindViewHolder(holder: Rec_ViewHolder, position: Int) {

        holder.list_title.text = allFiles[position].name
        holder.list_date.text = timeAgo!!.getTimeAgo(allFiles[position].lastModified())
//        if (allFiles[position].name.isEmpty()){
//            Toast.makeText(getApplicationContext(), "No Audio Found", Toast.LENGTH_SHORT).show()
//        }
        holder.imgDelete.setOnClickListener {
            allFiles[position].delete()
            notifyItemChanged(position)
            notifyItemRangeRemoved(position, 1)
            notifyItemRemoved(position)
            reresh.refresh()
        }
    }

    override fun getItemCount(): Int {
        return allFiles.size
    }

    inner class Rec_ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        private val list_image_view: ImageView
        val list_title: TextView
        val list_date: TextView
        val imgDelete: ImageView

        init {
            list_image_view = itemView.findViewById(R.id.list_image_view)
            list_title = itemView.findViewById(R.id.list_title)
            list_date = itemView.findViewById(R.id.list_date)
            imgDelete = itemView.findViewById(R.id.imgDeleteAudio)
            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            onitemList_click.onClick_Listener(allFiles[adapterPosition], adapterPosition)

            Log.e("Auidro list", allFiles.toString())
        }
    }

    interface onItemList_click {
        fun onCreateView(
            inflater: LayoutInflater?, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View?

        fun onViewCreated(view: View, savedInstanceState: Bundle?)
        fun onClick_Listener(file: File?, position: Int)
        fun refresh()
    }
}