package com.calmly.data.models

data class PlaybackState(
    val currentSound: Sound? = null,
    val isPlaying: Boolean = false,
    val progress: Float = 0f,
    val timerMinutes: Int = 0, // 0 means no timer
    val remainingTime: Long = 0L
)