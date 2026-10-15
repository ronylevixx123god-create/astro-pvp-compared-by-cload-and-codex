/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  org.bukkit.Bukkit
 *  org.bukkit.Material
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.me.astropvp.cratekeys.inventory.ItemStack
 */
package me.ifamasssxd.cosmiccontests.struct.prizes;

import com.google.common.collect.Lists;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Prize {
    private String displayName;
    private Material displayMaterial;
    private String name;
    private List<String> commands;
    private ItemStack giveItem;
    private int slotsNeeded = 0;
    private int rankingRequired;

    public Prize(String displayName, Material material, String name, List<String> commands, ItemStack giveItem, int slotsNeeded, int rankingRequired) {
        this.commands = commands;
        this.displayMaterial = material;
        this.rankingRequired = rankingRequired;
        this.displayName = displayName;
        this.giveItem = giveItem;
        this.slotsNeeded = slotsNeeded;
        this.name = name;
    }

    public Prize(int rankingRequired, String displayName, Material material, String name, String command, ItemStack giveItem, int slotsNeeded) {
        this(displayName, material, name, command == null ? null : Lists.newArrayList(command), giveItem, slotsNeeded, rankingRequired);
    }

    public void giveToPlayer(Player player) {
        if (this.giveItem != null) {
            player.getInventory().addItem(this.giveItem.clone());
        }
        if (this.commands == null) {
            return;
        }
        for (String commands : this.commands) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), commands.replace("{PLAYER}", player.getName()));
        }
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public Material getDisplayMaterial() {
        return this.displayMaterial;
    }

    public String getName() {
        return this.name;
    }

    public List<String> getCommands() {
        return this.commands;
    }

    public ItemStack getGiveItem() {
        return this.giveItem;
    }

    public int getSlotsNeeded() {
        return this.slotsNeeded;
    }

    public int getRankingRequired() {
        return this.rankingRequired;
    }
}

