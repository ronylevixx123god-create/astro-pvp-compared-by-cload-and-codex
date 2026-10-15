package me.ifamasssxd.cosmicfactionbanners.struct;

import org.bukkit.entity.*;
import me.ifamasssxd.cosmicfactionbanners.*;
import org.bukkit.plugin.*;
import org.arkhamnetwork.Arkkit.patches.*;
import org.bukkit.block.*;
import java.util.*;
import org.bukkit.*;
import com.massivecraft.factions.*;
import com.cosmicpvp.cosmicutils.utils.*;
import java.util.concurrent.*;

public class FactionBanner
{
    public static int secondCooldown;
    public static long expired;
    private UUID whoPlacedBanner;
    private String whoPlacedUsername;
    private Location activeLocation;
    private Faction faction;
    private long placeTime;
    
    public void removeBanner() {
        if (this.activeLocation == null) {
            return;
        }
        final Block block = this.activeLocation.getBlock();
        if (block == null) {
            return;
        }
        final Material type = block.getType();
        if (type == Material.WALL_BANNER || type == Material.STANDING_BANNER) {
            block.setType(Material.AIR);
            if (this.whoPlacedBanner != null) {
                final Player online = Bukkit.getPlayer(this.whoPlacedBanner);
                if (online != null) {
                    online.sendMessage(CC.RedB + "(!) " + CC.Red + "Your /f banner has expired or been destroyed!");
                    online.playSound(online.getLocation(), Sound.PORTAL, 1.0f, 1.4f);
                }
            }
            for (final Player p : this.faction.getOnlinePlayers()) {
                if (p.hasMetadata("bannerTp")) {
                    p.removeMetadata("bannerTp", (Plugin)CosmicFactionBanners.get());
                    TPWarmup.cancelQueuedTeleport(p);
                    p.sendMessage(CC.DarkPurpleB + "[/f banner]: " + CC.Red + "Your faction banner was destroyed, /f assist teleport cancelled!");
                }
            }
        }
        this.activeLocation = null;
    }
    
    public int getSecondsLeft() {
        final long time = System.currentTimeMillis() - this.placeTime;
        return (int)Math.ceil((double)((FactionBanner.expired - time) / 1000L));
    }
    
    public void placeBanner(final Faction faction, final Player whoPlaced, final Location location) {
        if (this.activeLocation != null) {
            this.removeBanner();
        }
        this.whoPlacedBanner = whoPlaced.getUniqueId();
        this.whoPlacedUsername = whoPlaced.getName();
        this.activeLocation = location;
        this.placeTime = System.currentTimeMillis();
        this.faction = faction;
        final JSONMessage msg = new JSONMessage("");
        msg.addText((Player)null, CC.Gray + "          Click ", ChatColor.GRAY);
        msg.addRunCommand(CC.GrayB + CC.Underline + "HERE", ChatColor.GRAY, "/frecall", CC.Gray + "Click to teleport to " + this.whoPlacedUsername + "'s banner!");
        msg.addText((Player)null, CC.Gray + " or use " + CC.DarkPurple + "/f assist" + CC.Gray + " to teleport!", ChatColor.GRAY);
        for (final FPlayer player : faction.getFPlayers()) {
            if (!player.isOnline()) {
                continue;
            }
            final Player pl = player.getPlayer();
            pl.sendMessage("");
            pl.sendMessage(StringUtils.getCenteredMessage(CC.DarkPurpleB + "[/f banner]: " + CC.LightPurple + this.whoPlacedUsername + ", at: " + LocationUtils.printPretty(this.activeLocation, ChatColor.DARK_PURPLE, false)));
            msg.sendToPlayer(pl);
            pl.sendMessage("");
        }
    }
    
    public boolean hasExpired() {
        return this.placeTime > 0L && System.currentTimeMillis() - this.placeTime >= FactionBanner.expired;
    }
    
    public UUID getWhoPlacedBanner() {
        return this.whoPlacedBanner;
    }
    
    public String getWhoPlacedUsername() {
        return this.whoPlacedUsername;
    }
    
    public Location getActiveLocation() {
        return this.activeLocation;
    }
    
    public Faction getFaction() {
        return this.faction;
    }
    
    public long getPlaceTime() {
        return this.placeTime;
    }
    
    static {
        FactionBanner.secondCooldown = 10;
        FactionBanner.expired = TimeUnit.SECONDS.toMillis(FactionBanner.secondCooldown);
    }
}
