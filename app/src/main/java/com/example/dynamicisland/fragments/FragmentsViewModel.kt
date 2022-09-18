package com.example.dynamicisland.fragments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dynamicisland.FragmentRepository
import com.example.dynamicisland.NOTCH
import com.example.dynamicisland.models.NotchTypeData
import javax.inject.Inject

class FragmentsViewModel @Inject constructor(private val repository: FragmentRepository) : ViewModel() {

    private val _listMLD = MutableLiveData<MutableList<Any>?>()
    val listMLD:LiveData<MutableList<Any>?> = _listMLD

    fun setupNotch(notch: NOTCH) {
        repository.setNotchType(notch)
    }

    fun getNotchList() {
        _listMLD.value = repository.getNotchList()
    }

    fun onNotchClicked(selected: Boolean, adapterPosition: Int) {
        val list = repository.getNotchList()
        list?.forEachIndexed { index, item ->
            if(item is NotchTypeData ) {
                if(adapterPosition == index) item.isSelected = selected
                else item.isSelected = false
            }
        }
        _listMLD.value = list
    }

    fun getSelectedNotch(): NOTCH? {
        return ((listMLD.value?.firstOrNull() {
            it is NotchTypeData && it.isSelected
        }) as? NotchTypeData)?.notch
    }

}