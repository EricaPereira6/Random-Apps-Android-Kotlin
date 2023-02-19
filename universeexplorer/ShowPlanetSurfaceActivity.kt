package dam_a42356.universeexplorer

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.MediaController
import android.widget.TextView
import android.widget.VideoView

class ShowPlanetSurfaceActivity : AppCompatActivity() {

    private lateinit var name : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_planet_surface)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        title = "Video Surface"

        val extras = intent.extras
        if (extras != null) {
            name = extras.getString("planetName").toString()
        }

        val nameView : TextView = findViewById(R.id.namePlanet)
        nameView.text = name

        // set video VideoView
        val vvPlanetSurface : VideoView = findViewById(R.id.videoPlayer)

        var path = "android.resource://$packageName/"
        path += when (name){
            "Mars" -> R.raw.flyovermars
            "Earth" -> R.raw.flyoverearth
            else -> R.raw.flyover
        }

        vvPlanetSurface.setVideoPath(path)

        // set mediaController
        val videoController = MediaController(this)
        vvPlanetSurface.setMediaController(videoController)

        // set videoPlayer in looping mode
        vvPlanetSurface.setOnPreparedListener { mp ->
            mp.isLooping = true
        }

        val button : TextView = findViewById<Button>(R.id.playButton)
        button.setOnClickListener {

            val isPlaying = vvPlanetSurface.isPlaying

            button.text = if (isPlaying) {
                vvPlanetSurface.pause()
                resources.getString(R.string.play_button)
            } else{
                vvPlanetSurface.start()
                resources.getString(R.string.pause_button)
            }
        }


    }

    override fun onBackPressed() {
        finish()
        super.onBackPressed()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }
}
