package me.ifamasssxd.cosmicfactionbanners.commands;

import org.bukkit.command.*;
import org.bukkit.entity.*;
import org.bukkit.*;
import com.cosmicpvp.cosmicutils.utils.*;
import me.ifamasssxd.cosmicfactionbanners.struct.*;
import me.ifamasssxd.cosmicfactionbanners.*;
import org.bukkit.inventory.*;
import com.massivecraft.factions.*;

public class CommandBanner implements CommandExecutor
{
    private double BANNER_COST;
    
    public CommandBanner() {
        this.BANNER_COST = 100000.0;
    }
    
    public boolean onCommand(final CommandSender sender, final Command command, final String s, final String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }
        final Player player = (Player)sender;
        final Faction faction = FactionUtils.getFaction(player, false);
        if (!FactionUtils.isNormal(faction)) {
            sender.sendMessage(CC.RedB + "(!) " + CC.Red + "You must be in a faction to use this command!");
            return true;
        }
        if (!EconomyUtils.hasBalance((OfflinePlayer)player, this.BANNER_COST)) {
            sender.sendMessage(CC.RedB + "(!) " + CC.Red + "You cannot afford " + CC.RedB + "$" + CC.Red + NumberUtils.formatMoney(this.BANNER_COST) + CC.Red + " for a Faction Banner!");
            return true;
        }
        if (player.getInventory().firstEmpty() == -1) {
            sender.sendMessage(CC.RedB + "(!) " + CC.Red + "Your me.astropvp.cratekeys.inventory is full!");
            return true;
        }
        final BannerDesign design = CosmicFactionBanners.get().getBannerManager().getBannerDesignMap().get(faction.getId());
        final ItemStack banner = (design != null) ? design.getBannerItem(faction) : CosmicFactionBanners.get().getBannerManager().createBanner(faction, null, true);
        sender.sendMessage(CC.RedB + "- $" + CC.Red + NumberUtils.formatMoney(this.BANNER_COST));
        sender.sendMessage(CC.GreenB + "+ 1 " + CC.Green + "Faction Banner");
        if (CosmicFactionBanners.get().getConfig().getInt("soulCost") > 0) {
            sender.sendMessage(CC.Gray + "(note: /f banner costs " + CosmicFactionBanners.get().getConfig().getInt("soulCost") + " souls to place.)");
        }
        EconomyUtils.withdrawBalance((OfflinePlayer)player, this.BANNER_COST);
        player.getInventory().addItem(new ItemStack[] { banner });
        player.updateInventory();
        return false;
    }
}
