package net.astropvp.space.struct

import com.cosmicpvp.cosmicutils.serializer.location.LocationSerializerDeserializer
import net.astropvp.space.SpacePlugin
import org.bukkit.Location

object LocationManager {

    val locations: MutableList<Location> = mutableListOf()

    fun saveLocationsToDisk() {
        val locationsAsStrings = locations.map { LocationSerializerDeserializer.serialize(it) }
        SpacePlugin.instance.config.set("locations", locationsAsStrings)
        SpacePlugin.instance.saveConfig()
    }

    fun loadLocationsFromDisk() {
        val locationsFromConfig = SpacePlugin.instance.config.getStringList("locations")
        this.locations.addAll(locationsFromConfig.map { LocationSerializerDeserializer.deserialize(it) })
    }
}