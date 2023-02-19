package dam_a42356.eventsapp

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.core.content.ContextCompat
import java.text.SimpleDateFormat
import java.util.*

internal class ScreenReceiver : BroadcastReceiver() {
    // return the IntentFilter with the kind of events that should be received
    private val intentFilter = IntentFilter()

    // method where the system deliver events to the app
    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        if (Intent.ACTION_SCREEN_OFF == action) { // Screen off actions ...
            log(context, Events.SCREENOFF)
        } else if (Intent.ACTION_SCREEN_ON == action) { // Screen on actions ...
            log(context, Events.SCREENON)
        }
    }

    // initialize IntentFilter with the kind of events that should be received
    init {
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF)
        intentFilter.addAction(Intent.ACTION_SCREEN_ON)
    }

    fun getIntentFilter(): IntentFilter {
        return intentFilter
    }

    private fun log(context : Context, event : Events) {

        val calendar : Calendar = Calendar.getInstance()
        val sdf = SimpleDateFormat("HH:mm:ss.SSS", Locale.UK)
        val time = sdf.format(calendar.time)

        val txt = TextView(context)

        val main : Activity = context as Activity
        val eventsLayout : LinearLayout = main.findViewById(R.id.eventsLayout)
        eventsLayout.addView(txt)

        val msg = event.event + " - " + time
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