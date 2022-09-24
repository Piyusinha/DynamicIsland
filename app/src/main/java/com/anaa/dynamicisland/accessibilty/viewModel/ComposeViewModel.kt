package com.anaa.dynamicisland.accessibilty.viewModel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.anaa.dynamicisland.ui.compose.IslandState

class ComposeViewModel: ViewModel() {
    private val _viewState: MutableState<IslandState> = mutableStateOf(IslandState.DEFAULT)
    val viewState: State<IslandState> = _viewState


    fun changeIsland(islandState: IslandState) {
        _viewState.value = islandState
    }

}