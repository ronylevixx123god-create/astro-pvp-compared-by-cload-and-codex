package net.astropvp.gambling.crash;


import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.EconomyUtils;
import  net.astropvp.gambling.crash.inventory.CrashInventory;
import  net.astropvp.gambling.crash.manager.CrashManager;
import  net.astropvp.gambling.crash.struct.CrashGame;
import  net.astropvp.gambling.crash.struct.CrashPlayer;
import net.astropvp.gambling.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import net.astropvp.gambling.GamblingPlugin;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

public class CrashAPI {

    public static final String prefix = "&6&lCRASH &8➥ ";

    private static  final DecimalFormat moneyFormat = new DecimalFormat("#,###");

    public static List<Player> getFixedCrashOpenInventoryList() {
        List<Player> players = new ArrayList<>();
        Bukkit.getOnlinePlayers().forEach(player -> {
            if(playerHasCrashGUIOpen(player)) players.add(player);
        });
        return players.stream().distinct().collect(Collectors.toList());
    }

    public static boolean playerHasCrashGUIOpen(Player player) {
        return player.getOpenInventory().getTopInventory().getTitle().equals("Astro Crash");
    }

    public static CrashPlayer getPlayerFromCrashGame(OfflinePlayer player, CrashGame crashGame) {
        return getPlayerFromCrashGame(player.getUniqueId(), crashGame);
    }

    public static boolean isPlayerInCrashGame(UUID uuid, CrashGame crashGame) {
        for(CrashPlayer player : crashGame.getPlayers()) {
            if(Objects.equals(player.getPlayerUUID().toString(), uuid.toString())) {
                return true;
            }
        }
        return false;
    }

    public static CrashPlayer getPlayerFromCrashGame(UUID uuid, CrashGame crashGame) {
        if(!isPlayerInCrashGame(uuid, crashGame)) {
            return new CrashPlayer(uuid, 0D, null, crashGame.getCrashX());
        };

        crashGame.getPlayers().forEach(crashPlayer -> Bukkit.getOfflinePlayer(crashPlayer.getPlayerUUID()).getName());
        for(CrashPlayer crashPlayer : crashGame.getPlayers()) {
            if(Objects.equals(crashPlayer.getPlayerUUID().toString(), uuid.toString())) {
                return crashPlayer;
            }
        }
        return new CrashPlayer(uuid, 0D, null, crashGame.getCrashX());
    }


    public static void tryBet(Player player, double bet) {

        if(bet < 100) {
            player.sendMessage(CC.translate(CrashAPI.prefix + "&cYou cannot bet less than $100"));
            player.playSound(player.getLocation(), Sound.ANVIL_LAND, 2.0F, 2.0F);
            return;
        }

        GamblingPlugin plugin = GamblingPlugin.getInstance();
        if(plugin.getCrashManager().hasBetted(player) || plugin.getCrashManager().getWaitingToJoinCrash().containsKey(player)) {
            player.sendMessage(CC.translate(CrashAPI.prefix + "&cYou have already betted for that round"));
            player.playSound(player.getLocation(), Sound.ANVIL_LAND, 2.0F, 2.0F);
            return;
        }

        if(plugin.getCrashManager().isActive()) {
            plugin.getCrashManager().getWaitingToJoinCrash().put(player, bet);
            player.sendMessage(CC.translate(CrashAPI.prefix + "&aYou will be entered into next game if you have enough money"));
            player.playSound(player.getLocation(), Sound.SUCCESSFUL_HIT, 2.0F, 2.0F);
            return;
        }

        if(!EconomyUtils.hasBalance(player, bet)) {
            player.sendMessage(CC.translate(CrashAPI.prefix + "&cYou do not have sufficient funds for that"));
            player.playSound(player.getLocation(), Sound.ANVIL_LAND, 2.0F, 2.0F);
            return;
        }

        EconomyUtils.withdrawBalance(player, bet);
        CrashManager crashManager = plugin.getCrashManager();
        crashManager.getBets().put(player, bet);
        crashManager.getLastBets().put(player, bet);
        player.sendMessage(CC.translate(CrashAPI.prefix + "&aYou have joined the crash game with $" + moneyFormat.format(bet)));
        player.playSound(player.getLocation(), Sound.SUCCESSFUL_HIT, 2.0F, 2.0F);

        CrashAPI.getFixedCrashOpenInventoryList().forEach(loopPlayer -> {
            ItemBuilder itemBuilder = new ItemBuilder(Material.SKULL_ITEM, 1, (short) 3,
                    "&fPlayer: &a" + player.getName(),
                    "&fBet: &a$" + moneyFormat.format(bet)).setSkullOwner(player.getName());
            loopPlayer.getOpenInventory().getTopInventory().addItem(itemBuilder.colorize().build());
            loopPlayer.updateInventory();

        });

        if(CrashAPI.playerHasCrashGUIOpen(player)) return;

        CrashInventory crashInventory = new CrashInventory(player);
        crashInventory.getUI().open(player);


    }
}
