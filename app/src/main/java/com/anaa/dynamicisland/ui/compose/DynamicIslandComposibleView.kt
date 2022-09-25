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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.anaa.dynamicisland.ui.compose.utils.NotchIslandStateSealedClass

@Preview(showSystemUi = true, showBackground = true)
@Composable
@ExperimentalAnimationApi
fun DynamicIslandComposibleView(
    isLandState: NotchIslandStateSealedClass = NotchIslandStateSealedClass.DefaultNotch,
    defaultSize: Size = Size(20.dp.value,20.dp.value),
    notchType: Int = 0,
    roundedCorner: Int = 8,

) {
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

    ConstraintLayout(modifier = Modifier.wrapContentSize()) {
        val box = createRef()

        val size by transition.animateSize(label = "Size") { state ->
            when (state) {
                is NotchIslandStateSealedClass.RingerNotch -> Size(150.dp.value, sizeFromSharedPref.height)
                is NotchIslandStateSealedClass.ChargingNotch-> Size(236.dp.value, sizeFromSharedPref.height)
                is  NotchIslandStateSealedClass.DefaultNotch -> defaultSize
            }
        }

        Box(modifier = Modifier
            .size(size.width.dp, size.height.dp)
            .constrainAs(box) {
                start.linkTo(parent.start)
                top.linkTo(parent.top)
                end.linkTo(parent.end)
            }) {
            when (isLandState) {
                is NotchIslandStateSealedClass.RingerNotch -> RingerIsland(isLandState,size,radiusFromSharedPref)
                is NotchIslandStateSealedClass.ChargingNotch -> chargingIsland(isLandState,size,radiusFromSharedPref)
                is NotchIslandStateSealedClass.DefaultNotch -> defaultIsland(size,roundedCorner)
            }
        }
    }

}