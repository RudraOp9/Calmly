package com.calmly.presentation.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.calmly.data.models.Sound

@Composable
fun SoundCard(
    sound: Sound,
    isCurrentSound: Boolean,
    isPlaying: Boolean,
    onClick: (Sound, Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var showTimerDialog by remember { mutableStateOf(false) }

    val containerColor by animateColorAsState(
        targetValue = if (isCurrentSound && isPlaying)
            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
        else
            MaterialTheme.colorScheme.surface,
        label = "container_color"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clickable {
                if (isCurrentSound && isPlaying) {
                    onClick(sound, 0) // Stop current sound
                } else {
                    showTimerDialog = true
                }
            },
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isCurrentSound) 6.dp else 2.dp
        ),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Sound Thumbnail
            Image(
                painter = painterResource(sound.thumbnailRes),
                contentDescription = sound.title,
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Sound Info
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = sound.title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )

                Text(
                    text = sound.subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            // Play/Pause Button
            IconButton(
                onClick = {
                    showTimerDialog = if (isCurrentSound && isPlaying) {
                        true
                    } else {
                        true
                    }
                }
            ) {
                Icon(
                    imageVector = if (isCurrentSound && isPlaying)
                        Icons.Filled.Replay
                    else
                        Icons.Filled.PlayArrow,
                    contentDescription = if (isCurrentSound && isPlaying) "Pause" else "Play",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }

    if (showTimerDialog) {
        TimerDialog(
            onTimerSelected = { minutes ->
                onClick(sound, minutes)
                showTimerDialog = false
            },
            onDismiss = { showTimerDialog = false }
        )
    }
}
