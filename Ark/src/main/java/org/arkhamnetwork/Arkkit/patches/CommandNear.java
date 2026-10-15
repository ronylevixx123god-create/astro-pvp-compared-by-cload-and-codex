/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.earth2me.essentials.CommandSource
 *  com.earth2me.essentials.I18n
 *  com.earth2me.essentials.User
 *  com.earth2me.essentials.me.astropvp.cratekeys.net.astropvp.armorsets.commands.NotEnoughArgumentsException
 *  com.earth2me.essentials.me.astropvp.cratekeys.net.astropvp.armorsets.commands.PlayerNotFoundException
 *  com.earth2me.essentials.me.astropvp.cratekeys.net.astropvp.armorsets.utils.FormatUtil
 *  com.massivecraft.factions.FPlayer
 *  com.massivecraft.factions.FPlayers
 *  com.massivecraft.factions.iface.RelationParticipator
 *  com.massivecraft.factions.net.astropvp.armorsets.struct.Relation
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Location
 *  org.bukkit.OfflinePlayer
 *  org.bukkit.Server
 *  org.bukkit.World
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.metadata.MetadataValue
 */
package org.arkhamnetwork.Arkkit.patches;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.earth2me.essentials.CommandSource;
import com.earth2me.essentials.I18n;
import com.earth2me.essentials.User;
import com.earth2me.essentials.commands.NotEnoughArgumentsException;
import com.earth2me.essentials.commands.PlayerNotFoundException;
import com.earth2me.essentials.utils.FormatUtil;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.perms.Relation;
import me.astropvp.proxypach.ProxyPatch;
import org.arkhamnetwork.Arkkit.libs.AbstractCommand;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

final class CommandNear
        extends AbstractCommand {
    public CommandNear(String command, String usage, String description, List<String> aliases) {
        super(command, usage, description, aliases);
    }

    private static boolean canInteractWith(User interactor, User interactee) {
        if (interactor == null) {
            return !interactee.isHidden();
        }
        if (interactor.equals(interactee)) {
            return true;
        }
        return interactor.getBase().canSee(interactee.getBase());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            try {
                this.lookupOtherPlayer(sender, args, Bukkit.getServer());
            } catch (NotEnoughArgumentsException | PlayerNotFoundException e) {
                if (e instanceof PlayerNotFoundException) {
                    sender.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + "(!) " + ChatColor.RED + args[0] + " is not locally online.");
                }
                e.printStackTrace();
            }
            return true;
        }
        if (!sender.hasPermission("essentials.near")) {
            sender.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD.toString() + "(!) " + ChatColor.RED + "You do not have permission to use /near.");
            sender.sendMessage(ChatColor.GRAY + "Purchase a rank on the server shop to unlock access.");
            return true;
        }
        User user = NearLegacy.ess3.getUser((Player) sender);
        Server server = Bukkit.getServer();
        long maxRadius = NearLegacy.ess3.getSettings().getChatRadius();
        if (maxRadius == 0L) {
            maxRadius = 200L;
        }
        long radius = maxRadius;
        User otherUser = null;
        if (args.length > 0) {
            try {
                radius = Long.parseLong(args[0]);
            } catch (NumberFormatException e) {
                try {
                    otherUser = this.getPlayer(server, user, args, 0);
                } catch (Exception exception) {
                    // empty catch block
                }
            }
            if (args.length > 1 && otherUser != null) {
                try {
                    radius = Long.parseLong(args[1]);
                } catch (NumberFormatException numberFormatException) {
                    // empty catch block
                }
            }
        }

        if ((radius = Math.abs(radius)) > maxRadius && !user.isAuthorized("essentials.near.maxexempt")) {
            NearLegacy.ess3.getI18n();
            user.sendMessage(I18n.tl("radiusTooBig", maxRadius));
            radius = maxRadius;
        }
        if (otherUser == null || !user.isAuthorized("essentials.near.others")) {
            otherUser = user;
        }
        NearLegacy.ess3.getI18n();
        user.sendMessage(I18n.tl("nearbyPlayers", this.getLocal(server, otherUser, radius)));
        return true;
    }

    private void lookupOtherPlayer(CommandSender sender, String[] args, Server server) throws PlayerNotFoundException, NotEnoughArgumentsException {
        if (args.length == 0) {
            return;
        }
        User otherUser = this.getPlayer(server, args, 0, true, false);
        long radius = 200L;
        if (args.length > 1) {
            try {
                radius = Long.parseLong(args[1]);
            } catch (NumberFormatException numberFormatException) {
                // empty catch block
            }
        }
        NearLegacy.ess3.getI18n();
        sender.sendMessage(I18n.tl("nearbyPlayers", this.getLocal(server, otherUser, radius)));
    }

    private String getLocal(Server server, User user, long radius) {
        String string;
        Location loc = user.getLocation();
        World world = loc.getWorld();
        StringBuilder output = new StringBuilder();
        long radiusSquared = radius * radius;
        boolean showHidden = user.canInteractVanished();
        FPlayer fuser = FPlayers.getInstance().getByPlayer(user.getBase().getPlayer());
        int counter = 0;
        for (Player p : Bukkit.getOnlinePlayers()) {
            long delta;
            Location playerLoc;
            User player = NearLegacy.ess3.getUser(p);
            if (player.equals(user) || player.isHidden(user.getBase()) && !showHidden && !user.getBase().canSee(player.getBase()) || (playerLoc = player.getLocation()).getWorld() != world || (delta = (long) playerLoc.distanceSquared(loc)) >= radiusSquared)
                continue;
            if (p.hasMetadata("ghostEnchantment") || p.hasMetadata("heroicGhostEnchantment")) {
                FPlayer fp = FPlayers.getInstance().getByPlayer(p);
                if (fp.getRelationTo(fuser).isAtMost(Relation.NEUTRAL)) {
                    continue;
                }
            }
            if (ProxyPatch.playerNames.contains(p.getName())) continue;
            if (output.length() > 0) {
                output.append(", ");
                counter++;
            }
            output.append(player.getDisplayName()).append("\u00a7f(\u00a74").append((long) Math.sqrt(delta)).append("m\u00a7f)");
        }
        if (counter >= 32) {
            string = CC.translate("&c&l(!)&c There are too many players nearby!");
        } else if (output.length() > 1) {
            string = output.toString();
        } else {
            NearLegacy.ess3.getI18n();
            NearLegacy.ess3.getI18n();
            string = I18n.tl("none");
        }
        return string;
    }

    protected User getPlayer(Server server, CommandSource sender, String[] args, int pos) throws PlayerNotFoundException, NotEnoughArgumentsException {
        if (sender.isPlayer()) {
            User user = NearLegacy.ess3.getUser(sender.getPlayer());
            return this.getPlayer(server, user, args, pos);
        }
        return this.getPlayer(server, args, pos, true, false);
    }

    protected User getPlayer(Server server, CommandSource sender, String searchTerm) throws PlayerNotFoundException, NotEnoughArgumentsException {
        if (sender.isPlayer()) {
            User user = NearLegacy.ess3.getUser(sender.getPlayer());
            return this.getPlayer(server, user, searchTerm, user.canInteractVanished(), false);
        }
        return this.getPlayer(server, searchTerm, true, false);
    }

    protected User getPlayer(Server server, User user, String[] args, int pos) throws PlayerNotFoundException, NotEnoughArgumentsException {
        return this.getPlayer(server, user, args, pos, user.canInteractVanished(), false);
    }

    protected User getPlayer(Server server, String[] args, int pos, boolean getHidden, boolean getOffline) throws PlayerNotFoundException, NotEnoughArgumentsException {
        return this.getPlayer(server, null, args, pos, getHidden, getOffline);
    }

    private User getPlayer(Server server, User sourceUser, String[] args, int pos, boolean getHidden, boolean getOffline) throws PlayerNotFoundException, NotEnoughArgumentsException {
        if (args.length <= pos) {
            throw new NotEnoughArgumentsException();
        }
        if (args[pos].isEmpty()) {
            throw new PlayerNotFoundException();
        }
        return this.getPlayer(server, sourceUser, args[pos], getHidden, getOffline);
    }

    protected User getPlayer(Server server, String searchTerm, boolean getHidden, boolean getOffline) throws PlayerNotFoundException {
        return this.getPlayer(server, null, searchTerm, getHidden, getOffline);
    }

    private User getPlayer(Server server, User sourceUser, String searchTerm, boolean getHidden, boolean getOffline) throws PlayerNotFoundException {
        Player exPlayer;
        try {
            exPlayer = server.getPlayer(UUID.fromString(searchTerm));
        } catch (IllegalArgumentException ex) {
            exPlayer = server.getPlayer(searchTerm);
        }
        User user = exPlayer != null ? NearLegacy.ess3.getUser(exPlayer) : NearLegacy.ess3.getUser(searchTerm);
        if (user != null) {
            if (!getOffline && !user.getBase().isOnline()) {
                throw new PlayerNotFoundException();
            }
            if (getHidden || CommandNear.canInteractWith(sourceUser, user)) {
                return user;
            }
            throw new PlayerNotFoundException();
        }
        List<Player> matches = server.matchPlayer(searchTerm);
        if (matches.isEmpty()) {
            String matchText = searchTerm.toLowerCase(Locale.ENGLISH);
            for (Player p : Bukkit.getOnlinePlayers()) {
                String displayName;
                User userMatch = NearLegacy.ess3.getUser(p);
                if (!getHidden && !CommandNear.canInteractWith(sourceUser, userMatch) || !FormatUtil.stripFormat(userMatch.getName()).toLowerCase(Locale.ENGLISH).contains(matchText))
                    continue;
                return userMatch;
            }
        } else {
            for (Player player : matches) {
                User userMatch = NearLegacy.ess3.getUser(player);
                if (!userMatch.getName().startsWith(searchTerm) || !getHidden && !CommandNear.canInteractWith(sourceUser, userMatch))
                    continue;
                return userMatch;
            }
            User userMatch = NearLegacy.ess3.getUser(matches.get(0));
            if (getHidden || CommandNear.canInteractWith(sourceUser, userMatch)) {
                return userMatch;
            }
        }
        throw new PlayerNotFoundException();
    }

    boolean canInteractWith(CommandSource interactor, User interactee) {
        if (interactor == null) {
            return !interactee.isHidden();
        }
        if (interactor.isPlayer()) {
            return CommandNear.canInteractWith(NearLegacy.ess3.getUser(interactor.getPlayer()), interactee);
        }
        return true;
    }
}

