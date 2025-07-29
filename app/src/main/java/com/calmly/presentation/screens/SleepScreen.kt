package com.calmly.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.calmly.data.models.Sound
import com.calmly.presentation.components.SoundCard

@Composable
fun SleepScreen(
    sounds: List<Sound>,
    currentSound: Sound?,
    isPlaying: Boolean,
    onSoundClick: (Sound, Int) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Text(
                text = "Drift into deep, restful sleep with soothing sounds",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 8.dp)
            )
        }

        items(sounds) { sound ->
            SoundCard(
                sound = sound,
                isCurrentSound = currentSound?.id == sound.id,
                isPlaying = isPlaying && currentSound?.id == sound.id,
                onClick = onSoundClick
            )
        }

        item {
            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}
