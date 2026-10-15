package net.astropvp.core.listeners;

import net.astropvp.core.AstroCore;
import net.astropvp.core.utils.HolyWhiteScrollUtils;
import com.cosmicpvp.cosmicutils.customevents.CustomCosmicPlayerEvent;
import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.ItemUtils;
import com.cosmicpvp.cosmicutils.utils.MaskUtils;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.arkhamnetwork.Arkkit.patches.combatlog.CombatLoggerDeathEvent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public class HolyWhiteScrollCombatLoggerListener implements Listener {
    @EventHandler
    public void onCombatLoggerDeath(CombatLoggerDeathEvent event) {
        if (event.getCombatLoggerEntity().getWorld().getName().contains("world_duel"))
            return;
        if (event.getCombatLoggerEntity().getWorld().getName().contains("world_the_end"))
            return;
        if (event.getInventoryItems() != null) {
            List<ItemStack> savedItems = new ArrayList<>();
            LivingEntity entity = event.getCombatLoggerEntity();
            Player killer = null;
            if (event.getRawEvent() instanceof EntityDamageByEntityEvent) {
                EntityDamageByEntityEvent e = (EntityDamageByEntityEvent)event.getRawEvent();
                if (e.getDamager() instanceof Player || (e.getDamager() instanceof Projectile && ((Projectile)e.getDamager()).getShooter() instanceof Player))
                    killer = (e.getDamager() instanceof Player) ? (Player)e.getDamager() : (Player)((Projectile)e.getDamager()).getShooter();
            }
            boolean wearingMonopolyMask = isWearingMonopoly(killer);
            for (ItemStack item : Lists.newArrayList(event.getInventoryItems())) {
                if (item != null && item.getType() != Material.AIR) {
                    if (HolyWhiteScrollUtils.itemHasHolyWhitescroll(item)) {
                        double chance = getChance(killer);
                        if (wearingMonopolyMask && Math.random() <= chance) {
                            HolyWhiteScrollUtils.removeHolyWhitescroll(item);
                            try {
                                (new ItemStack[1])[0] = item;
                                Bukkit.getPluginManager().callEvent(new CustomCosmicPlayerEvent(UUID.fromString(event.getPlayerUUIDString()), "HOLY_WHITESCROLL_DEATH", new Object[]{Lists.newArrayList(new ItemStack[1])}));
                            } catch (Exception e2) {
                                e2.printStackTrace();
                            }
                            entity.getWorld().playSound(entity.getLocation(), Sound.ITEM_BREAK, 5.0F, 1.2F);
                            entity.getWorld().playSound(entity.getLocation(), Sound.WITHER_SHOOT, 3.0F, 10.0F);
                            sendMessage(killer, event, item);
                            Bukkit.getLogger().info("Dropping " + item + " due to " + killer.getName() + "'s Monopoly Mask procing.");
                            event.getInventoryItems().remove(item);
                            entity.getWorld().dropItemNaturally(entity.getLocation(), item);
                        } else {
                            savedItems.add(item);
                            event.getInventoryItems().remove(item);
                        }
                        if (savedItems.size() <= 0)
                            continue;
                        AstroCore.getInstance().getHolyWhiteScrollListener().getSavedItems().put(event.getPlayerUUIDString(), savedItems);
                        Bukkit.getLogger().info("Storing " + Arrays.toString(savedItems.toArray(new ItemStack[savedItems.size()])) + " holy whitescroll'd items for " + event.getPlayerName() + " due to combat logger death!");
                    }
                }
            }
        }
    }

    private void sendMessage(Player killer, CombatLoggerDeathEvent event, ItemStack item) {
        if(getChance(killer) == 0.66) {
            killer.sendMessage(CC.GreenB + "* MUTATED MONOPOLY MASK [" + CC.Gray + event.getPlayerName() + " (+ " + ItemUtils.getDisplayName(item) + CC.Gray + ")" + CC.GreenB + "] *");
        } else if(getChance(killer) == 0.33) {
            killer.sendMessage(CC.GreenB + "* MONOPOLY MASK [" + CC.Gray + event.getPlayerName() + " (+ " + ItemUtils.getDisplayName(item) + CC.Gray + ")" + CC.GreenB + "] *");

        }
    }

    private double getChance(Player killer) {
        return MaskUtils.hasMaskEquipped(killer, "M_MONOPOLY") ? 0.66 : MaskUtils.hasMaskEquipped(killer, "M_MONOPOLY") ? 0.33 : 0;
    }

    private boolean isWearingMonopoly(Player killer) {
        if(killer == null) return false;
        return MaskUtils.hasMaskEquipped(killer, "MONOPOLY") || MaskUtils.hasMaskEquipped(killer, "M_MONOPOLY");
    }
}
