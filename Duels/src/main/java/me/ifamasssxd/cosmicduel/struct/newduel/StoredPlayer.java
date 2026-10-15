/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.gmail.nossr50.runnables.skills.BleedTimerTask
 *  org.arkhamnetwork.enchantments.me.astropvp.cratekeys.net.astropvp.armorsets.listeners.PlayerPotionEffectListener
 *  org.bukkit.Bukkit
 *  org.bukkit.Sound
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.me.astropvp.cratekeys.inventory.ItemStack
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.potion.PotionEffect
 */
package me.ifamasssxd.cosmicduel.struct.newduel;

import me.ifamasssxd.cosmicduel.CosmicDuel;
import me.ifamasssxd.cosmicduel.utils.DuelUtils;
import org.arkhamnetwork.enchantments.listeners.PlayerPotionEffectListener;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class StoredPlayer {
    private UUID uuid;
    boolean restored = false;
    private ItemStack[] inventory;
    private ItemStack[] armor = new ItemStack[4];
    private float xp;
    private int level;
    private List<PotionEffect> storedEffects = new ArrayList<PotionEffect>();

    public StoredPlayer(Player p) {
        int i;
        for (PotionEffect effect : p.getActivePotionEffects()) {
            this.storedEffects.add(new PotionEffect(effect.getType(), effect.getDuration(), effect.getAmplifier()));
        }
        this.inventory = new ItemStack[p.getInventory().getContents().length];
        for (i = 0; i < p.getInventory().getArmorContents().length; ++i) {
            this.armor[i] = p.getInventory().getArmorContents()[i];
            if (this.armor[i] == null) continue;
            this.armor[i] = this.armor[i].clone();
        }
        for (i = 0; i < this.inventory.length; ++i) {
            this.inventory[i] = p.getInventory().getContents()[i];
            if (this.inventory[i] == null) continue;
            this.inventory[i] = this.inventory[i].clone();
        }
        this.xp = p.getExp();
        this.level = p.getLevel();
        this.uuid = p.getUniqueId();
    }

    public void restorePlayer(Player p, boolean reloading) {
        if (!p.getUniqueId().equals(this.uuid) || this.restored) {
            return;
        }
        p.setMetadata("removeItems", (MetadataValue)new FixedMetadataValue((Plugin)CosmicDuel.get(), (Object)""));
        p.closeInventory();
        p.setItemOnCursor((ItemStack)null);
        try {
            DuelUtils.unequipEffects(p);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        p.getInventory().clear();
        p.getInventory().setArmorContents((ItemStack[])null);
        p.removeMetadata("removeItems", (Plugin)CosmicDuel.get());
        p.updateInventory();
        for (PotionEffect e2 : p.getActivePotionEffects()) {
            p.removePotionEffect(e2.getType());
        }
        p.getInventory().setContents((ItemStack[])this.inventory.clone());
        p.getInventory().setArmorContents((ItemStack[])this.armor.clone());
        p.updateInventory();
        if (!reloading) {
            Bukkit.getScheduler().scheduleSyncDelayedTask((Plugin)CosmicDuel.get(), () -> {
                if (!p.isOnline()) {
                    Bukkit.getLogger().info("UNABLE to find player after duel....");
                    return;
                }
                PlayerPotionEffectListener.rescanPlayerArmor((Player)p);
            }, 40L);
        } else {
            PlayerPotionEffectListener.rescanPlayerArmor((Player)p);
        }
        Bukkit.getLogger().info("Restoring level " + this.level + " and EXP: " + this.xp + " for " + p.getName());
        p.setLevel(this.level);
        p.setExp(this.xp);
        p.setFallDistance(0.0f);
        p.setFireTicks(0);
        p.playSound(p.getLocation(), Sound.ITEM_PICKUP, 3.0f, 1.0f);
        p.setHealth(p.getMaxHealth());
        this.restored = true;

    }
}

