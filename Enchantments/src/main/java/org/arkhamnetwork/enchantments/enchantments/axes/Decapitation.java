/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.rit.sucy.CustomEnchantment
 *  net.minecraft.util.org.apache.commons.lang3.text.WordUtils
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.event.entity.PlayerDeathEvent
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.inventory.meta.SkullMeta
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 */
package org.arkhamnetwork.enchantments.enchantments.axes;

import com.rit.sucy.CustomEnchantment;
import org.apache.commons.lang.WordUtils;
import org.arkhamnetwork.enchantments.Enchantments;
import org.arkhamnetwork.enchantments.util.GeneralUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

import java.text.SimpleDateFormat;
import java.util.*;

public class Decapitation
extends CustomEnchantment {
    public Decapitation() {
        super("Decapitation", GeneralUtil.axe, 7);
        this.max = 3;
        this.base = 30.0;
        this.interval = 10.0;
        Bukkit.getPluginManager().registerEvents((Listener)new DecapitationEvents(), (Plugin)Enchantments.getInstance());
    }

    public void applyEffect(LivingEntity user, LivingEntity target, int enchantLevel, EntityDamageByEntityEvent event) {
        double chance;
        if (!(target instanceof Player)) {
            return;
        }
        Player ptarget = (Player)target;
        double random = Math.random();
        if (random < (chance = 0.03 * (double)enchantLevel)) {
            DecapitationEvents.decapitationEnchant.add(ptarget.getName());
        }
    }

    private static class DecapitationEvents
    implements Listener {
        private static HashSet<String> decapitationEnchant = new HashSet();
        private static Calendar calendar = Calendar.getInstance(TimeZone.getDefault());

        private DecapitationEvents() {
        }

        @EventHandler(priority=EventPriority.LOW)
        public void onPlayerDeath(PlayerDeathEvent e) {
            Player p_dead = e.getEntity();
            if (decapitationEnchant.contains(p_dead.getName())) {
                Bukkit.getLogger().info("HEADLESS ENCH -- Dropping skull of " + p_dead.getName() + "!");
                if (p_dead.getKiller() != null && p_dead.getKiller() instanceof Player) {
                    e.getDrops().add(this.getPlayerHead(p_dead.getName(), p_dead.getKiller()));
                    if (Bukkit.getPluginManager().isPluginEnabled("CosmicContests") && !p_dead.getKiller().hasMetadata("cosmicContests_gotHead_" + p_dead.getName())) {
                        Bukkit.dispatchCommand((CommandSender)Bukkit.getConsoleSender(), (String)("contests add " + p_dead.getKiller().getName() + " headsCollected 1"));
                        p_dead.getKiller().setMetadata("cosmicContests_gotHead_" + p_dead.getName(), (MetadataValue)new FixedMetadataValue((Plugin)Enchantments.getInstance(), (Object)true));
                    }
                } else {
                    e.getDrops().add(GeneralUtil.getPlayerHead(p_dead.getName()));
                }
                decapitationEnchant.remove(p_dead.getName());
            }
        }

        private ItemStack getPlayerHead(String p_name, Player pKiller) {
            ItemStack is = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
            SkullMeta sm = (SkullMeta)is.getItemMeta();
            sm.setOwner(p_name);
            sm.setDisplayName((Object)ChatColor.WHITE + "Skull of " + p_name);
            SimpleDateFormat month_date = new SimpleDateFormat("MMMM");
            String monthName = month_date.format(calendar.getTime());
            sm.setLore(new ArrayList<String>(Arrays.asList((Object)ChatColor.GRAY + "Defeated by " + (Object)ChatColor.WHITE + pKiller.getName() + (Object)ChatColor.GRAY + " on ", (Object)ChatColor.WHITE + monthName + " " + calendar.get(5) + ", " + calendar.get(1) + (Object)ChatColor.GRAY + " at ", (Object)ChatColor.WHITE + "" + pKiller.getLocation().getBlockX() + ", " + pKiller.getLocation().getBlockY() + ", " + pKiller.getLocation().getBlockZ() + (Object)ChatColor.GRAY + " with a(n) ", (Object)ChatColor.WHITE + this.getItemName(pKiller.getItemInHand()) + "!")));
            is.setItemMeta((ItemMeta)sm);
            return is;
        }

        private String getItemName(ItemStack i) {
            if (i == null || i.getType() == Material.AIR) {
                return "Fists";
            }
            if (i.hasItemMeta() && i.getItemMeta().hasDisplayName()) {
                return i.getItemMeta().getDisplayName();
            }
            return WordUtils.capitalize((String)i.getType().name());
        }
    }
}

