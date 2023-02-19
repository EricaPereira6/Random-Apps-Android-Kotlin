package dam_a42356.universeexplorer

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.content.IntentFilter
import android.util.Log

abstract class TimerBroadcastReceiver (context: Context) : BroadcastReceiver() {

    companion object {
        private val REQUEST_CODE = 101
        private var TIMER_ID = 0
    }
    private val ACTION_TIMER = "dam_a42356.ACTIONTIMER -" + TIMER_ID++
    private val context : Context
    private val alarmManager : AlarmManager
    private val alarmPendingIntent : PendingIntent

    init {
        this.context = context
        alarmManager = context.getSystemService(ALARM_SERVICE) as AlarmManager
        context.registerReceiver(this, IntentFilter(ACTION_TIMER))
        alarmPendingIntent = PendingIntent.getBroadcast(context , REQUEST_CODE , Intent(ACTION_TIMER), 0)
    }

    fun start(delaySecs : Long) {
        Log.d("timer", "fez start(7)")
        val time = System.currentTimeMillis() + delaySecs * 1000
        alarmManager.setExact(AlarmManager.RTC, time, alarmPendingIntent)
    }

    fun cancel() {
        alarmManager.cancel(alarmPendingIntent)
    }

    override fun onReceive(context : Context, intent : Intent) {
        onTick()
    }

    abstract fun onTick()

    fun onDestroy() {
        context.unregisterReceiver(this)
    }
}