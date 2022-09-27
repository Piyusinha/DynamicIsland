package com.anaa.dynamicisland.ui.compose.notchViewCompose

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.anaa.dynamicisland.R
import com.anaa.dynamicisland.accessibilty.viewModel.ComposeViewModel
import com.anaa.dynamicisland.ui.compose.custom.AnimationDirection
import com.anaa.dynamicisland.ui.compose.custom.DeterminateProgressView
import com.anaa.dynamicisland.ui.compose.utils.NotchIslandStateSealedClass
import java.util.Random

@SuppressLint("MissingPermission")
@Composable
fun BluetoothDeviceExpandedView(
    isLandState: NotchIslandStateSealedClass.BluetoothExpanderConnected,
    size: Size,
    radius: Int = 10,
    notchType: Int,
    composeViewModel: ComposeViewModel = viewModel()
) {
    ConstraintLayout(
        modifier = Modifier
            .height(size.height.dp)
            .width(size.width.dp)
            .background(Color.Black, shape = RoundedCornerShape(radius.dp))
            .padding(0.dp, 0.dp, 0.dp, 0.dp)
    ) {
        val (progressbar, airpods,header,name) = createRefs()

        val imageModifiers = remember {
            Modifier
                .height(38.dp)
                .width(38.dp)
                .constrainAs(airpods) {
                    start.linkTo(parent.start, 32.dp)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }
        }
        AsyncImage(model = R.drawable.airpodsl,
            contentDescription = null,
            modifier = imageModifiers)
        Text(
            "Connected",
            color = Color(0xff898989),
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.constrainAs(header) {
                top.linkTo(airpods.top)
                start.linkTo(airpods.end,16.dp)
            })
        Text(
            isLandState.device?.name ?: "Earphone",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.constrainAs(name) {
                top.linkTo(header.bottom)
                start.linkTo(airpods.end,16.dp)
            })
        DeterminateProgressView(
            strokeWidth = 4.dp,
            strokeBackgroundWidth = 4.dp,
            progress = composeViewModel.randomValue,
            progressDirection = AnimationDirection.RIGHT,
            roundedBorder = true,
            durationInMilliSecond = 100,
            startDelay = 10,
            radius = 20.dp,
            waveAnimation = true,
            textSize = 12,
            showText = true,
            modifier = Modifier.constrainAs(progressbar) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                when (notchType) {
                    0, 2 -> end.linkTo(parent.end, 16.dp)
                    1 -> end.linkTo(parent.end, (size.height + 10).dp)
                }
            }
        )

    }
}

fun getRandomValue(): Float {
    return (58..96).random().toFloat()
}

@SuppressLint("MissingPermission")
@Composable
fun BluetoothDeviceView(
    isLandState: NotchIslandStateSealedClass.BluetoothConnected,
    size: Size,
    radius: Int = 10,
    notchType: Int,
    composeViewModel: ComposeViewModel = viewModel()
) {
    val interactionSource = remember { MutableInteractionSource() }
    composeViewModel.randomValue = getRandomValue()
    ConstraintLayout(
        modifier = Modifier
            .height(size.height.dp)
            .width(size.width.dp)
            .background(Color.Black, shape = RoundedCornerShape(radius.dp))
            .padding(0.dp, 0.dp, 0.dp, 0.dp)
            .clickable(
                onClick = {
                    composeViewModel.changeView(
                        NotchIslandStateSealedClass.BluetoothExpanderConnected(
                            isLandState.device
                        )
                    )
                },
                interactionSource = interactionSource,
                indication = null
            )
    ) {
        val (progressbar, airpods) = createRefs()
        val imageModifier = remember {
            Modifier.constrainAs(airpods) {
                when (notchType) {
                    0 -> start.linkTo(parent.start, (size.height + 10).dp)
                    else -> start.linkTo(parent.start, 16.dp)
                }
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
            }
        }
        AsyncImage(model = R.drawable.airpods,
            contentDescription = null,
            modifier = imageModifier)
        DeterminateProgressView(
            strokeWidth = 2.dp,
            strokeBackgroundWidth = 2.dp,
            progress = composeViewModel.randomValue,
            progressDirection = AnimationDirection.RIGHT,
            roundedBorder = true,
            durationInMilliSecond = 100,
            startDelay = 10,
            radius = 10.dp,
            waveAnimation = true,
            textSize = 12,
            modifier = Modifier.constrainAs(progressbar) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                when (notchType) {
                    0, 2 -> end.linkTo(parent.end, 16.dp)
                    1 -> end.linkTo(parent.end, (size.height + 10).dp)
                }
            }
        )

    }
}
