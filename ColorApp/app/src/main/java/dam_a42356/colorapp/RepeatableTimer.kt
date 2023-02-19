package dam_a42356.colorapp

import android.os.Handler
import android.os.Message
import android.os.SystemClock
import kotlin.properties.Delegates


abstract class RepeatableTimer (initialDelay : Long, repeatableDelay : Long) {

    private var initDelay : Long = initialDelay
    private var delay : Long = repeatableDelay

    private val message = 1
    private var cancelled = false
    private var timeout by Delegates.notNull<Long>()

    private val handler : Handler = object : Handler() {

        override fun handleMessage(msg: Message) {

            if (cancelled) {
                return
            }

            val timeRemaining: Long = timeout - SystemClock.elapsedRealtime()

            if (timeRemaining <= delay) {

                cancel()

            } else {

                val timeI = SystemClock.elapsedRealtime()

                onTick()

                val timeF = SystemClock.elapsedRealtime()
                
                val duration = timeF - timeI

                var delayed : Long = delay - duration

                if (delayed < 0) {
                    delayed = 0

                    cancel()
                }
                sendEmptyMessageDelayed(message, delayed)
            }
        }
    }

    fun start() : RepeatableTimer {
        cancelled = false

        if (initDelay <= 0) {
            cancel()
            return this
        }
        timeout = SystemClock.elapsedRealtime() + initDelay

        handler.sendMessage(handler.obtainMessage(message))

        return this
    }

    fun cancel(){
        cancelled = true
        handler.removeMessages(message)
    }

    abstract fun onTick()
}