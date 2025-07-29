package com.calmly.data.repositories

import com.calmly.data.local.DataStoreManager
import kotlinx.coroutines.flow.Flow

class PreferencesRepositoryImpl(
    private val dataStoreManager: DataStoreManager
) : PreferencesRepository {

    override suspend fun saveLastPlayedSoundId(soundId: String) {
        dataStoreManager.saveLastPlayedSound(soundId)
    }

    override fun getLastPlayedSoundId(): Flow<String?> =
        dataStoreManager.getLastPlayedSound()

    override suspend fun saveTimerPreference(minutes: Int) {
        dataStoreManager.saveTimerPreference(minutes)
    }

    override fun getTimerPreference(): Flow<Int> =
        dataStoreManager.getTimerPreference()
}
