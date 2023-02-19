package dam_a42356.universeexplorer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.GridView
import kotlin.collections.ArrayList

class SelectPlanetActivity : AppCompatActivity() {

    private lateinit var indexes : ArrayList<Int>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_planet)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val extras = intent.extras
        if (extras != null) {
            indexes = extras.getIntegerArrayList("indexes") as ArrayList<Int>
        }
        val freePlanets : ArrayList<Planet> = getFreePlanets(indexes)

        val gvSelectPlanets : GridView = findViewById(R.id.freePlanetsGrid)

        gvSelectPlanets.adapter = GridViewAdapter(this, freePlanets)

        gvSelectPlanets.onItemClickListener = AdapterView.OnItemClickListener {
                _: AdapterView<*>, _: View, _: Int, _: Long ->
            onClickGridViewPlanetsItem(gvSelectPlanets)
        }
    }

    private fun getFreePlanets(activePlanets : ArrayList<Int>) : ArrayList<Planet> {

        val planets : ArrayList<Planet> = ArrayList()

        for (i in 0..7) {
            if (activePlanets.size > 0) {
                for (j in 0 until activePlanets.size) {
                    if (i == activePlanets[j]) {
                        break
                    }
                    if (j == activePlanets.size - 1) {
                        planets.add(Planet.getPlanetFromResources(i, resources))
                    }
                }
            } else {
                planets.add(Planet.getPlanetFromResources(i, resources))
            }
        }
        return planets
    }

    fun onClickGridViewPlanetsItem(v : View) {
        val tag = v.tag as Planet

        val index = tag.getIndex()

        val intent = Intent(this@SelectPlanetActivity, MainActivity::class.java)
        intent.putExtra("index", index)
        setResult(RESULT_OK, intent)
        finish()
    }

    override fun onBackPressed() {
        val intent = Intent(this@SelectPlanetActivity, MainActivity::class.java)
        setResult(RESULT_CANCELED, intent)
        finish()
        super.onBackPressed()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                val intent = Intent(this@SelectPlanetActivity, MainActivity::class.java)
                setResult(RESULT_CANCELED, intent)
                finish()
            }
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }
}
