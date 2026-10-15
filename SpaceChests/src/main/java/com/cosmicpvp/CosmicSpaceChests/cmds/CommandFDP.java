/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.CC
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.NumberUtils
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.TimeUtils
 *  me.ifamasssxd.cosmicevolutionkits.structure.EvolutionKits
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Location
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.me.astropvp.cratekeys.inventory.ItemStack
 */
package com.cosmicpvp.CosmicSpaceChests.cmds;

import com.cosmicpvp.CosmicSpaceChests.CosmicSpaceChestsAPI;
import com.cosmicpvp.CosmicSpaceChests.CosmicSpaceChestsPlugin;
import com.cosmicpvp.CosmicSpaceChests.generators.LegendaryGenerator;
import com.cosmicpvp.CosmicSpaceChests.libs.ItemRarity;
import com.cosmicpvp.CosmicSpaceChests.libs.RankItems;
import com.cosmicpvp.CosmicSpaceChests.tasks.CosmicEnvoyTask;
import com.cosmicpvp.CosmicSpaceChests.utils.ItemUtils;
import com.cosmicpvp.CosmicSpaceChests.utils.LocationUtils;
import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.NumberUtils;
import com.cosmicpvp.cosmicutils.utils.TimeUtils;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CommandFDP
implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("fdp")) {
            Player pl;
            if (!sender.isOp()) {
                return true;
            }
            Player player = pl = sender instanceof Player ? (Player)sender : null;
            if (args[0].equalsIgnoreCase("chest")) {
                int i;
                int n = i = args.length == 2 ? Integer.parseInt(args[1]) : 1;
                if (i > 100) {
                    i = 100;
                }
                int passed = 0;
                for (int x = 0; x < i; ++x) {
                    Location loc = LocationUtils.getNearbyLocation(pl.getLocation(), 4, 12, 25);
                    if (loc == null) continue;
                    ++passed;
                    CosmicSpaceChestsAPI.dropChest(loc);
                }
                pl.sendMessage((Object)ChatColor.YELLOW + "Dropping " + passed + " / " + i + " loot chests, LOOK UP!");
            }
            if (args[0].equalsIgnoreCase("task")) {
                new CosmicEnvoyTask(CosmicSpaceChestsPlugin.plugin).run();
                return true;
            }
            if (args[0].equalsIgnoreCase("starthween")) {
                Integer parsed = args.length == 2 ? NumberUtils.parseIntOrNull((String)args[1]) : 901;
                if (parsed == null) {
                    parsed = 901;
                }
                sender.sendMessage(CC.Red + "Starting next pumpkin event in " + TimeUtils.formatSeconds((long)parsed.intValue()));
                return true;
            }
            if (args[0].equalsIgnoreCase("christmas")) {
                Integer parsed = args.length == 2 ? NumberUtils.parseIntOrNull((String)args[1]) : 901;
                if (parsed == null) {
                    parsed = 901;
                }
                sender.sendMessage(CC.Red + "Starting next pumpkin event in " + TimeUtils.formatSeconds((long)parsed.intValue()));
                return true;
            }
            if (args[0].equalsIgnoreCase("0")) {
                pl.getInventory().clear();
            }
            if (args[0].equalsIgnoreCase("1")) {
                ItemStack[] isl;
                for (ItemStack is : isl = CosmicSpaceChestsAPI.getItems(ItemRarity.SIMPLE, CosmicSpaceChestsAPI.getEmptyInventorySlots(pl))) {
                    pl.getInventory().addItem(new ItemStack[]{is});
                }
            }
            if (args[0].equalsIgnoreCase("2")) {
                ItemStack[] isl;
                for (ItemStack is : isl = CosmicSpaceChestsAPI.getItems(ItemRarity.UNIQUE, CosmicSpaceChestsAPI.getEmptyInventorySlots(pl))) {
                    pl.getInventory().addItem(new ItemStack[]{is});
                }
            }
            if (args[0].equalsIgnoreCase("3")) {
                ItemStack[] isl;
                for (ItemStack is : isl = CosmicSpaceChestsAPI.getItems(ItemRarity.ELITE, CosmicSpaceChestsAPI.getEmptyInventorySlots(pl))) {
                    pl.getInventory().addItem(new ItemStack[]{is});
                }
            }
            if (args[0].equalsIgnoreCase("4")) {
                ItemStack[] isl;
                for (ItemStack is : isl = CosmicSpaceChestsAPI.getItems(ItemRarity.ULTIMATE, CosmicSpaceChestsAPI.getEmptyInventorySlots(pl))) {
                    pl.getInventory().addItem(new ItemStack[]{is});
                }
            }
            if (args[0].equalsIgnoreCase("5")) {
                ItemStack[] isl;
                for (ItemStack is : isl = CosmicSpaceChestsAPI.getItems(ItemRarity.LEGENDARY, CosmicSpaceChestsAPI.getEmptyInventorySlots(pl))) {
                    pl.getInventory().addItem(new ItemStack[]{is});
                }
            }

            if (args[0].equalsIgnoreCase("legend")) {
                ItemUtils.giveItemToPlayer(pl, LegendaryGenerator.getRandomLegendary());
            }
            if (args[0].equalsIgnoreCase("ranknote")) {
                String givenArg = args[1];
                UUID givenUUID = null;
                if (givenArg.contains("-")) {
                    try {
                        givenUUID = UUID.fromString(givenArg);
                    }
                    catch (Exception e) {
                        Bukkit.getLogger().info("Unable to parse uuid from: " + givenArg);
                    }
                }
                ItemUtils.giveItemToPlayer(givenUUID != null ? Bukkit.getPlayer((UUID)givenUUID) : Bukkit.getPlayer((String)givenArg), RankItems.getRankItem(args[2]));
            }
        }
        return true;
    }
}

