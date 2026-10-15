package me.astropvp.hotfixplugin.listeners;

import com.cosmicpvp.cosmicutils.nbt.NBTTagCompoundBuilder;
import com.cosmicpvp.cosmicutils.utils.*;
import com.earth2me.essentials.utils.DateUtil;
import com.massivecraft.factions.perms.Role;
import me.astropvp.hotfixplugin.HotFixPlugin;
import org.arkhamnetwork.Arkkit.utils.EssentialsUtils;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.awt.*;
import java.lang.management.ManagementFactory;
import java.util.Arrays;
import java.util.List;

public class CommandPingListener implements Listener {
    @EventHandler(priority = EventPriority.LOWEST)
    public void pingCommand(PlayerCommandPreprocessEvent e) {
        Player player = e.getPlayer();
        int ping = HotFixPlugin.getPing(player);
        String tps = HotFixPlugin.df.format(EssentialsUtils.getEssentials().getTimer().getAverageTPS());
        if (e.getMessage().equalsIgnoreCase("/ping") || e.getMessage().equalsIgnoreCase("/tps") || e.getMessage().equalsIgnoreCase("/mem") || e.getMessage().equalsIgnoreCase("/ping") || e.getMessage().equalsIgnoreCase("/lag")) {
            e.setMessage("/null");
            e.setCancelled(true);
            player.sendMessage("");
            player.sendMessage(CC.translate("&6&lAstro&f&lPvP &6&lPerformance"));
            player.sendMessage(CC.translate("&6&l* &eUptime: &f" + DateUtil.formatDateDiff(ManagementFactory.getRuntimeMXBean().getStartTime())));
            player.sendMessage(CC.translate("&6&l* &eServer \"TPS\": &a" + tps));
            player.sendMessage(CC.translate("&6&l* &ePing: &a" + ping + "ms"));
            player.sendMessage("");

        }

    }

    @EventHandler
    public void addmutated(PlayerCommandPreprocessEvent e) {
        Player player = e.getPlayer();
        if (!player.isOp()) return;
        if (e.getMessage().startsWith("/addmutated")) {
            List<String> args = Arrays.asList(e.getMessage().split(" "));
            e.setMessage("/null");
            e.setCancelled(true);
            GlintUtils.addGlint(player.getItemInHand(), Color.GREEN);
            player.updateInventory();
        }
    }



/*    @EventHandler(priority = EventPriority.LOWEST)
    public void altFix(PlayerCommandPreprocessEvent e) {
        if (FactionUtils.getFPlayer(e.getPlayer()).getRole() == Role.ALT) {
            e.setMessage("/null");
            e.setCancelled(true);
            e.getPlayer().sendMessage(CC.translate("&c&l(!)&c You cannot do this while being an alt"));
        }
    }*/


}
