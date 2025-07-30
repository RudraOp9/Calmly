package com.calmly.service

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Binder
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.calmly.R
import com.calmly.data.models.Sound
import com.calmly.data.models.SoundCategory
import com.calmly.service.CalmlyNotificationManager.Companion.CHANNEL_ID
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.IOException

class MediaPlaybackService() : Service() {

    private val binder = MediaBinder()
    private var mediaPlayer: MediaPlayer? = null
    private var currentSound: Sound? = null
    private var timerJob: Job? = null
    private var notificationManager: CalmlyNotificationManager? = null

    inner class MediaBinder : Binder() {
        fun getService(): MediaPlaybackService = this@MediaPlaybackService
    }

    override fun onCreate() {
        super.onCreate()
        Log.d("MediaService", "Service created")
        mediaPlayer = MediaPlayer()
    }

    override fun onBind(intent: Intent?): IBinder {
        return binder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        // Handle media button actions from notification
        when (intent?.action) {
            ACTION_PLAY -> {
                val id = intent.getStringExtra("id")!!
                val title = intent.getStringExtra("title")!!
                val subtitle = intent.getStringExtra("subtitle")!!
                val soundRes = intent.getIntExtra("soundRes", 0)
                val category = intent.getIntExtra("category", 0)
                val thumbRes = intent.getIntExtra("thumbRes", 0)
                val duration = intent.getLongExtra("duration", 0L)
                val sound = Sound(id, title, subtitle, SoundCategory.entries[category], soundRes, thumbRes, duration)
                val timerMinutes = intent.getIntExtra("timerMinutes", 0)
                playSound(sound, timerMinutes)
            }
            ACTION_RESUME -> {
                if (currentSound != null){
                    resumePlayback()
                }else{
                    val id = intent.getStringExtra("id")
                    val title = intent.getStringExtra("title")
                    val subtitle = intent.getStringExtra("subtitle")
                    val soundRes = intent.getIntExtra("soundRes", 0)
                    val category = intent.getIntExtra("category",0)
                    val thumbRes = intent.getIntExtra("thumbRes",0)
                    val duration = intent.getLongExtra("duration",0L)
                    val sound = Sound(id!!, title!!, subtitle!!, SoundCategory.entries[category], soundRes,thumbRes,duration)
                    val timerMinutes = intent.getIntExtra("timerMinutes", 0)
                    playSound(sound, timerMinutes)

                }

            }

            ACTION_PAUSE -> pausePlayback()
            ACTION_STOP -> stopPlayback()
        }
        return START_STICKY
    }

    fun playSound(sound: Sound, timerMinutes: Int = 0) {
        try {
            mediaPlayer?.release()
            mediaPlayer = MediaPlayer().apply {
                setDataSource(this@MediaPlaybackService, getRawUri(sound.soundRes))
                prepareAsync()
                isLooping = true
                setOnPreparedListener {
                    currentSound = sound
                    // Initialize notification manager if not already done
                    updateNotification(sound, true)

                    // Start timer if specified
                    if (timerMinutes > 0) {
                        startTimer(timerMinutes)
                    }
                    start()
                    Log.d("MediaService", "Started playing: ${sound.title}")
                }

                setOnErrorListener { mp, what, extra ->
                    Log.e("MediaService", "MediaPlayer error: what=$what, extra=$extra")
                    false
                }
                //  prepareAsync()
            }
        } catch (e: IOException) {
            Log.e("MediaService", "Error setting up MediaPlayer", e)
        }
    }

    fun pausePlayback() {
        mediaPlayer?.let { player ->
            if (player.isPlaying) {
                player.pause()
                currentSound?.let { sound ->
                    updateNotification(sound, false)
                }
                Log.d("MediaService", "Playback paused")
            }
        }
    }

    fun resumePlayback() {
        mediaPlayer?.let { player ->
            if (!player.isPlaying) {
                player.start()
                currentSound?.let { sound ->
                    updateNotification(sound, true)
                }
                Log.d("MediaService", "Playback resumed")
            }
        }
    }

    fun stopPlayback() {
        mediaPlayer?.let { player ->
            if (player.isPlaying) {
                player.stop()
            }
            player.release()
        }
        mediaPlayer = null
        currentSound = null

        // Cancel timer
        timerJob?.cancel()
        timerJob = null

        // Remove notification
        stopForeground(STOP_FOREGROUND_REMOVE)
        Log.d("MediaService", "Playback stopped")
    }

    fun isPlaying(): Boolean = mediaPlayer?.isPlaying == true

    fun getCurrentSound(): Sound? = currentSound

    private fun startTimer(minutes: Int) {
        timerJob?.cancel()
        timerJob = CoroutineScope(Dispatchers.IO).launch {
            delay(minutes * 60 * 1000L)
            stopPlayback()
        }
        Log.d("MediaService", "Timer set for $minutes minutes")
    }

    private fun updateNotification(sound: Sound?, isPlaying: Boolean) {
        if (sound == null) {
            stopForeground(STOP_FOREGROUND_REMOVE)
            return
        }
        // Ensure notificationManager is initialized

        Log.e("tagy", "trying to send notification")
        try {
            if (notificationManager == null) {1111
                notificationManager = CalmlyNotificationManager()
            }
            val notification =
                notificationManager?.createNotification(this@MediaPlaybackService, sound,isPlaying)
            // Use ServiceCompat.startForeground to be compatible with older Android versions
            startForeground(1, notification)
            /*      startForeground(
                       NOTIFICATION_ID, notification, FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK
                  )*/
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        stopPlayback()
        notificationManager = null
        Log.d("MediaService", "Service destroyed")
    }


    companion object {
        const val ACTION_PLAY = "com.calmly.ACTION_PLAY"
        const val ACTION_PAUSE = "com.calmly.ACTION_PAUSE"
        const val ACTION_STOP = "com.calmly.ACTION_STOP"
        const val ACTION_RESUME = "com.calmly.ACTION_RESUME"
        const val NOTIFICATION_ID = 1
    }

    private fun getRawUri(id: Int) = Uri.parse("android.resource://${packageName}/${id}")

}
