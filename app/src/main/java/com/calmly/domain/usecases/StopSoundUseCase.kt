package com.calmly.domain.usecases

import com.calmly.service.MediaPlaybackService

class StopSoundUseCase(

) {
    operator fun invoke(pause:Boolean): Result<Unit> {
        return try {
       /*     if (pause){
                mediaService.pausePlayback()
            }else{
                mediaService.stopPlayback()
            }*/
            Result.success(Unit)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)

        }
    }
}