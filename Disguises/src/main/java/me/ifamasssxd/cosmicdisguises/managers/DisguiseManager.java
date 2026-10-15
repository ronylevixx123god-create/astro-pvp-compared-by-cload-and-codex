/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.compilex.CompilexPermissions.manager.CacheManager
 *  com.cosmicpvp.cosmicutils.CosmicUtils
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.CC
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.TimeUtils
 *  com.google.common.collect.Lists
 *  me.libraryaddict.disguise.DisguiseAPI
 *  me.libraryaddict.disguise.disguisetypes.DisguiseType
 *  me.libraryaddict.disguise.disguisetypes.TargetedDisguise
 *  me.libraryaddict.disguise.utilities.DisguiseUtilities
 *  org.arkhamnetwork.Arkkit.patches.combatlog.CombatLog
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.bukkit.me.astropvp.cratekeys.inventory.ItemStack
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 */
package me.ifamasssxd.cosmicdisguises.managers;

import com.cosmicpvp.cosmicutils.CosmicUtils;
import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.TimeUtils;
import com.google.common.collect.Lists;
import me.ifamasssxd.cosmicdisguises.CosmicDisguises;
import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import me.libraryaddict.disguise.disguisetypes.TargetedDisguise;
import me.libraryaddict.disguise.utilities.DisguiseUtilities;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import org.arkhamnetwork.Arkkit.patches.combatlog.CombatLog;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;

import java.util.*;
import java.util.regex.Pattern;

public class DisguiseManager {
    private final Map<Integer, DisguiseType> disguises = new HashMap<Integer, DisguiseType>(){
        {
            this.put(1, DisguiseType.COW);
            this.put(2, DisguiseType.HORSE);
            this.put(3, DisguiseType.SHEEP);
            this.put(4, DisguiseType.PIG);
            this.put(5, DisguiseType.CREEPER);
            this.put(6, DisguiseType.SKELETON_HORSE);
        }
    };
    private final Map<UUID, Set<DisguiseType>> cachedAvailableTypes = new HashMap<UUID, Set<DisguiseType>>();

    public void onEnable(CosmicDisguises plugin) {
        //This used to be sync
        Bukkit.getScheduler().scheduleAsyncRepeatingTask(plugin, this::clearBannedCombatDisguises, 100L, 100L);
    }

    public void clearBannedCombatDisguises() {
        ArrayList<Player> toRemove = Lists.newArrayList();
        long start = System.currentTimeMillis();
        ArrayList<Player> list = Lists.newArrayList();
        DisguiseUtilities.getDisguises().forEach((id, disguises) -> {
            for (TargetedDisguise disguise : disguises) {
                Player fasterLookup;
                if (disguise.getType() != DisguiseType.SLIME && disguise.getType() != DisguiseType.MAGMA_CUBE && disguise.getType() != DisguiseType.GHAST || (fasterLookup = CosmicUtils.get().getAsyncPlayerMapUUID().get(id)) == null) continue;
                for (Entity ent : fasterLookup.getNearbyEntities(8.0, 8.0, 8.0)) {
                    if (!(ent instanceof Player) || !CombatLog.inCombat((Player) ent)) continue;
                    list.add(fasterLookup);
                }
            }
        });
        toRemove.forEach(player -> {
            player.setMetadata("disguiseCooldown", new FixedMetadataValue(CosmicDisguises.get(), System.currentTimeMillis() + 30000L));
            DisguiseAPI.undisguiseToAll(player);
            player.sendMessage(CC.RedB + "(!) " + CC.Red + "You have been undisguised due to there being a nearby player in combat!");
        });
        if (toRemove.size() > 0) {
            Bukkit.getLogger().info("[CosmicDisguises] Took " + (System.currentTimeMillis() - start) + "ms to remove " + toRemove.size() + " illegal disguises!");
        }
    }

    public String getDisguiseCooldownString(Player player) {
        List values = player.getMetadata("disguiseCooldown");
        if (values == null || values.isEmpty()) {
            return null;
        }
        long expires = ((MetadataValue)values.get(0)).asLong();
        if (expires <= System.currentTimeMillis()) {
            return null;
        }
        long secondsTillExpiration = expires - System.currentTimeMillis();
        if (secondsTillExpiration < 1000L) {
            return "1s";
        }
        return TimeUtils.formatSeconds(secondsTillExpiration / 1000L);
    }

    public DisguiseType getDisguiseAvailable(Player player) {
        int rankTier = this.getRankTier(player);
        return this.disguises.get(rankTier);
    }

    public DisguiseType getDisguiseAvailable(Set<String> permList) {
        int rankTier = this.getRankTier(permList);
        return this.disguises.get(rankTier);
    }

    public boolean isDisabledDisguise(DisguiseType type) {
        return type == DisguiseType.PLAYER || type == DisguiseType.SPLASH_POTION || type == DisguiseType.ARROW || type == DisguiseType.DROPPED_ITEM || type == DisguiseType.WITHER_SKULL || type == DisguiseType.FALLING_BLOCK || type == DisguiseType.THROWN_EXP_BOTTLE || type == DisguiseType.EXPERIENCE_ORB || type == DisguiseType.EGG || type == DisguiseType.ENDER_PEARL || type == DisguiseType.ENDER_SIGNAL || type == DisguiseType.FISHING_HOOK || type == DisguiseType.PAINTING || type == DisguiseType.LEASH_HITCH || type == DisguiseType.ITEM_FRAME || type == DisguiseType.FIREBALL || type == DisguiseType.SMALL_FIREBALL || type == DisguiseType.MINECART_MOB_SPAWNER || type == DisguiseType.MINECART_CHEST || type == DisguiseType.MINECART_COMMAND || type == DisguiseType.MINECART_FURNACE || type == DisguiseType.MINECART || type == DisguiseType.MINECART_TNT || type == DisguiseType.MINECART_HOPPER || type == DisguiseType.FIREWORK || type == DisguiseType.SNOWBALL || type == DisguiseType.ENDER_CRYSTAL || type == DisguiseType.BOAT;
    }

    public Set<DisguiseType> getAvailableDisguises(Player player, boolean force) {
        UUID uuid = player.getUniqueId();
        Set<DisguiseType> set = this.cachedAvailableTypes.get(uuid);
        if (set == null || force) {
            set = new HashSet<DisguiseType>();
            DisguiseType available = this.getDisguiseAvailable(player);
            if (available != null) {
                set.add(available);
            }
            User user = LuckPermsProvider.get().getUserManager().getUser(uuid);
            Set<String> perms = user.getCachedData().getPermissionData().getPermissionMap().keySet();
            for (DisguiseType type : DisguiseType.values()) {
                String perm;
                if (this.isDisabledDisguise(type) || !player.hasPermission(perm = "libsdisguises.disguise." + type.name().toLowerCase()) && (perms == null || !perms.contains(perm))) continue;
                set.add(type);
            }
            this.cachedAvailableTypes.put(uuid, set);
        }
        return set;
    }

    public DisguiseType getDisguiseTypeFromItem(ItemStack item) {
        if (!(item != null && item.getType() == Material.BONE && item.hasItemMeta() && item.getItemMeta().hasDisplayName() && item.getItemMeta().hasLore())) {
            return null;
        }
        if (item.getItemMeta().getDisplayName().startsWith(CC.GreenB + "Player Disguise ") && item.getItemMeta().getDisplayName().endsWith(CC.Gray + "(Right Click)")) {
            String type = ChatColor.stripColor(item.getItemMeta().getDisplayName().split("Disguise")[1].split(Pattern.quote("(Right"))[0]).trim().replace("\"", "").toUpperCase().replace(" ", "_");
            try {
                return DisguiseType.valueOf(type);
            }
            catch (Exception e) {
                Bukkit.getLogger().info("Unable to parse: " + item.getItemMeta().getDisplayName() + " into Disguise: " + type);
            }
        }
        return null;
    }

    public boolean isRedeemableDisguise(ItemStack item) {
        return item != null && item.getType() == Material.BONE && item.hasItemMeta() && item.getItemMeta().hasDisplayName() && item.getItemMeta().hasLore() && item.getItemMeta().getDisplayName().startsWith(CC.GreenB + "Player Disguise ") && item.getItemMeta().getDisplayName().endsWith(CC.Gray + "(Right Click)") && item.getItemMeta().getLore().size() == 2;
    }

    public Set<DisguiseType> getAvailableDisguises(UUID uuid, Set<String> permissions, boolean force) {
        Set<DisguiseType> set = this.cachedAvailableTypes.get(uuid);
        if (set == null || force) {
            set = new HashSet<DisguiseType>();
            DisguiseType available = this.getDisguiseAvailable(permissions);
            if (available != null) {
                set.add(available);
            }
            for (DisguiseType type : DisguiseType.values()) {
                if (this.isDisabledDisguise(type) || !permissions.contains("libsdisguises.disguise." + type.name().toLowerCase())) continue;
                set.add(type);
            }
            this.cachedAvailableTypes.put(uuid, set);
        }
        return set;
    }

    public int getRankTier(Set<String> nodes) {
        if (nodes.contains("cosmicpvp.heroic")) {
            return 6;
        }
        if (nodes.contains("enchantments.max.20")) {
            return 5;
        }
        if (nodes.contains("enchantments.max.12")) {
            return 4;
        }
        if (nodes.contains("enchantments.max.9")) {
            return 3;
        }
        if (nodes.contains("enchantments.max.6")) {
            return 2;
        }
        if (nodes.contains("enchantments.max.3")) {
            return 1;
        }
        return 0;
    }

    public int getRankTier(Player player) {
        if (player.hasPermission("cosmicpvp.heroic")) {
            return 6;
        }
        if (player.hasPermission("enchantments.max.20")) {
            return 5;
        }
        if (player.hasPermission("enchantments.max.12")) {
            return 4;
        }
        if (player.hasPermission("enchantments.max.9")) {
            return 3;
        }
        if (player.hasPermission("enchantments.max.6")) {
            return 2;
        }
        if (player.hasPermission("enchantments.max.3")) {
            return 1;
        }
        return 0;
    }

    public Map<UUID, Set<DisguiseType>> getCachedAvailableTypes() {
        return this.cachedAvailableTypes;
    }
}

