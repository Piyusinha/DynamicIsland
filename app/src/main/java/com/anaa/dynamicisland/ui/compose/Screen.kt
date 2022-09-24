package com.anaa.dynamicisland.ui.compose

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout

/**
 * @Author : wyl
 * @Date : 2022/9/14
 * Desc :
 */

enum class IslandState {
    PHONE, AIRPODS, FACE, DEFAULT;
}

@Preview(showBackground = true)
@ExperimentalAnimationApi
@Composable
fun mainScreen() {

    var isLandState by remember { mutableStateOf(IslandState.DEFAULT) }
    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (btn, btnHeadSet, btnFace, btnDefault, box) = createRefs()


        DynamicIslandComposibleView(isLandState)

        Button(onClick = {
            isLandState = IslandState.DEFAULT
        }, modifier = Modifier.constrainAs(btnDefault) {
            start.linkTo(parent.start)
            end.linkTo(btn.start)
            bottom.linkTo(parent.bottom, margin = 10.dp)
        }) {
            Text("default")
        }



        Button(onClick = {
            isLandState = IslandState.PHONE
        }, modifier = Modifier.constrainAs(btn) {
            start.linkTo(btnDefault.end)
            end.linkTo(btnHeadSet.start)
            bottom.linkTo(parent.bottom, margin = 10.dp)
        }) {
            Text("incoming call")
        }

        Button(onClick = {
            isLandState = IslandState.AIRPODS
        }, modifier = Modifier.constrainAs(btnHeadSet) {
            start.linkTo(btn.end)
            end.linkTo(btnFace.start)
            bottom.linkTo(parent.bottom, margin = 10.dp)
        }) {
            Text("earphone")
        }

        Button(onClick = {
            isLandState = IslandState.FACE
        }, modifier = Modifier.constrainAs(btnFace) {
            start.linkTo(btnHeadSet.end)
            end.linkTo(parent.end)
            bottom.linkTo(parent.bottom, margin = 10.dp)
        }) {
            Text("Face")
        }
    }
}
