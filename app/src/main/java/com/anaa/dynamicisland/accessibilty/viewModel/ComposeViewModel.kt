package com.anaa.dynamicisland.accessibilty.viewModel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import com.anaa.dynamicisland.ui.compose.IslandState
import com.anaa.dynamicisland.ui.compose.utils.NotchIslandStateSealedClass

class ComposeViewModel: ViewModel() {
    private val _viewState: MutableState<NotchIslandStateSealedClass> = mutableStateOf(NotchIslandStateSealedClass.DefaultNotch)
    val viewState: State<NotchIslandStateSealedClass> = _viewState


    fun changeIsland(islandState: NotchIslandStateSealedClass) {
        _viewState.value = islandState
    }

    private val _radius: MutableState<Int> = mutableStateOf(8)
    val radius: State<Int> = _radius

    fun changeRadius(radius:Int) {
        _radius.value = radius
    }

    private val _size: MutableState<Size> = mutableStateOf(Size(20.dp.value,20.dp.value))
    val size: State<Size> = _size

    fun changeSize(size:Int) {
        _size.value = Size(size.dp.value,size.dp.value)
    }

}