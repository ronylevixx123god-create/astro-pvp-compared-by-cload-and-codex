/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  me.cosmicpvp.CosmicGlobalKits.libs.GlobalKits
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.Inventory
 *  org.bukkit.inventory.InventoryHolder
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.plugin.Plugin
 */
package me.ifamasssxd.cosmicduel.struct;

import com.google.common.collect.Lists;
import me.ifamasssxd.cosmicduel.utils.ItemBuilder;
import me.ifamasssxd.cosmicduel.utils.KitUtils;
import me.ifamasssxd.cosmicduel.utils.Serializer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

public class DuelPlayer {
    public boolean loading = false;
    private static Map<UUID, DuelPlayer> duelPlayers = new HashMap<UUID, DuelPlayer>();
    private String name;
    private UUID uuid;
    private boolean pendingDeletion = false;
    private static final String KIT_SEPERATOR = "$";
    private static final String KIT_TYPE_SEPERATOR = ":";
    private static final String CUSTOM_KIT_SEPERATOR = "#";

    public DuelPlayer(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;
        duelPlayers.put(uuid, this);
    }





    public static DuelPlayer getDuelPlayer(UUID uuid) {
        return duelPlayers.get(uuid);
    }

    public static DuelPlayer getDuelPlayer(Player player) {
        return duelPlayers.get(player.getUniqueId());
    }

    public static void removeDuelPlayer(Player player) {
        duelPlayers.remove(player.getUniqueId());
    }

    public static void setDuelPlayers(Map<UUID, DuelPlayer> duelPlayers) {
        DuelPlayer.duelPlayers = duelPlayers;
    }

    public String getName() {
        return this.name;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public boolean isPendingDeletion() {
        return this.pendingDeletion;
    }

    public void setPendingDeletion(boolean pendingDeletion) {
        this.pendingDeletion = pendingDeletion;
    }
}

