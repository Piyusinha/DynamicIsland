package com.anaa.dynamicisland.ui.compose.notchViewCompose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.anaa.dynamicisland.R
import com.anaa.dynamicisland.ui.compose.utils.NotchIslandStateSealedClass

@Composable
fun MusicIsland(
    isLandState: NotchIslandStateSealedClass.MusicSmallView,
    size: Size,
    radius: Int = 10,
    notchType: Int
) {

    var isPlaying by remember {
        mutableStateOf(true)
    }
    var speed by remember {
        mutableStateOf(1f)
    }

    val composition by rememberLottieComposition(
        LottieCompositionSpec
            .RawRes(R.raw.music)
    )
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever,
        isPlaying = isPlaying,
        speed = speed,
        restartOnPlay = false

    )

    ConstraintLayout(
        modifier = Modifier
            .height(size.height.dp)
            .width(size.width.dp)
            .background(Color.Black, shape = RoundedCornerShape(radius.dp))
            .padding(0.dp, 0.dp, 0.dp, 0.dp)
    ) {
        val (animation, ringerImage) = createRefs()
        Image(bitmap = isLandState.image.asImageBitmap(),
            contentDescription = null,
            modifier = Modifier.padding(4.dp)
                .clip(RoundedCornerShape(16.dp)).constrainAs(ringerImage) {
                when (notchType) {
                    0 -> start.linkTo(parent.start,margin = (size.height + 10).dp)
                    else -> start.linkTo(parent.start, margin = 8.dp)
                }
                top.linkTo(parent.top, margin = 10.dp)
                bottom.linkTo(parent.bottom, margin = 10.dp)
            })


        LottieAnimation(
            composition,
            progress,
            modifier = Modifier.size(size.height.dp).constrainAs(animation) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                when (notchType) {
                    0, 2 -> end.linkTo(parent.end,margin = 16.dp)
                    1 -> end.linkTo(parent.end, margin =(size.height + 10).dp)
                }

            }
        )

    }
}
