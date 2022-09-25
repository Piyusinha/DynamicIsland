package com.anaa.dynamicisland.ui.compose

import android.content.Context
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.content.ContextCompat
import com.anaa.dynamicisland.R
import com.anaa.dynamicisland.ui.compose.utils.NotchIslandStateSealedClass

/**
 * @Author : wyl
 * @Date : 2022/9/14
 * Desc :
 */
@Composable
fun defaultIsland(size: Size = Size(80.dp.value, 22.dp.value), roundedCorner: Int) {
    ConstraintLayout(
        modifier = Modifier
            .height(size.height.dp)
            .width(size.width.dp)
            .background(Color.Black, shape = RoundedCornerShape(roundedCorner.dp))
    ) {
        val island = createRef()
        Image(painter = painterResource(R.drawable.island),
            contentDescription = null,
            modifier = Modifier.constrainAs(island) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
            })
    }
}

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

        val (charginText,chargingValue, chargingImage) = createRefs()

        Text("Charging", color = Color.White, modifier = Modifier.constrainAs(charginText){
            top.linkTo(parent.top)
            bottom.linkTo(parent.bottom)
            when(notchType) {
                0 -> start.linkTo(parent.start,(size.height + 10).dp)
                else -> start.linkTo(parent.start,16.dp)
            }

        })

        Image(painter = painterResource(isLandState.drawable ?: R.drawable.ic_chargepercentage_100),
            contentDescription = null,
            contentScale = ContentScale.None,
            modifier = Modifier.constrainAs(chargingImage) {
                when(notchType) {
                    0,2 -> end.linkTo(parent.end,16.dp)
                    1 -> end.linkTo(parent.end,(size.height + 10).dp)
                }

                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
            })


        Text(isLandState.percentage.toString()+"%", color = Color.Green, fontSize = 12.sp,fontWeight = FontWeight.Bold, modifier = Modifier.constrainAs(chargingValue){
            top.linkTo(parent.top)
            end.linkTo(chargingImage.start,8.dp)
            bottom.linkTo(parent.bottom)
        })
    }
}

@Composable
fun RingerIsland(
    isLandState: NotchIslandStateSealedClass.RingerNotch,
    size: Size,
    radius: Int = 10,
    notchType: Int
) {
    ConstraintLayout(
        modifier = Modifier
            .height(size.height.dp)
            .width(size.width.dp)
            .background(Color.Black, shape = RoundedCornerShape(radius.dp))
            .padding(0.dp, 0.dp,0.dp, 0.dp)
    ) {
        val (text,ringerImage) = createRefs()
        Image(painter = painterResource(isLandState.drawable ?: R.drawable.ic_leftsilent_icon),
            contentDescription = null,
            contentScale = ContentScale.None,
            modifier = Modifier.constrainAs(ringerImage) {
                when(notchType) {
                    0 -> start.linkTo(parent.start,(size.height + 10).dp)
                    else -> start.linkTo(parent.start,16.dp)
                }
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
            })

        Text(isLandState.text, color = colorResource(id = getFontColor(isLandState.text)), fontSize = 14.sp,fontWeight = FontWeight.Bold, modifier = Modifier.constrainAs(text){
            top.linkTo(parent.top)
            bottom.linkTo(parent.bottom)
            when(notchType) {
                0,2 -> end.linkTo(parent.end,16.dp)
                1 -> end.linkTo(parent.end,(size.height + 10).dp)
            }

        })
    }
}

fun getFontColor(text: String): Int {
    when(text) {
        "Ring" -> return R.color.green_dark
        "Silent" -> return R.color.red
    }
    return R.color.black
}



