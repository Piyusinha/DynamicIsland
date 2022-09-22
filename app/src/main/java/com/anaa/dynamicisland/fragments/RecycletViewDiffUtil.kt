package com.anaa.dynamicisland.fragments

import androidx.recyclerview.widget.DiffUtil
import com.anaa.dynamicisland.models.NotchTypeData
class RecyclerViewDiffUtil : DiffUtil.ItemCallback<Any>() {
    override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
        return if (oldItem is NotchTypeData && newItem is NotchTypeData) {
            oldItem.notch.value == newItem.notch.value
        } else {
            true
        }
    }

    override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
        return if (oldItem is NotchTypeData && newItem is NotchTypeData) {
            oldItem.isSelected == newItem.isSelected
        } else {
            true
        }
    }
}