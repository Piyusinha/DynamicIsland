package com.anaa.dynamicisland.ui.compose.utils

import android.bluetooth.BluetoothDevice
import android.graphics.Bitmap
import android.graphics.drawable.Drawable

sealed class NotchIslandStateSealedClass {
    object DefaultNotch : NotchIslandStateSealedClass()
    class ChargingNotch(val percentage:Int,val drawable: Int?) : NotchIslandStateSealedClass()
    class RingerNotch(val text:String,val drawable: Int?) : NotchIslandStateSealedClass()
    class BluetoothConnected(val device: BluetoothDevice?) : NotchIslandStateSealedClass()
    class BluetoothExpanderConnected(val device: BluetoothDevice?) : NotchIslandStateSealedClass()
    class MusicSmallView(val image: Bitmap,val title:String,val description:String) : NotchIslandStateSealedClass()
    class MusicExpanderView(val image: Bitmap,val title:String,val description:String) : NotchIslandStateSealedClass()
}