package com.anaa.dynamicisland.accessibilty.viewModel

import android.graphics.Bitmap
import android.media.MediaMetadata
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anaa.dynamicisland.ui.compose.utils.NotchIslandStateSealedClass
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ComposeViewModel: ViewModel() {
    var randomValue: Float = 90f
    private val _viewState: MutableState<NotchIslandStateSealedClass> = mutableStateOf(NotchIslandStateSealedClass.DefaultNotch)
    val viewState: State<NotchIslandStateSealedClass> = _viewState

    var defaultNotch: NotchIslandStateSealedClass = NotchIslandStateSealedClass.DefaultNotch

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

    fun changeView(notchView: NotchIslandStateSealedClass) {
        viewModelScope.coroutineContext.cancelChildren()
        changeIsland(notchView)
        gotoDefaultState()

    }

    private fun gotoDefaultState() {
        viewModelScope.launch {
            delay(3000)
            changeIsland(defaultNotch)
        }
    }

    fun getMediaBitmapData(metadata: MediaMetadata): Bitmap {
        return metadata.getBitmap(MediaMetadata.METADATA_KEY_ART) ?: metadata.getBitmap(MediaMetadata.METADATA_KEY_ALBUM_ART) ?: metadata.getBitmap(MediaMetadata.METADATA_KEY_ALBUM_ART)

    }

}