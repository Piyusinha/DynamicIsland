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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.anaa.dynamicisland.R
import com.anaa.dynamicisland.ui.compose.utils.NotchIslandStateSealedClass

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
            .padding(0.dp, 0.dp, 0.dp, 0.dp)
    ) {
        val (text, ringerImage) = createRefs()
        Image(painter = painterResource(isLandState.drawable ?: R.drawable.ic_leftsilent_icon),
            contentDescription = null,
            modifier = Modifier.constrainAs(ringerImage) {
                when (notchType) {
                    0 -> start.linkTo(parent.start,margin = (size.height + 10).dp)
                    else -> start.linkTo(parent.start, margin = 16.dp)
                }
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
            })

        Text(
            isLandState.text,
            color = colorResource(id = getFontColor(isLandState.text)),
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.constrainAs(text) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                when (notchType) {
                    0, 2 -> end.linkTo(parent.end,margin = 16.dp)
                    1 -> end.linkTo(parent.end, margin =(size.height + 10).dp)
                }

            })
    }
}
fun getFontColor(text: String): Int {
    when (text) {
        "Ring" -> return R.color.green_dark
        "Silent" -> return R.color.red
    }
    return R.color.black
}