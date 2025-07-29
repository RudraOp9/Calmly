package com.calmly.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.calmly.presentation.components.TopBar
import com.calmly.presentation.viewmodel.SoundsViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: SoundsViewModel = koinViewModel()
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Meditation", "Sleep")
    val playbackState by viewModel.playbackState
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Custom Top Bar with current playing info
        TopBar(
            currentSound = playbackState.currentSound,
            isPlaying = playbackState.isPlaying,
            onPlayPauseClick = { viewModel.pauseResumeSound(context) },
            onStopClick = { viewModel.stopCurrentSound() }
        )

        // Tab Row with soft Material 3 design
        PrimaryTabRow(
            selectedTabIndex = selectedTab,
            modifier = Modifier.fillMaxWidth(),
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    modifier = Modifier.padding(vertical = 16.dp)
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = if (selectedTab == index) FontWeight.SemiBold else FontWeight.Normal
                        ),
                        color = if (selectedTab == index)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // Content based on selected tab
        when (selectedTab) {
            0 -> MeditationScreen(
                sounds = viewModel.meditationSounds.value,
                currentSound = playbackState.currentSound,
                isPlaying = playbackState.isPlaying,
                onSoundClick = { sound, timer -> viewModel.playSound(sound, timer,context) }
            )
            1 -> SleepScreen(
                sounds = viewModel.sleepSounds.value,
                currentSound = playbackState.currentSound,
                isPlaying = playbackState.isPlaying,
                onSoundClick = { sound, timer -> viewModel.playSound(sound, timer,context) }
            )
        }
    }
}
