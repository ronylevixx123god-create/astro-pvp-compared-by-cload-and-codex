package me.ifamasssxd.cosmicfactionbanners.listeners;

import com.cosmicpvp.cosmicutils.nbt.NBTTagCompoundBuilder;
import com.google.common.collect.*;
import com.massivecraft.factions.perms.Relation;
import me.ifamasssxd.cosmicfactionbanners.*;
import com.massivecraft.factions.event.*;
import me.ifamasssxd.cosmicfactionbanners.struct.*;
import com.massivecraft.factions.iface.*;
import java.util.*;

import net.astropvp.core.killtracker.KillTrackerAPI;
import org.bukkit.plugin.*;
import me.ifamasssxd.cosmicfactionbanners.managers.*;
import org.bukkit.inventory.*;
import org.bukkit.event.player.*;
import org.bukkit.event.block.*;
import org.bukkit.event.*;
import com.cosmicpvp.cosmicutils.utils.*;
import org.bukkit.block.*;
import com.massivecraft.factions.*;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.meta.*;
import org.bukkit.entity.*;
import org.bukkit.*;
import org.bukkit.block.banner.*;

public class BannerListener implements Listener
{
    private BlockPlaceEvent waitingForMonitorCall;
    private List<String> bannerAllowedWorlds;
    private int BANNER_SOUL_COST;
    
    public BannerListener() {
        this.waitingForMonitorCall = null;
        this.bannerAllowedWorlds = (List<String>)Lists.newArrayList(new String[] { "world", "world_nether", "world_end", "world_arena", "flat" });
        this.BANNER_SOUL_COST = CosmicFactionBanners.get().getConfig().getInt("soulCost");
    }
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onFactionDisband(final FactionDisbandEvent event) {
        CosmicFactionBanners.get().getBannerManager().getBannerDesignMap().remove(event.getFaction().getId());
    }
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onFactionDisband(final FPlayerLeaveEvent event) {
        if (event.getFaction().getFPlayers().size() <= 1 && event.getFaction().getFPlayers().contains(event.getfPlayer())) {
            CosmicFactionBanners.get().getBannerManager().getBannerDesignMap().remove(event.getFaction().getId());
        }
    }
    
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = false)
    public void onBlockBreak(final BlockBreakEvent event) {
        final Block block = event.getBlock();
        final Material type = block.getType();
        if (type == Material.STANDING_BANNER || type == Material.WALL_BANNER) {
            for (final FactionBanner banner : CosmicFactionBanners.get().getBannerManager().getFactionBannerMap().values()) {
                final Location l = banner.getActiveLocation();
                if (l == null) {
                    continue;
                }
                if (l.getBlockX() != block.getX() || l.getBlockY() != block.getY()) {
                    continue;
                }
                if (l.getBlockZ() != block.getZ()) {
                    continue;
                }
                final FPlayer fp = (FPlayer)FPlayers.getInstance().getByPlayer(event.getPlayer());
                if (fp.getFaction().getRelationTo((RelationParticipator)banner.getFaction()).isAtLeast(Relation.ALLY)) {
                    event.setCancelled(true);
                    event.getPlayer().sendMessage(CC.RedB + "(!) " + CC.Red + "You cannot destroy " + banner.getWhoPlacedUsername() + "'s Faction Banner!");
                    event.getPlayer().sendMessage(CC.Gray + "It will despawn in: " + banner.getSecondsLeft() + "s!");
                    return;
                }
                event.setCancelled(true);
                banner.removeBanner();
            }
        }
    }
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerTeleport(final PlayerTeleportEvent e) {
        if (e.getPlayer().hasMetadata("bannerTp") && e.getCause() == PlayerTeleportEvent.TeleportCause.PLUGIN) {
            e.getPlayer().removeMetadata("bannerTp", (Plugin)CosmicFactionBanners.get());
        }
    }
    
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = false)
    public void onPlayerPlace(final BlockPlaceEvent event) {
        final ItemStack item = event.getItemInHand();
        if (item != null && item.getType() == BannerManager.bannerMaterial) {
            final NBTWrapper wrapper = new NBTWrapper(item);
            if (wrapper.getString("cosmicType").equals("FACTION_BANNER")) {
                event.setCancelled(true);
                this.waitingForMonitorCall = event;
            }
        }
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerInteract(final PlayerInteractEvent event) {
        final ItemStack item = event.getItem();
        if (item != null && item.getType() == BannerManager.bannerMaterial && new NBTWrapper(item).getString("cosmicType").equals("FACTION_BANNER")) {
            if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                final Block placingOn = event.getClickedBlock().getRelative(event.getBlockFace());
                if (placingOn.getType() != Material.AIR) {
                    event.getPlayer().sendMessage(CC.RedB + "(!) " + CC.Red + "You must place a Faction Banner in an valid location outside of spawn!");
                    event.setCancelled(true);
                    return;
                }
            }
            event.setUseItemInHand(Event.Result.ALLOW);
        }
    }
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = false)
    public void onPlayerPlaceMonitor(final BlockPlaceEvent event) {
        if (!event.isCancelled() || !event.equals(this.waitingForMonitorCall)) {
            return;
        }
        this.waitingForMonitorCall = null;
        final Faction fac = FactionUtils.getFaction(event.getPlayer(), false);
        if (!FactionUtils.isNormal(fac)) {
            event.getPlayer().sendMessage(CC.RedB + "(!) " + CC.Red + "You must be in a faction to place a Faction Banner!");
            return;
        }
        final Block placedOn = event.getBlockPlaced();
        if (!this.bannerAllowedWorlds.contains(placedOn.getWorld().getName())) {
            event.getPlayer().sendMessage(CC.RedB + "(!) " + CC.Red + "You cannot place Faction Banners in this world!");
            event.getPlayer().sendMessage(CC.Gray + "Allowed Worlds: [overworld, nether, end, battlefield]");
            return;
        }
        if (!WorldGuardUtils.isPvPEnabled(placedOn.getLocation())) {
            event.getPlayer().sendMessage(CC.RedB + "(!) " + CC.Red + "You can only place Faction Banners in PvP-enabled areas!");
            return;
        }
        final Location playerLoc = event.getPlayer().getLocation();
        if (playerLoc.getBlockX() != placedOn.getX() || playerLoc.getBlockZ() != placedOn.getZ() || Math.abs(playerLoc.getBlockY() - placedOn.getY()) > 1) {
            event.getPlayer().sendMessage(CC.RedB + "(!) " + CC.Red + "You must place faction banners directly underneath you.");
            return;
        }
        final BannerManager manager = CosmicFactionBanners.get().getBannerManager();
        FactionBanner banner = manager.getFactionBannerMap().get(fac.getId());
        if (banner != null && !banner.hasExpired()) {
            event.getPlayer().sendMessage(CC.RedB + "(!) " + CC.Red + "Your faction already has an active /f banner placed!");
            event.getPlayer().sendMessage(CC.Gray + "You can place a new /f banner in: " + banner.getSecondsLeft() + "s");
            return;
        }
        boolean freePlace = false;
        final NBTWrapper item = new NBTWrapper(event.getItemInHand());
        final NBTTagCompoundBuilder cosmicData = item.getconaxData();
        if (cosmicData != null && cosmicData.hasKey("noSoulCost")) {
            freePlace = true;
        }
        if (this.BANNER_SOUL_COST > 0 && !freePlace && !KillTrackerAPI.hasSoulAmount(event.getPlayer(), this.BANNER_SOUL_COST)) {
            event.getPlayer().sendMessage(CC.RedB + "(!) " + CC.Red + "You do not have enough souls to place this /f banner!");
            event.getPlayer().sendMessage(CC.RedB + "COST: " + CC.Gray + "500 souls");
            return;
        }
        final Material type = placedOn.getType();
        if ((type == Material.AIR || type == Material.STANDING_BANNER || type == Material.WALL_BANNER) && (placedOn.getRelative(BlockFace.UP).getType() == Material.AIR || placedOn.getY() == 255)) {
            if (this.BANNER_SOUL_COST > 0 && !freePlace) {
                KillTrackerAPI.removeSoulsFromGems(event.getPlayer(), this.BANNER_SOUL_COST);
                event.getPlayer().sendMessage(CC.RedB + " -" + this.BANNER_SOUL_COST + " souls");
            }
            event.getPlayer().sendMessage(CC.DarkPurpleB + "(!) " + CC.DarkPurple + "You have placed a Faction Banner!");
            event.getPlayer().sendMessage(CC.Gray + "Faction Members have " + FactionBanner.secondCooldown + "s to teleport to it using " + CC.LightPurple + "/f assist");
            banner = CosmicFactionBanners.get().getBannerManager().getFactionBannerMap().computeIfAbsent(fac.getId(), e -> new FactionBanner());
            banner.removeBanner();
            banner.placeBanner(fac, event.getPlayer(), placedOn.getLocation());
            event.setCancelled(false);
            return;
        }
        event.getPlayer().sendMessage(CC.RedB + "(!) " + CC.Red + "You must place your /f banner in an valid location!");
        event.setCancelled(true);
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerCraft(final CraftItemEvent event) {
        if (event.getRecipe().getResult().getType() == Material.BANNER && !event.getWhoClicked().isOp()) {
            final ItemStack banner = event.getCurrentItem();
            final BannerMeta bannerMeta = (BannerMeta)banner.getItemMeta();
            if (this.isBlacklistedBanner(bannerMeta) || banner.getDurability() == 0 || banner.getDurability() == 7 || banner.getDurability() == 8) {
                event.setCancelled(true);
                ((Player)event.getWhoClicked()).sendMessage(CC.RedB + "(!) " + CC.Red + "You cannot craft this style/color of banner!");
                ((Player)event.getWhoClicked()).sendMessage(CC.Gray + "Some banner styles/colors are limited edition and cannot be crafted.");
            }
        }
    }
    
    private boolean isBlacklistedBanner(final BannerMeta bannerMeta) {
        if (bannerMeta.getBaseColor() == DyeColor.BLACK || bannerMeta.getBaseColor() == DyeColor.GRAY || bannerMeta.getBaseColor() == DyeColor.SILVER) {
            return true;
        }
        for (final Pattern p : bannerMeta.getPatterns()) {
            if (p.getColor() == DyeColor.BLACK || p.getColor() == DyeColor.GRAY || p.getColor() == DyeColor.SILVER || p.getPattern() == PatternType.SKULL) {
                return true;
            }
        }
        return false;
    }
    
    public List<String> getBannerAllowedWorlds() {
        return this.bannerAllowedWorlds;
    }
}
