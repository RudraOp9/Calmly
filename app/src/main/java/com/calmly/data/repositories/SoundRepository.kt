package com.calmly.data.repositories

import com.calmly.data.models.Sound

interface SoundRepository {
    fun getMeditationSounds(): List<Sound>
    fun getSleepSounds(): List<Sound>
    fun getAllSounds(): List<Sound>
    fun getSoundById(id: String): Sound?
}