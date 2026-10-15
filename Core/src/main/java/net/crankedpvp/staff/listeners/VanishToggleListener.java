package net.crankedpvp.staff.listeners;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.earth2me.essentials.User;
import de.tr7zw.nbtapi.NBTItem;
import net.astropvp.core.AstroCore;
import org.arkhamnetwork.Arkkit.utils.EssentialsUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class VanishToggleListener implements Listener {

    private final AstroCore plugin;
    public VanishToggleListener(AstroCore plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void toggleVanish(PlayerInteractEvent event) {
        if(event.getItem() == null || event.getItem().getType() == Material.AIR) return;

        ItemStack itemStack = event.getItem();
        NBTItem nbtItem = new NBTItem(itemStack);
        Player player = event.getPlayer();
        if(!nbtItem.getString("staffModeItem").equals("vanish")) return;
        if(!this.plugin.getStaffManager().isPlayerInStaffMode(player)) return;
        event.setCancelled(true);
        User user = EssentialsUtils.getEssentials().getUser(player);
        user.setVanished(!user.isVanished());
        String toggleString = user.isVanished() ? "&a&lON" : "&c&lOFF";
        player.sendMessage(CC.translate(AstroCore.prefix + "&eYou have toggled vanish " + toggleString + " &ewith staff mode"));
    }
}
