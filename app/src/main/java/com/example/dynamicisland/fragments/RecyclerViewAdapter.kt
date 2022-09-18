package com.example.dynamicisland.fragments

import android.os.Parcelable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.dynamicisland.R
import com.example.dynamicisland.fragments.interfaces.NotchSelectedInterface
import com.example.dynamicisland.models.NotchTypeData


class RecyclerViewAdapter(private var notchSelectedInterface: NotchSelectedInterface) :
    ListAdapter<Any,RecyclerView.ViewHolder>(RecyclerViewDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var viewHolder: RecyclerView.ViewHolder? = null
        val inflater = LayoutInflater.from(parent.context)
        when (viewType) {
            R.layout.notch_rv_item -> viewHolder = NotchViewHolder.create(inflater, parent,notchSelectedInterface)
        }
        return viewHolder!!
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is NotchViewHolder -> holder.bind(getItem(position) as NotchTypeData)

        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position) ) {
            is NotchTypeData -> R.layout.notch_rv_item
            else -> -1
        }
    }

}
