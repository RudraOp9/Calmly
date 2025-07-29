package com.calmly.data.repositories

import com.calmly.data.local.DataStoreManager
import kotlinx.coroutines.flow.Flow

interface PreferencesRepository {
    suspend fun saveLastPlayedSoundId(soundId: String)
    fun getLastPlayedSoundId(): Flow<String?>
    suspend fun saveTimerPreference(minutes: Int)
    fun getTimerPreference(): Flow<Int>
}