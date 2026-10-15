package me.astropvp.astrospawners.ftop;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.NumberUtils;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

public class CommandFactionsTop implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
        Player player = (Player)sender;
        int page = (args.length >= 1) ? Integer.valueOf(args[0]).intValue() : 1;
        page = Math.min(5, Math.max(1, page));
        player.sendMessage("");
        player.sendMessage(CC.GreenB + "Top Factions (" + CC.WhiteB + page + CC.Green + "/" + CC.WhiteB + "5" + CC.GreenB + ")");
        int pos = 1;
        int from = (page - 1) * 9;
        int to = (page - 1) * 9 + 9;
        Map<Integer, FactionData> data = FactionsTopAPI.getTopFactions(from, to);
        for (int x = from; x < to; x++) {
            if (data.containsKey(x)) {
                FactionData faction = data.get(x);
                TextComponent msg = new TextComponent(CC.GreenB + pos + ". " + CC.Green + faction.getFaction().getTag() + CC.Green + " - " + CC.WhiteB + "$" + NumberUtils.formatMoney(faction.getSpawnerValue()));
                String builder = CC.GreenB + CC.Underline + faction.getFaction().getTag() + "\n\n" +
                        CC.White + "Member Wealth: " + CC.Green + "$" + NumberUtils.formatMoney(faction.getMemberValue()) + "\n" +
                        CC.White + "Spawner Value: " + CC.Green + "$" + NumberUtils.formatMoney(faction.getSpawnerValue()) + "\n" +
                        CC.White + "Spawners: " + CC.Green + faction.getSpawnerCount() + "\n" +
                        CC.White + "Stacked Spawners: " + CC.Green + faction
                        .getStackedSpawnerCount() +
                        "\n" +
                        CC.White + "Richest Member:\n" +
                        " " + CC.Green + faction.getRichestPlayer() + ": $" + NumberUtils.formatMoney(faction.getRichestPlayerBalance());
                msg.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, (new ComponentBuilder(builder)).create()));
                player.sendMessage(msg);
                pos++;
            }
        }
        player.sendMessage("");
        return false;
    }
}
