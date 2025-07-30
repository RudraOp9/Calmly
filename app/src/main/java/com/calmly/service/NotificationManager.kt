package com.calmly.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.calmly.MainActivity
import com.calmly.R
import com.calmly.data.models.Sound

class CalmlyNotificationManager { // Or whatever your manager class is named

    companion object {
        const val CHANNEL_ID = "media_playback_channel" // Ensure this channel is created
        const val NOTIFICATION_ID = 1 // Matches your error
    }

    // Helper to create PendingIntents
    private fun createActionIntent(context: Context, action: String, sound: Sound?): PendingIntent {
        val intent = Intent(context, MediaPlaybackService::class.java).apply {
            this.action = action
            // Pass necessary data for the service to identify the sound, if needed
            // This is important if your play/pause actions need to know which sound
            // especially if the service could be started without a specific sound initially
            // and then told to play something.
            sound?.let {
                putExtra("id", it.id)
                putExtra("title", it.title)
                putExtra("subtitle", it.subtitle)
                putExtra("soundRes", it.soundRes)
                putExtra("category", it.category.ordinal)
                putExtra("thumbRes", it.thumbnailRes)
                putExtra("duration", it.duration)
                // Any other relevant data for the service to resume/play this sound
            }
        }
        // Use a unique request code if you have multiple instances of the same action type,
        // or if the extras in the intent matter for uniqueness.
        // For simple play/pause/stop, using action.hashCode() can be okay if you always
        // want to update the existing PendingIntent for that action.
        val requestCode = action.hashCode() + (sound?.id?.hashCode() ?: 0) // Make request code more unique if sound changes

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            PendingIntent.getForegroundService( // Use for foreground services
                context,
                requestCode,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        } else {
            PendingIntent.getService(
                context,
                requestCode,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        }
    }

    fun createNotification(
        context: Context,
        sound: Sound?,
        isPlaying: Boolean,
        albumArt: Bitmap? = null // Optional: Pass album art
    ): Notification {
        if (sound == null) {
            // This case should ideally be handled before calling createNotification
            // or return a basic "stopped" notification or throw an error.
            // For now, let's create a minimal notification to avoid NPE,
            // though the service should ideally stopForeground directly.
            return NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_background) // Replace with actual placeholder
                .setContentTitle("No sound selected")
                .build()
        }

        val playPauseIntent = if (isPlaying) {
            createActionIntent(context, MediaPlaybackService.ACTION_PAUSE, sound)
        } else {
            createActionIntent(context, MediaPlaybackService.ACTION_PLAY, sound)
        }

        val stopIntent = createActionIntent(context, MediaPlaybackService.ACTION_STOP, sound)

        val playPauseIcon = if (isPlaying) R.drawable.ic_pause else R.drawable.ic_play
        val playPauseTitle = if (isPlaying) "Pause" else "Play"

        val notificationBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification) // **IMPORTANT: Use a real notification icon**
            .setContentTitle(sound.title)
            .setContentText(sound.subtitle).setOngoing(true)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setPriority(NotificationCompat.PRIORITY_LOW)

            // === Add actions FIRST ===
            .addAction(playPauseIcon, playPauseTitle, playPauseIntent) // Action index 0
            .addAction(R.drawable.ic_stop, "Stop", stopIntent)      // Action index 1
        // You can add more actions like Next, Previous if your player supports them
        // .addAction(R.drawable.ic_skip_next, "Next", nextIntent) // Action index 2

        // === Then apply MediaStyle and specify compact view actions ===
        val mediaStyle = androidx.media.app.NotificationCompat.MediaStyle()
        // You might need to integrate with MediaSessionCompat for full media style features
        // .setMediaSession(mediaSessionToken) // If you have a MediaSession

        // Now that actions are added, these indices are valid:
        mediaStyle.setShowActionsInCompactView(0, 1) // Show Play/Pause (index 0) and Stop (index 1)

        notificationBuilder.setStyle(mediaStyle)

        return notificationBuilder.build()
    }
}