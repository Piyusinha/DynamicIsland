package com.anaa.dynamicisland

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class MainActivityViewModel @Inject constructor():ViewModel() {
    val changeFragment = MutableLiveData<DaggerFragment>()
    var count = 1

    fun changeFragment(fragment: DaggerFragment) {
        count++
        changeFragment.value = fragment
    }
}