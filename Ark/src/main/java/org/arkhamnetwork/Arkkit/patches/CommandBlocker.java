/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.plugin.Plugin
 */
package org.arkhamnetwork.Arkkit.patches;

import org.arkhamnetwork.Arkkit.Arkkit;
import org.arkhamnetwork.Arkkit.libs.AbstractCommand;
import org.arkhamnetwork.Arkkit.plugins.ArkPlugin;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class CommandBlocker
        extends ArkPlugin {
    private static final List<String> blocked_commands = new ArrayList<String>();
    private static final List<String> blocked_aliases = new ArrayList<String>();
    private static boolean initialized = false;

    public CommandBlocker(Plugin p) {
        super(p);
    }

    protected static boolean isBlockedCommand(String cmd) {
        return blocked_commands.contains(cmd) || blocked_commands.contains(cmd.toLowerCase()) || blocked_commands.contains("/" + cmd.toLowerCase()) || blocked_commands.contains("/" + cmd) || blocked_aliases.contains(cmd) || blocked_aliases.contains(cmd.toLowerCase()) || blocked_aliases.contains("/" + cmd.toLowerCase()) || blocked_aliases.contains("/" + cmd);
    }

    protected static boolean blockCommand(String cmd) {
        if (blocked_commands.contains(cmd)) {
            return false;
        }
        blocked_commands.add(cmd);
        try {
            if (AbstractCommand.cmap.getCommand(cmd) != null && AbstractCommand.cmap.getCommand(cmd).getAliases() != null && AbstractCommand.cmap.getCommand(cmd).getAliases().size() > 0) {
                for (String alias : AbstractCommand.cmap.getCommand(cmd).getAliases()) {
                    if (blocked_aliases.contains(alias)) continue;
                    Bukkit.getLogger().info("(CommandBlocker) Added alias of command, '" + alias + "', to blocked net.astropvp.armorsets.commands list.");
                    blocked_aliases.add(alias);
                }
            }
        } catch (NullPointerException npe) {
            Bukkit.getLogger().info("(CommandBlocker) No command '" + cmd + "' registered, could not lookup aliases.");
        }
        if (initialized && blocked_commands.size() > 0) {
            Arkkit.get().getConfig().set("settings.blocked_commands", blocked_commands);
        }
        return true;
    }

    protected static boolean unblockCommand(String cmd) {
        if (!blocked_commands.contains(cmd) && !blocked_aliases.contains(cmd)) {
            Bukkit.getLogger().info("(CommandBlocker) Failed to unblock cmd " + cmd + " because it was not found in blocked_commands map!");
            return false;
        }
        blocked_commands.remove(cmd);
        blocked_aliases.remove(cmd);
        try {
            if (AbstractCommand.cmap.getCommand(cmd) != null && AbstractCommand.cmap.getCommand(cmd).getAliases() != null && AbstractCommand.cmap.getCommand(cmd).getAliases().size() > 0) {
                for (String alias : AbstractCommand.cmap.getCommand(cmd).getAliases()) {
                    if (!blocked_aliases.contains(alias)) continue;
                    Bukkit.getLogger().info("(CommandBlocker) Removed alias of command, '" + alias + "', to blocked net.astropvp.armorsets.commands list.");
                    blocked_aliases.remove(alias);
                }
            }
        } catch (NullPointerException npe) {
            Bukkit.getLogger().info("(CommandBlocker) No command '" + cmd + "' registered, could not lookup aliases for unblock.");
        }
        if (initialized) {
            Arkkit.get().getConfig().set("settings.blocked_commands", blocked_commands);
        }
        return true;
    }

    protected static String[] getBlockedCommands() {
        return blocked_commands.toArray(new String[blocked_commands.size()]);
    }

    protected static String[] getBlockedAliases() {
        return blocked_aliases.toArray(new String[blocked_aliases.size()]);
    }

    @Override
    public void enable() {
        if (Arkkit.get().getConfig().contains("settings.blocked_commands")) {
            for (String s : Arkkit.get().getConfig().getStringList("settings.blocked_commands")) {
                CommandBlocker.blockCommand(s);
                Bukkit.getLogger().info("(CommandBlocker) Added command '" + s + "' to blocked net.astropvp.armorsets.commands list.");
            }
        }
        initialized = true;
        if (blocked_commands.size() == 0) {
            Bukkit.getLogger().info("(CommandBlocker) blocked_commands.size()==0? Auto-populating block list.");
            CommandBlocker.blockCommand("me");
            CommandBlocker.blockCommand("eback");
            CommandBlocker.blockCommand("version");
            CommandBlocker.blockCommand("plugins");
            CommandBlocker.blockCommand("ehome");
            CommandBlocker.blockCommand("eat");
            CommandBlocker.blockCommand("about");
            CommandBlocker.blockCommand("enchantapi");
            CommandBlocker.blockCommand("random");
            CommandBlocker.blockCommand("pvdel");
            CommandBlocker.blockCommand("hat");
            CommandBlocker.blockCommand("etpo");
            CommandBlocker.blockCommand("versions");
            CommandBlocker.blockCommand("mkick");
            CommandBlocker.blockCommand("kick");
            CommandBlocker.blockCommand("ecall");
            CommandBlocker.blockCommand("f access p woofless yes");
            CommandBlocker.blockCommand("f access f flowerking yes");
            CommandBlocker.blockCommand("auctionhouse");
            CommandBlocker.blockCommand("bounty add ypperin");
            CommandBlocker.blockCommand("f invite ypperin");
            CommandBlocker.blockCommand("trade ypperin");
            CommandBlocker.blockCommand("msg ypperin");
            CommandBlocker.blockCommand("m ypperin");
            CommandBlocker.blockCommand("w ypperin");
            CommandBlocker.blockCommand("pay ypperin");
            CommandBlocker.blockCommand("duel ypperin");
            CommandBlocker.blockCommand("auctionhouse sell");
            CommandBlocker.blockCommand("createtitle");
            CommandBlocker.blockCommand("party teleport");
            CommandBlocker.blockCommand("buy");
            CommandBlocker.blockCommand("fly");
            CommandBlocker.blockCommand("emsg xsSlayer3");
            CommandBlocker.blockCommand("emsg ypperin");
            CommandBlocker.blockCommand("playtime ypperin");
            CommandBlocker.blockCommand("playtime Its_");
            CommandBlocker.blockCommand("playtime Koeru");
            CommandBlocker.blockCommand("feed *");
            CommandBlocker.blockCommand("ver");
            CommandBlocker.blockCommand("calc");
            CommandBlocker.blockCommand("eval");
            CommandBlocker.blockCommand("calculate");
            CommandBlocker.blockCommand("evaluate");
            CommandBlocker.blockCommand("solve");
            CommandBlocker.blockCommand("seen ypperin");
            CommandBlocker.blockCommand("bec");
        }
        this.registerListener(new CommandBlockerListener());
        this.registerCommand(new CommandBlockCommand("blockcommand", "/<command> [opperation] [args]", "Block or unblock given command."));
        this.registerCommand(new CommandNull("null", "/<command>", "Nothing."));
    }

    @Override
    public void disable() {
        if (blocked_commands.size() > 0) {
            Bukkit.getLogger().info("[CommandBlocker] Saving " + blocked_commands.size() + " blocked net.astropvp.armorsets.commands to file... " + blocked_commands);
            Arkkit.get().getConfig().set("settings.blocked_commands", new ArrayList<String>(blocked_commands));
        }
        blocked_commands.clear();
        blocked_aliases.clear();
        initialized = false;
    }
}

