package dam_a42356.eventsapp

import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Surface
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {

    private val tag = "event"
    private lateinit var eventsLayout : LinearLayout
    private var saveEvents = ArrayList<String>()

    private var alreadySaved = false

    private lateinit var brScreenOnOff : ScreenReceiver
    private lateinit var brBluetooth : BluetoothReceiver
    private lateinit var bluetoothAdapter : BluetoothAdapter

    private val REQUEST_ENABLE_BT = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState != null){
            onRestoreInstanceState(savedInstanceState)
        }

        brScreenOnOff = ScreenReceiver()
        registerReceiver(brScreenOnOff , brScreenOnOff.getIntentFilter())

        brBluetooth = BluetoothReceiver()
        registerReceiver(brBluetooth , brBluetooth.getIntentFilter())

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        if (bluetoothAdapter == null) {
            val message = "Device do not support BLT"
            val txt = TextView(this)
            eventsLayout = findViewById(R.id.eventsLayout)
            eventsLayout.addView(txt)
            txt.text = message
            val param = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            param.setMargins(50, 5, 50, 5)
            txt.layoutParams = param
            txt.background = ContextCompat.getDrawable(baseContext, R.color.white)
            txt.textSize = 18F
        }
        // start bluetooth adapter
        else if (!bluetoothAdapter.isEnabled) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
        }

        bluetoothAdapter.isDiscovering
        bluetoothAdapter.scanMode


        val sepBtn : TextView = findViewById<Button>(R.id.sepButton)
        sepBtn.setOnClickListener{
            log(Events.SEPARATOR.event, Events.SEPARATOR)
        }
        val clearBtn : TextView = findViewById<Button>(R.id.clearButton)
        clearBtn.setOnClickListener {
            saveEvents = ArrayList()
            if (savedInstanceState != null) {
                onSaveInstanceState(savedInstanceState)
            }
            eventsLayout = findViewById(R.id.eventsLayout)
            eventsLayout.removeAllViews()
        }
        val discBtn : TextView = findViewById<Button>(R.id.bltDiscButton)
        discBtn.setOnClickListener {

            bluetoothAdapter.startDiscovery()

            discBtn.isEnabled = !discBtn.isEnabled
            discBtn.setTextColor(ContextCompat.getColor(baseContext, Events.SCREENOFF.color))
        }
        val annBtn : TextView = findViewById<Button>(R.id.bltAnnButton)
        annBtn.setOnClickListener {

            val discIntent = Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE)
            discIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 60)
            startActivity(discIntent)

            annBtn.isEnabled = !annBtn.isEnabled
            annBtn.setTextColor(ContextCompat.getColor(baseContext, Events.CREATE.color))
        }


        val display = (baseContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
        val rotation = display.rotation
        val message = if (rotation == Surface.ROTATION_90 || rotation == Surface.ROTATION_270){
            Events.CREATE.event + " - landscape"
        } else {
            Events.CREATE.event + " - Portrait"
        }

        log(message, Events.CREATE)
    }

    override fun onDestroy () {
        super.onDestroy()

        unregisterReceiver(brScreenOnOff)
        unregisterReceiver(brBluetooth)

        log(Events.DESTROY.event, Events.DESTROY)
    }
    override fun onStart () {
        super.onStart()
        log(Events.START.event, Events.START)
    }
    override fun onStop () {
        super.onStop()
        log(Events.STOP.event, Events.STOP)
    }
    override fun onRestart () {
        super.onRestart()
        log(Events.RESTART.event, Events.RESTART)
    }
    override fun onResume () {
        super.onResume()
        log(Events.RESUME.event, Events.RESUME)
        log(Events.SEPARATOR.event, Events.SEPARATOR)
    }
    override fun onPause () {
        super.onPause()
        log(Events.PAUSE.event, Events.PAUSE)
    }
    override fun onSaveInstanceState (outState : Bundle) {
        super.onSaveInstanceState(outState)

        eventsLayout = findViewById(R.id.eventsLayout)

        for (i in 0 until eventsLayout.childCount){
            val txtView : TextView = eventsLayout.getChildAt(i) as TextView
            val txtList = txtView.text.split(" - ")
            when (txtList.size) {
                2 -> saveEvents.add(txtList[0] + "," + txtList[1])
                3 -> saveEvents.add(txtList[0] + " - " + txtList[1] + "," + txtList[2])
                else -> saveEvents.add(Events.SEPARATOR.event + "," + txtList[txtList.size - 1])
            }
        }
        outState.putStringArrayList("events", saveEvents)

        val disc : TextView = findViewById<Button>(R.id.bltDiscButton)
        val ann : TextView = findViewById<Button>(R.id.bltAnnButton)
        outState.putBoolean("disc", disc.isEnabled)
        outState.putBoolean("ann", ann.isEnabled)

        saveEvents = ArrayList()

        log(Events.SAVEINSTANCESTATE.event, Events.SAVEINSTANCESTATE)
    }
    override fun onRestoreInstanceState (outState : Bundle) {

        if(!alreadySaved) {

            val saved = outState.getStringArrayList("events")
            if (saved != null) {
                for (i in saved.indices) {
                    val name = saved[i].split(",", " - ")[0]
                    log(saved[i], Events.CREATE.getValue(name))
                }
            }
            saveEvents = ArrayList()
            alreadySaved = true
        }

        val discBool = outState.getBoolean("disc")
        val annBool = outState.getBoolean("ann")
        if (!discBool) {
            val disc : TextView = findViewById<Button>(R.id.bltDiscButton)
            disc.setTextColor(ContextCompat.getColor(baseContext, Events.SCREENOFF.color))
            disc.isEnabled = discBool
        }
        if (!annBool){
            val ann : TextView = findViewById<Button>(R.id.bltAnnButton)
            ann.setTextColor(ContextCompat.getColor(baseContext, Events.CREATE.color))
            ann.isEnabled = annBool
        }

        log(Events.RESTOREINSTANCESTATE.event, Events.RESTOREINSTANCESTATE)
    }

    private fun log(message : String, event : Events) {

        val eventMessage = message.split(",")

        val time : String = if (eventMessage.size == 1){
            val calendar : Calendar = Calendar.getInstance()
            val sdf = SimpleDateFormat("HH:mm:ss.SSS", Locale.UK)
            sdf.format(calendar.time)
        } else {
            eventMessage[1]
        }

        //Log.d(tag, eventMessage[0])
        //val saveMsg = eventMessage[0] + "," + time
        //saveEvents.add(saveMsg)


        val txt = TextView(this)

        eventsLayout = findViewById(R.id.eventsLayout)
        eventsLayout.addView(txt)

        val msg = eventMessage[0] + " - " + time
        txt.text = msg

        val param = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        param.setMargins(50, 5, 50, 5)

        txt.layoutParams = param
        txt.background = ContextCompat.getDrawable(baseContext, event.color)
        txt.textSize = 18F


        val scroll : ScrollView = findViewById(R.id.scrollEvents)
        scroll.post {
            scroll.scrollY = txt.y.toInt()
        }
    }
}
