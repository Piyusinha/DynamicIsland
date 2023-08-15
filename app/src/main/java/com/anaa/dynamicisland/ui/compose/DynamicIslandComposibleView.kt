package com.anaa.dynamicisland.ui.compose

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateSize
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.anaa.dynamicisland.ui.compose.notchViewCompose.BluetoothDeviceExpandedView
import com.anaa.dynamicisland.ui.compose.notchViewCompose.BluetoothDeviceView
import com.anaa.dynamicisland.ui.compose.notchViewCompose.MusicBigIsland
import com.anaa.dynamicisland.ui.compose.notchViewCompose.MusicIsland
import com.anaa.dynamicisland.ui.compose.notchViewCompose.RingerIsland
import com.anaa.dynamicisland.ui.compose.notchViewCompose.chargingIsland
import com.anaa.dynamicisland.ui.compose.utils.NotchIslandStateSealedClass

@Composable
@ExperimentalAnimationApi
fun DynamicIslandComposibleView(
    isLandState: NotchIslandStateSealedClass = NotchIslandStateSealedClass.DefaultNotch,
    defaultSize: Size = Size(20.dp.value, 20.dp.value),
    notch: Int = 0,
    roundedCorner: Int = 8,
    dimension: Int? = null,

    ) {

    val configuration = LocalConfiguration.current

    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp


    val transition = updateTransition(
        targetState = isLandState, label = "Island Transition"
    )

    var isLandStateInternal by remember { mutableStateOf(isLandState) }
    val sizeFromSharedPref by remember {
        mutableStateOf(defaultSize)
    }
    val radiusFromSharedPref by remember {
        mutableStateOf(roundedCorner)
    }

    val notchType by remember {
        mutableStateOf(notch)
    }

    ConstraintLayout(modifier = Modifier.wrapContentSize()) {
        val box = createRef()

        val size by transition.animateSize(label = "Size") { state ->
            when (state) {
                is NotchIslandStateSealedClass.RingerNotch -> Size(150.dp.value, sizeFromSharedPref.height)
                is NotchIslandStateSealedClass.ChargingNotch-> Size(236.dp.value, sizeFromSharedPref.height)
                is NotchIslandStateSealedClass.DefaultNotch -> defaultSize
                is NotchIslandStateSealedClass.BluetoothConnected -> Size(150.dp.value, sizeFromSharedPref.height)
                is NotchIslandStateSealedClass.BluetoothExpanderConnected -> Size(320.dp.value,80.dp.value)
                is NotchIslandStateSealedClass.MusicSmallView -> Size(150.dp.value, sizeFromSharedPref.height)
                is NotchIslandStateSealedClass.MusicExpanderView -> Size(screenWidth.value,157.dp.value)
            }
        }

        Box(modifier = Modifier
            .size(size.width.dp, size.height.dp)
            .constrainAs(box) {
                bottom.linkTo(parent.bottom)
                top.linkTo(parent.top)
                end.linkTo(parent.end)
            }) {
            when (isLandState) {
                is NotchIslandStateSealedClass.RingerNotch -> RingerIsland(isLandState,size,radiusFromSharedPref,notchType)
                is NotchIslandStateSealedClass.ChargingNotch -> chargingIsland(isLandState,size,radiusFromSharedPref,notchType)
                is NotchIslandStateSealedClass.DefaultNotch -> DefaultIsland(size,roundedCorner)
                is NotchIslandStateSealedClass.BluetoothConnected -> BluetoothDeviceView(
                    isLandState = isLandState,
                    size = size,
                    notchType = notchType,
                    radius = radiusFromSharedPref
                )
                is NotchIslandStateSealedClass.BluetoothExpanderConnected -> BluetoothDeviceExpandedView(
                    isLandState = isLandState,
                    size = size,
                    notchType = notchType,
                    radius = radiusFromSharedPref
                )
                is NotchIslandStateSealedClass.MusicSmallView -> {
                    MusicIsland(isLandState = isLandState, size = size, notchType = notchType, radius = radiusFromSharedPref)
                }
//                is NotchIslandStateSealedClass.MusicExpanderView -> {
//                    MusicBigIsland(isLandState = isLandState, size = size , notchType = notchType, radius = radiusFromSharedPref)
//                }
            }
        }
    }

}