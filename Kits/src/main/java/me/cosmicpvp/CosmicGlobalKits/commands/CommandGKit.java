/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.cosmicpvp.cosmicutils.customevents.CustomCosmicPlayerEvent
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.CC
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.JSONMessage
 *  org.apache.commons.lang.StringUtils
 *  org.bukkit.Bukkit
 *  org.bukkit.Sound
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.me.astropvp.cratekeys.inventory.Inventory
 *  org.bukkit.me.astropvp.cratekeys.inventory.ItemStack
 */
package me.cosmicpvp.CosmicGlobalKits.commands;

import com.cosmicpvp.cosmicutils.customevents.CustomCosmicPlayerEvent;
import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.JSONMessage;
import java.util.Arrays;
import java.util.Map;
import me.cosmicpvp.CosmicGlobalKits.CosmicGlobalKits;
import me.cosmicpvp.CosmicGlobalKits.CosmicGlobalKitsAPI;
import me.cosmicpvp.CosmicGlobalKits.libs.GlobalKits;
import me.cosmicpvp.CosmicGlobalKits.struct.GlobalKitData;
import me.cosmicpvp.CosmicGlobalKits.struct.GlobalKitUser;
import me.cosmicpvp.CosmicGlobalKits.utils.GlobalCooldowns;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class CommandGKit
implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(CC.Red + "Only players can view the gkit menu!");
            return true;
        }
        Player pl = (Player)sender;
        GlobalKitUser kitUser = CosmicGlobalKits.getPlugin().getGlobalKitUser(pl.getUniqueId());
        if (kitUser == null || kitUser.isLoadingData()) {
            pl.sendMessage(CC.RedB + "(!) " + CC.Red + "Please wait while your Global Kit data is loaded!");
            pl.sendMessage(CC.Gray + "Allow up to 15 seconds before attempting to relog.");
            return true;
        }
        pl.openInventory(this.getKitMenu(pl));
        pl.playSound(pl.getLocation(), Sound.BAT_TAKEOFF, 1.0f, 0.65f);
        return true;
    }

    private Inventory getKitMenu(Player pl) {
        Inventory inv = Bukkit.createInventory(null, (int)27, (String)"Astro Kits");
        GlobalKitUser user = CosmicGlobalKits.getPlugin().getGlobalKitUser(pl.getUniqueId());
        Map<GlobalKits, GlobalKitData> kitData = user.getGlobalKits();
        for (GlobalKits gkit : GlobalKits.values()) {
            inv.addItem(gkit.createMenuItem(kitData, pl, true));
        }
        return inv;
    }
}

