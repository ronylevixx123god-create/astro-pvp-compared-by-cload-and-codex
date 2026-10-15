/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.cosmicpvp.CosmicSpaceChests.libs.KitItems
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.CC
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.NBTWrapper
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.PlayerUtil
 *  me.ifamasssxd.cosmicevolutionkits.structure.EvolutionKit
 *  me.ifamasssxd.cosmicevolutionkits.structure.EvolutionKits
 *  org.arkhamnetwork.Arkkit.patches.ItemID
 *  org.arkhamnetwork.Arkkit.plugins.ArkPlugins
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.me.astropvp.cratekeys.inventory.ItemStack
 */
package me.cosmicpvp.CosmicGlobalKits.commands;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.NBTWrapper;
import com.cosmicpvp.cosmicutils.utils.PlayerUtil;
import me.cosmicpvp.CosmicGlobalKits.CosmicGlobalKits;
import me.cosmicpvp.CosmicGlobalKits.libs.FallenHero;
import me.cosmicpvp.CosmicGlobalKits.libs.GlobalKits;
import me.cosmicpvp.CosmicGlobalKits.libs.KitItems;
import me.cosmicpvp.CosmicGlobalKits.utils.ItemUtils;
import org.arkhamnetwork.Arkkit.patches.ItemID;
import org.arkhamnetwork.Arkkit.plugins.ArkPlugins;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CommandFallenHero
implements CommandExecutor {
    String gkitString = "";

    public CommandFallenHero() {
        for (GlobalKits gkit : GlobalKits.values()) {
            this.gkitString = this.gkitString + gkit.name() + ", ";
        }
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player p;
        Player p2;
        String playerName;
        if (!sender.isOp()) {
            return true;
        }
        if (args.length == 0) {
            sender.sendMessage("/fh <give>/vheroicbundle <player> <gkitName>");
            sender.sendMessage("gkits: " + this.gkitString);
            return true;
        }
        if (args[0].equalsIgnoreCase("toggle")) {
            if (!CosmicGlobalKits.kitGemsEnabled) {
                CosmicGlobalKits.kitGemsEnabled = true;
                CosmicGlobalKits.getPlugin().getConfig().set("kitGemsEnabled", (Object)true);
                sender.sendMessage("kitGemsEnabled => true");
            } else {
                CosmicGlobalKits.kitGemsEnabled = false;
                CosmicGlobalKits.getPlugin().getConfig().set("kitGemsEnabled", (Object)false);
                sender.sendMessage("kitGemsEnabled => false");
            }
            CosmicGlobalKits.getPlugin().saveConfig();
            return true;
        }
        if (args[0].equalsIgnoreCase("noheroic")) {
            playerName = args[1];
            if (Bukkit.getPlayer((String)playerName) != null) {
                p2 = Bukkit.getPlayer((String)playerName);
                ItemUtils.giveItemToPlayer(p2, FallenHero.getFallenHeroItem(GlobalKits.Loki));
                ItemUtils.giveItemToPlayer(p2, FallenHero.getFallenHeroItem(GlobalKits.Cannoner));
                ItemUtils.giveItemToPlayer(p2, FallenHero.getFallenHeroItem(GlobalKits.Cosmonaut));
                ItemUtils.giveItemToPlayer(p2, FallenHero.getFallenHeroItem(GlobalKits.Paladin));
                ItemUtils.giveItemToPlayer(p2, FallenHero.getFallenHeroItem(GlobalKits.Warlock));
                ItemUtils.giveItemToPlayer(p2, FallenHero.getFallenHeroItem(GlobalKits.Independence));
                ItemUtils.giveItemToPlayer(p2, FallenHero.getFallenHeroItem(GlobalKits.Butcher));
                ItemUtils.giveItemToPlayer(p2, FallenHero.getFallenHeroItem(GlobalKits.Duelist));
            } else {
                sender.sendMessage((Object)ChatColor.RED + "Player '" + playerName + "' not online!");
            }
        }
        if (args[0].equalsIgnoreCase("heroicbundle")) {
            playerName = args[1];
            if (Bukkit.getPlayer((String)playerName) != null) {
                p2 = Bukkit.getPlayer((String)playerName);
                ItemUtils.giveItemToPlayer(p2, CommandFallenHero.getHeroicFallenHeroBundle());
            } else {
                sender.sendMessage((Object)ChatColor.RED + "Player '" + playerName + "' not online!");
            }
            return true;
        }
        if (args[0].equalsIgnoreCase("ghostbundle")) {
            p = Bukkit.getPlayer((String)args[1]);
            if (p != null) {
                ItemUtils.giveItemToPlayer(p, CommandFallenHero.getGhostMasteryKitBundle());
                sender.sendMessage(CC.Red + "Ghost bundle given!");
            } else {
                sender.sendMessage((Object)ChatColor.RED + "Player '" + args[1] + "' not online!");
            }
            return true;
        }
        if (args[0].equalsIgnoreCase("necromancerbundle") || args[0].equalsIgnoreCase("necrobundle")) {
            p = Bukkit.getPlayer((String)args[1]);
            if (p != null) {
                ItemUtils.giveItemToPlayer(p, CommandFallenHero.getNecromancerKitBundle());
                sender.sendMessage(CC.Red + "Necromancer bundle given!");
            } else {
                sender.sendMessage((Object)ChatColor.RED + "Player '" + args[1] + "' not online!");
            }
            return true;
        }
        if (args[0].equalsIgnoreCase("deathknightbundle") || args[0].equalsIgnoreCase("deathbundle")) {
            p = Bukkit.getPlayer((String)args[1]);
            if (p != null) {
                ItemUtils.giveItemToPlayer(p, CommandFallenHero.getDeathKnightBundle());
                sender.sendMessage(CC.Red + "Necromancer bundle given!");
            } else {
                sender.sendMessage((Object)ChatColor.RED + "Player '" + args[1] + "' not online!");
            }
            return true;
        }
        if (args[0].equalsIgnoreCase("vheroicbundle")) {
            ItemStack bundle;
            Player player = Bukkit.getPlayer((String)args[1]);
            if (player != null && !PlayerUtil.giveOrDropProtectedItem((Player)player, (ItemStack)(bundle = CommandFallenHero.getHeroicVKitHeroBundle()), (int)-1)) {
                Bukkit.getLogger().info("[CosmicGlobalKits] Dropping vkit item on ground for " + player.getName() + " due to full me.astropvp.cratekeys.inventory!");
            }
            return true;
        }
        if (args[0].equalsIgnoreCase("give")) {
            playerName = args[1];
            String gkitName = args[2];
            GlobalKits gkit = GlobalKits.valueOf(gkitName);
            if (Bukkit.getPlayer((String)playerName) != null) {
                ItemStack hero;
                Player p3 = Bukkit.getPlayer((String)playerName);
                ItemStack itemStack = hero = args.length == 4 && args[3].equalsIgnoreCase("gem") ? KitItems.getKitItem((GlobalKits)gkit) : FallenHero.getFallenHeroItem(gkit);
                if (ArkPlugins.ITEM_ID.getArkPlugin().isRunning()) {
                    hero = ItemID.setItemID((ItemStack)hero, (long)ItemID.generateNewItemID());
                }
                ItemUtils.giveItemToPlayer(p3, hero);
            } else {
                sender.sendMessage((Object)ChatColor.RED + "Player '" + playerName + "' not online!");
            }
            return true;
        }

        return true;
    }

    public static ItemStack getHeroicVKitHeroBundle() {
        return new NBTWrapper(ItemUtils.buildItem(Material.CHEST, (short)0, (Object)ChatColor.DARK_GREEN + "" + (Object)ChatColor.BOLD + "Evolution Kit Fallen Hero Bundle", (Object)ChatColor.GRAY + "Right-click to reveal " + (Object)ChatColor.UNDERLINE + "4 random", (Object)ChatColor.GRAY + "evolution kit fallen hero me.astropvp.cratekeys.items!", "", (Object)ChatColor.GOLD + "Contains 4 of the following...", (Object)ChatColor.GRAY + "* " + CC.DarkPurpleB + "Phoenix", (Object)ChatColor.GRAY + "* " + CC.DarkAquaB + "Troll", (Object)ChatColor.GRAY + "* " + CC.GoldB + "Alchemist", (Object)ChatColor.GRAY + "* " + CC.DarkGreenB + "Ogre", (Object)ChatColor.GRAY + "* " + CC.DarkRedB + "Slayer", (Object)ChatColor.GRAY + "* " + CC.DarkGrayB + "Judgement", (Object)ChatColor.GRAY + "* " + CC.YellowB + "Mimic")).setString("cosmicType", "vkitFHBundle").build();
    }

    public static ItemStack getGhostMasteryKitBundle() {
        return new NBTWrapper(ItemUtils.buildItem(Material.CHEST, (short)0, (Object)ChatColor.DARK_AQUA + "" + (Object)ChatColor.BOLD + "Ghost Mastery Kit Bundle", (Object)ChatColor.GRAY + "Right-click to reveal the", CC.Gray + "following Fallen Heroes!", "", (Object)ChatColor.GOLD + "Contains the following...", CC.Gray + "* 5x " + GlobalKits.Spooky.getItemName(), CC.Gray + "* 5x " + GlobalKits.Butcher.getItemName(), CC.Gray + "* 5x " + GlobalKits.GrimReaper.getItemName())).setString("cosmicType", "ghostMasteryBundle").build();
    }

    public static ItemStack getNecromancerKitBundle() {
        return new NBTWrapper(ItemUtils.buildItem(Material.CHEST, (short)0, (Object)ChatColor.DARK_GREEN + "" + (Object)ChatColor.BOLD + "Necromancer Kit Bundle", (Object)ChatColor.GRAY + "Right-click to reveal the", CC.Gray + "following Fallen Heroes!", "", (Object)ChatColor.GOLD + "Contains the following...", CC.Gray + "* 5x " + GlobalKits.DIABLO.getItemName(), CC.Gray + "* 5x " + GlobalKits.Voidwalker.getItemName(), CC.Gray + "* 5x " + GlobalKits.Warlock.getItemName())).setString("cosmicType", "necromancerMasteryBundle").build();
    }

    public static ItemStack getDeathKnightBundle() {
        return new NBTWrapper(ItemUtils.buildItem(Material.CHEST, (short)0, (Object)ChatColor.BLUE + "" + (Object)ChatColor.BOLD + "Death Knight Kit Bundle", (Object)ChatColor.GRAY + "Right-click to reveal the", CC.Gray + "following Fallen Heroes!", "", (Object)ChatColor.GOLD + "Contains the following...", CC.Gray + "* 5x " + GlobalKits.GLADIATOR.getItemName(), CC.Gray + "* 5x " + GlobalKits.Loki.getItemName(), CC.Gray + "* 5x " + GlobalKits.TEMPLAR.getItemName())).setString("cosmicType", "deathKnightMasteryBundle").build();
    }

    public static ItemStack getHeroicFallenHeroBundle() {
        return ItemUtils.buildItem(Material.CHEST, (short)0, (Object)ChatColor.GOLD + "" + (Object)ChatColor.BOLD + "Heroic Fallen Hero Bundle", (Object)ChatColor.GRAY + "Right-click to reveal " + (Object)ChatColor.UNDERLINE + "10 random", (Object)ChatColor.GRAY + "heroic fallen hero me.astropvp.cratekeys.items!", "", (Object)ChatColor.GOLD + "Contains 10 of the following...", (Object)ChatColor.GRAY + "* Heroic Butcher", (Object)ChatColor.GRAY + "* Heroic Warlock", (Object)ChatColor.GRAY + "* Heroic Independence", (Object)ChatColor.GRAY + "* Heroic Paladin", (Object)ChatColor.GRAY + "* Heroic Spooky", (Object)ChatColor.GRAY + "* Heroic Hyperdrive", (Object)ChatColor.GRAY + "* Heroic Destroyer", (Object)ChatColor.GRAY + "* Heroic Guardian", (Object)ChatColor.GRAY + "* Heroic Diablo", (Object)ChatColor.GRAY + "* Heroic Valentine", (Object)ChatColor.GRAY + "* Heroic Arcane", (Object)ChatColor.GRAY + "* Heroic Gladiator", (Object)ChatColor.GRAY + "* Heroic Trickster", (Object)ChatColor.GRAY + "* Heroic Templar", (Object)ChatColor.GRAY + "* Heroic Viking", (Object)ChatColor.GRAY + "* Heroic Tinkermaster", (Object)ChatColor.GRAY + "* Heroic Admiral", (Object)ChatColor.GRAY + "* Heroic Party Animal", (Object)ChatColor.GRAY + "* Heroic Enchantment", (Object)ChatColor.GRAY + "* Heroic Grand Master", (Object)ChatColor.GRAY + "* Heroic Void Walker", (Object)ChatColor.GRAY + "* Heroic Master Builder");
    }
}

