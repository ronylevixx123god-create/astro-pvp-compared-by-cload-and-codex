package net.astropvp.core.giveaway;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.massivecraft.factions.util.Clickable;
import lombok.Getter;
import lombok.Setter;
import net.astropvp.core.AstroCore;
import org.apache.commons.lang.StringUtils;
import org.arkhamnetwork.Arkkit.libs.JSONMessage;
import org.arkhamnetwork.Arkkit.patches.ItemLinking;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class GiveAwayManager {

    public GiveAway currentGiveAway;
    public AstroCore plugin;

    @Setter @Getter
    private int fakeJoins = 0;

    public GiveAwayManager(AstroCore plugin) {
        this.plugin = plugin;
        this.currentGiveAway = null;
    }

    public void startGiveAway(Player player, int winnersAmount, ItemStack prize, int timeInSeconds) {
        if (this.currentGiveAway != null) {
            player.sendMessage(CC.translate("&c&l(!)&c There is already a giveaway running!"));
            return;
        }

        if (winnersAmount < 0) {
            player.sendMessage(CC.translate("&c&l(!)&c Winners must be over 0"));
            return;
        }
        if (timeInSeconds < 60) {
            player.sendMessage(CC.translate("&c&l(!)&c Time must be atleast 60 seconds"));
            return;
        }

        if (player.getItemInHand() == null) {
            player.sendMessage(CC.translate("&c&l(!)&c Please hold the item you would like to giveaway!"));
            return;
        }
        this.fakeJoins = 0;
        player.sendMessage(CC.translate("&a&l(!)&a You have started the giveaway with prize: " + player.getItemInHand().getItemMeta().getDisplayName() + " &awith &7" + winnersAmount + " &awinners and will last &7" + timeInSeconds + "s&a."));
        this.currentGiveAway = new GiveAway(winnersAmount, prize, timeInSeconds);
        this.currentGiveAway.setGiveAwayTaskId(Bukkit.getScheduler().scheduleSyncRepeatingTask(AstroCore.getInstance(), new GiveAwayTask(), 0L, 20L));
        announceStart(this.currentGiveAway);

    }

    public void cancelGiveAway(Player player) {
        if (this.currentGiveAway != null) {
            Bukkit.getScheduler().cancelTask(this.currentGiveAway.giveAwayTaskId);
            this.currentGiveAway = null;
            player.sendMessage(CC.translate("&a&l(!)&a Giveaway has been cancelled!"));
        } else {
            player.sendMessage(CC.translate("&c&l(!)&c There is currently no giveaway running!"));
        }
    }

    public void joinGiveAway(Player player) {
        if (this.currentGiveAway == null) {
            player.sendMessage(CC.translate("&c&l(!)&c There is currently no giveaway running!"));
            return;
        }
        if (this.currentGiveAway.playersJoined.contains(player.getUniqueId())) {
            player.sendMessage(CC.translate("&c&l(!)&c You have already joined this giveaway!"));
            return;
        }

        this.currentGiveAway.playersJoined.add(player.getUniqueId());
        player.sendMessage(CC.translate("&a&l(!)&a You have successfully joined the giveaway"));
    }

    public void endGiveAway() {
        if (this.currentGiveAway != null) {
            GiveAway giveAway = this.currentGiveAway;
            if (giveAway.getPlayersJoined().isEmpty()) {
                Bukkit.broadcastMessage(CC.translate("&c&l(!)&c No one has entered the giveaway!"));
                return;
            }
            List<Player> winners = selectWinner(giveAway.getWinnersAmount());
            announceWinners(winners, giveAway);
            for (Player winner : winners) {
                winner.getInventory().addItem(giveAway.getPrize());
                winner.sendMessage(CC.translate("&a&l(!)&a Congratulations, you have won the giveaway that had a total of &7" + giveAway.playersJoined.size() + " &aentries!"));
            }
            this.currentGiveAway = null;
        }
    }

    public List<Player> selectWinner(int amountWinners) {
        Random rand = new Random();
        List<Player> winners = new ArrayList<>();

        for (int i = 0; i < amountWinners; i++) {
            int randomIndex = rand.nextInt(this.currentGiveAway.playersJoined.size());
            UUID uuid = this.currentGiveAway.playersJoined.get(randomIndex);
            if (Bukkit.getPlayer(uuid) == null || !Bukkit.getPlayer(uuid).isOnline()) {
                i--;
            } else if (winners.contains(uuid)) {
                i--;
            } else {
                winners.add(Bukkit.getPlayer(uuid));
            }
        }
        return winners;
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

    public void announce(int seconds, GiveAway giveAway) {
        JSONMessage message = new JSONMessage("", ChatColor.GRAY);
        message.addText(CC.translate("&c&l * &cPrize: &f"));
        message.addItem(this.currentGiveAway.getPrize(), ItemLinking.formatColors(ChatColor.WHITE + this.getFormattedName(this.currentGiveAway.getPrize())).getText());
        Clickable clickable = new Clickable("");
        clickable.add(CC.translate("&7[Click here to join the giveaway]"), CC.translate("&7/giveaway join"), "/giveaway join");
        Bukkit.broadcastMessage("");
        Bukkit.broadcastMessage(CC.translate("&c&lAstro Giveaway Countdown"));
        Bukkit.broadcastMessage(CC.translate("&c&l * &cTime Left: &f" + seconds + "s"));
        Bukkit.broadcastMessage(CC.translate("&c&l * &cEntries: &f" + getAmountInGiveaway(giveAway)));

        for (Player player : Bukkit.getOnlinePlayers()) {
            message.sendToPlayer(player);
            clickable.sendToPlayer(player);
            player.sendMessage("");
        }
    }

    private int getAmountInGiveaway(GiveAway giveAway) {
        return giveAway.getPlayersJoined().size() + this.fakeJoins;
    }

    public void announceStart(GiveAway giveAway) {
        JSONMessage message = new JSONMessage("", ChatColor.GRAY);
        message.addText(CC.translate("&c&l * &cPrize: &f"));
        message.addItem(this.currentGiveAway.getPrize(), ItemLinking.formatColors(ChatColor.WHITE + this.getFormattedName(this.currentGiveAway.getPrize())).getText());
        Clickable clickable = new Clickable("");
        clickable.add(CC.translate("&7[Click here to join the giveaway]"), CC.translate("&7/giveaway join"), "/giveaway join");
        Bukkit.broadcastMessage("");
        Bukkit.broadcastMessage(CC.translate("&c&lAstro Giveaway Started"));
        Bukkit.broadcastMessage(CC.translate("&c&l * &cTime: &f" + giveAway.getTimeInSeconds() + "s"));
        Bukkit.broadcastMessage(CC.translate("&c&l * &cWinners: &f" + giveAway.getWinnersAmount()));

        for (Player player : Bukkit.getOnlinePlayers()) {
            message.sendToPlayer(player);
            clickable.sendToPlayer(player);
            player.sendMessage("");
        }
    }

    public void announceWinners(List<Player> winners, GiveAway giveAway) {
        JSONMessage message = new JSONMessage("", ChatColor.GRAY);
        message.addText(CC.translate("&c&l * &cPrize: &f"));
        message.addItem(this.currentGiveAway.getPrize(), ItemLinking.formatColors(ChatColor.WHITE + this.getFormattedName(this.currentGiveAway.getPrize())).getText());
        StringBuilder winnersString = new StringBuilder();
        for (Player player : winners) {
            winnersString.append(player.getName()).append(", ");
        }
        winnersString = new StringBuilder(removeLastCharOptional(winnersString.toString()));
        winnersString = new StringBuilder(removeLastCharOptional(winnersString.toString()));
        Bukkit.broadcastMessage("");
        Bukkit.broadcastMessage(CC.translate("&c&lAstro Giveaway Ended"));
        Bukkit.broadcastMessage(CC.translate("&c&l * &cWinners: &f" + winnersString));
        Bukkit.broadcastMessage(CC.translate("&c&l * &cEntries: &f" + getAmountInGiveaway(giveAway)));




        for (Player player : Bukkit.getOnlinePlayers()) {
            message.sendToPlayer(player);
            player.sendMessage("");
        }



    }

    public static String removeLastCharOptional(String s) {
        return Optional.ofNullable(s)
                .filter(str -> str.length() != 0)
                .map(str -> str.substring(0, str.length() - 1))
                .orElse(s);
    }




}
