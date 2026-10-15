package me.ifamasssxd.cosmicfactionbanners.commands;

import com.massivecraft.factions.perms.Role;
import me.ifamasssxd.cosmicfactionbanners.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;
import com.massivecraft.factions.struct.*;
import org.bukkit.*;
import com.cosmicpvp.cosmicutils.utils.*;
import me.ifamasssxd.cosmicfactionbanners.struct.*;
import org.bukkit.inventory.*;
import com.massivecraft.factions.*;

public class CommandSetBanner implements CommandExecutor
{
    private int BANNER_SET_COST;
    
    public CommandSetBanner() {
        this.BANNER_SET_COST = CosmicFactionBanners.get().getConfig().getInt("setBannerCost");
    }
    
    public boolean onCommand(final CommandSender sender, final Command command, final String s, final String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }
        final Player player = (Player)sender;
        final ItemStack handItem = player.getItemInHand();
        if (handItem == null || handItem.getType() != Material.BANNER) {
            sender.sendMessage(CC.RedB + "(!) " + CC.Red + "You must be holding a banner to set your Faction Banner!");
            return true;
        }
        final Faction faction = FactionUtils.getFaction(player, false);
        if (!FactionUtils.isNormal(faction)) {
            sender.sendMessage(CC.RedB + "(!) " + CC.Red + "You must be in a valid faction to run /f setbanner");
            return true;
        }
        final FPlayer fplayer = FactionUtils.getFPlayer(player);
        if (!fplayer.getRole().isAtLeast(Role.COLEADER)) {
            sender.sendMessage(CC.RedB + "(!) " + CC.Red + "You must be atleast a faction Co-Leader to set your Faction Banner!");
            return true;
        }
        if (!EconomyUtils.hasBalance((OfflinePlayer)player, (double)this.BANNER_SET_COST)) {
            player.sendMessage(CC.RedB + "(!) " + CC.Red + "You cannot afford to customize your Faction Banner!");
            player.sendMessage(CC.GrayU + "Cost:" + CC.GrayB + " $" + CC.Gray + NumberUtils.formatMoney((double)this.BANNER_SET_COST));
            return true;
        }
        EconomyUtils.withdrawBalance((OfflinePlayer)player, (double)this.BANNER_SET_COST);
        player.sendMessage(CC.RedB + "- $" + CC.Red + NumberUtils.formatMoney((double)this.BANNER_SET_COST));
        player.sendMessage(CC.Green + "Faction banner updated to your held item!");
        player.sendMessage(CC.Gray + "Use /f banner to receive a Faction Banner for a " + this.BANNER_SET_COST + "!");
        final BannerDesign design = CosmicFactionBanners.get().getBannerManager().getBannerDesignMap().computeIfAbsent(faction.getId(), des -> new BannerDesign());
        design.setBanner(handItem);
        player.setItemInHand((ItemStack)null);
        return true;
    }
}
