package dam_a42356.universeexplorer

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView

class GridViewAdapter(context: Context, data: ArrayList<Planet>) :
    ArrayAdapter<Planet>(context, R.layout.layout_planet_item, R.id.textViewPlanetSelect, data) {

    override fun getView(position : Int, view : View?, parent : ViewGroup) : View {

        var v = view

        if (view == null) { // create base view
            v = super.getView(position , view, parent)

            // set data from data object to view Planet
            val planet : Planet? = getItem(position)

            val tv : TextView = v.findViewById(R.id.textViewPlanetSelect)
            if (planet != null) {
                tv.text = planet.getName()
            }

            val iv : ImageView = v.findViewById(R.id.imageViewPlanetSelect)
            if (planet != null) {
                iv.setImageResource(planet.getImgResourceValues()[0])
            }

            // set object on view tag
            v.tag = planet
        }
        return v!!
    }
}