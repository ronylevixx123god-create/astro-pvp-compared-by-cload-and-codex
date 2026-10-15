package me.cosmicpvp.CosmicGlobalKits.listeners;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.google.common.collect.Lists;
import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import me.cosmicpvp.CosmicGlobalKits.CosmicGlobalKits;
import me.cosmicpvp.CosmicGlobalKits.CosmicGlobalKitsAPI;
import me.cosmicpvp.CosmicGlobalKits.libs.FallenHero;
import me.cosmicpvp.CosmicGlobalKits.libs.GlobalKits;
import me.cosmicpvp.CosmicGlobalKits.libs.KitItems;
import me.cosmicpvp.CosmicGlobalKits.utils.WorldGuardUtils;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class FallenHeroListener
implements Listener {
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) throws Exception {
        if (e.hasItem() && (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) && FallenHero.isFallenHeroItem(e.getItem())) {
            Player pNear;
            String itemName;
            LivingEntity fallenHero;
            Location l;
            ItemStack i;
            e.setCancelled(true);
            e.setUseItemInHand(Event.Result.DENY);
            Player p = e.getPlayer();
            FLocation fl = new FLocation(p.getLocation());
            if (!Board.getInstance().getFactionAt((FLocation)fl).isWarZone()) {
                p.sendMessage((Object)ChatColor.RED + "" + (Object)ChatColor.BOLD + "(!) " + (Object)ChatColor.RED + "You cannot summon a Fallen Hero outside the Warzone.");
                return;
            }
            if (com.cosmicpvp.cosmicutils.utils.WorldGuardUtils.isInRegionsContainsFast((Location)p.getLocation(), (List)Lists.newArrayList(new String[]{"sell_room"}))) {
                p.sendMessage((Object)ChatColor.RED + "" + (Object)ChatColor.BOLD + "(!) " + (Object)ChatColor.RED + "You cannot summon a Fallen Hero at this location.");
                return;
            }
            if (WorldGuardUtils.isPvPDisabled(p.getLocation())) {
                p.sendMessage((Object)ChatColor.RED + "" + (Object)ChatColor.BOLD + "(!) " + (Object)ChatColor.RED + "You cannot summon a Fallen Hero inside a Safezone.");
                return;
            }
            if (p.getWorld().getEnvironment() == World.Environment.NETHER && p.getLocation().getY() >= 128.0) {
                p.sendMessage((Object)ChatColor.RED + "" + (Object)ChatColor.BOLD + "(!) " + (Object)ChatColor.RED + "You cannot summon a Fallen Hero at this location.");
                return;
            }
            if (p.getWorld().getName().contains("world_duels")) {
                p.sendMessage(CC.RedB + "(!) " + CC.Red + "You cannot summon a fallen hero inside a duel!");
                return;
            }
            GlobalKits gkit = FallenHero.getGlobalKitFromFallenHeroItem(e.getItem());
            if (gkit != null) {
                i = e.getItem();
                if (i.getAmount() > 1) {
                    i.setAmount(i.getAmount() - 1);
                    p.setItemInHand(i);
                } else {
                    p.setItemInHand(null);
                }
                l = p.getTargetBlock((HashSet<Byte>) null, 6).getLocation().add(0.0, 1.0, 0.0);
                fallenHero = FallenHero.spawnFallenHero(gkit, l);
                fallenHero.setMetadata("fallenHeroSummoner", (MetadataValue)new FixedMetadataValue((Plugin)CosmicGlobalKits.getPlugin(), (Object)p.getName()));
                itemName = gkit.getItemName().replace(" Kit", "");
                p.sendMessage("");
                p.sendMessage((Object)ChatColor.WHITE + "" + (Object)ChatColor.BOLD + "*** " + (Object)ChatColor.UNDERLINE + "Fallen Hero:" + (Object)ChatColor.RESET + " " + itemName + (Object)ChatColor.WHITE + "" + (Object)ChatColor.BOLD + " ***");
                p.sendMessage((Object)ChatColor.GRAY + "You have summoned a Fallen Hero! Defeat it to claim its treasures and a chance to " + (Object)ChatColor.UNDERLINE + "unlock its /gkit!");
                p.sendMessage("");
                for (Entity ent : p.getNearbyEntities(32.0, 32.0, 32.0)) {
                    if (!(ent instanceof Player)) continue;
                    pNear = (Player)ent;
                    pNear.sendMessage((Object)ChatColor.YELLOW + "" + (Object)ChatColor.BOLD + "(!) " + (Object)ChatColor.YELLOW + p.getName() + " has summoned a " + itemName + (Object)ChatColor.YELLOW + " Fallen Hero!");
                    pNear.sendMessage((Object)ChatColor.GRAY + "Locate and slay the Fallen Hero to steal its loot for yourself!");
                }
                p.playSound(p.getLocation(), Sound.WITHER_SPAWN, 1.0f, 1.2f);
            }
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent e) {
        if (e.getEntity().hasMetadata("fallenHero") && !e.getEntity().hasMetadata("hasDied")) {
            LivingEntity ent = e.getEntity();
            String internalName = e.getEntity().hasMetadata("fallenHeroName") ? ((MetadataValue)e.getEntity().getMetadata("fallenHeroName").get(0)).asString() : null;
            GlobalKits gkit = null;
            try {
                gkit = GlobalKits.valueOf(((MetadataValue)ent.getMetadata("fallenHero").get(0)).asString());
            }
            catch (IllegalArgumentException illegalArgumentException) {
                // empty catch block
            }
            e.getDrops().clear();
            if (gkit != null) {
                double kitDropChance;
                ent.setMetadata("hasDied", (MetadataValue)new FixedMetadataValue((Plugin)CosmicGlobalKits.getPlugin(), (Object)true));
                double d = kitDropChance = ent.hasMetadata("mysteryFallenHero") ? 0.5 : 0.22;
                if (Math.random() <= kitDropChance) {
                    this.createCustomItem(ent.getWorld().dropItem(ent.getLocation(), KitItems.getKitItem((GlobalKits)gkit)));
                    String playerName = ((MetadataValue)ent.getMetadata("fallenHeroSummoner").get(0)).asString();
                    Bukkit.broadcastMessage((String)((Object)ChatColor.GOLD + "" + (Object)ChatColor.BOLD + "(!) " + (Object)ChatColor.GOLD + playerName + " has unlocked the " + gkit.getItemName() + (Object)ChatColor.GOLD + " from a Fallen Hero!"));
                } else {
                    for (ItemStack i : CosmicGlobalKitsAPI.getGlobalKitItems(gkit, null)) {
                        this.createCustomItem(ent.getWorld().dropItem(ent.getLocation(), i));
                    }
                }
                ent.remove();
            }
        }
    }

    private Item createCustomItem(Item item) {
        item.setMetadata("noItemDamage", (MetadataValue)new FixedMetadataValue((Plugin)CosmicGlobalKits.getPlugin(), (Object)true));
        item.setMetadata("do_not_clear", (MetadataValue)new FixedMetadataValue((Plugin)CosmicGlobalKits.getPlugin(), (Object)true));
        return item;
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent e) {
        if (e.getEntity().hasMetadata("noItemDamage")) {
            e.setCancelled(true);
            e.setDamage(0.0);
        }
    }
}

