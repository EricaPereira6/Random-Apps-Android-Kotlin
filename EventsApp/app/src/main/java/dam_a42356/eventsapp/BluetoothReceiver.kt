package dam_a42356.eventsapp

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.core.content.ContextCompat
import java.text.SimpleDateFormat
import java.util.*

class BluetoothReceiver : BroadcastReceiver() {

    private val intentFilter = IntentFilter()

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            BluetoothAdapter.ACTION_SCAN_MODE_CHANGED -> {
                var str = ""
                when(intent.getIntExtra(BluetoothAdapter.EXTRA_SCAN_MODE, BluetoothAdapter.ERROR)) {

                    BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE ->
                       str = Events.ACTION_SCAN_MODE_CHANGED.event + " - SCAN_MODE_CONNECTABLE_DISCOVERABLE"

                    BluetoothAdapter.SCAN_MODE_CONNECTABLE ->
                        str = Events.ACTION_SCAN_MODE_CHANGED.event + " - SCAN_MODE_CONNECTABLE"

                    BluetoothAdapter.SCAN_MODE_NONE ->
                        str = Events.ACTION_SCAN_MODE_CHANGED.event + " - SCAN_MODE_NONE"

                }
                log(context, str, Events.ACTION_SCAN_MODE_CHANGED)
            }
            BluetoothDevice.ACTION_FOUND -> {
                val dvc: BluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)!!
                val deviceName = dvc.name

                val str : String = Events.ACTION_FOUND.event + ": " + deviceName

                log(context, str, Events.ACTION_FOUND)
            }

            BluetoothAdapter.ACTION_DISCOVERY_STARTED -> {
                log(context, Events.ACTION_DISCOVERY_STARTED.event, Events.ACTION_DISCOVERY_STARTED)
            }
            BluetoothAdapter.ACTION_DISCOVERY_FINISHED -> {
                log(context, Events.ACTION_DISCOVERY_FINISHED.event, Events.ACTION_DISCOVERY_FINISHED)
                val main : Activity = context as Activity
                val disc : TextView = main.findViewById<Button>(R.id.bltDiscButton)
                disc.isEnabled = !disc.isEnabled
                disc.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary))
                val ann : TextView = main.findViewById<Button>(R.id.bltAnnButton)
                ann.isEnabled = !ann.isEnabled
                ann.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary))
            }
            BluetoothAdapter.ACTION_STATE_CHANGED -> {
                var str = ""
                when(intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR)) {

                    BluetoothAdapter.STATE_TURNING_ON ->
                        str = Events.ACTION_STATE_CHANGED.event + " - STATE_TURNING_ON"

                    BluetoothAdapter.STATE_ON ->
                        str = Events.ACTION_STATE_CHANGED.event + " - STATE_ON"

                    BluetoothAdapter.STATE_TURNING_OFF ->
                        str = Events.ACTION_STATE_CHANGED.event + " - STATE_TURNING_OFF"

                    BluetoothAdapter.STATE_OFF ->
                        str = Events.ACTION_STATE_CHANGED.event + " - STATE_OFF"

                }
                log(context, str, Events.ACTION_STATE_CHANGED)
            }
        }
    }

    // initialize IntentFilter with the kind of events that should be received
    init {
        intentFilter.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED)
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED)
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND)
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED)
    }

    fun getIntentFilter(): IntentFilter {
        return intentFilter
    }

    private fun log(context : Context, message: String, event : Events) {

        val calendar : Calendar = Calendar.getInstance()
        val sdf = SimpleDateFormat("HH:mm:ss.SSS", Locale.UK)
        val time = sdf.format(calendar.time)

        val txt = TextView(context)

        val main : Activity = context as Activity
        val eventsLayout : LinearLayout = main.findViewById(R.id.eventsLayout)
        eventsLayout.addView(txt)

        val msg = "$message - $time"
        txt.text = msg

        val param = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        param.setMargins(50, 5, 50, 5)

        txt.layoutParams = param
        txt.background = ContextCompat.getDrawable(context, event.color)
        txt.textSize = 18F

        val scroll : ScrollView = main.findViewById(R.id.scrollEvents)
        scroll.post {
            scroll.scrollY = txt.y.toInt()
        }

    }
}