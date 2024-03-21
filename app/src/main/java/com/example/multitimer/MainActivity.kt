package com.example.multitimer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.multitimer.ui.theme.MultiTimerTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MultiTimerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MultiTimerApp()
                }
            }
        }
    }
}


@Composable
fun TimerCard(timer: Float,onComplete: ()->Unit) {
    var timeLeft by rememberSaveable { mutableFloatStateOf(timer) }
    var startFlag by rememberSaveable { mutableStateOf(false) }
    var completeFlag by rememberSaveable { mutableStateOf(false) }

    val startTimerColor = MaterialTheme.colorScheme.secondary
    val stopTimerColor = MaterialTheme.colorScheme.outline

    LaunchedEffect(key1 = startFlag) {
        val startTime = System.currentTimeMillis() // タイマー開始時刻を記録
        while (startFlag) {
            val currentTime = System.currentTimeMillis()
            val elapsedTime = (currentTime - startTime) / 1000f // 経過時間（秒）
            timeLeft = timer - elapsedTime // 残り時間を計算

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
                val progress = timeLeft / timer // 進捗を計算
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
fun MultiTimerApp(modifier: Modifier = Modifier){
    val timerList = remember { mutableStateListOf(10f,10f) }

    MultiTimerTheme {
        Scaffold(
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        timerList.add(20f)
                    }
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
                }
            },
            modifier = modifier
        ) { innerPadding->
            LazyColumn(
                contentPadding = PaddingValues(8.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                items(
                    count = timerList.size,
                    itemContent = {
                        Card(modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom = 8.dp)) {
                            //  TODO : データを再構築してしまうので、現状の設計では他のカウントダウンが初期化される。上のタイマーが先に消されると再構築が発生するっぽい
                            TimerCard(timerList[it]){
                                timerList.removeAt(it)
                            }
                        }
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CoroutineTestAppPreview() {
    MultiTimerApp()
}
