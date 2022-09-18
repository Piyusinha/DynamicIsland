package com.example.dynamicisland.fragments

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.dynamicisland.R
import com.example.dynamicisland.databinding.NotchRvItemBinding
import com.example.dynamicisland.fragments.interfaces.NotchSelectedInterface
import com.example.dynamicisland.models.NotchTypeData

class NotchViewHolder(
    private val binding: NotchRvItemBinding,
    private val notchSelectedInterface: NotchSelectedInterface
) : RecyclerView.ViewHolder(binding.root) {
    companion object {
        fun create(
            inflater: LayoutInflater,
            viewGroup: ViewGroup,
            notchSelectedInterface: NotchSelectedInterface,
        ): NotchViewHolder {
            val binding = DataBindingUtil.inflate<NotchRvItemBinding>(
                inflater, R.layout.notch_rv_item, viewGroup, false
            )
            return NotchViewHolder(binding,notchSelectedInterface)
        }
    }


    fun bind(notchTypeData: NotchTypeData) {
        binding.title.text = notchTypeData.notch.notch_name
        binding.notchImage.setImageDrawable(notchTypeData.drawable)
        binding.checkBox.isChecked = notchTypeData.isSelected



        binding.checkBox.setOnCheckedChangeListener { _, b ->
            if(b) notchSelectedInterface.onClicked(b,adapterPosition)
        }

    }


}
