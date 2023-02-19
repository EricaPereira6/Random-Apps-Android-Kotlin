package dam_a42356.universeexplorer

import android.annotation.SuppressLint
import android.content.res.Resources
import android.content.res.TypedArray
import android.util.Log
import java.lang.reflect.Type
import java.util.ArrayList

class Planet  (planetIndex : Int, planetName : String, planetMass : Double, planetGravity : Double,
               imageResourceValues : ArrayList<Int>, imageSmallResourceValues : Int){

    private var index : Int // planet index in resource file
    private var name : String // name
    private var mass : Double // mass
    private var gravity : Double // gravity
    private var imgResourceValues : ArrayList<Int>  // images resource IDs of the planet
    private var imgSmallResourceValues : Int // small image resource ID of the planet
    private var forceField = false // forceField activated: true or false
    private var numColonies = 0 // number of colonies
    private var numMilitaryBases = 0 // number of military bases

    // initializer block
    init {
        index = planetIndex
        name = planetName
        mass = planetMass
        gravity = planetGravity
        imgResourceValues = imageResourceValues
        imgSmallResourceValues = imageSmallResourceValues
    }

    fun getIndex() : Int {
        return index
    }

    fun getName() : String {
        return name
    }

    fun getMass() : Double {
        return mass
    }

    fun getGravity() : Double {
        return gravity
    }

    fun getImgResourceValues() : ArrayList<Int> {
        return imgResourceValues
    }

    fun getImgSmallResourceValues() : Int {
        return imgSmallResourceValues
    }

    fun getForceField() : Boolean {
        return forceField
    }

    fun setForceField(force : Boolean) {
        forceField = force
    }

    fun getNumColonies() : Int {
        return numColonies
    }

    fun setNumColonies(nColonies : Int) {
        numColonies = nColonies
    }

    fun getNumMilitaryBases() : Int {
        return numMilitaryBases
    }

    fun setNumMilitaryBases(nMilitaryBases : Int) {
        numMilitaryBases = nMilitaryBases
    }

    override fun toString(): String {
        return "\n Planet " + getName() + "\nIndex: " + getIndex() + "\nMass: " + getMass() + "\nGravity: " +
                getGravity() + "\nForce Field: " + getForceField() + "\nNumber of Colonies: " + getNumColonies() +
                "\nNumber of Military Bases: " + getNumMilitaryBases() + "\nImage Resource Values: " +
                getImgResourceValues() + "\nSmall Image Resource Values: " + getImgSmallResourceValues()
    }
    companion object {
        @SuppressLint("Recycle")
        fun getPlanetFromResources(index: Int, res: Resources): Planet {

            val planets: TypedArray = res.obtainTypedArray(R.array.planets)

            val idPlanet = planets.getResourceId(index, -1)
            val planet : TypedArray = res.obtainTypedArray(idPlanet)

            val size = planet.length()

            val name = planet.getString(0).toString()
            @SuppressLint("ResourceType") val mass = planet.getString(1).toString()
            @SuppressLint("ResourceType") val gravity = planet.getString(2).toString()
            @SuppressLint("ResourceType") val smallResourceValues = planet.getResourceId(3, -1)

            val resourceValues = ArrayList<Int>()
            for (i in 4 until size) {
                val id = planet.getResourceId(i, -1)
                resourceValues.add(id)
            }

            return Planet(
                index,
                name,
                mass.toDouble(),
                gravity.toDouble(),
                resourceValues,
                smallResourceValues
            )
        }
    }
}
