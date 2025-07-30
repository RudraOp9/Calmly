package com.calmly.domain.usecases

import android.content.Context
import android.content.Intent
import com.calmly.service.MediaPlaybackService

class StopSoundUseCase(

) {
    operator fun invoke(pause: Boolean, context: Context): Result<Unit> {
        return try {
            val intent =
                Intent(context, MediaPlaybackService::class.java)
            if (pause) {
                intent.action = MediaPlaybackService.ACTION_PAUSE
            } else {
                intent.action = MediaPlaybackService.ACTION_STOP
            }
            context.startService(intent)
            Result.success(Unit)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)

        }
    }
}