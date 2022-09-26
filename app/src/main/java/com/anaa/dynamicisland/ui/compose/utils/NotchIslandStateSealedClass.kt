package com.anaa.dynamicisland.ui.compose.utils

import android.bluetooth.BluetoothDevice
import android.graphics.drawable.Drawable

sealed class NotchIslandStateSealedClass {
    object DefaultNotch : NotchIslandStateSealedClass()
    class ChargingNotch(val percentage:Int,val drawable: Int?) : NotchIslandStateSealedClass()
    class RingerNotch(val text:String,val drawable: Int?) : NotchIslandStateSealedClass()
    class BluetoothConnected(val device: BluetoothDevice?) : NotchIslandStateSealedClass()
    class BluetoothExpanderConnected(val device: BluetoothDevice?) : NotchIslandStateSealedClass()
}