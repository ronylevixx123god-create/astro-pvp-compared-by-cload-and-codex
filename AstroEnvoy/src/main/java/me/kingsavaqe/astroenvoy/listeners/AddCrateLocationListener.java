package me.kingsavaqe.astroenvoy.listeners;


import com.cosmicpvp.cosmicutils.serializer.location.LocationSerializerDeserializer;
import com.cosmicpvp.cosmicutils.utils.CC;
import me.kingsavaqe.astroenvoy.EnvoyPlugin;
import me.kingsavaqe.astroenvoy.struct.EnvoyManager;
import me.kingsavaqe.astroenvoy.utils.CustomItemUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.ArrayList;
import java.util.List;

public class AddCrateLocationListener implements Listener {

    private final EnvoyPlugin plugin;
    public AddCrateLocationListener(EnvoyPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void placeCrateEvent(final BlockPlaceEvent event) {
        if(event.getItemInHand() == null || event.getItemInHand().getType() != Material.CHEST) return;
        if(!CustomItemUtils.getCrankedCustomItem(event.getItemInHand()).equals("envoySetupChest")) return;
        event.setCancelled(true);
        Player player = event.getPlayer();
        Location location = event.getBlockPlaced().getLocation();
        this.plugin.getEnvoyManager().getLocations().add(location);
        player.sendMessage(CC.translate(EnvoyManager.getPrefix() + "&aAdded crate location at: &7" + LocationSerializerDeserializer.serialize(location)));
        player.playSound(player.getLocation(), Sound.SUCCESSFUL_HIT, 2.0F, 2.0F);


        FileConfiguration yamlConfiguration = this.plugin.getConfig();
        List<String> serializedLocations = new ArrayList<>();
        String base64Location = LocationSerializerDeserializer.serialize(location);
        if(yamlConfiguration.contains("locations")) serializedLocations.addAll(yamlConfiguration.getStringList("locations"));
        serializedLocations.add(base64Location);
        yamlConfiguration.set("locations", serializedLocations);
        this.plugin.saveConfig();
    }

}
