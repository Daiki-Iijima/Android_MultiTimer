package com.example.multitimer.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.concurrent.timer

const val LOG_TAG = "MULTI_TIMER"

class MultiTimerViewModel : ViewModel() {


    private val _uiState = MutableStateFlow(TimerListUiState())
    val uiState: StateFlow<TimerListUiState> = _uiState.asStateFlow()

    fun addTimer(
        name: String = "タイマー",
        time: Float,
        keepAfterFinish: Boolean = false,
    ) {
        val uiTimerCardState = TimerCardUiState(
            id = _uiState.value.timerCards.size,
            name = name,
            defaultTime = time,
            timeLeft = time,
            isStart = false,
            keepAfterFinish = keepAfterFinish
        )

        _uiState.value =
            _uiState.value.copy(timerCards = _uiState.value.timerCards + uiTimerCardState)
    }

    private fun findTimer(id: Int): TimerCardUiState? {
        //  指定されたIDのタイマーがあるかのチェック
        return uiState.value.timerCards.find {
            it.id == id
        }
    }

    //  idが同じタイマーカードを渡すことで更新できる
    private fun updateTimerCardUiState(timerCard: TimerCardUiState) {
        //  タイマー配列をまるっとコピーして対象のデータだけ更新
        val updateTimerCars = _uiState.value.timerCards.map { timer ->
            if (timer.id == timerCard.id) timerCard
            else timer
        }

        _uiState.value = _uiState.value.copy(timerCards = updateTimerCars)
    }

    private fun isStartToggle(id: Int): Boolean {
        val timerCard = findTimer(id) ?: return false
        val updateTimerCard = timerCard.copy(isStart = !timerCard.isStart)

        updateTimerCardUiState(updateTimerCard)

        return updateTimerCard.isStart
    }

    fun toggleStart(id: Int) {
        //  現状の状態を取得
        val flag = isStartToggle(id)

        if (flag) {
            timerProcess(id)
        }
    }

    private fun timerProcess(id: Int) {
        viewModelScope.launch {
            //  タイマー開始時の時間を保存
            val startTime = System.currentTimeMillis()
            var latestElapseTime = 0f

            while (true) {

                //  指定されたIDのタマーがあるかのチェック
                val timerCard = findTimer(id) ?: break

                //  タイマーが開始していない場合は終了
                if (!timerCard.isStart) {
                    break
                }

                val currentTime = System.currentTimeMillis()
                val elapsedTime = (currentTime - startTime) / 1000f // 経過時間（秒）

                val updateTime = timerCard.timeLeft - elapsedTime + latestElapseTime  // 残り時間を計算
                latestElapseTime = elapsedTime

                //  終了判定
                if (updateTime <= 0f) {
                    val updateTimerCard = timerCard.copy(
                        timeLeft = 0f,
                        isStart = false,
                        isComplete = true
                    )

                    //  更新
                    updateTimerCardUiState(updateTimerCard)

                    Log.d(LOG_TAG, "タイマー終了 id : $id")
                    break
                }

                //  タイマー更新
                val updateTimerCard = timerCard.copy(timeLeft = updateTime) // 残り時間を計算
                updateTimerCardUiState(updateTimerCard)

                delay(10L) // 次の更新まで少し待機
            }
        }
    }

    fun deleteTimer(id: Int) {

    }
}
