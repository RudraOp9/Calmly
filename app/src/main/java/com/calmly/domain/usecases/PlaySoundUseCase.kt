package com.calmly.domain.usecases

import android.content.Context
import android.content.Intent
import com.calmly.data.models.Sound
import com.calmly.service.MediaPlaybackService

class PlaySoundUseCase(

) {
    operator fun invoke(sound: Sound, timerMinutes: Int = 0, context: Context): Result<Unit> {
        return try {
            val intent =
                Intent(context, MediaPlaybackService::class.java).apply {
                    action = MediaPlaybackService.ACTION_PLAY
                    putExtra("id", sound.id)
                    putExtra("title", sound.title)
                    putExtra("subtitle", sound.subtitle)
                    putExtra("soundRes", sound.soundRes)
                    putExtra("category", sound.category.ordinal)
                    putExtra("thumbRes", sound.thumbnailRes)
                    putExtra("timerMinutes", timerMinutes)
                    putExtra("duration", sound.duration)
                }
            context.startService(intent)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun resume(sound: Sound, timerMinutes: Int, context: Context): Result<Unit> {
        return try {
            val intent =
                Intent(context, MediaPlaybackService::class.java).apply {
                    action = MediaPlaybackService.ACTION_RESUME
                    putExtra("id", sound.id)
                    putExtra("title", sound.title)
                    putExtra("subtitle", sound.subtitle)
                    putExtra("soundRes", sound.soundRes)
                    putExtra("category", sound.category.ordinal)
                    putExtra("thumbRes", sound.thumbnailRes)
                    putExtra("timerMinutes", timerMinutes)
                    putExtra("duration", sound.duration)
                }
            context.startService(intent)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}