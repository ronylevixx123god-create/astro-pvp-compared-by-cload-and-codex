/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.earth2me.essentials.ChargeException
 *  com.earth2me.essentials.CommandSource
 *  com.earth2me.essentials.I18n
 *  com.earth2me.essentials.Trade
 *  com.earth2me.essentials.User
 *  com.earth2me.essentials.me.astropvp.cratekeys.net.astropvp.armorsets.commands.EssentialsLoopCommand
 *  com.earth2me.essentials.me.astropvp.cratekeys.net.astropvp.armorsets.commands.NotEnoughArgumentsException
 *  com.earth2me.essentials.me.astropvp.cratekeys.net.astropvp.armorsets.commands.PlayerExemptException
 *  com.earth2me.essentials.me.astropvp.cratekeys.net.astropvp.armorsets.commands.PlayerNotFoundException
 *  com.earth2me.essentials.me.astropvp.cratekeys.net.astropvp.armorsets.utils.FormatUtil
 *  net.ess3.api.IEssentials
 *  net.ess3.api.MaxMoneyException
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Location
 *  org.bukkit.Server
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 */
package net.astropvp.core.betterpay.commands;

import com.earth2me.essentials.ChargeException;
import com.earth2me.essentials.CommandSource;
import com.earth2me.essentials.I18n;
import com.earth2me.essentials.User;
import com.earth2me.essentials.commands.EssentialsLoopCommand;
import com.earth2me.essentials.commands.NotEnoughArgumentsException;
import com.earth2me.essentials.commands.PlayerExemptException;
import com.earth2me.essentials.commands.PlayerNotFoundException;
import com.earth2me.essentials.utils.FormatUtil;
import net.astropvp.core.AstroCore;
import net.ess3.api.IEssentials;
import net.ess3.api.MaxMoneyException;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.entity.Player;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;

public class CommandPay
extends EssentialsLoopCommand {
    BigDecimal amount;

    public CommandPay() {
        super("pay");
    }

    public void run(Server server, Player sender, User user, String commandLabel, String[] args) throws Exception {
        if (args.length < 2) {
            throw new NotEnoughArgumentsException();
        }
        this.amount = new BigDecimal(args[1].replaceAll("[^0-9\\.]", ""));
        this.loopOnlinePlayers(server, new CommandSource(sender), false, user.isAuthorized("essentials.pay.multiple"), args[0], args);
    }

    protected void loopOnlinePlayers(Server server, CommandSource sender, boolean multipleStringMatches, boolean matchWildcards, String searchTerm, String[] commandArgs) throws PlayerNotFoundException, NotEnoughArgumentsException, PlayerExemptException, ChargeException, MaxMoneyException {
        boolean skipHidden;
        if (searchTerm.isEmpty()) {
            throw new PlayerNotFoundException();
        }
        if (this.ess == null) {
            this.grabEssentials();
        }
        boolean bl = skipHidden = sender.isPlayer() && !this.ess.getUser(sender.getPlayer()).canInteractVanished();
        if (matchWildcards && (searchTerm.contentEquals("**") || searchTerm.contentEquals("*") || searchTerm.contentEquals("all"))) {
            for (Player p : server.getOnlinePlayers()) {
                if (AstroCore.getInstance().getPayToggles().hasToggle(p)) continue;
                User onlineUser = this.ess.getUser(p);
                if (skipHidden && onlineUser.isHidden(sender.getPlayer()) && !sender.getPlayer().canSee(onlineUser.getBase())) continue;
                this.updatePlayer(server, sender, onlineUser, commandArgs);
            }
        } else if (multipleStringMatches) {
            if (searchTerm.trim().length() < 2) {
                throw new PlayerNotFoundException();
            }
            boolean foundUser = false;
            List<Player> matchedPlayers = server.matchPlayer(searchTerm);
            if (matchedPlayers.isEmpty()) {
                String matchText = searchTerm.toLowerCase(Locale.ENGLISH);
                for (Player p : server.getOnlinePlayers()) {
                    String displayName;
                    if (AstroCore.getInstance().getPayToggles().hasToggle(p)) continue;
                    User player = this.ess.getUser(p);
                    if (skipHidden && player.isHidden(sender.getPlayer()) && !sender.getPlayer().canSee(player.getBase()) || !(displayName = FormatUtil.stripFormat(player.getDisplayName()).toLowerCase(Locale.ENGLISH)).contains(matchText)) continue;
                    foundUser = true;
                    this.updatePlayer(server, sender, player, commandArgs);
                }
            } else {
                for (Player matchPlayer : matchedPlayers) {
                    User player = this.ess.getUser(matchPlayer);
                    if (AstroCore.getInstance().getPayToggles().hasToggle(player.getBase())) {
                        sender.sendMessage(ChatColor.RED + player.getName() + " has /pay toggled " + ChatColor.RED + ChatColor.BOLD.toString() + "OFF");
                        continue;
                    }
                    if (skipHidden && player.isHidden(sender.getPlayer()) && !sender.getPlayer().canSee(matchPlayer)) continue;
                    foundUser = true;
                    this.updatePlayer(server, sender, player, commandArgs);
                }
            }
            if (!foundUser) {
                throw new PlayerNotFoundException();
            }
        } else {
            User player = this.getPlayer(server, sender, searchTerm);
            if (AstroCore.getInstance().getPayToggles().hasToggle(player.getBase())) {
                sender.sendMessage(ChatColor.RED + player.getName() + " has /pay toggled " + ChatColor.RED + ChatColor.BOLD.toString() + "OFF");
                return;
            }
            this.updatePlayer(server, sender, player, commandArgs);
        }
    }

    protected void updatePlayer(Server server, CommandSource sender, User player, String[] args) throws NotEnoughArgumentsException, PlayerExemptException, ChargeException, MaxMoneyException {
        User user = this.ess.getUser(sender.getPlayer());
        try {
            if (AstroCore.getInstance().getPayToggles().hasToggle(player.getBase())) {
                return;
            }
            user.payUser(player, this.amount);
            //Trade.log("Command", "Pay", "Player", user.getName(), new Trade(this.amount, this.ess), player.getName(), new Trade(this.amount, this.ess), user.getLocation(), this.ess);
        }
        catch (MaxMoneyException ex) {
            sender.sendMessage(I18n.tl("maxMoney", new Object[0]));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void grabEssentials() {
        try {
            this.ess = (IEssentials)Bukkit.getPluginManager().getPlugin("Essentials");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        if (this.ess == null && !Bukkit.getPluginManager().isPluginEnabled("Essentials")) {
            Bukkit.getPluginManager().disablePlugin(AstroCore.getInstance());
            Bukkit.getLogger().severe("Disabling BetterPay due to missing Essentials...");
            return;
        }
    }
}

