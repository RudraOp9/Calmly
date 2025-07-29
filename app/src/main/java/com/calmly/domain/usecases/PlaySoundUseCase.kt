package com.calmly.domain.usecases

import android.content.Context
import com.calmly.data.models.Sound
import com.calmly.service.MediaPlaybackService

class PlaySoundUseCase(
    private val mediaService: MediaPlaybackService
) {
    operator fun invoke(sound: Sound, timerMinutes: Int = 0, context: Context):Result<Unit> {
        return try {
            mediaService.playSound(sound, timerMinutes, context)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun resume(sound: Sound, timerMinutes: Int,context: Context): Result<Unit> {
        return try {
            if (mediaService.getCurrentSound() != null){
                mediaService.resumePlayback()
            }else{
                invoke(sound,timerMinutes, context )
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}