package com.example.ehcf_doctor.HealthCube.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ClipData
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ehcf_doctor.HealthCube.Adapter.ItemAdapter
import com.example.ehcf_doctor.R

//class MainActivity : AppCompatActivity(), ItemAdapter.OnClickAction {
//    var activity: Activity = this@MainActivity
//    var rv: RecyclerView? = null
//    var adapter: ItemAdapter? = null
//    var actionMode: ActionMode? = null
//    private val actionModeCallback: ActionMode.Callback = object : ActionMode.Callback {
//        override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
//            val inflater = mode.menuInflater
//            (inflater as MenuInflater).inflate(R.menu.cab_menu, menu)
//            return true
//        }
//
//        override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean {
//            return false
//        }
//
//        override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
//            return when (item.itemId) {
//                R.id.menu_email -> {
//                    Toast.makeText(activity,
//                        adapter!!.selected.size.toString() + " selected",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                    mode.finish()
//                    true
//                }
//                else -> false
//            }
//        }
//
//        override fun onDestroyActionMode(mode: ActionMode) {
//            actionMode = null
//        }
//    }
//
//    @SuppressLint("MissingInflatedId")
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.mainactivity_multiselected)
//        val lm = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
//        adapter = ItemAdapter(this)
//        rv = findViewById(R.id.rcView)
//        rv!!.adapter = adapter
//        rv!!.setHasFixedSize(true)
//        rv!!.setLayoutManager(lm)
//        val dividerItemDecoration = DividerItemDecoration(rv!!.getContext(), lm.orientation)
//        rv!!.addItemDecoration(dividerItemDecoration)
//        val items: MutableList<ClipData.Item> = ArrayList()
//        items.add(ClipData.Item("Item 1"))
//        items.add(ClipData.Item("Item 2"))
//        items.add(ClipData.Item("Item 3"))
//        items.add(ClipData.Item("Item 4"))
//        items.add(ClipData.Item("Item 5"))
//        items.add(ClipData.Item("Item 6"))
//        items.add(ClipData.Item("Item 7"))
//        items.add(ClipData.Item("Item 8"))
//        items.add(ClipData.Item("Item 9"))
//        adapter!!.addAll(items)
//        adapter!!.setActionModeReceiver(activity as ItemAdapter.OnClickAction)
//    }
//
//    fun selectAll(v: View?) {
//        adapter!!.selectAll()
//        if (actionMode == null) {
//            actionMode = startActionMode(actionModeCallback)
//            actionMode!!.title = "Selected: " + adapter!!.selected.size
//        }
//    }
//
//    fun deselectAll(v: View?) {
//        adapter!!.clearSelected()
//        if (actionMode != null) {
//            actionMode!!.finish()
//            actionMode = null
//        }
//    }
//
//    override fun onClickAction() {
//        val selected = adapter!!.selected.size
//        if (actionMode == null) {
//            actionMode = startActionMode(actionModeCallback)
//            actionMode!!.title = "Selected: $selected"
//        } else {
//            if (selected == 0) {
//                actionMode!!.finish()
//            } else {
//                actionMode!!.title = "Selected: $selected"
//            }
//        }
//    }
//}