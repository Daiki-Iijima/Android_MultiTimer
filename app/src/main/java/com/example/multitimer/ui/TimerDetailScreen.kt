package com.example.multitimer.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun TimerDetail(
    onBackClicked: ()->Unit,
){
    Box(
        modifier = Modifier.fillMaxSize()
    ){
        Button(onClick = onBackClicked) {
            Text(text = "戻る")
        }
    }
}