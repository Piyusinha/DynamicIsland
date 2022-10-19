package com.anaa.dynamicisland.service

import android.content.ComponentName
import android.content.Context
import android.media.MediaMetadata
import android.media.session.MediaController
import android.media.session.MediaSession
import android.media.session.MediaSessionManager
import android.media.session.PlaybackState
import android.os.Bundle
import android.service.notification.NotificationListenerService
import android.util.Log
import android.widget.Toast
import com.anaa.dynamicisland.AccessbilityStaticClass
import com.anaa.dynamicisland.accessibilty.ComposeAccessibiltyService
import com.anaa.dynamicisland.application.DynamicApplication


class DynamicNotificationListenerService : NotificationListenerService() {

    private val TAG = this.javaClass.simpleName

    private var mediaController: MediaController? = null

    private lateinit var mediaSessionManager: MediaSessionManager

    var musicIsPaused = true

    var currentMediaData :MediaMetadata? = null
    private val mediaControllerCallback: MediaController.Callback = object : MediaController.Callback() {
        override fun onSessionDestroyed() {
            super.onSessionDestroyed()
        }

        override fun onSessionEvent(event: String, extras: Bundle?) {
            super.onSessionEvent(event, extras)
        }

        override fun onQueueChanged(queue: MutableList<MediaSession.QueueItem>?) {
            super.onQueueChanged(queue)
        }

        override fun onQueueTitleChanged(title: CharSequence?) {
            super.onQueueTitleChanged(title)
        }

        override fun onExtrasChanged(extras: Bundle?) {
            super.onExtrasChanged(extras)
        }

        override fun onAudioInfoChanged(info: MediaController.PlaybackInfo?) {
            super.onAudioInfoChanged(info)
        }

        override fun onPlaybackStateChanged(playbackState: PlaybackState?) {
            when (playbackState?.state) {
                PlaybackState.STATE_PLAYING -> {
                    musicIsPaused = false
                    mediaController?.metadata?.let { metadata ->
                        try {
                            if(currentMediaData?.getString(MediaMetadata.METADATA_KEY_TITLE) == metadata.getString(MediaMetadata.METADATA_KEY_TITLE)) return
                            (application as DynamicApplication).composeAccessibiltyService?.onMusicStartPlaying(metadata)
                            currentMediaData = metadata
                        } catch (e: RuntimeException) {
                            Log.e(TAG, "An error occurred reading the media metadata: $e")
                        }
                    }
                }
                PlaybackState.STATE_PAUSED -> {
                    musicIsPaused = true
                    currentMediaData = null
                    (application as DynamicApplication).composeAccessibiltyService?.onMusicStopped()
                }
                PlaybackState.STATE_STOPPED -> {
                    musicIsPaused = true
                    currentMediaData = null
                    (application as DynamicApplication).composeAccessibiltyService?.onMusicStopped()
                }
            }
        }

        override fun onMetadataChanged(metadata: MediaMetadata?) {
            metadata?.let { metadata ->
                if((musicIsPaused && currentMediaData?.getString(MediaMetadata.METADATA_KEY_TITLE) == metadata.getString(MediaMetadata.METADATA_KEY_TITLE))) return
                try {
                    currentMediaData = metadata
                    (application as DynamicApplication).composeAccessibiltyService?.onMusicStartPlaying(metadata)
                } catch (e: RuntimeException) {
                    Log.e(TAG, "An error occurred reading the media metadata: $e")
                }
            }
        }
    }

    private val activeSessionsChangedListener = MediaSessionManager.OnActiveSessionsChangedListener { registerActiveMediaControllerCallback(mediaSessionManager) }

    override fun onCreate() {
        super.onCreate()

        DynamicApplication().setNotificationServiceValue(this)
        mediaSessionManager = getSystemService(Context.MEDIA_SESSION_SERVICE) as MediaSessionManager

        registerActiveMediaControllerCallback(mediaSessionManager)
        addSessionStateChangeListener()
    }

    override fun onListenerConnected() {
        super.onListenerConnected()

        addSessionStateChangeListener()
    }

    override fun onDestroy() {
        unregisterCallback(mediaController)

        super.onDestroy()
    }

    private fun addSessionStateChangeListener() {
        try {
            mediaSessionManager.addOnActiveSessionsChangedListener(activeSessionsChangedListener, ComponentName(this, DynamicNotificationListenerService::class.java))
            Log.i(TAG, "Successfully added session change listener")
        } catch (e: SecurityException) {
            Log.e(TAG, "Failed to add session change listener")
        }
    }

    private fun getActiveMediaController(mediaSessionManager: MediaSessionManager): MediaController? {
        return try {
            val mediaControllers = mediaSessionManager.getActiveSessions(ComponentName(this, NotificationListenerService::class.java))
            mediaControllers.firstOrNull()
        } catch (e: SecurityException) {
            null
        }
    }

    private fun registerActiveMediaControllerCallback(mediaSessionManager: MediaSessionManager) {
        getActiveMediaController(mediaSessionManager)?.let { mediaController ->
            registerCallback(mediaController)

            mediaController.metadata?.let { metadata ->
                mediaControllerCallback.onMetadataChanged(metadata)
            }
        }
    }

    private fun registerCallback(mediaController: MediaController) {

        unregisterCallback(this.mediaController)

        Log.i(TAG, "Registering callback for ${mediaController.packageName}")

        this.mediaController = mediaController
        mediaControllerCallback.let { mediaControllerCallback ->
            mediaController.registerCallback(mediaControllerCallback)
        }

    }

    private fun unregisterCallback(mediaController: MediaController?) {

        Log.i(TAG, "Unregistering callback for ${mediaController?.packageName}")

        mediaControllerCallback.let { mediaControllerCallback ->
            mediaController?.unregisterCallback(mediaControllerCallback)
        }
    }

}