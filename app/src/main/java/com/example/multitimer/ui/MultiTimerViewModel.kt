package com.example.multitimer.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MultiTimerViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(TimerListUiState())
    val uiState: StateFlow<TimerListUiState> = _uiState.asStateFlow()

    fun addTimer(name: String = "タイマー",time: Float,keepAfterFinish: Boolean){
        val uiTimerCardState = TimerCardUiState(
            name = name,
            time = time,
            isStart = false,
            keepAfterFinish = keepAfterFinish
        )
        _uiState.value = _uiState.value.copy(timerCards = _uiState.value.timerCards + uiTimerCardState)
    }
}
