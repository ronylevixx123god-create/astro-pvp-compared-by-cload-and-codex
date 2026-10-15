/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  com.massivecraft.factions.FPlayer
 *  com.massivecraft.factions.FPlayers
 *  com.massivecraft.factions.iface.RelationParticipator
 *  com.massivecraft.factions.net.astropvp.armorsets.struct.Relation
 *  org.apache.commons.lang.StringUtils
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.OfflinePlayer
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.entity.PlayerDeathEvent
 *  org.bukkit.me.astropvp.cratekeys.inventory.ItemStack
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 */
package org.arkhamnetwork.Arkkit.patches;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.google.common.collect.Lists;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.perms.Relation;
import org.apache.commons.lang.StringUtils;
import org.arkhamnetwork.Arkkit.Arkkit;
import org.arkhamnetwork.Arkkit.libs.AbstractCommand;
import org.arkhamnetwork.Arkkit.libs.JSONMessage;
import org.arkhamnetwork.Arkkit.plugins.ArkPlugin;
import org.arkhamnetwork.Arkkit.utils.LocationUtils;
import org.arkhamnetwork.Arkkit.utils.WorldGuardUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;

public class DeathMessages
        extends ArkPlugin {
    public DeathMessages(Plugin p) {
        super(p);
    }

    @Override
    public void enable() {
        this.registerListener(new DeathMessageListener());
        this.registerCommand(new CommandToggleDeathMessages());
    }

    @Override
    public void disable() {
        this.unregisterListeners();
        this.unregisterCommands();
    }

    class CommandToggleDeathMessages
            extends AbstractCommand {
        public CommandToggleDeathMessages() {
            super("toggledeathmessages", "/<command>", "Death message toggle.", Lists.newArrayList("toggledeathmessage"));
        }

        @Override
        public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
            Player pl = (Player) sender;
            if (pl.hasMetadata("deathMessageToggle")) {
                pl.removeMetadata("deathMessageToggle", Arkkit.get());
                pl.sendMessage(ChatColor.YELLOW + "Death Messages have been " + ChatColor.GREEN + ChatColor.BOLD + "ENABLED");
            } else {
                pl.setMetadata("deathMessageToggle", new FixedMetadataValue(Arkkit.get(), ""));
                pl.sendMessage(ChatColor.YELLOW + "Death Messages have been " + ChatColor.RED + ChatColor.BOLD + "DISABLED");
            }
            return false;
        }
    }

    class DeathMessageListener
            implements Listener {
        DeathMessageListener() {
        }

        @EventHandler(priority = EventPriority.LOW)
        public void onPlayerDeath(PlayerDeathEvent event) {
            Player killer = event.getEntity().getKiller();
            if (killer != null) {
                if (event.getDeathMessage() != null && event.getDeathMessage().equals("customdeath")) {
                    return;
                }
                event.setDeathMessage(null);
                FPlayer player = FPlayers.getInstance().getByPlayer(killer);
                FPlayer deadFPlayer = FPlayers.getInstance().getByPlayer(event.getEntity());
                Bukkit.getLogger().info("[Arkkit (DeathMessages)] Player " + killer.getName() + " killed " + event.getEntity().getName() + " at " + LocationUtils.convertLocationToString(event.getEntity().getLocation()));
                for (Entity e : event.getEntity().getNearbyEntities(64.0, 64.0, 64.0)) {
                    Player nearbyPlayer;
                    if (!(e instanceof Player) || WorldGuardUtils.isPvPDisabled((nearbyPlayer = (Player) e).getLocation()) || nearbyPlayer.hasMetadata("deathMessageToggle"))
                        continue;
                    FPlayer foundFPlayer = FPlayers.getInstance().getByPlayer(nearbyPlayer);
                    this.sendMessage(player, foundFPlayer, deadFPlayer, killer, nearbyPlayer);
                }
                this.sendMessage(player, deadFPlayer, deadFPlayer, killer, event.getEntity());
            }
        }

        private void sendMessage(FPlayer player, FPlayer nearbyFPlayer, FPlayer deadFPlayer, Player killer, Player toReceive) {
            JSONMessage message = new JSONMessage("", ChatColor.GRAY);
            String playerName = player.getPlayer().getName();
            String deadName = deadFPlayer.getPlayer().getName();
            String killerTag = player.getChatTag(nearbyFPlayer);
            String deadTag = deadFPlayer.getChatTag(nearbyFPlayer);
            Relation deadRelation = nearbyFPlayer.getRelationTo(deadFPlayer);
            ChatColor prefix = deadRelation == Relation.ENEMY ? ChatColor.GREEN : (deadRelation == Relation.NEUTRAL ? ChatColor.GRAY : (deadRelation == Relation.ALLY || deadRelation == Relation.MEMBER ? ChatColor.RED : ChatColor.GREEN));
            message.addText(prefix.toString() + ChatColor.BOLD + "\u2731" + ChatColor.GRAY + " ");
            message.addText(ChatColor.WHITE.toString() + (killerTag.isEmpty() ? "" : killerTag + " ") + player.getRelationTo(nearbyFPlayer).getColor() + playerName + ChatColor.GRAY + " killed " + ChatColor.WHITE + (deadTag.isEmpty() ? "" : deadTag + " ") + deadFPlayer.getRelationTo(nearbyFPlayer).getColor() + deadName + ChatColor.GRAY + " with ");
            if (killer.getItemInHand() == null || killer.getItemInHand().getType() == Material.AIR) {
                message.addText(this.getFormattedName(null));
            } else {
                message.addItem(killer.getItemInHand(), ItemLinking.formatColors(ChatColor.WHITE + this.getFormattedName(killer.getItemInHand())).getText());
            }
            message.sendToPlayer(toReceive);
        }

        private String getFormattedName(ItemStack hand) {
            if (hand == null || hand.getType() == Material.AIR) {
                return "their fists!";
            }
            if (hand.hasItemMeta() && hand.getItemMeta().hasDisplayName()) {
                return hand.getItemMeta().getDisplayName();
            }
            if (hand.getType() == Material.TNT) {
                return "TNT";
            }
            return StringUtils.capitaliseAllWords(hand.getType().name().toLowerCase().replace("_", " "));
        }
    }
}

