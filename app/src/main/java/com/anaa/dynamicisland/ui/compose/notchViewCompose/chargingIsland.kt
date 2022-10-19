package com.anaa.dynamicisland.ui.compose.notchViewCompose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.anaa.dynamicisland.R
import com.anaa.dynamicisland.ui.compose.utils.NotchIslandStateSealedClass

@Composable
fun chargingIsland(
    isLandState: NotchIslandStateSealedClass.ChargingNotch,
    size: Size = Size(236.dp.value, 30.dp.value),
    radius: Int = 10,
    notchType: Int
) {
    ConstraintLayout(
        modifier = Modifier
            .height(size.height.dp)
            .width(size.width.dp)
            .background(Color.Black, shape = RoundedCornerShape(radius.dp))
            .padding(0.dp, 0.dp, 0.dp, 0.dp)
    ) {

        val (charginText, chargingValue, chargingImage) = createRefs()

        Text("Charging", color = Color.White, modifier = Modifier.constrainAs(charginText) {
            top.linkTo(parent.top)
            bottom.linkTo(parent.bottom)
            when (notchType) {
                0 -> start.linkTo(parent.start,margin = (size.height + 10).dp)
                else -> start.linkTo(parent.start,margin = 16.dp)
            }

        })

        Image(painter = painterResource(isLandState.drawable ?: R.drawable.ic_chargepercentage_100),
            contentDescription = null,
            contentScale = ContentScale.None,
            modifier = Modifier.constrainAs(chargingImage) {
                when (notchType) {
                    0, 2 -> end.linkTo(parent.end, margin = 16.dp)
                    1 -> end.linkTo(parent.end,margin = (size.height + 10).dp)
                }

                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
            })


        Text(
            isLandState.percentage.toString() + "%",
            color = Color.Green,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.constrainAs(chargingValue) {
                top.linkTo(parent.top)
                end.linkTo(chargingImage.start, margin = 8.dp)
                bottom.linkTo(parent.bottom)
            })
    }
}