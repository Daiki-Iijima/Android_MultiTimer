package com.example.multitimer.ui

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job

data class TimerCardUiState(
    val id : Int,
    val name: String,
    val defaultTime: Float,
    val timeLeft: Float,
    val isStart: Boolean,
    val keepAfterFinish: Boolean,
    val isComplete: Boolean = false,
    val timerCoroutineScope: CoroutineScope? = null,
)
