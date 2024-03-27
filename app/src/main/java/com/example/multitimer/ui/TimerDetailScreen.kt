package com.example.multitimer.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun TimerDetail(
    onBackClicked: ()->Unit,
    onCreateClicked: (Float)->Unit,
){
    var tensMinutesPlace = rememberSaveable { mutableIntStateOf(0) }
    var onesMinutesPlace = rememberSaveable { mutableIntStateOf(0) }
    var tensSecondsPlace = rememberSaveable { mutableIntStateOf(0) }
    var onesSecondsPlace = rememberSaveable { mutableIntStateOf(0) }

    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        Text(
            text = "タイマー"
        )
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.Center,
        ){
            //  分 : 10の位
            SimpleListItemPicker(){
                tensMinutesPlace.intValue = it
            }
            //  分 : 1の位
            SimpleListItemPicker(){
                onesMinutesPlace.intValue = it
            }
            Box(
                modifier = Modifier
                    .height(100.dp)
                    .width(40.dp),
            )
            //  秒 : 10の位
            SimpleListItemPicker(){
                tensSecondsPlace.intValue = it
            }
            //  秒 : 1の位
            SimpleListItemPicker(){
                onesSecondsPlace.intValue = it
            }
        }
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Button(
                modifier = Modifier.padding(end = 6.dp),
                onClick = onBackClicked
            ) {
                Text(text = "キャンセル")
            }
            Button(onClick = {
                val seconds = (tensMinutesPlace.intValue * 600) +
                        (onesMinutesPlace.intValue * 60) +
                        (tensSecondsPlace.intValue * 10) +
                        onesSecondsPlace.intValue
                onCreateClicked(seconds.toFloat())
            }) {
                Text(text = "作成")
            }
        }
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun TimeDetailPreview() {
    TimerDetail(onBackClicked = {}, onCreateClicked = {})
}
