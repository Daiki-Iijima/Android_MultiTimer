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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun MultiTimerList(
    modifier: Modifier = Modifier,
    timerListViewModel: MultiTimerViewModel,
) {
    val timerListUiState by timerListViewModel.uiState.collectAsState()

    LazyColumn(
        contentPadding = PaddingValues(8.dp),
        modifier = modifier
            .fillMaxSize()
    ) {
        items(
            count = timerListUiState.timerCards.size,
            itemContent = {
                Card(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 8.dp)
                ) {
                    val timerCard = timerListUiState.timerCards[it]
                    TimerCard(
                        timerCardUiState = timerCard,
                        onStartToggle = {
                            timerListViewModel.toggleStart(timerCard.id)
                        },
                    )
                }
            }
        )
    }
}

@Composable
fun TimerCard(
    timerCardUiState: TimerCardUiState,
    onStartToggle: () -> Unit,
) {

    val startTimerColor = MaterialTheme.colorScheme.secondary
    val stopTimerColor = MaterialTheme.colorScheme.outline

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .clickable {
                onStartToggle()
            }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val progress = timerCardUiState.timeLeft / timerCardUiState.defaultTime // 進捗を計算
                clipRect(
                    top = 0f,
                    bottom = size.height,
                    left = 0f,
                    right = size.width * progress // 進捗に応じて右端を計算
                ) {
                    drawRect(
                        color = if (timerCardUiState.isStart) startTimerColor else stopTimerColor // タイマーの進行とともに増える色
                    )
                }
            }
            Column(modifier = Modifier.align(Alignment.Center)) {
                Text(
                    text = "${timerCardUiState.timeLeft.toInt()}",
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}

@Composable
@Preview(showSystemUi = true)
fun MultiTimerListPreview() {
    val timerListViewModel: MultiTimerViewModel = viewModel()
    timerListViewModel.addTimer(time = 10f, keepAfterFinish = false)
    MultiTimerList(timerListViewModel = timerListViewModel)
}