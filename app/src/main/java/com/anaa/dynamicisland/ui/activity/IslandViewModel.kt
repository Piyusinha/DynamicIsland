package com.anaa.dynamicisland.ui.activity

import android.view.Gravity
import androidx.lifecycle.ViewModel
import com.anaa.dynamicisland.FragmentRepository
import javax.inject.Inject

class IslandViewModel @Inject constructor(private val repository: FragmentRepository) : ViewModel() {
    fun getSavedNotch(): Int? {
        return repository.getNotch()
    }
    fun getX(): Int {
        return repository.getX()
    }

    fun getY(): Int {
        return repository.getY()
    }

    fun getRadius(): Float {
        return repository.getRadius()
    }

    fun getDimension(): Float {
        return repository.getDimension()
    }

    fun getGravity(): Int {
        when(getSavedNotch()) {
            0 -> return Gravity.TOP or Gravity.LEFT
            1 -> return Gravity.TOP or Gravity.RIGHT
            2,3 -> return Gravity.TOP or Gravity.CENTER
        }
        return -1
    }
    fun isSetupDone(): Boolean {
        return repository.isSetupComplete()
    }
}