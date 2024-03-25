package com.example.multitimer.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.delay

@Composable
fun MultiTimerList(
    modifier: Modifier = Modifier,
    timerListViewModel: MultiTimerViewModel,
){
    val timerListUiState by timerListViewModel.uiState.collectAsState()

    LazyColumn(
        contentPadding = PaddingValues(8.dp),
        modifier = modifier
            .fillMaxSize()
    ) {
        items(
            count = timerListUiState.timerCards.size,
            itemContent = {
                Card(modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 8.dp)) {
                    TimerCard(timerListUiState.timerCards[it]){
                        //  タイマー終了後の処理
                    }
                }
            }
        )
    }
}

@Composable
fun TimerCard(timerCardUiState: TimerCardUiState,onComplete: ()->Unit) {
    var timeLeft by rememberSaveable { mutableFloatStateOf(timerCardUiState.time) }
    var startFlag by rememberSaveable { mutableStateOf(false) }
    var completeFlag by rememberSaveable { mutableStateOf(false) }

    val startTimerColor = MaterialTheme.colorScheme.secondary
    val stopTimerColor = MaterialTheme.colorScheme.outline

    LaunchedEffect(key1 = startFlag) {
        val startTime = System.currentTimeMillis() // タイマー開始時刻を記録
        while (startFlag) {
            val currentTime = System.currentTimeMillis()
            val elapsedTime = (currentTime - startTime) / 1000f // 経過時間（秒）
            timeLeft = timerCardUiState.time - elapsedTime // 残り時間を計算

            if (timeLeft <= 0f) {
                // タイマー終了時の処理
                timeLeft = 0f
                startFlag = false
                completeFlag = true
                onComplete()
                break
            }

            delay(10L) // 次の更新まで少し待機
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .clickable {
                if (completeFlag) {
                    completeFlag = false
                }
                startFlag = !startFlag
            }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val progress = timeLeft / timerCardUiState.time // 進捗を計算
                clipRect(
                    top = 0f,
                    bottom = size.height,
                    left = 0f,
                    right = size.width * progress // 進捗に応じて右端を計算
                ) {
                    drawRect(
                        color = if(startFlag) startTimerColor else stopTimerColor // タイマーの進行とともに増える色
                    )
                }
            }
            Column(modifier = Modifier.align(Alignment.Center)) {
                Text(
                    text = "${timeLeft.toInt()}",
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}

@Composable
@Preview(showSystemUi = true)
fun MultiTimerListPreview(){
    val timerListViewModel:MultiTimerViewModel = viewModel()
    timerListViewModel.addTimer(time = 10f, keepAfterFinish = false)
    MultiTimerList(timerListViewModel = timerListViewModel)
}