/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandSender
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.block.BlockPhysicsEvent
 *  org.bukkit.event.player.PlayerBucketEmptyEvent
 */
package org.arkhamnetwork.Arkkit.patches;

import org.arkhamnetwork.Arkkit.libs.AbstractCommand;
import org.arkhamnetwork.Arkkit.plugins.ArkPlugins;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;

final class CommandToggleLogic
        extends AbstractCommand {
    protected boolean blockPhysicsDisabled = false;
    protected boolean blockBuckets = false;

    public CommandToggleLogic(String command, String usage, String description) {
        super(command, usage, description);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.isOp()) {
            return true;
        }
        if (args.length == 1 && args[0].equalsIgnoreCase("bucket")) {
            this.blockBuckets = !this.blockBuckets;
            sender.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "blockBuckets => " + this.blockBuckets);
            return true;
        }
        this.blockPhysicsDisabled = !this.blockPhysicsDisabled;
        sender.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "blockPhysicsDisabled => " + this.blockPhysicsDisabled);
        return true;
    }

    final class CommandToggleLogicListener
            implements Listener {
        CommandToggleLogicListener() {
        }

        @EventHandler(priority = EventPriority.HIGHEST)
        public void onBlockPhysics(BlockPhysicsEvent e) {
            if (CommandToggleLogic.this.blockPhysicsDisabled) {
                e.setCancelled(true);
            }
        }

        @EventHandler(priority = EventPriority.HIGHEST)
        public void onBucketEmpty(PlayerBucketEmptyEvent e) {
            if (CommandToggleLogic.this.blockBuckets) {
                Bukkit.getLogger().info("[Ypperin] bucketEmptyEvent(" + e.getPlayer() + ", " + e.getBlockClicked().getLocation() + "," + e.getItemStack() + ")");
            }
        }
    }
}

