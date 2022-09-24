package com.anaa.dynamicisland.ui.compose

import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.animateSize
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.constraintlayout.compose.ConstrainedLayoutReference
import androidx.constraintlayout.compose.ConstraintLayout
import com.anaa.dynamicisland.MainActivityViewModel

@Preview
@Composable
fun DynamicIslandComposibleView(
    isLandState: IslandState = IslandState.DEFAULT,
) {
    val transition = updateTransition(
        targetState = isLandState, label = "Island Transition"
    )

    var isLandStateInternal by remember { mutableStateOf(isLandState) }

    ConstraintLayout(modifier = Modifier.wrapContentSize()) {
        val box = createRef()

        val size by transition.animateSize(label = "Size") { state ->
            when (state) {
                IslandState.PHONE -> Size(400.dp.value, 70.dp.value)
                IslandState.AIRPODS -> Size(230.dp.value, 35.dp.value)
                IslandState.FACE -> Size(180.dp.value, 180.dp.value)
                IslandState.DEFAULT -> Size(80.dp.value, 22.dp.value)
            }
        }

        Box(modifier = Modifier.size(size.width.dp, size.height.dp).constrainAs(box) {
            start.linkTo(parent.start)
            end.linkTo(parent.end)
            top.linkTo(parent.top, margin = 10.dp)
        }) {
            when (isLandState) {
                IslandState.PHONE -> phoneIsLand(size)
                IslandState.AIRPODS -> headsetIsland(size)
                IslandState.FACE -> faceIsland(size)
                IslandState.DEFAULT -> defaultIsland(size)
            }
        }
    }

}