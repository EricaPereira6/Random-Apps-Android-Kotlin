package dam_a42356.colorapp

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.marginBottom
import androidx.core.view.marginTop


class MainActivity : AppCompatActivity() {

    private val redD = R.id.decreaseRed
    private val redI = R.id.increaseRed
    private val greenD = R.id.decreaseGreen
    private val greenI = R.id.increaseGreen
    private val blueD = R.id.decreaseBlue
    private val blueI = R.id.increaseBlue

    private val red = R.id.redColor
    private val green = R.id.greenColor
    private val blue = R.id.blueColor
    private val layout = R.id.finalLayout
    private var time = System.currentTimeMillis()

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initialize()
        if (savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState)
        }

        val display = (baseContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
        val rotation = display.rotation
        if (rotation == Surface.ROTATION_90 || rotation == Surface.ROTATION_270){
            landscapeAdjust()
        }


        val redBtnD : TextView = findViewById<Button>(redD)
        redBtnD.setOnClickListener {
            onClick(redBtnD)
            if (savedInstanceState != null)
                onSaveInstanceState(savedInstanceState)
        }

        redBtnD.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_MOVE ->{
                    if (System.currentTimeMillis() - time > 300) {
                        v.performClick()
                        time = System.currentTimeMillis()
                    }
                }
            }

            v.onTouchEvent(event)
        }


        val redBtnI : TextView = findViewById<Button>(redI)
        redBtnI.setOnClickListener {
            onClick(redBtnI)
            if (savedInstanceState != null)
                onSaveInstanceState(savedInstanceState)
        }

        redBtnI.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_MOVE ->{
                    if (System.currentTimeMillis() - time > 300) {
                        v.performClick()
                        time = System.currentTimeMillis()
                    }
                }
            }

            v.onTouchEvent(event)
        }

        val greenBtnD : TextView = findViewById<Button>(greenD)
        greenBtnD.setOnClickListener {
            onClick(greenBtnD)
            if (savedInstanceState != null)
                onSaveInstanceState(savedInstanceState)
        }

        val timerD = object: CountDownTimer(10000, 300) {
            override fun onTick(millisUntilFinished: Long) {
                greenBtnD.performClick()
            }
            override fun onFinish() {}
        }

        greenBtnD.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> timerD.start()
                MotionEvent.ACTION_UP -> timerD.cancel()
            }

            v.onTouchEvent(event)
        }

        val greenBtnI : TextView = findViewById<Button>(greenI)
        greenBtnI.setOnClickListener {
            onClick(greenBtnI)
            if (savedInstanceState != null)
                onSaveInstanceState(savedInstanceState)
        }

        val timerI = object: CountDownTimer(10000, 300) {
            override fun onTick(millisUntilFinished: Long) {
                greenBtnI.performClick()
            }
            override fun onFinish() {}
        }

        greenBtnI.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> timerI.start()
                MotionEvent.ACTION_UP -> timerI.cancel()
            }

            v.onTouchEvent(event)
        }

        val blueBtnD : TextView = findViewById<Button>(blueD)
        blueBtnD.setOnClickListener {
            onClick(blueBtnD)
            if (savedInstanceState != null)
                onSaveInstanceState(savedInstanceState)
        }

        val repeatableTimerD = object: RepeatableTimer(10000, 300) {
            override fun onTick() {
                blueBtnD.performClick()
            }
        }

        blueBtnD.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> repeatableTimerD.start()
                MotionEvent.ACTION_UP -> repeatableTimerD.cancel()
            }

            v.onTouchEvent(event)
        }

        val blueBtnI : TextView = findViewById<Button>(blueI)
        blueBtnI.setOnClickListener {
            onClick(blueBtnI)
            if (savedInstanceState != null)
                onSaveInstanceState(savedInstanceState)
        }

        val repeatableTimerI = object: RepeatableTimer(10000, 300) {
            override fun onTick() {
                blueBtnI.performClick()
            }
        }

        blueBtnI.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN ->repeatableTimerI.start()
                MotionEvent.ACTION_UP ->{
                    repeatableTimerI.cancel()
                    Log.d("CANCELA!!!!", "UP!!!!!! ")
                }
            }

            v.onTouchEvent(event)
        }
    }

    private fun onClick(v : View) {

        val id = v.id
        var btn : Button = findViewById(red)

        when(id){
            greenD, greenI -> btn = findViewById(green)
            blueD, blueI -> btn = findViewById(blue)
        }

        val str = btn.text.toString()
        var value = str.toInt()
        if (value in 1..254) {
            when (id) {
                redD, greenD, blueD -> value -= 1
                redI, greenI, blueI -> value += 1
            }
        }

        val txt = value.toString()
        btn.text = txt

        val hex = "%02x".format(value)

        when(id){
            redD, redI -> btn.setBackgroundColor(Color.parseColor("#" + hex + "0000"))
            greenD, greenI ->btn.setBackgroundColor(Color.parseColor("#00" + hex + "00"))
            blueD, blueI -> btn.setBackgroundColor(Color.parseColor("#0000$hex"))
        }

        val finalColor : TextView = findViewById(R.id.finalColor)
        val txtColor = finalColor.text.toString()
        val read = txtColor.split(", ")
        var color = ""

        when(id){
            redI, redD -> color = txt + ", " + read[1] + ", " + read[2]
            greenI, greenD -> color = read[0] + ", " + txt + ", " + read[2]
            blueI, blueD -> color = read[0] + ", " + read[1] + ", " + txt
        }

        finalColor.text = color
        val fc = makeFinalColor(color)

        val layout : LinearLayout = findViewById(layout)
        layout.setBackgroundColor(Color.parseColor(fc))
    }

    private fun makeFinalColor(txt: String) : String {

        val rgb = txt.split(", ")

        val hexR = "%02x".format(rgb[0].toInt())
        val hexG = "%02x".format(rgb[1].toInt())
        val hexB = "%02x".format(rgb[2].toInt())

        return "#$hexR$hexG$hexB"
    }

    private fun initialize(){
        val bRED : Button = findViewById(red)
        val bGREEN : Button = findViewById(green)
        val bBLUE : Button = findViewById(blue)

        val initialValue = resources.getInteger(R.integer.initial_value)
        val txt = initialValue.toString()
        bRED.text = txt
        bGREEN.text = txt
        bBLUE.text = txt

        val hex = "%02x".format(initialValue)

        bRED.setBackgroundColor(Color.parseColor("#" + hex + "0000"))
        bGREEN.setBackgroundColor(Color.parseColor("#00" + hex + "00"))
        bBLUE.setBackgroundColor(Color.parseColor("#0000$hex"))

        val finalColor : TextView = findViewById(R.id.finalColor)
        val color = "$initialValue, $initialValue, $initialValue"
        finalColor.text = color

        val layout : LinearLayout = findViewById(layout)
        layout.setBackgroundColor(Color.parseColor("#$hex$hex$hex"))
    }

    private fun landscapeAdjust() {

        val layoutRed : LinearLayout = findViewById(R.id.redLayout)

        val redHeight = layoutRed.height
        val redMargin = layoutRed.marginTop + layoutRed.marginBottom
        val heightNeeded : Int = redHeight + redMargin // based on layoutRed.getHeight() and margins size

        val layoutTop : LinearLayout = findViewById(R.id.topLayout)

        if (layoutTop.height < heightNeeded) {
            // move Blue Layout: remove from control layout, add to Main/End layout ...

            val layoutGreen : LinearLayout = findViewById(R.id.greenLayout)
            val layoutBlue : LinearLayout = findViewById(R.id.blueLayout)

            val colorLayout : LinearLayout = findViewById(R.id.colorsLayout)
            colorLayout.removeView(layoutBlue)

            val layoutFinal : LinearLayout = findViewById(R.id.finalLayout)
            layoutFinal.gravity = Gravity.NO_GRAVITY
            colorLayout.gravity = Gravity.NO_GRAVITY

            val finalTxt : TextView = findViewById(R.id.finalColor)
            finalTxt.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
            finalTxt.gravity = Gravity.CENTER

            val param = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                350,
                0.0f
            )
            param.setMargins(5, 5, 5, 10)

            layoutRed.layoutParams = param
            layoutGreen.layoutParams = param
            layoutBlue.layoutParams = param

            layoutFinal.addView(layoutBlue, 0)
        }
    }

    /*
    companion object fun getScreenDensity (context : Context) : Float {
        val dm = DisplayMetrics()
        val windowManager : WindowManager = context.getSystemService(WINDOW_SERVICE) as WindowManager
        Objects.requireNonNull(windowManager).defaultDisplay.getMetrics(dm)
        return dm.density
    }
     */

        override fun onSaveInstanceState (outState : Bundle)
    {
        val bRED : Button = findViewById(red)
        val tr = bRED.text.toString()
        val hr = "%02x".format(tr.toInt())
        val cr = "#" + hr + "0000"

        val bGREEN : Button = findViewById(green)
        val tg = bGREEN.text.toString()
        val hg = "%02x".format(tg.toInt())
        val cg = "#00" + hg + "00"

        val bBLUE : Button = findViewById(blue)
        val tb = bBLUE.text.toString()
        val hb = "%02x".format(tb.toInt())
        val cb = "#0000$hb"

        val color : TextView = findViewById(R.id.finalColor)
        val ft = color.text.toString()
        val fc = makeFinalColor(ft)

        //Log.d("COLOR!!!!!!!!!!!!!!!" , "#" + hr + "0000")

        outState.putString("redText", tr)
        outState.putString("redColor", cr)
        outState.putString("greenText", tg)
        outState.putString("greenColor", cg)
        outState.putString("blueText", tb)
        outState.putString("blueColor", cb)
        outState.putString("finalText", ft)
        outState.putString("finalColor", fc)

        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState (savedInstanceState : Bundle) {

        val bRED : Button = findViewById(red)
        bRED.text = savedInstanceState.getString("redText")
        bRED.setBackgroundColor(Color.parseColor(savedInstanceState.getString("redColor")))

        val bGREEN : Button = findViewById(green)
        bGREEN.text = savedInstanceState.getString("greenText")
        bGREEN.setBackgroundColor(Color.parseColor(savedInstanceState.getString("greenColor")))

        val bBLUE : Button = findViewById(blue)
        bBLUE.text = savedInstanceState.getString("blueText")
        bBLUE.setBackgroundColor(Color.parseColor(savedInstanceState.getString("blueColor")))

        val layout : LinearLayout = findViewById(layout)
        layout.setBackgroundColor(Color.parseColor(savedInstanceState.getString("finalColor")))
        val color : TextView = findViewById(R.id.finalColor)
        color.text = savedInstanceState.getString("finalText")

    }
}
