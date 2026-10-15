package me.kingsavaqe.astroenvoy.commands;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.ItemBuilder;
import com.cosmicpvp.cosmicutils.utils.PlayerUtil;
import me.kingsavaqe.astroenvoy.EnvoyPlugin;
import me.kingsavaqe.astroenvoy.language.Language;
import me.kingsavaqe.astroenvoy.struct.EnvoyManager;
import me.kingsavaqe.astroenvoy.utils.CustomItemUtils;
import me.mattstudios.mf.annotations.*;
import me.mattstudios.mf.base.CommandBase;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command("aenvoy")
public class AdminEnvoyCommand extends CommandBase {

    private final EnvoyPlugin plugin;
    public AdminEnvoyCommand(EnvoyPlugin plugin) {
        this.plugin = plugin;
    }

    @Default
    @Permission("cranked.admin")
    public void defaultCommand(CommandSender commandSender) {
        sendHelpMessage(commandSender);
    }

    @SubCommand("setup")
    @Permission("cranked.admin")
    public void setupCommand(final CommandSender commandSender) {
        if(!(commandSender instanceof Player)) {
            commandSender.sendMessage(CC.translate("&cYou must be a player to execute this command"));
            return;
        }

        final Player player = ((Player) commandSender);
        final ItemBuilder itemBuilder = new ItemBuilder(Material.CHEST, "&b&lEnvoy Setup Chest",
                "&7Place this down to set the blocks placed",
                "&7location to a envoy location");
        player.getInventory().addItem(CustomItemUtils.setCrankedCustomItem(itemBuilder.build(), "envoySetupChest"));
        player.sendMessage(CC.translate("&aYou have been given a crate to edit crate locations"));
    }

    @SubCommand("reload")
    @WrongUsage("&cUsage: /aenvoy reload")
    @Permission("cranked.admin")
    public void reloadCommand(Player sender) {
        Language.ADMIN_RELOADING.send(sender);
        long then = System.currentTimeMillis();

        EnvoyPlugin.getInstance().getLanguageHandler().load();

        long diff = System.currentTimeMillis() - then;
        Language.ADMIN_RELOADED.send(sender, str -> str.replace("%took%", String.valueOf(diff)));
    }

    @SubCommand("summoner")
    @Permission("cranked.admin")
    public void summonerCommand(final CommandSender commandSender, @Optional @Completion("#players") Player player) {
        if(player == null) player = (Player)commandSender;
        ItemBuilder itemBuilder = new ItemBuilder(Material.TORCH, "&f&ki&r&e✦&f&ki&r &6&lEnvoy Summoner &f&ki&e✦&f&ki&r",
                "&7Right-Click this summoner to",
                "&7forcefully begin a envoy event",
                "",
                "&7The event will not start if",
                "&7another envoy event is ongoing");
        PlayerUtil.giveOrDropProtectedItem(player, CustomItemUtils.setCrankedCustomItem(itemBuilder.build(), "envoySummoner"), 15);
    }

    @SubCommand("forcestart")
    @Permission("cranked.admin")
    public void forceStartCommand(final CommandSender commandSender) {
        if(this.plugin.getEnvoyManager().isActive()) {
            commandSender.sendMessage(CC.translate(EnvoyManager.getPrefix() + "&cThere is already an active envoy event"));
            return;
        }

        /*
        this.plugin.getServer().getOnlinePlayers()
                .stream()
                .filter(Objects::nonNull) //TODO filter people with envoy messages enabled
                .forEach(player -> player.sendMessage(CC.translate(EnvoyManager.prefix + "&aAn envoy event has just started")));

         */
        this.plugin.getEnvoyManager().startEnvoyEvent();
    }

    @SubCommand("forceend")
    @Permission("cranked.admin")
    public void forceEndCommand(final CommandSender commandSender) {
        if(!this.plugin.getEnvoyManager().isActive()) {
            commandSender.sendMessage(CC.translate(EnvoyManager.getPrefix() + "&cThere is no ongoing envoy event"));
            return;
        }

        /*
        this.plugin.getServer().getOnlinePlayers()
                .stream()
                .filter(Objects::nonNull) //TODO filter people with envoy messages enabled
                .forEach(player -> player.sendMessage(CC.translate(EnvoyManager.prefix + "&cThe envoy event has ended")));

         */
        this.plugin.getEnvoyManager().endEnvoyEvent();
    }



    private void sendHelpMessage(CommandSender commandSender) {
        commandSender.sendMessage("/aenvoy forceend");
        commandSender.sendMessage("/aenvoy forcestart");
        commandSender.sendMessage("/aenvoy setup");
        commandSender.sendMessage("/aenvoy summoner");
    }

}
