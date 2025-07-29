package com.calmly.data.repositories

import com.calmly.R
import com.calmly.data.models.Sound
import com.calmly.data.models.SoundCategory

class SoundRepositoryImpl : SoundRepository {

    private val meditationSounds = listOf(
        Sound(
            id = "forest_rain",
            title = "Sonically sound",
            subtitle = "sonically sound music",
            category = SoundCategory.MEDITATION,
            soundRes = R.raw.sonically_sound__music_box_2_g,
            thumbnailRes = R.drawable.img
        ),
        Sound(
            id = "ocean_waves",
            title = "Ocean Waves",
            subtitle = "river ligh stream day time",
            category = SoundCategory.MEDITATION,
            soundRes = R.raw.vrymaa__river_light_stream_daytime,
            thumbnailRes = R.drawable.img
        ),
        Sound(
            id = "campfire",
            title = "Flying Deer",
            subtitle = "FX music box",
            category = SoundCategory.MEDITATION,
            soundRes = R.raw.flying_deer_fx_music_box_j,
            thumbnailRes = R.drawable.img
        )
    )

    private val sleepSounds = listOf(
        Sound(
            id = "white_noise",
            title = "Water Tickle",
            subtitle = "Consistent, calming static",
            category = SoundCategory.SLEEP,
            soundRes = R.raw.alexyquest42__water_trickle,
            thumbnailRes = R.drawable.img
        ),
        Sound(
            id = "brown_noise",
            title = "Forest Birds",
            subtitle = "Ibiza Sant Mateu forest birds",
            category = SoundCategory.SLEEP,
            soundRes = R.raw.burokelis_ibiza_sant_mateu_forest_birds,
            thumbnailRes = R.drawable.img
        ),
        Sound(
            id = "fan_sound",
            title = "Flying Sound",
            subtitle = "Steady fan whirring",
            category = SoundCategory.SLEEP,
            soundRes = R.raw.flying_deer_fx_music_box_j,
            thumbnailRes = R.drawable.img
        )
    )

    override fun getMeditationSounds(): List<Sound> = meditationSounds
    override fun getSleepSounds(): List<Sound> = sleepSounds
    override fun getAllSounds(): List<Sound> = meditationSounds + sleepSounds

    override fun getSoundById(id: String): Sound? =
        getAllSounds().find { it.id == id }
}
