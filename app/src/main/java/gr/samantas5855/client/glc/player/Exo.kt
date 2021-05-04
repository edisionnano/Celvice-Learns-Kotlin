package gr.samantas5855.client.glc.player

import android.content.pm.ActivityInfo
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import gr.samantas5855.client.glc.R
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.MimeTypes
import com.google.android.exoplayer2.util.Util

const val STATE_RESUME_WINDOW = "resumeWindow"
const val STATE_RESUME_POSITION = "resumePosition"
const val STATE_PLAYER_FULLSCREEN = "playerFullscreen"
const val STATE_PLAYER_PLAYING = "playerOnPlay"

class Exo : AppCompatActivity() {
    private lateinit var exoPlayer: SimpleExoPlayer
    private lateinit var dataSourceFactory: DataSource.Factory
    private lateinit var playerView: PlayerView

    private var currentWindow = 0
    private var playbackPosition: Long = 0
    private var isFullscreen = false
    private var isPlayerPlaying = true

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        playerView = findViewById(R.id.player_view)
        dataSourceFactory = DefaultDataSourceFactory(this, Util.getUserAgent(this, "testapp"))
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
                // Set the content to appear under the system bars so that the
                // content doesn't resize when the system bars hide and show.
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                // Hide the nav bar and status bar
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)
        val mealLayout = findViewById<LinearLayout>(R.id.lelos)
        mealLayout.setBackgroundColor(Color.BLACK)


        if (savedInstanceState != null) {
            currentWindow = savedInstanceState.getInt(STATE_RESUME_WINDOW)
            playbackPosition = savedInstanceState.getLong(STATE_RESUME_POSITION)
            isFullscreen = savedInstanceState.getBoolean(STATE_PLAYER_FULLSCREEN)
            isPlayerPlaying = savedInstanceState.getBoolean(STATE_PLAYER_PLAYING)
        }
    }

    private fun initPlayer() {
        val stringOne = intent.getStringExtra("pos")
        val mediaItem = MediaItem.Builder()
                .setUri(stringOne)
                .setMimeType(MimeTypes.APPLICATION_M3U8)
                .build()
        exoPlayer = SimpleExoPlayer.Builder(this).build().apply {
            playWhenReady = isPlayerPlaying
            seekTo(currentWindow, playbackPosition)
            setMediaItem(mediaItem, false)
            prepare()
        }
        playerView.player = exoPlayer
    }

    private fun releasePlayer(){
        isPlayerPlaying = exoPlayer.playWhenReady
        playbackPosition = exoPlayer.currentPosition
        currentWindow = exoPlayer.currentWindowIndex
        exoPlayer.release()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(STATE_RESUME_WINDOW, exoPlayer.currentWindowIndex)
        outState.putLong(STATE_RESUME_POSITION, exoPlayer.currentPosition)
        outState.putBoolean(STATE_PLAYER_FULLSCREEN, isFullscreen)
        outState.putBoolean(STATE_PLAYER_PLAYING, isPlayerPlaying)
        super.onSaveInstanceState(outState)
    }

    override fun onStart() {
        super.onStart()
        if (Util.SDK_INT > 23) {
            initPlayer()
            playerView.onResume()
        }
    }

    override fun onResume() {
        super.onResume()
        if (Util.SDK_INT <= 23) {
            initPlayer()
            playerView.onResume()
        }
    }

    override fun onPause() {
        super.onPause()
        if (Util.SDK_INT <= 23) {
            playerView.onPause()
            releasePlayer()
        }
    }

    override fun onStop() {
        super.onStop()
        if (Util.SDK_INT > 23) {
            playerView.onPause()
            releasePlayer()
        }
    }
}