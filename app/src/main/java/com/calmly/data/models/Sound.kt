package com.calmly.data.models

import androidx.annotation.DrawableRes
import androidx.annotation.RawRes

data class Sound(
    val id: String,
    val title: String,
    val subtitle: String,
    val category: SoundCategory,
    @RawRes val soundRes: Int,
    @DrawableRes val thumbnailRes: Int,
    val duration: Long = 0L // For infinite looping sounds
)