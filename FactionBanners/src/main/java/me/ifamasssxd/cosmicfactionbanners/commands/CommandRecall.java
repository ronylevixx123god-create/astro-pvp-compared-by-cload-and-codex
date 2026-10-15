package me.ifamasssxd.cosmicfactionbanners.commands;

import org.bukkit.command.*;
import org.bukkit.entity.*;
import org.arkhamnetwork.Arkkit.patches.*;
import me.ifamasssxd.cosmicfactionbanners.*;
import me.ifamasssxd.cosmicfactionbanners.struct.*;
import org.bukkit.*;
import com.cosmicpvp.cosmicutils.utils.*;
import org.bukkit.plugin.*;
import org.bukkit.metadata.*;
import org.bukkit.event.player.*;
import com.massivecraft.factions.*;
import me.ifamasssxd.cosmicfactionbanners.managers.*;

public class CommandRecall implements CommandExecutor
{
    public boolean onCommand(final CommandSender sender, final Command command, final String s, final String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }
        final Player player = (Player)sender;
        final Faction faction = FactionUtils.getFaction(player, false);
        if (CombatLog.inCombat((Player)sender)) {
            player.sendMessage(CC.translate("&cYou Cannot use /f assist on combat"));
            return true;
        }
        if (!FactionUtils.isNormal(faction)) {
            sender.sendMessage(CC.RedB + "(!) " + CC.Red + "You must be in a faction to teleport to a /f banner!");
            return true;
        }
        final BannerManager manager = CosmicFactionBanners.get().getBannerManager();
        final FactionBanner banner = manager.getFactionBannerMap().get(faction.getId());
        if (banner == null || banner.getActiveLocation() == null || banner.getPlaceTime() == 0L) {
            sender.sendMessage(CC.RedB + "(!) " + CC.Red + "Your faction does not have an active /f banner placed!");
            return true;
        }
        if (banner.hasExpired()) {
            sender.sendMessage(CC.RedB + "(!) " + CC.Red + "Your faction does not have an active Faction Banner!");
            sender.sendMessage(CC.Gray + "Your last /f banner was placed > " + FactionBanner.secondCooldown + "s ago!");
            return true;
        }
        player.sendMessage(CC.DarkPurpleB + "(!) " + CC.DarkPurple + "Teleporting to " + banner.getWhoPlacedUsername() + "'s /f banner at: ");
        player.sendMessage(CC.LightPurple + LocationUtils.printPretty(banner.getActiveLocation(), ChatColor.LIGHT_PURPLE, true) + "!");
        player.setMetadata("bannerTp", (MetadataValue)new FixedMetadataValue((Plugin)CosmicFactionBanners.get(), (Object)banner.getActiveLocation().getBlock().getLocation().add(0.5, 0.0, 0.5)));
        player.teleport(banner.getActiveLocation().getBlock().getLocation().add(0.5, 0.0, 0.5), PlayerTeleportEvent.TeleportCause.PLUGIN);
        return false;
    }
}
