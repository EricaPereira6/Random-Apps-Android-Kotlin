package dam_a42356.universeexplorer

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import kotlin.collections.HashMap
import kotlin.properties.Delegates

class AttackPlanetActivity : AppCompatActivity() {

    private var index by Delegates.notNull<Int>()

    private lateinit var bombBtn : ImageButton
    private lateinit var invadeBtn : ImageButton
    private lateinit var infectBtn : ImageButton
    private lateinit var laserBtn : ImageButton
    private lateinit var exitBtn : ImageButton

    private var bombPressed = false
    private var invadePressed = false
    private var infectPressed = false
    private var laserPressed = false

    private lateinit var infectAnimation : AnimationDrawable
    private lateinit var rotateBomb : Animation
    private lateinit var shakeLaser : Animation

    private lateinit var timer : TimerBroadcastReceiver

    private enum class SoundType {
        EXPLOSION,
        INVADE,
        VIRUS,
        LASER
    }

    private val soundPoolMap = HashMap<SoundType, Int>()
    private lateinit var soundPoolFX : SoundPool


    private var musicOn = false

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_attack_planet)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        title = "Attack Planet"

        val extras = intent.extras
        if (extras != null) {
            index = extras.getInt("planetIndex")
        }
        val planet = Planet.getPlanetFromResources(index, resources)

        val planetName : TextView = findViewById(R.id.attackPlanetName)
        planetName.text = planet.getName()

        val planetImg : ImageView = findViewById(R.id.attackPlanetImage)
        planetImg.setImageResource(planet.getImgSmallResourceValues())

        bombBtn = findViewById(R.id.bombButton)
        bombBtn.setOnClickListener {
            onClick(bombBtn)
        }
        invadeBtn = findViewById(R.id.invadeButton)
        invadeBtn.setOnClickListener {
            onClick(invadeBtn)
        }
        infectBtn = findViewById(R.id.infectButton)
        infectBtn.setOnClickListener {
            onClick(infectBtn)
        }
        laserBtn = findViewById(R.id.laserButton)
        laserBtn.setOnClickListener {
            onClick(laserBtn)
        }
        exitBtn = findViewById(R.id.exitButton)
        exitBtn.setOnClickListener {
            onClick(exitBtn)
        }

        infectBtn.setBackgroundResource(R.drawable.infect_anim)
        infectAnimation = infectBtn.background as AnimationDrawable

        rotateBomb = AnimationUtils.loadAnimation(this,  R.anim.bomb_anim)

        shakeLaser = AnimationUtils.loadAnimation(this, R.anim.laser_anim)

        timer = object: TimerBroadcastReceiver(baseContext){
            override fun onTick(){
                shakeLaser.cancel()
            }
        }

        // SoundPool.Builder() requires Android Lollipop
        val audioAttrs = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)

        val soundPoolBuilder = SoundPool.Builder()
        soundPoolFX = soundPoolBuilder
            .setAudioAttributes(audioAttrs.build())
            .setMaxStreams(4)
            .build()

        // Load sounds into SoundPool and save them in one array
        //soundPoolMap = SparseIntArray(4)
        soundPoolMap[SoundType.EXPLOSION] = soundPoolFX.load(this, R.raw.explosion, 1)
        soundPoolMap[SoundType.INVADE] = soundPoolFX.load(this, R.raw.transport, 1)
        soundPoolMap[SoundType.VIRUS] = soundPoolFX.load(this, R.raw.virus, 1)
        soundPoolMap[SoundType.LASER] = soundPoolFX.load(this, R.raw.laser, 1)

        val musicBtn : TextView = findViewById<Button>(R.id.music)
        musicBtn.setOnClickListener {
            if(musicOn) {
                musicBtn.text = resources.getString(R.string.music_on_string)
                stopService(Intent(this, MusicService::class.java))
            }
            else {
                musicBtn.text = resources.getString(R.string.music_off_string)
                startService(Intent(this, MusicService::class.java))
            }
            musicOn = !musicOn
        }
    }

    override fun onTouchEvent(event : MotionEvent) : Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {

            if (!infectAnimation.isRunning) {
                Log.d("Animation" , "Starting animation ..... Running: " + infectAnimation.isRunning)
                infectAnimation.start()
                bombBtn.startAnimation(rotateBomb)
                laserBtn.startAnimation(shakeLaser)
                timer.start(7)
            }
            else {
                Log.d("Animation" , "Stopping animation ..... Running: " + infectAnimation.isRunning)
                infectAnimation.stop()
                rotateBomb.cancel()
                //timer.cancel()
            }
            return true
        }
        return super.onTouchEvent(event)
    }

    private fun playSample(sound : SoundType) {
        val manageAudio : AudioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        val curSampleVolume : Float = manageAudio.getStreamVolume(AudioManager.STREAM_MUSIC).toFloat()
        val maxSampleVolume : Float = manageAudio.getStreamMaxVolume(AudioManager.STREAM_MUSIC).toFloat()
        val setSampleVolume : Float = curSampleVolume / maxSampleVolume
        val soundID : Int = soundPoolMap[sound]!!
        soundPoolFX.play(soundID,
            setSampleVolume, setSampleVolume, 0, 0, 1.0f)
    }

    private fun onClick(v : View){

        when (v.id){
            R.id.bombButton -> {
                if (!bombPressed && !invadePressed && !infectPressed && !laserPressed){
                    bombPressed = true
                    v.isActivated = true
                    enableButtons (v, false)
                    playSample(SoundType.EXPLOSION)

                }
                else if (bombPressed) {
                    v.isActivated = false
                    bombPressed = false
                    enableButtons (v, true)
                }
            }
            R.id.invadeButton -> {
                if (!bombPressed && !invadePressed && !infectPressed && !laserPressed){
                    invadePressed = true
                    v.isActivated = true
                    enableButtons (v, false)
                    playSample(SoundType.INVADE)
                }
                else if (invadePressed) {
                    v.isActivated = false
                    invadePressed = false
                    enableButtons (v, true)
                }
            }
            R.id.infectButton -> {
                if (!bombPressed && !invadePressed && !infectPressed && !laserPressed){
                    infectPressed = true
                    v.isActivated = true
                    enableButtons (v, false)
                    playSample(SoundType.VIRUS)
                }
                else if (infectPressed) {
                    v.isActivated = false
                    infectPressed = false
                    enableButtons (v, true)
                }
            }
            R.id.laserButton -> {
                if (!bombPressed && !invadePressed && !infectPressed && !laserPressed){
                    laserPressed = true
                    v.isActivated = true
                    enableButtons (v, false)
                    playSample(SoundType.LASER)
                }
                else if (laserPressed) {
                    v.isActivated = false
                    laserPressed = false
                    enableButtons (v, true)
                }
            }
            R.id.exitButton -> {
                if (!bombPressed && !invadePressed && !infectPressed && !laserPressed) {
                    endActivity()
                }
            }
        }
    }

    private fun enableButtons (v : View, enable : Boolean) {
        when (v){
            bombBtn -> {
                invadeBtn.isEnabled = enable
                infectBtn.isEnabled = enable
                laserBtn.isEnabled = enable
            }
            invadeBtn -> {
                bombBtn.isEnabled = enable
                infectBtn.isEnabled = enable
                laserBtn.isEnabled = enable
            }
            infectBtn -> {
                bombBtn.isEnabled = enable
                invadeBtn.isEnabled = enable
                laserBtn.isEnabled = enable
            }
            laserBtn -> {
                bombBtn.isEnabled = enable
                invadeBtn.isEnabled = enable
                infectBtn.isEnabled = enable
            }
        }
    }

    override fun onBackPressed() {
        if (!bombPressed && !invadePressed && !infectPressed && !laserPressed) {
            endActivity()
            super.onBackPressed()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                if (!bombPressed && !invadePressed && !infectPressed && !laserPressed) {
                    endActivity()
                }
            }
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }

    private fun endActivity(){
        timer.onDestroy()
        stopService(Intent(this, MusicService::class.java))
        finish()
    }
}
