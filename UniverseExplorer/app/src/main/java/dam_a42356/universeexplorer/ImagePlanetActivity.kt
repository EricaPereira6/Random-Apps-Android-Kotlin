package dam_a42356.universeexplorer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import java.util.*
import kotlin.properties.Delegates

class ImagePlanetActivity : AppCompatActivity() {

    private var index by Delegates.notNull<Int>()
    private var increment = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_planet)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val extras = intent.extras
        if (extras != null) {
            index = extras.getInt("planetIndex")
        }

        val planet = Planet.getPlanetFromResources(index, resources)

        val nameP : TextView = findViewById(R.id.imgPlanetName)
        nameP.text = planet.getName()

        val img : ImageView = findViewById(R.id.imgPlanetImage)
        img.setImageResource(planet.getImgResourceValues()[0])

        val name2 : TextView = findViewById(R.id.imgPlanetName)
        title = name2.text.toString()

        img.setOnClickListener{
            increment++
            val imgIndex = increment % planet.getImgResourceValues().size
            img.setImageResource(planet.getImgResourceValues()[imgIndex])
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        return true
    }
}
