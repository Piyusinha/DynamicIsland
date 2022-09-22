package com.anaa.dynamicisland.utils.liveData

import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.recyclerview.widget.LinearLayoutManager

    @MainThread
fun <T> LiveData<T>.toFreshLiveData(): LiveData<T> {
    val freshLiveData = FreshLiveData<T>()
    val output = MediatorLiveData<T>()
    // push any onChange from the LiveData to the FreshLiveData
    output.addSource(this) { liveDataValue -> freshLiveData.value = liveDataValue }
    // then push any onChange from the FreshLiveData out
    output.addSource(freshLiveData) { freshLiveDataValue -> output.value = freshLiveDataValue }
    return output
}

fun LinearLayoutManager.hitBottomOfList(): Boolean {
    val visibleItemCount: Int = childCount
    val totalItemCount: Int = itemCount
    val pastVisibleItems: Int = findFirstVisibleItemPosition()
    return pastVisibleItems + visibleItemCount >= totalItemCount - 5
}
