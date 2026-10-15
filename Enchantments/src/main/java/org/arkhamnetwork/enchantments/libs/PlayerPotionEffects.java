package org.arkhamnetwork.enchantments.libs;

import com.compilex.CosmicDungeons.DungeonsAPI;
import com.compilex.CosmicDungeons.dungeons.libs.DungeonPlayer;
import com.rit.sucy.CustomEnchantment;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

import me.astropvp.astropvpskins.AstroItemSkinsAPI;
import me.astropvp.astropvpskins.enums.SkinType;
import org.arkhamnetwork.Arkkit.utils.WorldGuardUtils;
import org.arkhamnetwork.enchantments.Enchantments;
import org.arkhamnetwork.enchantments.cmds.CommandApply;
import org.arkhamnetwork.enchantments.listeners.PlayerPotionEffectListener;
import org.arkhamnetwork.enchantments.mastery.MasteryMortalCoil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PlayerPotionEffects {
    String playerName;

    UUID playerUUID;

    Player bukkitPlayer;

    ConcurrentHashMap<SpecificCustomEnchantment, CopyOnWriteArrayList<AbstractPotionEffect>> effects;

    public PlayerPotionEffects(Player p) {
        this.bukkitPlayer = p;
        this.playerName = p.getName();
        this.playerUUID = p.getUniqueId();
        this.effects = new ConcurrentHashMap<>();
    }

    public Player getPlayer() {
        return this.bukkitPlayer;
    }

    public void addPotionEffect(SpecificCustomEnchantment ce, PotionEffect pe) {
        addPotionEffect(ce, pe, null);
    }

    public void addPotionEffect(SpecificCustomEnchantment ce, PotionEffect pe, Consumer<AbstractPotionEffect> consumer) {
        AbstractPotionEffect retr;
        boolean hasBetterActiveEffect = false;
        if (getPlayer().getLocation().getWorld().getName().equals("world_outpost_pluto")) return;

        for (Map.Entry<SpecificCustomEnchantment, CopyOnWriteArrayList<AbstractPotionEffect>> effects : this.effects.entrySet()) {
            if (hasBetterActiveEffect)
                break;
            label33: for (AbstractPotionEffect ape : effects.getValue()) {
                if (ape.getEffectType().equals(pe.getType()) && ape.getPotionLevel() >= pe.getAmplifier()) {
                    if (CommandApply.debug) {
                        Bukkit.getLogger().info("[PPE] Skipped adding of potionEffect from " + pe.toString() + " to " + getPlayerName() + " from " + ce.toString() + " due to existing LVL:" + ape.getPotionLevel() + " of effect from " + ((SpecificCustomEnchantment)effects.getKey()).toString() + "!");
                        hasBetterActiveEffect = true;
                        continue;
                    }
                    break label33;
                }
            }
        }
        if (this.effects.containsKey(ce)) {
            CopyOnWriteArrayList<AbstractPotionEffect> effects2 = this.effects.get(ce);
            effects2.add(retr = new AbstractPotionEffect(pe.getType(), pe.getAmplifier()));
            this.effects.put(ce, effects2);
        } else {
            ConcurrentHashMap<SpecificCustomEnchantment, CopyOnWriteArrayList<AbstractPotionEffect>> effects3 = this.effects;
            AbstractPotionEffect[] a = { null };
            retr = a[0] = new AbstractPotionEffect(pe.getType(), pe.getAmplifier());
            effects3.put(ce, new CopyOnWriteArrayList<>(Arrays.asList(a)));
        }
        String regionName = WorldGuardUtils.getRegionName(getPlayer().getLocation());
        if (regionName.contains("parkour") || regionName.contains("find_corrupt_room") || regionName.contains("maze") || regionName.equalsIgnoreCase("fireball_alley"))
            return;
        if (!hasBetterActiveEffect) {
            if (consumer != null)
                consumer.accept(retr);
            applyPotionEffects(true);
            if (CommandApply.debug)
                Bukkit.getLogger().info("[PPE] Added " + pe.getType().getName() + ":" + pe.getAmplifier() + " to " + getPlayerName() + " trackedEffects of " + ce.toString());
        }
    }

    public void removePotionEffect(SpecificCustomEnchantment ce, PotionEffectType effectType) {
        CopyOnWriteArrayList<AbstractPotionEffect> pEffects = this.effects.containsKey(ce) ? this.effects.get(ce) : null;
        if (pEffects != null) {
            for (AbstractPotionEffect effect : pEffects) {
                if (CommandApply.debug)
                    Bukkit.getLogger().info("[PPE] Effect type: " + effect.getEffectType().getName());
                if (effect.getEffectType().equals(effectType)) {
                    getPlayer().removePotionEffect(effect.getEffectType());
                    pEffects.remove(effect);
                    if (!CommandApply.debug)
                        continue;
                    Bukkit.getLogger().info("[PPE] Removed " + effect.toString() + " from " + getPlayerName() + " due to (PARTIAL) removal of " + ce.toString());
                }
            }
            if (pEffects.size() > 0) {
                this.effects.put(ce, pEffects);
            } else {
                this.effects.remove(ce);
            }
        }
    }

    public void removePotionEffect(PotionEffectType effectType, boolean alertChanges) {
        for (Map.Entry<SpecificCustomEnchantment, CopyOnWriteArrayList<AbstractPotionEffect>> data : this.effects.entrySet()) {
            if (data.getValue() != null) {
                CopyOnWriteArrayList<AbstractPotionEffect> pEffects = data.getValue();
                for (AbstractPotionEffect effect : pEffects) {
                    if (effect.getEffectType().equals(effectType)) {
                        if (alertChanges)
                            getPlayer().sendMessage(ChatColor.RED.toString() + ChatColor.BOLD + "[-] " + ChatColor.RED + "" + ((SpecificCustomEnchantment)data.getKey()).getEnchantment().name() + " " + RomanNumber.toRoman(((SpecificCustomEnchantment)data.getKey()).getEnchantmentLevel()) + ":" + ChatColor.GRAY + " removing " + effectType.getName() + " " + RomanNumber.toRoman(effect.getPotionLevel() + 1));
                        pEffects.remove(effect);
                        if (!CommandApply.debug)
                            continue;
                        Bukkit.getLogger().info("[PPE] Removed " + effect.toString() + " of " + ((SpecificCustomEnchantment)data.getKey()).toString() + " from " + getPlayerName() + " due to SPECIFIC untracking.");
                    }
                }
                if (pEffects.size() > 0) {
                    this.effects.put(data.getKey(), pEffects);
                } else {
                    this.effects.remove(data.getKey());
                }
                getPlayer().removePotionEffect(effectType);
            }
        }
    }

    public void removePotionEffect(SpecificCustomEnchantment ce) {
        CopyOnWriteArrayList<AbstractPotionEffect> effects = this.effects.get(ce);
        if (CommandApply.debug)
            Bukkit.getLogger().info("[PPE] removePotionEffect() effects == null? " + ((effects == null) ? 1 : 0));
        if (effects != null) {
            HashSet<PotionEffectType> potionEffectsToNotRemove = new HashSet<>();
            if (effects.size() > 1) {
                HashMap<String, Integer> potionEffectCounts = new HashMap<>();
                for (AbstractPotionEffect effect : effects) {
                    potionEffectCounts.put(effect.toString(), Integer.valueOf(potionEffectCounts.containsKey(effect.toString()) ? (((Integer)potionEffectCounts.get(effect.toString())).intValue() + 1) : 1));
                    if (((Integer)potionEffectCounts.get(effect.toString())).intValue() > 1) {
                        if (CommandApply.debug)
                            Bukkit.getLogger().info("[PPE] removePotionEffect() potionEffectsToNotRemove adding  " + effect.getEffectType().getName());
                        potionEffectsToNotRemove.add(effect.getEffectType());
                    }
                }
            }
            this.effects.remove(ce);
            int highestLevelOfEnchantment = getHighestEnchantmentLevel(ce.getEnchantment());
            if (highestLevelOfEnchantment < ce.getEnchantmentLevel())
                for (AbstractPotionEffect effect : effects) {
                    if (!potionEffectsToNotRemove.contains(effect.getEffectType())) {
                        getPlayer().removePotionEffect(effect.getEffectType());
                        if (!CommandApply.debug)
                            continue;
                        Bukkit.getLogger().info("[PPE] Removed " + effect.toString() + " from " + getPlayerName() + " due to removal of " + ce.toString());
                    }
                }
            if (potionEffectsToNotRemove.size() > 0) {
                for (int i = 0; i < effects.size(); i++) {
                    PotionEffectType effectType = ((AbstractPotionEffect)effects.get(i)).getEffectType();
                    if (potionEffectsToNotRemove.contains(effectType)) {
                        effects.remove(i);
                        potionEffectsToNotRemove.remove(effectType);
                    }
                }
                this.effects.put(new SpecificCustomEnchantment(ce.getEnchantment(), ce.getEnchantmentLevel()), effects);
            }
            if (highestLevelOfEnchantment > 0 || this.effects.containsKey(ce))
                applyPotionEffects(true);
        }
    }

    public void applyPotionEffects(boolean alertChanges) {
        Player p = getPlayer();
        if (p == null)
            return;
        if (p.getLocation().getWorld().getName().equals("events_world")) return;
        if (p.getLocation().getWorld().getName().equals("world_outpost_pluto")) return;

        String regionName = WorldGuardUtils.getRegionName(p.getLocation());
        if (regionName.contains("parkour") && !regionName.contains("enchants"))
            return;
        if ((Enchantments.getInstance()).dungeonsEnabled) {
            DungeonPlayer dp = DungeonsAPI.get().getPlayerDungeonData(p);
            if (dp != null && dp.getCurrentDungeon() != null) {
                if (regionName == null)
                    regionName = WorldGuardUtils.getRegionName(p.getLocation());
                if (regionName.contains("parkour") || regionName.contains("find_corrupt_room") || regionName.contains("maze") || regionName.equalsIgnoreCase("fireball_alley"))
                    return;
            }
        }
        for (Map.Entry<SpecificCustomEnchantment, CopyOnWriteArrayList<AbstractPotionEffect>> data : this.effects.entrySet()) {
            for (AbstractPotionEffect ape : data.getValue()) {
                if (ape.getEffectType() == PotionEffectType.HEALTH_BOOST && MasteryMortalCoil.isAffected(p)) {
                    int mortalCoilLevel = MasteryMortalCoil.getMortalCoilLevel(p);
                    int allowedHealthBoostLevel = ape.getPotionLevel() - mortalCoilLevel + 1;
                    if (allowedHealthBoostLevel <= 0)
                        continue;
                    ape = new AbstractPotionEffect(PotionEffectType.HEALTH_BOOST, allowedHealthBoostLevel);
                }

                if (data.getKey().getEnchantment().getTier() > 6 &&      p.getLocation().getWorld().getName().equals("space")) continue;


                if (!p.hasPotionEffect(ape.getEffectType())) {
                    if (AstroItemSkinsAPI.hasSkinEquipped(p, SkinType.CHRISTMAS_BOOTS_SLIPPERS) && ape.getEffectType().getName().equals("SLOW"))
                        continue;
                    p.addPotionEffect(new PotionEffect(ape.getEffectType(), 2147483647, ape.getPotionLevel(), true));
                    if (!alertChanges)
                        continue;
                    p.sendMessage(ChatColor.GREEN.toString() + ChatColor.BOLD + "[+] " + ChatColor.GREEN + "" + ((SpecificCustomEnchantment)data.getKey()).getEnchantment().name() + " " + RomanNumber.toRoman(((SpecificCustomEnchantment)data.getKey()).getEnchantmentLevel()) + ":" + ChatColor.GRAY + " applying " + ape.getEffectType().getName() + " " + RomanNumber.toRoman(ape.getPotionLevel() + 1));
                    continue;
                }
                for (PotionEffect pe : p.getActivePotionEffects()) {
                    if (!pe.getType().equals(ape.getEffectType()) || ape.getPotionLevel() <= pe.getAmplifier() || pe.getDuration() <= 100000 || (
                            AstroItemSkinsAPI.hasSkinEquipped(p, SkinType.CHRISTMAS_BOOTS_SLIPPERS) && ape.getEffectType().getName().equals("SLOW")))
                        continue;
                    p.removePotionEffect(ape.getEffectType());
                    p.addPotionEffect(new PotionEffect(ape.getEffectType(), 2147483647, ape.getPotionLevel(), true));
                    if (!alertChanges)
                        continue;
                    if (((SpecificCustomEnchantment)data.getKey()).getEnchantment().name().equals("Gears") && ((SpecificCustomEnchantment)data.getKey()).getEnchantmentLevel() == 4)
                        continue;
                    p.sendMessage(ChatColor.GREEN.toString() + ChatColor.BOLD + "[+] " + ChatColor.GREEN + "" + ((SpecificCustomEnchantment)data.getKey()).getEnchantment().name() + " " + RomanNumber.toRoman(((SpecificCustomEnchantment)data.getKey()).getEnchantmentLevel()) + ":" + ChatColor.GRAY + " applying " + ape.getEffectType().getName() + " " + RomanNumber.toRoman(ape.getPotionLevel() + 1));
                }
            }
        }
    }

    public boolean hasPotionEffect(PotionEffectType pet, int level) {
        for (CopyOnWriteArrayList<AbstractPotionEffect> activeEffects : this.effects.values()) {
            for (AbstractPotionEffect ape : activeEffects) {
                if (ape.getEffectType() == pet && ape.getPotionLevel() == level)
                    return true;
            }
        }
        return false;
    }

    private int getHighestEnchantmentLevel(CustomEnchantment ce) {
        int highest = 0;
        for (SpecificCustomEnchantment sce : this.effects.keySet()) {
            if (sce.getEnchantment().equals(ce) && sce.getEnchantmentLevel() > highest)
                highest = sce.getEnchantmentLevel();
        }
        return highest;
    }

    public static PlayerPotionEffects getTrackedPotionEffects(Player p) {
        return PlayerPotionEffectListener.getPlayerPotionEffectTracker().computeIfAbsent(p.getUniqueId(), def -> new PlayerPotionEffects(p));
    }

    public static void trackPotionEffect(Player p, SpecificCustomEnchantment ce, PotionEffect pe) {
        getTrackedPotionEffects(p).addPotionEffect(ce, pe);
    }

    public static void trackPotionEffect(Player p, SpecificCustomEnchantment ce, PotionEffect pe, Consumer<AbstractPotionEffect> addedEffectConsumer) {
        getTrackedPotionEffects(p).addPotionEffect(ce, pe, addedEffectConsumer);
    }

    public static void untrackPotionEffect(Player p, SpecificCustomEnchantment ce) {
        PlayerPotionEffects effects = getTrackedPotionEffects(p);
        if (CommandApply.debug)
            Bukkit.getLogger().info("[PPE] untrackPotionEffect() effects == null? " + ((effects == null) ? 1 : 0));
        if (effects == null)
            return;
        effects.removePotionEffect(ce);
    }

    public static void untrackPotionEffect(Player p, SpecificCustomEnchantment ce, PotionEffectType specificEffect) {
        PlayerPotionEffects effects = getTrackedPotionEffects(p);
        if (effects == null)
            return;
        effects.removePotionEffect(ce, specificEffect);
    }

    public static void untrackPotionEffect(Player p, PotionEffectType specificEffect) {
        PlayerPotionEffects effects = getTrackedPotionEffects(p);
        if (effects == null)
            return;
        effects.removePotionEffect(specificEffect, true);
    }

    public static void applyPotionEffects(Player p, boolean alertChanges) {
        getTrackedPotionEffects(p).applyPotionEffects(alertChanges);
    }

    public static void resetPotionTracker(Player p) {
        (getTrackedPotionEffects(p)).effects.clear();
    }

    public String getPlayerName() {
        return this.playerName;
    }

    public UUID getPlayerUUID() {
        return this.playerUUID;
    }

    public Player getBukkitPlayer() {
        return this.bukkitPlayer;
    }

    public void setBukkitPlayer(Player bukkitPlayer) {
        this.bukkitPlayer = bukkitPlayer;
    }

    public ConcurrentHashMap<SpecificCustomEnchantment, CopyOnWriteArrayList<AbstractPotionEffect>> getEffects() {
        return this.effects;
    }
}
