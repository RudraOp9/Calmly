package com.calmly.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.calmly.MainActivity
import com.calmly.R
import com.calmly.data.models.Sound

class CalmlyNotificationManager() {
    companion object {
        const val CHANNEL_ID = "media_playback_channel"
    }
}