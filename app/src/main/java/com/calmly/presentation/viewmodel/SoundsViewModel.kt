package com.calmly.presentation.viewmodel

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.calmly.data.models.PlaybackState
import com.calmly.data.models.Sound
import com.calmly.data.repositories.PreferencesRepository
import com.calmly.data.repositories.SoundRepository
import com.calmly.domain.usecases.PlaySoundUseCase
import com.calmly.domain.usecases.StopSoundUseCase
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class SoundsViewModel(
    private val soundRepository: SoundRepository,
    private val preferencesRepository: PreferencesRepository,
    private val playSoundUseCase: PlaySoundUseCase,
    private val stopSoundUseCase: StopSoundUseCase
) : ViewModel() {

    private val _playbackState = mutableStateOf(PlaybackState())
    val playbackState: State<PlaybackState> = _playbackState

    private val _meditationSounds = mutableStateOf<List<Sound>>(emptyList())
    val meditationSounds: State<List<Sound>> = _meditationSounds

    private val _sleepSounds = mutableStateOf<List<Sound>>(emptyList())
    val sleepSounds: State<List<Sound>> = _sleepSounds

    // when download ing from online
    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    init {
        loadSounds()
        observeLastPlayedSound()
    }

    private fun loadSounds() {
        _meditationSounds.value = soundRepository.getMeditationSounds()
        _sleepSounds.value = soundRepository.getSleepSounds()
    }

    private fun observeLastPlayedSound() {
        preferencesRepository.getLastPlayedSoundId()
            .onEach { soundId ->
                soundId?.let { id ->
                    val sound = soundRepository.getSoundById(id)
                    if (sound != null && _playbackState.value.currentSound == null) {
                        // Don't auto-play, just remember the last sound
                        _playbackState.value = _playbackState.value.copy(currentSound = sound)
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    fun playSound(sound: Sound, timerMinutes: Int = 0, context: Context) {
        _isLoading.value = true
        // Stop current sound if playing
        if (_playbackState.value.isPlaying) {
            stopSoundUseCase(false)
        }
        playSoundUseCase(sound, timerMinutes, context)
            .onSuccess {
                _playbackState.value = _playbackState.value.copy(
                    currentSound = sound,
                    isPlaying = true,
                    timerMinutes = timerMinutes
                )
                // Save last played sound
                viewModelScope.launch {
                    preferencesRepository.saveLastPlayedSoundId(sound.id)
                }
            }
            .onFailure { error ->
                // Handle error - could show a snackbar or toast
                println("Error playing sound: ${error.message}")
                error.printStackTrace()
                error.cause?.printStackTrace()
            }

        _isLoading.value = false

    }

    fun stopCurrentSound() {
        viewModelScope.launch {
            stopSoundUseCase(pause = false)
                .onSuccess {
                    _playbackState.value = _playbackState.value.copy(
                        isPlaying = false,
                        timerMinutes = 0
                    )
                }
        }
    }

    fun pauseCurrentSound() {
        viewModelScope.launch {
            stopSoundUseCase(pause = true)
                .onSuccess {
                    _playbackState.value = _playbackState.value.copy(
                        isPlaying = false,
                        timerMinutes = 0
                    )
                }
        }
    }

    fun resumeCurrentSound(sound: Sound, timerMinutes: Int,context: Context) {
        viewModelScope.launch {
            playSoundUseCase.resume(sound,timerMinutes,context)
                .onSuccess {
                    _playbackState.value = _playbackState.value.copy(
                        isPlaying = true,
                        timerMinutes = 0
                    )
                }
        }
    }

    fun pauseResumeSound(context: Context) {
        if (_playbackState.value.isPlaying) {
            pauseCurrentSound()
        } else {
               _playbackState.value.currentSound?.let { sound ->
                   resumeCurrentSound(sound, _playbackState.value.timerMinutes,context)
               }
        }
    }
}