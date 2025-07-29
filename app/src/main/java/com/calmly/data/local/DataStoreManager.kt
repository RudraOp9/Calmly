package com.calmly.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "calmly_preferences")

class DataStoreManager(private val context: Context) {

    companion object {
        private val LAST_PLAYED_SOUND = stringPreferencesKey("last_played_sound")
        private val TIMER_PREFERENCE = intPreferencesKey("timer_preference")
    }

    suspend fun saveLastPlayedSound(soundId: String) {
        context.dataStore.edit { preferences ->
            preferences[LAST_PLAYED_SOUND] = soundId
        }
    }

    fun getLastPlayedSound(): Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[LAST_PLAYED_SOUND]
    }

    suspend fun saveTimerPreference(minutes: Int) {
        context.dataStore.edit { preferences ->
            preferences[TIMER_PREFERENCE] = minutes
        }
    }

    fun getTimerPreference(): Flow<Int> = context.dataStore.data.map { preferences ->
        preferences[TIMER_PREFERENCE] ?: 0
    }
}