/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.earth2me.essentials.CommandSource
 *  com.earth2me.essentials.User
 *  com.earth2me.essentials.me.astropvp.cratekeys.net.astropvp.armorsets.commands.NotEnoughArgumentsException
 *  com.earth2me.essentials.me.astropvp.cratekeys.net.astropvp.armorsets.commands.PlayerNotFoundException
 *  com.earth2me.essentials.me.astropvp.cratekeys.net.astropvp.armorsets.utils.FormatUtil
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Server
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 */
package org.arkhamnetwork.Arkkit.patches;

import com.earth2me.essentials.CommandSource;
import com.earth2me.essentials.User;
import com.earth2me.essentials.commands.NotEnoughArgumentsException;
import com.earth2me.essentials.commands.PlayerNotFoundException;
import com.earth2me.essentials.utils.FormatUtil;
import org.arkhamnetwork.Arkkit.libs.AbstractCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

final class CommandBalance
        extends AbstractCommand {
    DecimalFormat formatter = new DecimalFormat("#,###.00");

    public CommandBalance(String command, String usage, String description, List<String> aliases) {
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
            this.doBalanceOther(args, Bukkit.getServer(), sender);
            return true;
        }
        User user = Balance.ess3.getUser((Player) sender);
        Server server = Bukkit.getServer();
        if (args.length == 1 && user.isAuthorized("essentials.balance.others")) {
            User target = null;
            try {
                target = this.getPlayer(server, args, 0, true, true);
            } catch (NotEnoughArgumentsException | PlayerNotFoundException e) {
                if (e instanceof PlayerNotFoundException) {
                    sender.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + "(!) " + ChatColor.RED + args[0] + " is not locally online.");
                } else {
                    e.printStackTrace();
                }
                return true;
            }
            BigDecimal bal = target.getMoney();
            sender.sendMessage(ChatColor.YELLOW.toString() + ChatColor.BOLD + target.getName() + "'s Balance: " + ChatColor.WHITE + "$" + this.formatMoney(target.getMoney()));
            if (user.getMoney().compareTo(target.getMoney()) > 0) {
                sender.sendMessage(ChatColor.GRAY + "You're richer than them!");
            } else if (user.getMoney().compareTo(target.getMoney()) == 0) {
                sender.sendMessage(ChatColor.GRAY + "They're just as rich as you!");
            } else {
                sender.sendMessage(ChatColor.GRAY + "They're richer than you!");
            }
        } else if (args.length < 2) {
            BigDecimal bal = user.getMoney();
            user.sendMessage(ChatColor.YELLOW.toString() + ChatColor.BOLD + "Your Balance: " + ChatColor.WHITE + "$" + this.formatMoney(bal));
            user.sendMessage(ChatColor.GRAY + "Use /withdraw or /pay to transfer funds.");
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        ArrayList<String> results = new ArrayList<String>();
        if (args.length != 1) {
            return null;
        }
        String s = args[0];
        for (Player pl : Bukkit.getOnlinePlayers()) {
            if (!pl.getName().toLowerCase().startsWith(s.toLowerCase())) continue;
            results.add(pl.getName());
        }
        return results.size() > 0 ? results : null;
    }

    private void doBalanceOther(String[] args, Server server, CommandSender sender) {
        if (args.length < 1) {
            return;
        }
        User target = null;
        try {
            target = this.getPlayer(server, args, 0, true, true);
        } catch (NotEnoughArgumentsException | PlayerNotFoundException e) {
            if (e instanceof PlayerNotFoundException) {
                sender.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + "(!) " + ChatColor.RED + args[0] + " is not locally online.");
            }
            e.printStackTrace();
        }
        sender.sendMessage(target.getName() + "'s Balance: " + ChatColor.WHITE + "$" + this.formatMoney(target.getMoney()));
    }

    private String formatMoney(BigDecimal bal) {
        String s = this.formatter.format(bal);
        return s.contains(".") ? s.split("\\.")[0] + ChatColor.GRAY + "." + s.split("\\.")[1] : s;
    }

    private User getPlayer(Server server, CommandSource sender, String[] args, int pos) throws PlayerNotFoundException, NotEnoughArgumentsException {
        if (sender.isPlayer()) {
            User user = Balance.ess3.getUser(sender.getPlayer());
            return this.getPlayer(server, user, args, pos);
        }
        return this.getPlayer(server, args, pos, true, false);
    }

    private User getPlayer(Server server, CommandSource sender, String searchTerm) throws PlayerNotFoundException, NotEnoughArgumentsException {
        if (sender.isPlayer()) {
            User user = Balance.ess3.getUser(sender.getPlayer());
            return this.getPlayer(server, user, searchTerm, user.canInteractVanished(), false);
        }
        return this.getPlayer(server, searchTerm, true, false);
    }

    private User getPlayer(Server server, User user, String[] args, int pos) throws PlayerNotFoundException, NotEnoughArgumentsException {
        return this.getPlayer(server, user, args, pos, user.canInteractVanished(), false);
    }

    private User getPlayer(Server server, String[] args, int pos, boolean getHidden, boolean getOffline) throws PlayerNotFoundException, NotEnoughArgumentsException {
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

    private User getPlayer(Server server, String searchTerm, boolean getHidden, boolean getOffline) throws PlayerNotFoundException {
        return this.getPlayer(server, null, searchTerm, getHidden, getOffline);
    }

    private User getPlayer(Server server, User sourceUser, String searchTerm, boolean getHidden, boolean getOffline) throws PlayerNotFoundException {
        Player exPlayer;
        try {
            exPlayer = server.getPlayer(UUID.fromString(searchTerm));
        } catch (IllegalArgumentException ex) {
            exPlayer = server.getPlayer(searchTerm);
        }
        User user = exPlayer != null ? Balance.ess3.getUser(exPlayer) : Balance.ess3.getUser(searchTerm);
        if (user != null) {
            if (!getOffline && !user.getBase().isOnline()) {
                throw new PlayerNotFoundException();
            }
            if (getHidden || CommandBalance.canInteractWith(sourceUser, user)) {
                return user;
            }
            throw new PlayerNotFoundException();
        }
        List<Player> matches = server.matchPlayer(searchTerm);
        if (matches.isEmpty()) {
            String matchText = searchTerm.toLowerCase(Locale.ENGLISH);
            for (Player p : Bukkit.getOnlinePlayers()) {
                String displayName;
                User userMatch = Balance.ess3.getUser(p);
                if (!getHidden && !CommandBalance.canInteractWith(sourceUser, userMatch) || !(displayName = FormatUtil.stripFormat(userMatch.getDisplayName()).toLowerCase(Locale.ENGLISH)).contains(matchText))
                    continue;
                return userMatch;
            }
        } else {
            for (Player player : matches) {
                User userMatch = Balance.ess3.getUser(player);
                if (!userMatch.getDisplayName().startsWith(searchTerm) || !getHidden && !CommandBalance.canInteractWith(sourceUser, userMatch))
                    continue;
                return userMatch;
            }
            User userMatch = Balance.ess3.getUser(matches.get(0));
            if (getHidden || CommandBalance.canInteractWith(sourceUser, userMatch)) {
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
            return CommandBalance.canInteractWith(Balance.ess3.getUser(interactor.getPlayer()), interactee);
        }
        return true;
    }
}

