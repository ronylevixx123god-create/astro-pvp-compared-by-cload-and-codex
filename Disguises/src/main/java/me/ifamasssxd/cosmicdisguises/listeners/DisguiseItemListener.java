/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.CC
 *  me.libraryaddict.disguise.disguisetypes.DisguiseType
 *  org.apache.commons.lang.StringUtils
 *  org.arkhamnetwork.Arkkit.patches.ItemID
 *  org.arkhamnetwork.Arkkit.plugins.ArkPlugins
 *  org.bukkit.Bukkit
 *  org.bukkit.Material
 *  org.bukkit.Sound
 *  org.bukkit.command.CommandSender
 *  org.bukkit.event.Event$Result
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.block.Action
 *  org.bukkit.event.player.PlayerInteractEvent
 *  org.bukkit.me.astropvp.cratekeys.inventory.ItemStack
 */
package me.ifamasssxd.cosmicdisguises.listeners;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.LuckPermUtils;
import me.ifamasssxd.cosmicdisguises.CosmicDisguises;
import me.ifamasssxd.cosmicdisguises.managers.DisguiseManager;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import org.apache.commons.lang.StringUtils;
import org.arkhamnetwork.Arkkit.patches.ItemID;
import org.arkhamnetwork.Arkkit.plugins.ArkPlugins;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Set;

public class DisguiseItemListener
implements Listener {
    @EventHandler(priority=EventPriority.HIGHEST)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.hasItem() && (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR)) {
            ItemStack item = event.getItem();
            if (item == null || item.getType() != Material.BONE) {
                return;
            }
            DisguiseManager manager = CosmicDisguises.get().getDisguiseManager();
            DisguiseType type = manager.getDisguiseTypeFromItem(item);
            if (type == null) {
                return;
            }
            if (item.getAmount() > 1) {
                event.getPlayer().sendMessage(CC.Red + "You cannot claim stacked disguise items!");
                return;
            }
            event.setCancelled(true);
            event.setUseItemInHand(Event.Result.DENY);
            String permission = "libsdisguises.disguise." + type.name().toLowerCase();
            if (event.getPlayer().hasPermission(permission)) {
                event.getPlayer().sendMessage(CC.RedB + "(!) " + CC.Red + "You already have this disguise unlocked!");
                return;
            }
            event.getPlayer().setItemInHand(null);
            event.getPlayer().updateInventory();
            event.getPlayer().sendMessage(CC.GreenB + "(!) " + CC.Green + "You have unlocked the " + CC.GreenU + StringUtils.capitaliseAllWords(type.name().toLowerCase().replace("_", " ")) + CC.Green + " disguise!");
            event.getPlayer().sendMessage(CC.Gray + "Use '/disguises' to view your new disguise!");
            event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.LEVEL_UP, 1.0f, 1.14f);
            LuckPermUtils.addPermissions(event.getPlayer(), permission);
            Set<DisguiseType> set = CosmicDisguises.get().getDisguiseManager().getAvailableDisguises(event.getPlayer(), true);

            Bukkit.getLogger().info("Giving " + event.getPlayer().getName() + " Disguise Type: " + type + " from claiming item!");
        }
    }
}

