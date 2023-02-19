package dam_a42356.universeexplorer

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlin.properties.Delegates


class PlanetActivity : AppCompatActivity() {

    private var dataSaved = true

    private var index by Delegates.notNull<Int>()
    private var sendColonies by Delegates.notNull<Int>()
    private var sendMilitary by Delegates.notNull<Int>()
    private var sendForceField by Delegates.notNull<Boolean>()

    private lateinit var planet : Planet

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_planet)

        // an AppCompatActivity should have an action bar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val name2 : TextView = findViewById(R.id.planetName2)
        title = name2.text.toString()

        val extras = intent.extras
        if (extras != null) {
            index = extras.getInt("planetIndex")
            sendColonies = extras.getInt("planetNColonies")
            sendMilitary = extras.getInt("planetNBases")
            sendForceField = extras.getBoolean("planetField")
        }

        planet = Planet.getPlanetFromResources(index, resources)
        planet.setNumColonies(sendColonies)
        planet.setNumMilitaryBases(sendMilitary)
        planet.setForceField(sendForceField)

        setScreenPlanetInfo(planet)

        val img : ImageView = findViewById(R.id.planetImage)
        img.setOnClickListener{

            val intent = Intent(this@PlanetActivity, ImagePlanetActivity::class.java)
            intent.putExtra("planetIndex", index)
            startActivity(intent)

            //Toast.makeText(this, "I'm alive! Yours, Toast", Toast.LENGTH_SHORT).show()
        }

    }

    @SuppressLint("SetTextI18n")
    private fun changeMode() {

        val colonies: TextView = findViewById(R.id.coloniesValue)
        val military: TextView = findViewById(R.id.militaryValue)
        val forceField: TextView = findViewById(R.id.forceFieldValue)
        val disquete: TextView = findViewById<Button>(R.id.save)

        colonies.setBackgroundColor(ContextCompat.getColor(baseContext, R.color.edit_bg))
        military.setBackgroundColor(ContextCompat.getColor(baseContext, R.color.edit_bg))
        forceField.setBackgroundColor(ContextCompat.getColor(baseContext, R.color.edit_bg))

        forceField.setOnClickListener {
            val value = forceField.text
            if (value == "false") {
                forceField.text = "true"
            } else {
                forceField.text = "false"
            }
        }

        colonies.isEnabled = true
        military.isEnabled = true
        disquete.visibility = View.VISIBLE

        dataSaved = false

        disquete.setOnClickListener {
            sendColonies = colonies.text.toString().toInt()
            sendMilitary = military.text.toString().toInt()
            sendForceField = forceField.text.toString().toBoolean()

            dataSaved = true

            colonies.setBackgroundColor(ContextCompat.getColor(baseContext, R.color.text_bg))
            military.setBackgroundColor(ContextCompat.getColor(baseContext, R.color.text_bg))
            forceField.setBackgroundColor(ContextCompat.getColor(baseContext, R.color.text_bg))

            colonies.isEnabled = false
            military.isEnabled = false
            disquete.visibility = View.INVISIBLE

        }
    }

    private fun setScreenPlanetInfo(planet : Planet) {

        val name : TextView = findViewById(R.id.planetName2)
        name.text = planet.getName()

        val img : ImageView = findViewById(R.id.planetImage)
        img.setImageResource(planet.getImgResourceValues()[0])

        val mass : TextView = findViewById(R.id.massValue)
        mass.text = planet.getMass().toString()

        val gravity : TextView = findViewById(R.id.gravityValue)
        gravity.text = planet.getGravity().toString()

        val nColonies : TextView = findViewById(R.id.coloniesValue)
        nColonies.text = planet.getNumColonies().toString()
        sendColonies = planet.getNumColonies()

        val nMilitaryBases : TextView = findViewById(R.id.militaryValue)
        nMilitaryBases.text = planet.getNumMilitaryBases().toString()
        sendMilitary = planet.getNumMilitaryBases()

        val forceField : TextView = findViewById(R.id.forceFieldValue)
        forceField.text = planet.getForceField().toString()
        sendForceField = planet.getForceField()

    }

    override fun onBackPressed() {
        if (!dataSaved) {
            Toast.makeText(this, "You're in Edit Mode. Save your changes", Toast.LENGTH_SHORT).show()
        } else {
            sendExtras()
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu : Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.planet_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
       when (item.itemId) {
           R.id.menuEdit -> {
               item.isEnabled = false
               changeMode()
           }
           R.id.menuShowSurface -> {
               val intent = Intent(this@PlanetActivity, ShowPlanetSurfaceActivity::class.java)
               intent.putExtra("planetName", planet.getName())
               startActivity(intent)
           }
           R.id.menuAttack -> {
               val intent = Intent(this@PlanetActivity, AttackPlanetActivity::class.java)
               intent.putExtra("planetIndex", planet.getIndex())
               startActivity(intent)
           }
           android.R.id.home -> {
               if (!dataSaved) {
                   Toast.makeText(this, "You're in Edit Mode. Save your changes", Toast.LENGTH_SHORT).show()
               } else {
                   sendExtras()
               }
           }
           else -> return super.onOptionsItemSelected(item)
       }
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {

        if (dataSaved) {
            val edit = menu?.findItem(R.id.menuEdit)
            if (edit != null) {
                edit.isEnabled = true
            } else {
                Toast.makeText(this, "It was null ...", Toast.LENGTH_SHORT).show()
            }

        }
        return true
    }

    private fun sendExtras(){

        val intent = Intent(this@PlanetActivity, MainActivity::class.java)

        intent.putExtra("index", index)
        intent.putExtra("nColonies", sendColonies)
        intent.putExtra("nMilitary", sendMilitary)
        intent.putExtra("forceField", sendForceField)

        setResult(RESULT_OK, intent) // or setResult(RESULT_CANCELED)
        finish()
    }
}
