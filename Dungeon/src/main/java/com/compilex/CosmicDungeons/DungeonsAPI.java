/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.utils.CC
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.utils.ItemUtils
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.utils.NBTWrapper
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.utils.ReflectionUtils
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.utils.WorldUtil
 *  org.arkhamnetwork.Arkkit.patches.chunkgc.ChunkGCTask
 *  org.arkhamnetwork.Arkkit.plugins.ArkPlugins
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.World
 *  org.bukkit.World$Environment
 *  org.bukkit.WorldCreator
 *  org.bukkit.command.CommandSender
 *  org.bukkit.craftbukkit.libs.org.apache.commons.io.FileUtils
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.player.PlayerTeleportEvent$TeleportCause
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 */
package com.compilex.CosmicDungeons;

import com.compilex.CosmicDungeons.CosmicDungeonsPlugin;
import com.compilex.CosmicDungeons.dungeons.libs.Dungeon;
import com.compilex.CosmicDungeons.dungeons.libs.DungeonPlayer;
import com.compilex.CosmicDungeons.dungeons.libs.DungeonType;
import com.compilex.CosmicDungeons.listeners.DungeonPortalCraftListener;
import com.compilex.CosmicDungeons.struct.DungeonDebuff;
import com.compilex.CosmicDungeons.utils.Logger;
import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.ItemUtils;
import com.cosmicpvp.cosmicutils.utils.NBTWrapper;
import com.cosmicpvp.cosmicutils.utils.ReflectionUtils;
import com.cosmicpvp.cosmicutils.utils.WorldUtil;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

public class DungeonsAPI {
    private CopyOnWriteArrayList<Dungeon> dungeons = new CopyOnWriteArrayList();
    private ConcurrentHashMap<UUID, DungeonPlayer> players = new ConcurrentHashMap();
    public int nextDungeonID = 0;
    boolean resetting = false;

    public CopyOnWriteArrayList<Dungeon> getDungeons() {
        return this.dungeons;
    }

    public ConcurrentHashMap<UUID, DungeonPlayer> getPlayers() {
        return this.players;
    }

    public static DungeonsAPI get() {
        return CosmicDungeonsPlugin.get_().getApi();
    }

    public void addDungeon(Dungeon d) {
        if (this.dungeons.contains(d)) {
            Logger.info("Already tracking dungeon: " + d.toString());
            return;
        }
        this.dungeons.add(d);
        d.getDungeonType().setDungeonInstance(d);
        Logger.info("Added dungeon: " + d.toString());
    }

    public Dungeon getDungeonByWorldName(String dungeonWorldName) {
        for (Dungeon d : this.dungeons) {
            if (!d.getWorldName().equals(dungeonWorldName)) continue;
            return d;
        }
        return null;
    }

    public Dungeon getDungeonByName(String dungeonName) {
        for (Dungeon d : this.dungeons) {
            if (!d.getRealDungeonName().equals(dungeonName)) continue;
            return d;
        }
        return null;
    }

    public Dungeon getDungeon(int dungeonID) {
        for (Dungeon d : this.dungeons) {
            if (d.getDungeonID() != dungeonID) continue;
            return d;
        }
        return null;
    }

    public static boolean isDungeonType(ItemStack item) {
        return new NBTWrapper(item).getString("cosmicType").startsWith("dungeon");
    }

    public static void clearDungeonItemsFromInventory(Player player) {
        for (ItemStack item : player.getInventory()) {
            if (item == null || item.getType() == Material.AIR || !DungeonsAPI.isDungeonType(item)) continue;
            player.getInventory().remove(item);
        }
    }

    public static boolean hasDungeonDebuff(Player player, DungeonDebuff debuff) {
        DungeonPlayer dp = DungeonsAPI.get().getPlayerDungeonData(player);
        if (dp == null || dp.getCurrentDungeon() == null) {
            return false;
        }
        return dp.getCurrentDungeon().getDungeonDebuffs().contains((Object)debuff);
    }

    private void tryDeleteWorld(String name) {
        try {
            FileUtils.deleteDirectory((File)new File(name));
        }
        catch (IOException e) {
            Bukkit.getLogger().warning("Failed to delete " + name + " directory in `tryDeleteWorld`, retrying!");
            e.printStackTrace();
            this.tryDeleteWorld(name);
        }
    }

    public void resetDungeonWorldIO(Dungeon dungeon) throws IOException {
        if (this.resetting) {
            return;
        }
        this.resetting = true;
        World w = dungeon.getWorld();
        boolean hasCopied = false;
        while (!hasCopied) {
            Location exit = dungeon.getPortalLocation() != null ? dungeon.getPortalLocation() : Bukkit.getWorld((String)"world").getSpawnLocation();
            for (Player p : w.getPlayers()) {
                p.setMetadata("bypassCombatTeleport", (MetadataValue)new FixedMetadataValue((Plugin)CosmicDungeonsPlugin.get_(), (Object)exit.clone()));
                p.teleport(exit, PlayerTeleportEvent.TeleportCause.UNKNOWN);
            }
            for (Entity e : w.getEntities()) {
                e.remove();
            }
            if (!Bukkit.unloadWorld((World)w, (boolean)false)) {
                Bukkit.getLogger().info("UNABLE TO UNLOAD WORLD!");
            }
            Bukkit.dispatchCommand((CommandSender)Bukkit.getConsoleSender(), (String)("mv unload " + dungeon.getWorldName()));
            try {
                WorldUtil.forceUnloadWorld((World)w, (boolean)false);
            }
            catch (Exception e) {
                System.out.println("failed to force unload world:");
                e.printStackTrace();
                continue;
            }
            this.tryDeleteWorld(dungeon.getWorldName());
            try {
                FileUtils.copyDirectory((File)new File(dungeon.getWorldName() + ".template"), (File)new File(dungeon.getWorldName()));
                hasCopied = true;
            }
            catch (IOException e) {
                Bukkit.getLogger().info("Failed copying world, trying again!");
                e.printStackTrace();
            }
        }
        WorldCreator wc = new WorldCreator(dungeon.getWorldName()).generateStructures(false).generator("VoidGenerator");
        if (dungeon.isDestroyedOutpost()) {
            wc.environment(World.Environment.NETHER);
        }
        w = Bukkit.createWorld((WorldCreator)wc);
        if (dungeon.isDestroyedOutpost()) {
            w.setKeepSpawnInMemory(false);
        }
        w.setFullTime(18000L);
        w.setGameRuleValue("doDaylightCycle", "false");
        w.setGameRuleValue("doMobSpawning", "false");
        w.setGameRuleValue("keepInventory", "true");
        if (dungeon.getDungeonType() == DungeonType.PLANET_NUL) {
            w.setGameRuleValue("randomTickSpeed", "125");
        }
        this.resetting = false;
    }

    public DungeonPlayer addPlayer(Player p) {
        DungeonPlayer found = this.players.get(p.getUniqueId());
        if (found != null) {
            found.setBukkitPlayer(p);
            return found;
        }
        Logger.debug("Added player " + p.getName() + " (" + p.getUniqueId() + ") to dungeon player map.");
        return this.players.put(p.getUniqueId(), new DungeonPlayer(p));
    }

    public void removePlayer(Player p) {
        if (!this.players.containsKey(p.getUniqueId())) {
            Logger.info("removePlayer() Player " + p.getName() + "(" + p.getUniqueId() + ") is not in the dungeon player map!");
            return;
        }
        this.players.remove(p.getUniqueId());
    }

    public DungeonPlayer getPlayerDungeonData(Player p) {
        if (this.players.containsKey(p.getUniqueId())) {
            return this.players.get(p.getUniqueId());
        }
        Logger.info("Player " + p.getName() + " (" + p.getUniqueId() + ") could not be found in dungeon player map!");
        return this.addPlayer(p);
    }

    public ItemStack getEnchantedObsidian() {
        return DungeonPortalCraftListener.enchantedObsidian;
    }

    public ItemStack getDimensionWeb() {
        return DungeonPortalCraftListener.dimensionWeb;
    }

    public ItemStack getCosmonautFuel() {
        return DungeonPortalCraftListener.cosmonautFuel;
    }

    public ItemStack getInterdimensionalCore() {
        return DungeonPortalCraftListener.dimensionalCore;
    }

    public ItemStack getTeleportationCrystal() {
        return DungeonPortalCraftListener.teleporterCrystal;
    }

    public boolean isEnchantedObsidian(ItemStack i) {
        return ItemUtils.areItemsSimilarIgnoringIID((ItemStack)DungeonPortalCraftListener.enchantedObsidian, (ItemStack)i);
    }

    public boolean isDimensionWeb(ItemStack i) {
        return ItemUtils.areItemsSimilarIgnoringIID((ItemStack)DungeonPortalCraftListener.dimensionWeb, (ItemStack)i);
    }

    public boolean isCosmonautFuel(ItemStack i) {
        return ItemUtils.areItemsSimilarIgnoringIID((ItemStack)DungeonPortalCraftListener.cosmonautFuel, (ItemStack)i);
    }

    public boolean isTransportationCrystal(ItemStack i) {
        return ItemUtils.areItemsSimilarIgnoringIID((ItemStack)DungeonPortalCraftListener.teleporterCrystal, (ItemStack)i);
    }

    public boolean isDimensionalCore(ItemStack i) {
        return ItemUtils.areItemsSimilarIgnoringIID((ItemStack)DungeonPortalCraftListener.dimensionalCore, (ItemStack)i);
    }

    public boolean isDungeonPortal(ItemStack i) {
        return i != null && i.getType() == Material.EYE_OF_ENDER && i.hasItemMeta() && i.getItemMeta().hasLore() && ((String)i.getItemMeta().getLore().get(0)).equals((Object)ChatColor.GRAY + "Right-click to create a");
    }

    public Dungeon getDungeonFromPortalItem(ItemStack i) {
        if (this.isDungeonPortal(i)) {
            String displayName = i.getItemMeta().getDisplayName();
            if (displayName.contains("Heroic")) {
                displayName = displayName.replace(CC.translate((String)"&b&k!!!&f&l&nHeroic&5&k!!!&r "), "");
            }
            String dungeonName = displayName.split(": ")[1];
            return DungeonsAPI.get().getDungeonByName(dungeonName);
        }
        return null;
    }

    public boolean isHeroicPortal(ItemStack i) {
        return this.isDungeonPortal(i) && i.getItemMeta().getDisplayName().contains("Heroic");
    }
}

