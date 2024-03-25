package com.example.multitimer.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.selects.select

@Composable
fun SimpleListItemPicker() {
    val list = remember { listOf("0","1", "2", "3", "4", "5","6","7","8","9","10") }

    var selectedIndex by remember { mutableStateOf(0) } // 初期選択値

    //  画面密度
    val density = LocalDensity.current

    var latestDragAmount by remember {
        mutableStateOf(0.dp)
    }

    val height = 100.dp
    Box(
        modifier = Modifier
            .height(height)
            .width(60.dp)
            .pointerInput(Unit) {
                detectVerticalDragGestures(onDragEnd = {
                    latestDragAmount = 0.dp
                }) { _, dragAmount ->
                    // ドラッグ距離をdpに変換して更新
                    val dragAmountDp = with(density) { dragAmount.toDp() }

                    //  最大かつ、もっと増やそうとしたら
                    if (selectedIndex == list.size - 1 && dragAmount < 0) {
                        return@detectVerticalDragGestures
                    }

                    //  最小かつ、もっと減らそうとしたら
                    if (selectedIndex == 0 && dragAmount > 0) {
                        return@detectVerticalDragGestures
                    }

                    latestDragAmount += dragAmountDp
                    if (latestDragAmount >= 32.dp) {
                        if (selectedIndex != 0) {
                            selectedIndex--.coerceIn(0, list.size - 1)
                        }
                        latestDragAmount = 0.dp
                    } else if (latestDragAmount <= -32.dp) {

                        if (selectedIndex != list.size - 1) {
                            selectedIndex++.coerceIn(0, list.size - 1)
                        }
                        latestDragAmount = 0.dp
                    }
                }
            }
    ) {
        val top = latestDragAmount
        val center = latestDragAmount + 32.dp
        val bottom = latestDragAmount + 32.dp * 2

        val topIndex = (selectedIndex - 1).coerceIn(0,list.size-1)
        val bottomIndex = (selectedIndex + 1).coerceIn(0,list.size-1)
        val topExIndex = (selectedIndex - 2).coerceIn(0,list.size-1)
        val bottomExIndex = (selectedIndex + 2).coerceIn(0,list.size-1)

        ProvideTextStyle(value = TextStyle(fontSize = 24.sp)) {
            //  上スクロール時なので、下に生成
            if(top < 0.dp && selectedIndex < list.size -2){
                Text(
                    text =  list[bottomExIndex],
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .offset(y = latestDragAmount + 32.dp * 3)
                        .alpha(0.05f)
                        .fillMaxWidth()
                    )
            }
            //  下スクロール時なので、上に生成
            if(bottom > 32.dp * 2 && selectedIndex > 1){
                Text(
                    text = list[topExIndex],
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .offset(y = latestDragAmount - 32.dp)
                        .alpha(0.05f)
                        .fillMaxWidth()
                )
            }
            if(selectedIndex > 0) {
                Text(
                    text = list[topIndex],
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .offset(y = top)
                        .alpha(0.3f)
                        .fillMaxWidth()
                )
            }
            Text(
                text = list[selectedIndex],
                style = LocalTextStyle.current.copy(fontWeight = FontWeight.Bold),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .offset(y = center)
                    .fillMaxWidth()
            )
            if(selectedIndex < list.size-1) {
                Text(
                    text = list[bottomIndex],
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .offset(y = bottom)
                        .alpha(0.3f)
                        .fillMaxWidth()
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun SimpleListItemPickerPreview(){
    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        Row (
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.Center,
        ){
            SimpleListItemPicker()
            SimpleListItemPicker()
            Box(
                modifier = Modifier
                    .height(100.dp)
                    .width(40.dp),
            )
            SimpleListItemPicker()
            SimpleListItemPicker()
        }
    }
}
