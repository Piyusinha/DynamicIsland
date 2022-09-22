package com.example.dynamicisland.fragments

import android.view.Gravity
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

    fun getSavedNotch(): Int? {
       return repository.getNotch()
    }

    fun setXandY(x:Int,y:Int) {
        repository.setXandY(x,y)
    }

    fun getX(): Int {
        return repository.getX()
    }

    fun getY(): Int {
        return repository.getY()
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

    fun getGravity(): Int {
        when(getSavedNotch()) {
            0 -> return Gravity.TOP or Gravity.LEFT
            1 -> return Gravity.TOP or Gravity.RIGHT
            2,3 -> return Gravity.TOP or Gravity.CENTER
        }
        return -1
    }

    fun setDimension(dimension: Float) {
        repository.setDimension(dimension)
    }

    fun setRadius(radius: Float) {
        repository.setRadius(radius)
    }
    fun getRadius(): Float {
        return repository.getRadius()
    }

    fun getDimension(): Float {
        return repository.getDimension()
    }

}