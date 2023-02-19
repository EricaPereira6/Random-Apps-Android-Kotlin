package dam_a42356.universeexplorer

import android.content.Intent
import android.graphics.Typeface
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {

    private var planets : ArrayList<Planet> = ArrayList()

    private lateinit var planetsLayout : LinearLayout

    private val resultPlanetActivityRequestCode = 1
    private val resultSelectActivityRequestCode = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        planetsLayout = findViewById(R.id.planetsLayout)

        for (i in 0..7) {
            val p1 = Planet.getPlanetFromResources(i, resources)
            if (p1.getNumColonies() > 0) {
                planets.add(p1)
            }
        }
        for (i in 0 until planets.size){
            addPlanetToUI(i, planets[i])
        }
    }

    private fun addPlanetToUI (i : Int, planet : Planet) {

        val hLayout = LinearLayout(this)

        planetsLayout.addView(hLayout , i)

        hLayout.id = planet.getIndex()
        hLayout.orientation = LinearLayout.HORIZONTAL
        hLayout.gravity = Gravity.CENTER
        hLayout.setBackgroundColor(ContextCompat.getColor(baseContext, R.color.layout_bg))

        val param = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        param.setMargins(0, 0, 0, 20)
        hLayout.layoutParams = param

        val imgPlanet = ImageView(this)
        hLayout.addView(imgPlanet)

        imgPlanet.setImageResource(planet.getImgSmallResourceValues())

        val imgParam = LinearLayout.LayoutParams(
            200,
            200
        )
        imgParam.setMargins(25, 25, 25, 25)
        imgPlanet.layoutParams = imgParam

        val txtPlanet = TextView(this)
        hLayout.addView(txtPlanet)

        val txtParam = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        txtParam.setMargins(25, 25, 25, 25)
        txtPlanet.layoutParams = txtParam

        txtPlanet.text = planet.getName()
        txtPlanet.setTextColor(ContextCompat.getColor(baseContext, R.color.text_color))
        txtPlanet.setBackgroundColor(ContextCompat.getColor(baseContext, R.color.layout_text_bg))
        txtPlanet.textSize = 24F
        txtPlanet.gravity = Gravity.CENTER
        txtPlanet.setTypeface(null, Typeface.BOLD)

        hLayout.setOnClickListener {

            val intent = Intent(this@MainActivity, PlanetActivity::class.java)
            intent.putExtra("planetIndex", planet.getIndex())
            intent.putExtra("planetNColonies", planet.getNumColonies())
            intent.putExtra("planetNBases", planet.getNumMilitaryBases())
            intent.putExtra("planetField", planet.getForceField())

            startActivityForResult(intent, resultPlanetActivityRequestCode)

            // Toast.makeText(this, "Hello toast!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateOptionsMenu(menu : Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item : MenuItem) : Boolean {
        return when(item.itemId){
            R.id.menuColonize -> {

                val intent = Intent(this@MainActivity, SelectPlanetActivity::class.java)

                val activePlanets : ArrayList<Int> = ArrayList()
                for (planet in planets) {
                    activePlanets.add(planet.getIndex())
                }

                intent.putIntegerArrayListExtra("indexes", activePlanets)

                startActivityForResult(intent, resultSelectActivityRequestCode)

                //Toast.makeText(this, "Hello again!", Toast.LENGTH_SHORT).show()
                true
            }
            else ->
                super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == resultPlanetActivityRequestCode) {

            if (resultCode == RESULT_OK) {
                val extras = data?.extras!!

                val index = extras.get("index") as Int
                val nColonies = extras.get("nColonies") as Int
                val nMilitary = extras.get("nMilitary") as Int
                val forceField = extras.get("forceField") as Boolean

                for (planet in planets){
                    if (index == planet.getIndex()){
                        planet.setNumColonies(nColonies)
                        planet.setNumMilitaryBases(nMilitary)
                        planet.setForceField(forceField)

                        if (planet.getNumColonies() == 0) {
                            val layout : LinearLayout = findViewById(planet.getIndex())
                            planetsLayout.removeView(layout)
                            planets.remove(planet)
                        }
                        break
                    }
                }
            }
            if (resultCode == RESULT_CANCELED) {
                Log.d("Cancelled!!!!", "THE RESULT WAS CANCELLED")
            }
        }
        else if (requestCode == resultSelectActivityRequestCode) {

            if (resultCode == RESULT_OK) {
                val extras = data?.extras!!
                val index = extras.get("index") as Int

                val planet = Planet.getPlanetFromResources(index, resources)
                planet.setNumColonies(1)

                val size = planetsLayout.childCount
                if (size > 0) {
                    for (i in 0 until size) {
                        if (planet.getIndex() < planets[i].getIndex()) {

                            planets.add(i, planet)
                            addPlanetToUI(i, planets[i])
                            break
                        }
                        if (i == size - 1) {

                            planets.add(planet)
                            addPlanetToUI(size, planets[planets.size - 1])
                        }
                    }
                }
                else {
                    planets.add(planet)
                    addPlanetToUI(0, planets[0])
                }
                createPlanetSound()
            }
            if (resultCode == RESULT_CANCELED) {
                Log.d("Cancelled!!!!", "THE RESULT WAS CANCELLED")
            }
        }
    }

    private fun createPlanetSound () {
        // short audio resource file
        val mediaPlayer = MediaPlayer.create(baseContext, R.raw.planetcreated)
        mediaPlayer.start()

    }
}
