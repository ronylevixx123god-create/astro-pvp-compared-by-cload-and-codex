package net.astropvp.space.listeners

import com.cosmicpvp.cosmicutils.serializer.location.LocationSerializerDeserializer
import de.tr7zw.nbtapi.NBTItem
import net.astropvp.space.struct.LocationManager
import net.astropvp.space.utils.msg
import net.astropvp.space.utils.playSuccessSound
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockPlaceEvent

class LocationSetListener : Listener {

    @EventHandler
    fun onPlace(event: BlockPlaceEvent) {
        val player = event.player
        val item = player.itemInHand

        val nbtItem = NBTItem(item)
        if(nbtItem.hasNBTData().not()) return
        if(nbtItem.hasKey("savCustomItem").not()) return
        if(nbtItem.getString("savCustomItem") != "spaceZombieSpawner") return

        event.isCancelled = true
        val location = event.block.location
        LocationManager.locations.add(location)
        event.player.msg("&aAdded ${LocationSerializerDeserializer.serialize(location)} to list of spawnpoints")
        event.player.playSuccessSound()
    }
}