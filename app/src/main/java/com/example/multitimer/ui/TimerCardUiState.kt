package com.example.multitimer.ui

data class TimerCardUiState(
    val name: String,
    val time: Float,
    val isStart: Boolean,
    val keepAfterFinish: Boolean,
)
